package DotComBust;

import java.util.*;

public class DotCom {
	
	/* locationCells ArrayList<String> instance variable will hold 
	three ArrayList<String> Objs contained the three coordinates of the Dot Coms */
	private ArrayList<String> locationCells;				// in example locationCells = {"b5","b6","b7");
	
	private String name; // name of this dotcome instance
	
	// setter for this dotcom locationCells instance variable
	public void setLocationCells(ArrayList<String> loc) {
		locationCells = loc;
	}
	
	// name setter for this dotcom instance
	public void setName(String n) {
		name = n;
	}
	
	// check YourSelf, dotcom object!
	
	// am I okay?
	public String checkYourself(String userInput) {			// userInput parameter gets passed as argument an ArrayList<String> containing a coordinate
		String result = "miss";								// we assume its a miss
		int index = locationCells.indexOf(userInput);		// which is the index of this ArrayList<String> obj? (which is a coordinate like b7
		
		if (index >= 0) {									// the index is this
			locationCells.remove(index);					// then remove it!
			
			if (locationCells.isEmpty()) {					// what if the ArrayList<String> locationCells is now empty? the DotCom has sunk, remove it
				result = "kill";							
				System.out.println("Ouch! You sunk " + name + "  : ( ");
			} else {
				result = "hit";
			} // close if
		} // close if
	return result;											// this whole checkYourself method is declared with a String return type, it must return a String.
	} // close method
} // close class