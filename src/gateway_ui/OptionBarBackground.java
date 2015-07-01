package gateway_ui;

import genesis_event.Drawable;
import genesis_event.HandlerRelay;
import genesis_util.StateOperator;
import omega_util.DependentGameObject;

/**
 * OptionBarBackgrounds are backgrounds placed behind option bars
 * @author Mikko Hilpinen
 * @since 1.7.2015
 */
public abstract class OptionBarBackground extends DependentGameObject<OptionBar> implements
		Drawable
{
	// CONSTRUCTOR	------------------------
	
	/**
	 * Creates a new background
	 * @param optionBar The optionBar to which this background is linked
	 * @param handlers
	 */
	public OptionBarBackground(OptionBar optionBar, HandlerRelay handlers)
	{
		super(optionBar, handlers);
	}
	
	
	// IMPLEMENTED METHODS	----------------

	@Override
	public int getDepth()
	{
		return getMaster().getDepth() + 5;
	}

	@Override
	public StateOperator getIsVisibleStateOperator()
	{
		return getMaster().getIsVisibleStateOperator();
	}
}
