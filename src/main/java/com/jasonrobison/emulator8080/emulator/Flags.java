package com.jasonrobison.emulator8080.emulator;

public class Flags {
	
	private boolean isZero;
	
	private boolean isSigned;
	
	private boolean isParity;
	
	private boolean isCarry;

	public boolean isZero() {
		return isZero;
	}

	public void setZero(boolean isZero) {
		this.isZero = isZero;
	}

	public boolean isSigned() {
		return isSigned;
	}

	public void setSigned(boolean isSigned) {
		this.isSigned = isSigned;
	}

	public boolean isParity() {
		return isParity;
	}

	public void setParity(boolean isParity) {
		this.isParity = isParity;
	}

	public boolean isCarry() {
		return isCarry;
	}

	public void setCarry(boolean isCarry) {
		this.isCarry = isCarry;
	}

}
