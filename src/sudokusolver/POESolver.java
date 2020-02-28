package sudokusolver;

public class POESolver {
	private int[][] grid;
	private boolean[][] userInput;
	
	public POESolver(int[][] grid){
		this.grid = grid;
		this.userInput = new boolean[9][9];
		
		// User input stores initial values that cannot be overwritten
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
		
		outerloop:
		while(!isBoardComplete()){
			boolean noNumbersSolved = true;
			for (int number = 1; number <= 9; number++){
				for (int row = 0; row < 9; row++){
					noNumbersSolved = false;
					if (checkRow(row, number)) break outerloop;
					
				}
			}
			
			if (noNumbersSolved){
				for (int number = 1; number <= 9; number++){
					for (int col = 0; col < 9; col++){
						checkCol(col, number);	
					}
				}
			}
		}
	}
	
	
	public boolean legalMove(int val, Coord coord){
		int row = coord.getRow();
		int col = coord.getCol();
		
		for (int i = 0; i < 9; i++){
			// Check entire column
			if (grid[i][col] == val) {
				return false;
			}
			// Check entire row
			if (grid[row][i] == val) {
				return false;
			}
		}
		
		// Check Box (Start by finding top left corner of small box)
		int rowStart = row - (row % 3);
		int colStart = col - (col % 3);
		
		// Check all 9 spaces in the small box
		for (int i = 0; i < 3; i++){
			for (int j = 0; j < 3; j++){
				if (grid[rowStart + i][colStart + j] == val) {
					return false;
				}
			}
		}
		return true;
	}
	
	public boolean checkRow(int row, int number){
		int legalMoveCounter = 0;
		int legalCol = -1;
		
		for (int col = 0; col < 9; col++){
			if (userInput[row][col] == true) continue;
			if (legalMove(number, new Coord(row, col))){
				legalCol = col;
				legalMoveCounter++;
			}
			if (legalMoveCounter > 1) return false;				
		}
		if (legalMoveCounter == 1){
			grid[row][legalCol] = number;
			return true;
		}
		return false;
	}
	
	public boolean checkCol(int col, int number){
		int legalMoveCounter = 0;
		int legalRow = -1;
		
		for (int row = 0; row < 9; row++){
			if (userInput[row][col] == true) continue;
			if (legalMove(number, new Coord(row, col))){
				legalRow = row;
				legalMoveCounter++;
			}
			if (legalMoveCounter > 1) return false;				
		}
		if (legalMoveCounter == 1){
			grid[legalRow][col] = number;
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
