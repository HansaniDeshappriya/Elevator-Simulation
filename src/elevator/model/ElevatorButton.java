package elevator.model;

import elevator.event.ElevatorButtonEvent;
import elevator.event.ElevatorButtonListener;
import elevator.event.MoveElevatorEvent;
import elevator.event.MoveElevatorListener;

public class ElevatorButton implements MoveElevatorListener {
	private ElevatorButtonListener buttonListener = null;

	   
	   private boolean buttonPressed = false;

	   
	   public void setButtonListener( ElevatorButtonListener listener )
	   {
	      buttonListener = listener;
	   }

	   public void elevatorDeparture( MoveElevatorEvent moveEvent ) {}

		  
	   public void elevatorArrival( MoveElevatorEvent moveEvent )
	   {
	      resetButton( moveEvent.getLocation() );
	   }
	   
	   public boolean isButtonPressed()
	   {
	      return buttonPressed;
	   }

	   public void pressButton( Place place1 )
	   {
		   buttonPressed = true;

	      buttonListener.buttonPressed( 
	         new ElevatorButtonEvent( this, place1 ) );
	   }
	   
	   public void resetButton( Place place1 )
	   {
		   buttonPressed = false;

	      buttonListener.buttonReset( 
	         new ElevatorButtonEvent( this, place1 ) );
	   }
	   
   
}
