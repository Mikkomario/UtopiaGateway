package gateway_ui;

import genesis_util.HelpMath;
import genesis_util.Vector3D;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.List;

/**
 * TextDrawer draws text over a certain area. The text is broken into lines and paragraphs.
 * 
 * @author Mikko Hilpinen
 * @since 12.3.2014
 */
public class TextDrawer
{
	// ATTRIBUTES	------------------------------------------------------
	
	private List<ParagraphDrawer> paragraphs;
	private Font font;
	private Color color;
	private String paragraphSeparator;
	private Vector3D dimensions, margins, origin;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new textDrawer that can be used to draw text with the given 
	 * parameters
	 * 
	 * @param text The text that will be drawn (can be changed later)
	 * @param paragraphSeparator The string that will indicate a paragraph change (null if 
	 * only a single paragraph is used)
	 * @param font The font with which the text will be drawn
	 * @param textColor The color with which the text will be drawn
	 * @param dimensions The size of the area on which the text is displayed, including 
	 * margins. The real text area may be higher or shorter depending on the text amount.
	 * @param margins The margins placed inside the text area
	 * @param origin The relative origin of the text area
	 */
	public TextDrawer(String text, String paragraphSeparator, Font font, 
			Color textColor, Vector3D dimensions, Vector3D margins, Vector3D origin)
	{
		// Initializes attributes
		
		this.font = font;
		this.color = textColor;
		this.dimensions = dimensions;
		this.origin = origin;
		this.margins = margins;
		this.paragraphSeparator = paragraphSeparator;
		
		setText(text);
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------

	/**
	 * Draws the text according to the drawer's settings
	 * @param g2d The graphics object that does the drawing
	 */
	public void drawText(Graphics2D g2d)
	{
		// Only draws itself if the scaling is not 0
		if (HelpMath.areApproximatelyEqual(g2d.getTransform().getScaleX(), 0) || 
				HelpMath.areApproximatelyEqual(g2d.getTransform().getScaleY(), 0))
			return;
		
		g2d.setColor(this.color);
		
		int h = this.margins.getSecondInt() - this.origin.getSecondInt();
		int x = this.margins.getFirstInt() - this.origin.getFirstInt();
		for (ParagraphDrawer paragraph : this.paragraphs)
		{
			h += paragraph.drawText(g2d, x, h) + 10;
		}
	}

	
	// GETTERS & SETTERS	----------------------------------------------
	
	/**
	 * @return The size of the text area (including margins)
	 */
	public Vector3D getDimensions()
	{
		return this.dimensions;
	}
	
	/**
	 * @return The drawer's relative origin
	 */
	public Vector3D getOrigin()
	{
		return this.origin;
	}
	
	/**
	 * Changes the origin used for the drawer
	 * @param origin The new relative origin
	 */
	public void setOrigin(Vector3D origin)
	{
		this.origin = origin;
	}
	
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
	 * Changes the size of the text area
	 * @param newDimensions The new size of the area
	 * @param scaleOrigin Should the relative origin be scaled accordingly?
	 */
	public void setDimensions(Vector3D newDimensions, boolean scaleOrigin)
	{
		if (scaleOrigin)
		{
			Vector3D scaling = newDimensions.dividedBy(this.dimensions);
			setOrigin(getOrigin().times(scaling));
		}
		
		this.dimensions = newDimensions;
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
			
			float wrappingWidth = getDimensions().getFirstInt() - 2 * 
					TextDrawer.this.margins.getFirstInt();
			
			while (measurer.getPosition() < this.text.length())
			{
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
