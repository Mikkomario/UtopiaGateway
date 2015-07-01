package gateway_ui;

import genesis_event.HandlerRelay;
import genesis_util.Vector3D;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Collection;

import vision_sprite.Sprite;

/**
 * This plus minus option presents the chosen value in a string format
 * @author Mikko Hilpinen
 * @since 1.7.2015
 * @param <T> The type of option that can be chosen through this object
 */
public class SimplePlusMinusOption<T> extends AbstractPlusMinusOption<T>
{
	// ATTRIBUTES	--------------------------------
	
	private Font textFont;
	private Color textColor;
	
	
	// CONSTRUCTOR	--------------------------------
	
	/**
	 * Creates a new option.
	 * @param options The options that can be chosen.
	 * @param defaultIndex The index of the option chosen at the beginning
	 * @param drawingDepth The drawing depth of the object
	 * @param handlers The handlers that will handle the object
	 * @param nextButton The button that will scroll the options forward
	 * @param previousButton The button that will scroll the options backwards
	 * @param width The width of the option
	 * @param margins The margins placed inside the option
	 * @param font The font used when drawing the text
	 * @param textColor The color used when drawing the text
	 */
	public SimplePlusMinusOption(Collection<T> options, int defaultIndex,
			int drawingDepth, HandlerRelay handlers, AbstractButton nextButton,
			AbstractButton previousButton, int width, Vector3D margins, Font font, 
			Color textColor)
	{
		super(options, defaultIndex, drawingDepth, handlers, nextButton,
				previousButton, width, margins);
		
		this.textColor = textColor;
		this.textFont = font;
	}
	
	
	// IMPLEMENTED METHODS	--------------------------

	@Override
	public void drawSelf(Graphics2D g2d)
	{
		AffineTransform lastTransform = getTransformation().transform(g2d);
		
		// Draws the current choice as a string
		g2d.setColor(this.textColor);
		g2d.setFont(this.textFont);
		Vector3D relTextPos = getRelativeOptionPosition();//.plus(new Vector3D(0, 
				//getDimensions().getSecond() - 2 * getMargins().getSecond()));
		g2d.drawString(getCurrentChoise().toString(), relTextPos.getFirstInt(), 
				relTextPos.getSecondInt());
		
		g2d.setTransform(lastTransform);
	}
	
	
	// OTHER METHODS	--------------------------
	
	/**
	 * Creates a new option that uses single sprite buttons.
	 * @param options The options that can be chosen.
	 * @param defaultIndex The index of the option chosen at the beginning
	 * @param drawingDepth The drawing depth of the object
	 * @param handlers The handlers that will handle the object
	 * @param nextButtonSprite The sprite used for the button that scrolls forward
	 * @param previousButtonSprite The sprite used for the button that scrolls backwards
	 * @param width The width of the option
	 * @param margins The margins placed inside the option
	 * @param font The font used when drawing the text
	 * @param textColor The color used when drawing the text
	 * @return A new option
	 */
	public static <T> SimplePlusMinusOption<T> createOption(Collection<T> options, 
			int defaultIndex, int drawingDepth, HandlerRelay handlers, 
			Sprite nextButtonSprite, Sprite previousButtonSprite, int width, Vector3D margins, 
			Font font, Color textColor)
	{
		AbstractButton nextButton = new SingleSpriteButton(Vector3D.zeroVector(), 
				drawingDepth - 1, nextButtonSprite, handlers);
		AbstractButton previousButton =  new SingleSpriteButton(Vector3D.zeroVector(), 
				drawingDepth - 1, previousButtonSprite, handlers);
		
		return new SimplePlusMinusOption<>(options, defaultIndex, drawingDepth, handlers, 
				nextButton, previousButton, width, margins, font, textColor);
	}
}
