package gateway_ui;

import genesis_event.Handled;
import genesis_util.Transformable;
import genesis_util.Vector3D;

/**
 * UiComponents are interface elements that have dimensions. All components should be visual 
 * even if they don't do the drawing themselves.
 * @author Mikko Hilpinen
 * @since 4.7.2015
 */
public interface UIComponent extends Handled, Transformable
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
}
