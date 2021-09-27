package elevator.view;
import java.awt.*;
import java.util.*;


import javax.swing.*;

public class PanelAnimation extends PanelMoving {

	   // ImageIcon should cycle frames if it's possible
	   private boolean animating;

	   // cycle rate of frames (i.e., rate advancing to next frame)
	   private int animationRate;
	   private int animationRateCounter;
	   private boolean cycleForward = true;

	   // For animation frames, separate ImageIcons are used
	   private ImageIcon imageIcons[];

	   // All frame sequences are saved in a single location.
	   private java.util.List frameSequences;
	   private int currentAnimation;

	   // Should the animation loop (continue) at the end of the cycle
	   private boolean loop;

	   // Should the last frame of an animation be displayed at the end of the animation
	   private boolean displayLastFrame;

	   // assists in determining the next frame to be presented
	   private int currentFrameCounter;

	   // The constructor accepts an array of filenames as well as the screen position
	   public PanelAnimation( int identifier, String imageName[] )
	   {
	      super( identifier, imageName[0] );

	      // ImageIcon objects are created from the imageName string array
	      imageIcons = new ImageIcon[ imageName.length ];

	      for ( int i = 0; i < imageIcons.length; i++ ) {
	         imageIcons[i] = new ImageIcon( 
	            getClass().getResource( imageName[i] ) );
	      }
	      
	      frameSequences = new ArrayList();

	   } // constructor for AnimatedPanel comes to an end
	   // change the position of the icon and the animation frame
	   public void animate()
	   {
	      super.animate();

	      // If the counter exceeds the animation rate, the next animation frame will be played
	      if ( frameSequences != null && isAnimating() ) {

	         if ( animationRateCounter > animationRate ) {
	            animationRateCounter = 0;
	            determineNextFrame();
	         }
	         else
	            animationRateCounter++;
	      }
	   } // end method of animate

	   // identify the next frame of animation
	   private void determineNextFrame()
	   {
	      int frameSequence[] = 
	         ( int[] ) frameSequences.get( currentAnimation );

	      // Determine the end frame if there are no further animation frames
	      // unless when a loop is supplied
	      if ( currentFrameCounter >= frameSequence.length ) {
	         currentFrameCounter = 0;

	         // If loop is false, the animation will be stopped
	         if ( !isLoop() ) {

	            setAnimating( false );

	            if ( isDisplayLastFrame() )

	               // show the last frame in the sequence
	               currentFrameCounter = frameSequence.length - 1;
	         }
	      }

	      // set the current frame of animation
	      setCurrentFrame( frameSequence[ currentFrameCounter ] );
	      currentFrameCounter++;

	   } // determineNextFrame is the final method

	   // frameSequences ArrayList frameSequences ArrayList frameSequences ArrayList frameSequences ArrayList frameSe
	   public void addFrameSequence( int frameSequence[] )
	   {
	      frameSequences.add( frameSequence );
	   }

	   // ask if AnimatedPanel has animated (cycling frames)
	   public boolean isAnimating()
	   {
	      return animating;
	   }

	   // set AnimatedPanel as animate
	   public void setAnimating( boolean animate )
	   {
	      animating = animate;
	   }

	   // current ImageIcon is set
	   public void setCurrentFrame( int frame )
	   {
	      setIcon( imageIcons[ frame ] );
	   }

	   // set the animation speed
	   public void setAnimationRate( int rate )
	   {
	      animationRate = rate;
	   }

	   // calculate the animation rate
	   public int getAnimationRate()
	   {
	      return animationRate;
	   }

	   // determine whether or not the animation should loop
	   public void setLoop( boolean loopAnimation  )
	   {
	      loop = loopAnimation;
	   }
	   
	   // determine whether the animation should be looped
	   public boolean isLoop()
	   {
	      return loop;
	   }

	   // determine whether the last frame should be displayed at the conclusion of the animation
	   private boolean isDisplayLastFrame()
	   {
	      return displayLastFrame;
	   }

	   // choose whether or not to show the last frame at the end of the animation
	   public void setDisplayLastFrame( boolean displayFrame )
	   {
	      displayLastFrame = displayFrame;
	   }

	   // begin playing the animation sequence specified by the index
	   public void playAnimation( int frameSequence )
	   {
	      currentAnimation = frameSequence;
	      currentFrameCounter = 0;
	      setAnimating( true );
	   }
}
