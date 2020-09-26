package de.gmasil.mhw.ctceditor.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.io.File;
import java.lang.invoke.MethodHandles;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.gmasil.mhw.ctceditor.ctc.CtcIO;
import de.gmasil.mhw.ctceditor.logging.SwingAppender;

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
		scrollTree.setMinimumSize(new Dimension(50, 0));
		scrollTree.setPreferredSize(new Dimension(500, 500));

		// editor panel
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		JScrollPane scrollMainPanel = new JScrollPane(mainPanel);
		scrollMainPanel.setMinimumSize(new Dimension(50, 0));
		scrollMainPanel.setPreferredSize(new Dimension(500, 500));

		// console
		JTextArea console = new JTextArea();
		console.setEditable(false);
		JScrollPane scrollConsole = new JScrollPane(console);
		scrollConsole.setMinimumSize(new Dimension(0, 50));
		scrollConsole.setPreferredSize(new Dimension(200, 200));
		// register console to logger
		SwingAppender.setConsole(console);

		// split
		JSplitPane splitTreeAndMain = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollTree, scrollMainPanel);
		splitTreeAndMain.setMinimumSize(new Dimension(0, 50));
		splitTreeAndMain.setPreferredSize(new Dimension(500, 500));
		splitTreeAndMain.setResizeWeight(0.0f);
		JSplitPane splitTopBottom = new JSplitPane(JSplitPane.VERTICAL_SPLIT, splitTreeAndMain, scrollConsole);
		splitTopBottom.setResizeWeight(1.0f);
		getContentPane().add(splitTopBottom, BorderLayout.CENTER);

		this.setVisible(true);

		// set default devider locations
		splitTreeAndMain.setDividerLocation(0.3D);
		splitTopBottom.setDividerLocation(0.7D);
	}

	@Override
	public void onFileOpened(File file) {
		try {
			treeViewer.setCtc(CtcIO.readFile(file));
			LOG.info("CTC file loaded successfully: " + file.getAbsolutePath());
		} catch (Exception e) {
			LOG.warn("Error while reading CTC file", e);
		}
	}

	@Override
	public void menuOpen() {
		windowsFileChooser.openDialog();
	}

	@Override
	public void menuClose() {
		treeViewer.setCtc(null);
	}

	public static void main(String[] args) {
		LOG.debug("MHW CTC Editor is starting");
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
