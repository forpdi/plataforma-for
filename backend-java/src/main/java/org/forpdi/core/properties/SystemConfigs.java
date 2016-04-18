package org.forpdi.core.properties;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * @author Renato Oliveira
 *
 */
public final class SystemConfigs {
	
	/** O bundle com as configura��es do sistema. */
	private static final ResourceBundle BUNDLE = PropertyResourceBundle.getBundle("properties.system");

	/** Mapa singleton com as configura��es. */
	private static Map<String, String> CONFIG_MAP;
	
	public static String getConfig(String key) {
		try {
			return SystemConfigs.BUNDLE.getString(key);
		} catch (Exception ex) {
			return "???"+key+"???";
		}
	}
	public static Map<String, String> getConfigMap() {
		if (SystemConfigs.CONFIG_MAP == null) {
			HashMap<String, String> map = new HashMap<String, String>();
			Enumeration<String> keys = SystemConfigs.BUNDLE.getKeys();
			while (keys.hasMoreElements()) {
				String key = keys.nextElement();
				map.put(key, SystemConfigs.BUNDLE.getString(key));
			}
			SystemConfigs.CONFIG_MAP = map;
		}
		return SystemConfigs.CONFIG_MAP;
	}
}
