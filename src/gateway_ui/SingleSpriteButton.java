package gateway_ui;

import genesis_event.HandlerRelay;
import genesis_util.Vector3D;
import vision_sprite.SingleSpriteDrawer;
import vision_sprite.Sprite;

/**
 * This button uses a single sprite frame to represent a single button status
 * @author Mikko Hilpinen
 * @since 6.6.2015
 */
public class SingleSpriteButton extends AbstractSpriteButton<SingleSpriteDrawer>
{	
	// CONSTRUCTOR	--------------------
	
	/**
	 * Creates a new button with the given sprite
	 * @param position The button's position
	 * @param initialDepth The initial drawing depth of the button
	 * @param sprite The sprite used for drawing the button. The sprite should have 3 
	 * sub-images. One for default look, one for mouse over and one for pressed. In that order.
	 * @param handlers The handlers that will handle the button
	 */
	public SingleSpriteButton(Vector3D position, int initialDepth, Sprite sprite, 
			HandlerRelay handlers)
	{
		super(position, handlers, new SingleSpriteDrawer(sprite, null, handlers), initialDepth);
		
		// Sets the drawer
		getSpriteDrawer().setMaster(this);
		getSpriteDrawer().setImageSpeed(0);
		getSpriteDrawer().setImageIndex(0);
	}
	
	
	// IMPLEMENTED METHODS	------------

	@Override
	protected void changeVisualStyle(ButtonStatus status)
	{
		int newIndex = status.getIndex();
		int maxIndex = getSpriteDrawer().getSprite().getImageNumber() - 1;
		
		if (newIndex > maxIndex)
			newIndex = maxIndex;
		
		getSpriteDrawer().setImageIndex(newIndex);
	}
}
