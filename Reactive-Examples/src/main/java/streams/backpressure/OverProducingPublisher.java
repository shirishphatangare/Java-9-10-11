package streams.backpressure;

import java.util.UUID;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import streams.basics.StockData;


// Over Producing Publisher but not infinite producer
public class OverProducingPublisher extends SubmissionPublisher<StockData> {

  public void start() {
    Stream<StockData> stockDataStream = Stream
        .generate(() ->
            new StockData(
                UUID.randomUUID().toString(),
                ThreadLocalRandom.current().nextFloat()
            )
        );


    //stockDataStream.limit(100_000).forEach(this::submit);
    stockDataStream.limit(1000).forEach(this::submit);
  }


}
