package streams.test_pkg_4;


import static com.jayway.awaitility.Awaitility.await;
import static org.assertj.core.api.Java6Assertions.assertThat;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import streams.basics.StockData;
import streams.basics.StockEndSubscriber;
import streams.basics.StocksPublisher;

public class SubscriptionObjectColdPublisherTest {

  @Test
  public void givenPublisher_whenSubscribe_thenShouldConsumeAllElements() throws InterruptedException {
    //given
    StocksPublisher publisher = new StocksPublisher(); // Cold Publisher (just 2 elements). No overproduction like Hot publisher
    StockEndSubscriber subscriber = new StockEndSubscriber(3); // consumption limit of 3. // No problem if subscriber limit is more than what produced by producer
    
    List<StockData> items = List.of(new StockData("APP", 123.4F), new StockData("GOO", 123.4F));
    List<StockData> expectedResult = List.of(new StockData("APP", 123.4F), new StockData("GOO", 123.4F));

    //when
    publisher.subscribe(subscriber);
    items.forEach(publisher::submit);
    publisher.close();

    //then
    await().atMost(1000, TimeUnit.MILLISECONDS).until(
        () -> assertThat(subscriber.consumedElements).containsExactlyElementsOf(expectedResult)
    );
  }

  @Test
  public void givenPublisher_whenSubscribe_thenShouldConsumeOneElement() throws InterruptedException {
    //given
    StocksPublisher publisher = new StocksPublisher();
    StockEndSubscriber subscriber = new StockEndSubscriber(1); // Since Subscriber consumes only one event and producer produces 2. ! extra event is buffered at producer. 
    // We can observer difference between 2 test cases. For givenPublisher_whenSubscribe_thenShouldConsumeAllElements test case publisher close method is called because there was no buffering of data
    // For givenPublisher_whenSubscribe_thenShouldConsumeOneElement test case publisher close method (onComplete() for subscriber) is not called because data is buffered at publisher. 
    
    List<StockData> items = List.of(new StockData("APP", 123.4F), new StockData("GOO", 123.4F));
    List<StockData> expectedResult = List.of(new StockData("APP", 123.4F));

    //when
    publisher.subscribe(subscriber);
    items.forEach(publisher::submit);
    publisher.close();

    //then
    await().atMost(1000, TimeUnit.MILLISECONDS).until(
        () -> assertThat(subscriber.consumedElements).containsExactlyElementsOf(expectedResult)
    );
  }
}
