package com.app.alm.dto;


public class ALMReportsDto {
	private int id;
	private String entityName;
	private String entityValue;

	public ALMReportsDto(int id, String enString, String enString2) {
		this.id = id;
		this.entityName = enString;
		this.entityValue = enString2;

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getEntityValue() {
		return entityValue;
	}

	public void setEntityValue(String entityValue) {
		this.entityValue = entityValue;
	}

}