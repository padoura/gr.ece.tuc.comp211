package gr.ece.tuc.comp211;

import java.io.IOException;

public class MainController {
	
	

	public static void main(String[] args) throws IOException{
		int bufferSize = 512; // Give the buffer size here
		int numberOfInt = 78125*128; // Give the total number of integers to save in file, ordered from 1 to numberOfInt. 78125*128 = 10^7 integers, 78125 diskpages
		String filename = "testfile1"; // Give the filename here
		int numberOfQueries = 10000; // Give how many integers will be searched
		
		
		
		// Subclass of RandomAccessFile. Contains all the writing and searching operations.
		FileHandler file = new FileHandler(filename, "rw");
		// Creates the specified file
		file.create(bufferSize, numberOfInt);
		file.close();
		
		file = new FileHandler(filename, "r");
		// Simple test of the create and readPage methods of class FileHandler
		FileHandlerTest.testCreate(file, bufferSize, numberOfInt);

		System.out.println("Counting average number of disk accesses...");
		// Searches random integers in a file by accessing diskpages sequentially (Question A)
		System.out.println("Sequential search: " + file.searchSequential(numberOfQueries, bufferSize, numberOfInt));
		
		// Searches random integers in a file by using Binary search (on diskpages) (Question B)
		System.out.println("Binary search: " + file.searchBinary(numberOfQueries, bufferSize));
		// Searches random integers in a file by using Binary search (on diskpages) and by ordering queries (Question C)
		System.out.println("Binary search with ordered queries: " + file.searchOrderedQueries(numberOfQueries, bufferSize));
		// Searches random integers in a file by using Binary search (on diskpages) and retaining a queue of diskpages in main memory  (Question D)
		System.out.println("Binary search with buffer queue: " + file.searchMemoryQueue(numberOfQueries, bufferSize));
		file.close();
		

	}

}
