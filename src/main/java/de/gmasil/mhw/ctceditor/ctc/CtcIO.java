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
package de.gmasil.mhw.ctceditor.ctc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

public class CtcIO {
	private CtcIO() {
	}

	public static byte[] readBytes(File file) throws IOException {
		try (FileInputStream fis = new FileInputStream(file)) {
			int current;
			List<Byte> bytes = new LinkedList<>();
			while ((current = fis.read()) != -1) {
				bytes.add((byte) current);
			}
			byte[] raw = new byte[bytes.size()];
			current = 0;
			for (byte b : bytes) {
				raw[current++] = b;
			}
			return raw;
		}
	}

	public static Ctc readFile(File file) throws IOException {
		return new Ctc(readBytes(file));
	}

	public static void write(Ctc ctc, File file) throws IOException {
		try (OutputStream os = new FileOutputStream(file)) {
			os.write(ctc.getBytes());
		}
	}
}
