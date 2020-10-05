package de.gmasil.mhw.ctceditor.ui.api;

import java.io.Serializable;

@FunctionalInterface
public interface RepaintTreeCallback extends Serializable {
	public void repaintTree();
}
