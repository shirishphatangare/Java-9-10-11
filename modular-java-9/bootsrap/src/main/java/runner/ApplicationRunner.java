package runner;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.json.simple.parser.ParseException;

import car.ModularCar;
import carprocessor.ModularCarProcessor;
import csv.ModularCSVWriter;
import json.ModularJSONReader;

public class ApplicationRunner {
    public static void main(String args[]) throws IOException, ParseException {
    	System.out.println("Initiaiting ApplicationRunner..");
        
    	//Using getResource() method as on below line do not work in case of a jar file
    	//because cars.json file is not found even though it is present in a jar file
    	//When the resource files are packaged inside the JAR and so we need a different way of accessing them.
    	//URL file_path = ApplicationRunner.class.getClassLoader().getResource("cars.json");
    	
    	// ClassLoader.getResourceAsStream() looks at the classpath for the given resource.
    	// The leading slash on the filename to getResourceAsStream() tells the loader to read from the base of the classpath
    	InputStream inputStream = ApplicationRunner.class.getResourceAsStream("/cars.json");

        ModularJSONReader jsonReader = new ModularJSONReader(inputStream);
        List<ModularCar> cars = jsonReader.parseFile();

        ModularCarProcessor modularCarProcessor = new ModularCarProcessor(cars);
        List<ModularCar> fileredList = modularCarProcessor.processList("Ford", "Mercedes");

        ModularCSVWriter csvWriter = new ModularCSVWriter(new File("cars.csv").getAbsolutePath());
        csvWriter.writeToCSV(fileredList);
        System.out.println("Terminating ApplicationRunner..");
    }
}

/*Notes: Let's instead use resource loading to load resources from the classpath instead of a specific file location. This will work regardless of how the code is packaged:

// SUCCESS case:
 
	try (InputStream inputStream = getClass().getResourceAsStream("/input.txt");
	    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
	    String contents = reader.lines()
	      .collect(Collectors.joining(System.lineSeparator()));
	}
	
	ClassLoader.getResourceAsStream() looks at the classpath for the given resource. The leading slash on the input to getResourceAsStream() tells the loader to read from the base of the classpath. The contents of our JAR file are on the classpath, so this method works.
	An IDE typically includes src/main/resources on its classpath and, thus, finds the files. */


/*FAILURE Case:
	
	try (FileReader fileReader = new FileReader("src/main/resources/input.txt"); 
		     BufferedReader reader = new BufferedReader(fileReader)) {
		    String contents = reader.lines()
		      .collect(Collectors.joining(System.lineSeparator()));
		}
*/
