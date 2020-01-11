import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class Execute {
	public static Grid grid;
	public ArrayList<NodeR2D2> nodes;
	public ArrayList<NodeR2D2_informed> nodes_informed;
	public HelpR2D2 helpr2d2;
	int counterNodes;

	public Execute() {
		helpr2d2 = new HelpR2D2();
		genGrid();
		// genTestGrid();
		nodes = new ArrayList<NodeR2D2>();
		nodes_informed = new ArrayList<NodeR2D2_informed>();

	}
	// generating the random grid
	private void genGrid() {
		int m = (int) (Math.random() * 4 + 3);
		int n = (int) (Math.random() * 4 + 3);
		grid = new Grid(m, n, (R2D2) helpr2d2.initial_state);
		File file = new File("KB1.txt");
		try {
			file.createNewFile();
			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.append(grid.teleportalLocation);
			bw.newLine();
			for (int i = 0; i < grid.obstacleLocation.size(); i++) {
				bw.append(grid.obstacleLocation.get(i));
				bw.newLine();
			}
			for (int i = 0; i < grid.pressurePadsLoc.size(); i++) {
				bw.append(grid.pressurePadsLoc.get(i));
				bw.newLine();
			}
			for (int i = 0; i < grid.RocksLoc.size(); i++) {
				bw.append(grid.RocksLoc.get(i));
				bw.newLine();
			}
			helpr2d2.setInitialState();
			R2D2 temp = (R2D2)helpr2d2.initial_state;
			String r2d2Loc = "r2d2("+temp.location[1]+","+temp.location[0]+",s0).";
			bw.append(r2d2Loc);
			bw.newLine();
			bw.append("wallLeft(0).");
			bw.newLine();
			bw.append("wallUp(0).");
			bw.newLine();
			bw.append("wallDown("+m+").");
			bw.newLine();
			bw.append("wallRight("+n+").");
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	// generating the test grid
	public void genTestGrid() {
		int m = 5;
		int n = 5;
		grid = new Grid(m, n, true);
		// helpr2d2.initial_state = new R2D2();
		// helpr2d2.initial_state.location[0] = 2;
		// helpr2d2.initial_state.location[1] = 4;
		// helpr2d2.initial_state.rocks = new int[1][2];
		// helpr2d2.initial_state.rocks[0][0] = 1;
		// helpr2d2.initial_state.rocks[0][1] = 1;
	}
	
	public NodeR2D2 search(Grid grid, Strategy strategy, boolean visualize)
			throws CloneNotSupportedException {
		counterNodes = 0;
		nodes.clear();
		nodes_informed.clear();
		NodeR2D2.previous.clear();
		// storing the intial state in the arraylist of nodes
		NodeR2D2 n = new NodeR2D2((R2D2) helpr2d2.initial_state, null, null, 0,
				0);
		NodeR2D2_informed n_informed = new NodeR2D2_informed(
				(R2D2) helpr2d2.initial_state, null, null, 0, 0);
		nodes_informed.add(n_informed);
		System.out.println(helpr2d2.initial_state);
		System.out.println(Execute.grid);
		nodes.add(n);
		switch (strategy) {
		case DF:
		case BF:
		case UC:
			while (!nodes.isEmpty()) {
				NodeR2D2 removed = nodes.remove(0);
				// counting nodes expanded
				counterNodes++;
				R2D2 current = (R2D2) removed.r2d2;
				if (visualize) {
					System.out.println(removed.r2d2);
					System.out.println(grid);
				}

				if (helpr2d2.goalTest(current)) {
					System.out.println("Success!");
					return removed;
				}
				expand(removed, strategy, 0);
			}
			break;
		case ID:
			for (int limit = 0; limit < Integer.MAX_VALUE; limit++) {
				// the limit Keeps increasing until finding solution
				while (!nodes.isEmpty()) {
					NodeR2D2 removed = nodes.remove(0);
					// counting nodes expanded
					counterNodes++;
					R2D2 current = (R2D2) removed.r2d2;
					if (helpr2d2.goalTest(current)) {
						System.out.println("Success at limit=" + limit + " !");
						return removed;
					}
					if (visualize) {
						System.out.println(removed.r2d2);
						System.out.println(grid);
					}
					expand(removed, strategy, limit);

				}
				NodeR2D2.previous.clear();
				nodes.clear();
				nodes.add(n);
			}
			break;
		case GR1:
		case GR2:
			while (!nodes_informed.isEmpty()) {
				NodeR2D2_informed removed = (NodeR2D2_informed) nodes_informed
						.remove(0);
				counterNodes++;
				// counting nodes expanded
				R2D2 current = (R2D2) removed.r2d2;
				if (visualize) {
					System.out.println(removed.r2d2);
					System.out.println(grid);
				}

				if (helpr2d2.goalTest(current)) {
					System.out.println("Success!");
					return removed;
				}
				expand(removed, strategy, 0);

			}
			break;

		case AS1:
		case AS2:
			while (!nodes_informed.isEmpty()) {
				NodeR2D2_informed removed = (NodeR2D2_informed) nodes_informed
						.remove(0);
				counterNodes++;
				// counting nodes expanded
				R2D2 current = (R2D2) removed.r2d2;
				if (visualize) {
					System.out.println(removed.r2d2);
					System.out.println(grid);
				}
				if (helpr2d2.goalTest(current)) {
					System.out.println("Success!");
					return removed;
				}
				// System.out.println(removed);
				expand(removed, strategy, 0);
			}
			break;

		default:
			return null;
		}

		System.out.println("Fail");
		return null;
	}
	
	// expanding every to with the each element in the list of operators
	public void expand(NodeR2D2 removed, Strategy strategy, int limit)
			throws CloneNotSupportedException {
		R2D2 current = (R2D2) removed.r2d2;
		switch (strategy) {
		case DF:
		case BF:
		case UC:
			for (int i = 0; i < helpr2d2.operators.length; i++) {
				R2D2 oldState = (R2D2) current.clone();
				R2D2 newState = (R2D2)helpr2d2.operation(oldState, helpr2d2.operators[i]);
				newState.moves = oldState.moves + 1;
				NodeR2D2 newNode = new NodeR2D2(newState, removed,
						helpr2d2.operators[i], removed.depth + 1,
						helpr2d2.path_cost(newState));
				if (!newNode.comparePrevious())
					add(newNode, 0, strategy);
			}
			break;
		case ID:
			for (int i = 0; i < helpr2d2.operators.length; i++) {
				R2D2 oldState = (R2D2) current.clone();
				R2D2 newState = (R2D2)helpr2d2.operation(oldState, helpr2d2.operators[i]);
				newState.moves = oldState.moves + 1;
				NodeR2D2 newNode = new NodeR2D2(newState, removed,
						helpr2d2.operators[i], removed.depth + 1,
						helpr2d2.path_cost(newState));
				if (!newNode.comparePrevious())
					add(newNode, limit, strategy);
			}
			break;
		case GR1:
		case GR2:
			for (int i = 0; i < helpr2d2.operators.length; i++) {
				R2D2 oldState = (R2D2) current.clone();

				R2D2 newState = (R2D2)helpr2d2.operation(oldState, helpr2d2.operators[i]);
				newState.moves = oldState.moves + 1;
				NodeR2D2_informed newNode = new NodeR2D2_informed(newState,
						removed, helpr2d2.operators[i], removed.depth + 1,
						helpr2d2.path_cost(newState));
				if (strategy == Strategy.GR1)
					newNode.heuristic_cost = helpr2d2
							.generateHeuristicFun1(newState);
				else
					newNode.heuristic_cost = helpr2d2
							.generateHeuristicFun2(newState);
				newNode.AS = false;
				if (!newNode.comparePrevious())
					add(newNode, 0, strategy);
			}
			break;
		case AS1:
		case AS2:
			for (int i = 0; i < helpr2d2.operators.length; i++) {
				R2D2 oldState = (R2D2) current.clone();
				R2D2 newState = (R2D2)helpr2d2.operation(oldState, helpr2d2.operators[i]);
				newState.moves = oldState.moves + 1;
				NodeR2D2_informed newNode = new NodeR2D2_informed(newState,
						removed, helpr2d2.operators[i], removed.depth + 1,
						removed.path_cost + 1);
				if (strategy == Strategy.AS1)
					newNode.heuristic_cost = helpr2d2
							.generateHeuristicFun1(newState);
				else
					newNode.heuristic_cost = helpr2d2
							.generateHeuristicFun2(newState);
				newNode.AS = true;
				if (!newNode.comparePrevious())
					add(newNode, 0, strategy);
			}
			break;
		default:
			break;
		}
	}

	// limit is only used in Iterative Deepening
	public void add(NodeR2D2 node, int limit, Strategy strategy) {
		switch (strategy) {
		case BF:
			nodes.add(node);
			break;
		case DF:
			// adding in first positions
			nodes.add(0, node);
			break;
		case ID:
			if (limit >= node.depth)
				nodes.add(0, node);
			break;
		case UC:
			nodes.add(node);
			// sorting the arraylist using compareTo which was implemented in NodeR2D2
			Collections.sort(nodes);
			break;
		case GR1:
		case GR2:
		case AS1:
		case AS2:
			// sorting the arraylist using compareTo which was implemented in NodeR2D2_informed 
			nodes_informed.add((NodeR2D2_informed) node);
			Collections.sort(nodes_informed);
			break;
		default:
			break;

		}
	}

	public static void main(String[] args) throws CloneNotSupportedException {
		Execute exe = new Execute();
//		System.out.println("BF");
//		NodeR2D2 res = exe.search(Execute.grid, Strategy.BF, false);
//		System.out.println(res);
//		if (!(res == null)) {
//			System.out.println("Path Cost: " + res.path_cost);
//			System.out.println("Number of nodes expanded: " + exe.counterNodes);
//		}
//		System.out.println("DF");
//		res = exe.search(Execute.grid, Strategy.DF, false);
//		System.out.println(res);
//		if (!(res == null)) {
//			System.out.println("Path Cost: " + res.path_cost);
//			System.out.println("Number of nodes expanded: " + exe.counterNodes);
//		}
//		System.out.println("UC");
//		res = exe.search(Execute.grid, Strategy.UC, false);
//		System.out.println(res);
//		if (!(res == null)) {
//			System.out.println("Path Cost: " + res.path_cost);
//			System.out.println("Number of nodes expanded: " + exe.counterNodes);
//		}
//		System.out.println("ID");
//		res = exe.search(Execute.grid, Strategy.ID, false);
//		System.out.println(res);
//		if (!(res == null)) {
//			System.out.println("Path Cost: " + res.path_cost);
//			System.out.println("Number of nodes expanded: " + exe.counterNodes);
//		}
//		System.out.println("GR1");
//		res = exe.search(Execute.grid, Strategy.GR1, false);
//		System.out.println(res);
//		if (!(res == null)) {
//			System.out.println("Path Cost: " + res.path_cost);
//			System.out.println("Number of nodes expanded: " + exe.counterNodes);
//		}
//		System.out.println("GR2");
//		res = exe.search(Execute.grid, Strategy.GR2, false);
//		System.out.println(res);
//		if (!(res == null)) {
//			System.out.println("Path Cost: " + res.path_cost);
//			System.out.println("Number of nodes expanded: " + exe.counterNodes);
//		}
//		System.out.println("AS1");
//		res = exe.search(Execute.grid, Strategy.AS1, false);
//		System.out.println(res);
//		if (!(res == null)) {
//			System.out.println("Path Cost: " + res.path_cost);
//			System.out.println("Number of nodes expanded: " + exe.counterNodes);
//		}
//		System.out.println("AS2");
//		res = exe.search(Execute.grid, Strategy.AS2, false);
//		System.out.println(res);
//		if (!(res == null)) {
//			System.out.println("Path Cost: " + res.path_cost);
//			System.out.println("Number of nodes expanded: " + exe.counterNodes);
//		}
		
	}
}
