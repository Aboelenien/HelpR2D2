import java.util.ArrayList;

public class NodeR2D2 extends SearchNode implements Comparable<NodeR2D2>  {
	public NodeR2D2 parent;
	
	public static ArrayList<R2D2> previous = new ArrayList<R2D2>();
	
	public NodeR2D2(R2D2 r2d2,NodeR2D2 parent,Direction direction,int depth,int path_cost){
		this.parent=parent;
		this.path_cost = path_cost;
		this.depth= depth;
		this.direction = direction;
		this.r2d2 = r2d2;
	}
	
	// compare previous to use it for not returning to the same state
	public boolean comparePrevious(){
		R2D2 temp = (R2D2)r2d2;
		for(int i=0;i<previous.size();i++){
			if(temp.compareR2D2(previous.get(i))){
				return true;
			}
		}
		previous.add(temp);
		return false;
	}

	public String toString(){
		String s = "";
		NodeR2D2 current = this;
		while(current.parent !=null){
			s = current.direction +" "+ s;
			//System.out.println(current.direction);
			current=current.parent;
		}
		return s;
		
	}

	// compareTo to be used in sorting
	@Override
	public int compareTo(NodeR2D2 node) {
		return path_cost - node.path_cost;
	}
	
	
	
}
