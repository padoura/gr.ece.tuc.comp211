package gr.ece.tuc.comp211;


/*
* This code is free software; you can redistribute it and/or modify it
* under the terms of the GNU General Public License version as
* published by the Free Software Foundation, either version 3 of the License, 
* or (at your option) any later version.
*
* This code is distributed in the hope that it will be useful, but WITHOUT
* ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
* FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
* version 3 for more details.
*
* You should have received a copy of the GNU General Public License version
* 3 along with this work; if not, see <http://www.gnu.org/licenses/>.
*
* Please contact Michail Pantourakis via Github repository 
* https://github.com/padoura/gr.ece.tuc.comp211 if you need additional 
* information or have any questions.
*/



import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/**
* This class consists the main launching file of Exercise 1 of 
* the course "Data Structures and Files", Spring Semester 2017, ECE, 
* Technical University of Crete. This is an introductory exercise to basic 
* searching methods on disks and their complexity. 10^7 integers, 
* ordered from 1 to 10^7, are stored in a file, assuming a page size of 512 bytes. 
* This file is subsequently used to compare their average disk accesses of four 
* methods that search for 10000 random integers: 
* 1) sequential search (first come, first served basis), 
* 2) binary search (first come, first served basis), 
* 3) binary search on sorted and batch examined keys, 
* 4) binary search with "page-caching" using a queue.
*
*
* @author	Michail Pantourakis
* @see		FileHandler
* @since	1.8
*/


public class MainController {
	
	

	public static void main(String[] args) throws IOException{
		int bufferSize = Integer.BYTES*128; // Give the buffer size as a number of integers here
		int numberOfInt = 78125*128; // Give the total number of integers to save in file. 78125*128 = 10^7 integers, 78125 disk pages
		String filename = "testfile.bin"; // Give the filename here. This file is DELETED in the end to ensure a new file!
		int numberOfQueries = 10000; // Give how many integers will be searched
		int[] queueSize = {1, 50, 100}; // Give the size of the queue in main memory (number of buffers)
		
//		// Uncomment here and in FileHandler if there is a need to check where each number if found
//		PrintStream log = new PrintStream(new FileOutputStream("logfile1.txt"));
//		System.setErr(log); // Log file where every searched integer and its position is printed. 

		
		
		// Subclass of RandomAccessFile. Contains all the writing and searching operations.
		FileHandler file = new FileHandler(filename, "rw");
		// Creates the specified file
		file.create(bufferSize, numberOfInt);
		file.close();
		
		file = new FileHandler(filename, "r");

		System.out.println("Counting average number of disk accesses...");
		// Searches random integers in a file by accessing diskpages sequentially (Question A)
//		System.out.println("Sequential search: " + file.searchSequential(numberOfQueries, bufferSize, numberOfInt));
		// Searches random integers in a file by using Binary search (on diskpages) (Question B)
		System.out.println("Binary search: " + file.searchBinary(numberOfQueries, bufferSize, numberOfInt));
		// Searches random integers in a file by using Binary search (on diskpages) and by ordering queries (Question C)
		System.out.println("Binary search with ordered queries: " + file.searchOrderedQueries(numberOfQueries, bufferSize, numberOfInt));
		// Searches random integers in a file by using Binary search (on diskpages) and retaining a queue of diskpages in main memory  (Question D)
		for (int i=0;i<queueSize.length;i++)
			System.out.println("Binary search with a queue in memory (size = " + queueSize[i] + "): "  + file.searchMemoryQueue(numberOfQueries, bufferSize, numberOfInt, queueSize[i]));
		file.close();
//		log.close();
		
		File oldFile = new File(filename);
		oldFile.delete();
		

	}

}
