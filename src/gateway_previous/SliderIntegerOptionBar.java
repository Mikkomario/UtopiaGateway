package gateway_previous;

import genesis_logic.AdvancedMouseListener;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Point2D.Double;

import omega_gameplay.CollisionType;
import omega_graphic.DimensionalDrawnObject;
import omega_graphic.DrawnObject;
import omega_graphic.SingleSpriteDrawer;
import omega_graphic.Sprite;
import omega_world.Area;

/**
 * SliderOptionBar uses a slider to choose the value.
 * 
 * @author Mikko Hilpinen
 * @since 18.4.2014
 */
public class SliderIntegerOptionBar extends AbstractOptionBar
{
	// ATTRIBUTES	----------------------------------------------------
	
	private SliderBack sliderBack;
	private SliderHandle sliderHandle;
	
	
	// CONSTRUCTOR	----------------------------------------------------
	
	/**
	 * Creates a new OptionBar with the given settings
	 * 
	 * @param x The x-coordinate of the bar's left side (in pixels)
	 * @param y The y-coordinate of the bar's top (in pixels)
	 * @param defaultValue The value the bar will have as default
	 * @param minValue The minimum value the bar can have
	 * @param maxValue The maximum value the bar can have
	 * @param description The description shown in the bar
	 * @param textFont What font the text will use
	 * @param textColor What color the text will have
	 * @param sliderBackSprite The sprite used to draw the background for the 
	 * slider
	 * @param sliderHandleSprite The sprite used to draw the handle for the 
	 * slider (should have 3 images)
	 * @param area The area where the object is placed to
	 */
	public SliderIntegerOptionBar(int x, int y, int defaultValue, int minValue,
			int maxValue, String description, Font textFont, Color textColor,
			Sprite sliderBackSprite, Sprite sliderHandleSprite, Area area)
	{
		super(x, y, defaultValue, minValue, maxValue, description, textFont,
				textColor, area);
		
		// Initializes attributes
		this.sliderBack = new SliderBack(x, y + 15, getDepth() + 2, sliderBackSprite, 100, area);
		this.sliderHandle = new SliderHandle(x, y + 15, getDepth() + 1, 100, 
				minValue, maxValue, defaultValue, sliderHandleSprite, area);
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------
	
	@Override
	public void kill()
	{
		// Also kills the slider
		this.sliderBack.kill();
		this.sliderHandle.kill();
		
		super.kill();
	}

	
	// SUBCLASSES	-----------------------------------------------------
	
	private class SliderBack extends DrawnObject
	{
		// ATTRIBUTES	-------------------------------------------------
		
		private SingleSpriteDrawer spriteDrawer;
		
		
		// CONSTRUCTOR	-------------------------------------------------
		
		public SliderBack(int x, int y, int depth, Sprite backSprite, 
				int sliderWidth, Area area)
		{
			super(x, y, depth, area);
			
			// Initializes attributes
			this.spriteDrawer = new SingleSpriteDrawer(backSprite, 
					area.getActorHandler(), this);
			
			// Sets the sprite to cover a certain area
			setXScale((double) backSprite.getWidth() / sliderWidth);
		}

		
		// IMPLEMENTED METHODS	----------------------------------------
		
		@Override
		public int getOriginX()
		{
			return 0;
		}

		@Override
		public int getOriginY()
		{
			if (this.spriteDrawer == null)
				return 0;
			return this.spriteDrawer.getSprite().getOriginY();
		}

		@Override
		public void drawSelfBasic(Graphics2D g2d)
		{
			// Draws the sprite
			if (this.spriteDrawer != null)
				this.spriteDrawer.drawSprite(g2d, 0, 0);
		}
	}
	
	private class SliderHandle extends DimensionalDrawnObject implements AdvancedMouseListener
	{
		// ATTRIBUTES	-------------------------------------------------
		
		private SingleSpriteDrawer spriteDrawer;
		private int minX, maxX, sliderWidth, maxValue, minValue;
		private boolean grabbed;
		
		
		// CONSTRUCTOR	-------------------------------------------------
		
		public SliderHandle(int minX, int y, int depth, int sliderWidth, 
				int minValue, int maxValue, int defaultValue, Sprite handleSprite, Area area)
		{
			super(minX + (int) (((double) (defaultValue - minValue) / (maxValue - minValue)) * sliderWidth), 
					y, depth, false, CollisionType.BOX, area);
			
			// Initializes attributes
			this.spriteDrawer = new SingleSpriteDrawer(handleSprite, null, this);
			this.minX = minX;
			this.maxX = minX + sliderWidth;
			this.sliderWidth = sliderWidth;
			this.minValue = minValue;
			this.maxValue = maxValue;
			this.grabbed = false;
			
			// Adds the object to the handler(s)
			area.getMouseHandler().addMouseListener(this);
		}
		
		
		// IMPLEMENTED METHODS	-----------------------------------------

		@Override
		public boolean isVisible()
		{
			return super.isVisible() && SliderIntegerOptionBar.this.isVisible();
		}
		
		@Override
		public boolean isActive()
		{
			// The handle is always active as long as its visible
			return isVisible();
		}

		@Override
		public void activate()
		{
			// Does nothing
		}

		@Override
		public void inactivate()
		{
			// Does nothing
		}

		@Override
		public void onMouseButtonEvent(MouseButton button,
				MouseButtonEventType eventType, Double mousePosition,
				double eventStepTime)
		{
			// If left mouse button was pressed, the slider becomes grabbed
			if (button == MouseButton.LEFT && eventType == MouseButtonEventType.PRESSED)
			{
				this.grabbed = true;
				this.spriteDrawer.setImageIndex(2);
			}
			
			// If the mouse button was released, releases the grab
			else if (button == MouseButton.LEFT && eventType == MouseButtonEventType.RELEASED)
			{
				this.grabbed = false;
				
				if (listensPosition(mousePosition))
					this.spriteDrawer.setImageIndex(1);
				else
					this.spriteDrawer.setImageIndex(0);
			}
		}

		@Override
		public boolean listensPosition(Double testedPosition)
		{
			return pointCollides(testedPosition);
		}

		@Override
		public boolean listensMouseEnterExit()
		{
			// Position is only a concern when the button is not grabbed
			return !this.grabbed;
		}

		@Override
		public void onMousePositionEvent(MousePositionEventType eventType,
				Double mousePosition, double eventStepTime)
		{
			// Changes sprite indexes at enter / exit
			if (eventType == MousePositionEventType.ENTER)
				this.spriteDrawer.setImageIndex(1);
			else if (eventType == MousePositionEventType.EXIT)
				this.spriteDrawer.setImageIndex(0);
		}

		@Override
		public void onMouseMove(Double newMousePosition)
		{
			// If the handle is being dragged, moves to a new position and 
			// updates the bar value
			if (this.grabbed)
			{
				setX(newMousePosition.getX());
				
				// Checks if the handle went over the boundaries
				if (getX() < this.minX)
					setX(this.minX);
				else if (getX() > this.maxX)
					setX(this.maxX);
				
				// Updates the value
				int value = this.minValue + (int) Math.round(
						((getX() - this.minX) / this.sliderWidth) * 
						(this.maxValue - this.minValue));
				SliderIntegerOptionBar.this.setValue(value);
			}
		}

		@Override
		public MouseButtonEventScale getCurrentButtonScaleOfInterest()
		{
			// If the object is being dragged, it is interested in global events
			if (this.grabbed)
				return MouseButtonEventScale.GLOBAL;
			
			return MouseButtonEventScale.LOCAL;
		}

		@Override
		public int getOriginX()
		{
			if (this.spriteDrawer == null)
				return 0;
			return this.spriteDrawer.getSprite().getOriginX();
		}

		@Override
		public int getOriginY()
		{
			if (this.spriteDrawer == null)
				return 0;
			return this.spriteDrawer.getSprite().getOriginY();
		}

		@Override
		public void drawSelfBasic(Graphics2D g2d)
		{
			// Draws the sprite
			if (this.spriteDrawer != null)
				this.spriteDrawer.drawSprite(g2d, 0, 0);
		}

		@Override
		public Class<?>[] getSupportedListenerClasses()
		{
			// Doesn't limit the listener classes
			return null;
		}

		@Override
		public int getWidth()
		{
			if (this.spriteDrawer == null)
				return 0;
			return this.spriteDrawer.getSprite().getWidth();
		}

		@Override
		public int getHeight()
		{
			if (this.spriteDrawer == null)
				return 0;
			return this.spriteDrawer.getSprite().getHeight();
		}	
	}
}
