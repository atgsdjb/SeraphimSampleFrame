package com.seraphim.td.remote.airplay.mode;

public class AirPlayScrub extends AirPlayMode{
	private float duration;
	private float position;
	public AirPlayScrub(float duration, float position) {
		super(Category.PARATEMERS);
		this.duration = duration;
		this.position = position;
	}
	public float getDuration() {
		return duration;
	}
	public float getPosition() {
		return position;
	}
	
	
}
