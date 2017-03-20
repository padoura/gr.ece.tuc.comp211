package gr.ece.tuc.comp211;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class MainController {
	
	

	public static void main(String[] args) throws IOException{
		int bufferSize = 512; // Give the buffer size here
		int numberOfInt = 78125*128; // Give the total number of integers to save in file, ordered from 1 to numberOfInt. 78125*128 = 10^7 integers, 78125 disk pages
		String filename = "testfile.bin"; // Give the filename here
		int numberOfQueries = 10000; // Give how many integers will be searched
		int[] queueSize = {1, 50, 100}; // Give the size of the queue in main memory (number of buffers)
		
		
		PrintStream log = new PrintStream(new FileOutputStream("logfile.txt"));
		System.setOut(log); // Log file where every searched integer and its position is printed.

		
		
		// Subclass of RandomAccessFile. Contains all the writing and searching operations.
		FileHandler file = new FileHandler(filename, "rw");
		// Creates the specified file
		file.create(bufferSize, numberOfInt);
		file.close();
		
		file = new FileHandler(filename, "r");

		System.out.println("Counting average number of disk accesses...");
		// Searches random integers in a file by accessing diskpages sequentially (Question A)
		System.out.println("Sequential search: " + file.searchSequential(numberOfQueries, bufferSize, numberOfInt));
		// Searches random integers in a file by using Binary search (on diskpages) (Question B)
		System.out.println("Binary search: " + file.searchBinary(numberOfQueries, bufferSize, numberOfInt));
		// Searches random integers in a file by using Binary search (on diskpages) and by ordering queries (Question C)
		System.out.println("Binary search with ordered queries: " + file.searchOrderedQueries(numberOfQueries, bufferSize, numberOfInt));
		// Searches random integers in a file by using Binary search (on diskpages) and retaining a queue of diskpages in main memory  (Question D)
		for (int i=0;i<queueSize.length;i++)
			System.out.println("Binary search with a queue in memory (size = " + queueSize[i] + "): "  + file.searchMemoryQueue(numberOfQueries, bufferSize, numberOfInt, queueSize[i]));
		file.close();
		log.close();
		
		File oldFile = new File(filename);
		oldFile.delete();
		

	}

}
