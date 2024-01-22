package com.jasonrobison.emulator8080.emulator;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.jasonrobison.emulator8080.emulator.opcodes.OpCodes;

@RunWith(PowerMockRunner.class)
@PrepareForTest({OpCodes.class})
public class CpuTest {
	
	private static final int OP_CODE = 0x12;
	Cpu sut;
	
	@Before
	public void setUp() {
		sut = new Cpu();
	}

	@Test
	public void getCurrentOpCodeProgramStart() {
		sut.getMemory()[0] = OP_CODE;
		
		int actual = sut.getCurrentOpCode();
		
		assertEquals(OP_CODE, actual);
	}
	
	@Test
	public void getCurrentOpCodeProgramCounterSetZero() {
		sut.getMemory()[0] = 0x00;
		sut.getMemory()[5] = OP_CODE;
		sut.getRegisters().setProgramCounter((short) 0);
		
		int actual = sut.getCurrentOpCode();
		
		assertEquals(0x00, actual);
	}
	
	@Test
	public void getCurrentOpCode() {
		sut.getMemory()[0] = 0x00;
		sut.getMemory()[5] = OP_CODE;
		sut.getRegisters().setProgramCounter((short) 5);
		
		int actual = sut.getCurrentOpCode();
		
		assertEquals(OP_CODE, actual);
	}
	
	@Test
	@Ignore
	public void testProgram() throws IOException {
		Emulator emulator = new Emulator();
		emulator.loadGameIntoMemory("/Users/jason/Downloads/invaders");
		emulator.executeGame();
	}
	
	@Test
	public void getBC() {
		sut.getRegisters().setB((byte) 0x11);
		sut.getRegisters().setC((byte) 0x22);
		
		assertEquals((short) 0x1122, sut.getRegisters().getBC());
	}
	
	@Test
	public void setBC() {
		sut.getRegisters().setBC((short) 0x1122);
		
		assertEquals((byte) 0x11, sut.getRegisters().getB());
		assertEquals((byte) 0x22, sut.getRegisters().getC());
	}
}
