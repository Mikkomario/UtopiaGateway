package gateway_test;

import java.awt.Color;
import java.awt.Font;

import omega_util.SimpleGameObject;
import omega_util.Transformable;
import omega_util.Transformation;
import gateway_event.ButtonEvent;
import gateway_event.ButtonEventListener;
import gateway_ui.AbstractButton;
import gateway_ui.MessageBox;
import gateway_ui.MessageBoxSpriteBackground;
import gateway_ui.SimpleMessageBoxInputBar;
import gateway_ui.TextDrawer;
import gateway_ui.SingleSpriteButton;
import genesis_event.DrawableHandler;
import genesis_event.EventSelector;
import genesis_event.HandlerRelay;
import genesis_event.KeyListenerHandler;
import genesis_event.MouseEvent;
import genesis_event.MouseListener;
import genesis_event.MouseListenerHandler;
import genesis_util.DepthConstants;
import genesis_util.StateOperator;
import genesis_util.Vector3D;
import genesis_video.GamePanel;
import genesis_video.GameWindow;
import arc_bank.GamePhaseBank;
import arc_resource.ResourceActivator;
import vision_sprite.SpriteBank;

/**
 * This class tests the basic features introduced in this module
 * @author Mikko Hilpinen
 * @since 27.6.2015
 */
public class GatewayTest
{
	// CONSTRUCTOR	------------------------------
	
	private GatewayTest()
	{
		// The interface is static
	}

	
	// MAIN METHOD	--------------------------
	
	/**
	 * Starts the test
	 * @param args Not used
	 */
	public static void main(String[] args)
	{
		// Initializes the resources
		SpriteBank.initializeSpriteResources("sprites.txt");
		GamePhaseBank.initializeGamePhaseResources("gamePhases.txt", "default");
		
		ResourceActivator.startPhase(GamePhaseBank.getGamePhase("phase1"), true);
		
		// Creates the window & panel
		GameWindow window = new GameWindow(new Vector3D(500, 500), "Gateway test", true, 
				120, 20);
		GamePanel panel = window.getMainPanel().addGamePanel();
		
		// Creates the handlers
		HandlerRelay handlers = new HandlerRelay();
		handlers.addHandler(new MouseListenerHandler(false, window.getHandlerRelay()));
		handlers.addHandler(new DrawableHandler(false, panel.getDrawer()));
		handlers.addHandler(new KeyListenerHandler(false, window.getHandlerRelay()));
		
		// Creates the objects
		AbstractButton button = new SingleSpriteButton(new Vector3D(100, 100), 
				DepthConstants.HUD, SpriteBank.getSprite("test", "button"), handlers);
		new TestButtonListener(handlers, button);
		
		new MouseTextDrawer(handlers);
		//new InputDrawer(handlers);
		
		Font testFont = new Font(Font.SERIF, Font.BOLD, 14);
		
		MessageBox box = new MessageBox(new Vector3D(50, 200), new Vector3D(300, 200), 
				new Vector3D(20, 10), "This is an awesome message box!#Yay :)", "#", 
				testFont, testFont, Color.BLACK, handlers);
		new MessageBoxSpriteBackground(box, SpriteBank.getSprite("test", "background"));
		box.addButton(SpriteBank.getSprite("test", "button"), new Vector3D(10, 10), 
				"Button 1", true);
		box.addButton(SpriteBank.getSprite("test", "button"), new Vector3D(10, 10), 
				"Button 2", true);
		new SimpleMessageBoxInputBar(box, new Vector3D(10, 12), 15, testFont, Color.WHITE, 
				Color.BLACK);
		//new SpriteMessageBoxInputBar(box, new Vector3D(10, 12), testFont, Color.WHITE, 
		//		15, SpriteBank.getSprite("test", "button"));
	}
	
	
	// SUBCLASSES	-------------------------
	
	private static class TestButtonListener extends SimpleGameObject implements ButtonEventListener
	{
		// ATTRIBUTES	---------------------
		
		private EventSelector<ButtonEvent> selector;
		
		
		// CONSTRUCTOR	---------------------
		
		public TestButtonListener(HandlerRelay handlers, AbstractButton button)
		{
			super(handlers);
			
			this.selector = ButtonEvent.createButtonPressReleaseEventSelector();
			
			button.getListenerHandler().add(this);
		}
		
		
		// IMPLEMENTED METHODS	-------------

		@Override
		public void onButtonEvent(ButtonEvent e)
		{
			System.out.println(e.getType());
		}

		@Override
		public StateOperator getListensToButtonEventsOperator()
		{
			return getIsActiveStateOperator();
		}

		@Override
		public EventSelector<ButtonEvent> getButtonEventSelector()
		{
			return this.selector;
		}
	}
	
	private static class MouseTextDrawer extends SimpleGameObject implements MouseListener
	{
		// ATTRIBUTES	---------------------
		
		private EventSelector<MouseEvent> selector;
		private TextDrawer textDrawer;
		
		
		// CONSTRUCTOR	---------------------
		
		public MouseTextDrawer(HandlerRelay handlers)
		{
			super(handlers);
			
			this.selector = MouseEvent.createMouseMoveSelector();
			this.textDrawer = new TextDrawer(Vector3D.zeroVector(), 
					"Uskomattoman kalliit keng�t tuolla herrasmiehell� kyll� on!�Totta viek��n...", "�", 
					new Font(Font.SERIF, Font.PLAIN, 14), Color.BLACK, 120, 
					DepthConstants.TOP, handlers);
			
			Transformable.transform(this.textDrawer, Transformation.rotationTransformation(45));
		}
		
		
		// IMPLEMENTED METHODS	-------------

		@Override
		public StateOperator getListensToMouseEventsOperator()
		{
			return getIsActiveStateOperator();
		}

		@Override
		public EventSelector<MouseEvent> getMouseEventSelector()
		{
			return this.selector;
		}

		@Override
		public boolean isInAreaOfInterest(Vector3D position)
		{
			return false;
		}

		@Override
		public void onMouseEvent(MouseEvent event)
		{
			if (this.textDrawer != null)
				this.textDrawer.setTrasformation(this.textDrawer.getTransformation().withPosition(
						event.getPosition()).withScaling(event.getPosition().dividedBy(200)));
		}
	}
	
	/*
	private static class InputDrawer extends SimpleGameObject implements Drawable
	{
		// ATTRIBUTES	--------------------------
		
		private InputReader input;
		private StateOperator isVisibleOperator;
		
		
		// CONSTRUCTOR	--------------------------
		
		public InputDrawer(HandlerRelay handlers)
		{
			super(handlers);
			
			this.input = new InputReader(handlers);
			this.isVisibleOperator = new StateOperator(true, true);
		}

		@Override
		public void drawSelf(Graphics2D g2d)
		{
			g2d.drawString(this.input.getInput(), 300, 300);
		}

		@Override
		public int getDepth()
		{
			return 0;
		}

		@Override
		public StateOperator getIsVisibleStateOperator()
		{
			return this.isVisibleOperator;
		}
	}
	*/
}