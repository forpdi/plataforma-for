package org.forpdi.core.properties;

import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;

import br.com.caelum.vraptor.boilerplate.util.GeneralUtils;

/**
 * @author Renato R. R. de Oliveira
 * 
 */
public final class CoreMessages {

	/** Caminho do bundle. */
	public static final String BUNDLE_NAME = "properties.messages";
	/** Locale padrão do sistema. */
	public static final Locale DEFAULT_LOCALE = new Locale("pt", "BR");
	static {
		Locale.setDefault(CoreMessages.DEFAULT_LOCALE);
	}

	/** Lolcale para um objeto definido com uma lingua. */
	private final Locale locale;
	/** Bundle com mensagens traduzidas. */
	private final PropertyResourceBundle bundle;
	/** Mapa com overlays de mensagens ao bundle padrão. */
	private Map<String, String> overlay;
	
	/**
	 * Retorna o texto no locale padr�o (ingl�s).
	 * 
	 * @param key
	 *            Chave do texto.
	 * @return Texto carregado.
	 */
	public static String getText(String key) {
		try {
			return PropertyResourceBundle.getBundle(BUNDLE_NAME, DEFAULT_LOCALE).getString(key);
		} catch (MissingResourceException ex) {
			return "???"+key+"???";
		}
	}

	/**
	 * Retorna o texto no locale passado.
	 * 
	 * @param key
	 *            Chave do texto.
	 * @param locale
	 *            Locale desejado.
	 * @return Texto carregado.
	 */
	public static String getText(String key, Locale locale) {
		try {
			return PropertyResourceBundle.getBundle(BUNDLE_NAME, locale).getString(key);
		} catch (MissingResourceException ex) {
			return "???"+key+"???";
		}
	}

	/**
	 * Retorna uma mensagem formatada utilizando o MessageFormat.
	 * @param key Chave da mesnagem.
	 * @param args Parametros a serem inseridos na mensagem.
	 * @return Mensagem formatada inserindo os argumentos no template est�tico obtivdo do arquivo de propriedades.
	 */
	public String formatText(String key, Locale locale, Object[] args) {
		try {
			String raw = PropertyResourceBundle.getBundle(BUNDLE_NAME, locale).getString(key);
			return MessageFormat.format(raw, args);
		} catch (MissingResourceException ex) {
			return "???!"+key+"!???";
		}
	}
	

	public CoreMessages(Locale locale) {
		this.locale = locale;
		this.bundle = (PropertyResourceBundle) PropertyResourceBundle.getBundle(BUNDLE_NAME, this.locale);
	}

	public CoreMessages(String locale) {
		this(GeneralUtils.parseLocaleString(locale));
	}
	
	/**
	 * Define um mapa de overlays de mensagens.
	 * @param overlay Mapa contendo mensagens na forma chave-valor.
	 */
	public void setOverlay(Map<String, String> overlay) {
		this.overlay = overlay;
	}
	
	/**
	 * Recupera uma mensagem do mapa de overlay (se houver).
	 * @param key Chave da mensagem a ser recuperada.
	 * @return Mensagem contida no overlay.
	 */
	private String getOverlayMessage(String key) {
		if ((this.overlay == null) || GeneralUtils.isEmpty(key))
			return null;
		return this.overlay.get(key);
	}
	
	/**
	 * Obt�m uma mensagem dada a chave.
	 * @param key Chave no arquivo de properties.
	 * @return Texto referente a chave passada.
	 */
	public String getMessage(String key) {
		try {
			String overlayed = this.getOverlayMessage(key);
			if (!GeneralUtils.isEmpty(overlayed)) {
				return overlayed;
			}
			return this.bundle.getString(key);
		} catch (MissingResourceException ex) {
			return "???"+key+"???";
		}
	}
	
	/**
	 * Retorna uma mensagem formatada utilizando o MessageFormat.
	 * @param key Chave da mesnagem.
	 * @param args Parametros a serem inseridos na mensagem.
	 * @return Mensagem formatada inserindo os argumentos no template estático obtivdo do arquivo de propriedades.
	 */
	public String formatMessage(String key, Object[] args) {
		try {
			String raw = this.getMessage(key);
			return MessageFormat.format(raw, args);
		} catch (MissingResourceException ex) {
			return "???!"+key+"!???";
		}
	}
	
	/**
	 * Obtém uma string com um mapa em JSON de todas as mensagens.
	 */
	public String getJSONMessages() {
		StringBuilder builder = new StringBuilder();
		builder.append("{");
		
		boolean first = true;
		Enumeration<String> keys = this.bundle.getKeys();
		while (keys.hasMoreElements()) {
			if (!first)
				builder.append(",");
			else
				first = false;
			String key = keys.nextElement();
			String value = this.getMessage(key);
			String escaped = value.replaceAll("\\'", "\\\\'");
			builder.append("'").append(key).append("':'").append(escaped).append("'");
		}
		
		builder.append("}");
		return builder.toString();
	}
}
