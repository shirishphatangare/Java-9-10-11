package betterJava;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class LocalVarTypeInference {

	public void printCatNames() {
		// A) Local variables
		
		//String cat = "Jack";
		var cat = "Jack";
		
		int i,j=0; // allowed
		int ii=0,jj=0; // allowed
		//var iii,jjj=0; // ii is not initialized so var can not be used
		//var iiii=0,jjjj=0; // 'var' is not allowed in a compound declaration
		
		int index1 = 5;
		var index2 = 5;
		
		int [] array1 = new int[index1];
		int [] array2 = new int[index2];
		//'var' is not allowed as an element type of an array
		//var [] array3 = new int[index1]; // not allowed 
		//var [] array4 = new int[index2]; //  not allowed
		
		//List<String> catNames = List.of("Ella","Jelly","Eclair");
		var catNames = List.of("Ella","Jelly","Eclair");
		
		//var x = List.of("Ella","Jelly","Eclair"); // Allowed but not a good practice. Use descriptive variable names
		
		// Not a good practice
		var cats = new ArrayList<>();
		cats.add("jelly");
		cats.add(true);
		cats.add(123);

		// Good practice
		var cats1 = new ArrayList<String>();
		cats1.add("jelly");
		//cats2.add(true); // Not allowed
		//cats3.add(123);  // Not allowed
		
		
		/*Map<String,List<String>> catsWithDescription = Map.of(
				"Micky", List.of("Super-fluffy", "Sleeps all day long."),
				"Lilly", List.of("Black Bombay Cat", "Playful,fast,agile")
		);*/
		
		var catsWithDescription = Map.of(
				"Micky", List.of("Super-fluffy", "Sleeps all day long."),
				"Lilly", List.of("Black Bombay Cat", "Playful,fast,agile")
		);
		
		// B) In a for loop
		
		/*for(int i=0;i<10;i++) {
			System.out.println(i);
		}*/
		
		for(var k=0;k<10;k++) {
			System.out.println(k);
		}
		
		// C) In a try-with-resources 
		
		/*
		 * try(Stream<String> lines = Files.lines(Paths.get("path1","path2"))) {
		 * lines.forEach(line -> System.out.println(line)); } catch(IOException e) {
		 * e.printStackTrace(); }
		 */
		

		try(var lines = Files.lines(Paths.get("path1","path2"))) {
			lines.forEach(line -> System.out.println(line));
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		final var bestCat = "Ella"; // works
		//bestCat = "ddd"; // final local var can not be reassigned 
		
		var anotherCat = "Jelly";
		anotherCat = "Bashy";
		// anotherCat = 153; // var type can not be changed like this. Java is statically and strongly typed language
		
		//var getThisCompiler; // Cannot use 'var' on variable without initializer
		
		//var getThisCompiler = null; // Cannot infer type for local variable initialized to 'null'
		
		var var = "Teddy"; // It works. var is not a new keyword. It is an identifier (reserved type name)
		
		var List = "list"; // Works
		
		var Map = "map"; // Works
		
		// var final = "fff"; // Do not work! final is a keyword

	}
	
	// D) Method signature
	/*public void getFavoriteFood(String cat) {
	}*/
	
	/*public void getFavoriteFood(var cat) { // var not allowed here !
	}*/
	
	
	// E) Class Fields
	//public String jelly = "jelly";
	
	//public var jelly = "jelly"; // var not allowed here !
	
	
	public static void coolThingsWithVar() {
		
		// F) With anonymous class 1
		Object ella = new Object() {
			String name = "Ellas";
			String description = "Fluffy";
		};
		
		//System.out.println(ella.name); // can not access anonymous class field like this
		
		var ella1 = new Object() {
			String name = "Ellas";
			String description = "Fluffy";
		};
		
		System.out.println(ella1.name); // with var we can access anonymous class field
		
		// G) With anonymous class 2
		var catsWithDescription = Map.of(
				"Micky", List.of("Super-fluffy", "Sleeps all day long."),
				"Lilly", List.of("Black Bombay Cat", "Playful,fast,agile")
		);
		
		/*List<Object> catObjects = catsWithDescription.entrySet().stream()
				.map(cat -> new Object() {
					String name = cat.getKey();
					List<String> description = cat.getValue();
				}).collect(Collectors.toList());*/
		
		var catObjects = catsWithDescription.entrySet().stream()
				.map(cat -> new Object() {
					String name = cat.getKey();
					List<String> description = cat.getValue();
				}).collect(Collectors.toList());
		
		catObjects.forEach(cat -> System.out.println(cat.name + ": " + cat.description));
		
		// H) var in Lambda parameters - Since Java 11
		AddressOperation toSingleLine1 = (String line1, int aptNumber, int zip) -> line1 + ", apt." + aptNumber + " " + zip; // VALID
		AddressOperation toSingleLine2 = (line1, aptNumber, zip) -> line1 + ", apt." + aptNumber + " " + zip;   // VALID
		
		// If var is used in an implicitly typed Lambda expression, it must be used for all formal parameters
		AddressOperation toSingleLine3 = (var line1, var aptNumber, var zip) -> line1 + ", apt." + aptNumber + " " + zip;  // VALID
		//AddressOperation toSingleLine4 = (@Nullable var line1, var aptNumber, @ZipCode var zip) -> line1 + ", apt." + aptNumber + " " + zip;  // var allows annotations to be used directly on lambda params
		
	}
	
	
	public static void main(String[] args) {

	}
	
	@FunctionalInterface
	interface AddressOperation {
		public String xxx (String line1, int aptNumber, int zip);
	}

}
