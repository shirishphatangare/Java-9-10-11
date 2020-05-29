package betterJava;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FilesImprovements {
	private static String path1 = "C:\\temp\\testFile1.txt";
	private static String path2 = "C:\\temp\\testFile2.txt";
	
	public static void main(String[] args) throws IOException{
		FilesImprovements xxx = new FilesImprovements();
		xxx.demoReadWriteFileAsString();
	}
	
	public void demoReadWriteFileAsString() throws IOException {
		// old way of reading a multi-line file in Java 8
		System.out.println("Old Java 8 way of reading a File");
		Files.lines(Paths.get(path1)).forEach(line -> System.out.println(line));
		// File reading New methods
		System.out.println("Improved Java 11 way of reading a File");
		System.out.println(Files.readString(Paths.get(path1)));

		// File writing New methods
		System.out.println("Improved Java 11 way of writing a File");
		Files.writeString(Paths.get(path2),"one\ntwo\nthree");
		System.out.println(Files.readString(Paths.get(path2)));
	}
	
	// Java 11 new File read/write methods - Files.readString(Path) and Files.writeString(Path,CharSequence..) - Read/Write files as a single string

}
