package de.gmasil.mhw.ctceditor.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class ColoredLevelConverter extends ClassicConverter {
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";

	@Override
	public String convert(ILoggingEvent event) {
		return getColor(event.getLevel()) + event.getLevel() + ANSI_RESET;
	}

	private String getColor(Level level) {
		if (level == Level.INFO) {
			return ANSI_BLUE;
		} else if (level == Level.WARN) {
			return ANSI_YELLOW;
		} else if (level == Level.ERROR) {
			return ANSI_RED;
		}
		return ANSI_BLUE;
	}
}
