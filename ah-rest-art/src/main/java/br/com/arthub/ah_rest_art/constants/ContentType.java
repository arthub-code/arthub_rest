package br.com.arthub.ah_rest_art.constants;

public enum ContentType {
	JPEG("image/jpeg"),
	WEBP("image/webp"),
	PNG("image/png"),
	SVG("image/svg")
	;
	
	private String type;
	ContentType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
}
