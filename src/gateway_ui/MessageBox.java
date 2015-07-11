package gateway_ui;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import gateway_event.ButtonEvent;
import gateway_event.ButtonEvent.ButtonEventType;
import gateway_event.ButtonEventListener;
import genesis_event.EventSelector;
import genesis_event.HandlerRelay;
import genesis_util.DependentStateOperator;
import genesis_util.DepthConstants;
import genesis_util.StateOperator;
import genesis_util.Vector3D;
import omega_util.SimpleGameObject;
import omega_util.Transformation;
import vision_sprite.Sprite;

/**
 * MessageBoxes are simple somewhat abstract tools for presenting information to the user. 
 * MessageBoxes use components like background and buttons, which have to be added separately
 * @author Mikko Hilpinen
 * @since 28.6.2015
 */
public class MessageBox extends SimpleGameObject implements ButtonEventListener, UIComponent
{
	// ATTRIBUTES	------------------------
	
	private TextDrawer textDrawer;
	private Transformation transformation;
	private Vector3D margin, dimensions;
	private Font buttonFont;
	private Color textColor;
	private List<OptionButton> buttons;
	private HandlerRelay handlers;
	private InputBar inputBar;
	
	private StateOperator isVisibleOperator;
	private EventSelector<ButtonEvent> selector;
	
	
	// CONSTRUCTOR	------------------------
	
	/**
	 * Creates a new messagebox that only contains text
	 * 
	 * @param position The position of the boxes top left corner
	 * @param dimensions The width and height of the box
	 * @param margin The horizontal and vertical margins inside the box
	 * @param message The message shown in the box
	 * @param paragraphSeparator The string that separates each paragraph in the text
	 * @param font The font used for drawing the text
	 * @param buttonFont The font used in the boxes buttons
	 * @param textColor The color used for drawing the text
	 * @param handlers The handlers that will handle the box and it's components
	 */
	public MessageBox(Vector3D position, Vector3D dimensions, Vector3D margin, String message, 
			String paragraphSeparator, Font font, Font buttonFont, Color textColor, 
			HandlerRelay handlers)
	{
		super(handlers);
		
		this.transformation = new Transformation(position);
		this.isVisibleOperator = new StateOperator(true, true);
		this.margin = margin;
		this.handlers = handlers;
		this.buttons = new ArrayList<>();
		this.inputBar = null;
		this.dimensions = dimensions;
		this.selector = ButtonEvent.createButtonEventSelector(ButtonEventType.RELEASED);
		this.buttonFont = buttonFont;
		this.textColor = textColor;
		this.textDrawer = new TextDrawer(position, message, paragraphSeparator, buttonFont, 
				textColor, dimensions, margin, getDepth() - 2, handlers);
		
		this.textDrawer.setIsDeadStateOperator(new DependentStateOperator(
				getIsDeadStateOperator()));
		this.textDrawer.setIsActiveStateOperator(new DependentStateOperator(
				getIsActiveStateOperator()));
		this.textDrawer.setIsVisibleStateOperator(new DependentStateOperator(
				getIsVisibleStateOperator()));
	}
	
	
	// IMPLEMENTED METHODS	-------------------------

	@Override
	public Transformation getTransformation()
	{
		return this.transformation;
	}

	@Override
	public void setTrasformation(Transformation t)
	{
		this.transformation = t;
		this.textDrawer.setTrasformation(t);
		
		updateInputBarTransformations();
		updateButtonTransformations();
	}
	
	@Override
	public void onButtonEvent(ButtonEvent e)
	{
		// A button release kills the box
		getIsDeadStateOperator().setState(true);
	}

	@Override
	public StateOperator getListensToButtonEventsOperator()
	{
		return getIsActiveStateOperator();
	}

	@Override
	public EventSelector<ButtonEvent> getButtonEventSelector()
	{
		return this.selector;
	}
	
	@Override
	public StateOperator getIsVisibleStateOperator()
	{
		return this.isVisibleOperator;
	}
	
	@Override
	public Vector3D getDimensions()
	{
		return this.dimensions;
	}

	@Override
	public Vector3D getOrigin()
	{
		return Vector3D.zeroVector();
	}
	
	@Override
	public int getDepth()
	{
		return DepthConstants.TOP + 10;
	}
	
	
	// GETTERS & SETTERS	----------------------
	
	/**
	 * @return The handlers used for handling this box
	 */
	protected HandlerRelay getHandlers()
	{
		return this.handlers;
	}
	
	
	// OTHER METHODS	--------------------------
	
	/**
	 * Adds a button to the box
	 * @param button The button that will be added to the box
	 * @param buttonMargins The margins inside the button
	 * @param message The text shown on the button
	 * @param killOnRelease Should the box die once the button is pressed & released
	 */
	public void addButton(AbstractButton button, Vector3D buttonMargins, String message, 
			boolean killOnRelease)
	{
		// Calculates the new button size
		int buttonWidth = (getDimensions().getFirstInt() - this.margin.getFirstInt() * (2 + 
				this.buttons.size())) / (this.buttons.size() + 1);
		
		// Finds out the highest button size
		int maxButtonHeight = getButtonHeight();
		if (button.getDimensions().getSecondInt() > maxButtonHeight)
			maxButtonHeight = button.getDimensions().getSecondInt();
		
		// Repositions the buttons
		Vector3D nextPosition = new Vector3D(this.margin.getFirst(), 
				getDimensions().getSecond() - this.margin.getSecond() - maxButtonHeight);
		for (OptionButton b : this.buttons)
		{
			nextPosition = nextPosition.plus(new Vector3D(b.reset(nextPosition, buttonWidth) + 
					this.margin.getFirst(), 0));
		}
		
		// Adds the latest button
		OptionButton newButton = new OptionButton(button, nextPosition, buttonWidth, 
				buttonMargins, message);
		this.buttons.add(newButton);
		
		if (killOnRelease)
			button.getListenerHandler().add(this);
		
		// The new button's state becomes dependent from the box
		button.setIsActiveStateOperator(new DependentStateOperator(getIsActiveStateOperator()));
		button.setIsDeadStateOperator(new DependentStateOperator(getIsDeadStateOperator()));
		button.setIsVisibleStateOperator(new DependentStateOperator(getIsVisibleStateOperator()));
		
		// Also updates the button transformations
		updateButtonTransformations();
	}
	
	/**
	 * Adds a new single sprite button to the box
	 * @param buttonSprite The sprite used for drawing the button
	 * @param buttonMargins The margins inside the button
	 * @param message The text shown on the button
	 * @param killOnRelease Should the box die once the button is pressed & released
	 * @return The button that was created in the process
	 */
	public SingleSpriteButton addButton(Sprite buttonSprite, Vector3D buttonMargins, 
			String message, boolean killOnRelease)
	{
		SingleSpriteButton button = new SingleSpriteButton(Vector3D.zeroVector(), 
				getDepth() - 1, buttonSprite, getHandlers());
		
		addButton(button, buttonMargins, message, killOnRelease);
		
		return button;
	}
	
	/**
	 * Adds a new rectangle button to the box
	 * @param lineColors The colors used when drawing the edges (0 - 3 instances)
	 * @param fillColors The colors used when drawing the inner rectangle (0 - 3 instances)
	 * @param buttonSize The size of the button
	 * @param buttonMargins The margins placed left and above the text
	 * @param message The text shown in the button
	 * @param killOnRelease Should the messageBox die once the button is pressed & released
	 * @return The button that was created and added to the box
	 */
	public RectangleButton addButton(Color[] lineColors, Color[] fillColors, 
			Vector3D buttonSize, Vector3D buttonMargins, String message, boolean killOnRelease)
	{
		RectangleButton button = new RectangleButton(Vector3D.zeroVector(), getHandlers(), 
				getDepth() - 1, buttonSize, Vector3D.zeroVector(), lineColors, fillColors);
		
		addButton(button, buttonMargins, message, killOnRelease);
		
		return button;
	}
			
	/**
	 * Adds a new input bar to the message box, removing the previous one
	 * @param inputBar The new input bar that will be placed in the box
	 * @param killPrevious Should the previous input bar (if any) be killed in the process
	 */
	public void addInputBar(InputBar inputBar, boolean killPrevious)
	{
		if (inputBar == null && this.inputBar != null)
		{
			if (killPrevious)
				this.inputBar.getIsDeadStateOperator().setState(true);
			this.inputBar = null;
			return;
		}
		
		if (inputBar.equals(this.inputBar))
			return;
		
		// Destroys the old bar if there is one
		if (this.inputBar != null && killPrevious)
			this.inputBar.getIsDeadStateOperator().setState(true);
		
		this.inputBar = inputBar;
		inputBar.setDimensions(new Vector3D(getDimensions().getFirstInt() - 
				this.margin.getFirstInt() * 2, inputBar.getDimensions().getSecond()));
		inputBar.setIsActiveStateOperator(new DependentStateOperator(getIsActiveStateOperator()));
		inputBar.setIsDeadStateOperator(new DependentStateOperator(getIsDeadStateOperator()));
		inputBar.setIsVisibleStateOperator(new DependentStateOperator(getIsVisibleStateOperator()));
		inputBar.setDepth(getDepth() - 4);
		
		updateInputBarTransformations();
	}
	
	private void updateInputBarTransformations()
	{
		if (this.inputBar == null)
			return;
		
		// Calculates the relative position for the bar's top left corner
		int buttonHeight = getButtonHeight();
		
		int fromBottom;
		if (buttonHeight == 0)
			fromBottom = this.margin.getSecondInt();
		else
			fromBottom = this.margin.getSecondInt() * 2 + buttonHeight;
		
		Vector3D relativeBarPosition = new Vector3D(this.margin.getFirst(), 
				getDimensions().getSecond() - fromBottom - 
				this.inputBar.getDimensions().getSecond());
		Vector3D absoluteBarPosition = getTransformation().transform(relativeBarPosition);
		
		// Applies transformations
		this.inputBar.setTrasformation(getTransformation().withPosition(absoluteBarPosition));
	}
	
	private void updateButtonTransformations()
	{
		for (OptionButton button : this.buttons)
		{
			button.updateTransformation();
		}
	}
	
	private int getButtonHeight()
	{
		int height = 0;
		for (OptionButton button : this.buttons)
		{
			if (button.getDimensions().getSecondInt() > height)
				height = button.getDimensions().getSecondInt();
		}
		
		return height;
	}
	
	
	// SUBCLASSES	------------------------------
	
	private class OptionButton
	{
		// ATTRIBUTES	--------------------------
		
		private AbstractButton button;
		private Vector3D relativePosition, scaling;
		private TextDrawer text;
		
		
		// CONSTRUCTOR	--------------------------
		
		public OptionButton(AbstractButton button, Vector3D relativePosition, 
				int maxWidth, Vector3D margin, String message)
		{
			this.scaling = new Vector3D(1, 1);
			this.button = button;
			this.text = new TextDrawer(Vector3D.zeroVector(), message, null, 
					MessageBox.this.buttonFont, MessageBox.this.textColor, getDimensions(), 
					margin, getDepth() - 2, MessageBox.this.handlers);
			
			StateOperator dependentActive = new DependentStateOperator(
					getIsActiveStateOperator());
			StateOperator dependentDead = new DependentStateOperator(getIsDeadStateOperator());
			StateOperator dependentVisible = new DependentStateOperator(getIsVisibleStateOperator());
			this.button.setIsActiveStateOperator(dependentActive);
			this.button.setIsDeadStateOperator(dependentDead);
			this.button.setIsVisibleStateOperator(dependentVisible);
			this.text.setIsActiveStateOperator(dependentActive);
			this.text.setIsDeadStateOperator(dependentDead);
			this.text.setIsVisibleStateOperator(dependentVisible);
			
			reset(relativePosition, maxWidth);
		}
		
		
		// GETTERS & SETTERS	------------------
		
		public Vector3D getDimensions()
		{
			return this.button.getDimensions().times(this.scaling);
		}
		
		
		// OTHER METHODS	----------------------
		
		public void updateTransformation()
		{
			this.text.setTrasformation(getTransformation().withPosition(
					getAbsoluteTextPosition()));
			
			this.button.setTrasformation(getTransformation().withPosition(
					getAbsoluteButtonPosition()).plus(
					Transformation.scalingTransformation(this.scaling)));
		}
		
		public double reset(Vector3D newRelativePosition, int maxWidth)
		{
			this.relativePosition = newRelativePosition;
			//if (this.dimensions.getFirst() > maxWidth)
			//	this.scaling = new Vector3D(1, 1);
			//else
			this.scaling = new Vector3D(maxWidth / this.button.getDimensions().getFirst(), 1);
			this.text.setDimensions(getDimensions());
			
			updateTransformation();
			
			return this.button.getDimensions().getFirst() * this.scaling.getFirst();
		}
		
		private Vector3D getAbsoluteButtonPosition()
		{
			return getTransformation().transform(this.relativePosition.plus(
					this.button.getOrigin().times(this.scaling)));
		}
		
		private Vector3D getAbsoluteTextPosition()
		{
			return getTransformation().transform(this.relativePosition);
		}
	}
}
