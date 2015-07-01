package gateway_previous;

import genesis_graphic.DepthConstants;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import omega_graphic.DrawnObject;
import omega_world.Area;
import omega_world.Room;
import omega_world.RoomListener;

/**
 * AbstractOptionBar is an interface element that lets the player choose 
 * an integer value. The actual methods of interaction will be provided by the 
 * subclasses
 * 
 * @author Mikko Hilpinen.
 * @since 18.4.2014
 */
public abstract class AbstractOptionBar extends DrawnObject implements
		RoomListener
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private int value;
	private int minValue;
	private int maxValue;
	private String description;
	private Font font;
	private Color textColor;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Constructs an OptionInterface based on the given parameters.
	 * 
	 * @param x The x-coordinate of the bar's left side (in pixels)
	 * @param y The y-coordinate of the bar's top (in pixels)
	 * @param defaultValue The value the bar will have as default
	 * @param minValue The minimum value the bar can have
	 * @param maxValue The maximum value the bar can have
	 * @param description The description shown in the bar
	 * @param textFont What font the text will use
	 * @param textColor What color the text will have
	 * @param area The area where the object is placed to
	 */
	public AbstractOptionBar(int x, int y, int defaultValue,
			int minValue, int maxValue, String description, Font textFont, 
			Color textColor, Area area)
	{
		super(x, y, DepthConstants.HUD, area);
		
		// Initializes attributes
		this.value = defaultValue;
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.description = description;
		this.textColor = textColor;
		this.font = textFont;
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------

	@Override
	public void onRoomStart(Room room)
	{
		// Does nothing
	}

	@Override
	public void onRoomEnd(Room room)
	{
		// Dies
		kill();
	}

	@Override
	public int getOriginX()
	{
		return 0;
	}

	@Override
	public int getOriginY()
	{
		return 0;
	}

	@Override
	public void drawSelfBasic(Graphics2D g2d)
	{
		// TODO: Make the text margin adjustable
		g2d.setFont(this.font);
		g2d.setColor(this.textColor);
		g2d.drawString(getValuePrint(this.value), 30, 15);
		g2d.drawString(this.description, 150, 15);
	}

	
	//GETTERS & SETTERS ------------------------------------------------
	
	/**
	 * @return The value the user has chosen
	 */
	public int getValue()
	{
		return this.value;
	}
	
	
	// OTHER METHODS	------------------------------------------------
	
	/**
	 * This method defines how the value is drawn. Some subclasses may wish 
	 * to override this method.
	 * 
	 * @param value The value that should be drawn in some manner
	 * @return How the value will be drawn
	 */
	protected String getValuePrint(int value)
	{
		return "" + value;
	}
	
	/**
	 * Changes the option's value by a certain amount
	 * @param adjustment How much the option's value is adjusted
	 */
	protected void adjustValue(int adjustment)
	{
		this.value += adjustment;
		checkValue();
	}
	
	/**
	 * Changes the shown option's value
	 * @param value The new value chosen / shown
	 */
	protected void setValue(int value)
	{
		this.value = value;
		checkValue();
	}
	
	/**
	 * @return Is the interface's value currently at its maximum
	 */
	protected boolean isAtMax()
	{
		return this.value == this.maxValue;
	}
	
	/**
	 * @return Is the interface's value currently at its minimum
	 */
	protected boolean isAtMin()
	{
		return this.value == this.minValue;
	}
	
	private void checkValue()
	{
		if (this.value < this.minValue)
			this.value = this.minValue;
		else if (this.value > this.maxValue)
			this.value = this.maxValue;
	}
}
