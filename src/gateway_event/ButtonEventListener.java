package gateway_event;

import genesis_event.EventSelector;
import genesis_event.Handled;
import genesis_util.StateOperator;

/**
 * ButtonEventListeners are interested in button events
 * @author Mikko Hilpinen
 * @since 6.6.2015
 */
public interface ButtonEventListener extends Handled
{
	/**
	 * This methods is called when a buttonEvent occurs that might interest the listener
	 * @param e The event that just occurred
	 */
	public void onButtonEvent(ButtonEvent e);
	
	/**
	 * @return The stateOperator that defines whether the listener is interested in new events 
	 * or not
	 */
	public StateOperator getListensToButtonEventsOperator();
	
	/**
	 * @return The eventSelector that defines whether the listener is interested in an event 
	 * or not.
	 */
	public EventSelector<ButtonEvent> getButtonEventSelector();
}
