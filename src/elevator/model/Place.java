package elevator.model;

public abstract class Place {
	private String venue;

	   public abstract ElevatorButton getButton();

	   
	   public abstract EDoor getDoor();
	   
	   protected void setVenueName( String vname )
	   {
		   venue = vname;
	   }

	   
	   public String getVenueName()
	   {
	      return venue;
	   }

	   
	   
}
