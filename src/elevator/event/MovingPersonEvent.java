package elevator.event;

import elevator.model.*;

public class MovingPersonEvent extends SimulationElevatorEvent {

	  
	   private int eventID;

	   
	   public MovingPersonEvent( Object source, Place place1,
	      int identifier )
	   {
	      super( source, place1 );
	      eventID = identifier;
	   }

	  
	   public int geteventID()
	   {
	      return( eventID );
	   }

}
