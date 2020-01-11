
public abstract class search_problem {
	
	@SuppressWarnings("rawtypes")
	Enum [] operators;
	Object initial_state;
	public abstract Object operation(Object state,Direction operator);
	public abstract boolean goalTest(Object state);
	public abstract int path_cost(Object state);
}
