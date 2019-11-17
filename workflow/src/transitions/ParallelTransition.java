package transitions;

import java.util.ArrayList;
import java.util.List;

public class ParallelTransition implements Transition {
	private String startState;
	private List<String> states;

	public ParallelTransition(String startState) {
		this.startState = startState;
		states = new ArrayList<>();
	}

	public void addState(String state) {
		if(state != null) {
			states.add(state);
		}
	}

	public String getStartState() {
		return startState;
	}

	public void setStartState(String startState) {
		this.startState = startState;
	}

	public List<String> getStates() {
		return states;
	}

	public void setStates(List<String> states) {
		this.states = states;
	}
	
	
}
