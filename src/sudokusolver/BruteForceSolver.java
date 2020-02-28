package sudokusolver;

import java.util.HashSet;

public class BruteForceSolver {
	private int[][] grid;
	private boolean[][] userInput;
	private boolean ableToWriteNumber;
	
	public BruteForceSolver(int[][] grid){
		this.grid = grid;
		this.userInput = new boolean[9][9];
		this.ableToWriteNumber = true;
		
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
	
	
	
	public void start(){
		
		boolean needToBackTrack;
		
		while(!isBoardComplete()){
			// Go Forward
			forwardTrack();
			needToBackTrack = !ableToWriteNumber;
			
			// If need to backtrack, then backtrack until good number is found, 
			// then forward track until back at original spot. 
			if(needToBackTrack){

				int backtrackingCounter = 0;
				do {
					do {
						backTrack();
						
						if (!ableToWriteNumber) backtrackingCounter++;
					}while(!ableToWriteNumber);
					
					do {
						forwardTrack();
						if(ableToWriteNumber) backtrackingCounter--;
					}while(ableToWriteNumber);
					
				}while(backtrackingCounter > 0);
				
			}
		}
		
		printMatrix();
	}
	
	
	public void forwardTrack(){
		Coord currentCoord = findNextOpenSpot();
		tryAllValues(currentCoord);
		printMatrix();
	}
	
	
	public void backTrack(){
		Coord backTrackCoord = findPrevOpenSpot();
		int prevRow = backTrackCoord.getRow();
		int prevCol = backTrackCoord.getCol();
		int prevVal = grid[prevRow][prevCol];
		
		tryAllValues(backTrackCoord, prevVal);
		printMatrix();
	}
	
	public void tryAllValues(Coord currentCoord){
		tryAllValues(currentCoord, 0);
	}
	
	public void tryAllValues(Coord currentCoord, int startingValue){
		int row = currentCoord.getRow();
		int col = currentCoord.getCol();
		for (int i = startingValue + 1; i <= 9; i++){
			if (legalMove(i, currentCoord)){
				grid[row][col] = i;
				ableToWriteNumber = true;
				return;
			}
		}
		grid[row][col] = 0;
		ableToWriteNumber = false;
	}
	
	
	public Coord findPrevOpenSpot(){
		Coord currentCoord = findNextOpenSpot();
		
		int currentRow = currentCoord.getRow();
		int currentCol = currentCoord.getCol();
		
		//Go to previous spot
		if (currentCol != 0) {
			currentCol--;
		}else{
			currentRow --;
			currentCol = 8;
		}
		
		// Find previous spot that was not occupied by user input
		boolean beginningSearch = true;
		for (int row = currentRow; row >= 0; row--){
			for (int col = 8; col >= 0; col--){
				
				// EXAMPLE: if starting at (3, 6), I do not need to inspect (3, 7) and (3, 8)
				if (beginningSearch) {
					col = currentCol;
					beginningSearch = false;
				}
				
				if (!userInput[row][col]) {
					return new Coord(row, col);
				}
			}
		}
		System.out.println("No spots left (error)");
		return null;
	}
	
	public Coord findNextOpenSpot(){
		
		for (int row = 0; row < 9; row++){
			for (int col = 0; col < 9; col++){
				if (grid[row][col] == 0) {
//					System.out.println("Board needs work. Going to next open spot: " + new Coord(row, col));
					return new Coord(row, col);
				}
			}
		}
		System.out.println("Board is complete");
		return null;
	}
	

	public boolean legalMove(int val, Coord coord){
		
		int row = coord.getRow();
		int col = coord.getCol();
		
		// Check row and column
		for (int i = 0; i < 9; i++){
			if (grid[i][col] == val) {
				return false;
			}
			if (grid[row][i] == val) {
				return false;
			}
		}
		
		// Check Box (Start by finding top left corner of small box)
		int rowStart = row - (row % 3);
		int colStart = col - (col % 3);
		
		for (int i = 0; i < 3; i++){
			for (int j = 0; j < 3; j++){
				if (grid[rowStart + i][colStart + j] == val) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	public boolean isBoardComplete(){
		if (findNextOpenSpot() == null) return true;
		return false;
	}
	
	public boolean isBoardSolved(){
		for (int i = 0; i < 9; i++){
			if (!isRowSolved(i)) return false;
			if (!isColSolved(i)) return false;
			if (!isSmallBoxSolved(i + 1)) return false;
		}
		return true;
	}
	
	public boolean isRowSolved(int row){
		HashSet<Integer> repeatCheck = new HashSet<Integer>();
		for (int i = 0; i < 9; i++){
			int val = grid[row][i];
			if (val > 0 && val <= 9) repeatCheck.add(val);
		}
		if (repeatCheck.size() == 9) return true;
		return false;
	}
	
	public boolean isColSolved(int col){
		HashSet<Integer> repeatCheck = new HashSet<Integer>();
		for (int i = 0; i < 9; i++){
			int val = grid[i][col];
			if (val > 0 && val <= 9) repeatCheck.add(val);
		}
		
		if (repeatCheck.size() == 9) return true;
		return false;
	}
	
	
	/**
	 * 
	 * @param box: 1 for top left, 2 top center, ... , 9 for bottom right
	 */
	public boolean isSmallBoxSolved(int box){

		// Convert box number into top left coordinate of the box
		int rowStart = 1;
		if (box < 4) rowStart = 0;
		if (box > 6) rowStart = 2;
		
		int colStart = 0;
		if (box % 3 == 2) colStart = 3;
		if (box % 3 == 0) colStart = 6;
		
		// Check for repeats
		HashSet<Integer> repeatCheck = new HashSet<Integer>();
		for (int i = rowStart; i < 3; i++){
			for(int j = colStart; j < 3; j++){
				repeatCheck.add(grid[i][j]);
			}
		}
		
		if (repeatCheck.size() == 9) return true;
		return false;
	}
	
	

	public void clearMatrix(boolean[][][] matrix){
		for (int i = 0; i < matrix.length; i++){
			for (int j = 0; j < matrix[i].length; j++){
				for (int k = 0; k < matrix[i][j].length; k++){
					matrix[i][j][k] = false;
				}
			}
		}
	}
	
	public void printMatrix(){
		System.out.println("\n\n");
		for (int i = 0; i < grid.length; i++){
			for (int j = 0; j < grid[i].length; j++){
				System.out.print(grid[i][j]);
				if(j % 3 == 2) {
					System.out.print("   |    ");
				}else{
					System.out.print("\t");
				}
			}
			if(i % 3 == 2) {
				System.out.println("\n____________________________________________________________________");
			}else{
				System.out.println("\n");
			}
		}
	}
	
}
