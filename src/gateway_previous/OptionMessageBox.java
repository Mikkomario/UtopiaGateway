package gateway_previous;

import genesis_logic.LogicalHandled;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;

import omega_graphic.Sprite;
import omega_graphic.TransformationListener;
import omega_world.Area;

/**
 * OptionMessageBoxes are interactive messageBoxes that show a number of 
 * buttons that represent different options the user can take. A number of 
 * users can listen to the events caused by the user-box-interaction
 * 
 * @author Mikko Hilpinen
 * @since 8.1.2014
 */
public class OptionMessageBox extends MessageBox implements LogicalHandled
{
	// ATTRIBUTES	------------------------------------------------------
	
	private boolean active, autodeath, deactivatesOthers;
	private OptionMessageBoxListener user;
	private Area area;
	
	/**
	 * This template setting is used for boxes that only need to present 
	 * information
	 */
	public static final String[] OKOPTIONS = {"OK"};
	/**
	 * This template setting is used for boxes that require the user to make 
	 * a simple yes no decision
	 */
	public static final String[] YESNOPTIONS = {"Yes", "No"};
	/**
	 * This template setting is used for boxes that allow the user to make 
	 * a yes no decision or cancel the operation
	 */
	public static final String[] YESNOCANCELOPTIONS = {"Yes", "No", "Cancel"};
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * @param x The x-coordinate of the box's center
	 * @param y The y-coordinate of the box's center
	 * @param depth The drawing depth of the box
	 * @param message The message shown on the box. # Marks a paragraph change
	 * @param margin How many empty pixels are left between the edges of the 
	 * box and the text
	 * @param textfont The font with which the message is drawn
	 * @param textcolor The color with which the text is drawn
	 * @param backgroundsprite The sprite used to draw the messageBox
	 * @param options A table containing all the names of the options shown 
	 * as buttons
	 * @param buttonsprite The sprite used to draw the buttons in the box. 
	 * Should contain (at least) 2 subimages so the buttons can react to 
	 * mouse hover
	 * @param diesafteruse Will the optionmessageBox die and disappear after 
	 * one of the options is chosen
	 * @param deactivateOtherComponents Should other objects in the mouse and 
	 * actorhandled be deactivated?
	 * @param user The object that is interested in which of the options 
	 * was pressed (optional)
	 * @param area The area where the object will reside
	 */
	public OptionMessageBox(int x, int y, int depth, int margin, String message,
			Font textfont, Color textcolor, Sprite backgroundsprite, 
			String[] options, Sprite buttonsprite, boolean diesafteruse, 
			boolean deactivateOtherComponents, 
			OptionMessageBoxListener user, Area area)
	{
		super(x, y, depth, margin, message, textfont, textcolor, 
				backgroundsprite, area);
		
		// Checks if the message should deactivate other objects
		if (deactivateOtherComponents)
		{
			area.getActorHandler().inactivate();
			area.getMouseHandler().inactivate();
		}
		
		// Initializes attributes
		this.active = true;
		this.autodeath = diesafteruse;
		this.user = user;
		this.deactivatesOthers = deactivateOtherComponents;
		this.area = area;
		
		int buttony = backgroundsprite.getHeight() - margin - 
				buttonsprite.getHeight() + buttonsprite.getOriginY();
		int minbuttonx = /*MARGIN +*/ buttonsprite.getOriginX();
		int maxbuttonx = backgroundsprite.getWidth() - //MARGIN - 
				buttonsprite.getWidth() + buttonsprite.getOriginX();
		
		// Creates the options
		for (int i = 0; i < options.length; i++)
		{
			int buttonx = (int) (minbuttonx + ((i + 1.0) / 
					(options.length + 1.0)) * (maxbuttonx - minbuttonx));
			
			new OptionButton(buttonx, buttony, buttonsprite, options[i], i, 
					textfont, textcolor, this, this.area);
		}
	}
	
	@Override
	public boolean isActive()
	{
		return this.active;
	}

	@Override
	public void activate()
	{
		this.active = true;
	}

	@Override
	public void inactivate()
	{
		this.active = false;
	}
	
	@Override
	public void kill()
	{
		// Checks if the other objects should be reactivated
		if (this.deactivatesOthers)
		{
			this.area.getMouseHandler().activate();
			this.area.getActorHandler().activate();
		}
			
		super.kill();
	}
	
	
	// OTHER METHODS	-------------------------------------------------
	
	private void onOptionClick(OptionButton option)
	{
		// Informs the listener about the event
		if (this.user != null)
			this.user.onOptionMessageEvent(option.getText(), option.getIndex());
		// Dies if autodeath is on
		if (this.autodeath)
			kill();
	}
	
	
	// SUBCLASSES	-----------------------------------------------------
	
	/**
	 * OptionButton is a simple button that listens to mouse clicks and 
	 * and informs the messagebox if it is clicked.
	 * 
	 * @author Mikko Hilpinen
	 * created 8.1.2014
	 */
	private class OptionButton extends AbstractButton implements 
			TransformationListener
	{
		// ATTRIBUTES	-------------------------------------------------
		
		private Point2D relativeposition;
		private OptionMessageBox box;
		private String text;
		private int index;
		private Font textfont;
		private Color textcolor;
		
		
		// CONSTRUCTOR	-------------------------------------------------
		
		// Note: Relativex and relativey are relative to the top left corner 
		// of the object
		public OptionButton(int relativex, int relativey, Sprite buttonsprite, 
				String text, int index, Font textfont, Color textcolor, 
				OptionMessageBox containerbox, Area area)
		{
			super(0, 0, containerbox.getDepth(), buttonsprite, area);
			
			// Initializes attributes
			this.box = containerbox;
			this.text = text;
			this.index = index;
			this.relativeposition = new Point(relativex, relativey);
			this.textfont = textfont;
			this.textcolor = textcolor;
			
			updatePosition();
			
			// Adds the object to the handler(s)
			this.box.getTransformationListenerHandler().addListener(this);
		}
		
		
		// IMPLEMENTED METHODS	------------------------------------------

		@Override
		public void drawSelfBasic(Graphics2D g2d)
		{
			// Draws the sprite
			super.drawSelfBasic(g2d);
			
			// Also draws some text
			g2d.setFont(this.textfont);
			g2d.setColor(this.textcolor);
			g2d.drawString(this.text, 5, getOriginY() + 5);
			
			// TODO: Change the text position
		}

		@Override
		public boolean isActive()
		{
			return this.box.isActive() && super.isActive();
		}

		@Override
		public void onTransformationEvent(TransformationEvent e)
		{
			// Resets the position
			updatePosition();
		}
		
		@Override
		public double getXScale()
		{
			return this.box.getXScale();
		}
		
		@Override
		public double getYScale()
		{
			return this.box.getYScale();
		}
		
		@Override
		public double getXShear()
		{
			return this.box.getXShear();
		}
		
		@Override
		public double getYShear()
		{
			return this.box.getYShear();
		}
		
		@Override
		public double getAngle()
		{
			return this.box.getAngle();
		}

		@Override
		public boolean listensMouseEnterExit()
		{
			return true;
		}
		
		@Override
		public boolean isVisible()
		{
			return super.isVisible() && this.box.isVisible();
		}
		
		@Override
		public boolean isDead()
		{
			return super.isDead() || this.box.isDead();
		}
		
		@Override
		public void onMouseButtonEvent(MouseButton button,
				MouseButtonEventType eventType, Point2D.Double mousePosition,
				double eventStepTime)
		{
			// If the button was clicked, Informs the box
			if (button == MouseButton.LEFT && 
					eventType == MouseButtonEventType.PRESSED)
				this.box.onOptionClick(this);
		}

		@Override
		public void onMousePositionEvent(MousePositionEventType eventType,
				Point2D.Double mousePosition, double eventStepTime)
		{
			// Button reacts to mouse over by changing sprite index
			if (eventType == MousePositionEventType.ENTER)
				getSpriteDrawer().setImageIndex(1);
			else if (eventType == MousePositionEventType.EXIT)
				getSpriteDrawer().setImageIndex(0);
		}
		
		
		// GETTERS & SETTERS	-----------------------------------------
		
		public int getIndex()
		{
			return this.index;
		}
		
		public String getText()
		{
			return this.text;
		}
		
		
		// OTHER METHODS	---------------------------------------------
		
		private void updatePosition()
		{
			// Keeps the object in the same relative point relative to the 
			// containing box
			Point2D newposition = this.box.transform(this.relativeposition);
			
			setPosition(newposition.getX(), newposition.getY());
		}
	}
}
