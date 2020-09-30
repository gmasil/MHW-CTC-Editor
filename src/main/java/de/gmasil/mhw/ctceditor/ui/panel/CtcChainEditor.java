package de.gmasil.mhw.ctceditor.ui.panel;

import java.util.Set;

import de.gmasil.mhw.ctceditor.ctc.CtcChain;

public class CtcChainEditor extends CtcEditorPanel {
	private Set<CtcChain> chains;
	private boolean dataChanged = false;

	public CtcChainEditor(Set<CtcChain> chains) {
		super("CTC Chain", true);
		this.chains = chains;
	}

	@Override
	public boolean hasDataChanged() {
		return dataChanged;
	}
}
