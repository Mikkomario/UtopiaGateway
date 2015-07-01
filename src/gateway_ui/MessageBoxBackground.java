package gateway_ui;

import genesis_event.Drawable;
import genesis_util.StateOperator;
import omega_util.DependentGameObject;

/**
 * This is an abstract component used in a messageBox that should draw the background area 
 * of the box
 * @author Mikko Hilpinen
 * @since 28.6.2015
 */
public abstract class MessageBoxBackground extends DependentGameObject<MessageBox> implements
		Drawable
{
	// CONSTRUCTOR	----------------------
	
	/**
	 * Creates a new background component for a box
	 * @param box The box that will contain this background
	 */
	public MessageBoxBackground(MessageBox box)
	{
		super(box, box.getHandlers());
	}
	
	
	// IMPLEMENTED METHODS	--------------

	@Override
	public int getDepth()
	{
		return getMaster().getDepth() + 1;
	}

	@Override
	public StateOperator getIsVisibleStateOperator()
	{
		return getMaster().getIsVisibleStateOperator();
	}
}
