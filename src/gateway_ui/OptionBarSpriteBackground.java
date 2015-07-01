package gateway_ui;

import genesis_event.HandlerRelay;
import genesis_util.Vector3D;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import vision_sprite.SingleSpriteDrawer;
import vision_sprite.Sprite;
import vision_sprite.SpriteDrawer;

/**
 * This component draws a background for an optionBar using a sprite
 * @author Mikko Hilpinen
 * @since 1.7.2015
 */
public class OptionBarSpriteBackground extends OptionBarBackground
{
	// TODO: Consider creating an component interface and changing this to componentBackground
	
	// ATTRIBUTES	---------------------
	
	private SingleSpriteDrawer drawer;
	
	
	// CONSTRUCTOR	---------------------
	
	/**
	 * Creates a new background
	 * @param optionBar The optionBar that holds this background
	 * @param handlers The handlers that will handle the background
	 * @param sprite The sprite used for drawing the background
	 */
	public OptionBarSpriteBackground(OptionBar optionBar, HandlerRelay handlers, Sprite sprite)
	{
		super(optionBar, handlers);
		
		this.drawer = new SingleSpriteDrawer(sprite.withDimensions(optionBar.getDimensions()), 
				this, handlers);
		getDrawer().setOrigin(Vector3D.zeroVector());
	}
	
	
	// IMPLEMENTED METHODS	-------------

	@Override
	public void drawSelf(Graphics2D g2d)
	{
		AffineTransform lastTransform = getMaster().getTransformation().transform(g2d);
		getDrawer().drawSprite(g2d);
		g2d.setTransform(lastTransform);
	}

	
	// GETTERS & SETTERS	-------------
	
	/**
	 * @return The drawer that draws the background sprite
	 */
	public SpriteDrawer getDrawer()
	{
		return this.drawer;
	}
}
