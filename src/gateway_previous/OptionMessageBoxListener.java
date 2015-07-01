package gateway_previous;

import genesis_logic.LogicalHandled;

/**
 * OptionMessageBoxListeners use OptionMessageBoxes and react to events 
 * caused by them.
 * 
 * @author Mikko Hilpinen.
 * @since 10.1.2014
 */
public interface OptionMessageBoxListener extends LogicalHandled
{
	/**
	 * This method is called when one of the options is clicked
	 * 
	 * @param clickedoptionname The name of the option that was clicked
	 * @param clickedoptionindex The index of the option that was clicked 
	 * (starting from 0)
	 */
	public void onOptionMessageEvent(String clickedoptionname, 
			int clickedoptionindex);
}