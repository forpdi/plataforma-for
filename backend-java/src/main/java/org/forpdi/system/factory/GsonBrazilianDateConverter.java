package org.forpdi.system.factory;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.enterprise.inject.Specializes;

import org.jboss.logging.Logger;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import br.com.caelum.vraptor.serialization.gson.DateGsonConverter;

@Specializes
public class GsonBrazilianDateConverter extends DateGsonConverter
		implements JsonDeserializer<Date>, JsonSerializer<Date> {

	private static final Logger LOG = Logger.getLogger(GsonBrazilianDateConverter.class);

	public static final Locale DEFAULT_LOCALE = new Locale("pt", "BR");
	public static final DateFormat DATETIME_FORMAT = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, DEFAULT_LOCALE);
	public static final DateFormat DATE_FORMAT = DateFormat.getDateInstance(DateFormat.MEDIUM, DEFAULT_LOCALE);

	public GsonBrazilianDateConverter() {
		// LOG.info("Utilizado parser custom.");
	}

	@Override
	public JsonElement serialize(Date date, Type typeOfSrc, JsonSerializationContext context) {
		String dateString = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(date);
		//gerando exceção java.lang.ArrayIndexOutOfBoundsException em certos casos
		//String dateString= DATETIME_FORMAT.format(date.getTime());

		return new JsonPrimitive(dateString);
	}

	@Override
	public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		final String dateString = json.getAsString();
		try {
			return DATETIME_FORMAT.parse(dateString);
		} catch (Exception e) {
			try {
				return DATE_FORMAT.parse(dateString);
			} catch (Exception e2) {
				LOG.errorf(e2, "Can't deserialize date field: %s", dateString);
				return null;
			}
		}
	}
}
