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
		OutputStream os = new FileOutputStream(file);
		os.write(ctc.getBytes());
		os.close();
	}
}
