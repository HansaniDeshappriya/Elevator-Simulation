package elevator.controller;

import java.awt.*;
import java.awt.event.*;


import javax.swing.*;

import elevator.model.*;
import elevator.event.*;
import elevator.Constants;

public class Controller extends JPanel
implements Constants {

// There are two JButtons in the controller
private JButton firstButtonOfController;
private JButton secondButtonOfController;

// a model reference
private SimulationElevator simulationofElevator;

public Controller( SimulationElevator simulation )
{
	simulationofElevator = simulation;
   setBackground( Color.white );

   // make the first button on the controller
   firstButtonOfController = new JButton( "Test Case 1" );
   add( firstButtonOfController );

   // add a second controller button
   secondButtonOfController = new JButton( "Test Case 2" );
   add( secondButtonOfController );

   // anonymous inner class registers for an ActionEvents from
   // JButton is the initial controller
   firstButtonOfController.addActionListener( 
      new ActionListener() {

         // When a JButton is pressed, this method is called
         public void actionPerformed( ActionEvent event )
         {
            // Person to be added to first floor
        	 simulationofElevator.addPerson( FIRST_FLOOR_NAME );

            // user input is disabled
            firstButtonOfController.setEnabled( false );
         }
      } // the end of the nameless inner class
   );

   // anonymous inner class registers for an ActionEvents from
   // JButton, the second controller
   secondButtonOfController.addActionListener( 
      new ActionListener() {

         // When a JButton is pressed, this method is called
         public void actionPerformed( ActionEvent event )
         {
            // Person to be added to second floor
        	 simulationofElevator.addPerson( SECOND_FLOOR_NAME );

            // user input is disabled
            secondButtonOfController.setEnabled( false );
         }
      } // the end of the nameless inner class
   );

   // If the anonymous inner class is used, user input on the Floor is enabled
   // Person enters to the Elevator on that Floor
   simulationofElevator.addPersonMoveListener( 
      new MovingPersonListener() {

         // When a person enters an elevator, this function is called
         public void personEntered( 
        		 MovingPersonEvent event )
         {
            // obtain the departure floor
            String location =
               event.getLocation().getVenueName();

            // If the first Floor departs, enable the first JButton
            if ( location.equals( FIRST_FLOOR_NAME ) )
            	firstButtonOfController.setEnabled( true );

            // turn on the second If JButton is on the second floor
            else
            	secondButtonOfController.setEnabled( true );

         } // end method of personEntered

         // alternative PersonMoveListener implementation methods
         public void personCreated( 
        		 MovingPersonEvent event ) {}

         public void personArrival( 
        		 MovingPersonEvent event ) {}

         public void personExited( 
        		 MovingPersonEvent event ) {}

         public void personDeparture( 
        		 MovingPersonEvent event ) {}

         public void personPressedButton( 
        		 MovingPersonEvent event ) {}

	


      } // the end of the nameless inner class
   );
} // constructor for ElevatorController comes to an end
}
