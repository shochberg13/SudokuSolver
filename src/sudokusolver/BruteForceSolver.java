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
		boolean needToBackTrack;
		
		outerloop:
		while(true){
			// Go forward
			forwardTrack();
			needToBackTrack = !ableToWriteNumber;
			
			// Go backwards
			if(needToBackTrack){
				int backtrackingCounter = 0;
				do {
					do {
						backTrack();
						if (!ableToWriteNumber) backtrackingCounter++;
						if (isBoardComplete()) break outerloop;
					}while(!ableToWriteNumber);
					
					do {
						forwardTrack();
						if(ableToWriteNumber) backtrackingCounter--;
						if (isBoardComplete()) break outerloop;
					}while(ableToWriteNumber);
				}while(backtrackingCounter > 0);
			}
		}
	}
	
	public void forwardTrack(){
		Coord currentCoord = findNextOpenSpot();
		tryAllValues(currentCoord);
	}
	
	public void backTrack(){
		Coord backTrackCoord = findPrevOpenSpot();
		int prevRow = backTrackCoord.getRow();
		int prevCol = backTrackCoord.getCol();
		int prevVal = grid[prevRow][prevCol];
		
		tryAllValues(backTrackCoord, prevVal);
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
		
		//Go to previous spot (not necessarily open)
		if (currentCol != 0) {
			currentCol--;
		}else{
			currentRow --;
			currentCol = 8;
		}
		
		// Search backwards to find spot not occupied by user input
		boolean beginningSearch = true;
		for (int row = currentRow; row >= 0; row--){
			for (int col = 8; col >= 0; col--){
				
				// EXAMPLE: if starting at (3, 6), I do not need to inspect (3, 7) and (3, 8)
				if (beginningSearch) {
					col = currentCol;
					beginningSearch = false;
				}
				
				// If there is no user input there, it is open
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
					return new Coord(row, col);
				}
			}
		}
		return null;
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
	
	public boolean isSmallBoxSolved(int box){
		// @param box: 1 for top left, 2 top center, ... , 9 for bottom right
		
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

	public boolean isBoardComplete(){
		if (findNextOpenSpot() == null) return true;
		return false;
	}
}
