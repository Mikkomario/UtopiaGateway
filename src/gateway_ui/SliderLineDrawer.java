package gateway_ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import genesis_event.Drawable;
import genesis_event.GenesisHandlerType;
import genesis_event.HandlerRelay;
import genesis_util.ConnectedHandled;
import genesis_util.DependentStateOperator;
import genesis_util.Vector3D;

/**
 * This object simply draws a line on a slider, visualizing the slider's area
 * @author Mikko Hilpinen
 * @since 11.7.2015
 */
public class SliderLineDrawer extends ConnectedHandled<AbstractSliderOption<?>> implements
		Drawable
{
	// ATTRIBUTES	---------------------------
	
	private Color color;
	
	
	// CONSTRUCTOR	---------------------------
	
	/**
	 * Creates a new drawer for the given slider
	 * @param slider The slider this line is drawn on
	 * @param handlers The handlers that will handle this drawer
	 * @param color The color of the drawn line
	 */
	public SliderLineDrawer(AbstractSliderOption<?> slider, HandlerRelay handlers, Color color)
	{
		super(slider, handlers);
		
		this.color = color;
		getHandlingOperators().setShouldBeHandledOperator(GenesisHandlerType.DRAWABLEHANDLER, 
				new DependentStateOperator(
				slider.getHandlingOperators().getShouldBeHandledOperator(
				GenesisHandlerType.DRAWABLEHANDLER)));
	}
	
	
	// IMPLEMENTED METHODS	-------------------

	@Override
	public void drawSelf(Graphics2D g2d)
	{
		AffineTransform lastTransform = getMaster().getTransformation().transform(g2d);
		
		Vector3D start = getMaster().getRelativeSliderStart();
		g2d.setColor(this.color);
		g2d.drawLine(start.getFirstInt(), start.getSecondInt(), 
				start.getFirstInt() + (int) getMaster().getSliderWidth(), start.getSecondInt());
		
		g2d.setTransform(lastTransform);
	}

	@Override
	public int getDepth()
	{
		return getMaster().getDepth() + 1;
	}
}
