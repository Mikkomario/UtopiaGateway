package gateway_ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import genesis_event.Drawable;
import genesis_event.HandlerRelay;
import genesis_util.StateOperator;
import genesis_util.StateOperatorListener;
import genesis_util.Vector3D;
import omega_util.SimpleGameObject;
import omega_util.Transformable;
import omega_util.Transformation;

/**
 * OptionBars present an option to the user in a horizontal line
 * @author Mikko Hilpinen
 * @since 29.6.2015
 */
public class OptionBar extends SimpleGameObject implements Drawable,
		Transformable, StateOperatorListener
{
	// ATTRIBUTES	----------------------
	
	private Transformation transformation;
	private String description;
	private Vector3D margins, dimensions;
	private Font textFont;
	private Color textColor;
	private StateOperator isVisibleOperator;
	private int depth;
	private AbstractOption<?> options;
	
	
	// CONSTRUCTOR	----------------------
	
	/**
	 * Creates a new option bar. The bar will initially only contain a description.
	 * @param position The position of the bar's top left corner
	 * @param description The descriptions shown in the bar
	 * @param dimensions The bar's dimensions
	 * @param margins The margins placed inside the bar
	 * @param drawingDepth The drawing depth of the bar
	 * @param font The font used when drawing the description
	 * @param textColor The color used when drawing the description
	 * @param handlers The handlers that will handle the bar
	 */
	public OptionBar(Vector3D position, String description, Vector3D dimensions, 
			Vector3D margins, int drawingDepth, Font font, Color textColor, 
			HandlerRelay handlers)
	{
		super(handlers);
		
		this.transformation = new Transformation(position);
		this.description = description;
		this.margins = margins;
		this.textColor = textColor;
		this.textFont = font;
		this.dimensions = dimensions;
		this.depth = drawingDepth;
		this.isVisibleOperator = new StateOperator(true, true);
		this.options = null;
		
		getIsDeadStateOperator().getListenerHandler().add(this);
		getIsActiveStateOperator().getListenerHandler().add(this);
		getIsVisibleStateOperator().getListenerHandler().add(this);
	}
	
	
	// IMPLEMENTED METHODS	--------------

	@Override
	public Transformation getTransformation()
	{
		return this.transformation;
	}

	@Override
	public void setTrasformation(Transformation t)
	{
		this.transformation = t;
		updateOptionTransformations();
	}

	@Override
	public void drawSelf(Graphics2D g2d)
	{
		AffineTransform lastTransform = getTransformation().transform(g2d);
		
		// Draws the description
		g2d.setFont(this.textFont);
		g2d.setColor(this.textColor);
		g2d.drawString(this.description, this.margins.getFirstInt(), 
				this.dimensions.getSecondInt() - this.margins.getSecondInt());
		
		g2d.setTransform(lastTransform);
	}

	@Override
	public int getDepth()
	{
		return this.depth;
	}

	@Override
	public StateOperator getIsVisibleStateOperator()
	{
		return this.isVisibleOperator;
	}

	@Override
	public void onStateChange(StateOperator source, boolean newState)
	{
		// State changes also affect the options
		if (this.options != null)
		{
			if (source.equals(getIsDeadStateOperator()))
				this.options.getIsDeadStateOperator().setState(newState);
			else if (source.equals(getIsActiveStateOperator()))
				this.options.getIsActiveStateOperator().setState(newState);
			else if (source.equals(getIsVisibleStateOperator()))
				this.options.getIsVisibleStateOperator().setState(newState);
		}
	}
	
	
	// OTHER METHODS	----------------------
	
	/**
	 * Adds an option component to the bar
	 * @param options The options that will be added to the bar
	 */
	public void setOptions(AbstractOption<?> options)
	{
		this.options = options;
		
		double width = this.options.getDimensions().getFirst();
		if (width > this.dimensions.getFirst())
			width = this.dimensions.getFirst();
		
		this.options.setDimensions(new Vector3D(width, this.dimensions.getSecond() - 
				2 * this.margins.getSecond()));
		
		updateOptionTransformations();
	}
	
	private void updateOptionTransformations()
	{
		if (this.options == null)
			return;
		
		// Calculates the option's location (which is at the end of the bar)
		Vector3D relativePos = new Vector3D(this.dimensions.getFirst() - 
				this.margins.getFirst() - this.options.getDimensions().getFirst(), 
				this.margins.getSecond());
		
		this.options.setTrasformation(getTransformation().plus(
				Transformation.transitionTransformation(
				getTransformation().withPosition(Vector3D.zeroVector()).transform(
				relativePos))));
	}
}