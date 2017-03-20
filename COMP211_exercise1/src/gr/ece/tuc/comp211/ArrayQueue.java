package gr.ece.tuc.comp211;

public class ArrayQueue implements Queue {

	private static final int defaultSize = 10;
	private int size; // Maximum size of queue
	private int front; // Index prior to front item
	private int rear; // Index of rear item
	private Object [] listArray; // Array holding Objects
	private int index; // Iterating index
	
	
	
	ArrayQueue(){setup(defaultSize); } // Constructor: default size
	
	ArrayQueue(int sz){ setup(sz); } // Constructor: set size
	
	
	private void setup(int sz){          // Initialize queue
		size = sz+1;
		front = rear = index = 0;
		listArray = new Object[sz+1];
	}
	
	public void clear(){
		front = rear = index = 0; // Remove all Objects from queue
	}
	
	public void enqueue(Object it){// Enqueue Object at rear
		
		if ((rear+1) % size == front) // If queue is full, delete the oldest Object first
			this.dequeue();
		rear = (rear+1) % size; // Increment rear (in circle)
		listArray[rear] = it;
	}
		
	
	public Object dequeue(){ // Dequeue Object from front
		Assert.notFalse(!isEmpty(), "Queue is empty");
		front = (front+1) % size; // Increment front
		this.resetIterator();
		return listArray[front]; // Return value
	}
		
		
	public Object firstValue(){ // Return value of front Object
		Assert.notFalse(!isEmpty(), "Queue is empty");
		return listArray[(front+1) % size];
	}

	public boolean isEmpty(){// Return true if queue is empty
		return front == rear;
	}
	
	public boolean hasNext(){// Return true if next is not empty
		return index != rear;
	}
	
	public Object getNext() throws QueueEndReachedException{ // Return the next value if it exists
		Assert.notFalse(!isEmpty(), "Queue is empty");
		
		if (this.hasNext()){
			index = (index+1) % size; // Increment index
			return listArray[index]; // Return value
		}else{
			this.resetIterator();
			throw new QueueEndReachedException("Index " + index + " is out of bounds");
		}
		
	}
	
	public void resetIterator(){
		index = front;
	}
	
	
	

}
