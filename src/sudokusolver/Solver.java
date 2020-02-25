package sudokusolver;

import java.util.HashSet;

public class Solver {
	private int[][] grid;
	
	public Solver(int[][] grid){
		this.grid = grid;
	}
	
	
	
	
	public int findNextOpenSpot(){
		for (int row = 0; row < 9; row++){
			for (int col = 0; col < 9; col++){
				if (grid[row][col] == 0) return row * 9 + col;
			}
		}
		System.out.println("Board is complete");
		return -1;
	}

	public boolean isBoardComplete(){
		if (findNextOpenSpot() == -1) return true;
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
