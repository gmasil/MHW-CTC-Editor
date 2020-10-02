package de.gmasil.mhw.ctceditor.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.lang.invoke.MethodHandles;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.gmasil.mhw.ctceditor.ctc.CtcBone;
import de.gmasil.mhw.ctceditor.ctc.CtcChain;
import de.gmasil.mhw.ctceditor.ctc.CtcHeader;
import de.gmasil.mhw.ctceditor.ctc.CtcIO;
import de.gmasil.mhw.ctceditor.logging.SwingAppender;
import de.gmasil.mhw.ctceditor.ui.api.AllowSelectionCallback;
import de.gmasil.mhw.ctceditor.ui.api.FileOpenedListener;
import de.gmasil.mhw.ctceditor.ui.api.MenuListener;
import de.gmasil.mhw.ctceditor.ui.api.SelectionListener;
import de.gmasil.mhw.ctceditor.ui.panel.CtcBoneEditorPanel;
import de.gmasil.mhw.ctceditor.ui.panel.CtcChainEditorPanel;
import de.gmasil.mhw.ctceditor.ui.panel.CtcHeaderEditorPanel;
import de.gmasil.mhw.ctceditor.ui.panel.generic.BaseCtcEditorPanel;

public class CtcEditor extends JFrame
		implements FileOpenedListener, MenuListener, SelectionListener, AllowSelectionCallback {
	private static final String SELECT_TYPE_INFO = "Please do not select items of different types";
	private static final String SELECT_INFO = "Select one or multiple objects on the left";
	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	private static final int DIVIDER_SIZE = 4;

	private CtcTreeViewer treeViewer;
	private WindowsFileChooser windowsFileChooser = new WindowsFileChooser(this);
	private JScrollPane scrollConsole;
	private JSplitPane splitTreeAndMain;
	private JSplitPane splitTopBottom;
	private boolean showConsole = Config.getShowConsole();
	private boolean showConsoleOnStartup = showConsole;
	private int consoleHeight;
	private boolean showUnknownFields = Config.getShowUnknownFields();
	private BaseCtcEditorPanel mainPanel;

	public CtcEditor(String... args) {
		this.setTitle("MHW CTC Editor");
		this.setSize(1000, 620);
		this.setResizable(true);
		this.setLocationByPlatform(true);
		this.setMinimumSize(new Dimension(300, 200));
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				menuExit();
			}
		});

		getContentPane().setLayout(new BorderLayout());

		// menu bar
		setJMenuBar(new EditorMenuBar(this));

		// CTC tree left
		treeViewer = new CtcTreeViewer(this, this, this, this);
		JScrollPane scrollTree = new JScrollPane(treeViewer);
		scrollTree.setMinimumSize(new Dimension(50, 0));
		scrollTree.setPreferredSize(new Dimension(500, 500));

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
		splitTreeAndMain = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollTree, new JPanel());
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

		// main panel
		setMainText("");

		this.setVisible(true);
		updateConsole();

		// set default divider locations
		splitTreeAndMain.setDividerLocation(0.3D);
		splitTopBottom.setDividerLocation(0.7D);

		if (args.length > 0) {
			onFileOpened(new File(args[0]));
		}
	}

	private void refreshUI() {
		invalidate();
		validate();
		repaint();
	}

	// FileOpenedListener

	@Override
	public void onFileOpened(File file) {
		try {
			treeViewer.setCtc(CtcIO.readFile(file));
			LOG.info("CTC file loaded successfully: {}", file.getAbsolutePath());
			setMainText(SELECT_INFO);
		} catch (Exception e) {
			LOG.warn("Error while reading CTC file", e);
		}
	}

	// MenuListener

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
		setMainText(SELECT_INFO);
		treeViewer.setCtc(null);
	}

	@Override
	public void menuExit() {
		LOG.info("Shutting down MHW CTC Editor");
		System.exit(0);
	}

	@Override
	public boolean menuToggleConsole() {
		showConsole = !showConsole;
		updateConsole();
		Config.setShowConsole(showConsole);
		Config.save();
		return showConsole;
	}

	@Override
	public boolean menuToggleUnknownFields() {
		showUnknownFields = !showUnknownFields;
		Config.setShowUnknownFields(showUnknownFields);
		Config.save();
		return showUnknownFields;
	}

	// SelectionListener

	@Override
	public void onHeaderSelected(CtcHeader header) {
		setMainPanel(new CtcHeaderEditorPanel(header));
	}

	@Override
	public void onChainSelected(Set<CtcChain> chains) {
		setMainPanel(new CtcChainEditorPanel(chains));
	}

	@Override
	public void onBoneSelected(Set<CtcBone> bones) {
		setMainPanel(new CtcBoneEditorPanel(bones));
	}

	@Override
	public void onTopicSelected() {
		setMainText(SELECT_INFO);
	}

	@Override
	public void onIllegalSelection() {
		setMainText(SELECT_TYPE_INFO);
	}

	// AllowSellectionCallback

	@Override
	public boolean allowSelectionChange() {
		if (getMainPanel().hasDataChanged()) {
			int showConfirmDialog = JOptionPane.showConfirmDialog(this,
					"You have unsaved changes, do you want to save them now?", "Unsaved changes",
					JOptionPane.YES_NO_CANCEL_OPTION);
			if (showConfirmDialog == JOptionPane.YES_OPTION) {
				JOptionPane.showMessageDialog(this, "saving...");
			} else if (showConfirmDialog == JOptionPane.NO_OPTION) {
				JOptionPane.showMessageDialog(this, "discarding...");
			} else {
				JOptionPane.showMessageDialog(this, "staying in windows");
				return false;
			}
		}
		return true;
	}

	private void setMainText(String text) {
		BaseCtcEditorPanel panel = new BaseCtcEditorPanel();
		panel.getMainPanel().add(new JLabel(text));
		setMainPanel(panel);
	}

	private void setMainPanel(BaseCtcEditorPanel component) {
		mainPanel = component;
		int dividerLocation = splitTreeAndMain.getDividerLocation();
		splitTreeAndMain.setRightComponent(component);
		splitTreeAndMain.setDividerLocation(dividerLocation);
	}

	private BaseCtcEditorPanel getMainPanel() {
		return mainPanel;
	}

	private void updateConsole() {
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
}
