package de.gmasil.mhw.ctceditor.logging;

import org.fusesource.jansi.internal.CLibrary;

import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

public class AnsiAwareConsoleAppender extends AppenderBase<ILoggingEvent> {
	private boolean useAnsi = false;
	private PatternLayoutEncoder encoder;

	public PatternLayoutEncoder getEncoder() {
		return encoder;
	}

	public void setEncoder(PatternLayoutEncoder encoder) {
		this.encoder = encoder;
	}

	@Override
	public void start() {
		useAnsi = CLibrary.isatty(CLibrary.STDOUT_FILENO) == 1;
		super.start();
	}

	@Override
	protected void append(ILoggingEvent event) {
		String msg = encoder.getLayout().doLayout(event);
		if (!useAnsi) {
			msg = msg.replaceAll("\u001B\\[[;\\d]*m", "");
		}
		System.out.print(msg);
	}
}
