package de.gmasil.mhw.ctceditor.ctc;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Ctc {
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
