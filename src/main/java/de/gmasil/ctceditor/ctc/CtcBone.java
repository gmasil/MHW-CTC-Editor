package de.gmasil.ctceditor.ctc;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class CtcBone {
	private float negMaxX; // Negative X axis maximum in radians
	private float negMaxY; // Negative Y axis maximum in radians
	private float negMaxZ; // Negative Z axis maximum in radians
	private float worldX; // Something to do with world space
	private float maxZ; // Positive Z axis maximum in radians
	private float maxY; // Positive Y axis maximum in radians
	private float maxX; // Positive X axis maximum in radians
	private float worldY; // Something to do with world space
	private float someX; // Something along X(?)
	private float someY; // Something along Y(?)
	private float someZ; // Something along Z(?)
	private float worldZ; // Something to do with world space
	private float unknown1;
	private float unknown2;
	private float unknown3;
	private float unknown4; // Usually 1
	private byte[] unknownByteSetOne = new byte[2];
	private byte isParent; // is parent of a chain
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

	public float getNegMaxY() {
		return negMaxY;
	}

	public float getNegMaxZ() {
		return negMaxZ;
	}

	public float getWorldX() {
		return worldX;
	}

	public float getMaxZ() {
		return maxZ;
	}

	public float getMaxY() {
		return maxY;
	}

	public float getMaxX() {
		return maxX;
	}

	public float getWorldY() {
		return worldY;
	}

	public float getSomeX() {
		return someX;
	}

	public float getSomeY() {
		return someY;
	}

	public float getSomeZ() {
		return someZ;
	}

	public float getWorldZ() {
		return worldZ;
	}

	public float getUnknown1() {
		return unknown1;
	}

	public float getUnknown2() {
		return unknown2;
	}

	public float getUnknown3() {
		return unknown3;
	}

	public float getUnknown4() {
		return unknown4;
	}

	public byte[] getUnknownByteSetOne() {
		return unknownByteSetOne;
	}

	public byte getIsParent() {
		return isParent;
	}

	public byte[] getUnknownByteSetTwo() {
		return unknownByteSetTwo;
	}

	public int getBoneFunctionID() {
		return boneFunctionID;
	}

	public byte[] getUnknownByteSetThree() {
		return unknownByteSetThree;
	}

	public float[] getUnknownFloatSet() {
		return unknownFloatSet;
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
