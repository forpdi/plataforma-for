package org.forpdi.core.notification;

import java.lang.reflect.Type;

import org.jboss.logging.Logger;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import br.com.caelum.vraptor.converter.ConversionException;
import br.com.caelum.vraptor.converter.ConversionMessage;

public class NotificationTypeConverter implements JsonDeserializer<NotificationType>, JsonSerializer<NotificationType> {
	
	protected final Logger LOGGER = Logger.getLogger(this.getClass());

	@Override
	public JsonElement serialize(NotificationType not, Type type, JsonSerializationContext arg2) {
		return new JsonPrimitive(not.getValue());
	}

	@Override
	public NotificationType deserialize(JsonElement arg0, Type type, JsonDeserializationContext arg2)
			throws JsonParseException {
		try {
			Integer notification = Integer.valueOf(arg0.getAsString());
			switch (notification) {
			case 1:
				return NotificationType.WELCOME;
			case 2:
				return NotificationType.ACCESSLEVEL_CHANGED;
			case 3:
				return NotificationType.PERMISSION_CHANGED;
			case 4:
				return NotificationType.PLAN_MACRO_CREATED;
			case 5:
				return NotificationType.PLAN_CREATED;
			case 6:
				return NotificationType.ATTRIBUTED_RESPONSIBLE;
			case 7:
				return NotificationType.GOAL_CLOSED;
			case 8:
				return NotificationType.PLAN_CLOSE_TO_MATURITY;
			case 9:
				return NotificationType.GOAL_CLOSE_TO_MATURITY;
			case 10:
				return NotificationType.LATE_GOAL;
			default:
				LOGGER.errorf("Invalid notification type: %d", notification);
				return null;
			}
		} catch (Exception e) {
			throw new ConversionException(new ConversionMessage("is_not_a_valid_integer", arg0));
		}
	}

}
