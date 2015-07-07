package gateway_ui;

import genesis_event.HandlerRelay;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

/**
 * This background draws a simple rectangle behind the component
 * @author Mikko Hilpinen
 * @since 4.7.2015
 */
public class RectangleUIComponentBackground extends UIComponentBackground
{
	// ATTRIBUTES	--------------------
	
	private Color lineColor, fillColor;
	
	
	// CONSTRUCTOR	--------------------
	
	/**
	 * Creates a new background
	 * @param master The component this background is for
	 * @param handlers The handlers that will handle the background
	 * @param outLineColor The color used for drawing the outLine (null if transparent)
	 * @param fillColor The color used for filling the background (null if transparent)
	 */
	public RectangleUIComponentBackground(UIComponent master, HandlerRelay handlers, 
			Color outLineColor, Color fillColor)
	{
		super(master, handlers);
		
		this.lineColor = outLineColor;
		this.fillColor = fillColor;
	}

	@Override
	public void drawSelf(Graphics2D g2d)
	{
		AffineTransform lastTransform = getMaster().getTransformation().transform(g2d);
		if (this.fillColor != null)
		{
			g2d.setColor(this.fillColor);
			g2d.fillRect(-getMaster().getOrigin().getFirstInt(), 
					-getMaster().getOrigin().getSecondInt(), 
					getMaster().getDimensions().getFirstInt(), 
					getMaster().getDimensions().getSecondInt());
		}
		if (this.lineColor != null)
		{
			g2d.setColor(this.lineColor);
			g2d.drawRect(-getMaster().getOrigin().getFirstInt(), 
					-getMaster().getOrigin().getSecondInt(), 
					getMaster().getDimensions().getFirstInt(), 
					getMaster().getDimensions().getSecondInt());
		}
		g2d.setTransform(lastTransform);
	}
}
