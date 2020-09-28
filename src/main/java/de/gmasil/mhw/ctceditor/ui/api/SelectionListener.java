package de.gmasil.mhw.ctceditor.ui.api;

import java.util.Set;

import de.gmasil.mhw.ctceditor.ctc.CtcBone;
import de.gmasil.mhw.ctceditor.ctc.CtcChain;
import de.gmasil.mhw.ctceditor.ctc.CtcHeader;

public interface SelectionListener {
	public void onHeaderSelected(CtcHeader header);

	public void onChainSelected(Set<CtcChain> chains);

	public void onBoneSelected(Set<CtcBone> bones);

	public void onIllegalSelection();
}
