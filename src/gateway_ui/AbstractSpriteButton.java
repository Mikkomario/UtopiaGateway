package gateway_ui;

import vision_drawing.DependentSpriteDrawer;
import vision_sprite.SpriteDrawer;
import genesis_event.HandlerRelay;
import genesis_util.HelpMath;
import genesis_util.StateOperator;
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
	private StateOperator isVisibleOperator;
	
	
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
		super(position, handlers);
		
		this.drawer = new DependentSpriteDrawer<>(this, initialDepth, drawer, handlers);
		this.isVisibleOperator = new StateOperator(true, true);
		getDrawer().setIsVisibleOperator(getIsVisibleStateOperator());
	}
	
	
	// IMPLEMENTED METHODS	----------------

	@Override
	public boolean isInAreaOfInterest(Vector3D position)
	{
		if (getDrawer() == null)
			return false;
		
		return HelpMath.pointIsInRange(getTransformation().inverseTransform(position).plus(
				getSpriteDrawer().getSprite().getOrigin()), Vector3D.zeroVector(), 
				getSpriteDrawer().getSprite().getDimensions());
	}

	@Override
	public StateOperator getIsVisibleStateOperator()
	{
		return this.isVisibleOperator;
	}
	
	@Override
	public Vector3D getDimensions()
	{
		return getSpriteDrawer().getSprite().getDimensions();
	}
	
	@Override
	public Vector3D getOrigin()
	{
		return getSpriteDrawer().getOrigin();
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
