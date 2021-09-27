package elevator.event;

import elevator.model.*;

public class ElevatorDoorEvent extends SimulationElevatorEvent {

	   
	   public ElevatorDoorEvent( Object source, Place place1 )
	   {
	      super( source, place1 );
	   }

}
