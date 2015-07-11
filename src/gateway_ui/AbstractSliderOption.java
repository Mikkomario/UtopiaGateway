package gateway_ui;

import java.util.Collection;

import omega_util.Transformation;
import gateway_event.ButtonEvent;
import gateway_event.ButtonEvent.ButtonEventType;
import gateway_event.ButtonEventListener;
import genesis_event.EventSelector;
import genesis_event.HandlerRelay;
import genesis_event.MouseEvent;
import genesis_event.MouseListener;
import genesis_util.DependentStateOperator;
import genesis_util.StateOperator;
import genesis_util.Vector3D;

/**
 * SliderOption uses a slider for choosing the current value
 * @author Mikko Hilpinen
 * @since 8.7.2015
 * @param <T> The type of values that can be chosen through this option
 */
public abstract class AbstractSliderOption<T> extends AbstractOption<T> implements 
		ButtonEventListener, MouseListener
{
	// ATTRIBUTES	----------------------
	
	private Vector3D dimensions, buttonDragPosition, lastMousePosition, sliderStart, 
			relativeSliderPosition;
	private EventSelector<ButtonEvent> buttonEventSelector;
	private EventSelector<MouseEvent> mouseEventSelector;
	private OptionButton sliderButton;
	private double sliderWidth;
	
	
	// CONSTRUCTOR	----------------------
	
	/**
	 * Creates a new option
	 * @param options The options that can be chosen
	 * @param defaultIndex The index that is presented at the beginning
	 * @param drawingDepth The drawing depth used when drawing the option
	 * @param handlers The handlers that will handle the option
	 * @param dimensions The dimensions of the option
	 * @param sliderHandle The button that is used as the slider handle
	 * @param relativeSliderStart The slider's origin's leftmost position (relative to 
	 * position, scales with dimension changes)
	 * @param sliderWidth How much the slider can travel horizontally
	 */
	public AbstractSliderOption(Collection<T> options, int defaultIndex,
			int drawingDepth, HandlerRelay handlers, Vector3D dimensions, 
			AbstractButton sliderHandle, Vector3D relativeSliderStart, double sliderWidth)
	{
		super(options, defaultIndex, drawingDepth, handlers);
		
		this.dimensions = dimensions;
		this.sliderStart = relativeSliderStart;
		this.sliderWidth = sliderWidth;
		
		this.sliderButton = new OptionButton(sliderHandle);
		sliderHandle.getListenerHandler().add(this);
		sliderHandle.setIsActiveStateOperator(new DependentStateOperator(
				getIsActiveStateOperator()));
		sliderHandle.setIsVisibleStateOperator(new DependentStateOperator(
				getIsVisibleStateOperator()));
		sliderHandle.setIsDeadStateOperator(new DependentStateOperator(
				getIsDeadStateOperator()));
		
		this.buttonDragPosition = Vector3D.zeroVector();
		this.lastMousePosition = Vector3D.zeroVector();
		this.buttonEventSelector = ButtonEvent.createButtonEventSelector(
				ButtonEventType.PRESSED);
		this.mouseEventSelector = MouseEvent.createMouseMoveSelector();
		
		resetButtonPosition();
	}
	
	
	// IMPLEMENTED METHODS	--------------

	@Override
	public Vector3D getDimensions()
	{
		return this.dimensions;
	}

	@Override
	public void onButtonEvent(ButtonEvent e)
	{
		// Remembers the mouse coordinates relative to the button
		this.buttonDragPosition = this.lastMousePosition.minus(
				this.sliderButton.getButton().getTransformation().getPosition());
	}

	@Override
	public StateOperator getListensToButtonEventsOperator()
	{
		return getIsActiveStateOperator();
	}

	@Override
	public EventSelector<ButtonEvent> getButtonEventSelector()
	{
		return this.buttonEventSelector;
	}

	@Override
	public void setDimensions(Vector3D newDimensions)
	{
		// Also scales the slider area
		Vector3D scaling = newDimensions.dividedBy(this.dimensions);
		this.sliderStart = this.sliderStart.times(scaling);
		this.sliderWidth *= scaling.getFirst();
		
		/*
		Vector3D buttonScaling = scaling;
		if (buttonScaling.getFirst() < buttonScaling.getSecond())
			buttonScaling = new Vector3D(buttonScaling.getFirst(), buttonScaling.getFirst());
		else
			buttonScaling = new Vector3D(buttonScaling.getSecond(), buttonScaling.getSecond());
		this.sliderButton.setDimensions(this.sliderButton.getDimensions().times(buttonScaling));
		*/
		
		this.dimensions = newDimensions;
		
		resetButtonPosition();
	}

	@Override
	public StateOperator getListensToMouseEventsOperator()
	{
		return getIsActiveStateOperator();
	}

	@Override
	public EventSelector<MouseEvent> getMouseEventSelector()
	{
		return this.mouseEventSelector;
	}

	@Override
	public boolean isInAreaOfInterest(Vector3D position)
	{
		return false;
	}

	@Override
	public void onMouseEvent(MouseEvent event)
	{
		// If the button is being pressed, moves it
		if (this.sliderButton.getButton().isPressed())
		{
			Vector3D newRelativeSliderPos = getTransformation().inverseTransform(
					event.getPosition().minus(this.buttonDragPosition));
			
			// TODO: Try using rounding instead of casting to int
			setCurrentIndex((int) Math.round(newRelativeSliderPos.getFirst() * 
					(getOptionAmount() - 1) / this.sliderWidth));
			
			resetButtonPosition();
		}
		
		this.lastMousePosition = event.getPosition();
		
		//System.out.println(this.relativeSliderPosition.minus(this.sliderStart));
		//System.out.println(this.sliderButton.getDimensions().dividedBy(this.sliderButton.getOrigin()));
		//System.out.println(this.sliderButton.getButton().getDimensions().dividedBy(this.sliderButton.getButton().getOrigin()));
	}
	
	@Override
	public void setTrasformation(Transformation t)
	{
		super.setTrasformation(t);
		updateButtonTransformations();
	}
	
	
	// GETTERS & SETTERS 	------------------
	
	/**
	 * @return The relative position of the leftmost end of the slider
	 */
	public Vector3D getRelativeSliderStart()
	{
		return this.sliderStart;
	}
	
	/**
	 * @return The width of the slider area in pixels (transformations are not applied here)
	 */
	public double getSliderWidth()
	{
		return this.sliderWidth;
	}
	
	
	// OTHER METHODS	---------------------
	
	private void updateButtonTransformations()
	{
		Vector3D absoluteSliderPosition = getTransformation().transform(this.relativeSliderPosition);
		//System.out.println(absoluteSliderPosition.minus(getTransformation().getPosition()).minus(this.relativeSliderPosition));
		//System.out.println(getTransformation().getPosition());
		this.sliderButton.setTransformation(getTransformation().withPosition(absoluteSliderPosition));
	}
	
	private void resetButtonPosition()
	{
		double valuePosition;
		if (getOptionAmount() < 2)
			valuePosition = this.sliderWidth / 2;
		else
			valuePosition = this.sliderWidth * (getCurrentIndex() / (getOptionAmount() - 1.0));
		
		this.relativeSliderPosition = this.sliderStart.plus(new Vector3D(valuePosition, 0));
		
		updateButtonTransformations();
	}
}
