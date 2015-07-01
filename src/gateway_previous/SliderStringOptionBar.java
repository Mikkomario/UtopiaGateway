package gateway_previous;

import java.awt.Color;
import java.awt.Font;

import omega_graphic.Sprite;
import omega_world.Area;

/**
 * SliderStringOptionBar is an optionBar that uses sliders to change the 
 * value. The values are represented as strings instead of just integers 
 * though they can still be used as integers through their indexes.
 * 
 * @author Mikko Hilpinen
 * @since 18.4.2014
 */
public class SliderStringOptionBar extends SliderIntegerOptionBar
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private String[] options;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new SliderOptionBar with the given statistics and options
	 * 
	 * @param x The x-coordinate of the bar's left side
	 * @param y The y-coordinate of the bar's top
	 * @param defaultIndex The option index that is shown as the default value
	 * @param description The description of the impact of the chosen value
	 * @param textFont The font used to draw the text in the bar
	 * @param textColor The color used to draw the text in the bar
	 * @param sliderBackSprite The sprite used to draw the slider's background
	 * @param sliderHandleSprite The sprite used to draw the slider's handle 
	 * (should have 3 images)
	 * @param options The options that can be chosen using this bar
	 * @param area The area where the bar resides
	 */
	public SliderStringOptionBar(int x, int y, int defaultIndex, 
			String description, Font textFont, Color textColor,
			Sprite sliderBackSprite, Sprite sliderHandleSprite, 
			String[] options, Area area)
	{
		super(x, y, defaultIndex, 0, options.length - 1, description, textFont,
				textColor, sliderBackSprite, sliderHandleSprite, area);
		
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
