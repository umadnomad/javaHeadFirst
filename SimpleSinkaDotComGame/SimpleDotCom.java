package SimpleSinkaDotComGame;

public class SimpleDotCom {
	
	int[] locationCells;
	int numOfHits;
	int lastHit;
	boolean firstTime = true;
	private boolean isValidGuess = true;
	
	public void setLocationCells(int[] locs) {
		locationCells = locs;
	}
	
	public String checkYourself(String stringGuess) {
		String result = "miss";
		if (stringGuess != null) {
			int guess = Integer.parseInt(stringGuess);
			
			if (!firstTime && (guess == lastHit)) {
				System.out.println("You already guessed this position");
				isValidGuess = false;
				return "";
			}
			
			firstTime = false;
			for (int cell : locationCells) {
				if (guess == cell) {
					result = "hit";
					numOfHits++;
					break;
				}
			}

			if (numOfHits == locationCells.length) {
				result = "kill";
			}
			lastHit = guess;
			System.out.println(result);
			isValidGuess = true;
				return result;
		} else {
			System.out.println("You must insert a value");
			isValidGuess = false;
			return result;
		}
	}
	
	public void setIsValidGuess(boolean param) {
		isValidGuess = param;
	}
	
	public boolean getIsValidGuess() {
		return isValidGuess;
	}

}