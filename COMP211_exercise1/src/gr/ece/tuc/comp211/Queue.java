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

public interface Queue {// Queue ADT
	public void clear(); // Remove all Objects from queue
	public void enqueue(Object it); // Enqueue Object at rear of queue
	public Object dequeue(); // Dequeue Object from front of queue
	public Object firstValue(); // Return value of top Object
	public boolean isEmpty(); // Return TRUE if stack is empty
}
