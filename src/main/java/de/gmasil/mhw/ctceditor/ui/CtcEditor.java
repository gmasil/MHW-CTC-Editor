package de.gmasil.mhw.ctceditor.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.io.File;
import java.lang.invoke.MethodHandles;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.gmasil.mhw.ctceditor.ctc.CtcIO;

public class CtcEditor extends JFrame implements FileOpenedListener, MenuListener {
	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private CtcTreeViewer treeViewer;
	private Config config = new Config();
	private WindowsFileChooser windowsFileChooser = new WindowsFileChooser(config, this);

	public CtcEditor() {
		this.setTitle("MHW CTC Editor");
		this.setSize(1000, 620);
		this.setResizable(true);
		this.setLocation(50, 50);
		this.setLocationByPlatform(true);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());

		// menu bar
		setJMenuBar(new EditorMenuBar(this));

		// CTC tree left
		treeViewer = new CtcTreeViewer(this, this);
		JScrollPane scrollTree = new JScrollPane(treeViewer);
		scrollTree.setMinimumSize(new Dimension(200, 0));
		scrollTree.setPreferredSize(new Dimension(500, 500));

		// editor panel
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		JScrollPane scrollMainPanel = new JScrollPane(mainPanel);
		scrollMainPanel.setMinimumSize(new Dimension(200, 0));
		scrollMainPanel.setPreferredSize(new Dimension(500, 500));

		// split
		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollTree, scrollMainPanel);
		getContentPane().add(split, BorderLayout.CENTER);

		this.setVisible(true);
	}

	@Override
	public void onFileOpened(File file) {
		try {
			treeViewer.setCtc(CtcIO.readFile(file));
			JOptionPane.showMessageDialog(CtcEditor.this, "CTC file loaded.", "CTC Read", JOptionPane.OK_OPTION);
		} catch (Exception e) {
			LOG.warn("Error while reading CTC file", e);
			JOptionPane.showMessageDialog(CtcEditor.this, e.getMessage(), "Error while reading CTC file",
					JOptionPane.OK_OPTION);
		}
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

	@Override
	public void menuOpen() {
		windowsFileChooser.openDialog();
	}

	@Override
	public void menuClose() {
		treeViewer.setCtc(null);
	}
}
