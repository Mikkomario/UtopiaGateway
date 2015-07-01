package gateway_ui;

import genesis_util.Vector3D;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import vision_sprite.SingleSpriteDrawer;
import vision_sprite.Sprite;

/**
 * This input bar uses a sprite as the background
 * @author Mikko Hilpinen
 * @since 29.6.2015
 */
public class SpriteMessageBoxInputBar extends AbstractMessageBoxInputBar
{
	// ATTRIBUTES	-----------------------
	
	private SingleSpriteDrawer drawer;
	
	
	// CONSTRUCTOR	-----------------------
	
	/**
	 * Creates a new input bar
	 * @param box The box where this bar will be placed
	 * @param textMargins the margins set before and above the text
	 * @param font The font used for drawing the input
	 * @param textColor The color used for drawing the input
	 * @param height The height of the bar
	 * @param sprite The sprite used for drawing the background
	 */
	public SpriteMessageBoxInputBar(MessageBox box, Vector3D textMargins,
			Font font, Color textColor, int height, Sprite sprite)
	{
		super(box, textMargins, font, textColor, height);
		
		this.drawer = new SingleSpriteDrawer(sprite, this, box.getHandlers());
		this.drawer.setOrigin(Vector3D.zeroVector());
		
		box.addInputBar(this);
	}
	
	
	// IMPLEMENTED METHODS	--------------

	@Override
	protected void setWidth(int newWidth)
	{
		getDrawer().setSprite(getDrawer().getSprite().withDimensions(new Vector3D(newWidth, 
				getHeight())));
	}

	@Override
	protected void drawBackground(Graphics2D g2d)
	{
		if (getDrawer() != null)
			getDrawer().drawSprite(g2d);
	}
	
	
	// OTHER METHODS	-----------------
	
	/**
	 * @return The spriteDrawer that draws the background sprite
	 */
	public SingleSpriteDrawer getDrawer()
	{
		return this.drawer;
	}
}
