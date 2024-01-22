package com.jasonrobison.emulator8080.emulator;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.jasonrobison.emulator8080.emulator.opcodes.OpCode;
import com.jasonrobison.emulator8080.emulator.opcodes.OpCodes;

public class Cpu {
	private Registers registers = new Registers();
	
	private static final Logger LOGGER = Logger.getLogger(Cpu.class);
	
	// 16 KB of addressable memory
	private byte[] memory = new byte[16384];
	
	private Flags flags = new Flags();
	
	private Map<Byte, OpCode> opcodeExecutors = new HashMap<Byte, OpCode>();
	
	public Cpu() {
		for (int i = 1; i <= 0xff; i++) {
			opcodeExecutors.put(Byte.valueOf((byte)i), OpCodes.notImplemented());
		}
		opcodeExecutors.put((byte) 0, OpCodes.nop());
		opcodeExecutors.put((byte) 0x05, OpCodes.dcrB());
		opcodeExecutors.put((byte) 0x06, OpCodes.mviBD8());
		opcodeExecutors.put((byte) 0x0e, OpCodes.movC());
		opcodeExecutors.put((byte) 0x11, OpCodes.lxiDD16());
		opcodeExecutors.put((byte) 0x13, OpCodes.inxD());
		opcodeExecutors.put((byte) 0x19, OpCodes.addHlDe());
		opcodeExecutors.put((byte) 0x1a, OpCodes.ldaxD());
		opcodeExecutors.put((byte) 0x21, OpCodes.lxiHD16());
		opcodeExecutors.put((byte) 0x23, OpCodes.inxH());
		opcodeExecutors.put((byte) 0x26, OpCodes.ldH());
		opcodeExecutors.put((byte) 0x29, OpCodes.addHlHl());
		opcodeExecutors.put((byte) 0x31, OpCodes.lxiSpD16());
		opcodeExecutors.put((byte) 0x36, OpCodes.mvHLm());
		opcodeExecutors.put((byte) 0x6f, OpCodes.movLA());
		opcodeExecutors.put((byte) 0x77, OpCodes.movMA());
		opcodeExecutors.put((byte) 0x7c, OpCodes.movAH());
		opcodeExecutors.put((byte) 0xc2, OpCodes.jnz());
		opcodeExecutors.put((byte) 0xc3, OpCodes.jmpAddr());
		opcodeExecutors.put((byte) 0xc9, OpCodes.ret());
		opcodeExecutors.put((byte) 0xcd, OpCodes.callAddr());
		opcodeExecutors.put((byte) 0xd5, OpCodes.pushDE());
		opcodeExecutors.put((byte) 0xeb, OpCodes.xchgHLDE());
		opcodeExecutors.put((byte) 0xe5, OpCodes.pushHL());
		opcodeExecutors.put((byte) 0xfe, OpCodes.subByteA());
	}
	
	public Registers getRegisters() {
		return registers;
	}

	public void setRegisters(Registers registers) {
		this.registers = registers;
	}

	public byte[] getMemory() {
		return memory;
	}

	public void setMemory(byte[] memory) {
		this.memory = memory;
	}

	public Flags getFlags() {
		return flags;
	}

	public void setFlags(Flags flags) {
		this.flags = flags;
	}

	public byte getCurrentOpCode() {
		return memory[registers.getProgramCounter()];
	}

	public void executeProgram() {
		while (registers.getProgramCounter() < memory.length) {
			LOGGER.debug(registerState());
			opcodeExecutors.get(
					memory[Short.toUnsignedInt(registers.getProgramCounter())])
					.execute(this);
		}
	}
	
	private String registerState() {
		return "\n"
				+ StringUtils.rightPad("af", 6)
				+ StringUtils.rightPad("bc", 6)
				+ StringUtils.rightPad("de", 6)
				+ StringUtils.rightPad("hl", 6)
				+ StringUtils.rightPad("pc", 6)
				+ StringUtils.rightPad("sp", 6)
				+ StringUtils.rightPad("flags",  6)
				+ "\n" 
				+ StringUtils.rightPad(OpCodes.getAsHexString(registers.getA()) + "" + "--", 6)
				+ StringUtils.rightPad(OpCodes.getAsHexString(registers.getBC()), 6) 
				+ StringUtils.rightPad(OpCodes.getAsHexString(registers.getDE()), 6)
				+ StringUtils.rightPad(OpCodes.getAsHexString(registers.getHL()), 6)
				+ StringUtils.rightPad(OpCodes.getAsHexString(registers.getProgramCounter()), 6)
				+ StringUtils.rightPad(OpCodes.getAsHexString(registers.getStackPointer()), 6)
				+ StringUtils.rightPad((flags.isCarry() ? "C" : ".")
						+ (flags.isParity() ? "P" : ".")
						+ (flags.isSigned() ? "S" : ".")
						+ (flags.isZero() ? "Z" : "."), 6);
	}
}
