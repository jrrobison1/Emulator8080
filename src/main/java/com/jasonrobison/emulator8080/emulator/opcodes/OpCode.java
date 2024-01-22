package com.jasonrobison.emulator8080.emulator.opcodes;

import com.jasonrobison.emulator8080.emulator.Cpu;

public interface OpCode {
	public void execute(Cpu cpu);
}
