package gateway_ui;

import genesis_event.HandlerRelay;
import genesis_util.Vector3D;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Collection;

/**
 * This slider draws the current choice as a string.
 * @author Mikko Hilpinen
 * @since 8.7.2015
 * @param <T> The type of the values that can be chosen through this option
 */
public class SimpleSliderOption<T> extends AbstractSliderOption<T>
{
	// ATTRIBUTES	------------------------
	
	private Font font;
	private Color textColor;
	private Vector3D margins;
	
	
	// CONSTRUCTOR	------------------------
	
	/**
	 * Creates a new slider
	 * @param options The options that can be chosen
	 * @param defaultIndex The index of the option that is chosen at first
	 * @param drawingDepth The drawing depth used when drawing the slider
	 * @param handlers The handlers that will handle the slider
	 * @param dimensions The size of the option
	 * @param sliderHandle The handle of the slider
	 * @param margins The margins inside the option and between text & slider
	 * @param font The font used when drawing the text
	 * @param textColor The color used when drawing the text
	 */
	public SimpleSliderOption(Collection<T> options, int defaultIndex,
			int drawingDepth, HandlerRelay handlers, Vector3D dimensions,
			AbstractButton sliderHandle, Vector3D margins, Font font, Color textColor)
	{
		super(options, defaultIndex, drawingDepth, handlers, dimensions, sliderHandle,
				new Vector3D(margins.getFirst(), margins.getSecond() + 
				(dimensions.getSecond() - margins.getSecond() * 3) / 2), dimensions.getFirst() 
				- 2 * margins.getFirst());
		
		this.font = font;
		this.textColor = textColor;
		this.margins = margins;
	}
	
	
	// IMPLEMENTED METHODS	----------------

	@Override
	public void drawSelf(Graphics2D g2d)
	{
		AffineTransform lastTransform = getTransformation().transform(g2d);
		
		g2d.setColor(this.textColor);
		g2d.setFont(this.font);
		g2d.drawString(getCurrentChoise().toString(), this.margins.getFirstInt(), 
				getDimensions().getSecondInt() - this.margins.getSecondInt());
		
		g2d.setTransform(lastTransform);
	}
	
	@Override
	public void setDimensions(Vector3D newDimensions)
	{
		// Also scales the margins
		Vector3D scaling = newDimensions.dividedBy(getDimensions());
		this.margins = this.margins.times(scaling);
		super.setDimensions(newDimensions);
	}
}
