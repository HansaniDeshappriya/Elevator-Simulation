package elevator.event;

import elevator.model.*;

public class SimulationElevatorEvent {

	   
	   private Place venue;
	   
	   
	   private Object source;

	   
	   public SimulationElevatorEvent(Object source, Place venue)
	   {
	      setSource( source );
	      setPlace( venue );
	   }

	   private void setSource( Object eventSource )
	   {
	      source = eventSource;
	   }

	   
	   public Object getSource()
	   {
	      return source;
	   }
	   
	   
	   
	   public void setPlace( Place eventPlace )
	   {
		   venue = eventPlace;
	   }

	   
	   public Place getLocation()
	   {
	      return venue;
	   }

	   
	 
}
