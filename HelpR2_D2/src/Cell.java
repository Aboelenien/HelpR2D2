
public class Cell {
	public Type type;

	public Cell(Type type) {
		this.type = type;
	}

	@Override
	public String toString() {
		String s="";
		switch(type){
		case PRESSURE: s="PRES"; break;
		case BLANK: s="BLNK"; break;
		case TELEPORTAL: s="TELE"; break;
		case OBSTACLE: s="OBST"; break;
		default:
			break;
		
		}
		return s;
	}

}
