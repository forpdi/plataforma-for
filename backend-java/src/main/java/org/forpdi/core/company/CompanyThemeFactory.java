package org.forpdi.core.company;

import java.util.LinkedList;

public final class CompanyThemeFactory {

	private static CompanyThemeFactory instance = new CompanyThemeFactory();
	
	public static CompanyThemeFactory getInstance() {
		return instance;
	}
	
	public static CompanyTheme getDefaultTheme() {
		return instance.themes.get(0);
	}
	
	private final LinkedList<CompanyTheme> themes = new LinkedList<CompanyTheme>();
	
	private CompanyThemeFactory() {
		this.themes.add(new CompanyTheme() {
			@Override
			public String getDisplayName() {
				return "Tema Padrão";
			}
			@Override
			public String getCSSFile() {
				return "default.css";
			}
		});
	}
	
	public int register(CompanyTheme theme) {
		if (theme == null) {
			throw new IllegalArgumentException("Null theme object passed.");
		}
		if (themes.contains(theme)) {
			throw new IllegalArgumentException("Duplicate theme registering: "+theme.getId());
		}
		this.themes.add(theme);
		return this.themes.size()-1;
	}
	
	public CompanyTheme getTheme(int index) {
		return this.themes.get(index);
	}
	
	public CompanyTheme getTheme(String themeId) {
		for (CompanyTheme theme : themes) {
			if (theme.getId().equals(themeId)) {
				return theme;
			}
		}
		return null;
	}
	
}
