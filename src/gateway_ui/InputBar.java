package gateway_ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import genesis_event.Drawable;
import genesis_event.GenesisHandlerType;
import genesis_event.Handled;
import genesis_event.HandlerRelay;
import genesis_util.DependentStateOperator;
import genesis_util.SimpleHandled;
import genesis_util.StateOperator;
import genesis_util.Transformation;
import genesis_util.Vector3D;

/**
 * InputBars listen for user input on the keyboard and draw it visually
 * @author Mikko Hilpinen
 * @since 7.7.2015
 */
public class InputBar extends SimpleHandled implements UIComponent, Drawable
{
	// ATTRIBUTES	-------------------------
	
	private Transformation transformation;
	private InputReader input;
	private Vector3D textMargins, dimensions;
	private Font font;
	private Color textColor;
	private int drawingDepth;
	
	
	// CONSTRUCTOR	--------------------------
	
	/**
	 * Creates a new inputBar
	 * @param handlers The handlers that will handle the bar and its components
	 * @param textMargins The margins placed left and <b>below</b> the text
	 * @param dimensions The dimensions of the bar
	 * @param font The font used when drawing the text
	 * @param textColor The color used when drawing the text
	 * @param drawingDepth The drawing depth of the bar
	 */
	public InputBar(HandlerRelay handlers, Vector3D textMargins, Vector3D dimensions, 
			Font font, Color textColor, int drawingDepth)
	{
		super(handlers);
		
		this.transformation = new Transformation();
		this.input = new InputReader(handlers);
		this.textMargins = textMargins;
		this.dimensions = dimensions;
		this.font = font;
		this.textColor = textColor;
		this.drawingDepth = drawingDepth;
		
		// The input bar has separate visibility and keyListening states
		getHandlingOperators().setShouldBeHandledOperator(GenesisHandlerType.DRAWABLEHANDLER, 
				new StateOperator(true, true));
		getHandlingOperators().setShouldBeHandledOperator(GenesisHandlerType.KEYHANDLER, 
				new StateOperator(true, true));
		
		this.input.makeDependentFrom(this);
	}
	
	
	// IMPLEMENTED METHODS	-----------------------

	@Override
	public Transformation getTransformation()
	{
		return this.transformation;
	}

	@Override
	public void setTrasformation(Transformation t)
	{
		this.transformation = t;
	}

	@Override
	public Vector3D getDimensions()
	{
		return this.dimensions;
	}

	@Override
	public Vector3D getOrigin()
	{
		return Vector3D.zeroVector();
	}

	@Override
	public int getDepth()
	{
		return this.drawingDepth;
	}
	
	@Override
	public void drawSelf(Graphics2D g2d)
	{
		AffineTransform lastTransform = getTransformation().transform(g2d);
		
		// Draws the text
		g2d.setColor(this.textColor);
		g2d.setFont(this.font);
		g2d.drawString(getInputReader().getInput(), this.textMargins.getFirstInt(), 
				getDimensions().getSecondInt() - this.textMargins.getSecondInt());
		
		g2d.setTransform(lastTransform);
	}

	
	// OTHER METHODS	-----------------------
	
	/**
	 * Makes the input bar dependent from the other object's states
	 * @param other The object this bar will become dependent from
	 */
	public void makeDependentFrom(Handled other)
	{
		setIsDeadOperator(new DependentStateOperator(other.getIsDeadStateOperator()));
		getHandlingOperators().makeDependent(other, GenesisHandlerType.DRAWABLEHANDLER);
		getHandlingOperators().makeDependent(other, GenesisHandlerType.KEYHANDLER);
	}
	
	/**
	 * Changes the size of the bar (before any transformations are applied)
	 * @param newDimensions The bar's new dimensions
	 */
	public void setDimensions(Vector3D newDimensions)
	{
		this.dimensions = newDimensions;
	}
	
	/**
	 * Changes the drawing depth used for the bar
	 * @param drawingDepth The new drawing depth used
	 */
	public void setDepth(int drawingDepth)
	{
		this.drawingDepth = drawingDepth;
	}
	
	/**
	 * @return The inputReader that contains the input shown in the bar
	 */
	public InputReader getInputReader()
	{
		return this.input;
	}
}
