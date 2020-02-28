package sudokusolver;

public class POESolver {
	private int[][] grid;
	private boolean[][] userInput;
	
	public POESolver(int[][] grid){
		this.grid = grid;
		this.userInput = new boolean[9][9];
		
		// userInput stores initial values that cannot be overwritten in Sudoku grid
		for (int i = 0; i < 9; i ++){
			for(int j = 0; j < 9; j++){
				if (this.grid[i][j] != 0){
					userInput[i][j] = true;
				}else{
					userInput[i][j] = false;
				}
			}
		}
	}
	
	public void run(){
		while(!isBoardComplete()){
		
			// Check row
			outerloop:
			for (int number = 1; number <= 9; number++){
				for (int row = 0; row < 9; row++){
					if (checkRow(row, number)) break outerloop;
				}
			}
			
			// Check column
			for (int number = 1; number <= 9; number++){
				for (int col = 0; col < 9; col++){
					checkCol(col, number);	
				}
			}
		}
	}
	
	
	public boolean legalMove(int number, int row, int col){
		for (int i = 0; i < 9; i++){
			
			// Check entire column
			if (grid[i][col] == number) {
				return false;
			}
			// Check entire row
			if (grid[row][i] == number) {
				return false;
			}
		}
		
		// Check small box (Start by finding top left corner of small box)
		int rowStart = row - (row % 3);
		int colStart = col - (col % 3);
		
		// Check all 9 spaces in the small box
		for (int i = 0; i < 3; i++){
			for (int j = 0; j < 3; j++){
				if (grid[rowStart + i][colStart + j] == number) {
					return false;
				}
			}
		}
		return true;
	}
	
	public boolean checkRow(int row, int number){
		// Will check entire row if there is only one legal move for each number. 
		// If there is only one legal move, then that move is correct.
		
		int legalMoveCounter = 0;
		int legalCol = -1;
		
		for (int col = 0; col < 9; col++){
			if (userInput[row][col] == true) continue;
			if (legalMove(number, row, col)){
				legalCol = col;
				legalMoveCounter++;
			}
			if (legalMoveCounter > 1) return false;				
		}
		if (legalMoveCounter == 1){
			grid[row][legalCol] = number;
			userInput[row][legalCol] = true;
			return true;
		}
		return false;
	}
	
	public boolean checkCol(int col, int number){
		// Will check entire column if there is only one legal move for each number. 
		// If there is only one legal move, then that move is correct.
		
		int legalMoveCounter = 0;
		int legalRow = -1;
		
		for (int row = 0; row < 9; row++){
			if (userInput[row][col] == true) continue;
			if (legalMove(number, row, col)){
				legalRow = row;
				legalMoveCounter++;
			}
			if (legalMoveCounter > 1) return false;				
		}
		if (legalMoveCounter == 1){
			grid[legalRow][col] = number;
			userInput[legalRow][col] = true;
			return true;
		}
		return false;
	}
	
	public boolean isBoardComplete(){
		for (int row = 0; row < 9; row++){
			for (int col = 0; col < 9; col++){
				if (grid[row][col] == 0) {
					return false;
				}
			}
		}
		return true;
	}
	
}
