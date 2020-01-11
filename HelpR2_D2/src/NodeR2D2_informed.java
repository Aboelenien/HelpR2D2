public class NodeR2D2_informed extends NodeR2D2 {

	public int heuristic_cost;
	public boolean AS;

	public NodeR2D2_informed(R2D2 r2d2, NodeR2D2 parent, Direction direction,
			int depth, int path_cost) {
		super(r2d2, parent, direction, depth, path_cost);
		// TODO Auto-generated constructor stub
	}

	// compareTo to be used in sorting
	public int compareTo(NodeR2D2 node) {
		NodeR2D2_informed nodeI = (NodeR2D2_informed) node;
		if (AS) {
			return (this.heuristic_cost + this.path_cost)
					- (nodeI.heuristic_cost + nodeI.path_cost);
		} else {
			return this.heuristic_cost - nodeI.heuristic_cost;
		}
	}

	public String toString() {
		String s = "";
		NodeR2D2 current = this;
		while (current.parent != null) {
			s = current.direction + " " + s;
			// System.out.println(current.direction);
			current = current.parent;
		}
		return s;

	}

}
