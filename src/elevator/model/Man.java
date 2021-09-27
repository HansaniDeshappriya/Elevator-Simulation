package elevator.model;

import java.util.*;


import elevator.event.*;

public class Man extends Thread {

	   // number of identification
	   private int manID = -1;

	   // Indicates whether the person is on the move or waiting
	   private boolean moving;

	   // a mention of the location (either on Floor or in Elevator)
	   private Place place;

	   // PersonMoveEvents listener object
	   private MovingPersonListener movePersonListener;

	   // time to walk to Button on Floor in milliseconds
	   private static final int TIME_TO_WALK = 3000;

	   // Messages that a person may send
	   public static final int PERSON_CREATED = 1;
	   public static final int PERSON_ARRIVED = 2;
	   public static final int PERSON_ENTERING_ELEVATOR = 3;
	   public static final int PERSON_PRESSING_BUTTON = 4;
	   public static final int PERSON_EXITING_ELEVATOR = 5;
	   public static final int PERSON_EXITED = 6;

	   // The starting location was set by the person constructor
	   public Man( int identifier, Place initialLocation )
	   {
	      super();

	      manID = identifier; // assign a distinct identification
	      place = initialLocation; // decide on a floor
	      moving = true; // Begin heading toward the Button on the Floor
	   }

	   // PersonMoveEvents has a listener set
	   public void setPersonMoveListener(
	   MovingPersonListener listener )
	   {
		   movePersonListener = listener;
	   }
	// Get the identifier
	   public int getID()
	   {
	      return manID;
	   }
	   
	   // Location of the man has been set
	   private void setLocation( Place newplace )
	   {
		   place = newplace;
	   }

	   // obtain the current location
	   private Place getLocation()
	   {
	      return place;
	   }


	   // determine whether or whether the person should move
	   public void setMoving( boolean movingperson )
	   {
	      moving = movingperson;
	   }

	   // determine whether or whether the individual should move
	   public boolean isMoving()
	   {
	      return moving;
	   }

	   // The person either rides the elevator or waits for it
	   public void run()
	   {
	      // signify the creation of a Person thread
	      sendPersonMoveEvent( PERSON_CREATED );

	      // a short walk to the elevator
	      pauseThread( TIME_TO_WALK );
	      
	      // stop walking at ElevatorAt the Elevator, you must come to a halt
	      setMoving( false );

	      // At the Elevator, a person arrived
	      sendPersonMoveEvent( PERSON_ARRIVED );    
	      
	      // obtain a door on the present floor
	      EDoor currentFloorDoor = place.getDoor();
	      
	      // get the Elevator
	      Elevator elevator = 
	         ( (BuildingFloor) getLocation() ).getElevatorShaft().getElevator();

	      // Start getting unique access to currentFloorDoor now
	      synchronized ( currentFloorDoor ) {

	         // to see if the floor door is open
	         if ( !currentFloorDoor.isDoorOpen() ) {

	            sendPersonMoveEvent( PERSON_PRESSING_BUTTON );
	            pauseThread( 1000 );

	            // To request an elevator, push the Floor's Button
	            ElevatorButton floorButton = getLocation().getButton();
	            floorButton.pressButton( getLocation() );
	         }      

	         // awaiting the opening of the floor door
	         try {

	            while ( !currentFloorDoor.isDoorOpen() )
	               currentFloorDoor.wait();
	         }
	         
	         // Waiting for the Floor Door to Open: Handle Exception
	         catch ( InterruptedException interruptedException ) {
	            interruptedException.printStackTrace();
	         }            

	         // It takes one second for the floor door to open
	         pauseThread( 1000 );   

	         // wait for exclusive elevator access implicitly
	         synchronized ( elevator ) { 

	            // wait for exclusive elevator access implicitly
	            sendPersonMoveEvent( PERSON_ENTERING_ELEVATOR );

	            // Elevator should be selected as the person's location
	            setLocation( elevator );

	            // A person enters the elevator in one second
	            pauseThread( 1000 );      

	            // It takes one second to press the elevator button
	            sendPersonMoveEvent( PERSON_PRESSING_BUTTON );
	            pauseThread( 1000 );

	            // Obtain the Elevator's Button
	            ElevatorButton elevatorButton = getLocation().getButton();

	            // Activate the Elevator's Button
	            elevatorButton.pressButton( place );

	            // It takes one second for the door to close
	            pauseThread( 1000 );
	         }    
	         
	      } // unique access to the Floor door

	      // gain access to Elevator on a first-come, first-serve basis
	      synchronized( elevator ) {
	         
	         // get the Elevator door
	         EDoor elevatorDoor = getLocation().getDoor();
	         
	         // awaiting the opening of the elevator door
	         synchronized( elevatorDoor ) {
	 
	            try {
	               
	               while ( !elevatorDoor.isDoorOpen() )
	                  elevatorDoor.wait();
	            }
	            
	            // Waiting for the elevator door to open is an exception that needs to be handled
	            catch ( InterruptedException interruptedException ) {
	               interruptedException.printStackTrace();
	            }       
	            
	            // It takes a second for the elevator door to open
	            pauseThread( 1000 );
	            
	            // Person should be moved to the floor
	            setLocation( elevator.getCurrentFloor() );
	            
	            // Take a step away from the elevator
	            setMoving( true );
	            
	            // Exiting the Elevator
	            sendPersonMoveEvent( PERSON_EXITING_ELEVATOR );
	            
	         } // eject the elevator Door lock that allows the door to be closed
	         
	      } // Release the elevator's lock, letting the person who has been waiting to enter

	      // It takes five seconds to walk from the elevator
	      pauseThread( 2 * TIME_TO_WALK );

	      // A person leaves the simulation
	      sendPersonMoveEvent( PERSON_EXITED );    
	      
	   } // end of method execution
	   
	   // thread is paused for the required length of milliseconds
	   private void pauseThread( int milliseconds )
	   {
	      try {
	         sleep( milliseconds );
	      }

	      // When paused, handle an exception if it is interrupted
	      catch ( InterruptedException interruptedException ) {
	         interruptedException.printStackTrace();
	      }
	   } // end pauseThread method

	   // Depending on the event type, deliver PersonMoveEvent to the listener
	   private void sendPersonMoveEvent( int eventType )
	   {
	      // create a new event
		   MovingPersonEvent event = 
	         new MovingPersonEvent( this, getLocation(), getID() );

	      // Depending on the eventType, send an event to this listener
	      switch ( eventType ) {

	         // Man has been created
	         case PERSON_CREATED:
	        	 movePersonListener.personCreated( event );
	            break;

	         // Man arrived at Elevator
	         case PERSON_ARRIVED:
	        	 movePersonListener.personArrival( event );
	            break;

	         // Man entered Elevator
	         case PERSON_ENTERING_ELEVATOR:
	        	 movePersonListener.personEntered( event );
	            break;

	         // Man pressed Button object
	         case PERSON_PRESSING_BUTTON:
	        	 movePersonListener.personPressedButton( event );
	            break;

	         // Man exited Elevator
	         case PERSON_EXITING_ELEVATOR:
	        	 movePersonListener.personDeparture( event );
	            break;

	         // Man exited simulation
	         case PERSON_EXITED:
	        	 movePersonListener.personExited( event );
	            break;

	         default:
	            break;
	      }
	   } // end method of sendPersonMoveEvent
}
