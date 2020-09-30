package de.gmasil.mhw.ctceditor.ui.panel;

import java.util.Set;

import de.gmasil.mhw.ctceditor.ctc.CtcBone;

public class CtcBoneEditor extends CtcEditorPanel {
	private Set<CtcBone> bones;
	private boolean dataChanged = false;

	public CtcBoneEditor(Set<CtcBone> bones) {
		super("CTC Bone", true);
		this.bones = bones;
	}

	@Override
	public boolean hasDataChanged() {
		return dataChanged;
	}
}
