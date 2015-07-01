package gateway_ui;

import genesis_event.Drawable;
import genesis_event.HandlerRelay;
import genesis_util.HelpMath;
import genesis_util.StateOperator;
import genesis_util.Vector3D;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.List;

import omega_util.SimpleGameObject;
import omega_util.Transformable;
import omega_util.Transformation;

/**
 * TextDrawer draws text over a certain area. The text is broken into lines and paragraphs.
 * 
 * @author Mikko Hilpinen
 * @since 12.3.2014
 */
public class TextDrawer extends SimpleGameObject implements Drawable, Transformable
{
	// ATTRIBUTES	------------------------------------------------------
	
	private List<ParagraphDrawer> paragraphs;
	private Font font;
	private Color color;
	private int areaWidth, depth;
	private String paragraphSeparator;
	
	private Transformation transformation;
	private StateOperator isVisibleOperator;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new textDrawer that can be used to draw text with the given 
	 * parameters
	 * 
	 * @param position The position of the text's origin
	 * @param text The text that will be drawn (can be changed later)
	 * @param paragraphSeparator The string that will indicate a paragraph change (null if 
	 * only a single paragraph is used)
	 * @param font The font with which the text will be drawn
	 * @param textColor The color with which the text will be drawn
	 * @param textAreaWidth The width of the area the text will cover
	 * @param initialDrawingDepth The drawing depth used for drawing the text
	 * @param handlers The handlers that will handle the drawer
	 */
	public TextDrawer(Vector3D position, String text, String paragraphSeparator, Font font, 
			Color textColor, int textAreaWidth, 
			int initialDrawingDepth, HandlerRelay handlers)
	{	
		super(handlers);
		
		// Initializes attributes
		this.depth = initialDrawingDepth;
		this.font = font;
		this.color = textColor;
		this.areaWidth = textAreaWidth;
		this.transformation = new Transformation(position);
		this.isVisibleOperator = new StateOperator(true, true);
		this.paragraphSeparator = paragraphSeparator;
		
		setText(text);
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------
	
	@Override
	public Transformation getTransformation()
	{
		return this.transformation;
	}

	@Override
	public void setTrasformation(Transformation t)
	{
		this.transformation = t;
	}

	@Override
	public void drawSelf(Graphics2D g2d)
	{
		// Only draws itself if the scaling is not 0
		Vector3D scaling = getTransformation().getScaling();
		if (HelpMath.areApproximatelyEqual(scaling.getFirst(), 0) || 
				HelpMath.areApproximatelyEqual(scaling.getSecond(), 0))
			return;
		
		AffineTransform lastTransform = g2d.getTransform();
		getTransformation().transform(g2d);
		
		g2d.setColor(this.color);
		
		int h = 0;
		for (ParagraphDrawer paragraph : this.paragraphs)
		{
			h += paragraph.drawText(g2d, 0, h) + 10;
		}
		
		g2d.setTransform(lastTransform);
	}

	@Override
	public int getDepth()
	{
		return this.depth;
	}

	@Override
	public StateOperator getIsVisibleStateOperator()
	{
		return this.isVisibleOperator;
	}

	
	// GETTERS & SETTERS	----------------------------------------------
	
	/**
	 * Changes the text that will be drawn
	 * 
	 * @param newText The new text that will be drawn
	 */
	public void setText(String newText)
	{
		this.paragraphs = new ArrayList<>();
		
		if (this.paragraphSeparator == null)
			this.paragraphs.add(new ParagraphDrawer(newText));
		else
		{
			String[] paragraphs = newText.split("\\" + this.paragraphSeparator);
			for (int i = 0; i < paragraphs.length; i++)
			{
				this.paragraphs.add(new ParagraphDrawer(paragraphs[i]));
			}
		}
	}
	
	/**
	 * Changes the width of the text area
	 * @param width The text area's new width
	 */
	public void setWidth(int width)
	{
		this.areaWidth = width;
	}
	
	
	// SUBCLASSES	------------------------------
	
	private class ParagraphDrawer
	{
		// ATTRIBUTES	--------------------------
		
		private String text;
		private AttributedCharacterIterator styledTextIterator;
		
		
		// CONSTRUCTOR	--------------------------
		
		public ParagraphDrawer(String paragraph)
		{
			this.text = paragraph;
			
			// Updates the attributedString
			AttributedString attstring = new AttributedString(this.text);
			attstring.addAttribute(TextAttribute.FONT, TextDrawer.this.font);
			this.styledTextIterator = attstring.getIterator();
		}
		
		
		// OTHER METHODS	----------------------
		
		public int drawText(Graphics2D g2d, int horizontalTranslation, int verticalTranslation)
		{
			// Empty strings are not drawn
			if (this.text.isEmpty())
				return 0;
			
			// From: http://docs.oracle.com/javase/7/docs/api/java/awt/font/LineBreakMeasurer.html
			Point pen = new Point(horizontalTranslation, verticalTranslation);
			FontRenderContext frc = g2d.getFontRenderContext();
			LineBreakMeasurer measurer = new LineBreakMeasurer(this.styledTextIterator, frc);
			
			float wrappingWidth = TextDrawer.this.areaWidth;
			
			while (measurer.getPosition() < this.text.length())
			{
				// TODO: Throws errors when the transformation gets negative
				// TODO: Also, the text layout changes when scaling, which can be a bug as well 
				// as a feature
				TextLayout layout = measurer.nextLayout(wrappingWidth);
			
				// TODO: May randomly throw a nullPointerException
				if (layout == null)
					break;
				
			    pen.y += (layout.getAscent());
			    float dx = layout.isLeftToRight() ?
			    		 0 : (wrappingWidth - layout.getAdvance());
			
			    layout.draw(g2d, pen.x + dx, pen.y);
			    pen.y += layout.getDescent() + layout.getLeading();
			}
			
			return pen.y - verticalTranslation;
			
			// From oracle docs: 
			// http://docs.oracle.com/javase/7/docs/api/java/awt/font/TextLayout.html
			/*
			FontRenderContext frc = g2d.getFontRenderContext();
			TextLayout layout = new TextLayout(this.message, this.font, frc);
			layout.draw(g2d, (float) topLeft.getX(), (float) topLeft.getY());

			Rectangle2D bounds = layout.getBounds();
			bounds.setRect(
					bounds.getX() + topLeft.getX(), 
					bounds.getY() + topLeft.getY(), 
					bounds.getWidth(), bounds.getHeight());
			g2d.draw(bounds);
			*/
			//g2d.setFont(this.font);
			//g2d.drawString(this.message, - getOriginX(), - getOriginY());
		}
	}
}