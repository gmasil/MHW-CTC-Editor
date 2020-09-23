package de.gmasil.mhw.ctceditor.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.sun.javafx.application.PlatformImpl;

import de.gmasil.mhw.ctceditor.ctc.Ctc;
import de.gmasil.mhw.ctceditor.ctc.CtcBone;
import de.gmasil.mhw.ctceditor.ctc.CtcChain;
import de.gmasil.mhw.ctceditor.ctc.CtcIO;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class CtcEditor extends JFrame {
	private File lastOpenedFile = new File(
			"D:\\Steam\\steamapps\\common\\Monster Hunter World\\nativePC\\pl\\f_equip\\pl077_0000\\body\\mod");
	private Ctc currentCtc = null;
	private DefaultMutableTreeNode rootNode;
	private JTree tree;

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
				if (lastOpenedFile.isFile()) {
					d.setInitialDirectory(lastOpenedFile.getParentFile());
				} else {
					d.setInitialDirectory(lastOpenedFile);
				}
			}
			File selectedFile = d.showOpenDialog(null);
			if (selectedFile != null) {
				lastOpenedFile = selectedFile;
				try {
					currentCtc = CtcIO.readFile(selectedFile);
					refreshTree();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}));
		menuFile.add(menuOpen);
		JMenuItem menuClose = new JMenuItem("Close");
		menuClose.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK));
		menuClose.addActionListener(event -> PlatformImpl.startup(() -> {
			currentCtc = null;
			refreshTree();
		}));
		menuFile.add(menuClose);
		setJMenuBar(menuBar);

		rootNode = new DefaultMutableTreeNode("CTC File");
		tree = new JTree(rootNode);
		JScrollPane scrollTree = new JScrollPane(tree);
		scrollTree.setMinimumSize(new Dimension(200, 0));
		scrollTree.setPreferredSize(new Dimension(500, 500));
		tree.addTreeSelectionListener(e -> {
			System.out.println("New Selection Event:");
			for (TreePath selectionPath : tree.getSelectionPaths()) {
				System.out.println("\t- " + selectionPath);
				int pathCount = selectionPath.getPathCount();
				System.out.println(pathCount);
			}
		});

		JTextArea textArea = new JTextArea();
		JScrollPane scrollTextArea = new JScrollPane(textArea);
		scrollTextArea.setMinimumSize(new Dimension(200, 0));
		scrollTextArea.setPreferredSize(new Dimension(500, 500));

		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollTree, textArea);
		getContentPane().add(split, BorderLayout.CENTER);

		this.setVisible(true);
	}

	public void refreshTree() {
		if (currentCtc != null) {
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
			rootNode.removeAllChildren();
		}
		tree.repaint();
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
						| UnsupportedLookAndFeelException ex) {
				}
				new CtcEditor();
			}
		});
	}
}
