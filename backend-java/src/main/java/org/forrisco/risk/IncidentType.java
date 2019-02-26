package org.forrisco.risk;

public enum IncidentType {
	AMEACA (1),
	OPORTUNIDADE (2);
	
	private int typeId;
	
	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	private IncidentType(int id) {
		this.typeId = id;
	}
	
	public static IncidentType getById(int id) {
		for (IncidentType type : IncidentType.values()) {
			if (type.typeId == id) {
				return type;
			}
		}
		return null;
	}
}
