package de.gmasil.mhw.ctceditor.ui.api;

import java.io.Serializable;

@FunctionalInterface
public interface AllowSelectionCallback extends Serializable {
	public boolean allowSelectionChange();
}
