package streams.test_pkg_1;

import org.junit.Test;

import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static com.jayway.awaitility.Awaitility.await;
import static org.assertj.core.api.Java6Assertions.assertThat;

public class ColdPublisher {

  @Test
  // ColdPublisher - A publisher that processes data on demand - pairing with Back Pressure
  public void shouldUsePublisherToSendEvents() {
    //given
    AtomicInteger atomicInteger = new AtomicInteger();
    SubmissionPublisher<Integer> coldPublisher = new SubmissionPublisher<>();
    Flow.Subscriber<Integer> subscriber = new Flow.Subscriber<>() {
      @Override
      public void onSubscribe(Flow.Subscription subscription) {
        System.out.println("onSubscribe: " + subscription);
        subscription.request(1); //cold publisher can request as many events as needs right now.
        // Try - subscription.request(11);
        // Producer is not unbounded.
        // Here publisher is NOT producing events at an infinite rate, and initially subscription will limit it 1 event
      }

      @Override
      public void onNext(Integer item) {
        System.out.println("on next: " + item);
        atomicInteger.incrementAndGet();
        // We can request more events here once subscriber is ready for next batch of events
        
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

    Stream<Integer> finiteProducer = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9);

    //when
    coldPublisher.subscribe(subscriber);//chain subscriber with publisher
    finiteProducer.forEach(coldPublisher::submit);
    //subscriber will NOT be flooded with data, can say: "stop, don't send more!" with subscription.request() method
    // Upon return, close() method does NOT guarantee that all subscribers have yet completed. This is reason of using await() method below
    coldPublisher.close();

    //then 
    await().atMost(1000, TimeUnit.MILLISECONDS).until(
        () -> assertThat(atomicInteger.get()).isEqualTo(1)
    );
  }
}

