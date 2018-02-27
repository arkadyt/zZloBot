package com.surveyo.zzlobot;

/**
 * TODO
 * Rethink Size and Date types.
 * */
public class Bot {

	private String bot_name, bot_date, bot_size;

	public Bot(String bot_name, String size, String date) {
		this.bot_name = bot_name;
		this.bot_size = size;
		this.bot_date = date;
	}

	public String getName() {
		return this.bot_name;
	}

	public void setName(String newName) {
		this.bot_name = newName;
	}
	
	public String getSize() {
		return this.bot_size;
	}

	public void setSize(String newSize) {
		this.bot_size = newSize;
	}

	public String getDate() {
		return this.bot_date;
	}

	public void setDate(String newDate) {
		this.bot_date = newDate;
	}
	
}
