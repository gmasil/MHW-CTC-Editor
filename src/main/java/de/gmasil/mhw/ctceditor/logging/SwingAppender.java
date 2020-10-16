/**
 * MHW-CTC-Editor
 * Copyright © 2020 Simon Oelerich (simon.oelerich@gmasil.de)
 *
 * This file is part of MHW-CTC-Editor.
 *
 * MHW-CTC-Editor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MHW-CTC-Editor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MHW-CTC-Editor. If not, see <https://www.gnu.org/licenses/>.
 */
package de.gmasil.mhw.ctceditor.logging;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.core.AppenderBase;

public class SwingAppender extends AppenderBase<ILoggingEvent> {
	private static JTextArea console = null;
	private static JScrollPane scroller = null;
	private static StringBuilder cache = new StringBuilder();
	private static boolean printFullStackTrace = true;

	public static void setConsole(JTextArea console, JScrollPane scroller) {
		SwingAppender.console = console;
		SwingAppender.scroller = scroller;
		synchronized (cache) {
			console.setText(cache.toString());
			cache.setLength(0);
			console.setCaretPosition(console.getDocument().getLength());
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
			console.append(msg.toString());
			JScrollBar bar = scroller.getVerticalScrollBar();
			if (bar.getValue() > bar.getMaximum() - bar.getHeight() - 20) {
				console.setCaretPosition(console.getDocument().getLength());
			}
		} else {
			synchronized (cache) {
				cache.append(msg);
			}
		}
	}
}
