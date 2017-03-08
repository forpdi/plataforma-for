package org.forpdi.system.factory;

import java.lang.reflect.Type;
import java.util.Date;

import javax.enterprise.inject.Specializes;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import br.com.caelum.vraptor.boilerplate.util.GeneralUtils;
import br.com.caelum.vraptor.serialization.gson.DateGsonConverter;

@Specializes
public class GsonBrazilianDateConverter extends DateGsonConverter
	implements JsonDeserializer<Date>, JsonSerializer<Date> {

	public GsonBrazilianDateConverter() {
		//LOG.info("Utilizado parser custom.");
	}
	
	@Override
	public JsonElement serialize(Date date, Type typeOfSrc, JsonSerializationContext context) {
		String dateString = GeneralUtils.DATETIME_FORMAT.format(date);
		return new JsonPrimitive(dateString);
	}

	@Override
	public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		try {
			return GeneralUtils.parseDate(json.getAsString());
		} catch (Throwable e) {}
		try {
			return GeneralUtils.parseDateTime(json.getAsString());
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}
}
