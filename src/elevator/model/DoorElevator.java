package elevator.model;

import java.util.*;


import elevator.event.*;

public class DoorElevator extends EDoor implements MoveElevatorListener {

  
   public synchronized void openDoor( Place place1 )
   {    
	   place1.getDoor().openDoor( place1 );
      
      super.openDoor( place1 );
      
   } 

   
   public synchronized void closeDoor( Place place1 )
   {      
	   place1.getDoor().closeDoor( place1 );
      
      super.closeDoor( place1 );
      
   } 
   
   
   public void elevatorDeparture( MoveElevatorEvent moveEvent ) {}

  
   public void elevatorArrival( MoveElevatorEvent moveEvent )
   {
      openDoor( moveEvent.getLocation() );
   }






}
