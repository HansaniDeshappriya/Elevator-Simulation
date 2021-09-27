package elevator.view;

import java.applet.Applet;
import java.applet.AudioClip;

public class EffectSounds {

	   // sound files placement
	   private String prefix = "";

	   public EffectSounds() {}

	   // obtain the AudioClip that is related with the soundFile
	   public AudioClip getAudioClip( String soundFile )
	   {
	      try {
	         return Applet.newAudioClip( getClass().getResource( 
	            prefix + soundFile ) );
	      }

	      // If soundFile does not exist, return null
	      catch ( NullPointerException nullPointerException ) {
	         return null;
	      }
	   }

	   // set the prefix for the soundFile's location
	   public void setPathPrefix( String string )
	   {
	      prefix = string;
	   }
}

