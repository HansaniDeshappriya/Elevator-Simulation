package elevator.model;

import elevator.event.ElevatorBellEvent;
import elevator.event.ElevatorBellListener;
import elevator.event.MoveElevatorEvent;
import elevator.event.MoveElevatorListener;

public class ElevatorBell implements MoveElevatorListener {

		   
		   private ElevatorBellListener elevatorBellListener;

		  
		   private void ringBell( Place location )
		   {
		      if ( elevatorBellListener != null )
		    	  elevatorBellListener.ringBell( 
		            new ElevatorBellEvent( this, location ) );
		   }
		   
		   public void setBellListener( ElevatorBellListener elevatorListener )
		   {
			   elevatorBellListener = elevatorListener;
		   }
		   
		   public void elevatorArrival( MoveElevatorEvent moveElevatorEvent )
		   {
		      ringBell( moveElevatorEvent.getLocation() );
		   }
		   
		   public void elevatorDeparture( MoveElevatorEvent moveEvent ) {}

		   
		   
}
