package de.gmasil.mhw.ctceditor.ui.panel;

import java.util.Set;

import de.gmasil.mhw.ctceditor.ctc.CtcChain;
import de.gmasil.mhw.ctceditor.ui.panel.generic.GenericCtcSetEditorPanel;

public class CtcChainEditorPanel extends GenericCtcSetEditorPanel<CtcChain> {
	public CtcChainEditorPanel(Set<CtcChain> chains) {
		super("CTC Chain", CtcChain.class, chains);
	}
}