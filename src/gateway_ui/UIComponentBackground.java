package gateway_ui;

import genesis_event.Drawable;
import genesis_event.HandlerRelay;
import genesis_util.DependentStateOperator;
import genesis_util.StateOperator;
import omega_util.DependentGameObject;

/**
 * These are backgrounds that can be placed behind various ui components. The backgrounds 
 * are dependent from the components.
 * @author Mikko Hilpinen
 * @since 4.7.2015
 */
public abstract class UIComponentBackground extends DependentGameObject<UIComponent> implements
		Drawable
{
	// ATTRIBUTES	-------------------
	
	private StateOperator isVisibleOperator;
	
	
	// CONSTRUCTOR	-------------------
	
	/**
	 * Creates a new background
	 * @param master The component behind which the background will be placed
	 * @param handlers The handlers that will handle the background
	 */
	public UIComponentBackground(UIComponent master, HandlerRelay handlers)
	{
		super(master, handlers);
		
		this.isVisibleOperator = new DependentStateOperator(master.getIsVisibleStateOperator());
	}
	
	
	// IMPLEMENTED METHODS	-----------

	@Override
	public int getDepth()
	{
		return getMaster().getDepth() + 2;
	}

	@Override
	public StateOperator getIsVisibleStateOperator()
	{
		return this.isVisibleOperator;
	}
}
