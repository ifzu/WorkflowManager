package transitions;

public class SimpleTransition implements Transition{
	private String startState;
	private String finishState;
	
	public SimpleTransition(String startState, String finishState) {
		super();
		this.startState = startState;
		this.finishState = finishState;
	}

	public String getStartState() {
		return startState;
	}

	public void setStartState(String startState) {
		this.startState = startState;
	}

	public String getFinishState() {
		return finishState;
	}

	public void setFinishState(String finishState) {
		this.finishState = finishState;
	}

}
