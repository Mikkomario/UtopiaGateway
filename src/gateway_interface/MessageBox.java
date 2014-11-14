package gateway_interface;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import omega_graphic.DrawnObject;
import omega_graphic.SingleSpriteDrawer;
import omega_graphic.Sprite;
import omega_world.Area;


/**
 * Messageboxes are used to present information to the user in text format. 
 * Messageboxes can be drawn on screen and manipulated like any DrawnObject
 * 
 * @author Mikko Hilpinen
 * @since 8.1.2014
 */
public class MessageBox extends DrawnObject
{
	// ATTRIBUTES	-----------------------------------------------------

	private SingleSpriteDrawer spritedrawer;
	private MultiParagraphDrawer textDrawer;
	private int margin;
	
	
	// CONSTRUCTOR	-----------------------------------------------------

	/**
	 * Creates a new abstract message to the given position with the given 
	 * message and background.
	 * 
	 * @param x The x-coordinate of the box's center
	 * @param y The y-coordinate of the box's center
	 * @param depth The drawing depth of the box
	 * @param margin How many empty pixels are left between the edges of the 
	 * box and the text
	 * @param message The message shown in the box. # marks a paragraph change.
	 * @param textfont The font used in drawing the message
	 * @param textcolor What color is used when drawing the text
	 * @param backgroundsprite The sprite used to draw the background of the 
	 * messageBox
	 * @param area The area where the object will reside
	 */
	public MessageBox(int x, int y, int depth, int margin, String message, 
			Font textfont, Color textcolor, Sprite backgroundsprite, Area area)
	{
		super(x, y, depth, area);

		// Initializes the attributes
		this.margin = margin;
		this.spritedrawer = new SingleSpriteDrawer(backgroundsprite, 
				area.getActorHandler(), this);
		this.textDrawer = new MultiParagraphDrawer(textfont, textcolor, 
				this.spritedrawer.getSprite().getWidth() - margin * 2, 32, this);
		
		this.textDrawer.addText(message, "#");
	}
	
	
	// IMPLEMENTED METHODS	--------------------------------------------

	@Override
	public int getOriginX()
	{
		if (this.spritedrawer == null)
			return 0;
		
		return this.spritedrawer.getSprite().getWidth() / 2;
	}

	@Override
	public int getOriginY()
	{
		if (this.spritedrawer == null)
			return 0;
		
		return this.spritedrawer.getSprite().getHeight() / 2;
	}

	@Override
	public void drawSelfBasic(Graphics2D g2d)
	{
		// TODO: Test the positioning that it's right
		
		// Draws the background
		if (this.spritedrawer != null)
			this.spritedrawer.drawSprite(g2d, 0, 0);
		
		// And the text
		if (this.textDrawer != null)
			this.textDrawer.drawTexts(g2d, this.margin, this.margin);
	}
	
	
	// GETTERS & SETTERS	---------------------------------------------
	/*
	protected int getMargin()
	{
		return this.margin;
	}
	*/
}
