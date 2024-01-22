package com.jasonrobison.emulator8080.emulator;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

public class Emulator {
	
	private static Logger LOGGER = Logger.getLogger(Emulator.class);
	
	Cpu cpu = new Cpu();
	
	public static void main(String[] args) {
		String fileName = args[0];
		
		Emulator emulator = new Emulator();
		emulator.loadGameIntoMemory(fileName);
		emulator.executeGame();
	}
	
	public void loadGameIntoMemory(String pathToGame) {
		try {
			InputStream inputStream = new FileInputStream(pathToGame);

			byte[] bytes = IOUtils.toByteArray(inputStream);

			for (int i = 0; i < bytes.length; i++) {
				cpu.getMemory()[i] = bytes[i];
			}
		} catch (IOException e) {
			LOGGER.error("Unable to read from file: " + pathToGame, e);
			System.exit(1);
		}
	}
	
	public void executeGame() {
		cpu.executeProgram();
	}

}
