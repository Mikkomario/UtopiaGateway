package gateway_interface;

import java.awt.geom.Point2D;

import omega_graphic.MaskChecker;
import omega_graphic.Sprite;
import omega_world.Area;


/**
 * AbstractMaskButton is an abstract button that in addition to normal button 
 * functions, also handles collision detection with a mask.
 *
 * @author Mikko Hilpinen.
 * @since 30.11.2013.
 */
public abstract class AbstractMaskButton extends AbstractButton
{
	// ATTRIBUTES	------------------------------------------------------
	
	private MaskChecker maskchecker;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new button with the given statistics.
	 *
	 * @param x The x-coordinate of the button
	 * @param y The y-coordinate of the button
	 * @param depth The drawing depth of the button
	 * @param sprite The sprite used to draw the button with
	 * @param mask The mask used to check the collisions
	 * @param area The area where the object is placed to
	 */
	public AbstractMaskButton(int x, int y, int depth, Sprite sprite, 
			Sprite mask, Area area)
	{
		super(x, y, depth, sprite, area);
		
		// Initializes attributes
		this.maskchecker = new MaskChecker(mask);
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------
	
	@Override
	public boolean pointCollides(Point2D testPosition)
	{
		// Uses mask for collision checking
		return (this.maskchecker != null && 
				this.maskchecker.maskContainsRelativePoint(
				negateTransformations(testPosition), 
				getSpriteDrawer().getImageIndex()));
	}
	
	
	// GETTERS & SETTERS	-----------------------------------------------
	
	/**
	 * @return The maskchecker used for collision testing
	 */
	protected MaskChecker getMaskChecker()
	{
		return this.maskchecker;
	}
}
