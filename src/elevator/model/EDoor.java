package elevator.model;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import elevator.event.ElevatorDoorEvent;
import elevator.event.ElevatorDoorListener;

public class EDoor {
	private boolean open = false;

	  
	   public static final int AUTOMATIC_CLOSE_DELAY = 3000;


	   private Set doorListeners;


	   private Place placeOfDoor;


	   public EDoor()
	   {
	      doorListeners = new HashSet( 1 );
	   }

	  
	   public void addDoorListener( ElevatorDoorListener listener )
	   {
	   
	      synchronized( doorListeners )
	      {
	         doorListeners.add( listener );
	      }
	   }

	 
	   public void removeDoorListener( ElevatorDoorListener listener )
	   {
	   
	      synchronized( doorListeners )
	      {
	         doorListeners.remove( listener );
	      }
	   }

	  
	   public synchronized void openDoor( Place location )
	   {
	      if ( !open ) {

	         open = true;

	        
	         Iterator iterator;
	         synchronized( doorListeners )
	         {
	            iterator = new HashSet( doorListeners ).iterator();
	         }

	       
	         while ( iterator.hasNext() ) {
	        	 ElevatorDoorListener doorListener = 
	               ( ElevatorDoorListener ) iterator.next();

	           
	            doorListener.elevatorDoorOpened(
	               new ElevatorDoorEvent( this, location ) );
	         }

	         placeOfDoor = location;

	         
	         Thread closeThread = new Thread( 
	            new Runnable() {

	               public void run()
	               {
	                 
	                  try {
	                     Thread.sleep( AUTOMATIC_CLOSE_DELAY );
	                     closeDoor( placeOfDoor );
	                  }

	                 
	                  catch ( InterruptedException exception ) {
	                     exception.printStackTrace();
	                  }                     
	               }
	            } 
	         );

	         closeThread.start();
	      }
	      
	    
	      notifyAll();
	      
	   } 

	   public synchronized void closeDoor( Place location )
	   {      
	      if ( open ) {

	         open = false;

	       
	         Iterator iterator;
	         synchronized( doorListeners )
	         {
	            iterator = new HashSet( doorListeners ).iterator();
	         }

	       
	         while ( iterator.hasNext() ) {
	        	 ElevatorDoorListener doorListener = 
	               ( ElevatorDoorListener ) iterator.next();

	           
	            doorListener.elevatorDoorClosed(
	               new ElevatorDoorEvent( this, location ) );
	         }
	      }
	      
	   } 
	   
	   

	  
	   public synchronized boolean isDoorOpen()
	   {
	      return open;
	   }
}
