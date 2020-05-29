package betterJava;

/* Many frequently-used methods on String or Files were not 
 * implemented as a part of the core API, and developers had to implement them themselves or use a third-party library. 
 * Now, these methods became a part of Java and the community.
 */

public class StringImprovements {

	public static void main(String[] args) {
		StringImprovements xxx = new StringImprovements();
		xxx.demoOperationsWithWhitespaces();
		xxx.demoLinesAndRepeat();

	}
	
	public void demoOperationsWithWhitespaces() {
		System.out.println("Demo: Operations with whitespaces in Java 11.");
		String str = "	Eclair is a very fluffy Persian cat.\u2003" ; // Trailing space is unicode '\u2003' (em space) // Could not print actual em space here! prints '?' but logic working
		System.out.printf("str.trim(): '%s'\n", str.trim()); // trim() function removes only space character whose codepoint is less than or equal to 'U+0020' (space character)
		// Apart from '\u2003' there are many other white space characters which are not handled by the trim() method.
		
		// Solution to above problem is to use strip(), stripLeading() and stripTrailing() String Functions introduced in Java 11
		
		System.out.printf("str.strip(): '%s'\n",str.strip());
		System.out.printf("str.stripLeading(): '%s'\n",str.stripLeading());
		System.out.printf("str.stripTrailing(): '%s'\n",str.stripTrailing());
		
		// Result of using new String isBlank() method
		String str1 = ""; // true
		String str2 = "not blank"; //false
		String str3 = "     "; // true
		String str4 = "\t	\t\u2003"; // true 
	}
	
	public void demoLinesAndRepeat() {
		System.out.println();
		System.out.println("Demo: String.repeat()");
		System.out.println("\uD83D\uDC08".repeat(10));
		
		// Example new lines() method
		String str = "	Eclair is a very fluffy Persian cat.\n Give her coconut cake . \r\nPlease..";
		// New lines() method to split single string into multiple lines using newline characters as delimiters
		// Returns a stream of lines extracted from this string,separated by line terminators. 
		str.lines()
				.map(String::strip)
				.map(line -> (line.contains("Please")) ? "\uD83D\uDE3B".repeat(5) + line : line )
				.forEach(line -> System.out.println(line));
		System.out.println();
	}
	
	// Java 9 - chars():IntStream()
	// Java 11 - strip() stripLeading() stripTrailing() isBlank() repeat(count) methods
	
}
