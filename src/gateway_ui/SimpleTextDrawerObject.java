package gateway_ui;

import genesis_event.HandlerRelay;
import genesis_util.SimpleHandled;
import genesis_util.Transformation;
import genesis_util.Vector3D;

/**
 * This object has individual transformations and draws text
 * @author Mikko Hilpinen
 * @since 4.8.2015
 */
public class SimpleTextDrawerObject extends SimpleHandled implements UIComponent
{
	// ATTRIBUTES	--------------------
	
	private Transformation transformation;
	private DependentTextDrawer<SimpleTextDrawerObject> drawer;
	
	
	// CONSTRUCTOR	--------------------
	
	/**
	 * Creates a new textDrawer object with the given text
	 * @param handlers The handlers that will handle the object
	 * @param position The position of the object's origin
	 * @param initialDrawingDepth The drawing depth used for the object
	 * @param textDrawer The textDrawer used when drawing the object's text
	 */
	public SimpleTextDrawerObject(HandlerRelay handlers, Vector3D position, 
			int initialDrawingDepth, TextDrawer textDrawer)
	{
		super(handlers);
		
		this.transformation = new Transformation(position);
		this.drawer = new DependentTextDrawer<SimpleTextDrawerObject>(this, 
				initialDrawingDepth, handlers, textDrawer);
	}
	
	
	// IMPLEMENTED METHODS	-------------

	@Override
	public Transformation getTransformation()
	{
		return this.transformation;
	}

	@Override
	public void setTrasformation(Transformation t)
	{
		this.transformation = t;
	}

	@Override
	public Vector3D getDimensions()
	{
		return getTextDrawer().getDimensions();
	}

	@Override
	public Vector3D getOrigin()
	{
		return getTextDrawer().getOrigin();
	}

	@Override
	public int getDepth()
	{
		return this.drawer.getDepth();
	}
	
	
	// GETTERS & SETTERS	---------------------
	
	/**
	 * @return The textDrawer used when drawing the object
	 */
	public TextDrawer getTextDrawer()
	{
		return this.drawer.getTextDrawer();
	}
	
	/**
	 * Changes the drawing depth of the object
	 * @param depth The object's new drawing depth
	 */
	public void setDepth(int depth)
	{
		this.drawer.setDepth(depth);
	}
	
	/**
	 * Changes the object's origin
	 * @param origin the object's new origin
	 */
	public void setOrigin(Vector3D origin)
	{
		getTextDrawer().setOrigin(origin);
	}
	
	/**
	 * Changes the dimensions of the object
	 * @param dimensions The object's new dimensions
	 */
	public void setDimensions(Vector3D dimensions)
	{
		getTextDrawer().setDimensions(dimensions, true);
	}
}
