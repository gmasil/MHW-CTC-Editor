package de.gmasil.mhw.ctceditor.ui;

import java.io.File;

@FunctionalInterface
public interface FileOpenedListener {
	public void onFileOpened(File file);
}
