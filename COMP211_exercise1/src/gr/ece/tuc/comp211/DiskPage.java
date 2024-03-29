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
