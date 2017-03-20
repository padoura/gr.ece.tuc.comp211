package gr.ece.tuc.comp211;

/**
 * A simple class that stores together the integer array of a  disk page and its corresponding  page number 
 */
public class DiskPage {
	
	protected int [] intBuffer;
	protected int pageNumber;
	
	public DiskPage() {
		pageNumber = -1;
		intBuffer = null;
	}
	
	public DiskPage(int bufferSize) {
		pageNumber = -1;
		intBuffer = new int[bufferSize];
	}




}
