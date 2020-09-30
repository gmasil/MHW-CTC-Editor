package de.gmasil.mhw.ctceditor.logging;

import org.fusesource.jansi.internal.CLibrary;
import org.fusesource.jansi.internal.WindowsSupport;

import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.pattern.PatternLayoutEncoderBase;

public class AnsiAwareConsoleAppender<E> extends AppenderBase<E> {
	private boolean useAnsi = false;
	private PatternLayoutEncoderBase<E> encoder;

	public PatternLayoutEncoderBase<E> getEncoder() {
		return encoder;
	}

	public void setEncoder(PatternLayoutEncoderBase<E> encoder) {
		this.encoder = encoder;
	}

	@Override
	public void start() {
		useAnsi = CLibrary.isatty(CLibrary.STDOUT_FILENO) == 1;
		if (useAnsi && WindowsSupport.getConsoleMode() != -1) {
			useAnsi = false;
		}
		super.start();
	}

	@Override
	protected void append(E event) {
		String msg = encoder.getLayout().doLayout(event);
		if (!useAnsi) {
			msg = msg.replaceAll("\u001B\\[[;\\d]*m", "");
		}
		System.out.print(msg);
	}
}
