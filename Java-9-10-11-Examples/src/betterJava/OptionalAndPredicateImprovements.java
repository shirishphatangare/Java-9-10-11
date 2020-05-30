package betterJava;

import static java.util.function.Predicate.not;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// Optional class getting richer and powerful with every Java release
public class OptionalAndPredicateImprovements {

    private final Cat fifie = new Cat("Fifie", "Sleeps on the heater in winter");

    private final List<Cat> cats = List.of(
            new Cat("Ella", "Loves height, but doesn't like jumping"),
            new Cat("Jelly", "Steals croissants, but doesn't eat them, just to play with them"),
            new Cat("Eclair", "Super lazy, sleeps 23 hours a day"),
            fifie);


    public static void main(String[] args) {
        var app = new OptionalAndPredicateImprovements();
        app.demoStream();
        app.demoIfPresentOrElse();
        app.demoOr();
        app.demoOrElseThrow();
        app.demoIsEmpty();
        app.demoNotPredicate();
    }

    // Optional.or() - Use another Optional when initial Optional value is absent
    public void demoOr() {
    	System.out.println("\n\nOptional.or():");
    	var cat = findCatByFunFact("croissant1")
    			  .or(() -> findCatByFunFact("lazy"));
    	System.out.println(cat);
    }

    
    // Optional.ifPresentOrElse() - Execute first argument of Optional.ifPresentOrElse() if element is present in an Optional, otherwise execute second argument 
    public void demoIfPresentOrElse() {
    	System.out.println("\n\nOptional.ifPresentOrElse():");
    	findCatByFunFact("croissant")
    		.ifPresentOrElse(cat -> System.out.println(cat),
    						() -> System.out.println("No Cat Found"));
    }
    
    
    // Optional.OrElseThrow() - Throws a NoSuchElementException in case of an empty Optional 
    public void demoOrElseThrow() {
    	System.out.println("\n\nOptional.orElseThrow():");
    	var cat = findCatByFunFact("croissant")
    			.orElseThrow();
    	// .orElseThrow(IllegalStateException::new); // A custom exception can be passed as an argument
    	
    	System.out.println(cat);
    }

    // Optional.stream() - Converts an Optional into a Stream with a single element if an element is present or else an empty Stream. 
    public void demoStream() {
    	System.out.println("\n\nOptional.stream():");
    	var castForDescriptions = Stream.of("coconut cake", "croissant", "jump")
    							  .map(desc -> findCatByFunFact(desc))
    							  .flatMap(Optional::stream)
    							  .collect(Collectors.toList());
    	System.out.println(castForDescriptions);
    }

    // Optional.isEmpty() returns true if value is absent in an Optional ( i.e. Optional is empty)
    public void demoIsEmpty() {
    	System.out.println("\n\nOptional.isEmpty():");
    	if(findCatByFunFact("cake1").isEmpty()) {
    		System.out.println("No Meow this time, try again later");
    	}
    }

    // Predicate.not() to negate a predicate
    public void demoNotPredicate() {
    	System.out.println("\n\nPredicate.not():");
    	var catsThatAreNotElla = cats.stream()
    							//.filter(hasName("Ella").negate())  // Pre-Java-11
    							.filter(not(hasName("Ella")))
    							.collect(Collectors.toList());
    	System.out.println(catsThatAreNotElla);
    }


    public Optional<Cat> findCatByFunFact(String description) {
        return cats.stream()
                .filter(cat -> cat.funFact.contains(description)).findFirst(); // Returns an Optional describing the first element of this stream, or an empty Optional if the stream is empty.
    }

    public Optional<Cat> getMyFavoriteCat() {
        return Optional.of(fifie);
    }

    public Optional<String> getRandomFactAboutCats() {
        return Optional.of("Can jump as much as 7 times its height");
    }

    public static class Cat {
        private final String name;
        private final String funFact;

        public Cat(String name, String funFact) {
            this.name = name;
            this.funFact = funFact;
        }

        public String getName() {
            return name;
        }

        public String getFunFact() {
            return funFact;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Cat cat = (Cat) o;
            return Objects.equals(name, cat.name) &&
                    Objects.equals(funFact, cat.funFact);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, funFact);
        }

        @Override
        public String toString() {
            return "\uD83D\uDC08 " + name + '\'' +
                    ": " + funFact + '\n';
        }

    }

    public static Predicate<Cat> hasName(String name) {
        return cat -> cat.name.equals(name);
    }

}

// Summary

//Optional

// Java 9 - Optional.or(), Optional.ifPresentOrElse() and Optional.stream()
// Java 10 - Optional.orElseThrow()
// Java 11 - Optional.isEmpty()


// Predicate
//Java 11 - Predicate.not()


