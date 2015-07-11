package gateway_ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import genesis_event.Drawable;
import genesis_event.HandlerRelay;
import genesis_util.StateOperator;
import genesis_util.Vector3D;

/**
 * This is a simple button that is visually represented as a rectangle the state of the 
 * button is represented with color
 * @author Mikko Hilpinen
 * @since 8.7.2015
 */
public class RectangleButton extends AbstractButton implements Drawable
{
	// ATTRIBUTES	------------------------
	
	private Color[] lineColors, fillColors;
	private int lineColorIndex, fillColorIndex;
	private Vector3D dimensions, origin;
	private StateOperator isVisibleOperator;
	
	
	// CONSTRUCTOR	------------------------
	
	/**
	 * Creates a new button
	 * @param position The button's position
	 * @param handlers The handlers that will handle the button
	 * @param drawingDepth The drawing depth used when drawing the button
	 * @param dimensions The size of the button
	 * @param origin The relative origin coordinates of the button
	 * @param lineColors The colors when for drawing the outer edges (0 - 3 instances)
	 * @param fillColors The colors when for drawing inner rectangle (0 - 3 instances)
	 */
	public RectangleButton(Vector3D position, HandlerRelay handlers,
			int drawingDepth, Vector3D dimensions, Vector3D origin, Color[] lineColors, 
			Color[] fillColors)
	{
		super(position, handlers, drawingDepth);
		
		this.dimensions = dimensions;
		this.origin = origin;
		
		this.lineColorIndex = 0;
		if (lineColors.length == 0)
			this.lineColorIndex = -1;
		this.fillColorIndex = 0;
		if (fillColors.length == 0)
			this.fillColorIndex = -1;
		
		this.lineColors = lineColors;
		this.fillColors = fillColors;
		
		this.isVisibleOperator = new StateOperator(true, true);
	}
	
	
	// IMPLEMENTED METHODS	-----------------

	@Override
	public Vector3D getDimensions()
	{
		return this.dimensions;
	}

	@Override
	public Vector3D getOrigin()
	{
		return this.origin;
	}

	@Override
	public StateOperator getIsVisibleStateOperator()
	{
		return this.isVisibleOperator;
	}

	@Override
	protected void changeVisualStyle(ButtonStatus status)
	{
		switch (status)
		{
			case DEFAULT:
				if (this.lineColorIndex > 0)
					this.lineColorIndex = 0;
				if (this.fillColorIndex > 0)
					this.fillColorIndex = 0;
				break;
			case HOVEROVER:
				if (this.lineColors.length > 1)
					this.lineColorIndex = 1;
				if (this.fillColors.length > 1)
					this.fillColorIndex = 1;
				break;
			case PRESSED:
				this.lineColorIndex = this.lineColors.length - 1;
				this.fillColorIndex = this.fillColors.length - 1;
				break;
		}
	}

	@Override
	public void setIsVisibleStateOperator(StateOperator operator)
	{
		this.isVisibleOperator = operator;
	}

	@Override
	public void drawSelf(Graphics2D g2d)
	{
		AffineTransform lastTransform = getTransformation().transform(g2d);
		
		// The color depends from the button state
		// Draws the button rectangle
		if (this.fillColors != null && this.fillColors.length > 0)
		{
			g2d.setColor(this.fillColors[this.fillColorIndex]);
			g2d.fillRect(getOrigin().getFirstInt(), getOrigin().getSecondInt(), 
					getDimensions().getFirstInt(), getDimensions().getSecondInt());
		}
		if (this.lineColors != null && this.lineColors.length > 0)
		{
			g2d.setColor(this.lineColors[this.lineColorIndex]);
			g2d.drawRect(getOrigin().getFirstInt(), getOrigin().getSecondInt(), 
					getDimensions().getFirstInt(), getDimensions().getSecondInt());
		}
		
		g2d.setTransform(lastTransform);
	}
}
