package SimpleSinkaDotComGame;

public class SimpleDotComGame {
	/*this class is the game and runs in main method, so you do not create an instance
	of this entity*/
	public static void main(String[] args) {
		
		int numOfGuesses = 0;
		
		//entity instance creation
		//user helper
		GameHelper helper = new GameHelper();
		
		//dotcom to be sinked
		SimpleDotCom theDotCom = new SimpleDotCom();
		
		//generate a random number between 0 and 4.
		int randomNum = (int) (Math.random() * 5);
		int[] locations = {randomNum, randomNum+1, randomNum+2};
		
		theDotCom.setLocationCells(locations);
		
		//keys of the engine
		boolean isAlive = true;
		
		//let the ball roll
		while (isAlive) {
			
			String guess = helper.getUserInput("enter a number");
			
			String result = theDotCom.checkYourself(guess);
			
			if (theDotCom.getIsValidGuess())
				numOfGuesses++;
				
			if (result.equals("kill")) {
				isAlive = false;
				System.out.println("You took " + numOfGuesses + " guesses");
			}
		}
	}
}