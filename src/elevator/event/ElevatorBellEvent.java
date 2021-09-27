package elevator.event;

import elevator.model.*;

public class ElevatorBellEvent extends SimulationElevatorEvent {

// Constructor for the BellEvent
	public ElevatorBellEvent( Object source, Place place1 )
	{
		super( source, place1 );
	}
}
