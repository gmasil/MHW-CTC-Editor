package de.gmasil.mhw.ctceditor.ctc;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class CtcBone implements Serializable {
	@Info("Negative X axis maximum in radians")
	private float negMaxX;
	@Info("Negative Y axis maximum in radians")
	private float negMaxY;
	@Info("Negative Z axis maximum in radians")
	private float negMaxZ;
	@Unknown
	@Info("Something to do with world space")
	private float worldX;
	@Info("Positive Z axis maximum in radians")
	private float maxZ;
	@Info("Positive Y axis maximum in radians")
	private float maxY;
	@Info("Positive X axis maximum in radians")
	private float maxX;
	@Unknown
	@Info("Something to do with world space")
	private float worldY;
	@Unknown
	@Info("Something along X(?)")
	private float someX;
	@Unknown
	@Info("Something along Y(?)")
	private float someY;
	@Unknown
	@Info("Something along Z(?)")
	private float someZ;
	@Unknown
	@Info("Something to do with world space")
	private float worldZ;
	private float unknown1;
	private float unknown2;
	private float unknown3;
	@Info("Usually 1")
	private float unknown4;
	private byte[] unknownByteSetOne = new byte[2];
	@Info("is parent of a chain")
	private byte isParent;
	private byte[] unknownByteSetTwo = new byte[5];
	private int boneFunctionID;
	private byte[] unknownByteSetThree = new byte[4];
	private float[] unknownFloatSet = new float[4];
	private byte[] unknownByteSetFour = new byte[16];

	public CtcBone(byte[] raw) {
		ByteBuffer buffer = ByteBuffer.wrap(raw).order(ByteOrder.LITTLE_ENDIAN);
		negMaxX = buffer.getFloat();
		negMaxY = buffer.getFloat();
		negMaxZ = buffer.getFloat();
		worldX = buffer.getFloat();
		maxZ = buffer.getFloat();
		maxY = buffer.getFloat();
		maxX = buffer.getFloat();
		worldY = buffer.getFloat();
		someX = buffer.getFloat();
		someY = buffer.getFloat();
		someZ = buffer.getFloat();
		worldZ = buffer.getFloat();
		unknown1 = buffer.getFloat();
		unknown2 = buffer.getFloat();
		unknown3 = buffer.getFloat();
		unknown4 = buffer.getFloat();
		for (int i = 0; i < unknownByteSetOne.length; i++) {
			unknownByteSetOne[i] = buffer.get();
		}
		isParent = buffer.get();
		for (int i = 0; i < unknownByteSetTwo.length; i++) {
			unknownByteSetTwo[i] = buffer.get();
		}
		boneFunctionID = buffer.getInt();
		for (int i = 0; i < unknownByteSetThree.length; i++) {
			unknownByteSetThree[i] = buffer.get();
		}
		for (int i = 0; i < unknownFloatSet.length; i++) {
			unknownFloatSet[i] = buffer.getFloat();
		}
		for (int i = 0; i < unknownByteSetFour.length; i++) {
			unknownByteSetFour[i] = buffer.get();
		}
	}

	public byte[] getBytes() {
		ByteBuffer buffer = ByteBuffer.allocate(Ctc.BONE_LENGTH).order(ByteOrder.LITTLE_ENDIAN);
		buffer.putFloat(negMaxX);
		buffer.putFloat(negMaxY);
		buffer.putFloat(negMaxZ);
		buffer.putFloat(worldX);
		buffer.putFloat(maxZ);
		buffer.putFloat(maxY);
		buffer.putFloat(maxX);
		buffer.putFloat(worldY);
		buffer.putFloat(someX);
		buffer.putFloat(someY);
		buffer.putFloat(someZ);
		buffer.putFloat(worldZ);
		buffer.putFloat(unknown1);
		buffer.putFloat(unknown2);
		buffer.putFloat(unknown3);
		buffer.putFloat(unknown4);
		for (int i = 0; i < unknownByteSetOne.length; i++) {
			buffer.put(unknownByteSetOne[i]);
		}
		buffer.put(isParent);
		for (int i = 0; i < unknownByteSetTwo.length; i++) {
			buffer.put(unknownByteSetTwo[i]);
		}
		buffer.putInt(boneFunctionID);
		for (int i = 0; i < unknownByteSetThree.length; i++) {
			buffer.put(unknownByteSetThree[i]);
		}
		for (int i = 0; i < unknownFloatSet.length; i++) {
			buffer.putFloat(unknownFloatSet[i]);
		}
		for (int i = 0; i < unknownByteSetFour.length; i++) {
			buffer.put(unknownByteSetFour[i]);
		}
		return buffer.array();
	}

	public float getNegMaxX() {
		return negMaxX;
	}

	public void setNegMaxX(float negMaxX) {
		this.negMaxX = negMaxX;
	}

	public float getNegMaxY() {
		return negMaxY;
	}

	public void setNegMaxY(float negMaxY) {
		this.negMaxY = negMaxY;
	}

	public float getNegMaxZ() {
		return negMaxZ;
	}

	public void setNegMaxZ(float negMaxZ) {
		this.negMaxZ = negMaxZ;
	}

	public float getWorldX() {
		return worldX;
	}

	public void setWorldX(float worldX) {
		this.worldX = worldX;
	}

	public float getMaxZ() {
		return maxZ;
	}

	public void setMaxZ(float maxZ) {
		this.maxZ = maxZ;
	}

	public float getMaxY() {
		return maxY;
	}

	public void setMaxY(float maxY) {
		this.maxY = maxY;
	}

	public float getMaxX() {
		return maxX;
	}

	public void setMaxX(float maxX) {
		this.maxX = maxX;
	}

	public float getWorldY() {
		return worldY;
	}

	public void setWorldY(float worldY) {
		this.worldY = worldY;
	}

	public float getSomeX() {
		return someX;
	}

	public void setSomeX(float someX) {
		this.someX = someX;
	}

	public float getSomeY() {
		return someY;
	}

	public void setSomeY(float someY) {
		this.someY = someY;
	}

	public float getSomeZ() {
		return someZ;
	}

	public void setSomeZ(float someZ) {
		this.someZ = someZ;
	}

	public float getWorldZ() {
		return worldZ;
	}

	public void setWorldZ(float worldZ) {
		this.worldZ = worldZ;
	}

	public float getUnknown1() {
		return unknown1;
	}

	public void setUnknown1(float unknown1) {
		this.unknown1 = unknown1;
	}

	public float getUnknown2() {
		return unknown2;
	}

	public void setUnknown2(float unknown2) {
		this.unknown2 = unknown2;
	}

	public float getUnknown3() {
		return unknown3;
	}

	public void setUnknown3(float unknown3) {
		this.unknown3 = unknown3;
	}

	public float getUnknown4() {
		return unknown4;
	}

	public void setUnknown4(float unknown4) {
		this.unknown4 = unknown4;
	}

	public byte[] getUnknownByteSetOne() {
		return unknownByteSetOne;
	}

	public void setUnknownByteSetOne(byte[] unknownByteSetOne) {
		this.unknownByteSetOne = unknownByteSetOne;
	}

	public byte getIsParent() {
		return isParent;
	}

	public void setIsParent(byte isParent) {
		this.isParent = isParent;
	}

	public byte[] getUnknownByteSetTwo() {
		return unknownByteSetTwo;
	}

	public void setUnknownByteSetTwo(byte[] unknownByteSetTwo) {
		this.unknownByteSetTwo = unknownByteSetTwo;
	}

	public int getBoneFunctionID() {
		return boneFunctionID;
	}

	public void setBoneFunctionID(int boneFunctionID) {
		this.boneFunctionID = boneFunctionID;
	}

	public byte[] getUnknownByteSetThree() {
		return unknownByteSetThree;
	}

	public void setUnknownByteSetThree(byte[] unknownByteSetThree) {
		this.unknownByteSetThree = unknownByteSetThree;
	}

	public float[] getUnknownFloatSet() {
		return unknownFloatSet;
	}

	public void setUnknownFloatSet(float[] unknownFloatSet) {
		this.unknownFloatSet = unknownFloatSet;
	}

	public byte[] getUnknownByteSetFour() {
		return unknownByteSetFour;
	}

	public void setUnknownByteSetFour(byte[] unknownByteSetFour) {
		this.unknownByteSetFour = unknownByteSetFour;
	}

	@Override
	public String toString() {
		return "CtcBone [negMaxX=" + negMaxX + ", negMaxY=" + negMaxY + ", negMaxZ=" + negMaxZ + ", worldX=" + worldX
				+ ", maxZ=" + maxZ + ", maxY=" + maxY + ", maxX=" + maxX + ", worldY=" + worldY + ", someX=" + someX
				+ ", someY=" + someY + ", someZ=" + someZ + ", worldZ=" + worldZ + ", unknown1=" + unknown1
				+ ", unknown2=" + unknown2 + ", unknown3=" + unknown3 + ", unknown4=" + unknown4 + ", isParent="
				+ isParent + ", boneFunctionID=" + boneFunctionID + "]";
	}
}
