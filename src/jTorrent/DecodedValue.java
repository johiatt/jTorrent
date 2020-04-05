package jTorrent;

public class DecodedValue {

	private Integer length;
	private String contents;
	
	public DecodedValue(Integer length, String contents) {
		this.setLength(length);
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
	
}
