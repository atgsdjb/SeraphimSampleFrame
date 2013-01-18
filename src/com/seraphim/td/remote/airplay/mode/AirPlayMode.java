package com.seraphim.td.remote.airplay.mode;

abstract public class AirPlayMode {
	enum Category {
			XML,PARATEMERS
	}
	
	private Category category;
	
	public AirPlayMode(Category type){
		this.category = type;
	};
	public Category getCategory(){return category;};
	
}
