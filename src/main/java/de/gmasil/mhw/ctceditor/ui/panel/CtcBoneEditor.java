package de.gmasil.mhw.ctceditor.ui.panel;

import java.util.Set;

import javax.swing.JLabel;

import de.gmasil.mhw.ctceditor.ctc.CtcBone;

public class CtcBoneEditor extends CtcEditorPanel {
	private Set<CtcBone> bones;
	private boolean dataChanged = false;

	public CtcBoneEditor(Set<CtcBone> bones) {
		this.bones = bones;
		add(new JLabel("CTC Bone"));
	}

	@Override
	public boolean hasDataChanged() {
		return dataChanged;
	}
}
