package gateway_previous;

import genesis_logic.AdvancedMouseListener;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import omega_gameplay.CollisionType;
import omega_graphic.DimensionalDrawnObject;
import omega_graphic.SingleSpriteDrawer;
import omega_graphic.Sprite;
import omega_world.Area;
import omega_world.Room;
import omega_world.RoomListener;


/**
 * AbstractButton class implements some of the most common functions all the 
 * buttons in the game have while leaving important functionalities to the 
 * subclasses to handle.<p>
 * 
 * The class uses a sprite to draw itself though it doesn't handle animation
 *
 * @author Mikko Hilpinen.
 * @since 30.11.2013.
 */
public abstract class AbstractButton extends DimensionalDrawnObject implements 
		AdvancedMouseListener, RoomListener
{
	// ATTRIBUTES	------------------------------------------------------
	
	private SingleSpriteDrawer spritedrawer;
	private boolean active;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new button to the given position with the given statistics. 
	 * The button uses the given sprite for drawing itself.
	 *
	 * @param x The new x-coordinate of the button (in pixels)
	 * @param y The new y-coordinate of teh button (in pixels)
	 * @param depth The drawing depth of the button
	 * @param sprite The sprite that is used for drawing the button
	 * @param area The area where the object will reside
	 */
	public AbstractButton(int x, int y, int depth, Sprite sprite, Area area)
	{
		super(x, y, depth, false, CollisionType.BOX, area);
		
		// Initializes attributes
		this.active = true;
		
		// Initializes spritedrawer
		this.spritedrawer = new SingleSpriteDrawer(sprite, null, this);
		
		// Adds the button to the handlers
		if (area.getMouseHandler() != null)
			area.getMouseHandler().addMouseListener(this);
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------

	@Override
	public int getWidth()
	{
		if (this.spritedrawer != null)
			return this.spritedrawer.getSprite().getWidth();
		
		return 0;
	}

	@Override
	public int getHeight()
	{
		if (this.spritedrawer != null)
			return this.spritedrawer.getSprite().getHeight();
		
		return 0;
	}

	@Override
	public int getOriginX()
	{
		if (this.spritedrawer != null)
			return this.spritedrawer.getSprite().getOriginX();
		
		return 0;
	}

	@Override
	public int getOriginY()
	{
		if (this.spritedrawer != null)
			return this.spritedrawer.getSprite().getOriginY();
		
		return 0;
	}

	@Override
	public void drawSelfBasic(Graphics2D g2d)
	{
		if (this.spritedrawer == null)
			return;
		
		// Draws the sprite
		this.spritedrawer.drawSprite(g2d, 0, 0);
	}

	@Override
	public void onRoomStart(Room room)
	{
		// Does nothing
	}

	@Override
	public void onRoomEnd(Room room)
	{
		//System.out.println("Button room ends");
		// Dies at the end of the room
		kill();
	}

	@Override
	public boolean isActive()
	{
		return this.active;
	}

	@Override
	public void activate()
	{
		this.active = true;
	}

	@Override
	public void inactivate()
	{
		this.active = false;
	}

	@Override
	public boolean listensPosition(Point2D.Double testPosition)
	{
		return pointCollides(testPosition);
	}

	@Override
	public void onMouseMove(Point2D.Double newMousePosition)
	{
		// Does nothing
	}
	
	@Override
	public MouseButtonEventScale getCurrentButtonScaleOfInterest()
	{
		return MouseButtonEventScale.LOCAL;
	}
	
	@Override
	public Class<?>[] getSupportedListenerClasses()
	{
		// Doens't limit collided classes
		return null;
	}
	
	/*
	@Override
	public void onMousePositionEvent(MousePositionEventType eventType,
			Point2D mousePosition, double eventStepTime)
	{
		// Changes sprite index when mouse enters or exits the button
		if (eventType == MousePositionEventType.ENTER)
			this.spritedrawer.setImageIndex(1);
		else if (eventType == MousePositionEventType.EXIT)
			this.spritedrawer.setImageIndex(0);
	}
	*/
	/*
	@Override
	public void setInvisible()
	{
		System.out.println("Sets " + getClass().getName() + " invisible.");
		super.setInvisible();
	}
	
	@Override
	public void setVisible()
	{
		System.out.println("Sets " + getClass().getName() + " visible.");
		super.setVisible();
	}
	*/
	
	// GETTERS & SETTERS	---------------------------------------------
	
	/**
	 * @return The spritedrawer used in drawing the button or null if the button 
	 * is dead
	 */
	protected SingleSpriteDrawer getSpriteDrawer()
	{
		return this.spritedrawer;
	}
}
