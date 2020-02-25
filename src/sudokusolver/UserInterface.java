package sudokusolver;

public class UserInterface {
	private int[][] grid;

	public UserInterface() {
		this.grid = new int[9][9];
	}

	public void solve(){
		createSamplePuzzle();
		printMatrix();
		Solver solver = new Solver(grid);
		
		System.out.println("\nSolving...\n");
		solver.start();
		
		printMatrix();
	}
	
	public void createSamplePuzzle() {

		// Future iterations would let user input numbers
		this.grid[0][0] = 5;
		this.grid[0][1] = 3;
		this.grid[0][4] = 7;
		this.grid[1][0] = 6;
		this.grid[1][3] = 1;
		this.grid[1][4] = 9;
		this.grid[1][5] = 5;
		this.grid[2][1] = 9;
		this.grid[2][2] = 8;
		this.grid[2][7] = 6;

		this.grid[3][0] = 8;
		this.grid[3][4] = 6;
		this.grid[3][8] = 3;
		this.grid[4][0] = 4;
		this.grid[4][3] = 8;
		this.grid[4][5] = 3;
		this.grid[4][8] = 1;
		this.grid[5][0] = 7;
		this.grid[5][4] = 2;
		this.grid[5][8] = 6;

		this.grid[6][1] = 6;
		this.grid[6][6] = 2;
		this.grid[6][7] = 8;
		this.grid[7][3] = 4;
		this.grid[7][4] = 1;
		this.grid[7][5] = 9;
		this.grid[7][8] = 5;
		this.grid[8][4] = 8;
		this.grid[8][7] = 7;
		this.grid[8][8] = 9;
	}
	
	
	public void printMatrix(){
		for (int i = 0; i < grid.length; i++){
			for (int j = 0; j < grid[i].length; j++){
				System.out.print(grid[i][j] + "\t");
			}
			System.out.println();
		}
	}
	
}
