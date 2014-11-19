package net.nhs.esb.rest.domain;

public class Meta {
	private String href;
	private String precedingHref;
	private String nextHref;

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getPrecedingHref() {
		return precedingHref;
	}

	public void setPrecedingHref(String precedingHref) {
		this.precedingHref = precedingHref;
	}

	public String getNextHref() {
		return nextHref;
	}

	public void setNextHref(String nextHref) {
		this.nextHref = nextHref;
	}
}
