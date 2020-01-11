
import java.util.Arrays;

public class R2D2 implements Cloneable {
	int[] location;
	int[][] rocks;
	int moves;

	public R2D2() {
		location = new int[2];
	}

	public static boolean compareArray(int[] a, int[] b) {
		if (a[0] == b[0])
			if (a[1] == b[1])
				return true;
		return false;
	}

	// use it to prevent returning to previous state
	public boolean compareR2D2(R2D2 r){
		if(compareArray(location, r.location)){
			for(int i=0;i<rocks.length;i++){
				if(!compareArray(rocks[i], r.rocks[i])){
					return false;
				}
			}
			return true;
		}
		return false;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		R2D2 r2d2 = new R2D2();
		r2d2.location = Arrays.copyOf(location, location.length);
		r2d2.rocks = new int[rocks.length][2];
		r2d2.moves =this.moves;
		for (int i = 0; i < rocks.length; i++) {
			r2d2.rocks[i] = Arrays.copyOf(rocks[i], rocks[i].length);
		}
		return r2d2;
	}

	@Override
	public String toString() {
		String s = "[";
		for (int i = 0; i < rocks.length; i++)
			s += "(" + rocks[i][0] + "," + rocks[i][1] + ")";
		s += "]";
		return " R2D2 position: [" + location[0] + "," + location[1] + "], Rocks:" + s;
	}

}
