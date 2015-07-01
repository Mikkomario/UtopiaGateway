package gateway_ui;

import genesis_event.HandlerRelay;
import genesis_util.Vector3D;
import vision_sprite.MultiSpriteDrawer;
import vision_sprite.Sprite;

/**
 * This button shows an animated sprite for each of the button phases.
 * @author Mikko Hilpinen
 * @since 6.6.2015
 */
public class MultiSpriteButton extends AbstractSpriteButton<MultiSpriteDrawer>
{
	// CONSTRUCTOR	-------------------------
	
	/**
	 * Creates a new button
	 * @param position The original position of the button
	 * @param initialDepth The initial drawing depth of the button
	 * @param sprites The sprites used for drawing the button. The array should contain three 
	 * animated sprites. One for default look, one for mouse hover over and one for button press.
	 * @param handlers The handler that will handle the button
	 */
	public MultiSpriteButton(Vector3D position, int initialDepth, Sprite[] sprites, HandlerRelay handlers)
	{
		super(position, handlers, new MultiSpriteDrawer(sprites, null, handlers), initialDepth);
		
		getSpriteDrawer().setMaster(this);
	}
	
	
	// IMPLEMENTED METHODS	------------------

	@Override
	protected void changeVisualStyle(ButtonStatus status)
	{
		int newIndex = status.getIndex();
		int maxIndex = getDrawer().getSpriteDrawer().getSpriteAmount() - 1;
		
		if (newIndex > maxIndex)
			newIndex = maxIndex;
		
		getDrawer().getSpriteDrawer().setSpriteIndex(newIndex, false);
	}
}
