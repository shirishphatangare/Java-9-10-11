package streams.test_pkg_1;

import org.junit.Ignore;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.jayway.awaitility.Awaitility.await;
import static org.assertj.core.api.Java6Assertions.assertThat;

public class HotPublisher {

  //@Ignore(value = "it is unbounded producer")
  @Test
  
  // HotPublisher - It is hard to take control over a hot publisher
  public void shouldUsePublisherToSendEvents() {
    //given
    AtomicInteger atomicInteger = new AtomicInteger();
    SubmissionPublisher<Integer> hotPublisher = new SubmissionPublisher<>();
    Flow.Subscriber<Integer> subscriber = new Flow.Subscriber<>() {
      @Override
      public void onSubscribe(Flow.Subscription subscription) {
        System.out.println("onSubscribe: " + subscription);
        subscription.request(Integer.MAX_VALUE); // unbounded publisher
        // Try - subscription.request(1000); // Limiting events to 1000
      }

      @Override
      public void onNext(Integer item) {
        System.out.println("on next: " + item);
        // onNext callback can contain complex event processing logic like send events to Third party systems or making some REST calls etc.
        atomicInteger.incrementAndGet();
      }

      @Override
      public void onError(Throwable throwable) {
        System.out.println("on error: " + throwable.getMessage());
      }

      @Override
      public void onComplete() {
        System.out.println("on complete");
      }
    };

    Stream<Integer> infiniteProducer = Stream.iterate(0, i -> i + 2); 

    //when
    hotPublisher.subscribe(subscriber);//chain subscriber with publisher
    infiniteProducer.forEach(hotPublisher::submit);
    // subscriber will be flooded with data, can't say: "stop, don't send more!" because subscription is defined to receive Integer.MAX_VALUE events
    // publisher has no information on how to limit sending this infinite stream of data that is why it is called as hot publisher
    // It is hard to take control over a hot publisher
    // In such a case, since publisher is producing infinite events, events are buffered at the producer side after Integer.MAX_VALUE or 1000 limit is reached 
    // and ultimately OutofMemoryException is thrown
    hotPublisher.close();

    //then 
    // Here we do not get test result because current thread is in infinite loop for Stream.iterate() logic above. 
    // If we want test result in such case, one solution is to create publisher infinite loop in another thread.
    await().atMost(10_000, TimeUnit.MILLISECONDS).until(
        () -> assertThat(atomicInteger.get()).isEqualTo(1000)
    );
  }
}
