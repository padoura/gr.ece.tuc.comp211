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
		// TODO Auto-generated constructor stub
	}

	// ---------------The 4 Search methods----------------------------------------
	public double searchSequential(int amountOfSearches, int bufferSize, int numberOfInt) throws IOException{
		double diskAccesses = 0;
		long pagePointer;
		int [] randomInteger = this.getRandomIntegers(amountOfSearches, numberOfInt); // get an array of random integers
		DiskPage diskPage;
		int currentPage;
		int maxPage = (int) Math.ceil(numberOfInt*Integer.BYTES/bufferSize); // total number of diskpages
		int indexFound;
		
//		System.err.println("---------------------------------");
//		System.err.println("Results of Sequential Search:");
//		System.err.println("---------------------------------");
		
		for (int searchCount = 0; searchCount < amountOfSearches; searchCount++){
			
			currentPage = 0; // restart from the beginning of the file
		
			do{
				currentPage++;
				pagePointer = (currentPage-1)*bufferSize;
				diskPage = this.readPage(bufferSize, pagePointer);
				pagePointer += bufferSize;
				diskAccesses++;
				indexFound = Arrays.binarySearch(diskPage.intBuffer, randomInteger[searchCount]); // negative if not found
			} while ( currentPage < maxPage &&  indexFound < 0);
			
//			System.err.println("Number " + randomInteger[searchCount] + " found in page " + currentPage + " and position " + indexFound);
		}
				
		return diskAccesses/amountOfSearches;
	}
	

	public double searchBinary(int amountOfSearches, int bufferSize, int numberOfInt) throws IOException {
		// TODO Auto-generated method stub
		double diskAccesses = 0;
		long pagePointer;
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
			maxPage = (int) Math.ceil(numberOfInt*Integer.BYTES/bufferSize);
			minPage = 1;

			
			do {
				currentPage = (maxPage + minPage)/2 ; // get the middle page
				pagePointer = (long) ((currentPage-1)*bufferSize); // start from the middle page of the file
				diskPage = this.readPage(bufferSize, pagePointer);
				diskAccesses++;
				indexFound = Arrays.binarySearch(diskPage.intBuffer, randomInteger[searchCount]); // -1 if smaller, -length-1 if larger
				
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
		long pagePointer;
		int maxPage = (int) Math.ceil(numberOfInt*Integer.BYTES/bufferSize);
		int minPage = 1;
		int currentPage;
		int [] randomInteger = this.getRandomIntegers(amountOfSearches, numberOfInt); // get an array of random integers
		DiskPage diskPage;
		int indexFound;
		
		Arrays.sort(randomInteger); // sort the wanted integers before searching
		
		do {
			currentPage = (maxPage + minPage)/2 ; // get the middle page
			pagePointer = (long) ((currentPage-1)*bufferSize); // start from the middle page of the file
			diskPage = this.readPage(bufferSize, pagePointer);
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
			maxPage = (int) Math.ceil(numberOfInt*Integer.BYTES/bufferSize);
			minPage = 1;
			
			
			indexFound = Arrays.binarySearch(diskPage.intBuffer, randomInteger[searchCount]); // check whether next integer is in the current buffer
			while ( indexFound < 0 &&  maxPage >= minPage  ){ // If not in memory, search the file again
				currentPage = (maxPage + minPage)/2 ; // get the middle page
				pagePointer = (long) ((currentPage-1)*bufferSize); // start from the middle page of the file
				diskPage = this.readPage(bufferSize, pagePointer);
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
		long pagePointer;
		int maxPage = (int) Math.ceil(numberOfInt*Integer.BYTES/bufferSize);
		int minPage = 1;
		int currentPage;
		int [] randomInteger = this.getRandomIntegers(amountOfSearches, numberOfInt); // get an array of random integers
		DiskPage diskPage;
		int indexFound;
		
		ArrayQueue queue = new ArrayQueue(queueSize);
		
		
		do {
			currentPage = (maxPage + minPage)/2 ; // get the middle page
			pagePointer = (long) ((currentPage-1)*bufferSize); // start from the middle page of the file
			diskPage = this.readPage(bufferSize, pagePointer);
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
			maxPage = (int) Math.ceil(numberOfInt*Integer.BYTES/bufferSize);
			minPage = 1;
			
			indexFound = -1;
			while ( indexFound < 0 && maxPage >= minPage  ){
				currentPage = (maxPage + minPage)/2 ; // get the middle page
				
				diskPage = this.searchInQueue(queue, currentPage); 
				if (diskPage.pageNumber == currentPage)
					indexFound = Arrays.binarySearch(diskPage.intBuffer, randomInteger[searchCount]);
				else{ // If not in memory, access the file again
					pagePointer = (long) ((currentPage-1)*bufferSize);
					diskPage = this.readPage(bufferSize, pagePointer);
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
	public int[] getRandomIntegers(int amountOfSearches, int numberOfInt){ // returns an array of random integers
		Random RNG = new Random();
		int [] randomInteger = new int[amountOfSearches];
		for (int i=0; i<amountOfSearches;i++)
			randomInteger[i] = 1 + RNG.nextInt(numberOfInt);
		return randomInteger;
	}
	
	
	public DiskPage readPage(int bufferSize, long pagePointer) throws IOException{
		ByteBuffer byteBuffer = ByteBuffer.allocate(bufferSize);        
        IntBuffer intBuffer = byteBuffer.asIntBuffer();
        byte[] buffer = new byte[bufferSize];
        DiskPage diskPage = new DiskPage(bufferSize/Integer.BYTES);
        this.seek(pagePointer);
		this.read(buffer);
		byteBuffer.put(buffer);
		intBuffer.get(diskPage.intBuffer);
		diskPage.pageNumber = (int) ((pagePointer/bufferSize) + 1);
		
		return diskPage;
		
	}
	
	
	public void create(int bufferSize, int numberOfInt) throws IOException{ // create a file of integers
		
		ByteBuffer byteBuffer = ByteBuffer.allocate(bufferSize);        
        IntBuffer intBuffer = byteBuffer.asIntBuffer();
        int[] allIntegers = new int[numberOfInt];
        int counterFile;
        this.seek(0);
        
        for (int i=1; i <= numberOfInt; i++)
			allIntegers[i-1] = i;
        
		for (counterFile = 1 ; counterFile <= numberOfInt ; counterFile += bufferSize/Integer.BYTES){
			intBuffer.put(Arrays.copyOfRange(allIntegers, counterFile-1, counterFile-1+bufferSize/Integer.BYTES));
			byte[] buffer = byteBuffer.array();
			this.write(buffer);
			intBuffer.clear();
		}
	}
	
	
	public DiskPage searchInQueue(ArrayQueue queue, int currentPage){
		
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
