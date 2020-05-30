package betterJava;

import java.util.Arrays;
import java.util.List;

public class CollectionImprovements {

	public static void main(String[] args) {
		CollectionImprovements xxx = new CollectionImprovements();
		xxx.demoList();
	}
	
	public void demoList() {
		// Pre-Java9 List
		var preJava9Dogs = Arrays.asList("Caesar","Snoopy","Brody");
		//preJava9Dogs.add("Cherry"); // Adding an element to the underlying array: fails at runtime with UnsupportedOperationException
		preJava9Dogs.set(1,"Snoopy Dog"); // Success. Modifiable List
		System.out.println(preJava9Dogs);
		
		// Java9 - List.of() function create immutable List. Map, Optional and Stream also has .of() method
		var java9Dogs = List.of("Caesar","Snoopy","Brody");
		//java9Dogs.add("Cherry"); // Unmodifiable(immutable) List; fails at runtime with UnsupportedOperationException
		java9Dogs.set(1,"Snoopy Dog"); // Unmodifiable(immutable) List; fails at runtime with UnsupportedOperationException
		
		// Java 10 - List.copyOf() - Mutable to immutable List.
		var copyOfList = List.copyOf(preJava9Dogs);
		copyOfList.add("Cherry"); // Unmodifiable(immutable) List
		copyOfList.set(1,"Snoopy Dog"); // Unmodifiable(immutable) List
		preJava9Dogs.set(1,"Snoooopy"); // Source List is still mutable
		
		System.out.println(preJava9Dogs);
		System.out.println(copyOfList);
		
	}
	
	// Java 9 - List - of() - Create immutable List
	// Java 9 - Map - of() and ofEntries() + entry() - 2 ways to create immutable Map
	
	// Java 10 - List - copyOf() - Copy mutable List to immutable List
	// Java 10 - Map - copyOf() - Copy mutable Map to immutable Map


}
