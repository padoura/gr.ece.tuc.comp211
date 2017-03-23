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
