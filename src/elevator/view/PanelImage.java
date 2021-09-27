package elevator.view;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;


import javax.swing.*;

public class PanelImage extends JPanel {

	   // unique identifier
	   private int ID;

	   // position on the screen
	   private Point2D.Double position;

	   // imagePaint a screen icon
	   private ImageIcon imageIcon;

	   // all ImagePanel children are saved
	   private Set panelChildren;

	 
	   public PanelImage( int identifier, String imageName )
	   {
	      super( null ); 
	      setOpaque( false );

	      
	      ID = identifier;

	
	      position = new Point2D.Double( 0, 0 );
	      setLocation( 0, 0 );

	     
	      imageIcon = new ImageIcon(
	         getClass().getResource( imageName ) );

	      Image image = imageIcon.getImage();
	      setSize( 
	         image.getWidth( this ), image.getHeight( this ) );

	 
	      panelChildren = new HashSet();

	   } 

	 
	   public void paintComponent( Graphics g )
	   {
	      super.paintComponent( g );

	
	      imageIcon.paintIcon( this, g, 0, 0 );
	   }

	   
	   public void add( PanelImage panel )
	   {
	      panelChildren.add( panel );
	      super.add( panel );
	   }

	
	   public void add( PanelImage panel, int index )
	   {
	      panelChildren.add( panel );
	      super.add( panel, index );
	   }

	 
	   public void remove( PanelImage panel )
	   {
	      panelChildren.remove( panel );
	      super.remove( panel );
	   }

	
	   public void setIcon( ImageIcon icon )
	   {
	      imageIcon = icon;
	   }

	 
	   public void setPosition( double x, double y )
	   {
	      position.setLocation( x, y );
	      setLocation( ( int ) x,  ( int ) y );
	   }

	  
	   public int getID()
	   {
	      return ID;
	   }

	  
	   public Point2D.Double getPosition()
	   {
	      return position;
	   }

	 
	   public ImageIcon getImageIcon()
	   {
	      return imageIcon;
	   }

	   
	   public Set getChildren()
	   {
	      return panelChildren;
	   }
}
