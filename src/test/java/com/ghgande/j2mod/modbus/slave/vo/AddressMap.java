package com.ghgande.j2mod.modbus.slave.vo;

public class AddressMap {
	private String type;
	private int address;
	private int value;
	private String description;
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public int getAddress() {
		return address;
	}
	
	public void setAddress(int address) {
		this.address = address;
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public String toString() {
		return "Type : " + type + ", Address : " + address + ",  Value : " + value + ", Description : " + description;
	}
}