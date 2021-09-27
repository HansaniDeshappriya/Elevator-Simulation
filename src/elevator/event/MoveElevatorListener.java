package elevator.event;

public interface MoveElevatorListener {

	public void elevatorArrival( MoveElevatorEvent moveEvent );
	
	public void elevatorDeparture( MoveElevatorEvent moveEvent );

	   
}
