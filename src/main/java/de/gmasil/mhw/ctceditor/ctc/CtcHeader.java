package de.gmasil.mhw.ctceditor.ctc;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class CtcHeader implements Serializable {
	private String fileType;
	private int[] unknownConstantIntSet = new int[3];
	private int boneChainCount;
	private int boneCount;
	private int unknownConstantInt;
	@Info("Keep 0.16666. Once every frame @ 60fps")
	private float updateTicks;
	@Info("Retention of the initial pose. Typically 1.0")
	private float poseSnapping;
	@Info("0 = springy, 1 = damp. Don't exceed 1.0")
	private float chainDamping;
	@Info("Sensitivity to movement")
	private float reactionSpeed;
	@Info("Can be negative. -1.0 to 1.0")
	private float gravityMult;
	@Info("Power of average wind. 0 = Stiff")
	private float windMultMid;
	@Info("Power of weak winds. 0 = Stiff")
	private float windMultLow;
	@Info("Power of heavy winds. 0 = Stiff")
	private float windMultHigh;
	private float[] unknownFloatSet = new float[3];
	private byte[] unknownByteSet = new byte[8];

	public CtcHeader(byte[] raw) {
		ByteBuffer buffer = ByteBuffer.wrap(raw).order(ByteOrder.LITTLE_ENDIAN);
		char[] chars = new char[3];
		chars[0] = (char) buffer.get();
		chars[1] = (char) buffer.get();
		chars[2] = (char) buffer.get();
		buffer.get();
		fileType = new String(chars);
		if (!fileType.equals("CTC")) {
			throw new IllegalStateException("File header does not look like a CTC file");
		}
		unknownConstantIntSet[0] = buffer.getInt();
		unknownConstantIntSet[1] = buffer.getInt();
		unknownConstantIntSet[2] = buffer.getInt();
		boneChainCount = buffer.getInt();
		boneCount = buffer.getInt();
		unknownConstantInt = buffer.getInt();
		updateTicks = buffer.getFloat();
		poseSnapping = buffer.getFloat();
		chainDamping = buffer.getFloat();
		reactionSpeed = buffer.getFloat();
		gravityMult = buffer.getFloat();
		windMultMid = buffer.getFloat();
		windMultLow = buffer.getFloat();
		windMultHigh = buffer.getFloat();
		for (int i = 0; i < 3; i++) {
			unknownFloatSet[i] = buffer.getFloat();
		}
		for (int i = 0; i < 8; i++) {
			unknownByteSet[i] = buffer.get();
		}
	}

	public byte[] getBytes() {
		ByteBuffer buffer = ByteBuffer.allocate(Ctc.HEADER_LENGTH).order(ByteOrder.LITTLE_ENDIAN);
		buffer.put((byte) fileType.charAt(0));
		buffer.put((byte) fileType.charAt(1));
		buffer.put((byte) fileType.charAt(2));
		buffer.put((byte) 0);
		buffer.putInt(unknownConstantIntSet[0]);
		buffer.putInt(unknownConstantIntSet[1]);
		buffer.putInt(unknownConstantIntSet[2]);
		buffer.putInt(boneChainCount);
		buffer.putInt(boneCount);
		buffer.putInt(unknownConstantInt);
		buffer.putFloat(updateTicks);
		buffer.putFloat(poseSnapping);
		buffer.putFloat(chainDamping);
		buffer.putFloat(reactionSpeed);
		buffer.putFloat(gravityMult);
		buffer.putFloat(windMultMid);
		buffer.putFloat(windMultLow);
		buffer.putFloat(windMultHigh);
		for (int i = 0; i < 3; i++) {
			buffer.putFloat(unknownFloatSet[i]);
		}
		for (int i = 0; i < 8; i++) {
			buffer.put(unknownByteSet[i]);
		}
		return buffer.array();
	}

	public String getFileType() {
		return fileType;
	}

	public int[] getUnknownConstantIntSet() {
		return unknownConstantIntSet;
	}

	public int getBoneChainCount() {
		return boneChainCount;
	}

	public int getBoneCount() {
		return boneCount;
	}

	public int getUnknownConstantInt() {
		return unknownConstantInt;
	}

	public float getUpdateTicks() {
		return updateTicks;
	}

	public float getPoseSnapping() {
		return poseSnapping;
	}

	public float getChainDamping() {
		return chainDamping;
	}

	public float getReactionSpeed() {
		return reactionSpeed;
	}

	public float getGravityMult() {
		return gravityMult;
	}

	public float getWindMultMid() {
		return windMultMid;
	}

	public float getWindMultLow() {
		return windMultLow;
	}

	public float getWindMultHigh() {
		return windMultHigh;
	}

	public float[] getUnknownFloatSet() {
		return unknownFloatSet;
	}

	public byte[] getUnknownByteSet() {
		return unknownByteSet;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public void setUnknownConstantIntSet(int[] unknownConstantIntSet) {
		this.unknownConstantIntSet = unknownConstantIntSet;
	}

	public void setBoneChainCount(int boneChainCount) {
		this.boneChainCount = boneChainCount;
	}

	public void setBoneCount(int boneCount) {
		this.boneCount = boneCount;
	}

	public void setUnknownConstantInt(int unknownConstantInt) {
		this.unknownConstantInt = unknownConstantInt;
	}

	public void setUpdateTicks(float updateTicks) {
		this.updateTicks = updateTicks;
	}

	public void setPoseSnapping(float poseSnapping) {
		this.poseSnapping = poseSnapping;
	}

	public void setChainDamping(float chainDamping) {
		this.chainDamping = chainDamping;
	}

	public void setReactionSpeed(float reactionSpeed) {
		this.reactionSpeed = reactionSpeed;
	}

	public void setGravityMult(float gravityMult) {
		this.gravityMult = gravityMult;
	}

	public void setWindMultMid(float windMultMid) {
		this.windMultMid = windMultMid;
	}

	public void setWindMultLow(float windMultLow) {
		this.windMultLow = windMultLow;
	}

	public void setWindMultHigh(float windMultHigh) {
		this.windMultHigh = windMultHigh;
	}

	public void setUnknownFloatSet(float[] unknownFloatSet) {
		this.unknownFloatSet = unknownFloatSet;
	}

	public void setUnknownByteSet(byte[] unknownByteSet) {
		this.unknownByteSet = unknownByteSet;
	}

	@Override
	public String toString() {
		return "CtcHeader [fileType=" + fileType + ", boneChainCount=" + boneChainCount + ", boneCount=" + boneCount
				+ ", updateTicks=" + updateTicks + ", poseSnapping=" + poseSnapping + ", chainDamping=" + chainDamping
				+ ", reactionSpeed=" + reactionSpeed + ", gravityMult=" + gravityMult + ", windMultMid=" + windMultMid
				+ ", windMultLow=" + windMultLow + ", windMultHigh=" + windMultHigh + "]";
	}
}
