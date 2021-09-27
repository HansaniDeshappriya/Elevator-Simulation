package elevator.event;

public interface MovingPersonListener {
	
	   public void personCreated( MovingPersonEvent moveEvent );

	   public void personArrival( MovingPersonEvent moveEvent );

	   public void personEntered( MovingPersonEvent moveEvent );
	   
	   public void personPressedButton(MovingPersonEvent moveEvent );
	   
	   public void personDeparture( MovingPersonEvent moveEvent );

	   public void personExited( MovingPersonEvent moveEvent );
	
}
