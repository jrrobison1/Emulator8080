package com.jasonrobison.emulator8080.emulator.opcodes;

import org.apache.log4j.Logger;

import com.jasonrobison.emulator8080.emulator.Cpu;

public class OpCodes {

	private static final Logger LOGGER = Logger.getLogger(OpCodes.class);

	public static OpCode notImplemented() {
		return new OpCode() {
			public void execute(Cpu cpu) {
				LOGGER.debug(getCurrentAddressAsString(cpu) + "Opcode not implemented: "
						+ Integer.toHexString(Byte.toUnsignedInt(cpu
								.getCurrentOpCode())));
				System.exit(1);

			}
		};
	}

	public static OpCode nop() {
		return new OpCode() {
			public void execute(Cpu cpu) {
				LOGGER.debug(getCurrentAddressAsString(cpu) + "NOP");
				cpu.getRegisters().incrementProgramCounter();
			}
		};
	}

	public static OpCode lxiBD16() {
		return new OpCode() {
			public void execute(Cpu cpu) {
				LOGGER.debug(getCurrentAddressAsString(cpu) + "LXI B,D16 $"
						+ readWord(cpu));
				cpu.getRegisters().setBC(readWord(cpu));
				cpu.getRegisters().incrementProgramCounter(3);
			}
		};
	}

	public static OpCode jmpAddr() {
		return new OpCode() {
			public void execute(Cpu cpu) {
				LOGGER.debug(getCurrentAddressAsString(cpu) + "JMP ADDR $"
						+ getAsHexString(readWord(cpu)));
				cpu.getRegisters().setProgramCounter(readWord(cpu));
			}
		};
	}

	/**
	 * 0x31
	 * 
	 * @return
	 */
	public static OpCode lxiSpD16() {
		return new OpCode() {
			public void execute(Cpu cpu) {
				LOGGER.debug(getCurrentAddressAsString(cpu) + "LXI SP, D16 "
						+ getActualOrderWord(cpu));
				cpu.getRegisters().setStackPointer(readWord(cpu));
				cpu.getRegisters().incrementProgramCounter(3);
			}
		};
	}

	/**
	 * B <- byte 2
	 * 
	 * @param byteToConvert
	 * @return
	 */
	public static OpCode mviBD8() {
		return new OpCode() {
			public void execute(Cpu cpu) {
				byte argument1 = cpu.getMemory()[cpu.getRegisters()
						.getProgramCounter() + 1];

				LOGGER.debug(getCurrentAddressAsString(cpu) + "MVI B, D8 "
						+ getAsHexString(argument1));
				cpu.getRegisters().setB(argument1);
				cpu.getRegisters().incrementProgramCounter(2);
			}
		};
	}

	/**
	 * 0xcd Call Addr (SP-1)<-PC.hi;(SP-2)<-PC.lo;SP<-SP+2;PC=adr
	 * 
	 * @return
	 */
	public static OpCode callAddr() {
		return new OpCode() {
			public void execute(Cpu cpu) {
				short returnAddress = (short) (cpu.getRegisters()
						.getProgramCounter() + 3);
				cpu.getMemory()[cpu.getRegisters().getStackPointer() - 1] = (byte) ((returnAddress >> 8) & 0xff);
				cpu.getMemory()[cpu.getRegisters().getStackPointer() - 2] = (byte) ((returnAddress & 0xff));

				LOGGER.debug(getCurrentAddressAsString(cpu) + "CALL Addr "
						+ getActualOrderWord(cpu));
				cpu.getRegisters().setProgramCounter(readWord(cpu));
				cpu.getRegisters().setStackPointer(
						(short) (cpu.getRegisters().getStackPointer() - 2));

			}
		};
	}

	/**
	 * 0x11 D <- byte 3, E <- byte 2
	 * 
	 * @return
	 */
	public static OpCode lxiDD16() {
		return new OpCode() {
			public void execute(Cpu cpu) {
				LOGGER.debug(getCurrentAddressAsString(cpu) + "LXI D,D16 "
						+ getActualOrderWord(cpu));

				cpu.getRegisters().setDE(readWord(cpu));
				cpu.getRegisters().incrementProgramCounter(3);
			}
		};
	}

	/**
	 * 0x21 H <- byte 3, L <- byte 2
	 * 
	 * @return
	 */
	public static OpCode lxiHD16() {
		return new OpCode() {
			public void execute(Cpu cpu) {
				LOGGER.debug(getCurrentAddressAsString(cpu) + "LXI H,D16 "
						+ getActualOrderWord(cpu));

				cpu.getRegisters().setHL(readWord(cpu));
				cpu.getRegisters().incrementProgramCounter(3);
			}
		};
	}

	/**
	 * 0x1a LDAX D A <- (DE)
	 * 
	 * @return
	 */
	public static OpCode ldaxD() {
		return new OpCode() {
			public void execute(Cpu cpu) {
				LOGGER.debug(getCurrentAddressAsString(cpu) + "LDAX D ");

				cpu.getRegisters().setA(
						cpu.getMemory()[cpu.getRegisters().getDE()]);
				cpu.getRegisters().incrementProgramCounter();
			}
		};
	}

	public static OpCode movMA() {
		return new OpCode() {
			public void execute(Cpu cpu) {
				LOGGER.debug(getCurrentAddressAsString(cpu) + "MOV M A ");

				cpu.getMemory()[cpu.getRegisters().getHL()] = cpu
						.getRegisters().getA();
				cpu.getRegisters().incrementProgramCounter();
			}
		};
	}

	/**
	 * 0x23 INX H HL <- HL + 1
	 */
	public static OpCode inxH() {
		return new OpCode() {
			public void execute(Cpu cpu) {
				LOGGER.debug(getCurrentAddressAsString(cpu) + "INX H ");

				cpu.getRegisters().setHL(
						(short) (cpu.getRegisters().getHL() + 1));
				cpu.getRegisters().incrementProgramCounter();
			}
		};
	}

	/**
	 * 0x13 INX D DE <- DE + 1
	 */
	public static OpCode inxD() {
		return new OpCode() {
			public void execute(Cpu cpu) {
				LOGGER.debug(getCurrentAddressAsString(cpu) + "INX D ");

				cpu.getRegisters().setDE(
						(short) (cpu.getRegisters().getDE() + 1));
				cpu.getRegisters().incrementProgramCounter();
			}
		};
	}

	/**
	 * 0x05 DCR B B <- B-1 Needs to check arithmetic flags
	 */
	public static OpCode dcrB() {
		return new OpCode() {
			public void execute(Cpu cpu) {
				LOGGER.debug(getCurrentAddressAsString(cpu) + "DCR B ");

				cpu.getRegisters().setB(
						decrementByte(cpu.getRegisters().getB(), cpu));
				cpu.getRegisters().incrementProgramCounter();
			}
		};
	}

	/**
	 * 0xc2 JNZ if NZ, PC <- adr
	 */
	public static OpCode jnz() {
		return new OpCode() {
			public void execute(Cpu cpu) {
				LOGGER.debug(getCurrentAddressAsString(cpu) + "JNZ "
						+ getActualOrderWord(cpu));

				if (cpu.getFlags().isZero()) {
					cpu.getRegisters().incrementProgramCounter(3);
				} else {
					cpu.getRegisters().setProgramCounter(readWord(cpu));
				}
			}
		};
	}
	
	/**
	 * 0xc9	RET	
	 * PC.lo <- (sp); PC.hi<-(sp+1); SP <- SP+2
	 */
	public static OpCode ret() {
		return new OpCode() {
			public void execute(Cpu cpu) {
				LOGGER.debug(getCurrentAddressAsString(cpu) + "RET " + getAsHexString(readStackPointerWord(cpu)));

				cpu.getRegisters().setProgramCounter(readStackPointerWord(cpu));
				cpu.getRegisters().setStackPointer((short)(cpu.getRegisters().getStackPointer() + 2));
			}
		};
	}
	
	/**
	 * 0x36	MVI M,D8	2		(HL) <- byte 2
	 * LD HL
	 */
	public static OpCode mvHLm() {
		return new OpCode() {
			public void execute(Cpu cpu) {
				LOGGER.debug(getCurrentAddressAsString(cpu) + "MVI M,D8 " + getAsHexString(readByte(cpu)));

				cpu.getMemory()[cpu.getRegisters().getHL()] = readByte(cpu);
				cpu.getRegisters().incrementProgramCounter(2);
			}
		};
	}
	
	/**
	 * 0x7c
	 * MOV A,H		
	 * A <- H
	 */
	public static OpCode movAH() {
		return new OpCode() {
			public void execute(Cpu cpu) {
				LOGGER.debug(getCurrentAddressAsString(cpu) + "MOV A,H ");

				cpu.getRegisters().setA(cpu.getRegisters().getH());
				cpu.getRegisters().incrementProgramCounter();
			}
		};
	}
	
	/**
	 * 0xfe	
	 * CPI D8
	 * A - next byte
	 */
	public static OpCode subByteA() {
		return new OpCode() {
			public void execute(Cpu cpu) {
				LOGGER.debug(getCurrentAddressAsString(cpu) + "CPI D8 " + readByte(cpu));

				subtractByte(cpu.getRegisters().getA(), readByte(cpu), cpu);
				cpu.getRegisters().incrementProgramCounter(2);
			}
		};
	}
	
	/**
	 * 0x0e
	 * MVI C,D8	
	 * C <- byte 2
	 */
	public static OpCode movC() {
		return new OpCode() {
			public void execute(Cpu cpu) {
				LOGGER.debug(getCurrentAddressAsString(cpu) + "MVI C,D8 " + readByte(cpu));
				
				cpu.getRegisters().setC(readByte(cpu));
				cpu.getRegisters().incrementProgramCounter(2);
			}
		};
	}
	
	/**
	 * 0xd5
	 * PUSH D
	 * (sp-2)<-E; (sp-1)<-D; sp <- sp - 2
	 */
	public static OpCode pushDE() {
		return new OpCode() {
			public void execute(Cpu cpu) {
				LOGGER.debug(getCurrentAddressAsString(cpu) + "PUSH DE ");
				
				pushWordToStack(cpu.getRegisters().getDE(), cpu);
				cpu.getRegisters().incrementProgramCounter();
			}
		};
	}
	
	/**
	 * 0xe5	
	 * PUSH H
	 * (sp-2)<-L; (sp-1)<-H; sp <- sp - 2
	 */
	public static OpCode pushHL() {
		return new OpCode() {
			public void execute(Cpu cpu) {
				LOGGER.debug(getCurrentAddressAsString(cpu) + "PUSH HL ");
				
				pushWordToStack(cpu.getRegisters().getHL(), cpu);
				cpu.getRegisters().incrementProgramCounter();
			}
		};
	}
	
	/**
	 * 0x26	
	 * MVI H,D8		
	 * H <- byte 2
	 */
	public static OpCode ldH() {
		return new OpCode() {
			public void execute(Cpu cpu) {
				LOGGER.debug(getCurrentAddressAsString(cpu) + "LD H " + getAsHexString(readByte(cpu)));
				
				cpu.getRegisters().setH(readByte(cpu));
				cpu.getRegisters().incrementProgramCounter(2);
			}
		};
	}
	
	/**
	 * 0x6f
	 * MOV L,A
	 * L <- A
	 */
	public static OpCode movLA() {
		return new OpCode() {
			public void execute(Cpu cpu) {
				LOGGER.debug(getCurrentAddressAsString(cpu) + "MOV L,A ");
				
				cpu.getRegisters().setL(cpu.getRegisters().getA());
				cpu.getRegisters().incrementProgramCounter();
			}
		};
	}
	
	/**
	 * 0x29	DAD H
	 * CY	
	 * HL = HL + HL
	 */
	public static OpCode addHlHl() {
		return new OpCode() {
			public void execute(Cpu cpu) {
				LOGGER.debug(getCurrentAddressAsString(cpu) + "ADD HL,HL ");
				
				cpu.getRegisters().setHL(addWords(cpu.getRegisters().getHL(), cpu.getRegisters().getHL(), cpu));
				cpu.getRegisters().incrementProgramCounter();
			}
		};
	}
	
	/**
	 * 0x19	DAD D
	 * CY	
	 * HL = HL + DE
	 */
	public static OpCode addHlDe() {
		return new OpCode() {
			public void execute(Cpu cpu) {
				LOGGER.debug(getCurrentAddressAsString(cpu) + "ADD HL,DE ");
				
				cpu.getRegisters().setHL(addWords(cpu.getRegisters().getHL(), cpu.getRegisters().getDE(), cpu));
				cpu.getRegisters().incrementProgramCounter();
			}
		};
	}
	
	/**
	 * 0xeb	XCHG
	 * H <-> D
	 * L <-> E
	 */
	public static OpCode xchgHLDE() {
		return new OpCode() {
			public void execute(Cpu cpu) {
				LOGGER.debug(getCurrentAddressAsString(cpu) + "XCHG HL,DE ");
				
				short dE = cpu.getRegisters().getDE();
				cpu.getRegisters().setDE(cpu.getRegisters().getHL());
				cpu.getRegisters().setHL(dE);
				
				cpu.getRegisters().incrementProgramCounter();
			}
		};
	}
	
	private static void pushWordToStack(short word, Cpu cpu) {
		cpu.getMemory()[cpu.getRegisters().getStackPointer() - 1] = (byte)((word >>> 8) & 0xff);
		cpu.getMemory()[cpu.getRegisters().getStackPointer() - 2] = (byte)(word  & 0x00ff);
		
		cpu.getRegisters().setStackPointer((short)(cpu.getRegisters().getStackPointer() - 2));
	}
	

	private static String getActualOrderWord(Cpu cpu) {
		byte argument1 = cpu.getMemory()[cpu.getRegisters().getProgramCounter() + 1];
		byte argument2 = cpu.getMemory()[cpu.getRegisters().getProgramCounter() + 2];

		return getAsHexString(argument1) + getAsHexString(argument2);
	}

	private static short readWord(Cpu cpu) {
		byte argument1 = cpu.getMemory()[cpu.getRegisters().getProgramCounter() + 1];
		byte argument2 = cpu.getMemory()[cpu.getRegisters().getProgramCounter() + 2];

		return (short) ((argument2 << 8) | (argument1 & 0xff));
	}
	
	private static byte readByte(Cpu cpu) {
		return cpu.getMemory()[cpu.getRegisters().getProgramCounter() + 1];
	}
	
	private static short readStackPointerWord(Cpu cpu) {
		byte argument1 = cpu.getMemory()[cpu.getRegisters().getStackPointer()];
		byte argument2 = cpu.getMemory()[cpu.getRegisters().getStackPointer() + 1];

		return (short) ((argument2 << 8) | (argument1 & 0xff));
	}

	private static byte decrementByte(byte byteToDecrement, Cpu cpu) {
		short answer = (short) (((short) byteToDecrement - 1));
		setFlags(answer, cpu);
		
		return (byte) (answer & 0xff);
	}
	
	private static byte subtractByte(byte original, byte subtractBy, Cpu cpu) {
		short answer = (short) (((short) original - subtractBy));
		setFlags(answer, cpu);
		
		return (byte) (answer & 0xff);
	}
	
	private static short addWords(short word1, short word2, Cpu cpu) {
		int answer = Short.toUnsignedInt(word1) + Short.toUnsignedInt(word2);
		
		cpu.getFlags().setCarry(answer > 0xffff);
		return (short)(answer & 0xffff);
	}
	
	private static void setFlags(short answer, Cpu cpu) {
		cpu.getFlags().setZero((answer & 0xff) == 0);
		cpu.getFlags().setSigned((answer & 0x80) > 0);
		cpu.getFlags().setParity(isParity((byte) ((answer) & 0xff)));
	}

	private static boolean isParity(byte b) {
		int s = Byte.toUnsignedInt(b) & 0xff;

		boolean isParity = true;
		while (s != 0) {
			isParity = !isParity;
			s = s & (s - 1);
		}

		return isParity;
	}

	public static String getAsHexString(byte byteToConvert) {
		return String.format("%02x", Byte.toUnsignedInt(byteToConvert));
	}

	public static String getAsHexString(short shortToConvert) {
		return String.format("%04x", Short.toUnsignedInt(shortToConvert));
	}

	public static String getAsBinaryString(byte byteToConvert) {
		return Integer.toBinaryString(Byte.toUnsignedInt(byteToConvert));
	}

	public static String getCurrentAddressAsString(Cpu cpu) {
		return String.format("%04x", cpu.getRegisters().getProgramCounter())
				+ ": ";
	}
}
