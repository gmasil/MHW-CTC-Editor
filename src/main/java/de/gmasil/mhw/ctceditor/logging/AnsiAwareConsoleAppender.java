package de.gmasil.mhw.ctceditor.logging;

import org.fusesource.jansi.internal.CLibrary;
import org.fusesource.jansi.internal.WindowsSupport;

import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.pattern.PatternLayoutEncoderBase;

public class AnsiAwareConsoleAppender<E> extends AppenderBase<E> {
	private static boolean useAnsi = false;
	private static boolean autoAnsi = true;
	private PatternLayoutEncoderBase<E> encoder;

	public PatternLayoutEncoderBase<E> getEncoder() {
		return encoder;
	}

	public void setEncoder(PatternLayoutEncoderBase<E> encoder) {
		this.encoder = encoder;
	}

	@Override
	public void start() {
		if (autoAnsi) {
			boolean setUseAnsi = CLibrary.isatty(CLibrary.STDOUT_FILENO) == 1;
			if (useAnsi && WindowsSupport.getConsoleMode() != -1) {
				setUseAnsi = false;
			}
			if (setUseAnsi != useAnsi) {
				setUseAnsi(setUseAnsi);
			}
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

	public static synchronized void setUseAnsi(boolean useAnsi) {
		AnsiAwareConsoleAppender.useAnsi = useAnsi;
	}

	public static synchronized void setAutoAnsi(boolean autoAnsi) {
		AnsiAwareConsoleAppender.autoAnsi = autoAnsi;
	}
}
