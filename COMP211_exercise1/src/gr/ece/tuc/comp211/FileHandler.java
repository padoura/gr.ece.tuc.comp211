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

package gr.ece.tuc.comp211;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.Random;



public class FileHandler extends RandomAccessFile{
	

	public FileHandler(String name, String mode) throws FileNotFoundException {
		super(name, mode);
	}

	// ---------------The 4 Search methods----------------------------------------
	public double searchSequential(int amountOfSearches, int bufferSize, int numberOfInt) throws IOException{
		double diskAccesses = 0;
		long filePointer;
		int [] randomInteger = this.getRandomIntegers(amountOfSearches, numberOfInt); // get an array of random integers
		DiskPage diskPage;
		int currentPage;
		int maxPage = (int) Math.ceil((double) numberOfInt*Integer.BYTES/bufferSize); // total number of diskpages
		int indexFound;
		
//		System.err.println("---------------------------------");
//		System.err.println("Results of Sequential Search:");
//		System.err.println("---------------------------------");
		
		for (int searchCount = 0; searchCount < amountOfSearches; searchCount++){
			
			currentPage = 0; // restart from the beginning of the file
		
			do{
				currentPage++;
				filePointer = (currentPage-1)*bufferSize;
				diskPage = this.readPage(bufferSize, filePointer);
				filePointer += bufferSize;
				diskAccesses++;
				indexFound = Arrays.binarySearch(diskPage.intBuffer, randomInteger[searchCount]); // negative if not found
			} while ( currentPage < maxPage &&  indexFound < 0);
			
//			System.err.println("Number " + randomInteger[searchCount] + " found in page " + currentPage + " and position " + indexFound);
		}
				
		return diskAccesses/amountOfSearches;
	}
	

	public double searchBinary(int amountOfSearches, int bufferSize, int numberOfInt) throws IOException {
		double diskAccesses = 0;
		long filePointer;
		int maxPage;
		int minPage;
		int currentPage;
		int [] randomInteger = this.getRandomIntegers(amountOfSearches, numberOfInt); // get an array of random integers
		DiskPage diskPage;
		int indexFound;
		
//		System.err.println("---------------------------------");
//		System.err.println("Results of Binary Search:");
//		System.err.println("---------------------------------");

		
		for (int searchCount = 0; searchCount < amountOfSearches; searchCount++){
			maxPage = (int) Math.ceil((double) numberOfInt*Integer.BYTES/bufferSize);
			minPage = 1;

			
			do {
				currentPage = (maxPage + minPage)/2 ; // get the middle page
				filePointer = (long) ((currentPage-1)*bufferSize); // start from the middle page of the file
				diskPage = this.readPage(bufferSize, filePointer);
				diskAccesses++;
				// indexFound = -1 if smaller, -length-1 if larger
				indexFound = Arrays.binarySearch(diskPage.intBuffer, randomInteger[searchCount]); 
				
				if ( indexFound != -1)
					minPage = currentPage+1; // go to the right side
				else
					maxPage = currentPage-1; // go to the left side
			} while ( indexFound < 0 &&  maxPage >= minPage  );
			
			
			
//			System.err.println("Number " + randomInteger[searchCount] + " found in page " + currentPage + " and position " + indexFound);

		}
				
		return diskAccesses/amountOfSearches;
	}

	public double searchOrderedQueries(int amountOfSearches, int bufferSize, int numberOfInt) throws IOException {
		double diskAccesses = 0;
		long filePointer;
		int maxPage = (int) Math.ceil((double) numberOfInt*Integer.BYTES/bufferSize);
		int minPage = 1;
		int currentPage;
		int [] randomInteger = this.getRandomIntegers(amountOfSearches, numberOfInt); // get an array of random integers
		DiskPage diskPage;
		int indexFound;
		
		Arrays.sort(randomInteger); // sort the wanted integers before searching
		// run it once to get the first page
		do {
			currentPage = (maxPage + minPage)/2 ; // get the middle page
			filePointer = (long) ((currentPage-1)*bufferSize); // start from the middle page of the file
			diskPage = this.readPage(bufferSize, filePointer);
			diskAccesses++;
			indexFound = Arrays.binarySearch(diskPage.intBuffer, randomInteger[0]); // -1 if smaller, -length-1 if larger
			
			if ( indexFound != -1)
				minPage = currentPage+1; // go to the right side
			else
				maxPage = currentPage-1; // go to the left side
		} while ( indexFound < 0 &&  maxPage >= minPage  );
		
//		System.err.println("-------------------------------------------------");
//		System.err.println("Results of Binary Search with Ordered Queries:");
//		System.err.println("-------------------------------------------------");
//		System.err.println("Number " + randomInteger[0] + " found in page " + currentPage + " and position " + indexFound);
		
		
		for (int searchCount = 1; searchCount < amountOfSearches; searchCount++){
			maxPage = (int) Math.ceil((double) numberOfInt*Integer.BYTES/bufferSize);
			minPage = 1;
			
			
			indexFound = Arrays.binarySearch(diskPage.intBuffer, randomInteger[searchCount]); // check whether next integer is in the current buffer
			while ( indexFound < 0 &&  maxPage >= minPage  ){ // If not in memory, search the file again
				currentPage = (maxPage + minPage)/2 ; // get the middle page
				filePointer = (long) ((currentPage-1)*bufferSize); // start from the middle page of the file
				diskPage = this.readPage(bufferSize, filePointer);
				diskAccesses++;
				indexFound = Arrays.binarySearch(diskPage.intBuffer, randomInteger[searchCount]);
				if ( indexFound != -1)
					minPage = currentPage+1; // go to the right side
				else
					maxPage = currentPage-1; // go to the left side
			}
			
//			System.err.println("Number " + randomInteger[searchCount] + " found in page " + currentPage + " and position " + indexFound);

		}
		
		return diskAccesses/amountOfSearches;
	}
	
	
	
	

	public double searchMemoryQueue(int amountOfSearches, int bufferSize, int numberOfInt, int queueSize) throws IOException {
		double diskAccesses = 0;
		long filePointer;
		int maxPage = (int) Math.ceil((double) numberOfInt*Integer.BYTES/bufferSize);
		int minPage = 1;
		int currentPage;
		int [] randomInteger = this.getRandomIntegers(amountOfSearches, numberOfInt); // get an array of random integers
		DiskPage diskPage;
		int indexFound;
		
		ArrayQueue queue = new ArrayQueue(queueSize);
		
		// run it once to fill the queue
		do {
			currentPage = (maxPage + minPage)/2 ; // get the middle page
			filePointer = (long) ((currentPage-1)*bufferSize); // start from the middle page of the file
			diskPage = this.readPage(bufferSize, filePointer);
			diskAccesses++;
			indexFound = Arrays.binarySearch(diskPage.intBuffer, randomInteger[0]); // -1 if smaller, -length-1 if larger
			
			queue.enqueue(diskPage); // Add last accessed disk page to the memory queue (if queue is full, also delete the oldest disk page)
			
			if ( indexFound != -1)
				minPage = currentPage+1; // go to the right side
			else
				maxPage = currentPage-1; // go to the left side
		} while ( indexFound < 0 &&  maxPage >= minPage  );
		
		
//		System.err.println("-------------------------------------------------");
//		System.err.println("Results of Binary Search with a Queue in Memory (size = " + queueSize + ")");
//		System.err.println("-------------------------------------------------");
//		System.err.println("Number " + randomInteger[0] + " found in page " + currentPage + " and position " + indexFound);
		
		
		for (int searchCount = 1; searchCount < amountOfSearches; searchCount++){
			maxPage = (int) Math.ceil((double) numberOfInt*Integer.BYTES/bufferSize);
			minPage = 1;
			
			indexFound = -1;
			while ( indexFound < 0 && maxPage >= minPage  ){
				currentPage = (maxPage + minPage)/2 ; // get the middle page
				
				diskPage = this.searchInQueue(queue, currentPage); 
				if (diskPage.pageNumber == currentPage)
					indexFound = Arrays.binarySearch(diskPage.intBuffer, randomInteger[searchCount]);
				else{ // If not in memory, access the file again
					filePointer = (long) ((currentPage-1)*bufferSize);
					diskPage = this.readPage(bufferSize, filePointer);
					diskAccesses++;
					indexFound = Arrays.binarySearch(diskPage.intBuffer, randomInteger[searchCount]);
					queue.enqueue(diskPage); // Add last accessed disk page to the memory queue (if queue is full, also delete the oldest disk page)
				}
				if ( indexFound != -1)
					minPage = currentPage+1; // go to the right side
				else
					maxPage = currentPage-1; // go to the left side
			}
			
//			System.err.println("Number " + randomInteger[searchCount] + " found in page " + currentPage + " and position " + indexFound);
			queue.resetIterator();
		}		
		
		return diskAccesses/amountOfSearches;
	}
	
	
	
	
	
	// --------------- Utility methods----------------------------------------
	public int[] getRandomIntegers(int amountOfSearches, int numberOfInt){ // return an array of random integers
		Random RNG = new Random();
		int [] randomInteger = new int[amountOfSearches];
		for (int i=0; i<amountOfSearches;i++)
			randomInteger[i] = 1 + RNG.nextInt(numberOfInt);
		return randomInteger;
	}
	
	
	public DiskPage readPage(int bufferSize, long filePointer) throws IOException{
		ByteBuffer byteBuffer = ByteBuffer.allocate(bufferSize);      
        IntBuffer intBuffer = byteBuffer.asIntBuffer();
        byte[] buffer = new byte[bufferSize];
        DiskPage diskPage = new DiskPage(bufferSize/Integer.BYTES);
        this.seek(filePointer);
		this.read(buffer);
		byteBuffer.put(buffer);
		intBuffer.get(diskPage.intBuffer);
		diskPage.pageNumber = (int) ((filePointer/bufferSize) + 1);
		
		return diskPage;
		
	}
	
	
	public void create(int bufferSize, int numberOfInt) throws IOException{ // create a file of integers [1:numberOfInt]
		
		ByteBuffer byteBuffer = ByteBuffer.allocate(bufferSize);       
        IntBuffer intBuffer = byteBuffer.asIntBuffer();
        
        int[] allIntegers = new int[numberOfInt + (bufferSize/Integer.BYTES) - numberOfInt % (bufferSize/Integer.BYTES)];
        int counterFile;
        this.seek(0);
        byte[] buffer;
        
        for (int i=1; i <= numberOfInt; i++)
			allIntegers[i-1] = i;
        // ensure that Arrays.binarySearch will work for the last disk page even if it's not completely filled
        // filling with 0s would not keep that array ordered anymore
        Arrays.fill(allIntegers, numberOfInt, allIntegers.length, Integer.MAX_VALUE);
        
		for (counterFile = 1 ; counterFile <= numberOfInt ; counterFile += bufferSize/Integer.BYTES){
			intBuffer.put(Arrays.copyOfRange(allIntegers, counterFile-1, counterFile-1+bufferSize/Integer.BYTES));
			buffer = byteBuffer.array();
			this.write(buffer);
			intBuffer.clear();
		}
		
	}
	
	
	public DiskPage searchInQueue(ArrayQueue queue, int currentPage){
		//Perform sequential search.
		//A hash table for constant time search could have used instead.
		
		DiskPage diskPage = new DiskPage();
		
		do{
			try {
				diskPage = ((DiskPage) queue.getNext());
			} catch (QueueEndReachedException e) {
				return diskPage;
			}
		} while (diskPage.pageNumber != currentPage);
		return diskPage;
	}

}
