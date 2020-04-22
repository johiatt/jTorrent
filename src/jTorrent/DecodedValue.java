package jTorrent;

public class DecodedValue {

	private Integer length;
	private String tracker;
	private String contents;
	
	public DecodedValue(Integer length, String contents) {
		this.setLength(length);
		this.setContents(contents);
	}
	
	public DecodedValue(String tracker, String contents) {
		this.setLength(-1);
		this.setTracker(tracker);
		this.setContents(contents);
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public String getTracker() {
		return tracker;
	}

	public void setTracker(String tracker) {
		this.tracker = tracker;
	}
	
}
