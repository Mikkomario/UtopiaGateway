package gateway_ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import genesis_event.Drawable;
import genesis_util.StateOperator;
import genesis_util.Vector3D;
import omega_util.DependentGameObject;
import omega_util.Transformable;
import omega_util.Transformation;

/**
 * These are bars for collecting and presenting user text input, and are designed to be used 
 * in message boxes
 * @author Mikko Hilpinen
 * @since 28.6.2015
 */
public abstract class AbstractMessageBoxInputBar extends DependentGameObject<MessageBox> 
		implements Drawable, Transformable
{
	// TODO: Use an uiComponentBackground here instead
	
	// ATTRIBUTES	--------------------------
	
	private Transformation transformation;
	private InputReader input;
	private Vector3D textMargins;
	private Font font;
	private Color textColor;
	private int height;
	
	
	// CONSTRUCTOR	--------------------------
	
	/**
	 * Creates a new inputBar. The subclass should add itself to the box when ready.
	 * @param box The box where this bar will be placed
	 * @param textMargins The margins that are placed left and above the input
	 * @param font The font that is used when drawing the input
	 * @param textColor The color of the input text
	 * @param height The height of the bar (pixels)
	 */
	public AbstractMessageBoxInputBar(MessageBox box, Vector3D textMargins, Font font, 
			Color textColor, int height)
	{
		super(box, box.getHandlers());
		
		this.transformation = new Transformation();
		this.input = new InputReader(box.getHandlers());
		this.textMargins = textMargins;
		this.font = font;
		this.textColor = textColor;
		this.height = height;
		
		this.input.setIsActiveStateOperator(getIsActiveStateOperator());
		this.input.setIsDeadStateOperator(getIsDeadStateOperator());
	}
	
	
	// ABSTRACT METHODS	----------------------
	
	/**
	 * Changes the width of the bar (before scaling)
	 * @param newWidth The bar's new width (pixels)
	 */
	protected abstract void setWidth(int newWidth);
	
	/**
	 * This method is called when the background should be drawn. The transformations are 
	 * already in place. The origin of the background should be at the top left corner.
	 * @param g2d The graphics object that will handle the drawing
	 */
	protected abstract void drawBackground(Graphics2D g2d);
	
	
	// IMPLEMENTED METHODS	------------------

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
	public int getDepth()
	{
		return getMaster().getDepth() - 3;
	}

	@Override
	public StateOperator getIsVisibleStateOperator()
	{
		return getMaster().getIsVisibleStateOperator();
	}
	
	@Override
	public void drawSelf(Graphics2D g2d)
	{
		AffineTransform lastTransform = g2d.getTransform();
		getTransformation().transform(g2d);
		
		// Draws the background
		drawBackground(g2d);
		
		// Draws the text
		g2d.setColor(this.textColor);
		g2d.setFont(this.font);
		g2d.drawString(getInput(), this.textMargins.getFirstInt(), 
				this.textMargins.getSecondInt());
		
		g2d.setTransform(lastTransform);
	}
	
	
	// GETTERS & SETTERS	-----------------
	
	/**
	 * @return The input provided by the user
	 */
	public String getInput()
	{
		return this.input.getInput();
	}
	
	/**
	 * @return The height of the bar
	 */
	public int getHeight()
	{
		return this.height;
	}
}
