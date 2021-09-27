package elevator.model;

import java.util.*;

import elevator.event.*;
import elevator.Constants;

public class Elevator extends Place implements Runnable, 
   ElevatorBellListener, Constants {

   
   private boolean runningElevator = false;

   
   private boolean moving = false;

   
   private Place currentFloorPlace;

   
   private Place destinationFloorPlace;
   
   
   private boolean summoned;
   
   
   private Set moveElevatorListeners;
   private ElevatorBellListener ElevatorbellListener;
   private ElevatorButtonListener buttonElevatorListener;
   private ElevatorDoorListener doorElevatorListener;

   
   private DoorElevator doorOfElevator;
   private ElevatorButton buttonOfElevator;
   private ElevatorBell elevatorBell;

   public static final int ONE_SECOND = 1000;

  
   private static final int TRAVEL_TIME = 5 * ONE_SECOND;

   
   private Thread thread;

   
   public Elevator( BuildingFloor firstFloor, BuildingFloor secondFloor )
   {
      setVenueName( ELEVATOR_NAME );

      
      doorOfElevator = new DoorElevator();
      buttonOfElevator = new ElevatorButton();
      elevatorBell = new ElevatorBell();

     
      elevatorBell.setBellListener( this );

      
      moveElevatorListeners = new HashSet( 1 );

     
      currentFloorPlace = firstFloor;
      destinationFloorPlace = secondFloor;

      
      addElevatorMoveListener( buttonOfElevator );

     
      addElevatorMoveListener( doorOfElevator );

      
      addElevatorMoveListener( elevatorBell );

      
      buttonOfElevator.setButtonListener( 
         new ElevatorButtonListener() {

          
            public void buttonPressed( ElevatorButtonEvent buttonEvent )
            {
              
            	buttonElevatorListener.buttonPressed( 
                  buttonEvent );

               
               setMoving( true );
            }

            
            public void buttonReset( ElevatorButtonEvent buttonEvent )
            {
               
            	buttonElevatorListener.buttonReset( 
                  buttonEvent );
            }
         }
      );

     
      doorOfElevator.addDoorListener( 
         new ElevatorDoorListener() {

            
            public void elevatorDoorOpened( ElevatorDoorEvent doorEvent )
            {
               
            	doorElevatorListener.elevatorDoorOpened( new ElevatorDoorEvent( 
                  doorEvent.getSource(), Elevator.this ));
            }

            
            public void elevatorDoorClosed( ElevatorDoorEvent doorEvent )
            {
               
            	doorElevatorListener.elevatorDoorClosed( new ElevatorDoorEvent( 
                 doorEvent.getSource(), Elevator.this ));
            }
         } 
      );
   } 

  
   private void changeFloors()
   {
      Place location = currentFloorPlace;
      currentFloorPlace = destinationFloorPlace;
      destinationFloorPlace = location;
   }

   public void run()
   {
      while ( isElevatorRunning() ) {
         
         
         while ( !isMoving() )
            pauseThread( 10 );
         
         
         pauseThread( ONE_SECOND );
         
         
         getDoor().closeDoor( currentFloorPlace );         

         
         pauseThread( ONE_SECOND );

        
         sendDepartureEvent( currentFloorPlace );

         
         pauseThread( TRAVEL_TIME );

        
         setMoving( false );

        
         changeFloors();

         
         sendArrivalEvent( currentFloorPlace );

      } 

   } 
   
   public void start()
   {
      if ( thread == null )
         thread = new Thread( this );

      runningElevator = true;
      thread.start();
   }

  
   public void stopElevator()
   {
	   runningElevator = false;
   }

  

   
   
   private void pauseThread( int milliseconds )
   {
      try {
         Thread.sleep( milliseconds );
      }

      
      catch ( InterruptedException exception ) {
         exception.printStackTrace();
      }
   } 

   public EDoor getDoor()
   {
      return doorOfElevator;
   }
   
   public ElevatorButton getButton()
   {
      return buttonOfElevator;
   }

   
   private void setMoving( boolean elevatorMoving )
   {
      moving = elevatorMoving;
   }

   
   public boolean isMoving()
   {
      return moving;
   }
 

   private boolean isElevatorRunning()
   {
      return runningElevator;
   }

   public void setButtonListener( ElevatorButtonListener listener )
   {
	   buttonElevatorListener = listener;
   }
   
   public void setDoorListener( ElevatorDoorListener listener )
   {
	   doorElevatorListener = listener;
   }
   
   public void setBellListener( ElevatorBellListener listener )
   {
	   ElevatorbellListener = listener;
   }

  
  
   public void addElevatorMoveListener( 
		   MoveElevatorListener listener )
   {
	   moveElevatorListeners.add( listener );
   }

   private void sendDepartureEvent( Place location )
   {
      
      Iterator iterator = moveElevatorListeners.iterator();

     
      while ( iterator.hasNext() ) {

        
    	  MoveElevatorListener listener = 
            ( MoveElevatorListener ) iterator.next();

         
         listener.elevatorDeparture( new MoveElevatorEvent( 
            this, currentFloorPlace ) );

      } 
   } 

   private void sendArrivalEvent( Place place1 )
   {
      
      Iterator iterator = moveElevatorListeners.iterator();

      
      while ( iterator.hasNext() ) {

         
    	  MoveElevatorListener listener = 
            ( MoveElevatorListener ) iterator.next();

       
         listener.elevatorArrival( new 
        		 MoveElevatorEvent( this, place1 ) );

      }

     
      if ( summoned ) {
         setMoving( true ); 
      }

      summoned = false; 

   } 
  
   public void requestElevator( Place location )
   {
     
      if ( !isMoving() ) {

       
         if ( location == currentFloorPlace )

            
            sendArrivalEvent( currentFloorPlace );

         
         else {
            setMoving( true ); 
         }
      }
      else 

        
         if ( location == currentFloorPlace )
            summoned = true;

   } 

 
   public Place getCurrentFloor()
   {
      return currentFloorPlace;
   }

   public void ringBell( ElevatorBellEvent bellEvent )
   {
      
      if ( ElevatorbellListener != null )
    	  ElevatorbellListener.ringBell( bellEvent );
   }

}
