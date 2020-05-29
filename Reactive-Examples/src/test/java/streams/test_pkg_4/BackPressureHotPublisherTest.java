package streams.test_pkg_4;


import static com.jayway.awaitility.Awaitility.await;
import static org.assertj.core.api.Java6Assertions.assertThat;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

import streams.backpressure.OverProducingPublisher;
import streams.basics.StockEndSubscriber;

public class BackPressureHotPublisherTest {

  @Test
  public void whenNotUsingBackPressureSubscriberWillBeFloodedWithData() throws InterruptedException {
    //given
    OverProducingPublisher publisher = new OverProducingPublisher(); // OverProducingPublisher (Hot Publisher)
    StockEndSubscriber subscriber = StockEndSubscriber.createUnbounded(); // Not using BackPressure i.e. unbounded. Subscriber is overwhelmed with events and ultimately OutOfMemoryException

    //when
    publisher.subscribe(subscriber);
    
    new Thread(() -> {
      publisher.start();
      publisher.close();
    }).start();

    //then
    await().atMost(1000, TimeUnit.MILLISECONDS).until(
        () -> assertThat(subscriber.consumedElements.size()).isGreaterThan(100) 
        //if gt 100 then subscriber will be flooded with data and test succeeds
        // Our test ends with just 100 events, In real time application we will get OutOfMemoryException for such a case
    );
  }


  @Test
  public void shouldApplyBackPressureOnOverProducingPublisher() throws InterruptedException {
    //given
    OverProducingPublisher publisher = new OverProducingPublisher(); // OverProducingPublisher (Hot Publisher)
    StockEndSubscriber subscriber = new StockEndSubscriber(3); // Using BackPressure with a consumption limit of 3. Subscriber is NOT overwhelmed with events. Events are buffered at producer till subscriber is ready for next batch of events.

    //when
    publisher.subscribe(subscriber);
    new Thread(() -> {
      publisher.start();
      publisher.close();
    }).start();

    //then
    await().atMost(1000, TimeUnit.MILLISECONDS).until(
        () -> assertThat(subscriber.consumedElements.size()).isEqualTo(3) // No possibility of OutOfMemoryException with BackPressure for Hot Publisher
    );
  }

}
