import java.util.Arrays;


public class HelpR2D2 extends search_problem{

	Direction [] operators={Direction.NORTH, Direction.SOUTH,Direction.EAST, Direction.WEST };
	
	public HelpR2D2(){
		R2D2 x  = new R2D2();
		x.moves=0;
		initial_state=x;
		
	}
	//initializing the state of problem
	public void setInitialState(){
		boolean f = true;
		while (f) {
			int x = (int) (Math.random() * Execute.grid.cells[0].length);
			int y = (int) (Math.random() * Execute.grid.cells.length);
			R2D2 r2d2=(R2D2)initial_state;
			if (Execute.grid.cells[y][x].type == Type.BLANK) {
				boolean rockLocation = false;
				for (int i = 0; i < r2d2.rocks.length; i++) {
					rockLocation |= y == r2d2.rocks[i][0]
							&& x == r2d2.rocks[i][1];
				}
				if (!rockLocation) {
					r2d2.location[0] = y;
					r2d2.location[1] = x;
					f = false;
				}
			}
		}
	}
	
	// returning state after adding an operator on it
	public Object operation(Object in,Direction operator){
		R2D2 r2d2 = (R2D2)in;
		int[] target = target(r2d2.location, operator);
		Cell[][] cells = Execute.grid.cells;
		try {
			if (cells[target[0]][target[1]].type == Type.OBSTACLE)
				return r2d2;
			for (int i = 0; i < r2d2.rocks.length; i++) {
				if (compareArray(target, r2d2.rocks[i])) {
					int[] rockTarget = target(target, operator);
					if (cells[rockTarget[0]][rockTarget[1]].type == Type.OBSTACLE 
							||cells[rockTarget[0]][rockTarget[1]].type ==Type.TELEPORTAL)
						return r2d2;
					for (int j = 0; j < r2d2.rocks.length; j++)
						if (compareArray(rockTarget, r2d2.rocks[j]))
							return r2d2;
					r2d2.rocks[i][0] = rockTarget[0];
					r2d2.rocks[i][1] = rockTarget[1];
				}

			}
			r2d2.location = Arrays.copyOf(target, target.length);
		} catch (IndexOutOfBoundsException e) {
		}
		return r2d2;
	}
	
	public static boolean compareArray(int[] a, int[] b) {
		if (a[0] == b[0])
			if (a[1] == b[1])
				return true;
		return false;
	}
	
	// using the target method for the movement of the state
	public int[] target(int[] current, Direction direction) {
		int[] target = Arrays.copyOf(current, current.length);
		switch (direction) {
		case NORTH:
			target[0]--;
			break;
		case SOUTH:
			target[0]++;
			break;
		case EAST:
			target[1]++;
			break;
		case WEST:
			target[1]--;
			break;
		}
		return target;
	}
	
	// implementing the goal test where all rock on pads and r2d2 on teleportal
	public boolean goalTest(Object state) {
		R2D2 r2d2 = (R2D2)state;
		for (int i = 0; i < r2d2.rocks.length; i++) {
			if (Execute.grid.cells[r2d2.rocks[i][0]][r2d2.rocks[i][1]].type != Type.PRESSURE) {
				return false;
			}
		}
		return (Execute.grid.cells[r2d2.location[0]][r2d2.location[1]].type == Type.TELEPORTAL);
	}
	
	// generating heuristic cost for the state
	public int path_cost(Object state){
		R2D2 r2d2 = (R2D2)state;
		return r2d2.moves;
	}
	
	// heuristic function 1 (distance from state to Teleportal)
	public int generateHeuristicFun1(R2D2 r2d2) {
		int[] teleportalPos = new int[2];
		for (int i = 0; i < Execute.grid.cells.length; i++) {
			for (int j = 0; j < Execute.grid.cells[i].length; j++) {
				if (Execute.grid.cells[i][j].type == Type.TELEPORTAL) {
					teleportalPos[0] = i;
					teleportalPos[1] = j;
				}
			}
		}
		return Math.abs(r2d2.location[1] - teleportalPos[1])
				+ Math.abs(r2d2.location[0] - teleportalPos[0]);
	}
	
	// generate Heuristic Function 2
	public int generateHeuristicFun2(R2D2 r2d2) {
		// getting the pads position
		int[][] padsPosition = new int[r2d2.rocks.length][r2d2.rocks[0].length];
		int padsCounter = 0;
		for (int i = 0; i < Execute.grid.cells.length; i++) {
			for (int j = 0; j < Execute.grid.cells[i].length; j++) {
				if (Execute.grid.cells[i][j].type == Type.PRESSURE) {
					padsPosition[padsCounter][0] = i;
					padsPosition[padsCounter][1] = j;
					padsCounter++;
				}
			}
		}
		// calculating the sum of distance of the rocks and their nearest pads
		int[] sum = new int[padsCounter];
		for (int i = 0; i < r2d2.rocks.length; i++) {
			int MinI = 400;
			for (int j = 0; j < padsPosition.length; j++) {
				int deltaX = Math.abs(r2d2.rocks[i][1] - padsPosition[j][1]);
				int deltaY = Math.abs(r2d2.rocks[i][0] - padsPosition[j][0]);
				MinI = Math.min(MinI, deltaX + deltaY);
			}
			sum[i] = MinI;
		}
		int h = 0;
		for (int i = 0; i < sum.length; i++) {
			h += sum[i];
		}
		// adding to the sum the distance from the state to the teleportal
		return h+generateHeuristicFun1(r2d2);
	}

}
