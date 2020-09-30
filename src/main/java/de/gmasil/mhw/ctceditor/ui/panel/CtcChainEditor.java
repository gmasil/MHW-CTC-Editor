package de.gmasil.mhw.ctceditor.ui.panel;

import java.util.Set;

import javax.swing.JLabel;

import de.gmasil.mhw.ctceditor.ctc.CtcChain;

public class CtcChainEditor extends CtcEditorPanel {
	private Set<CtcChain> chains;
	private boolean dataChanged = false;

	public CtcChainEditor(Set<CtcChain> chains) {
		this.chains = chains;
		add(new JLabel("CTC Chain"));
	}

	@Override
	public boolean hasDataChanged() {
		return dataChanged;
	}
}
