package gateway_ui;

import genesis_event.EventSelector;
import genesis_event.HandlerRelay;
import genesis_event.KeyEvent;
import genesis_event.KeyEvent.ContentType;
import genesis_event.KeyListener;
import genesis_event.KeyEvent.KeyEventType;
import genesis_util.StateOperator;
import omega_util.SimpleGameObject;

/**
 * Inputreader listens to the keyboard and tries to form string depending 
 * on which key was pressed
 *
 * @author Mikko Hilpinen.
 * @since 25.8.2013.
 */
public class InputReader extends SimpleGameObject implements KeyListener
{
	// ATTRIBUTES	-------------------------------------------------------
	
	private StringBuilder input;
	private String inputString;
	private boolean inputUpdated;
	private EventSelector<KeyEvent> selector;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new inputreader
	 * @param handlers The handlers that will handle this object
	 */
	public InputReader(HandlerRelay handlers)
	{
		super(handlers);
		
		// Initializes attributes
		this.input = new StringBuilder();
		this.selector = KeyEvent.createEventTypeSelector(KeyEventType.PRESSED);
		this.inputString = null;
		this.inputUpdated = true;
	}
	
	
	// GETTERS & SETTERS	----------------------------------------------
	
	/**
	 * @return The input the object has received
	 */
	public String getInput()
	{
		if (this.inputUpdated)
		{
			this.inputString = this.input.toString();
			this.inputUpdated = false;
		}
		
		return this.inputString;
	}
	
	/**
	 * Changes the input to a certain value
	 * @param newInput the new input
	 */
	public void setInput(String newInput)
	{
		this.input = new StringBuilder(newInput);
		this.inputUpdated = true;
	}
	
	
	// IMPLEMENTED METHODS	--------------------------------------------------

	@Override
	public EventSelector<KeyEvent> getKeyEventSelector()
	{
		return this.selector;
	}

	@Override
	public StateOperator getListensToKeyEventsOperator()
	{
		return getIsActiveStateOperator();
	}

	@Override
	public void onKeyEvent(KeyEvent event)
	{
		if (event.getKey() == KeyEvent.BACKSPACE)
		{
			if (this.input.length() > 0)
			{
				this.input.deleteCharAt(this.input.length() - 1);
				this.inputUpdated = true;
			}
		}
		else if (event.getContentType() == ContentType.KEYCODE || event.getKeyChar() == 
				KeyEvent.ENTER)
			return;
		else
		{
			this.input.append(event.getKeyChar());
			this.inputUpdated = true;
		}
	}
}
