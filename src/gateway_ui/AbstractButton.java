package gateway_ui;

import gateway_event.ButtonEvent;
import gateway_event.ButtonEventHandler;
import genesis_event.EventSelector;
import genesis_event.GenesisHandlerType;
import genesis_event.Handled;
import genesis_event.HandlerRelay;
import genesis_event.MouseEvent;
import genesis_event.MouseListener;
import genesis_event.MultiEventSelector;
import genesis_event.StrictEventSelector;
import genesis_event.MouseEvent.MouseButton;
import genesis_event.MouseEvent.MouseButtonEventType;
import genesis_event.MouseEvent.MouseEventType;
import genesis_event.MouseEvent.MouseMovementEventType;
import genesis_util.DependentStateOperator;
import genesis_util.HelpMath;
import genesis_util.SimpleHandled;
import genesis_util.StateOperator;
import genesis_util.Transformation;
import genesis_util.Vector3D;

/**
 * Buttons are used as visual interface elements that can be used with the mouse
 * @author Mikko Hilpinen
 * @since 6.6.2015
 */
public abstract class AbstractButton extends SimpleHandled implements UIComponent,
		MouseListener
{
	// ATTRIBUTES	--------------------------
	
	private EventSelector<MouseEvent> selector;
	private boolean hovering, clicked;
	private ButtonEventHandler listenerHandler;
	private ButtonStatus status;
	private Transformation transformation;
	private int drawingDepth;
	
	
	// CONSTRUCTOR	--------------------------
	
	/**
	 * Creates a new button
	 * @param position The button's original position
	 * @param handlers The handlers that will handle the button
	 * @param drawingDepth The drawing depth used when drawing the button
	 */
	public AbstractButton(Vector3D position, HandlerRelay handlers, int drawingDepth)
	{
		super(handlers);
		
		this.transformation = new Transformation(position);
		this.hovering = false;
		this.clicked = false;
		this.listenerHandler = new ButtonEventHandler(false);
		this.status = ButtonStatus.DEFAULT;
		
		// The object is interested in a) Mouse enter & exit, b) left mouse press & release
		MultiEventSelector<MouseEvent> selector = new MultiEventSelector<>();
		selector.addOption(MouseEvent.createEnterExitSelector());
		StrictEventSelector<MouseEvent, MouseEvent.Feature> leftStateChange = 
				MouseEvent.createButtonStateChangeSelector();
		leftStateChange.addRequiredFeature(MouseButton.LEFT);
		selector.addOption(leftStateChange);
		this.drawingDepth = drawingDepth;
		
		this.selector = selector;
		
		// Buttons have separate visibility and mouse listening operators
		getHandlingOperators().setShouldBeHandledOperator(GenesisHandlerType.DRAWABLEHANDLER, 
				new StateOperator(true, true));
		getHandlingOperators().setShouldBeHandledOperator(GenesisHandlerType.MOUSEHANDLER, 
				new StateOperator(true, true));
	}
	
	
	// ABSTRACT METHODS	------------------------
	
	/**
	 * Changes the button's visual to represent the given button status
	 * @param status The button's new status that should be visually indicated.
	 */
	protected abstract void changeVisualStyle(ButtonStatus status);
	
	
	// IMPLEMENTED METHODS	------------------

	@Override
	public boolean isInAreaOfInterest(Vector3D position)
	{
		return HelpMath.pointIsInRange(getTransformation().inverseTransform(position).plus(
				getOrigin()), Vector3D.zeroVector(), getDimensions());
	}

	@Override
	public EventSelector<MouseEvent> getMouseEventSelector()
	{
		return this.selector;
	}

	@Override
	public void onMouseEvent(MouseEvent event)
	{
		// On mouse enter, goes to hover, on mouse exit, goes out
		if (event.getType() == MouseEventType.MOVEMENT)
		{
			if (event.getMovementEventType() == MouseMovementEventType.ENTER)
				this.hovering = true;
			else
				this.hovering = false;
		}
		else
		{
			// Button can be released anywhere
			if (event.getButtonEventType() == MouseButtonEventType.RELEASED)
			{
				if (this.clicked)
				{
					this.clicked = false;
					getListenerHandler().onButtonEvent(
							ButtonEvent.createButtonReleaseEvent(this));
				}
			}
			// But clicked only on hover over
			else if (this.hovering)
			{
				this.clicked = true;
				getListenerHandler().onButtonEvent(ButtonEvent.createButtonPressEvent(this));
			}
		}
		
		updateStatus();
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
	public int getDepth()
	{
		return this.drawingDepth;
	}
	
	
	// GETTERS & SETTERS	---------------------
	
	/**
	 * @return The buttonEventHandler that informs objects about events originated from this 
	 * button
	 */
	public ButtonEventHandler getListenerHandler()
	{
		return this.listenerHandler;
	}
	
	/**
	 * @return The button's current status
	 */
	public ButtonStatus getCurrentStatus()
	{
		return this.status;
	}
	
	/**
	 * @return Is the button currently being pressed
	 */
	public boolean isPressed()
	{
		return this.clicked;
	}
	
	
	// OTHER METHODS	------------------------
	
	/**
	 * Makes the button dependent from another object
	 * @param other The object the button will depend from
	 */
	public void makeDependentFrom(Handled other)
	{
		getHandlingOperators().makeDependent(other, GenesisHandlerType.MOUSEHANDLER);
		getHandlingOperators().makeDependent(other, GenesisHandlerType.DRAWABLEHANDLER);
		setIsDeadOperator(new DependentStateOperator(other.getIsDeadStateOperator()));
	}
	
	private void updateStatus()
	{
		ButtonStatus style = ButtonStatus.DEFAULT;
		
		if (this.clicked)
			style = ButtonStatus.PRESSED;
		else if (this.hovering)
			style = ButtonStatus.HOVEROVER;
		
		if (style != this.status)
		{
			changeVisualStyle(style);
			this.status = style;
			getListenerHandler().onButtonEvent(ButtonEvent.createButtonStatusChangeEvent(this));
		}
	}
	
	
	// ENUMERATIONS	---------------------
	
	/**
	 * These represent the different statuses the button can have
	 * @author Mikko Hilpinen
	 * @since 6.6.2015
	 */
	public enum ButtonStatus implements ButtonEvent.Feature
	{
		/**
		 * The default button look
		 */
		DEFAULT,
		/**
		 * The look that is presented when the mouse is hovering over the button
		 */
		HOVEROVER,
		/**
		 * The look that is presented when the button is being pressed
		 */
		PRESSED;
		
		
		// METHODS	------------------
		
		/**
		 * @return The index of this style. Default = 0, Hover over = 1, Pressed = 2
		 */
		int getIndex()
		{
			switch (this)
			{
				case PRESSED: return 2;
				case HOVEROVER: return 1;
				default: return 0;
			}
		}
	}
}
