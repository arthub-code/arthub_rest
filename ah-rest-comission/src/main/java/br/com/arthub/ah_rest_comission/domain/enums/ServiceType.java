package br.com.arthub.ah_rest_comission.domain.enums;

public enum ServiceType {
	PROVIDED(1,"provided"),
	CUSTOMIZED(2,"customized")
	;
	int code;
	String type;
	
	ServiceType(int code, String type) {
		this.code = code;
		this.type = type;
	}
	
	public int getCode() {
		return this.code;
	}
	
	public String getType() {
		return this.type;
	}
}
