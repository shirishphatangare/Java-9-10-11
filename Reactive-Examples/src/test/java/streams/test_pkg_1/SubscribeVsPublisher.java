package streams.test_pkg_1;

import org.junit.Test;

import java.util.List;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static com.jayway.awaitility.Awaitility.await;
import static org.assertj.core.api.Java6Assertions.assertThat;

public class SubscribeVsPublisher {

  @Test
  public void shouldUsePublisherToSendEvents() throws InterruptedException {
    // given
    AtomicInteger atomicInteger = new AtomicInteger();
    SubmissionPublisher<String> publisher = new SubmissionPublisher<>();
    Flow.Subscriber<String> subscriber = new Flow.Subscriber<>() { // Anonymous class
      
      // Initial call-back when a subscriber is registered with a publisher to define a subscription (contract) between them
      @Override
      public void onSubscribe(Flow.Subscription subscription) {
        System.out.println("onSubscribe: " + subscription);
        subscription.request(Integer.MAX_VALUE); // unbounded request. We do not care about backpressure here 
      }
      
      // When a new item is pushed by publisher, subscriber will receive it with onNext call-back
      @Override
      public void onNext(String item) {
        System.out.println("on next: " + item);
        atomicInteger.incrementAndGet(); // AtomicInteger is used for thread safety
      }
      
      // When some error condition is triggered, onError() call-back is called with reason of error in Throwable object
      @Override
      public void onError(Throwable throwable) {
        System.out.println("on error: " + throwable.getMessage());
      }
      
      // When subscriber is done with message/event consumption. Used for graceful shutdown of resources
      @Override
      public void onComplete() {
        System.out.println("on complete");
      }
    };

    List<String> items = List.of("item-1", "item-2");

    // when
    publisher.subscribe(subscriber);  // chain subscriber with publisher
    items.forEach(publisher::submit); // publisher submits each item to the subscriber
    publisher.close(); // Signals all subscribers that this flow is closed and triggers onComplete callback of subscribers

    // then
    // Await (maximum for 20 secs) until a java.lang.Runnable supplier execution passes (ends without throwing an exception). 
    await().atMost(20000, TimeUnit.MILLISECONDS).until(
        () -> assertThat(atomicInteger.get()).isEqualTo(2)
    );
  }
}
