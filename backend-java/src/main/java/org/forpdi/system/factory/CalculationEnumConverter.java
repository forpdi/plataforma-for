package org.forpdi.system.factory;

import java.lang.reflect.Type;

import org.forpdi.planning.attribute.types.enums.CalculationType;
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

public class CalculationEnumConverter implements JsonDeserializer<CalculationType>, JsonSerializer<CalculationType> {

	protected final Logger LOGGER = Logger.getLogger(this.getClass());
	
	@Override
	public JsonElement serialize(CalculationType calc, Type type, JsonSerializationContext arg2) {
		return new JsonPrimitive(calc.getValue());
	}

	@Override
	public CalculationType deserialize(JsonElement arg0, Type type, JsonDeserializationContext arg2)
			throws JsonParseException {		
		try {

			Integer calc = Integer.valueOf(arg0.getAsString());
			switch (calc) {
			case 0:
				return CalculationType.NORMAL_AVG;
			case 1:
				return CalculationType.WEIGHTED_AVG;
			case 2:
				return CalculationType.SUM;
			default:
				LOGGER.errorf("Invalid calculation type: %d", calc);
				return null;
			}

		} catch (Exception e) {
			throw new ConversionException(new ConversionMessage("is_not_a_valid_integer", arg0));
		}
	}
}
