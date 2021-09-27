package elevator.event;

public interface ElevatorLightListener {
	
	public void lightTurnedOn( ElevatorLightEvent lightEvent );

	public void lightTurnedOff( ElevatorLightEvent lightEvent );
}
