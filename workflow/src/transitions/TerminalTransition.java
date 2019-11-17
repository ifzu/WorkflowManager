package transitions;

public class TerminalTransition implements Transition{
	private String terminalTrans;

	public TerminalTransition(String terminalTrans) {
		super();
		this.terminalTrans = terminalTrans;
	}

	public String getTerminalTrans() {
		return terminalTrans;
	}

	public void setTerminalTrans(String terminalTrans) {
		this.terminalTrans = terminalTrans;
	}

}
