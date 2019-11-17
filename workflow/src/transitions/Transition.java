package transitions;

public interface Transition {
	
	default boolean transitionType(String type) {
		if (this.getClass().getSimpleName().equalsIgnoreCase(type))
			return true;
		return false;
	}
}
