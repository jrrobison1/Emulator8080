package com.jasonrobison.emulator8080.emulator;

public class Registers {
	private byte a;
	private byte b;
	private byte c;
	private byte d;
	private byte e;
	private byte h;
	private byte l;
	private short sp;
	private short pc;
	

	public byte getA() {
		return a;
	}

	public void setA(byte a) {
		this.a = a;
	}

	public byte getB() {
		return b;
	}

	public void setB(byte b) {
		this.b = b;
	}

	public byte getC() {
		return c;
	}

	public void setC(byte c) {
		this.c = c;
	}
	
	public short getBC() {
		return (short) ((this.b & 0xff) << 8 | (this.c & 0xff));
	}
	
	public void setBC(short s) {
		this.c = (byte)(s & 0xff);
		this.b = (byte)((s >> 8) & 0xff);
	}
	
	public short getDE() {
		return (short) ((this.d & 0xff) << 8 | (this.e & 0xff));
	}
	
	public void setDE(short s) {
		this.e = (byte)(s & 0xff);
		this.d = (byte)((s >> 8) & 0xff);
	}
	
	public short getHL() {
		return (short) ((this.h & 0xff) << 8 | (this.l & 0xff));
	}
	
	public void setHL(short s) {
		this.l = (byte)(s & 0xff);
		this.h = (byte)((s >> 8) & 0xff);
	}

	public byte getD() {
		return d;
	}

	public void setD(byte d) {
		this.d = d;
	}

	public byte getE() {
		return e;
	}

	public void setE(byte e) {
		this.e = e;
	}

	public byte getH() {
		return h;
	}

	public void setH(byte h) {
		this.h = h;
	}

	public byte getL() {
		return l;
	}

	public void setL(byte l) {
		this.l = l;
	}

	public short getStackPointer() {
		return sp;
	}

	public void setStackPointer(short sp) {
		this.sp = sp;
	}

	public short getProgramCounter() {
		return pc;
	}

	public void setProgramCounter(short pc) {
		this.pc = pc;
	}
	
	public void incrementProgramCounter() {
		this.pc++;
	}
	
	public void incrementProgramCounter(int incrementBy) {
		this.pc += incrementBy;
	}

}
