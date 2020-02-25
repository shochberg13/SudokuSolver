package sudokusolver;

import java.util.HashSet;

public class Solver {
	private int[][] grid;
	private boolean[][] userInput;
	
	public Solver(int[][] grid){
		this.grid = grid;
		this.userInput = new boolean[9][9];
		
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
		while(!isBoardComplete()){
			Coord nextCoord = findNextOpenSpot(0, 0);
			
		}
	}
	
	public Coord findPrevOpenSpot(int currentRow, int currentCol){
		for (int row = currentRow; row >= 0; row--){
			for (int col = currentCol; col >= 0; col--){
				if (!userInput[row][col]) return new Coord(row, col);
			}
		}
		System.out.println("No spots left");
		return null;
	}
	
	public Coord findNextOpenSpot(int currentRow, int currentCol){
		for (int row = currentRow; row < 9; row++){
			for (int col = currentCol; col < 9; col++){
				if (grid[row][col] == 0) return new Coord(row, col);
			}
		}
		System.out.println("Board is complete");
		return null;
	}

	public boolean legalMove(int val, int row, int col){
		
		// Check row and column
		for (int i = 0; i < 9; i++){
			if (grid[i][col] == val) return false;
			if (grid[row][i] == val) return false;
		}
		
		// Check Box (Start by finding top left corner of small box)
		int rowStart = row - (row % 3);
		int colStart = col - (col % 3);
		
		for (int i = 0; i < 3; i++){
			for (int j = 0; j < 3; j++){
				if (grid[rowStart + i][colStart + j] == val) return false;
			}
		}
		
		return true;
	}
	
	public boolean isBoardComplete(){
		if (findNextOpenSpot(0,0) == null) return true;
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
}
