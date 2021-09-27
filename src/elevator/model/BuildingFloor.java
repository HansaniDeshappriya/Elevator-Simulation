package elevator.model;

import elevator.*;

public class BuildingFloor extends Place 
   implements Constants {

   
   private ShaftElevator shaftelevator;

   
   public BuildingFloor( String name )
   {
      setVenueName( name );
   }

   public ShaftElevator getElevatorShaft()
   {
      return shaftelevator;
   }

   
   public void setElevatorShaft( ShaftElevator shaft )
   {
	   shaftelevator = shaft;
   }


   public EDoor getDoor()
   {
      if ( getVenueName().equals( FIRST_FLOOR_NAME ) )
         return getElevatorShaft().getFirstFloorDoor();
      else 

         if ( getVenueName().equals( SECOND_FLOOR_NAME ) )
            return getElevatorShaft().getSecondFloorDoor();
      else

         return null;

   } 

   public ElevatorButton getButton()
   {
      if ( getVenueName().equals( FIRST_FLOOR_NAME ) )
         return getElevatorShaft().getFirstFloorButton();
      else

         if ( getVenueName().equals( SECOND_FLOOR_NAME ) )
            return getElevatorShaft().getSecondFloorButton();
      else

         return null;

   } 
}

