package elevator.event;

public interface ElevatorButtonListener {
	public void buttonPressed( ElevatorButtonEvent buttonEvent );

	  
	   public void buttonReset( ElevatorButtonEvent buttonEvent );
}
