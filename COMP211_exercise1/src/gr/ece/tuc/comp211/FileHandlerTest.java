package gr.ece.tuc.comp211;

import java.io.IOException;
import java.util.Arrays;

public class FileHandlerTest {

	public static void testCreate(FileHandler file, int bufferSize, int numberOfInt) throws IOException {
		// TODO Auto-generated method stub
		int pagePointer = 0;
		int[] returnedIntegers = new int[numberOfInt];
		int[] expectedIntegers = new int[numberOfInt];
		for (int counterFile = 0; counterFile < numberOfInt; counterFile += bufferSize/Integer.BYTES){
			System.arraycopy(file.readPage(bufferSize, pagePointer), 0, returnedIntegers, counterFile, bufferSize/Integer.BYTES);
			pagePointer += bufferSize;
		}
		
        for (int i=1; i <= numberOfInt; i++){
			expectedIntegers[i-1] = i;
		}
		
		System.out.println("Testing file read/write (successful if true):  " + Arrays.equals(returnedIntegers, expectedIntegers));
		
	}

}
