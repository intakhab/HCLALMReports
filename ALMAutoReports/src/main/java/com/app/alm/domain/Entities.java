package com.app.alm.domain;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Entities")
public class Entities {

	protected String TotalResults;

	protected Entity [] Entity;

	public String getTotalResults() {
		return TotalResults;
	}

	public void setTotalResults(String totalResults) {
		TotalResults = totalResults;
	}

	public Entity[] getEntity() {
		return Entity;
	}

	public void setEntity(Entity[] entity) {
		Entity = entity;
	}

	

}