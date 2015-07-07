package gateway_ui;

import genesis_util.StateOperator;
import genesis_util.Vector3D;
import omega_util.GameObject;
import omega_util.Transformable;

/**
 * UiComponents are interface elements that have dimensions. All components should be visual 
 * even if they don't do the drawing themselves.
 * @author Mikko Hilpinen
 * @since 4.7.2015
 */
public interface UIComponent extends GameObject, Transformable
{
	/**
	 * @return The size of the component (before any transformations are applied)
	 */
	public Vector3D getDimensions();
	
	/**
	 * @return The relative coordinates of the component's origin
	 */
	public Vector3D getOrigin();
	
	/**
	 * @return The drawing depth used for the object
	 */
	public int getDepth();
	
	/**
	 * @return The stateOperator that defines whether the component should be drawn or not
	 */
	public StateOperator getIsVisibleStateOperator();
}
