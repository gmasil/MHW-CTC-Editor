package de.gmasil.mhw.ctceditor.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.io.File;
import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.gmasil.mhw.ctceditor.ctc.CtcBone;
import de.gmasil.mhw.ctceditor.ctc.CtcChain;
import de.gmasil.mhw.ctceditor.ctc.CtcHeader;
import de.gmasil.mhw.ctceditor.ctc.CtcIO;
import de.gmasil.mhw.ctceditor.logging.SwingAppender;
import de.gmasil.mhw.ctceditor.ui.api.FileOpenedListener;
import de.gmasil.mhw.ctceditor.ui.api.MenuListener;
import de.gmasil.mhw.ctceditor.ui.api.SelectionListener;

public class CtcEditor extends JFrame implements FileOpenedListener, MenuListener, SelectionListener {
	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	private static final int DIVIDER_SIZE = 4;

	private CtcTreeViewer treeViewer;
	private Config config = new Config();
	private WindowsFileChooser windowsFileChooser = new WindowsFileChooser(config, this);
	private JScrollPane scrollConsole;
	private JSplitPane splitTreeAndMain;
	private JSplitPane splitTopBottom;
	private boolean showConsole = config.getShowConsole();
	private boolean showConsoleOnStartup = showConsole;
	private int consoleHeight;
	private JPanel mainPanel;

	public CtcEditor(String... args) {
		this.setTitle("MHW CTC Editor");
		this.setSize(1000, 620);
		this.setResizable(true);
		this.setLocationByPlatform(true);
		this.setMinimumSize(new Dimension(300, 200));
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());

		// menu bar
		setJMenuBar(new EditorMenuBar(this, config));

		// CTC tree left
		treeViewer = new CtcTreeViewer(this, this, this);
		JScrollPane scrollTree = new JScrollPane(treeViewer);
		scrollTree.setMinimumSize(new Dimension(50, 0));
		scrollTree.setPreferredSize(new Dimension(500, 500));

		// editor panel
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		JScrollPane scrollMainPanel = new JScrollPane(mainPanel);
		scrollMainPanel.setMinimumSize(new Dimension(50, 0));
		scrollMainPanel.setPreferredSize(new Dimension(500, 500));

		// console
		JTextArea console = new JTextArea();
		console.setEditable(false);
		scrollConsole = new JScrollPane(console);
		scrollConsole.setBorder(BorderFactory.createEmptyBorder());
		scrollConsole.setMinimumSize(new Dimension(0, 50));
		scrollConsole.setPreferredSize(new Dimension(200, 200));
		// register console to logger
		SwingAppender.setConsole(console, scrollConsole);

		// split
		splitTreeAndMain = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollTree, scrollMainPanel);
		splitTreeAndMain.setDividerSize(DIVIDER_SIZE);
		splitTreeAndMain.setBorder(BorderFactory.createEmptyBorder());
		splitTreeAndMain.setMinimumSize(new Dimension(0, 50));
		splitTreeAndMain.setPreferredSize(new Dimension(500, 500));
		splitTreeAndMain.setResizeWeight(0.0f);
		splitTopBottom = new JSplitPane(JSplitPane.VERTICAL_SPLIT, splitTreeAndMain, scrollConsole);
		splitTopBottom.setDividerSize(DIVIDER_SIZE);
		splitTopBottom.setBorder(BorderFactory.createEmptyBorder());
		splitTopBottom.setResizeWeight(1.0f);
		setContentPane(splitTopBottom);

		this.setVisible(true);
		updateConsole();

		// set default divider locations
		splitTreeAndMain.setDividerLocation(0.3D);
		splitTopBottom.setDividerLocation(0.7D);

		if (args.length > 0) {
			onFileOpened(new File(args[0]));
		}
	}

	public void refreshUI() {
		invalidate();
		validate();
		repaint();
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
	public void menuSave() {
		// TODO: implement save operation
	}

	@Override
	public void menuSaveAs() {
		// TODO: implement save operation
	}

	@Override
	public void menuClose() {
		treeViewer.setCtc(null);
	}

	@Override
	public void menuExit() {
		System.exit(0);
	}

	@Override
	public boolean menuToggleConsole() {
		showConsole = !showConsole;
		updateConsole();
		config.setShowConsole(showConsole);
		config.save();
		return showConsole;
	}

	public void updateConsole() {
		if (showConsole) {
			setContentPane(splitTopBottom);
			splitTopBottom.setLeftComponent(splitTreeAndMain);
			if (showConsoleOnStartup) {
				int dividerLocation = getHeight() - consoleHeight;
				if (dividerLocation < 50) {
					dividerLocation = 50;
				}
				refreshUI();
				splitTopBottom.setDividerLocation(dividerLocation);
			} else {
				refreshUI();
				splitTopBottom.setDividerLocation(0.7D);
				showConsoleOnStartup = true;
			}
		} else {
			consoleHeight = getHeight() - splitTopBottom.getDividerLocation();
			setContentPane(splitTreeAndMain);
			refreshUI();
		}
	}

	@Override
	public void onHeaderSelected(CtcHeader header) {
		LOG.debug("header selected");
	}

	@Override
	public void onChainSelected(Set<CtcChain> chains) {
		LOG.debug(chains.size() + " chains selected");
	}

	@Override
	public void onBoneSelected(Set<CtcBone> bones) {
		LOG.debug(bones.size() + " bones selected");
	}

	@Override
	public void onIllegalSelection() {
		LOG.debug("illegal selection");
	}

	public static void main(String[] args) {
		LOG.debug("MHW CTC Editor is starting");
		copyStarterFile();
		EventQueue.invokeLater(() -> {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception e) {
				LOG.info("Could not set system look and feel");
			}
			new CtcEditor(args);
		});
	}

	public static void copyStarterFile() {
		try {
			File sourceLocation = new File(CtcEditor.class.getProtectionDomain().getCodeSource().getLocation().toURI());
			if (sourceLocation.getAbsolutePath().replace('\\', '/').endsWith("target/classes")) {
				LOG.debug("Running from IDE");
			} else if (sourceLocation.getAbsolutePath().endsWith(".jar")) {
				File starterFile = new File(sourceLocation.getParentFile(), "MHW-CTC-Editor.cmd");
				if (!starterFile.exists()) {
					URL url = MethodHandles.lookup().lookupClass().getResource("/MHW-CTC-Editor.cmd");
					FileUtils.copyURLToFile(url, starterFile);
				}
			}
		} catch (Exception e) {
			LOG.warn("Could not copy starter file", e);
		}
	}
}
