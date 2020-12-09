package io.dfjinxin.modules.sys.entity;

public class AttributeEntity {
	private String name;
	private String type;
	private boolean isRequired;
	private boolean isMultivalued;

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isRequired() {
		return this.isRequired;
	}

	public void setRequired(boolean isRequired) {
		this.isRequired = isRequired;
	}

	public boolean isMultivalued() {
		return this.isMultivalued;
	}

	public void setMultivalued(boolean isMultivalued) {
		this.isMultivalued = isMultivalued;
	}
}