package elevator.view;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.applet.*;

// Java extension package
import javax.swing.*;


import elevator.event.*;
import elevator.Constants;

public class ViewElevator extends JPanel 
implements ActionListener, SimulationElevatorListener,
   Constants {

// Dimensions of the ElevatorView
private static final int ELEVATORVIEW_WIDTH = 800;
private static final int ELEVATORVIEW_HEIGHT = 435;

// Panels in ElevatorView have an adjustment for positioning
private static final int OFFSET = 10;

//A number of times walking to the elevator and buttons of floors
private static final int WALKING_TIME_FOR_BTN = 3000; // 3 seconds
private static final int WALKING_TIME_FOR_ELEVATOR = 1000; // 1 seconds

//In the elevator, you can travel across time (5 seconds)
private static final int TRAVEL_TIME_FOR_ELEVATOR = 5000;

// Every 50 milliseconds, the elements of the elevator are refreshed
private static final int ANIMATION_DELAY = 50;

// Equations of angular displacement
private static final int BUTTON_TO_MAN_WIDTH = 400;
private static final int ELEVATOR_TO_BUTTON_WIDTH = 50;
private static final int ELEVATOR_TO_MAN_WIDTH = 
BUTTON_TO_MAN_WIDTH + ELEVATOR_TO_BUTTON_WIDTH;

// Animated door images
private static final String framesElevatorDoor[] = {
   "images/elevatordoor1.png", "images/elevatordoor2.png", "images/elevatordoor3.png",
   "images/elevatordoor4.png", "images/elevatordoor5.png" };

// Animated person images
private static final String framesFroPerson[] = { 
   "images/man1.png", "images/man2.png", "images/man3.png", 
   "images/man4.png", "images/man5.png", "images/man6.png",
   "images/man7.png", "images/man8.png" };

//Animated bell images
private static final String framesForBell[] = { 
"images/bell1.png", "images/bell2.png", 
"images/bell3.png" };

//Animated floor Button images
private static final String framesFloorButton[] = { 
"images/floorButtonUnpressed.png",
"images/floorButtonPressed.png",
"images/floorButtonLit.png" };

//Animated elevator Button images
private static final String framesElevatorButton[] = {
"images/elevatorbuttonunpressed.png",
"images/elevatorbuttonpressed.png",
"images/elevatorbuttonlitght.png" };

// Animated light images
private static final String framesForLight[] = {
   "images/lightOff.png", "images/lightOn.png" };

// Animated floor light images
private static final String firstFloorFramesForLight[] = {
   "images/firstflooroff.png", 
   "images/firstflooron.png" };

private static final String secondFloorFramesForLight[] = {
   "images/secondflooroff.png", 
   "images/secondflooron.png", };

private static final String imageForFloor = 
   "images/floor.png";
private static final String imageForCeiling = 
   "images/ceiling.png";
private static final String imageForElevator = 
   "images/elevator.jpg";
private static final String imageForWall = 
   "images/wall.jpg";
private static final String imageForElevatorShaft = 
   "images/elevatorShaft.png";

// audio files
private static final String soundOfDoorClose = "doorClose.wav";
private static final String soundOfWalking = "walk.wav";
private static final String soundOfDoorOpen = "doorOpen.wav";
private static final String soundOfButton = "button.wav";
private static final String soundOfElevatorMusic = "liszt.mid";
private static final String soundOfElevator = "elevator.au";
private static final String soundOfBell = "bell.wav";


// Wall, Ceiling, Floor, and ElevatorShaft ImagePanels
private PanelImage panelForWall;
private PanelImage panelForCeiling;
private PanelImage panelForFirstFloor;
private PanelImage panelForSecondFloor;
private PanelImage panelelevatorShaft;

// Bell, Buttons, Door and Lights AnimatedPanels
private PanelAnimation panelForBell;
private PanelAnimation panelForFirstFloorButton;
private PanelAnimation panelForSecondFloorButton;
private PanelAnimation panelForElevatorButton;
private PanelAnimation panelForDoor;
private PanelAnimation panelForElevatorLight;
private PanelAnimation panelForFirstFloorLight;
private PanelAnimation panelForSecondFloorLight;

//Elevator MovingPanels
private PanelMoving panelForElevator;

// For every Person objects, there is a list of AnimatedPanels
private java.util.List panelsForPersonAnimated;

// AudioClips for sound effects
private AudioClip bellClip;
private AudioClip doorOpenClip;
private AudioClip doorCloseClip;
private AudioClip elevatorClip;
private AudioClip buttonClip;
private AudioClip walkClip;

// ElevatorMusic to play in Elevator
private AudioClip elevatorMusicClip;

// To animation controller there is a Timer; 
private javax.swing.Timer animationTimer;

// From top of screen to display Floors distance
private int positionOfFirstFloor;
private int positionOfSecondFloor;

// The speed of the elevator
private double velocityOfElevator;

// Constructor for ElevatorView
public ViewElevator()
{
   // Null Layout with specificity
   super( null );

   instantiatePanels();
   placePanelsOnView();
   initializeAudio();

   // Calculate the distance traveled by an elevator
   double floorDistance = 
   positionOfFirstFloor - positionOfSecondFloor;

   // Calculate the amount of time required for travel
   double time = TRAVEL_TIME_FOR_ELEVATOR / ANIMATION_DELAY;

   // calculate the elevator's speed (rate = distance / time)
   velocityOfElevator = ( floorDistance + OFFSET ) / time;

   // Thread to begin animation
   startAnimation();

} // constructor for ElevatorView comes to an end

// All Panels should be created at the same time (Floors, Elevator, etc.)
private void instantiatePanels()
{
	 // ElevatorShaft ImagePanel: build and position
	   panelelevatorShaft = 
	      new PanelImage( 0, imageForElevatorShaft );

	   double xPosition = ELEVATOR_TO_MAN_WIDTH + OFFSET;
	   double yPosition = 
	   positionOfFirstFloor - panelelevatorShaft.getHeight();

	   panelelevatorShaft.setPosition( xPosition, yPosition );

	   // Create an ImagePanel for the ceiling and position it
	   panelForCeiling = new PanelImage( 0, imageForCeiling );

	   yPosition = panelelevatorShaft.getPosition().getY() -
			   panelForCeiling.getHeight();

	   panelForCeiling.setPosition( xPosition, yPosition );
	   
   // Create ImagePanels to represent floors
	panelForFirstFloor = new PanelImage( 0, imageForFloor );
	panelForSecondFloor = new PanelImage( 0, imageForFloor );

   // first and second floor positions to be calculated
	positionOfFirstFloor = 
      ELEVATORVIEW_HEIGHT - panelForFirstFloor.getHeight();
	positionOfSecondFloor = 
      ( int ) ( positionOfFirstFloor / 2 ) - OFFSET;

   panelForFirstFloor.setPosition( 0, positionOfFirstFloor );
   panelForSecondFloor.setPosition( 0, positionOfSecondFloor );

   panelForWall = new PanelImage( 0, imageForWall );

  

   // Elevator: Create and place MovingPanel
   panelForElevator = new PanelMoving( 0, imageForElevator );

   yPosition = positionOfFirstFloor - panelForElevator.getHeight();

   panelForElevator.setPosition( xPosition, yPosition );

   // Make the first Floor Button and place it where you want it
   panelForFirstFloorButton = 
      new PanelAnimation( 0, framesFloorButton );

   xPosition = BUTTON_TO_MAN_WIDTH + 2 * OFFSET;
   yPosition = positionOfFirstFloor - 5 * OFFSET;
   panelForFirstFloorButton.setPosition( xPosition, yPosition );

   int floorButtonPressedFrameOrder[] = { 0, 1, 2 };
   panelForFirstFloorButton.addFrameSequence( 
      floorButtonPressedFrameOrder );

   // Make a second Floor Button and place it where you want it
   panelForSecondFloorButton = 
      new PanelAnimation( 1, framesFloorButton );

   xPosition = BUTTON_TO_MAN_WIDTH + 2 * OFFSET;
   yPosition = positionOfSecondFloor - 5 * OFFSET;
   panelForSecondFloorButton.setPosition( xPosition, yPosition );

   panelForSecondFloorButton.addFrameSequence( 
      floorButtonPressedFrameOrder );

   // Floor Lights should be created and placed in strategic locations
   panelForFirstFloorLight = 
      new PanelAnimation( 0, firstFloorFramesForLight );

   xPosition = panelForElevator.getLocation().x - 4 * OFFSET;
   yPosition = 
   panelForFirstFloorButton.getLocation().y - 10 * OFFSET;
   panelForFirstFloorLight.setPosition( xPosition, yPosition );

   panelForSecondFloorLight = 
      new PanelAnimation( 1, secondFloorFramesForLight );

   yPosition = 
   panelForSecondFloorButton.getLocation().y - 10 * OFFSET;
   panelForSecondFloorLight.setPosition( xPosition, yPosition );

   // Create and place AnimatedPanels for Doors
   panelForDoor = new PanelAnimation( 0, framesElevatorDoor );
   int elevatorDoorOpenedFrameOrder[] = { 0, 1, 2, 3, 4 };
   int elevatorDoorClosedFrameOrder[] = { 4, 3, 2, 1, 0 };
   panelForDoor.addFrameSequence( elevatorDoorOpenedFrameOrder );
   panelForDoor.addFrameSequence( elevatorDoorClosedFrameOrder );

   // Identify the location of the door in relation to the elevator
   yPosition = 
		   panelForElevator.getHeight() - panelForDoor.getHeight();

   panelForDoor.setPosition( 0, yPosition );

   // Make a Light AnimatedPanel and place it where you want it
   panelForElevatorLight = new PanelAnimation( 0, framesForLight );
   panelForElevatorLight.setPosition( OFFSET, 5 * OFFSET );

   // Make a Bell AnimatedPanel and place it where you want it
   panelForBell = new PanelAnimation( 0, framesForBell );

   yPosition = panelForElevatorLight.getPosition().getY() +
		   panelForElevatorLight.getHeight() + OFFSET;

   panelForBell.setPosition( OFFSET, yPosition );
   int bellRingAnimation[] = { 0, 1, 0, 2 };
   panelForBell.addFrameSequence( bellRingAnimation );

   // construct and position the Button AnimatedPanel for the elevator
   panelForElevatorButton = 
      new PanelAnimation( 0, framesElevatorButton );

   yPosition = panelForElevator.getHeight() - 6 * OFFSET;
   panelForElevatorButton.setPosition( 10 * OFFSET, yPosition );

   int buttonPressedFrameOrder[] = { 0, 1, 2 };
   panelForElevatorButton.addFrameSequence( 
      buttonPressedFrameOrder );

   // Person AnimatedPanels should be stored in a List
   panelsForPersonAnimated = new ArrayList();

} // instantiatePanels is the final method

// ElevatorView should be used for all panels
private void placePanelsOnView()
{
   // ElevatorView can now have panels added to it
   add( panelForFirstFloor );
   add( panelForSecondFloor );
   add( panelForCeiling );
   add( panelForElevator );
   add( panelForFirstFloorButton );
   add( panelForSecondFloorButton );
   add( panelForFirstFloorLight );
   add( panelForSecondFloorLight );
   add( panelelevatorShaft );
   add( panelForWall );

   // Elevator's MovingPanel can now have panels added to it
   panelForElevator.add( panelForDoor );
   panelForElevator.add( panelForElevatorLight );
   panelForElevator.add( panelForBell );
   panelForElevator.add( panelForElevatorButton );

} // end method of placePanelsOnView

// obtain elevator music and sound effects
private void initializeAudio()
{
   // audio files to make AudioClip sound effects
	EffectSounds sounds = new EffectSounds();
   sounds.setPathPrefix( "sounds/" );

   bellClip = sounds.getAudioClip( soundOfBell );
   doorOpenClip = sounds.getAudioClip( soundOfDoorOpen );
   doorCloseClip = sounds.getAudioClip( soundOfDoorClose );
   elevatorClip = sounds.getAudioClip( soundOfElevator );
   buttonClip = sounds.getAudioClip( soundOfButton );
   walkClip = sounds.getAudioClip( soundOfWalking );
   elevatorMusicClip = sounds.getAudioClip( soundOfElevatorMusic );

} // end initializeAudio method

// starts the animation by drawing images on the screen repeatedly
public void startAnimation()
{
   if ( animationTimer == null ) {
      animationTimer = 
         new javax.swing.Timer( ANIMATION_DELAY, this );
      animationTimer.start();
   }
   else

      if ( !animationTimer.isRunning() )
         animationTimer.restart();
}

// Animation has come to an end
public void stopAnimation()
{
   animationTimer.stop();
}

// In response to the Timer, update the animation of the AnimatedPanels
public void actionPerformed( ActionEvent actionEvent )
{
	panelForElevator.animate();

	panelForFirstFloorButton.animate();
	panelForSecondFloorButton.animate();

   Iterator iterator = getPersonAnimatedPanelsIterator();

   while ( iterator.hasNext() ) {

      // retrieve the AnimatedPanel of the Person from the Set
	   PanelAnimation personPanel = 
         ( PanelAnimation ) iterator.next();

      personPanel.animate(); // update the panel
   }

   repaint(); // all Components have being painted 

} // method action's conclusion Executed

private Iterator getPersonAnimatedPanelsIterator()
{
   //get the iterator from the list
   synchronized( panelsForPersonAnimated )
   {
      return new ArrayList( panelsForPersonAnimated ).iterator();
   }
}

// Person walking sound clip should be stopped
private void stopWalkingSound()
{
   // pause the walking sound
   walkClip.stop();

   Iterator iterator = getPersonAnimatedPanelsIterator();

   // However, if Person is still walking, continue to play
   while ( iterator.hasNext() ) {
	   PanelAnimation panel = ( PanelAnimation ) iterator.next();

      if ( panel.getXVelocity() != 0 )
         walkClip.loop();
   }
} // stopWalkingSound is the last method in the stopWalkingSound class

// produces a PersonAnimatedPanel that has the correct identifier
private PanelAnimation getPersonPanel( MovingPersonEvent event )
{
   Iterator iterator = getPersonAnimatedPanelsIterator();

   while ( iterator.hasNext() ) {

      // get the next AnimatedPanel
	   PanelAnimation personPanel = 
         ( PanelAnimation ) iterator.next();
      
      // return an AnimatedPanel with a matching identifier
      if ( personPanel.getID() == event.geteventID() )
         return personPanel;
   }

   // If there is no match with the correct identifier, return null
   return null;

} // end of getPersonPanel method

//When a Person is created in the model, this method is called
public void personCreated( MovingPersonEvent personEvent )
{
   int personID = personEvent.geteventID();

   String floorLocation = 
      personEvent.getLocation().getVenueName();

   // make an AnimatedPanel that represents a person
   PanelAnimation personPanel = 
      new PanelAnimation( personID, framesFroPerson );

   // decide where the Person should be drawn first
   // Person is drawn offscreen when xPosition is negative
   double xPosition = - personPanel.getWidth();
   double yPosition = 0;

   if ( floorLocation.equals( FIRST_FLOOR_NAME ) )
      yPosition = positionOfFirstFloor +
         ( panelForFirstFloor.getHeight() / 2 );
   else

      if ( floorLocation.equals( SECOND_FLOOR_NAME ) )
         yPosition = positionOfSecondFloor + 
            ( panelForSecondFloor.getHeight() / 2 );

   yPosition -= personPanel.getHeight();

   personPanel.setPosition( xPosition, yPosition );

   // for each person, add some animations
   int walkFrameOrder[] = { 1, 0, 1, 2 };
   int pressButtonFrameOrder[] = { 1, 3, 3, 4, 4, 1 };
   int walkAwayFrameOrder[] = { 6, 5, 6, 7 };
   personPanel.addFrameSequence( walkFrameOrder );
   personPanel.addFrameSequence( pressButtonFrameOrder );
   personPanel.addFrameSequence( walkAwayFrameOrder );

   // Person should begin strolling to the elevator
   personPanel.playAnimation( 0 );
   personPanel.setLoop( true );
   personPanel.setAnimating( true );
   personPanel.setMoving( true );

   // determine the speed of a person
   double time = 
      ( double ) ( WALKING_TIME_FOR_BTN / ANIMATION_DELAY );

   double xDistance = BUTTON_TO_MAN_WIDTH - 
      2 * OFFSET + personPanel.getSize().width;
   double xVelocity = xDistance / time;

   personPanel.setVelocity( xVelocity, 0 );
   personPanel.setAnimationRate( 1 );

   walkClip.loop(); // Person walking is being played as a sound clip

   // storeAnimatedPanels in a person
   synchronized( panelsForPersonAnimated )
   {
	   panelsForPersonAnimated.add( personPanel );
   }

   add( personPanel, 0 );

} // personCreated is the last method in the personCreated class

// When a person arrives in the elevator, this command is executed
public void personArrival( MovingPersonEvent personEvent )
{
   // Locate the panel for the person who issued the event
	PanelAnimation panel = getPersonPanel( personEvent );

   if ( panel != null ) { // if there is a person

      // At the Floor Button, a person comes to a halt
      panel.setMoving( false );
      panel.setAnimating( false );
      panel.setCurrentFrame( 1 );
      stopWalkingSound();

      double xPosition = BUTTON_TO_MAN_WIDTH - 
         ( panel.getSize().width / 2 );
      double yPosition = panel.getPosition().getY();

      panel.setPosition( xPosition, yPosition );
   }
} // personArrival is the last method in the personArrival class

// When a person presses the Button, this function is called
public void personPressedButton( MovingPersonEvent personEvent )
{
   // Find the Panel for the Person Who Issued the Event
	PanelAnimation panel = getPersonPanel( personEvent );

   if ( panel != null ) { // if there is a person

      // A person comes to a complete halt and presses the Button
      panel.setLoop( false );
      panel.playAnimation( 1 );

      panel.setVelocity( 0, 0 );
      panel.setMoving( false );
      panel.setAnimating( true );
      stopWalkingSound();
   }
} // end method of personPressedButton

// When a person begins to enter an elevator, this function is called
public void personEntered( MovingPersonEvent personEvent )
{
   // Locate the panel for the person who issued the event
	PanelAnimation panel = getPersonPanel( personEvent );

   if ( panel != null ) {

      // determine the speed
      double time = WALKING_TIME_FOR_ELEVATOR / ANIMATION_DELAY;

      double distance = 
    	 panelForElevator.getPosition().getX() - 
         panel.getPosition().getX() + 2 * OFFSET;

      panel.setVelocity( distance / time, -1.5 );

      // A person begins walking
      panel.setMoving( true );
      panel.playAnimation( 0 );
      panel.setLoop( true );
   }
} // end method of personEntered

// When a person has exited an elevator, this command is called
public void personDeparture( MovingPersonEvent personEvent)
{
   // Locate the panel for the person who issued the event
	PanelAnimation panel = getPersonPanel( personEvent );

   if ( panel != null ) { // if there is a person

      // determine the speed (in opposite direction)
      double time = WALKING_TIME_FOR_BTN / ANIMATION_DELAY;
      double xVelocity = - BUTTON_TO_MAN_WIDTH / time;

      panel.setVelocity( xVelocity, 0 );

      // eject a person from the elevator
      panelForElevator.remove( panel );

      double xPosition = 
    		  ELEVATOR_TO_MAN_WIDTH + 3 * OFFSET;
      double yPosition = 0;

      String floorLocation = 
         personEvent.getLocation().getVenueName();

      // select the floor from which the person will exit
      if ( floorLocation.equals( FIRST_FLOOR_NAME ) )
         yPosition = positionOfFirstFloor +
            ( panelForFirstFloor.getHeight() / 2 );
      else

         if ( floorLocation.equals( SECOND_FLOOR_NAME ) )
            yPosition = positionOfSecondFloor + 
               ( panelForSecondFloor.getHeight() / 2 );

      yPosition -= panel.getHeight();

      panel.setPosition( xPosition, yPosition );

      // Person should be added to the ElevatorView
      add( panel, 0 );

      // A person begins walking
      panel.setMoving( true );
      panel.setAnimating( true );
      panel.playAnimation( 2 );
      panel.setLoop( true );
      walkClip.loop();
   }
} // end method of personDeparture

// When a person exits a simulation, this method is called
public void personExited( MovingPersonEvent personEvent)
{
   // identify the panel for the person who issued the moveEvent
	PanelAnimation panel = getPersonPanel( personEvent );

   if ( panel != null ) { // if there is a person

      panel.setMoving( false );
      panel.setAnimating( false );

      // Person should be removed forever, and the sound of walking should be turned off
      synchronized( panelsForPersonAnimated )
      {
    	  panelsForPersonAnimated.remove( panel );
      }
      remove( panel );
      stopWalkingSound();
   }
} // personExited was called at the end of the method


// When the Elevator has left the Floor, this function is called
public void elevatorDeparture( MoveElevatorEvent moveEvent )
{
   String location = 
      moveEvent.getLocation().getVenueName();

   // detect whether or not a person is on an elevator
   Iterator iterator = getPersonAnimatedPanelsIterator();

   while ( iterator.hasNext() ) {

	   PanelAnimation personPanel =
         ( PanelAnimation ) iterator.next();

      double yPosition = personPanel.getPosition().getY();
      String panelLocation;

      // ascertain which floor the individual entered
      if ( yPosition > positionOfSecondFloor )
         panelLocation = FIRST_FLOOR_NAME;
      else
         panelLocation = SECOND_FLOOR_NAME;

      int xPosition = 
         ( int ) personPanel.getPosition().getX();

      // if a person is present in the elevator
      if ( panelLocation.equals( location ) 
         && xPosition > BUTTON_TO_MAN_WIDTH + OFFSET ) {

         // PersonAnimatedPanel should be removed from ElevatorView
         remove( personPanel );

         // Elevator should have a PersonAnimatedPanel added to it
         panelForElevator.add( personPanel, 1 );
         personPanel.setLocation( 2 * OFFSET, 9 * OFFSET );
         personPanel.setMoving( false );
         personPanel.setAnimating( false );
         personPanel.setVelocity( 0, 0 );
         personPanel.setCurrentFrame( 1 );
      }
   } // end of the while loop

   // determine the elevator speed based on the floor
   if ( location.equals( FIRST_FLOOR_NAME ) )
	   panelForElevator.setVelocity( 0, -velocityOfElevator );
   else

      if ( location.equals( SECOND_FLOOR_NAME ) )
    	  panelForElevator.setVelocity( 0, velocityOfElevator );

   // Elevator should start moving and Elevator music should be played
  // elevatorPanel.setMoving( true );

   if ( elevatorClip != null )
      elevatorClip.play();

   elevatorMusicClip.play();

} // end of elevatorDeparture menue

// When the Elevator reaches the desired Floor, this command is executed
public void elevatorArrival( MoveElevatorEvent moveEvent )
{
   // Elevator and music must be turned off
	panelForElevator.setMoving( false );
   elevatorMusicClip.stop();

   double xPosition = panelForElevator.getPosition().getX();
   double yPosition;

   // set the elevator to either the first or second floor
   if ( panelForElevator.getYVelocity() < 0 )
      yPosition = 
     positionOfSecondFloor - panelForElevator.getHeight();
   else
      yPosition = 
      positionOfFirstFloor - panelForElevator.getHeight();

   panelForElevator.setPosition( xPosition, yPosition );

} // end of elevatorArrival method


// When the model's Door has opened, this method is called
public void elevatorDoorOpened( ElevatorDoorEvent doorEvent )
{
   //find the location of the DoorEvent
   String location = 
      doorEvent.getLocation().getVenueName();

   // play a door opening animation
   panelForDoor.playAnimation( 0 );
   panelForDoor.setAnimationRate( 2 );
   panelForDoor.setDisplayLastFrame( true );

   // play a door opening sound clip
   if ( doorOpenClip != null )
      doorOpenClip.play();

} // end method of elevatorDoorOpened

// When the model's Door has closed, this method is called
public void elevatorDoorClosed( ElevatorDoorEvent doorEvent )
{
   // find the location of the DoorEvent
   String location = 
      doorEvent.getLocation().getVenueName();

   // play a door shutting animation
   panelForDoor.playAnimation( 1 );
   panelForDoor.setAnimationRate( 2 );
   panelForDoor.setDisplayLastFrame( true );

   // play a door closing sound clip
   if ( doorCloseClip != null )
      doorCloseClip.play();

} // end of elevatorDoorClosed method

// When the model's Button is pressed, this method is called
public void buttonPressed( ElevatorButtonEvent buttonEvent )
{
   // obtain the ButtonEventLocation
   String location = 
      buttonEvent.getLocation().getVenueName();

   // If you're coming from an elevator, push the elevator button
   if ( location.equals( ELEVATOR_NAME ) ) {
	   panelForElevatorButton.playAnimation( 0 );
	   panelForElevatorButton.setDisplayLastFrame( true );
   }

   // If you're starting from the ground, press the Floor Button
   else

      if ( location.equals( FIRST_FLOOR_NAME ) ) {
    	  panelForFirstFloorButton.playAnimation( 0 );
    	  panelForFirstFloorButton.setDisplayLastFrame( true );
      }
   else

      if ( location.equals( SECOND_FLOOR_NAME ) ) {
    	  panelForSecondFloorButton.playAnimation( 0 );
    	  panelForSecondFloorButton.setDisplayLastFrame( true );
      }

   if ( buttonClip != null )
      buttonClip.play(); // hit the play button to hear a sound clip

} // end method of buttonPressed

// When the model's Button has been reset, this method is called
public void buttonReset( ElevatorButtonEvent buttonEvent )
{
   // obtain the ButtonEventLocation
   String location = 
      buttonEvent.getLocation().getVenueName();

   // If you're coming from the elevator, you'll need to reset the elevator button
   if ( location.equals( ELEVATOR_NAME ) ) {

      // If you're still animating, go back to the first frame
      if ( panelForElevatorButton.isAnimating() )
    	  panelForElevatorButton.setDisplayLastFrame( false );
      else
    	  panelForElevatorButton.setCurrentFrame( 0 );
   }

   // If the Floor Button is pressed, it will be reset
   else

      if ( location.equals( FIRST_FLOOR_NAME ) ) {

         // If you're still animating, go back to the first frame
         if ( panelForFirstFloorButton.isAnimating() )
        	 panelForFirstFloorButton.setDisplayLastFrame( 
               false );
         else
        	 panelForFirstFloorButton.setCurrentFrame( 0 );
      }
   else

      if ( location.equals( SECOND_FLOOR_NAME ) ) {

         // If you're still animating, go to the first frame
         if ( panelForSecondFloorButton.isAnimating() )
        	 panelForSecondFloorButton.setDisplayLastFrame( 
               false );
         else
        	 panelForSecondFloorButton.setCurrentFrame( 0 );
      }

} // method buttonReset comes to an end

// When the model's bell has rung, this function is called
public void ringBell( ElevatorBellEvent bellEvent )
{
	panelForBell.playAnimation( 0 ); // animated Bell

   if ( bellClip != null ) // sound clip of a bell
      bellClip.play();
}

// When the model's light is turned on, this command is triggered
public void lightTurnedOn( ElevatorLightEvent lightEvent )
{
   // enter the elevator, turn on the light
	panelForElevatorLight.setCurrentFrame( 1 );

   String location = 
      lightEvent.getLocation().getVenueName();

   // On the first or second floor, turn on the light
   if ( location.equals( FIRST_FLOOR_NAME ) )
	   panelForFirstFloorLight.setCurrentFrame( 1 );

   else

      if ( location.equals( SECOND_FLOOR_NAME ) )
    	  panelForSecondFloorLight.setCurrentFrame( 1 );

} // end method of lightTurnedOn

// When the model's light is switched off, this function is called
public void lightTurnedOff( ElevatorLightEvent lightEvent )
{
   // Elevator Lights Must Be Turned Off
	panelForElevatorLight.setCurrentFrame( 0 );

   String location = 
      lightEvent.getLocation().getVenueName();

   // On the first or second floor, turn off the light
   if ( location.equals( FIRST_FLOOR_NAME ) )
	   panelForFirstFloorLight.setCurrentFrame( 0 );

   else

      if ( location.equals( SECOND_FLOOR_NAME ) )
    	  panelForSecondFloorLight.setCurrentFrame( 0 );

} // end method of lightTurnedOff

// return the ElevatorView's preferred size
public Dimension getPreferredSize()
{
   return new Dimension( ELEVATORVIEW_WIDTH, ELEVATORVIEW_HEIGHT );
}

// return the ElevatorView's smallest size
public Dimension getMinimumSize()
{
   return getPreferredSize();
}

   // return the ElevatorView's maximum size
public Dimension getMaximumSize()
{
   return getPreferredSize();
}


}
