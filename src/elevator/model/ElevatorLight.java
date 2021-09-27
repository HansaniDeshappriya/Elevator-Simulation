package elevator.model;

import elevator.event.MoveElevatorEvent;
import elevator.event.MoveElevatorListener;
import elevator.event.ElevatorLightEvent;
import elevator.event.ElevatorLightListener;

public class ElevatorLight implements MoveElevatorListener {

  
   private boolean elevatorLightOn;

   
   public static final int AUTOMATIC_TURNOFF_DELAY = 3000;

   
   private ElevatorLightListener elevatorLightListener;

   
   private Place lightPlace;

   
   public void setLightListener( ElevatorLightListener ElevatorListener )
   {
	   elevatorLightListener = ElevatorListener;
   }
   public boolean isLightOn()
   {
      return elevatorLightOn;
   }

   
   public void elevatorDeparture( 
		   MoveElevatorEvent moveEvent )
   {
      turnOffLight( moveEvent.getLocation() );
   }

   
   public void elevatorArrival( 
		   MoveElevatorEvent moveEvent )
   {
      turnOnLight( moveEvent.getLocation() );
   }

  
   public void turnOnLight( Place place1 )
   {
      if ( !elevatorLightOn ) {

    	  elevatorLightOn = true;

         
    	  elevatorLightListener.lightTurnedOn( 
            new ElevatorLightEvent( this, place1 ) );

    	  lightPlace = place1;

         
         Thread thread = new Thread( 
            new Runnable() {

               public void run()
               {
                  
                  try {
                     Thread.sleep( AUTOMATIC_TURNOFF_DELAY );
                     turnOffLight( lightPlace );
                 }

                  
                  catch ( InterruptedException exception ) {
                     exception.printStackTrace();
                  }
               }
            }
         );

         thread.start();
      }
   } 

   
   public void turnOffLight( Place place )
   {
      if ( elevatorLightOn ) {

    	  elevatorLightOn = false;

        
    	  elevatorLightListener.lightTurnedOff( 
            new ElevatorLightEvent( this, place ) );
      }
   } 

}
