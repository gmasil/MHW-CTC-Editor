package de.gmasil.mhw.ctceditor.ui;

import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.lang.invoke.MethodHandles;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreePath;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.gmasil.mhw.ctceditor.ctc.Ctc;
import de.gmasil.mhw.ctceditor.ctc.CtcBone;
import de.gmasil.mhw.ctceditor.ctc.CtcChain;
import de.gmasil.mhw.ctceditor.ctc.CtcHeader;
import de.gmasil.mhw.ctceditor.ui.api.AllowSelectionCallback;
import de.gmasil.mhw.ctceditor.ui.api.CtcChangedCallback;
import de.gmasil.mhw.ctceditor.ui.api.FileOpenedListener;
import de.gmasil.mhw.ctceditor.ui.api.SelectionListener;

public class CtcTreeViewer extends JTree implements CtcChangedCallback {
	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	public static final String DEFAULT_ROOT_TEXT = "Drag a CTC file into this window or use the open menu";

	private DefaultMutableTreeNode rootNode;
	private Ctc ctc = null;
	private boolean isCtcChanged = false;

	public CtcTreeViewer(Component parent, FileOpenedListener fileListener, SelectionListener selectionListener,
			AllowSelectionCallback allowSellectionCallback) {
		this(parent, fileListener, selectionListener, allowSellectionCallback,
				new DefaultMutableTreeNode(DEFAULT_ROOT_TEXT));
	}

	public CtcTreeViewer(Component parent, FileOpenedListener fileListener, SelectionListener selectionListener,
			AllowSelectionCallback allowSellectionCallback, DefaultMutableTreeNode rootNode) {
		super(rootNode);
		this.rootNode = rootNode;
		setSelectionModel(new VetoableTreeSelectionModel(allowSellectionCallback));
		setupSelectionListener(selectionListener);
		setupDragAndDrop(parent, fileListener);
		refreshTree();
	}

	private void setupSelectionListener(SelectionListener selectionListener) {
		addTreeSelectionListener(e -> {
			if (getSelectionPaths() != null) {
				Class<?> clazz = getSelectedClass();
				if (clazz != null) {
					performSelection(selectionListener, clazz);
					return;
				}
			}
			selectionListener.onIllegalSelection();
		});
	}

	private void performSelection(SelectionListener selectionListener, Class<?> clazz) {
		if (clazz == CtcHeader.class) {
			CtcHeader header = (CtcHeader) ((DefaultMutableTreeNode) getSelectionPath().getLastPathComponent())
					.getUserObject();
			selectionListener.onHeaderSelected(header);
		} else if (clazz == CtcChain.class) {
			Set<CtcChain> list = new HashSet<>();
			for (TreePath selectionPath : getSelectionPaths()) {
				list.add((CtcChain) ((DefaultMutableTreeNode) selectionPath.getLastPathComponent()).getUserObject());
			}
			selectionListener.onChainSelected(list);
		} else if (clazz == CtcBone.class) {
			Set<CtcBone> list = new HashSet<>();
			for (TreePath selectionPath : getSelectionPaths()) {
				list.add((CtcBone) ((DefaultMutableTreeNode) selectionPath.getLastPathComponent()).getUserObject());
			}
			selectionListener.onBoneSelected(list);
		} else if (clazz == String.class) {
			selectionListener.onTopicSelected();
		} else {
			selectionListener.onIllegalSelection();
		}
	}

	private Class<?> getSelectedClass() {
		Class<?> clazz = null;
		if (getSelectionCount() > 0) {
			for (TreePath selectionPath : getSelectionPaths()) {
				Object lastPathComponent = selectionPath.getLastPathComponent();
				if (lastPathComponent instanceof DefaultMutableTreeNode) {
					Object userObject = ((DefaultMutableTreeNode) lastPathComponent).getUserObject();
					if (clazz != null && userObject.getClass() != clazz) {
						return null;
					}
					clazz = userObject.getClass();
				} else {
					return null;
				}
			}
		}
		return clazz;
	}

	private void setupDragAndDrop(Component parent, FileOpenedListener fileListener) {
		setDropTarget(new DropTarget() {
			@Override
			public synchronized void drop(DropTargetDropEvent evt) {
				try {
					evt.acceptDrop(DnDConstants.ACTION_COPY);
					Object transferData = evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
					if (transferData instanceof List<?>) {
						List<?> transferList = (List<?>) transferData;
						if (transferList.size() == 1) {
							Object transferObject = transferList.get(0);
							if (transferObject instanceof File) {
								File transferFile = (File) transferObject;
								fileListener.onFileOpened(transferFile);
							} else {
								JOptionPane.showMessageDialog(parent, "You can only drop files here.",
										"Drag and Drop Error", JOptionPane.OK_OPTION);
							}
						} else {
							JOptionPane.showMessageDialog(parent, "Cannot drop multiple files at once.",
									"Drag and Drop Error", JOptionPane.OK_OPTION);
						}
					}
				} catch (Exception e) {
					LOG.warn("Error during drag and drop", e);
				}
			}
		});
	}

	public void setCtc(Ctc ctc) {
		this.ctc = ctc;
		refreshTree();
	}

	public Ctc getCtc() {
		return ctc;
	}

	public void refreshTree() {
		if (ctc != null) {
			rootNode.removeAllChildren();
			rootNode.setUserObject("CTC File");
			rootNode.add(new DefaultMutableTreeNode(ctc.getHeader()));
			DefaultMutableTreeNode chainsNode = new DefaultMutableTreeNode("Bone Chains");
			rootNode.add(chainsNode);
			for (CtcChain chain : ctc.getChains()) {
				DefaultMutableTreeNode chainNode = new DefaultMutableTreeNode(chain);
				chainsNode.add(chainNode);
				for (CtcBone bone : chain.getBones()) {
					DefaultMutableTreeNode boneNode = new DefaultMutableTreeNode(bone);
					chainNode.add(boneNode);
				}
			}
			DefaultMutableTreeNode bonesNode = new DefaultMutableTreeNode("Bones");
			rootNode.add(bonesNode);
			for (CtcBone bone : ctc.getBones()) {
				DefaultMutableTreeNode boneNode = new DefaultMutableTreeNode(bone);
				bonesNode.add(boneNode);
			}
		} else {
			rootNode.setUserObject(DEFAULT_ROOT_TEXT);
			rootNode.removeAllChildren();
		}
		DefaultTreeModel model = (DefaultTreeModel) getModel();
		model.reload();
		expandRow(0);
		expandRow(3);
		expandRow(2);
	}

	public static class VetoableTreeSelectionModel extends DefaultTreeSelectionModel {
		private AllowSelectionCallback callback;

		public VetoableTreeSelectionModel(AllowSelectionCallback callback) {
			this.callback = callback;
		}

		@Override
		public void setSelectionPath(TreePath path) {
			if (callback.allowSelectionChange()) {
				super.setSelectionPath(path);
			}
		}
	}

	@Override
	public boolean isCtcChanged() {
		return isCtcChanged;
	}

	@Override
	public void setCtcChanged(boolean changed) {
		isCtcChanged = changed;
	}
}
