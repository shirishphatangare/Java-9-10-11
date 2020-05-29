package streams.transform;

import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.function.Function;

import streams.basics.StockData;

public class StockTransformProcessor<R> extends SubmissionPublisher<R> implements Flow.Processor<StockData, R> {
//public class StockTransformProcessor<R> extends SubmissionPublisher<R> implements Flow.Subscriber<StockData> { // Can this be done?
	
    private Function<StockData, R> function; // Transformation function takes StockData as an input and outputs R
    private Flow.Subscription subscription;

    public StockTransformProcessor(Function<StockData, R> function) {
        super();
        this.function = function;
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        subscription.request(1);
    }

    @Override
    public void onNext(StockData item) {
        submit(function.apply(item)); // StockTransformProcessor acting as a Publisher - Transform and submit
        subscription.request(1); // StockTransformProcessor acting as a Subscriber
    }

    @Override
    public void onError(Throwable t) {
        t.printStackTrace();
    }

    @Override
    public void onComplete() {
        close(); // When Processor gets onComplete() callback from first publisher in the chain, close itself too so that subscribers in the chain gets onComplete() callback
    }
}  