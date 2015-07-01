package gateway_event;

import genesis_event.HandlerType;

/**
 * These are the different handler types introduced by the gateway module
 * @author Mikko Hilpinen
 * @since 6.6.2015
 */
public enum GatewayHandlerType implements HandlerType
{
	/**
	 * The handler that informs objects about button events
	 */
	BUTTONEVENTHANDLER;

	
	// IMPLEMENTED METHODS	------------------
	
	@Override
	public Class<?> getSupportedHandledClass()
	{
		return ButtonEventListener.class;
	}
}
