package de.gmasil.mhw.ctceditor.ui;

import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import de.gmasil.mhw.ctceditor.ctc.Ctc;
import de.gmasil.mhw.ctceditor.ctc.CtcBone;
import de.gmasil.mhw.ctceditor.ctc.CtcChain;
import de.gmasil.mhw.ctceditor.ctc.CtcHeader;
import de.gmasil.mhw.ctceditor.ui.api.FileOpenedListener;
import de.gmasil.mhw.ctceditor.ui.api.SelectionListener;

public class CtcTreeViewer extends JTree {
	public static final String DEFAULT_ROOT_TEXT = "Drag a CTC file into this window or use the open menu";

	private DefaultMutableTreeNode rootNode;
	private Ctc ctc = null;

	public CtcTreeViewer(Component parent, FileOpenedListener fileListener, SelectionListener selectionListener) {
		this(parent, fileListener, selectionListener, new DefaultMutableTreeNode(DEFAULT_ROOT_TEXT));
	}

	public CtcTreeViewer(Component parent, FileOpenedListener fileListener, SelectionListener selectionListener,
			DefaultMutableTreeNode rootNode) {
		super(rootNode);
		this.rootNode = rootNode;
		addTreeSelectionListener(e -> {
			if (getSelectionPaths() != null) {
				Class<?> clazz = null;
				if (getSelectionCount() == 1) {
					Object lastPathComponent = getSelectionPath().getLastPathComponent();
					if (lastPathComponent instanceof DefaultMutableTreeNode) {
						Object userObject = ((DefaultMutableTreeNode) lastPathComponent).getUserObject();
						clazz = userObject.getClass();
					} else {
						selectionListener.onIllegalSelection();
						return;
					}
				} else if (getSelectionCount() > 1) {
					for (TreePath selectionPath : getSelectionPaths()) {
						Object lastPathComponent = selectionPath.getLastPathComponent();
						if (lastPathComponent instanceof DefaultMutableTreeNode) {
							Object userObject = ((DefaultMutableTreeNode) lastPathComponent).getUserObject();
							if (clazz != null) {
								if (userObject.getClass() != clazz) {
									selectionListener.onIllegalSelection();
									return;
								}
							}
							clazz = userObject.getClass();
						} else {
							selectionListener.onIllegalSelection();
							return;
						}
					}
				}
				if (clazz != null) {
					if (clazz == CtcHeader.class) {
						CtcHeader header = (CtcHeader) ((DefaultMutableTreeNode) getSelectionPath()
								.getLastPathComponent()).getUserObject();
						selectionListener.onHeaderSelected(header);
					} else if (clazz == CtcChain.class) {
						Set<CtcChain> list = new HashSet<>();
						for (TreePath selectionPath : getSelectionPaths()) {
							list.add((CtcChain) ((DefaultMutableTreeNode) selectionPath.getLastPathComponent())
									.getUserObject());
						}
						selectionListener.onChainSelected(list);
					} else if (clazz == CtcBone.class) {
						Set<CtcBone> list = new HashSet<>();
						for (TreePath selectionPath : getSelectionPaths()) {
							list.add((CtcBone) ((DefaultMutableTreeNode) selectionPath.getLastPathComponent())
									.getUserObject());
						}
						selectionListener.onBoneSelected(list);
					} else {
						selectionListener.onIllegalSelection();
					}
				} else {
					selectionListener.onIllegalSelection();
				}
			} else {
				selectionListener.onIllegalSelection();
			}
		});
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
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		refreshTree();
	}

	public void setCtc(Ctc ctc) {
		this.ctc = ctc;
		refreshTree();
	}

	public Ctc getCtc() {
		return ctc;
	}

	private void refreshTree() {
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
			rootNode.setUserObject("Drag a CTC file into this window or use the open menu");
			rootNode.removeAllChildren();
		}
		DefaultTreeModel model = (DefaultTreeModel) getModel();
		model.reload();
		expandRow(0);
		expandRow(3);
		expandRow(2);
	}
}
