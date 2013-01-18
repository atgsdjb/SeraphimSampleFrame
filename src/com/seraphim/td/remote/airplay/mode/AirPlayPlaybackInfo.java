package com.seraphim.td.remote.airplay.mode;

import java.util.List;

public class AirPlayPlaybackInfo extends AirPlayMode {
	private float duration;
	private float position;
	private float rate;
	private boolean readyToPlay;
	private boolean playbackBufferEmpty;
	private boolean playbackBufferFull;
	private boolean playbackLikelyToKeepUp;
	private List<TimeRange> seekableTimeRanges;
	private List<TimeRange> loadedTimeRanges;
	/**
	 * 
	 * @param duration
	 * @param position
	 * @param rate
	 * @param readyToPlay
	 * @param playbackBufferEmpty
	 * @param playbackBufferFull
	 * @param playbackLikelyToKeepUp
	 * @param seekableTimeRanges
	 * @param loadedTimeRanges
	 */
	public AirPlayPlaybackInfo(float duration, float position, float rate,
			boolean readyToPlay, boolean playbackBufferEmpty,
			boolean playbackBufferFull, boolean playbackLikelyToKeepUp,
			List<TimeRange> seekableTimeRanges, List<TimeRange> loadedTimeRanges) {
		super(Category.XML);
		this.duration = duration;
		this.position = position;
		this.rate = rate;
		this.readyToPlay = readyToPlay;
		this.playbackBufferEmpty = playbackBufferEmpty;
		this.playbackBufferFull = playbackBufferFull;
		this.playbackLikelyToKeepUp = playbackLikelyToKeepUp;
		this.seekableTimeRanges = seekableTimeRanges;
		this.loadedTimeRanges = loadedTimeRanges;
	}
	
	public float getDuration() {
		return duration;
	}

	public float getPosition() {
		return position;
	}

	public float getRate() {
		return rate;
	}

	public boolean isReadyToPlay() {
		return readyToPlay;
	}

	public boolean isPlaybackBufferEmpty() {
		return playbackBufferEmpty;
	}

	public boolean isPlaybackBufferFull() {
		return playbackBufferFull;
	}

	public boolean isPlaybackLikelyToKeepUp() {
		return playbackLikelyToKeepUp;
	}

	public List<TimeRange> getSeekableTimeRanges() {
		return seekableTimeRanges;
	}

	public List<TimeRange> getLoadedTimeRanges() {
		return loadedTimeRanges;
	}

	/**
	 * 
	 * @author root
	 *
	 */
	public static class TimeRange{
		float duration;
		float start;
	}
}
