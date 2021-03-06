package gateway_ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import genesis_event.Drawable;
import genesis_event.GenesisHandlerType;
import genesis_event.Handled;
import genesis_event.HandlerRelay;
import genesis_util.DependentStateOperator;
import genesis_util.SimpleHandled;
import genesis_util.StateOperator;
import genesis_util.Transformation;
import genesis_util.Vector3D;

/**
 * This option allows the user to pick a value from a set of values
 * @author Mikko Hilpinen
 * @param <T> The type of option chosen in this element
 * @since 29.6.2015
 */
public abstract class AbstractOption<T> extends SimpleHandled implements UIComponent,
		Drawable
{
	// ATTRIBUTES	------------------------
	
	private int depth;
	private Transformation transformation;
	private List<T> options;
	private int currentIndex;
	
	
	// CONSTRUCTOR	------------------------
	
	/**
	 * Creates a new option
	 * @param options The possible values that can be picked
	 * @param defaultIndex The index of the value that is chosen at the beginning
	 * @param drawingDepth The drawing depth of the option
	 * @param handlers The handlers that will handle the option
	 */
	public AbstractOption(Collection<T> options, int defaultIndex, int drawingDepth, 
			HandlerRelay handlers)
	{
		super(handlers);
		
		this.depth = drawingDepth;
		this.transformation = new Transformation();
		this.options = new ArrayList<>();
		this.options.addAll(options);
		this.currentIndex = defaultIndex;
		
		checkIndex();
		
		// The option uses a separate visibility and mouse listening states
		getHandlingOperators().setShouldBeHandledOperator(GenesisHandlerType.DRAWABLEHANDLER, 
				new StateOperator(true, true));
		getHandlingOperators().setShouldBeHandledOperator(GenesisHandlerType.MOUSEHANDLER, 
				new StateOperator(true, true));
	}
	
	// ABSTRACT METHODS	-------------------
	
	/**
	 * Changes the size of the option
	 * @param newDimensions The new size of the option
	 */
	public abstract void setDimensions(Vector3D newDimensions);
	
	
	// IMPLEMENTED METHODS	---------------

	@Override
	public int getDepth()
	{
		return this.depth;
	}

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
	public Vector3D getOrigin()
	{
		return Vector3D.zeroVector();
	}
	
	
	// GETTERS & SETTERS	---------------
	
	/**
	 * Changes the drawing depth of the option
	 * @param depth The new drawing depth used
	 */
	public void setDepth(int depth)
	{
		this.depth = depth;
	}
	
	
	// OTHER METHODS	-------------------
	
	/**
	 * Makes the option dependent from another object's states (alive, mouse & drawing)
	 * @param other The object this option will become dependent from
	 */
	public void makeDependentFrom(Handled other)
	{
		setIsDeadOperator(new DependentStateOperator(other.getIsDeadStateOperator()));
		getHandlingOperators().makeDependent(other, GenesisHandlerType.DRAWABLEHANDLER);
		getHandlingOperators().makeDependent(other, GenesisHandlerType.MOUSEHANDLER);
	}
	
	/**
	 * Adds a new option to the possible options. The option will be added to the end of the 
	 * previous options.
	 * @param option The option that will be added
	 */
	public void addOption(T option)
	{
		if (!this.options.contains(option))
			this.options.add(option);
	}
	
	/**
	 * Adds multiple new options to the possible options available. The new options will be 
	 * added to the end.
	 * @param options The options that will be added
	 */
	public void addOptions(Collection<T> options)
	{
		for (T option : options)
		{
			addOption(option);
		}
	}
	
	/**
	 * @return The currently chosen option
	 */
	public T getCurrentChoise()
	{
		return this.options.get(getCurrentIndex());
	}
	
	/**
	 * @return How many different options there are available
	 */
	public int getOptionAmount()
	{
		return this.options.size();
	}
	
	/**
	 * Changes the currently chosen option
	 * @param newIndex The index of the new choice
	 * @return The given index became the new index
	 */
	public boolean setCurrentIndex(int newIndex)
	{
		this.currentIndex = newIndex;
		return !checkIndex();
	}
	
	/**
	 * Chooses the next possible option (one right)
	 * @return Was the operation successful
	 */
	protected boolean next()
	{
		return setCurrentIndex(getCurrentIndex() + 1);
	}
	
	/**
	 * Chooses the previous possible option (one left)
	 * @return Was the operation successful
	 */
	protected boolean previous()
	{
		return setCurrentIndex(getCurrentIndex() - 1);
	}
	
	/**
	 * @return Is the currently chosen option farthest to the right
	 */
	protected boolean isAtMax()
	{
		return getCurrentIndex() >= getOptionAmount() - 1;
	}
	
	/**
	 * @return Is the currently chosen option farthest to the left
	 */
	protected boolean isAtMin()
	{
		return getCurrentIndex() <= 0;
	}
	
	/**
	 * @return The index of the currently chosen option
	 */
	protected int getCurrentIndex()
	{
		return this.currentIndex;
	}
	
	private boolean checkIndex()
	{
		if (this.currentIndex < 0)
		{
			this.currentIndex = 0;
			return true;
		}
		else if (this.currentIndex >= getOptionAmount())
		{
			this.currentIndex = getOptionAmount() - 1;
			return true;
		}
		
		return false;
	}
	
	
	// SUBCLASSES	------------------------
	
	/**
	 * OptionButton is a wrapper for an abstract button that allows easier scaling and 
	 * dependent states. The optionbuttons are also dependent from the option, so their states 
	 * don't have to be changed separately
	 * @author Mikko Hilpinen
	 * @since 30.6.2015
	 */
	protected class OptionButton
	{
		// ATTRIBUTES	--------------------
		
		private AbstractButton button;
		private Vector3D scaling;
		
		
		// CONSTRUCTOR	--------------------
		
		/**
		 * Creates a new option button
		 * @param button The button wrapped
		 */
		public OptionButton(AbstractButton button)
		{
			this.button = button;
			this.scaling = Vector3D.identityVector();
			
			this.button.makeDependentFrom(AbstractOption.this);
		}
		
		
		// GETTERS & SETTERS	-----------
		
		/**
		 * @return The button wrapped by this object
		 */
		public AbstractButton getButton()
		{
			return this.button;
		}
		
		
		// OTHER METHODS	---------------
		
		/**
		 * @return The origin of the button
		 */
		public Vector3D getOrigin()
		{
			return getButton().getOrigin().times(this.scaling);
		}
		
		/**
		 * @return The size of the button
		 */
		public Vector3D getDimensions()
		{
			return this.button.getDimensions().times(this.scaling);
		}
		
		/**
		 * Changes the button's size
		 * @param newDimensions The button's new size
		 */
		public void setDimensions(Vector3D newDimensions)
		{
			this.scaling = newDimensions.dividedBy(this.button.getDimensions());
		}
		
		/**
		 * Scales the button's size
		 * @param scaling The scaling of the button
		 * @param keepProportions Should the relationship between the button's width and 
		 * height stay the same
		 */
		public void scale(Vector3D scaling, boolean keepProportions)
		{
			if (keepProportions)
			{
				if (scaling.getFirst() < 1 || scaling.getSecond() < 1)
				{
					if (scaling.getFirst() < scaling.getSecond())
						scaling = new Vector3D(scaling.getFirst(), scaling.getFirst());
					else
						scaling = new Vector3D(scaling.getSecond(), scaling.getSecond());
				}
				else
				{
					if (scaling.getFirst() > scaling.getSecond())
						scaling = new Vector3D(scaling.getFirst(), scaling.getFirst());
					else
						scaling = new Vector3D(scaling.getSecond(), scaling.getSecond());
				}
			}
			
			this.scaling = this.scaling.times(scaling);
		}
		
		/**
		 * Updates the button's transformations
		 * @param t The button's new transformation
		 */
		public void setTransformation(Transformation t)
		{
			this.button.setTrasformation(t.plus(Transformation.scalingTransformation(
					this.scaling)));
		}
	}
}
