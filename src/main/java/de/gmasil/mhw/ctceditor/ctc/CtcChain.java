/**
 * MHW-CTC-Editor
 * Copyright © 2020 gmasil.de
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
import java.nio.ByteOrder;
import java.util.LinkedList;
import java.util.List;

public class CtcChain implements Serializable {
	@Readonly
	@Info("Chain Length")
	private int chainLength;
	@Info("Usually a number between 0 and 10")
	private byte collision;
	@Info("Weightiness")
	private byte weightiness;
	private byte[] unknownByteSet = new byte[26];
	@Info("10 = 1m/s^2, usually 0")
	private float xGravity;
	@Info("10 = 1m/s^2, -981 for normal gravity")
	private float yGravity;
	@Info("10 = 1m/s^2, usually 0")
	private float zGravity;
	@Info("Always 0")
	private float unknownFloatOne;
	@Info("Retention of T-pose. High values 'disable' physics. 0 stops the bone from 'bouncing'")
	private float poseSnapping;
	@Info("Level of movement allowed. Mostly noticeable when jumping from heights.")
	private float coneOfMotion;
	@Info("Higher values = more tension. Like a coiled doorstop.")
	private float tensionDamp;
	@Info("Always 100")
	private float unknownFloatTwo;
	@Info("Always 0")
	private float unknownFloatThree;
	@Info("Always 0.1")
	private float unknownFloatFour;
	@Info("Usually a float between 0 and 1")
	private float windMultiplier;
	private int lod;

	private List<CtcBone> bones = new LinkedList<>();

	public CtcChain(byte[] raw) {
		ByteBuffer buffer = ByteBuffer.wrap(raw).order(ByteOrder.LITTLE_ENDIAN);
		chainLength = buffer.getInt();
		collision = buffer.get();
		weightiness = buffer.get();
		for (int i = 0; i < unknownByteSet.length; i++) {
			unknownByteSet[i] = buffer.get();
		}
		xGravity = buffer.getFloat();
		yGravity = buffer.getFloat();
		zGravity = buffer.getFloat();
		unknownFloatOne = buffer.getFloat();
		poseSnapping = buffer.getFloat();
		coneOfMotion = buffer.getFloat();
		tensionDamp = buffer.getFloat();
		unknownFloatTwo = buffer.getFloat();
		unknownFloatThree = buffer.getFloat();
		unknownFloatFour = buffer.getFloat();
		windMultiplier = buffer.getFloat();
		lod = buffer.getInt();
	}

	public byte[] getBytes() {
		ByteBuffer buffer = ByteBuffer.allocate(Ctc.CHAIN_LENGTH).order(ByteOrder.LITTLE_ENDIAN);
		buffer.putInt(chainLength);
		buffer.put(collision);
		buffer.put(weightiness);
		for (int i = 0; i < unknownByteSet.length; i++) {
			buffer.put(unknownByteSet[i]);
		}
		buffer.putFloat(xGravity);
		buffer.putFloat(yGravity);
		buffer.putFloat(zGravity);
		buffer.putFloat(unknownFloatOne);
		buffer.putFloat(poseSnapping);
		buffer.putFloat(coneOfMotion);
		buffer.putFloat(tensionDamp);
		buffer.putFloat(unknownFloatTwo);
		buffer.putFloat(unknownFloatThree);
		buffer.putFloat(unknownFloatFour);
		buffer.putFloat(windMultiplier);
		buffer.putInt(lod);
		return buffer.array();
	}

	public int getChainLength() {
		return chainLength;
	}

	public byte getCollision() {
		return collision;
	}

	public byte getWeightiness() {
		return weightiness;
	}

	public byte[] getUnknownByteSet() {
		return unknownByteSet;
	}

	public float getXGravity() {
		return xGravity;
	}

	public float getYGravity() {
		return yGravity;
	}

	public float getZGravity() {
		return zGravity;
	}

	public float getUnknownFloatOne() {
		return unknownFloatOne;
	}

	public float getPoseSnapping() {
		return poseSnapping;
	}

	public float getConeOfMotion() {
		return coneOfMotion;
	}

	public float getTensionDamp() {
		return tensionDamp;
	}

	public float getUnknownFloatTwo() {
		return unknownFloatTwo;
	}

	public float getUnknownFloatThree() {
		return unknownFloatThree;
	}

	public float getUnknownFloatFour() {
		return unknownFloatFour;
	}

	public float getWindMultiplier() {
		return windMultiplier;
	}

	public int getLod() {
		return lod;
	}

	public List<CtcBone> getBones() {
		return bones;
	}

	public void setChainLength(int chainLength) {
		this.chainLength = chainLength;
	}

	public void setCollision(byte collision) {
		this.collision = collision;
	}

	public void setWeightiness(byte weightiness) {
		this.weightiness = weightiness;
	}

	public void setUnknownByteSet(byte[] unknownByteSet) {
		this.unknownByteSet = unknownByteSet;
	}

	public void setXGravity(float xGravity) {
		this.xGravity = xGravity;
	}

	public void setYGravity(float yGravity) {
		this.yGravity = yGravity;
	}

	public void setZGravity(float zGravity) {
		this.zGravity = zGravity;
	}

	public void setUnknownFloatOne(float unknownFloatOne) {
		this.unknownFloatOne = unknownFloatOne;
	}

	public void setPoseSnapping(float poseSnapping) {
		this.poseSnapping = poseSnapping;
	}

	public void setConeOfMotion(float coneOfMotion) {
		this.coneOfMotion = coneOfMotion;
	}

	public void setTensionDamp(float tensionDamp) {
		this.tensionDamp = tensionDamp;
	}

	public void setUnknownFloatTwo(float unknownFloatTwo) {
		this.unknownFloatTwo = unknownFloatTwo;
	}

	public void setUnknownFloatThree(float unknownFloatThree) {
		this.unknownFloatThree = unknownFloatThree;
	}

	public void setUnknownFloatFour(float unknownFloatFour) {
		this.unknownFloatFour = unknownFloatFour;
	}

	public void setWindMultiplier(float windMultiplier) {
		this.windMultiplier = windMultiplier;
	}

	public void setLod(int lod) {
		this.lod = lod;
	}

	public void setBones(List<CtcBone> bones) {
		this.bones = bones;
	}

	@Override
	public String toString() {
		return "CtcChain [chainLength=" + chainLength + ", collision=" + collision + ", weightiness=" + weightiness
				+ ", xGravity=" + xGravity + ", yGravity=" + yGravity + ", zGravity=" + zGravity + ", poseSnapping="
				+ poseSnapping + ", coneOfMotion=" + coneOfMotion + ", tensionDamp=" + tensionDamp + ", windMultiplier="
				+ windMultiplier + "]";
	}
}
