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

	public void create(int bufferSize, int numberOfInt) throws IOException{
		
		ByteBuffer byteBuffer = ByteBuffer.allocate(bufferSize);        
        IntBuffer intBuffer = byteBuffer.asIntBuffer();
        int[] allIntegers = new int[numberOfInt];
        int counterFile;
        this.seek(0);
        
        for (int i=1; i <= numberOfInt; i++){
			allIntegers[i-1] = i;
		}
        

		for (counterFile = 1 ; counterFile <= numberOfInt ; counterFile += bufferSize/Integer.BYTES){
			intBuffer.put(Arrays.copyOfRange(allIntegers, counterFile-1, counterFile-1+bufferSize/Integer.BYTES));
			byte[] buffer = byteBuffer.array();
			this.write(buffer);
			intBuffer.clear();
		}
	}
	

	public int[] readPage(int bufferSize, long pagePointer) throws IOException{
		ByteBuffer byteBuffer = ByteBuffer.allocate(bufferSize);        
        IntBuffer intBuffer = byteBuffer.asIntBuffer();
        byte[] buffer = new byte[bufferSize];
        int[] actualBuffer = new int[bufferSize/Integer.BYTES];
        this.seek(pagePointer);
		this.read(buffer);
		byteBuffer.put(buffer);
		intBuffer.get(actualBuffer);
		
		return actualBuffer;
		
	}

	public double searchSequential(int amountOfSearches, int bufferSize, int numberOfInt) throws IOException{
		// TODO Auto-generated method stub
		double diskAccesses = 0;
		long pagePointer;
		Random RNG = new Random();
		int randomInteger = RNG.nextInt(numberOfInt);
		int [] intBuffer;
		
		System.out.print("Sequential Search Progress (%): ");
		for (int searchCount = 0; searchCount < amountOfSearches; searchCount++){
			pagePointer = 0;
			intBuffer = this.readPage(bufferSize, pagePointer);
			pagePointer += bufferSize;
			diskAccesses++;
			while ( ( pagePointer < numberOfInt*Integer.BYTES ) && ( ! this.isInBuffer(intBuffer, randomInteger) ) ){
				// Executes if the randomInteger is not found inside the buffer, and the buffer is not empty!
				pagePointer += bufferSize;
				intBuffer = this.readPage(bufferSize, pagePointer);
				diskAccesses++;
			}
			
			randomInteger = RNG.nextInt(numberOfInt);

			if ((searchCount % 1000) ==0){
				System.out.println((searchCount+1)*100/amountOfSearches);
			}
		}
				
		return Math.round(diskAccesses/amountOfSearches);
	}
	
	

	private boolean isInBuffer(int[] intBuffer, int randomInteger) {
		return Arrays.binarySearch(intBuffer, randomInteger)>=0;
	}

	public double searchBinary(int amountOfSearches, int bufferSize) {
		// TODO Auto-generated method stub
		double diskAccesses = 0;
		
		
		return Math.round(diskAccesses/amountOfSearches);
	}

	public double searchOrderedQueries(int amountOfSearches, int bufferSize) {
		// TODO Auto-generated method stub
		double diskAccesses = 0;
		
		
		return Math.round(diskAccesses/amountOfSearches);
	}

	public double searchMemoryQueue(int amountOfSearches, int bufferSize) {
		// TODO Auto-generated method stub
		double diskAccesses = 0;
		
		
		return Math.round(diskAccesses/amountOfSearches);
	}
	



}
