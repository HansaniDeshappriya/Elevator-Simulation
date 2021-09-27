package elevator;

import java.awt.*;

import javax.swing.*;

import elevator.model.*;
import elevator.view.*;
import elevator.controller.*;

public class CaseStudy extends JFrame {

	   // the model, the view, and the controller
	   private SimulationElevator model;
	   private ViewElevator view;
	   private Controller controller;

	   // The constructor creates a model, a view, and a controller
	   public CaseStudy()
	   {
	      super( "Elevator Controller" );

	      // Create a model, a view, and a controller
	      model = new SimulationElevator();
	      view = new ViewElevator();
	      controller = new Controller( model );

	      // Model events are registered in View
	      model.setElevatorSimulationListener( view );

	      // ElevatorSimulation now has a view and a controller
	      getContentPane().add( view, BorderLayout.CENTER );
	      getContentPane().add( controller, BorderLayout.SOUTH );

	   } // constructor ElevatorSimulation comes to an end

	   // The main method initiates the program
	   public static void main( String args[] )
	   {
	      // ElevatorSimulation is a simulation of an elevator
	      CaseStudy caseStudy = new CaseStudy();
	      caseStudy.setDefaultCloseOperation( EXIT_ON_CLOSE );
	      caseStudy.pack();
	      caseStudy.setVisible( true );
	   }
}
