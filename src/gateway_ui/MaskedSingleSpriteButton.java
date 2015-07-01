package gateway_ui;

import genesis_event.HandlerRelay;
import genesis_util.Vector3D;
import vision_sprite.MaskChecker;
import vision_sprite.Sprite;

/**
 * These buttons use a mask for mouse hover-over recognizing
 * @author Mikko Hilpinen
 * @since 26.6.2015
 */
public class MaskedSingleSpriteButton extends SingleSpriteButton
{
	// ATTRIBUTES	----------------------
	
	private MaskChecker maskChecker;
	
	
	// CONSTRUCTOR	----------------------
	
	/**
	 * Creates a new button
	 * @param position The position of the button's origin
	 * @param initialDepth The initial drawing depth of the button
	 * @param sprite The sprite used for drawing the button
	 * @param mask The mask used for button's collsion checks
	 * @param handlers The handlers that will handle the button
	 */
	public MaskedSingleSpriteButton(Vector3D position, int initialDepth,
			Sprite sprite, Sprite mask, HandlerRelay handlers)
	{
		super(position, initialDepth, sprite, handlers);
		
		this.maskChecker = new MaskChecker(mask);
	}
	
	
	// IMPLEMENTED METHODS	-----------------
	
	@Override
	public boolean isInAreaOfInterest(Vector3D position)
	{
		return this.maskChecker.maskContainsRelativePoint(
				getTransformation().inverseTransform(position).plus(
				this.maskChecker.getMask().getOrigin()), getSpriteDrawer().getImageIndex());
	}
}
