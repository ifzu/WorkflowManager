package transitions;

public class ConditionalTransition implements Transition{
	private String startState;
	private String pathA;
	private String pathB;
	private boolean flagPathA;
	private boolean flagPathB;
	
	public ConditionalTransition(String startState, String pathA, String pathB, boolean flagPathA, boolean flagPathB) {
		super();
		this.startState = startState;
		this.pathA = pathA;
		this.pathB = pathB;
		this.flagPathA = flagPathA;
		this.flagPathB = flagPathB;
	}
	public String getStartState() {
		return startState;
	}
	public void setStartState(String startState) {
		this.startState = startState;
	}
	public String getPathA() {
		return pathA;
	}
	public void setPathA(String pathA) {
		this.pathA = pathA;
	}
	public String getPathB() {
		return pathB;
	}
	public void setPathB(String pathB) {
		this.pathB = pathB;
	}
	public boolean isFlagPathA() {
		return flagPathA;
	}
	public void setFlagPathA(boolean flagPathA) {
		this.flagPathA = flagPathA;
	}
	public boolean isFlagPathB() {
		return flagPathB;
	}
	public void setFlagPathB(boolean flagPathB) {
		this.flagPathB = flagPathB;
	}
	
	

	
}
