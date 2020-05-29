package streams.test_pkg_2;

import static com.jayway.awaitility.Awaitility.await;
import static org.assertj.core.api.Java6Assertions.assertThat;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import streams.basics.StockData;
import streams.basics.StockEndSubscriber;
import streams.basics.StocksPublisher;

public class StockReactiveFlowTest {
  @Test
  public void givenPublisher_whenSubscribeToIt_thenShouldConsumeAllElements() throws InterruptedException {
    //given
    StocksPublisher stocksPublisher = new StocksPublisher();
    StockEndSubscriber subscriber =
        new StockEndSubscriber(
            2,
            true);
    stocksPublisher.subscribe(subscriber);
    List<StockData> items = List
        .of(new StockData("APP", 123.4F),
            new StockData("GOO", 123.4F));

    //when
    assertThat(stocksPublisher.getNumberOfSubscribers()).isEqualTo(1);
    items.forEach(stocksPublisher::submit);
    stocksPublisher.close();

    //then

    await().atMost(1000, TimeUnit.MILLISECONDS).until(
        () -> assertThat(subscriber.consumedElements)
            .containsExactlyElementsOf(items)
    );
  }

}
