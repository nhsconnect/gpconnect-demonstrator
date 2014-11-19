package net.nhs.domain.openehr.model;

public enum DataSourceId {
	LEGACY("legacy"),
	OPENEHR("handi.ehrscape.com");
	
	private String dataSourceId;
	
	private DataSourceId(String dataSourceId) {
		this.dataSourceId = dataSourceId;
	}
	
	public String getValue() {
		return dataSourceId;
	}
	
	public static DataSourceId fromString(String text) {
		if (text != null) {
			for (DataSourceId d : DataSourceId.values()) {
				if (text.equalsIgnoreCase(d.dataSourceId)) {
					return d;
				}
			}
		}
		return null;
	}
}
