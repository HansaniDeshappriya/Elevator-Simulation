package elevator.model;

import java.util.*;


import elevator.event.*;
import elevator.Constants;

public class SimulationElevator implements SimulationElevatorListener,
   Constants {

   
   private BuildingFloor firstFloor;
   private BuildingFloor secondFloor;

   
   private ShaftElevator elevatorShaft;

  
   private Set personMoveListeners;
   private ElevatorDoorListener doorListener;
   private ElevatorButtonListener buttonListener;
   private ElevatorLightListener lightListener;
   private ElevatorBellListener bellListener;
   private MoveElevatorListener elevatorMoveListener;

   
   private int numberOfPeople = 0;

  
   public SimulationElevator()
   {
      
      firstFloor = new BuildingFloor( FIRST_FLOOR_NAME );
      secondFloor = new BuildingFloor( SECOND_FLOOR_NAME );

      
      elevatorShaft =
         new ShaftElevator( firstFloor, secondFloor );

     
      firstFloor.setElevatorShaft( elevatorShaft );
      secondFloor.setElevatorShaft( elevatorShaft );

     
      elevatorShaft.setDoorListener( this );
      elevatorShaft.setButtonListener( this );
      elevatorShaft.addElevatorMoveListener( this );
      elevatorShaft.setLightListener( this );
      elevatorShaft.setBellListener( this );

      
      personMoveListeners = new HashSet( 1 );

   } 

   
   private BuildingFloor getFloor( String name )
   {
      if ( name.equals( FIRST_FLOOR_NAME ) )
         return firstFloor;
      else

         if ( name.equals( SECOND_FLOOR_NAME ) )
            return secondFloor;
         else
            return null;

   } 

 
   public void addPerson( String floorName )
   {
      
      Man person = 
         new Man( numberOfPeople, getFloor( floorName ) );
      person.setName( Integer.toString( numberOfPeople ) );

      
      person.setPersonMoveListener( this );

      
      person.start();

      
      numberOfPeople++;

   } 

   
   public void elevatorDeparture( MoveElevatorEvent moveEvent )
   {
      elevatorMoveListener.elevatorDeparture( moveEvent );
   }

   
   public void elevatorArrival( MoveElevatorEvent moveEvent )
   {
      elevatorMoveListener.elevatorArrival( moveEvent );
   }

   
   private void sendPersonMoveEvent( 
      int eventType, MovingPersonEvent event )
   {
      Iterator iterator = personMoveListeners.iterator();

      while ( iterator.hasNext() ) {

    	  MovingPersonListener listener = 
            ( MovingPersonListener ) iterator.next();

       
         switch ( eventType ) {

            
            case Man.PERSON_CREATED:
               listener.personCreated( event );
               break;

           
            case Man.PERSON_ARRIVED:
               listener.personArrival( event );
               break;

            
            case Man.PERSON_ENTERING_ELEVATOR:
               listener.personEntered( event );
               break;

            
            case Man.PERSON_PRESSING_BUTTON:
               listener.personPressedButton( event );
               break;

           
            case Man.PERSON_EXITING_ELEVATOR:
               listener.personDeparture( event );
               break;

           
            case Man.PERSON_EXITED:
               listener.personExited( event );
               break;

            default:
               break;
         }
      }
   } 

  
   public void personCreated( MovingPersonEvent moveEvent )
   {
      sendPersonMoveEvent( Man.PERSON_CREATED, moveEvent );
   }

   
   public void personArrival( MovingPersonEvent moveEvent )
   {
      sendPersonMoveEvent( Man.PERSON_ARRIVED, moveEvent );
   }

   
   public void personPressedButton( MovingPersonEvent moveEvent )
   {
      sendPersonMoveEvent( Man.PERSON_PRESSING_BUTTON, 
         moveEvent );
   }

  
   public void personEntered( MovingPersonEvent moveEvent )
   {
      sendPersonMoveEvent( Man.PERSON_ENTERING_ELEVATOR, 
         moveEvent );
   }

   
   public void personDeparture( MovingPersonEvent moveEvent )
   {
      sendPersonMoveEvent( Man.PERSON_EXITING_ELEVATOR,
         moveEvent );
   }
  
  
   public void personExited( MovingPersonEvent moveEvent )
   {
      sendPersonMoveEvent( Man.PERSON_EXITED, moveEvent );
   }

   
   public void elevatorDoorOpened( ElevatorDoorEvent doorEvent )
   {
      doorListener.elevatorDoorOpened( doorEvent );
   }

   
   public void elevatorDoorClosed( ElevatorDoorEvent doorEvent )
   {
      doorListener.elevatorDoorClosed( doorEvent );
   }

   
   public void buttonPressed( ElevatorButtonEvent buttonEvent )
   {
      buttonListener.buttonPressed( buttonEvent );
   }

   
   public void buttonReset( ElevatorButtonEvent buttonEvent )
   {
      buttonListener.buttonReset( buttonEvent );
   }

  
   public void ringBell( ElevatorBellEvent bellEvent )
   {
      bellListener.ringBell( bellEvent );
   }

   
   public void lightTurnedOn( ElevatorLightEvent lightEvent )
   {
      lightListener.lightTurnedOn( lightEvent );
   }

   
   public void lightTurnedOff( ElevatorLightEvent lightEvent )
   {
      lightListener.lightTurnedOff( lightEvent );
   }

   
   public void setElevatorSimulationListener( 
   SimulationElevatorListener listener )
   {
      
      addPersonMoveListener( listener );
      setElevatorMoveListener( listener );
      setDoorListener( listener );
      setButtonListener( listener );
      setLightListener( listener );
      setBellListener( listener );
   }

  
   public void addPersonMoveListener(
   MovingPersonListener listener )
   {
      personMoveListeners.add( listener );
   }

   
   public void setDoorListener( ElevatorDoorListener listener )
   {
      doorListener = listener;
   }

   
   public void setButtonListener( ElevatorButtonListener listener )
   {
      buttonListener = listener;
   }

  
   public void setElevatorMoveListener(
   MoveElevatorListener listener )
   {
      elevatorMoveListener = listener;
   }

  
   public void setLightListener( ElevatorLightListener listener )
   {
      lightListener = listener;
   }

  
   public void setBellListener( ElevatorBellListener listener )
   {
      bellListener = listener;
   }



}
