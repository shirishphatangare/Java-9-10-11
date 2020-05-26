package json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import car.ModularCar;

public class ModularJSONReader {
    //private final String targetFilePath;
	private InputStream inputStream ;

//    public ModularJSONReader(String targetFilePath) {
//        this.targetFilePath = targetFilePath;
//    }
	
	  public ModularJSONReader(InputStream inputStream) {
		  this.inputStream = inputStream;
	  }

    public List<ModularCar> parseFile() throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        //JSONObject json = (JSONObject) parser.parse(new FileReader(targetFilePath));
        JSONObject json = (JSONObject) parser.parse(new BufferedReader(new InputStreamReader(inputStream)));
        JSONArray cars = (JSONArray) json.get("cars");

        List<JSONObject> carList = (List<JSONObject>) cars.stream().collect(Collectors.toList());

        return carList.stream()
            .map(x -> new ModularCar((String) x.get("make"), (String) x.get("model"), (String) x.get("colour"), (Double) x.get("engine_size")))
            .collect(Collectors.toList());
    }
}
