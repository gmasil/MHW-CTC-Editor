package de.gmasil.mhw.ctceditor.logging;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import javax.swing.JTextArea;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.core.AppenderBase;

public class SwingAppender extends AppenderBase<ILoggingEvent> {
	private static JTextArea console = null;
	private static StringBuilder cache = new StringBuilder();
	public static boolean printFullStackTrace = true;

	public static void setConsole(JTextArea console) {
		SwingAppender.console = console;
		synchronized (cache) {
			console.setText(cache.toString());
			cache.setLength(0);
		}
	}

	@Override
	protected void append(ILoggingEvent event) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String timestamp = formatter.format(new Date(event.getTimeStamp()));
		String formattedLevel = String.format("%-5s", event.getLevel().toString());
		StringBuilder msg = new StringBuilder();
		msg.append(timestamp + " [" + formattedLevel + "] " + event.getFormattedMessage() + "\n");
		if (printFullStackTrace) {
			IThrowableProxy throwableProxy = event.getThrowableProxy();
			if (throwableProxy != null) {
				msg.append(throwableProxy.getClassName() + ": " + throwableProxy.getMessage() + "\n");
				Arrays.asList(throwableProxy.getStackTraceElementProxyArray()).forEach(ste -> {
					msg.append("    " + ste.toString() + "\n");
				});
			}
		}
		if (console != null) {
			console.setText(console.getText() + msg);
		} else {
			synchronized (cache) {
				cache.append(msg);
			}
		}
	}
}
