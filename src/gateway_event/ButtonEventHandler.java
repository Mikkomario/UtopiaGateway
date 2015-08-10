package gateway_event;

import genesis_event.EventSelector;
import genesis_event.Handler;
import genesis_event.HandlerRelay;
import genesis_event.HandlerType;
import genesis_event.StrictEventSelector;

/**
 * ButtonEventHandlers inform multiple buttonListeners about buttonEvents.
 * @author Mikko Hilpinen
 * @since 6.6.2015
 */
public class ButtonEventHandler extends Handler<ButtonEventListener> implements
		ButtonEventListener
{
	// ATTRIBUTES	-------------------------
	
	private ButtonEvent lastEvent;
	private EventSelector<ButtonEvent> selector;
	
	
	// CONSTRUCTOR	-------------------------
	
	/**
	 * Creates a new buttonEventHandler
	 * @param autoDeath Will the handler die once it runs out of handled objects
	 * @param superHandlers The handlers that will handle this handler (buttonEventHandler)
	 */
	public ButtonEventHandler(boolean autoDeath, HandlerRelay superHandlers)
	{
		super(autoDeath, superHandlers);
		
		initialize();
	}
	
	/**
	 * Creates a new buttonEventHandler
	 * @param autoDeath Will the handler die once it runs out of handled objects
	 * @param superHandler The buttonEventHandler that will inform this handler about button 
	 * events
	 */
	public ButtonEventHandler(boolean autoDeath, ButtonEventHandler superHandler)
	{
		super(autoDeath);
		
		initialize();
		
		if (superHandler != null)
			superHandler.add(this);
	}
	
	/**
	 * Creates a new buttonEventHandler. The handler must be informed of the events manually.
	 * @param autoDeath Will the handler die once it runs out of handled objects
	 */
	public ButtonEventHandler(boolean autoDeath)
	{
		super(autoDeath);
		
		initialize();
	}
	
	
	// IMPLEMENTED METHODS	-------------------------

	@Override
	public void onButtonEvent(ButtonEvent e)
	{
		// Informs the listeners
		this.lastEvent = e;
		handleObjects(true);
		this.lastEvent = null;
	}

	@Override
	public EventSelector<ButtonEvent> getButtonEventSelector()
	{
		return this.selector;
	}

	@Override
	public HandlerType getHandlerType()
	{
		return GatewayHandlerType.BUTTONEVENTHANDLER;
	}

	@Override
	protected boolean handleObject(ButtonEventListener h)
	{
		// Only informs active listeners that select the current event
		if (h.getButtonEventSelector().selects(this.lastEvent))
			h.onButtonEvent(this.lastEvent);
		
		return true;
	}

	
	// OTHER METHODS	---------------------
	
	private void initialize()
	{
		this.lastEvent = null;
		this.selector = new StrictEventSelector<>();
	}
}
