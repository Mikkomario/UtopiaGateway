package gateway_ui;

import vision_drawing.DependentSpriteDrawer;
import vision_sprite.SpriteDrawer;
import genesis_event.HandlerRelay;
import genesis_util.Vector3D;

/**
 * These buttons use spriteDrawers for visualizing themselves
 * @author Huoltokäyttis
 * @param <SpriteDrawerType> The type of spriteDrawer used by this button
 */
public abstract class AbstractSpriteButton<SpriteDrawerType extends SpriteDrawer> extends AbstractButton
{
	// ATTRIBUTES	------------------------
	
	private DependentSpriteDrawer<AbstractButton, SpriteDrawerType> drawer;
	
	
	// CONSTRUCTOR	------------------------
	
	/**
	 * Creates a new button
	 * @param position The button's original position
	 * @param handlers The handlers that will handle the button
	 * @param drawer The spriteDrawer that is used for drawing the button
	 * @param initialDepth The initial drawing depth of the button
	 */
	public AbstractSpriteButton(Vector3D position, HandlerRelay handlers, 
			SpriteDrawerType drawer, int initialDepth)
	{
		super(position, handlers, initialDepth);
		
		if (drawer != null)
			drawer.setMaster(this);
		this.drawer = new DependentSpriteDrawer<>(this, initialDepth, drawer, handlers);
	}
	
	
	// IMPLEMENTED METHODS	----------------
	
	@Override
	public Vector3D getDimensions()
	{
		if (this.drawer == null)
			return Vector3D.identityVector();
		
		return getSpriteDrawer().getSprite().getDimensions();
	}
	
	@Override
	public Vector3D getOrigin()
	{
		if (this.drawer == null)
			return Vector3D.identityVector();
		
		return getSpriteDrawer().getOrigin();
	}
	
	@Override
	public int getDepth()
	{
		return getDrawer().getDepth();
	}

	
	// GETTERS & SETTERS	-----------------
	
	/**
	 * @return The drawer that draws the object
	 */
	public DependentSpriteDrawer<AbstractButton, SpriteDrawerType> getDrawer()
	{
		return this.drawer;
	}
	
	/**
	 * @return The drawer that draws the object's sprite
	 */
	public SpriteDrawerType getSpriteDrawer()
	{
		return getDrawer().getSpriteDrawer();
	}
}
