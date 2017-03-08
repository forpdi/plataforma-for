package org.forpdi.core.notification;

public enum NotificationSetting {
	DEFAULT(1),
	RECEIVE_ALL_BY_EMAIL(2),
	DO_NOT_RECEIVE_EMAIL(3);
	
	private final int setting;
	
	private NotificationSetting(int setting){
		this.setting = setting;
	}
	
	public int getSetting() {
		return setting;
	}
}
