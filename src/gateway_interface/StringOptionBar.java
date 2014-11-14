package gateway_interface;

import java.awt.Color;
import java.awt.Font;

import omega_graphic.Sprite;
import omega_world.Area;

/**
 * This OptionBar class offers a choice between multiple strings instead of 
 * just integers. The values are still handled as integers so keep that in 
 * mind when interpreting them.
 * 
 * @author Mikko Hilpinen
 * @since 18.4.2014
 */
public class StringOptionBar extends IntegerOptionBar
{
	// ATTRIBUTES	----------------------------------------------------
	
	private String[] options;
	
	
	// CONSTRUCTOR	----------------------------------------------------
	
	/**
	 * Creates a new optionBar with the given options and settings
	 * 
	 * @param x The x-coordinate of the bar's left side (in pixels)
	 * @param y The y-coordinate of the bar's top (in pixels)
	 * @param options The options presented in the bar
	 * @param defaultIndex Which option is offered as a default value
	 * @param description A short description of the option's effects
	 * @param textFont What font is used to draw the text in the bar
	 * @param textColor What color is used to draw the text in the bar
	 * @param buttonSprite What sprite is used to draw the value adjustment buttons
	 * @param buttonMask What sprite is used for the adjustment button collision checking
	 * @param area The area where the bar resides
	 */
	public StringOptionBar(int x, int y, String[] options, int defaultIndex, 
			String description, Font textFont, Color textColor,
			Sprite buttonSprite, Sprite buttonMask, Area area)
	{
		super(x, y, defaultIndex, 0, options.length - 1, description, textFont,
				textColor, buttonSprite, buttonMask, area);
		
		// Initializes attributes
		this.options = options;
	}

	
	// IMPLEMENTED METHODS	---------------------------------------------
	
	@Override
	protected String getValuePrint(int value)
	{
		// Overrides the print to show the correct option
		return this.options[value];
	}
	
	
	// OTHER METHODS	-------------------------------------------------
	
	/**
	 * @return The option that is currently chosen
	 */
	public String getCurrentOption()
	{
		return this.options[getValue()];
	}
}
