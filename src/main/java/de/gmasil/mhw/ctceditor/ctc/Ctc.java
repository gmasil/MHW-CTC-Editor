/**
 * MHW-CTC-Editor
 * Copyright © 2020 Simon Oelerich (simon.oelerich@gmasil.de)
 *
 * This file is part of MHW-CTC-Editor.
 *
 * MHW-CTC-Editor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MHW-CTC-Editor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MHW-CTC-Editor. If not, see <https://www.gnu.org/licenses/>.
 */
package de.gmasil.mhw.ctceditor.ctc;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Ctc implements Serializable {
	public static final int HEADER_LENGTH = 80;
	public static final int CHAIN_LENGTH = 80;
	public static final int BONE_LENGTH = 112;

	private CtcHeader header;
	private List<CtcChain> chains = new LinkedList<>();

	public Ctc(byte[] raw) {
		header = new CtcHeader(Arrays.copyOfRange(raw, 0, HEADER_LENGTH));
		for (int i = 0; i < getHeader().getBoneChainCount(); i++) {
			int from = HEADER_LENGTH + i * CHAIN_LENGTH;
			int to = from + CHAIN_LENGTH;
			chains.add(new CtcChain(Arrays.copyOfRange(raw, from, to)));
		}
		for (int i = 0; i < getHeader().getBoneCount(); i++) {
			int from = HEADER_LENGTH + getHeader().getBoneChainCount() * CHAIN_LENGTH + i * BONE_LENGTH;
			int to = from + BONE_LENGTH;
			getChainFromBone(i).getBones().add(new CtcBone(Arrays.copyOfRange(raw, from, to)));
		}
	}

	/**
	 * Recalculates the following fields:
	 * <ul>
	 * <li>header: bone chain count</li>
	 * <li>header: bone count</li>
	 * <li>bone chain: chain length</li>
	 * </ul>
	 * 
	 * @return boolean true if data has been changed
	 */
	public boolean recalculate() {
		boolean dataChanged = false;
		int chainCount = chains.size();
		if (header.getBoneChainCount() != chainCount) {
			header.setBoneChainCount(chainCount);
			dataChanged = true;
		}
		int boneCount = getBones().size();
		if (header.getBoneCount() != boneCount) {
			header.setBoneCount(boneCount);
			dataChanged = true;
		}
		for (CtcChain chain : chains) {
			int chainLength = chain.getBones().size();
			if (chain.getChainLength() != chainLength) {
				chain.setChainLength(chainLength);
				dataChanged = true;
			}
		}
		return dataChanged;
	}

	public List<CtcBone> findBonesWithDuplicateBoneFunctionIds() {
		List<CtcBone> list = new LinkedList<>();
		Set<Integer> boneFunctionIds = new HashSet<>();
		for (CtcBone bone : getBones()) {
			if (boneFunctionIds.contains(bone.getBoneFunctionID())) {
				list.add(bone);
			} else {
				boneFunctionIds.add(bone.getBoneFunctionID());
			}
		}
		return list;
	}

	public byte[] getBytes() {
		ByteBuffer buffer = ByteBuffer.allocate(getTotalByteLength());
		buffer.put(getHeader().getBytes());
		for (CtcChain chain : getChains()) {
			buffer.put(chain.getBytes());
		}
		for (CtcChain chain : getChains()) {
			for (CtcBone bone : chain.getBones()) {
				buffer.put(bone.getBytes());
			}
		}
		return buffer.array();
	}

	public CtcHeader getHeader() {
		return header;
	}

	public List<CtcChain> getChains() {
		return chains;
	}

	public List<CtcBone> getBones() {
		return chains.stream().map(CtcChain::getBones).flatMap(List::stream).collect(Collectors.toList());
	}

	public CtcChain getChainFromBone(int index) {
		int current = 0;
		for (CtcChain chain : getChains()) {
			current += chain.getChainLength();
			if (index < current) {
				return chain;
			}
		}
		return null;
	}

	public int getTotalByteLength() {
		return HEADER_LENGTH + header.getBoneChainCount() * CHAIN_LENGTH + header.getBoneCount() * BONE_LENGTH;
	}

	@Override
	public String toString() {
		String chainsString = String.join("\n", getChains().stream().map(Object::toString).toArray(String[]::new));
		String bonesString = String.join("\n", getBones().stream().map(Object::toString).toArray(String[]::new));
		return header + "\n\n" + chainsString + "\n\n" + bonesString;
	}
}
