package gateway_event;

import java.util.ArrayList;
import java.util.List;

import gateway_ui.AbstractButton;
import gateway_ui.AbstractButton.ButtonStatus;
import genesis_event.Event;
import genesis_event.StrictEventSelector;

/**
 * ButtonEvents are created when the user interacts with the various buttons
 * @author Mikko Hilpinen
 * @since 6.6.2015
 */
public class ButtonEvent implements Event
{
	// ATTRIBUTES	----------------------
	
	private AbstractButton source;
	private ButtonEventType type;
	private ButtonStatus newStatus;
	
	
	// CONSTRUCTOR	----------------------
	
	private ButtonEvent(AbstractButton source, ButtonEventType type, ButtonStatus newStatus)
	{
		this.type = type;
		this.source = source;
		this.newStatus = newStatus;
	}
	
	/**
	 * Creates a new button press event
	 * @param button The button that was pressed
	 * @return The generated event
	 */
	public static ButtonEvent createButtonPressEvent(AbstractButton button)
	{
		return new ButtonEvent(button, ButtonEventType.PRESSED, button.getCurrentStatus());
	}
	
	/**
	 * Creates a new button release event
	 * @param button The button that was released
	 * @return The generated event
	 */
	public static ButtonEvent createButtonReleaseEvent(AbstractButton button)
	{
		return new ButtonEvent(button, ButtonEventType.RELEASED, button.getCurrentStatus());
	}
	
	/**
	 * Creates a new button status change event
	 * @param button The button that changed
	 * @return The generated event
	 */
	public static ButtonEvent createButtonStatusChangeEvent(AbstractButton button)
	{
		return new ButtonEvent(button, ButtonEventType.STATUSCHANGE, button.getCurrentStatus());
	}
	
	
	// IMPLEMENTED METHODS	--------------

	@Override
	public List<Event.Feature> getFeatures()
	{
		List<Event.Feature> features = new ArrayList<>();
		features.add(getType());
		features.add(getNewStatus());
		
		return features;
	}
	
	
	// GETTERS & SETTERS	-------------
	
	/**
	 * @return The type of action that originated the event
	 */
	public ButtonEventType getType()
	{
		return this.type;
	}
	
	/**
	 * @return The button that originated the event
	 */
	public AbstractButton getSource()
	{
		return this.source;
	}
	
	/**
	 * @return The new status of the button
	 */
	public ButtonStatus getNewStatus()
	{
		return this.newStatus;
	}
	
	
	// OTHER METHODS	-----------------
	
	/**
	 * Creates a new buttonEvent selector that only accepts events of the given type
	 * @param type The type of buttonEvents accepted by this selector
	 * @return An eventSelector that only accepts buttonEvents of the given type
	 */
	public static StrictEventSelector<ButtonEvent, ButtonEvent.Feature> 
			createButtonEventSelector(ButtonEventType type)
	{
		StrictEventSelector<ButtonEvent, Feature> selector = new StrictEventSelector<>();
		selector.addRequiredFeature(type);
		return selector;
	}
	
	/**
	 * Creates a new buttonEvent selector that only accepts button press & button release 
	 * events
	 * @return An eventSelector that only accepts certain button events
	 */
	public static StrictEventSelector<ButtonEvent, ButtonEvent.Feature> 
			createButtonPressReleaseEventSelector()
	{
		StrictEventSelector<ButtonEvent, Feature> selector = new StrictEventSelector<>();
		selector.addUnacceptableFeature(ButtonEventType.STATUSCHANGE);
		return selector;
	}

	
	// INTERFACES	---------------------
	
	/**
	 * This is a wrapper for the features of button events
	 * @author Mikko Hilpinen
	 * @since 6.6.2015
	 */
	public interface Feature extends Event.Feature
	{
		// Used as a wrapper only
	}
	
	
	// ENUMERATIONS	---------------------
	
	/**
	 * The event type describes the action that caused the event
	 * @author Mikko Hilpinen
	 * @since 6.6.2015
	 */
	public enum ButtonEventType implements Feature
	{
		/**
		 * The event was originated from a button press
		 */
		PRESSED,
		/**
		 * The event was originated from a button release
		 */
		RELEASED,
		/**
		 * The event was originated from a status change
		 */
		STATUSCHANGE;
	}
}
