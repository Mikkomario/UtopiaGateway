package gateway_test;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import gateway_ui.AbstractOption;
import gateway_ui.InputBar;
import gateway_ui.MessageBox;
import gateway_ui.RectangleUIComponentBackground;
import gateway_ui.SpriteUIComponentBackground;
import gateway_ui.OptionBar;
import gateway_ui.SimplePlusMinusOption;
import genesis_event.DrawableHandler;
import genesis_event.HandlerRelay;
import genesis_event.KeyListenerHandler;
import genesis_event.MouseListenerHandler;
import genesis_util.DepthConstants;
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
		GameWindow window = new GameWindow(new Vector3D(800, 600), "Gateway test", true, 
				120, 20);
		GamePanel panel = window.getMainPanel().addGamePanel();
		
		// Creates the handlers
		HandlerRelay handlers = new HandlerRelay();
		handlers.addHandler(new MouseListenerHandler(false, window.getHandlerRelay()));
		handlers.addHandler(new DrawableHandler(false, panel.getDrawer()));
		handlers.addHandler(new KeyListenerHandler(false, window.getHandlerRelay()));
		
		// Creates the objects
		/*
		AbstractButton button = new SingleSpriteButton(new Vector3D(100, 100), 
				DepthConstants.HUD, SpriteBank.getSprite("test", "button"), handlers);
		new TestButtonListener(handlers, button);
		*/
		//new MouseTextDrawer(handlers);
		//new InputDrawer(handlers);
		
		Font testFont = new Font(Font.SERIF, Font.BOLD, 14);
		
		MessageBox box = new MessageBox(new Vector3D(50, 200), new Vector3D(300, 200), 
				new Vector3D(20, 10), "This is an awesome message box!#Yay :)", "#", 
				testFont, testFont, Color.BLACK, handlers);
		new SpriteUIComponentBackground(box, handlers, 
				SpriteBank.getSprite("test", "background"));
		
		box.addButton(SpriteBank.getSprite("test", "button"), new Vector3D(10, 10), 
				"Button 1", true);
		box.addButton(SpriteBank.getSprite("test", "button"), new Vector3D(10, 10), 
				"Button 2", true);
		
		InputBar inputBar = new InputBar(handlers, new Vector3D(10, 3), new Vector3D(100, 15), 
				testFont, Color.WHITE, 0);
		new RectangleUIComponentBackground(inputBar, handlers, Color.WHITE, Color.BLACK);
		box.addInputBar(inputBar, false);
		//new SpriteMessageBoxInputBar(box, new Vector3D(10, 12), testFont, Color.WHITE, 
		//		15, SpriteBank.getSprite("test", "button"));
		
		List<String> options = new ArrayList<>();
		options.add("low");
		options.add("medium");
		options.add("high");
		options.add("ultra");
		AbstractOption<String> option = SimplePlusMinusOption.createOption(options, 1, 
				DepthConstants.HUD - 1, handlers, SpriteBank.getSprite("test", "button"), 
				SpriteBank.getSprite("test", "button"), 200, new Vector3D(10, 5), testFont, 
				Color.BLACK);
		new RectangleUIComponentBackground(option, handlers, Color.BLACK, null);
		
		OptionBar bar = new OptionBar(new Vector3D(32, 500), "Texture Guality", new Vector3D(500, 64), 
				new Vector3D(10, 10), DepthConstants.HUD, testFont, Color.BLACK, 
				handlers);
		bar.setOptions(option);
		//new OptionBarSpriteBackground(bar, handlers, SpriteBank.getSprite("test", "background"));
		new RectangleUIComponentBackground(bar, handlers, Color.BLACK, null);
	}
	
	
	// SUBCLASSES	-------------------------
	
	/*
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
	*/
	
	/*
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
					"Uskomattoman kalliit kengät tuolla herrasmiehellä kyllä on!£Totta vieköön...", "£", 
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
	*/
	
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
