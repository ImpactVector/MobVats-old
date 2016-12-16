package net.impactvector.mobvats.api.imc;

import net.impactvector.mobvats.common.ModEventLog;
import net.minecraftforge.fml.common.event.FMLInterModComms;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * This class routes IMC messages to their designated handlers.
 * @author Erogenous Beef
 */
public class MessageRouter {

	protected static Map<String, Method> handlers = new HashMap<String, Method>();
	
	public static void route(FMLInterModComms.IMCEvent event) {
		for(FMLInterModComms.IMCMessage message : event.getMessages()) {
			Method handler = handlers.get(message.key);
			if(handler != null) {
				try {
					handler.invoke(message);
				} catch (IllegalAccessException e) {
					ModEventLog.warning("IllegalAccessException while handling message <%s>, ignoring. Error: %s", message.key, e.getMessage());
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					ModEventLog.warning("IllegalArgumentException while handling message <%s>, ignoring. Error: %s", message.key, e.getMessage());
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					ModEventLog.warning("InvocationTargetException while handling message <%s>, ignoring. Error: %s", message.key, e.getMessage());
					e.printStackTrace();
				}
			}
			else {
				ModEventLog.warning("Received an InterModComms event with an unrecognized key <%s>", message.key);
			}
		}
	}

	public static void register(String key, Method handler) {
		handlers.put(key, handler);
	}
	
}
