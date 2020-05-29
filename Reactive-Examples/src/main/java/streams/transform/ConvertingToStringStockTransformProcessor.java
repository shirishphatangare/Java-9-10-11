package streams.transform;

import java.util.function.Function;

import streams.basics.StockData;


// Creating a specific transformer ConvertingToStringStockTransformProcessor for (StockData --> String) from a generic transformer StockTransformProcessor<R> (StockData --> R) 
public class ConvertingToStringStockTransformProcessor extends StockTransformProcessor<String> {
	
  public static ConvertingToStringStockTransformProcessor create() {
    return new ConvertingToStringStockTransformProcessor(
        StockData::getName);
  }

  private ConvertingToStringStockTransformProcessor(
      Function<StockData, String> function) {
    super(function);
  }

  public static void main(String[] args) {
	//Specific transformer created here can be used for (StockData --> String) conversion 
    ConvertingToStringStockTransformProcessor transformer = ConvertingToStringStockTransformProcessor.create();

  }
}
