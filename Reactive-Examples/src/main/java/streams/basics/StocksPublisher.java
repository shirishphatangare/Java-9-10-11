package streams.basics;

import java.util.concurrent.SubmissionPublisher;


// If we would have to implement Publisher here, then we would have to handle everything related to subscriber.
// Create list of all subscribers
// Add each subscriber to the List
// When event arrives, iterate over subscribers and send event to each subscriber
// This is very cumbersome and error-prone. That is why we use SubmissionPublisher as below for convenience.
public class StocksPublisher extends SubmissionPublisher<StockData> {


}
