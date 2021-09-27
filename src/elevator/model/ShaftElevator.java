package elevator.model;

import java.util.*;


import elevator.event.*;

public class ShaftElevator implements MoveElevatorListener,
   ElevatorLightListener, ElevatorBellListener {


   private Elevator elevator;

   private ElevatorLight lightOfFirstFloor;
   private ElevatorLight lightOfSecondFloor;

   
   private ElevatorButton buttonOfFirstFloor;
   private ElevatorButton buttonOfSecondFloor;

   
   private EDoor doorOfFirstFloor;
   private EDoor doorOfSecondFloor;




   private ElevatorDoorListener elevatorDoorListener;
   private ElevatorButtonListener elevatorButtonListener;
   private ElevatorLightListener elevatorLightListener;
   private ElevatorBellListener elevatorbellListener;
   private Set moveElevatorListeners;

  
   public ShaftElevator( BuildingFloor floorOne, BuildingFloor floorTwo )
   {
     
	   moveElevatorListeners = new HashSet( 1 );

     
      ElevatorButtonListener elevatorFloorButtonListener = 
         new ElevatorButtonListener() {

         
         public void buttonPressed( ElevatorButtonEvent elevatorButtonEvent )
         {
            
            Place place1 = elevatorButtonEvent.getLocation();
            elevatorButtonListener.buttonPressed( elevatorButtonEvent );
            elevator.requestElevator( place1 );
         }

         
         public void buttonReset( ElevatorButtonEvent elevatorButtonEvent ) 
         {
        	 elevatorButtonListener.buttonReset( elevatorButtonEvent );
         }
      }; 

     
      buttonOfFirstFloor = new ElevatorButton();
      buttonOfSecondFloor = new ElevatorButton();

   
      buttonOfFirstFloor.setButtonListener( 
    		  elevatorFloorButtonListener );
      buttonOfSecondFloor.setButtonListener( 
    		  elevatorFloorButtonListener );

    
      addElevatorMoveListener( buttonOfFirstFloor );
      addElevatorMoveListener( buttonOfSecondFloor );

      
      ElevatorDoorListener floorDoorListener = new ElevatorDoorListener() {

         
         public void elevatorDoorOpened( ElevatorDoorEvent elevatorDoorEvent )
         {
            
        	 elevatorDoorListener.elevatorDoorOpened( elevatorDoorEvent );
         }

       
         public void elevatorDoorClosed( ElevatorDoorEvent elevatorDoorEvent )
         {
           
        	 elevatorDoorListener.elevatorDoorClosed( elevatorDoorEvent );
         }
      }; 

     
      doorOfFirstFloor = new EDoor();
      doorOfSecondFloor = new EDoor();

     
      doorOfFirstFloor.addDoorListener( floorDoorListener );
      doorOfSecondFloor.addDoorListener( floorDoorListener );

      
      lightOfFirstFloor = new ElevatorLight();
      addElevatorMoveListener( lightOfFirstFloor );
      lightOfFirstFloor.setLightListener( this );
      
      lightOfSecondFloor = new ElevatorLight();
      addElevatorMoveListener( lightOfSecondFloor );
      lightOfSecondFloor.setLightListener( this );

     
      elevator = new Elevator( floorOne, floorTwo );

      
      elevator.addElevatorMoveListener( this );

      
      elevator.setBellListener( this );

      
      elevator.setButtonListener( 
         new ElevatorButtonListener() {

           
            public void buttonPressed( ElevatorButtonEvent buttonEvent )
            {
               
            	elevatorButtonListener.buttonPressed( buttonEvent );
            }

           
            public void buttonReset( ElevatorButtonEvent buttonEvent )
            {
               
            	elevatorButtonListener.buttonReset( 
                  new ElevatorButtonEvent( this, elevator ) );
            }
         } 
      );

     
      
      elevator.setDoorListener( 
         new ElevatorDoorListener() {

            
            public void elevatorDoorOpened( ElevatorDoorEvent elevatorDoorEvent )
            {
             
            	elevatorDoorListener.elevatorDoorOpened( elevatorDoorEvent );
            }

            
            public void elevatorDoorClosed( ElevatorDoorEvent elevatorDoorEvent )
            {
               
            	elevatorDoorListener.elevatorDoorClosed( elevatorDoorEvent );
            }
         } 
      );

      
      elevator.start();

   } 

   
   public Elevator getElevator()
   {
      return elevator;
   }

   public ElevatorButton getFirstFloorButton()
   {
      return buttonOfFirstFloor;
   }

   
   public ElevatorButton getSecondFloorButton()
   {
      return buttonOfSecondFloor;
   }

   
   public EDoor getFirstFloorDoor()
   {
      return doorOfFirstFloor;
   }

  
   public EDoor getSecondFloorDoor()
   {
      return doorOfSecondFloor;
   }

  
   
   public ElevatorLight getFirstFloorLight()
   {
      return lightOfFirstFloor;
   }

   
   public ElevatorLight getSecondFloorLight()
   {
      return lightOfSecondFloor;
   }

   
   public void ringBell( ElevatorBellEvent elevatorDoorEvent )
   {
	   elevatorbellListener.ringBell( elevatorDoorEvent );
   }

   public void lightTurnedOff( ElevatorLightEvent elevatorDoorEvent )
   {
	   elevatorLightListener.lightTurnedOff( elevatorDoorEvent );
   }
   
   public void lightTurnedOn( ElevatorLightEvent elevatorDoorEvent )
   {
	   elevatorLightListener.lightTurnedOn( elevatorDoorEvent );
   }

   
   public void elevatorDeparture( MoveElevatorEvent elevatorMoveEvent )
   {
      Iterator iterator = moveElevatorListeners.iterator();

    
      while ( iterator.hasNext() ) {

         
    	  MoveElevatorListener listener = 
            ( MoveElevatorListener ) iterator.next();

        
         listener.elevatorDeparture( elevatorMoveEvent );
      }
   } 

   
   public void elevatorArrival( MoveElevatorEvent elevatorMoveEvent )
   {
      
      Iterator iterator = moveElevatorListeners.iterator();

      
      while ( iterator.hasNext() ) {

         
    	  MoveElevatorListener listener = 
            ( MoveElevatorListener ) iterator.next();

        
         listener.elevatorArrival( elevatorMoveEvent );

      } 
   } 

   
   public void setDoorListener( ElevatorDoorListener elevatorListener )
   {
      elevatorDoorListener = elevatorListener;
   }

   
   public void setButtonListener( ElevatorButtonListener elevatorListener )
   {
	   elevatorButtonListener = elevatorListener;
   }

   
   public void addElevatorMoveListener( 
		   MoveElevatorListener elevatorListener )
   {
	   moveElevatorListeners.add( elevatorListener );
   }

   
   public void setLightListener( ElevatorLightListener elevatorListener )
   {
	   elevatorLightListener = elevatorListener;
   }

   
   public void setBellListener( ElevatorBellListener elevatorListener )
   {
	   elevatorbellListener = elevatorListener;
   }
}
