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
package de.gmasil.mhw.ctceditor;

import java.io.File;

import de.gmasil.mhw.ctceditor.logging.AnsiAwareConsoleAppender;

public class CtcEditorStarterWrapper {
	private static boolean runningFromIde = false;

	// this class is not allowed to use a logger
	// logger has to be configured before first declaration of any logger
	public static void main(String... args) {
		// only check for logger configuration here
		boolean batchMode = false;
		boolean colorMode = false;
		for (String arg : args) {
			if (arg.startsWith("-")) {
				if (arg.equals("-B")) {
					batchMode = true;
				} else if (arg.equals("-C")) {
					colorMode = true;
				}
			}
		}
		if (batchMode != colorMode) {
			// user error handling is done in CtcEditorStarter when logger is available
			if (batchMode) {
				AnsiAwareConsoleAppender.setAutoAnsi(false);
				AnsiAwareConsoleAppender.setUseAnsi(false);
			} else {
				AnsiAwareConsoleAppender.setAutoAnsi(false);
				AnsiAwareConsoleAppender.setUseAnsi(true);
			}
		}
		// check if running from IDE
		try {
			File sourceLocation = new File(
					CtcEditorStarterWrapper.class.getProtectionDomain().getCodeSource().getLocation().toURI());
			if (sourceLocation.getAbsolutePath().replace('\\', '/').endsWith("target/classes")) {
				// is running from IDE
				runningFromIde = true;
				AnsiAwareConsoleAppender.setInstallAnsiConsole(false);
			}
		} catch (Exception e) {
			// ignore
		}
		// perform the actual startup
		CtcEditorStarter.start(args);
	}

	public static boolean isRunningFromIde() {
		return runningFromIde;
	}
}
