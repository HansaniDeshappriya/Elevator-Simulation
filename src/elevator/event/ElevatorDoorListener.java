package elevator.event;

public interface ElevatorDoorListener {
	
	public void elevatorDoorOpened( ElevatorDoorEvent doorEvent );

	   
	public void elevatorDoorClosed( ElevatorDoorEvent doorEvent );
}
