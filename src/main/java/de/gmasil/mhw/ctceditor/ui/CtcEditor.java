package de.gmasil.mhw.ctceditor.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.lang.invoke.MethodHandles;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.javafx.application.PlatformImpl;

import de.gmasil.mhw.ctceditor.ctc.Ctc;
import de.gmasil.mhw.ctceditor.ctc.CtcBone;
import de.gmasil.mhw.ctceditor.ctc.CtcChain;
import de.gmasil.mhw.ctceditor.ctc.CtcIO;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class CtcEditor extends JFrame {
	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private File lastOpenedFile = null;
	private Ctc currentCtc = null;
	private DefaultMutableTreeNode rootNode;
	private JTree tree;
	private Config config = new Config();

	public CtcEditor() {
		this.setTitle("MHW CTC Editor");
		this.setSize(1000, 620);
		this.setResizable(true);
		this.setLocation(50, 50);
		this.setLocationByPlatform(true);

		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		getContentPane().setLayout(new BorderLayout());

		JMenuBar menuBar = new JMenuBar();
		JMenu menuFile = new JMenu("File");
		menuBar.add(menuFile);
		JMenuItem menuOpen = new JMenuItem("Open");
		menuOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		menuOpen.addActionListener(event -> PlatformImpl.startup(() -> {
			FileChooser d = new FileChooser();
			d.getExtensionFilters().add(new ExtensionFilter("CTC files (*.ctc)", "*.ctc"));
			d.getExtensionFilters().add(new ExtensionFilter("All files", "*.*"));
			if (lastOpenedFile != null) {
				File initialDirectory;
				if (lastOpenedFile.isFile()) {
					initialDirectory = lastOpenedFile.getParentFile();
				} else {
					initialDirectory = lastOpenedFile;
				}
				if (initialDirectory.exists()) {
					d.setInitialDirectory(initialDirectory);
				}
			}
			File selectedFile = d.showOpenDialog(null);
			if (selectedFile != null) {
				lastOpenedFile = selectedFile;
				config.setProperty(Config.LAST_OPENED_FILE, lastOpenedFile.getAbsolutePath());
				config.save();
				loadFile(selectedFile);
			}
		}));
		menuFile.add(menuOpen);
		JMenuItem menuClose = new JMenuItem("Close");
		menuClose.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK));
		menuClose.addActionListener(event -> {
			currentCtc = null;
			refreshTree();
		});
		menuFile.add(menuClose);
		JMenuItem menuRefresh = new JMenuItem("Refresh");
		menuRefresh.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));
		menuRefresh.addActionListener(event -> {
			refreshTree();
		});
		menuFile.add(menuRefresh);
		setJMenuBar(menuBar);

		rootNode = new DefaultMutableTreeNode("");
		tree = new JTree(rootNode);
		JScrollPane scrollTree = new JScrollPane(tree);
		scrollTree.setMinimumSize(new Dimension(200, 0));
		scrollTree.setPreferredSize(new Dimension(500, 500));
		tree.addTreeSelectionListener(e -> {
			System.out.println("New Selection Event:");
			if (tree.getSelectionPaths() != null) {
				for (TreePath selectionPath : tree.getSelectionPaths()) {
					System.out.println("\t- " + selectionPath);
					int pathCount = selectionPath.getPathCount();
					System.out.println(pathCount);
				}
			}
		});

		JTextArea textArea = new JTextArea();
		JScrollPane scrollTextArea = new JScrollPane(textArea);
		scrollTextArea.setMinimumSize(new Dimension(200, 0));
		scrollTextArea.setPreferredSize(new Dimension(500, 500));

		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollTree, textArea);
		getContentPane().add(split, BorderLayout.CENTER);

		handleConfig();

		tree.setDropTarget(new DropTarget() {
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
								loadFile(transferFile);
							} else {
								JOptionPane.showMessageDialog(CtcEditor.this, "You can only drop files here.",
										"Drag and Drop Error", JOptionPane.OK_OPTION);
							}
						} else {
							JOptionPane.showMessageDialog(CtcEditor.this, "Cannot drop multiple files at once.",
									"Drag and Drop Error", JOptionPane.OK_OPTION);
						}
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});

		refreshTree();
		this.setVisible(true);
	}

	public void loadFile(File file) {
		try {
			currentCtc = CtcIO.readFile(file);
			refreshTree();
			JOptionPane.showMessageDialog(CtcEditor.this, "CTC file loaded.", "CTC Read", JOptionPane.OK_OPTION);
		} catch (Exception e) {
			LOG.warn("Error while reading CTC file", e);
			JOptionPane.showMessageDialog(CtcEditor.this, e.getMessage(), "Error while reading CTC file",
					JOptionPane.OK_OPTION);
		}
	}

	public void handleConfig() {
		lastOpenedFile = new File(config.getProperty(Config.LAST_OPENED_FILE));
	}

	int x = 1;

	public void refreshTree() {
		if (currentCtc != null) {
			rootNode.removeAllChildren();
			rootNode.setUserObject("CTC File " + x++);
			rootNode.add(new DefaultMutableTreeNode(currentCtc.getHeader()));
			DefaultMutableTreeNode chainsNode = new DefaultMutableTreeNode("Bone Chains");
			rootNode.add(chainsNode);
			for (CtcChain chain : currentCtc.getChains()) {
				DefaultMutableTreeNode chainNode = new DefaultMutableTreeNode(chain);
				chainsNode.add(chainNode);
				for (CtcBone bone : chain.getBones()) {
					DefaultMutableTreeNode boneNode = new DefaultMutableTreeNode(bone);
					chainNode.add(boneNode);
				}
			}
			DefaultMutableTreeNode bonesNode = new DefaultMutableTreeNode("Bones");
			rootNode.add(bonesNode);
			for (CtcBone bone : currentCtc.getBones()) {
				DefaultMutableTreeNode boneNode = new DefaultMutableTreeNode(bone);
				bonesNode.add(boneNode);
			}
		} else {
			rootNode.setUserObject("Drag a CTC file into this window or use the open menu");
			rootNode.removeAllChildren();
		}
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		model.reload();
		tree.expandRow(0);
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception e) {
				LOG.info("Could not set system look and feel");
			}
			new CtcEditor();
		});
	}
}
