package gateway_interface;

import genesis_graphic.DepthConstants;

import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Point2D;

import omega_graphic.Sprite;
import omega_world.Area;

/**
 * Creates an OptionBar for one of the game's options.
 * 
 * @author Unto Solala & Mikko Hilpinen
 * @since 8.9.2013
 */
public class IntegerOptionBar extends AbstractOptionBar
{
	// ATTRIBUTES-------------------------------------------------------
	
	private OptionBarButton leftbutton, rightbutton;

	
	//CONSTRUCTOR-------------------------------------------------------
	
	/**
	 * Constructs an OptionBar based on the given parameters.
	 * 
	 * @param x The x-coordinate of the bar's left side (in pixels)
	 * @param y The y-coordinate of the bar's top (in pixels)
	 * @param defaultValue The value the bar will have as default
	 * @param minValue The minimum value the bar can have
	 * @param maxValue The maximum value the bar can have
	 * @param description The description shown in the bar
	 * @param textFont What font the text will use
	 * @param textColor What color the text will have
	 * @param buttonSprite What sprite is used to draw the value-adjustment buttons
	 * @param buttonMask The mask used for the buttons' collision checking
	 * @param area The area where the object is placed to
	 */
	public IntegerOptionBar(int x, int y, int defaultValue,
			int minValue, int maxValue, String description, Font textFont, 
			Color textColor, Sprite buttonSprite, Sprite buttonMask, Area area)
	{
		super(x, y, defaultValue, minValue, maxValue, description, textFont, 
				textColor, area);

		// Initializes attributes
		this.leftbutton = new OptionBarButton((int)this.getX(),
				(int)this.getY(), OptionBarButton.LEFT, buttonSprite, 
				buttonMask, area);
		this.rightbutton = new OptionBarButton((int)this.getX()+100,
				(int)this.getY(), OptionBarButton.RIGHT, buttonSprite, 
				buttonMask, area);
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------
	
	@Override
	public void kill()
	{
		// Also kills the buttons
		this.leftbutton.kill();
		this.rightbutton.kill();
		
		super.kill();
	}
	
	
	// OTHER METHODS	--------------------------------------------------

	/**
	 * OptionBarButtons are buttons used to change the OptionBar's
	 * values.
	 * 
	 * @author Unto Solala & Mikko Hilpinen
	 * @since 8.9.2013
	 */
	private class OptionBarButton extends AbstractMaskButton
	{
		private static final int RIGHT = 0;
		private static final int LEFT = 1;
		
		
		// ATTRIBUTES-------------------------------------------------------
		
		private int direction;
		
		
		//CONSTRUCTOR-------------------------------------------------------
		
		/**
		 * Creates the OptionBarButtons, which are used to change the values
		 * of various options in the game.
		 * 
		 * @param x	The x-coordinate of the button
		 * @param y The y-coordinate of the button
		 * button about mouse events
		 * @param direction	The direction the button is pointing, if it points
		 * to the LEFT, the button will lower the value. If it points to the 
		 * RIGHT, the button will increase the value.
		 * @param buttonSprite The sprite with which the button will be drawn
		 * @param buttonMask The sprite which is used for the button's collision checking
		 * @param area The area where the object is placed to
		 */
		public OptionBarButton(int x, int y, int direction, Sprite buttonSprite, 
				Sprite buttonMask, Area area)
		{
			super(x, y, DepthConstants.NORMAL, buttonSprite, buttonMask, area);
			
			this.direction = direction;
			
			if (this.direction == LEFT)
				this.setXScale(-1);
		}

		
		// IMPLEMENTENTED METHODS ------------------------------------------

		@Override
		public boolean listensMouseEnterExit()
		{
			return true;
		}

		@Override
		public void onMousePositionEvent(MousePositionEventType eventType, 
				Point2D.Double mousePosition, double eventStepTime)
		{
			// Changes sprite index when mouse enters or exits the button
			if (eventType == MousePositionEventType.ENTER)
				getSpriteDrawer().setImageIndex(1);
			else if (eventType == MousePositionEventType.EXIT)
				getSpriteDrawer().setImageIndex(0);
		}
		
		@Override
		public boolean isVisible()
		{
			if (!super.isVisible())
				return false;
			
			// If the host bar is invisible, so are the buttons
			if (!IntegerOptionBar.this.isVisible())
				return false;
			
			// If the value is already at maximum / minimum, doesn't even 
			// show the button
			if ((this.direction == RIGHT && isAtMax()) || 
					(this.direction == LEFT && isAtMin()))
				return false;
			
			return true;
		}


		@Override
		public void onMouseButtonEvent(MouseButton button, 
				MouseButtonEventType eventType, Point2D.Double mousePosition, 
				double eventStepTime)
		{
			if (button == MouseButton.LEFT && 
					eventType == MouseButtonEventType.PRESSED)
			{
				if(this.direction == LEFT)
					adjustValue(-1);
				else
					adjustValue(1);
			}
		}
	}
}
