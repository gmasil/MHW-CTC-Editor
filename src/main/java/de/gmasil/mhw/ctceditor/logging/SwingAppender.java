package de.gmasil.mhw.ctceditor.logging;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JTextArea;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

public class SwingAppender extends AppenderBase<ILoggingEvent> {
	private static JTextArea console = null;
	private static StringBuilder cache = new StringBuilder();

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
		String msg = timestamp + " [" + formattedLevel + "] " + event.getFormattedMessage() + "\n";
		if (console != null) {
			console.setText(console.getText() + msg);
		} else {
			synchronized (cache) {
				cache.append(msg);
			}
		}
	}
}
