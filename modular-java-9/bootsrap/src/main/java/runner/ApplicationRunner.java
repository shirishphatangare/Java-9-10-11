package runner;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.json.simple.parser.ParseException;

import car.ModularCar;
import carprocessor.ModularCarProcessor;
import csv.ModularCSVWriter;
import json.ModularJSONReader;

public class ApplicationRunner {
    public static void main(String args[]) throws IOException, ParseException {
    	System.out.println("Initiaiting ApplicationRunner..");
        URL file_path = ApplicationRunner.class.getClassLoader().getResource("cars.json");

        ModularJSONReader jsonReader = new ModularJSONReader(file_path.getPath());
        List<ModularCar> cars = jsonReader.parseFile();

        ModularCarProcessor modularCarProcessor = new ModularCarProcessor(cars);
        List<ModularCar> fileredList = modularCarProcessor.processList("Ford", "Mercedes");

        ModularCSVWriter csvWriter = new ModularCSVWriter(new File("cars.csv").getAbsolutePath());
        csvWriter.writeToCSV(fileredList);
        System.out.println("Terminating ApplicationRunner..");
    }
}

