package streams.test_pkg_3;

import static com.jayway.awaitility.Awaitility.await;
import static org.assertj.core.api.Java6Assertions.assertThat;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import streams.basics.StockData;
import streams.basics.StocksPublisher;
import streams.transform.StockNameEndSubscriber;
import streams.transform.StockTransformProcessor;

public class StockTransformProcessorTest {

  @Test
  public void givenPublisher_whenSubscribeAndTransformElements_thenShouldConsumeAllElements() throws InterruptedException {
    //given
    StocksPublisher publisher = new StocksPublisher(); // First Publisher in the chain
    StockTransformProcessor<String> transformProcessor = new StockTransformProcessor<>(StockData::getName); // Generic processor (middle man) 
    StockNameEndSubscriber subscriber = new StockNameEndSubscriber(3); //  End subscriber in the chain - OK to have more limit of 3 here
    
    List<StockData> items = List.of(
        new StockData("APP", 123.4F),
        new StockData("GOO", 123.4F));
    
    List<String> expectedResult = List.of("APP", "GOO");

    //when
    publisher.subscribe(transformProcessor);
    transformProcessor.subscribe(subscriber);
    items.forEach(publisher::submit);
    publisher.close();

    //then
    await().atMost(1000, TimeUnit.MILLISECONDS).until(
        () -> assertThat(subscriber.consumedElements)
            .containsExactlyElementsOf(expectedResult)
    );
  }

}
