import java.util.ArrayList;

public class Grid {
	public Cell[][] cells;
	ArrayList<String> obstacleLocation;
	String teleportalLocation;
	ArrayList<String>pressurePadsLoc;
	ArrayList<String>RocksLoc;
	

	public Grid(int m, int n,R2D2 r2d2) {
		// note that m is the height of the list and n is the width
		// while addressing the cells 2d array, the format used is cells[y][x]
		// top left corner of the grid is the origin
		obstacleLocation = new ArrayList<String>();
		pressurePadsLoc = new ArrayList<String>();
		RocksLoc = new ArrayList<String>();
		cells = new Cell[m][n];
		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells[i].length; j++) {
				cells[i][j] = new Cell(Type.BLANK);
			}
		}
		int n_rocks_pads = (int) (Math.random() * 2 + 1);
		int n_obstacles = (int) (Math.random() * m * n * 0.25);
		// setting the list size of the r2d2 pads to be the same number of pads
		// on the map
		r2d2.rocks=new int[n_rocks_pads][2];
		
		// adding obstacles
		for (int i = 0; i < n_obstacles; i++) {
			boolean f = true;
			while (f) {
				int x = (int) (Math.random() * n);
				int y = (int) (Math.random() * m);
				if (cells[y][x].type == Type.BLANK) {
					cells[y][x].type = Type.OBSTACLE;
					obstacleLocation.add("obstacle("+x+","+y+").");
					f = false;
				}
			}

		}
		// putting the teleportal
		boolean f = true;
		while (f) {
			int x = (int) (Math.random() * n);
			int y = (int) (Math.random() * m);
			if (cells[y][x].type == Type.BLANK) {
				cells[y][x].type = Type.TELEPORTAL;
				teleportalLocation = "teleportal("+x+","+y+").";
				f = false;
			}
		}
		// putting the pressure pads
		for (int i = 0; i < n_rocks_pads; i++) {
			for (int j = 0; j < 2; j++) {
				 f = true;
				while (f) {
					int x = (int) (Math.random() * n);
					int y = (int) (Math.random() * m);
					if (cells[y][x].type == Type.BLANK) {
						if (j == 1){
							r2d2.rocks[i][0]=y;
							r2d2.rocks[i][1]=x;
							RocksLoc.add("rock("+x+","+y+",s0).");
						}
						else{
							cells[y][x].type = Type.PRESSURE;
							pressurePadsLoc.add("pad("+x+","+y+").");
							}
						f = false;
					}
				}
			}
		}

	}
	// generate simple grid for testing
	public Grid(int m,int n,boolean test){
		cells = new Cell[m][n];
		for(int i= 0; i < cells.length; i++) {
			for(int j = 0; j < cells[i].length; j++) {
				if(i==2&& j==1)
					cells[i][j]=new Cell(Type.TELEPORTAL);
				
				else if(i==2 && j==2)
					cells[i][j]=new Cell(Type.PRESSURE);

				else if(i ==0 && j==0)
					cells[i][j] = new Cell(Type.OBSTACLE);
				else
					cells[i][j] = new Cell(Type.BLANK);
				
			}
		}
	}

	@Override
	public String toString() {
		String s = "";
		for (int i = 0; i < cells.length; i++) {
			s += "[";
			for (int j = 0; j < cells[i].length; j++) {
				s += cells[i][j] + ",";
			}
			s += "]\n";
		}
		return s;
	}

}
