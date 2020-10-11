package de.gmasil.mhw.ctceditor.ui.panel;

import de.gmasil.mhw.ctceditor.ctc.CtcHeader;
import de.gmasil.mhw.ctceditor.ui.api.CtcChangedCallback;
import de.gmasil.mhw.ctceditor.ui.api.RepaintTreeCallback;
import de.gmasil.mhw.ctceditor.ui.panel.generic.GenericCtcObjectEditorPanel;

public class CtcHeaderEditorPanel extends GenericCtcObjectEditorPanel<CtcHeader> {
	public CtcHeaderEditorPanel(CtcHeader header, RepaintTreeCallback treeRepainter,
			CtcChangedCallback ctcChangedCallback) {
		super("CTC Header", CtcHeader.class, header, treeRepainter, ctcChangedCallback);
	}
}
