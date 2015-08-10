package gateway_ui;

import java.awt.Graphics2D;

import genesis_event.Handled;
import genesis_event.HandlerRelay;
import genesis_util.Transformable;
import vision_drawing.AbstractDependentDrawer;

/**
 * This object draws text according to another object's transformations
 * @author Mikko Hilpinen
 * @since 4.8.2015
 * @param <T> The type of object using this drawer
 */
public class DependentTextDrawer<T extends Transformable & Handled> extends AbstractDependentDrawer<T>
{
	// ATTRIBUTES	-------------------
	
	private TextDrawer drawer;
	
	
	// CONSTRUCTOR	-------------------
	
	/**
	 * Creates a new dependent drawer
	 * @param user The object that uses this drawer
	 * @param initialDepth The initial drawing depth of the drawer
	 * @param handlers The handlers that will handle the drawer
	 * @param textDrawer The textDrawer used for drawing the text
	 */
	public DependentTextDrawer(T user, int initialDepth, HandlerRelay handlers, 
			TextDrawer textDrawer)
	{
		super(user, initialDepth, handlers);
		
		this.drawer = textDrawer;
	}
	
	
	// IMPLEMENTED METHODS	------------

	@Override
	protected void drawSelfBasic(Graphics2D g2d)
	{
		if (this.drawer != null)
			this.drawer.drawText(g2d);
	}

	
	// GETTERS & SETTERS	-------------
	
	/**
	 * @return The textDrawer that is used for drawing the text in the object
	 */
	public TextDrawer getTextDrawer()
	{
		return this.drawer;
	}
}
