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
package de.gmasil.mhw.ctceditor.ui;

import java.lang.invoke.MethodHandles;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppParameters {
	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private List<String> fileNames = new LinkedList<>();
	private boolean batchMode = false;
	private boolean colorMode = false;

	public AppParameters(String... args) {
		for (String arg : args) {
			if (arg.startsWith("-")) {
				if (arg.equals("-B")) {
					batchMode = true;
				} else if (arg.equals("-C")) {
					colorMode = true;
				} else {
					LOG.warn("Unknown parameter: {}", arg);
				}
			} else {
				fileNames.add(arg);
			}
		}
		if (batchMode && colorMode) {
			LOG.warn("Cannot set batch and color mode, falling back to default (auto)");
			batchMode = false;
			colorMode = false;
		}
	}

	public List<String> getFileNames() {
		return Collections.unmodifiableList(fileNames);
	}

	public boolean isBatchMode() {
		return batchMode;
	}

	public boolean isColorMode() {
		return colorMode;
	}
}
