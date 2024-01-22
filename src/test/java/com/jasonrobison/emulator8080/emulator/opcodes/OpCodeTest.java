package com.jasonrobison.emulator8080.emulator.opcodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.jasonrobison.emulator8080.emulator.Cpu;

public class OpCodeTest {

	Cpu cpu;

	@Before
	public void setUp() {
		cpu = new Cpu();
	}

	@Test
	@Ignore
	public void notImplemented() {
		cpu.getRegisters().setProgramCounter((short) 0);

		OpCodes.notImplemented().execute(cpu);

		assertEquals(cpu.getRegisters().getProgramCounter(), 1);
	}

	@Test
	public void nop() {
		cpu.getRegisters().setProgramCounter((short) 0);

		OpCodes.nop().execute(cpu);

		assertEquals(cpu.getRegisters().getProgramCounter(), 1);
	}

	@Test
	public void lxiBD16() {
		cpu.getRegisters().setB((byte)0);
		cpu.getRegisters().setC((byte)0);
		cpu.getRegisters().setProgramCounter((short)0);
		
		cpu.getMemory()[0] = 0x01;
		cpu.getMemory()[1] = 0x02;
		cpu.getMemory()[2] = 0x03;
		
		OpCodes.lxiBD16().execute(cpu);
		
		assertEquals(3, cpu.getRegisters().getProgramCounter());
		assertEquals(0x03, cpu.getRegisters().getB());
		assertEquals(0x02, cpu.getRegisters().getC());
	}
	
	@Test
	public void jmpAddr() {
		cpu.getMemory()[0] = (byte)0xc3;
		cpu.getMemory()[1] = (byte)0x11;
		cpu.getMemory()[2] = (byte)0x22;
		cpu.getRegisters().setProgramCounter((short)0x00);
		
		OpCodes.jmpAddr().execute(cpu);
		
		assertEquals((short)0x2211, cpu.getRegisters().getProgramCounter());
	}
	
	/**
	 * SP.hi <- byte 3, SP.lo <- byte 2
	 */
	@Test
	public void lxiSpD16() {
		cpu.getMemory()[0] = (byte)0x31;
		cpu.getMemory()[1] = (byte)0x11;
		cpu.getMemory()[2] = (byte)0x22;
		cpu.getRegisters().setProgramCounter((short)0x00);
		cpu.getRegisters().setStackPointer((short)0x00);
		
		OpCodes.lxiSpD16().execute(cpu);
		
		assertEquals((short)0x2211, cpu.getRegisters().getStackPointer());
		assertEquals(3, cpu.getRegisters().getProgramCounter());
	}
	
	/**
	 * B <- byte 2
	 */
	@Test
	public void mviBD8() {
		cpu.getMemory()[0] = (byte)0x06;
		cpu.getMemory()[1] = (byte)0x11;
		cpu.getRegisters().setB((byte) 0x00);
		cpu.getRegisters().setProgramCounter((short)0x00);
		
		OpCodes.mviBD8().execute(cpu);
		
		assertEquals(0x11, cpu.getRegisters().getB());
		assertEquals(2, cpu.getRegisters().getProgramCounter());
	}
	
	/**
	 * (SP-1)<-PC.hi;(SP-2)<-PC.lo;SP<-SP+2;PC=adr
	 */
	@Test
	public void callAddr() {
		cpu.getMemory()[0] = (byte)0xcd;
		cpu.getMemory()[1] = (byte)0xe6;
		cpu.getMemory()[2] = (byte)0x01;
		cpu.getMemory()[3] = (byte)0x06;
		cpu.getMemory()[4] = (byte)0x00;
		cpu.getMemory()[5] = (byte)0x00;
		cpu.getMemory()[6] = (byte)0x00;
		
		cpu.getRegisters().setProgramCounter((short) 0x00);
		cpu.getRegisters().setStackPointer((short) 0x06);
		
		OpCodes.callAddr().execute(cpu);
		
		assertEquals((short) 0x01e6, cpu.getRegisters().getProgramCounter());
		assertEquals((short) 0x0004, cpu.getRegisters().getStackPointer());
		assertEquals((byte) 0x00, cpu.getMemory()[5]);
		assertEquals((byte) 0x03, cpu.getMemory()[4]);
	}
	
	/**
	 * 0x11
	 * D <- byte 3, E <- byte 2
	 */
	@Test
	public void lxiDD16() {
		cpu.getMemory()[0] = (byte)0x11;
		cpu.getMemory()[1] = (byte)0x22;
		cpu.getMemory()[2] = (byte)0x33;
		cpu.getRegisters().setProgramCounter((short)0x0000);
		
		cpu.getRegisters().setD((byte)0x00);
		cpu.getRegisters().setE((byte)0x00);
		
		OpCodes.lxiDD16().execute(cpu);
		
		assertEquals(0x33, cpu.getRegisters().getD());
		assertEquals(0x22, cpu.getRegisters().getE());
		assertEquals(0x0003, cpu.getRegisters().getProgramCounter());
	}
	
	/**
	 * 0x21
	 * H <- byte 3, L <- byte 2
	 */
	@Test
	public void lxiHD16() {
		cpu.getMemory()[0] = (byte)0x21;
		cpu.getMemory()[1] = (byte)0x11;
		cpu.getMemory()[2] = (byte)0x22;
		cpu.getRegisters().setProgramCounter((short)0x0000);
		
		cpu.getRegisters().setH((byte)0x00);
		cpu.getRegisters().setL((byte)0x00);
		
		OpCodes.lxiHD16().execute(cpu);
		
		assertEquals(0x22, cpu.getRegisters().getH());
		assertEquals(0x11, cpu.getRegisters().getL());
		assertEquals(0x0003, cpu.getRegisters().getProgramCounter());
	}
	
	/**
	 * 0x1a
	 * A <- (DE)
	 */
	@Test
	public void ldaxD() {
		cpu.getMemory()[0] = (byte)0x1a;
		cpu.getMemory()[1] = (byte)0x11;
		
		cpu.getRegisters().setProgramCounter((short)0x0000);
		
		cpu.getRegisters().setDE((short)0x0001);
		
		OpCodes.ldaxD().execute(cpu);
		
		assertEquals(cpu.getMemory()[1], cpu.getRegisters().getA());
		assertEquals(0x0001, cpu.getRegisters().getProgramCounter());
	}
	
	/**
	 * 0x77
	 * MOV M,A
	 * (HL) <- A
	 */
	@Test
	public void movMA() {
		cpu.getMemory()[0] = (byte)0x77;
		cpu.getMemory()[1] = (byte)0x88;
		cpu.getRegisters().setProgramCounter((short)0x00);
		
		cpu.getRegisters().setA((byte) 0x11);
		cpu.getRegisters().setHL((short)0x0001);
		
		OpCodes.movMA().execute(cpu);
		
		assertEquals(0x11, cpu.getMemory()[1]);
		assertEquals(1, cpu.getRegisters().getProgramCounter());
	}
	
	/**
	 * 0x23
	 * INX H
	 * HL <- HL + 1
	 */
	@Test
	public void inxH() {
		cpu.getMemory()[0] = (byte)0x23;
		cpu.getRegisters().setProgramCounter((short)0x0000);
		
		cpu.getRegisters().setHL((short) 0x000f);
		
		OpCodes.inxH().execute(cpu);
		
		assertEquals(0x0010, cpu.getRegisters().getHL());
		assertEquals(1, cpu.getRegisters().getProgramCounter());
	}
	
	/**
	 * 0x13
	 * INX D
	 * DE <- DE + 1
	 */
	@Test
	public void inxD() {
		cpu.getMemory()[0] = (byte)0x13;
		cpu.getRegisters().setProgramCounter((short)0x0000);
		
		cpu.getRegisters().setDE((short) 0x000f);
		
		OpCodes.inxD().execute(cpu);
		
		assertEquals(0x0010, cpu.getRegisters().getDE());
		assertEquals(1, cpu.getRegisters().getProgramCounter());
	}
	
	/**
	 * 0x05
	 * DCR B
	 * B <- B-1
	 * Needs to check arithmetic flags
	 */
	@Test
	public void dcrB() {
		cpu.getMemory()[0] = (byte)0x05;
		cpu.getRegisters().setProgramCounter((short)0x0000);
		cpu.getFlags().setZero(true);
		
		cpu.getRegisters().setB((byte)0x10);
		
		OpCodes.dcrB().execute(cpu);
		
		assertEquals(0x0f, cpu.getRegisters().getB());
		assertEquals(1, cpu.getRegisters().getProgramCounter());
		assertFalse(cpu.getFlags().isZero());
		assertFalse(cpu.getFlags().isSigned());
		assertTrue(cpu.getFlags().isParity());
	}
	
	/**
	 * 0x05
	 * DCR B
	 * B <- B-1
	 * Needs to check arithmetic flags
	 */
	@Test
	public void dcrResultZero() {
		cpu.getMemory()[0] = (byte)0x05;
		cpu.getRegisters().setProgramCounter((short)0x0000);
		
		cpu.getRegisters().setB((byte)0x01);
		
		OpCodes.dcrB().execute(cpu);
		
		assertEquals(0x00, cpu.getRegisters().getB());
		assertEquals(1, cpu.getRegisters().getProgramCounter());
		assertTrue(cpu.getFlags().isZero());
		assertFalse(cpu.getFlags().isSigned());
		assertTrue(cpu.getFlags().isParity());
	}
	
	/**
	 * 0x05
	 * DCR B
	 * B <- B-1
	 * Needs to check arithmetic flags
	 */
	@Test
	public void dcrBResultSignFlag() {
		cpu.getMemory()[0] = (byte)0x05;
		cpu.getRegisters().setProgramCounter((short)0x0000);
		
		cpu.getRegisters().setB((byte)0x00);
		
		OpCodes.dcrB().execute(cpu);
		
		assertEquals((byte) -1, cpu.getRegisters().getB());
		assertEquals(1, cpu.getRegisters().getProgramCounter());
		assertFalse(cpu.getFlags().isZero());
		assertTrue(cpu.getFlags().isSigned());
		assertTrue(cpu.getFlags().isParity());
	}
	
	/**
	 * 0xc2
	 * JNZ
	 * if NZ, PC <- adr
	 */
	@Test
	public void jnzNotZero() {
		cpu.getMemory()[0] = (byte)0xc2;
		cpu.getMemory()[1] = (byte)0xe6;
		cpu.getMemory()[2] = (byte)0x01;
		cpu.getRegisters().setProgramCounter((short)0x0000);
		cpu.getFlags().setZero(false);
		
		OpCodes.jnz().execute(cpu);
		
		assertEquals((short)(0x01e6), cpu.getRegisters().getProgramCounter());
	}
	
	/**
	 * 0xc2
	 * JNZ
	 * if NZ, PC <- adr
	 */
	@Test
	public void jnzIsZero() {
		cpu.getMemory()[0] = (byte)0xc2;
		cpu.getMemory()[1] = (byte)0xe6;
		cpu.getMemory()[2] = (byte)0x01;
		cpu.getRegisters().setProgramCounter((short)0x0000);
		cpu.getFlags().setZero(true);
		
		OpCodes.jnz().execute(cpu);
		
		assertEquals((short)(0x0003), cpu.getRegisters().getProgramCounter());
	}
	
	/**
	 * 0xc9	RET	
	 * PC.lo <- (sp); PC.hi<-(sp+1); SP <- SP+2
	 */
	@Test
	public void ret() {
		cpu.getMemory()[0] = (byte)0xc9;
		cpu.getMemory()[1] = (byte)0x00;

		cpu.getMemory()[2] = (byte)0xff;
		cpu.getMemory()[3] = (byte)0x11;
		
		cpu.getRegisters().setProgramCounter((short)0x0000);
		cpu.getRegisters().setStackPointer((short)0x0002);
		
		OpCodes.ret().execute(cpu);
		
		assertEquals(0x11ff, cpu.getRegisters().getProgramCounter());
		assertEquals(0x0004, cpu.getRegisters().getStackPointer());
	}
	
	/**
	 * 0x36	MVI M,D8	2		(HL) <- byte 2
	 * LD HL
	 */
	@Test
	public void mvHLm() {
		cpu.getMemory()[0] = (byte)0x36;
		cpu.getMemory()[1] = (byte)0xef;
		cpu.getMemory()[2] = (byte)0xbc;
		cpu.getRegisters().setProgramCounter((short)0x0000);
		cpu.getRegisters().setHL((short)0x0002);
		
		OpCodes.mvHLm().execute(cpu);
		
		assertEquals((byte)0xef, cpu.getMemory()[2]);
		assertEquals(0x0002, cpu.getRegisters().getProgramCounter());
	}
	
	/**
	 * 0x7c
	 * MOV A,H		
	 * A <- H
	 */
	@Test
	public void movAH() {
		cpu.getMemory()[0] = (byte)0x7c;
		cpu.getRegisters().setProgramCounter((short)0x0000);
		cpu.getRegisters().setA((byte)0xec);
		cpu.getRegisters().setH((byte)0x14);
		
		OpCodes.movAH().execute(cpu);
		
		assertEquals((byte)0x14, cpu.getRegisters().getA());
		assertEquals((short)0x0001, cpu.getRegisters().getProgramCounter());
	}
	
	/**
	 * 0xfe	
	 * CPI D8
	 * A - next byte
	 */
	@Test
	public void subByteA() {
		cpu.getMemory()[0] = (byte)0xfe;
		cpu.getMemory()[1] = (byte)0x10;
		cpu.getRegisters().setProgramCounter((short)0x0000);

		cpu.getRegisters().setA((byte)0xff);
		
		OpCodes.subByteA().execute(cpu);
		
		assertEquals((byte)0xff, cpu.getRegisters().getA());
		/** TODO Also assert flags set correctly since that is all that matters with this opcode */
		assertEquals((short)0x0002, cpu.getRegisters().getProgramCounter());
	}
	
	/**
	 * 0x0e
	 * MVI C,D8	2		
	 * C <- byte 2
	 */
	@Test
	public void movC() {
		cpu.getMemory()[0] = (byte)0x0e;
		cpu.getMemory()[1] = (byte)0xfe;
		cpu.getRegisters().setProgramCounter((short)0x0000);
		
		cpu.getRegisters().setC((byte)0x00);
		
		OpCodes.movC().execute(cpu);
		
		assertEquals((byte)0xfe, cpu.getRegisters().getC());
		assertEquals((short)0x0002, cpu.getRegisters().getProgramCounter());
	}
	
	/**
	 * 0xd5
	 * PUSH D
	 * (sp-2)<-E; (sp-1)<-D; sp <- sp - 2
	 */
	@Test
	public void pushDE() {
		cpu.getMemory()[0] = (byte)0xd5;
		cpu.getMemory()[1] = (byte)0xff;
		cpu.getMemory()[2] = (byte)0xee;
		cpu.getRegisters().setProgramCounter((short)0x0000);
		
		cpu.getRegisters().setStackPointer((short)0x0003);
		cpu.getRegisters().setDE((short)0x1122);
		
		OpCodes.pushDE().execute(cpu);
		
		assertEquals((byte)0x0022, cpu.getMemory()[1]);
		assertEquals((byte)0x0011, cpu.getMemory()[2]);
		assertEquals((short)0x0001, cpu.getRegisters().getStackPointer());
	}
	
	/**
	 * 0xe5	
	 * PUSH H
	 * (sp-2)<-L; (sp-1)<-H; sp <- sp - 2
	 */
	@Test
	public void pushHL() {
		cpu.getMemory()[0] = (byte)0xd5;
		cpu.getMemory()[1] = (byte)0xff;
		cpu.getMemory()[2] = (byte)0xee;
		cpu.getRegisters().setProgramCounter((short)0x0000);
		
		cpu.getRegisters().setStackPointer((short)0x0003);
		cpu.getRegisters().setHL((short)0x1122);
		
		OpCodes.pushHL().execute(cpu);
		
		assertEquals((byte)0x0022, cpu.getMemory()[1]);
		assertEquals((byte)0x0011, cpu.getMemory()[2]);
		assertEquals((short)0x0001, cpu.getRegisters().getStackPointer());
	}
	
	/**
	 * 0x26	
	 * MVI H,D8		
	 * H <- byte 2
	 */
	@Test
	public void ldH() {
		cpu.getMemory()[0] = (byte)0x26;
		cpu.getMemory()[1] = (byte)0x22;
		cpu.getRegisters().setProgramCounter((short)0x0000);
		
		cpu.getRegisters().setH((byte)0xaa);
		
		OpCodes.ldH().execute(cpu);
		
		assertEquals((byte)0x22, cpu.getRegisters().getH());
		assertEquals((short)0x0002, cpu.getRegisters().getProgramCounter());
	}
	
	/**
	 * 0x6f
	 * MOV L,A
	 * L <- A
	 */
	@Test
	public void movLA() {
		cpu.getMemory()[0] = (byte)0x6f;
		cpu.getRegisters().setProgramCounter((short)0x0000);
		
		cpu.getRegisters().setA((byte)0xcd);
		cpu.getRegisters().setC((byte)0x00);
		
		OpCodes.movLA().execute(cpu);
		
		assertEquals((byte)0xcd, cpu.getRegisters().getL());
		assertEquals((short)0x0001, cpu.getRegisters().getProgramCounter());
	}
	
	/**
	 * 0x29	DAD H
	 * CY	
	 * HL = HL + HL
	 */
	@Test
	public void addHlHl() {
		cpu.getMemory()[0] = (byte)0x29;
		cpu.getRegisters().setProgramCounter((short)0x0000);
		cpu.getFlags().setCarry(false);
		
		cpu.getRegisters().setHL((short)0x0e0a);
		
		OpCodes.addHlHl().execute(cpu);
		
		assertEquals((short)0x1c14, cpu.getRegisters().getHL());
		assertEquals((short)0x0001, cpu.getRegisters().getProgramCounter());
		assertFalse(cpu.getFlags().isCarry());
	}
	
	/**
	 * 0x29	DAD H
	 * CY	
	 * HL = HL + HL
	 */
	@Test
	public void addHlHlIsCarry() {
		cpu.getMemory()[0] = (byte)0x29;
		cpu.getRegisters().setProgramCounter((short)0x0000);
		cpu.getFlags().setCarry(false);
		
		cpu.getRegisters().setHL((short)0xcccc);
		
		OpCodes.addHlHl().execute(cpu);
		
		assertEquals((short)0x9998, cpu.getRegisters().getHL());
		assertEquals((short)0x0001, cpu.getRegisters().getProgramCounter());
		assertTrue(cpu.getFlags().isCarry());
	}
	
	/**
	 * 0x19	DAD D
	 * CY	
	 * HL = HL + DE
	 */
	@Test
	public void addHlDeNoCarry() {
		cpu.getMemory()[0] = (byte)0x29;
		cpu.getRegisters().setProgramCounter((short)0x0000);
		cpu.getFlags().setCarry(false);
		
		cpu.getRegisters().setHL((short)0x0e0a);
		cpu.getRegisters().setDE((short)0x0e09);
		
		OpCodes.addHlDe().execute(cpu);
		
		assertEquals((short)0x1c13, cpu.getRegisters().getHL());
		assertEquals((short)0x0001, cpu.getRegisters().getProgramCounter());
		assertFalse(cpu.getFlags().isCarry());
	}
	
	/**
	 * 0x19	DAD D
	 * CY	
	 * HL = HL + DE
	 */
	@Test
	public void addHlDeWithCarry() {
		cpu.getMemory()[0] = (byte)0x29;
		cpu.getRegisters().setProgramCounter((short)0x0000);
		cpu.getFlags().setCarry(false);
		
		cpu.getRegisters().setHL((short)0xcccc);
		cpu.getRegisters().setDE((short)0xcccd);
		
		OpCodes.addHlDe().execute(cpu);
		
		assertEquals((short)0x9999, cpu.getRegisters().getHL());
		assertEquals((short)0x0001, cpu.getRegisters().getProgramCounter());
		assertTrue(cpu.getFlags().isCarry());
	}
	
	/**
	 * 0xeb	XCHG
	 * H <-> D
	 * L <-> E
	 */
	@Test
	public void xchgHLDE() {
		cpu.getMemory()[0] = (byte)0xeb;
		cpu.getRegisters().setProgramCounter((short)0x0000);
		
		cpu.getRegisters().setHL((short)0x1111);
		cpu.getRegisters().setDE((short)0x2222);
		
		OpCodes.xchgHLDE().execute(cpu);
		
		assertEquals((short)0x2222, cpu.getRegisters().getHL());
		assertEquals((short)0x1111, cpu.getRegisters().getDE());
		assertEquals((short)0x0001, cpu.getRegisters().getProgramCounter());
	}
	
}
