package gateway_ui;

import gateway_event.ButtonEvent;
import gateway_event.ButtonEvent.ButtonEventType;
import gateway_event.ButtonEventListener;
import genesis_event.EventSelector;
import genesis_event.HandlerRelay;
import genesis_util.StateOperator;
import genesis_util.Vector3D;

import java.util.Collection;

import omega_util.Transformation;

/**
 * This option type presents two buttons that allow the user to scroll backwards and forwards 
 * through the options
 * @author Mikko Hilpinen
 * @since 29.6.2015
 * @param <T> The type of object presented as an option
 */
public abstract class AbstractPlusMinusOption<T> extends AbstractOption<T> implements 
		ButtonEventListener
{
	// ATTRIBUTES	-----------------------
	
	private OptionButton nextButton, previousButton;
	private Vector3D dimensions, margins;
	private EventSelector<ButtonEvent> selector;
	
	
	// CONSTRUCTOR	-----------------------
	
	/**
	 * Creates a new option
	 * @param options The options that can be chosen
	 * @param defaultIndex The index of the option shown at first
	 * @param drawingDepth The drawing depth used
	 * @param handlers The handlers that will handle the option
	 * @param nextButton The button that is used for choosing the next option
	 * @param previousButton The button that is used for choosing the previous option
	 * @param width The width of the options
	 * @param margins The margins placed inside the options
	 */
	public AbstractPlusMinusOption(Collection<T> options, int defaultIndex,
			int drawingDepth, HandlerRelay handlers, AbstractButton nextButton, 
			AbstractButton previousButton, int width, Vector3D margins)
	{
		super(options, defaultIndex, drawingDepth, handlers);
		
		this.selector = ButtonEvent.createButtonEventSelector(ButtonEventType.RELEASED);
		this.margins = margins;
		this.nextButton = new OptionButton(nextButton);
		this.previousButton = new OptionButton(previousButton);
		
		double maxHeight = this.nextButton.getDimensions().getSecond();
		if (this.previousButton.getDimensions().getSecond() > maxHeight)
		{
			maxHeight = this.previousButton.getDimensions().getSecond();
			this.nextButton.setDimensions(new Vector3D(
					this.nextButton.getDimensions().getFirst(), maxHeight));
		}
		else
			this.previousButton.setDimensions(new Vector3D(
					this.previousButton.getDimensions().getFirst(), maxHeight));
		
		this.dimensions = new Vector3D(width, maxHeight);
		
		nextButton.getListenerHandler().add(this);
		previousButton.getListenerHandler().add(this);
		
		updateButtonTransformations();
	}
	
	
	// IMPLEMENTED METHODS	----------------

	@Override
	public Vector3D getDimensions()
	{
		return this.dimensions;
	}

	@Override
	public void setDimensions(Vector3D newDimensions)
	{
		Vector3D scale = newDimensions.dividedBy(getDimensions());
		this.dimensions = newDimensions;
		
		// Also scales the buttons
		this.nextButton.scale(scale);
		this.previousButton.scale(scale);
	}

	@Override
	public void onButtonEvent(ButtonEvent e)
	{
		if (e.getSource().equals(this.previousButton.getButton()))
			previous();
		else
			next();
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
	public void setTrasformation(Transformation t)
	{
		super.setTrasformation(t);
		updateButtonTransformations();
	}
	
	
	// OTHER METHODS	--------------------
	
	/**
	 * @return The relative position where the option should be visualized
	 */
	protected Vector3D getRelativeOptionPosition()
	{
		return this.margins.plus(this.previousButton.getDimensions());
	}
	
	private void updateButtonTransformations()
	{
		Vector3D relPosPrev = this.margins.plus(this.previousButton.getOrigin());
		Vector3D relPosNext = new Vector3D(getDimensions().getFirst() - 
				this.margins.getFirst(), this.margins.getSecond()).plus(
				this.nextButton.getOrigin());
		
		// TODO: WET WET
		this.previousButton.setTransformation(getTransformation().plus(
				Transformation.transitionTransformation(getTransformation().transform(
				relPosPrev))));
		this.nextButton.setTransformation(getTransformation().plus(
				Transformation.transitionTransformation(getTransformation().transform(
				relPosNext))));
	}
}
