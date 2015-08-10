package gateway_ui;

import genesis_event.Drawable;
import genesis_event.HandlerRelay;
import genesis_util.ConnectedHandled;

/**
 * These are backgrounds that can be placed behind various ui components. The backgrounds 
 * are dependent from the components.
 * @author Mikko Hilpinen
 * @since 4.7.2015
 */
public abstract class UIComponentBackground extends ConnectedHandled<UIComponent> implements
		Drawable
{
	// CONSTRUCTOR	-------------------
	
	/**
	 * Creates a new background
	 * @param master The component behind which the background will be placed
	 * @param handlers The handlers that will handle the background
	 */
	public UIComponentBackground(UIComponent master, HandlerRelay handlers)
	{
		super(master, handlers);
	}
	
	
	// IMPLEMENTED METHODS	-----------

	@Override
	public int getDepth()
	{
		return getMaster().getDepth() + 2;
	}
}
