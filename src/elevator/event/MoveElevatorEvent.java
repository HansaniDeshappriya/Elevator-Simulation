package elevator.event;

import elevator.model.*;

public class MoveElevatorEvent extends SimulationElevatorEvent {

	public MoveElevatorEvent( Object source, Place place1 )
	   {
	      super( source, place1 );
	   }
}
