/**
 * MHW-CTC-Editor
 * Copyright © 2020 gmasil.de
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

import org.fusesource.jansi.AnsiConsole;
import org.fusesource.jansi.internal.CLibrary;

import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.pattern.PatternLayoutEncoderBase;

public class AnsiAwareConsoleAppender<E> extends AppenderBase<E> {
	private static boolean useAnsi = false;
	private static boolean autoAnsi = true;
	private static boolean installAnsiConsole = true;
	private PatternLayoutEncoderBase<E> encoder;

	public PatternLayoutEncoderBase<E> getEncoder() {
		return encoder;
	}

	public void setEncoder(PatternLayoutEncoderBase<E> encoder) {
		this.encoder = encoder;
	}

	@Override
	public void start() {
		if (installAnsiConsole) {
			AnsiConsole.systemInstall();
		}
		if (autoAnsi) {
			boolean setUseAnsi = CLibrary.isatty(CLibrary.STDOUT_FILENO) == 1;
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

	public static synchronized void setInstallAnsiConsole(boolean installAnsiConsole) {
		AnsiAwareConsoleAppender.installAnsiConsole = installAnsiConsole;
	}
}
