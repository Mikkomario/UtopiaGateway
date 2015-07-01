package gateway_ui;

import genesis_util.Vector3D;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

/**
 * This input bare is visually simple, just a single colour background and text
 * @author Mikko Hilpinen
 * @since 28.6.2015
 */
public class SimpleMessageBoxInputBar extends AbstractMessageBoxInputBar
{
	// ATTRIBUTES	---------------------
	
	private Color backColor, textColor;
	private int width;
	
	
	// CONSTRUCTOR	---------------------
	
	/**
	 * Creates a new input bar that uses the given font and colours
	 * @param box The box that will hold this bar
	 * @param textMargins The margins that will be placed before the text
	 * @param height The height of the bar
	 * @param font The font used for drawing the input text
	 * @param textColor The colour used for drawing the input text
	 * @param backColor The colour used for drawing the text background
	 */
	public SimpleMessageBoxInputBar(MessageBox box, Vector3D textMargins, int height, 
			Font font, Color textColor, Color backColor)
	{
		super(box, textMargins, font, textColor, height);
		
		this.backColor = backColor;
		this.textColor = textColor;
		this.width = 0;
		
		box.addInputBar(this);
	}
	
	
	// IMPLEMENTED METHODS	------------------------

	@Override
	protected void setWidth(int newWidth)
	{
		this.width = newWidth;
	}

	@Override
	protected void drawBackground(Graphics2D g2d)
	{
		g2d.setColor(this.backColor);
		g2d.fillRect(0, 0, this.width, getHeight());
		g2d.setColor(this.textColor);
		g2d.drawRect(0, 0, this.width, getHeight());
	}
}
