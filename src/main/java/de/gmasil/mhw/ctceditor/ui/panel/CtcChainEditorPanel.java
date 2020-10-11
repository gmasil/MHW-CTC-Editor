package de.gmasil.mhw.ctceditor.ui.panel;

import java.util.Set;

import de.gmasil.mhw.ctceditor.ctc.CtcChain;
import de.gmasil.mhw.ctceditor.ui.api.CtcChangedCallback;
import de.gmasil.mhw.ctceditor.ui.api.RepaintTreeCallback;
import de.gmasil.mhw.ctceditor.ui.panel.generic.GenericCtcSetEditorPanel;

public class CtcChainEditorPanel extends GenericCtcSetEditorPanel<CtcChain> {
	public CtcChainEditorPanel(Set<CtcChain> chains, RepaintTreeCallback treeRepainter,
			CtcChangedCallback ctcChangedCallback) {
		super("CTC Chain", CtcChain.class, chains, treeRepainter, ctcChangedCallback);
	}
}
