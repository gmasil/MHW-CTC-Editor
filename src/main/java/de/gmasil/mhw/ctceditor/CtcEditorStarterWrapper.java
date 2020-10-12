package de.gmasil.mhw.ctceditor;

import de.gmasil.mhw.ctceditor.logging.AnsiAwareConsoleAppender;

public class CtcEditorStarterWrapper {
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
		// perform the actual startup
		CtcEditorStarter.start(args);
	}
}
