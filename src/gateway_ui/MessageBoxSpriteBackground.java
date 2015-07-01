package gateway_ui;

import genesis_util.Vector3D;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import vision_sprite.SingleSpriteDrawer;
import vision_sprite.Sprite;

/**
 * This component uses a  sprite for drawing the background for the box
 * @author Mikko Hilpinen
 * @since 28.6.2015
 */
public class MessageBoxSpriteBackground extends MessageBoxBackground
{
	// ATTRIBUTES	--------------------------
	
	private SingleSpriteDrawer drawer;
	
	
	// CONSTRUCTOR	--------------------------
	
	/**
	 * Creates a new background that uses the given sprite (The sprite will be scaled to fill 
	 * the box)
	 * @param box The box that this background is part of
	 * @param sprite The sprite used for drawing the background
	 */
	public MessageBoxSpriteBackground(MessageBox box, Sprite sprite)
	{
		super(box);
		
		this.drawer = new SingleSpriteDrawer(
				sprite.withDimensions(box.getRelativeDimensions()), this, box.getHandlers());
		this.drawer.setOrigin(Vector3D.zeroVector());
	}
	
	
	// IMPLEMENTED METHODS	-----------------

	@Override
	public void drawSelf(Graphics2D g2d)
	{
		// Draws the sprite transformed accordingly
		AffineTransform lastTransform = g2d.getTransform();
		getMaster().getTransformation().transform(g2d);
		
		this.drawer.drawSprite(g2d);
		
		g2d.setTransform(lastTransform);
	}
	
	
	// OTHER METHODS	---------------------
	
	/**
	 * Changes the currently presented image
	 * @param index The index of the new image
	 */
	public void setImageIndex(int index)
	{
		this.drawer.setImageIndex(index);
	}
	
	/**
	 * Changes the animation speed of the sprite
	 * @param speed The sprite's new animation speed (frames / step) (0.1 by default)
	 */
	public void setImageSpeed(double speed)
	{
		this.drawer.setImageSpeed(speed);
	}
}
