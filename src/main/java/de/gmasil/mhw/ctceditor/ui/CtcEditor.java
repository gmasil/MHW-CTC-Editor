package de.gmasil.mhw.ctceditor.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
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
import de.gmasil.mhw.ctceditor.ui.api.RepaintTreeCallback;
import de.gmasil.mhw.ctceditor.ui.api.SelectionListener;
import de.gmasil.mhw.ctceditor.ui.panel.CtcBoneEditorPanel;
import de.gmasil.mhw.ctceditor.ui.panel.CtcChainEditorPanel;
import de.gmasil.mhw.ctceditor.ui.panel.CtcHeaderEditorPanel;
import de.gmasil.mhw.ctceditor.ui.panel.generic.BaseCtcEditorPanel;

public class CtcEditor extends JFrame
		implements FileOpenedListener, MenuListener, SelectionListener, AllowSelectionCallback, RepaintTreeCallback {
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
	private File currentlyOpenedFile = null;

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
		setupCopyPasteActions();
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

	private void setupCopyPasteActions() {
		ActionMap actionMap = treeViewer.getActionMap();
		actionMap.put("copy", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				menuCopy();
			}
		});
		actionMap.put("paste", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				menuPaste();
			}
		});
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
			currentlyOpenedFile = file;
			LOG.info("CTC file loaded successfully: {}", file.getAbsolutePath());
			setMainText(SELECT_INFO);
		} catch (Exception e) {
			LOG.warn("Error while reading CTC file", e);
		}
	}

	@Override
	public void onFileSaved(File file) {
		if (currentlyOpenedFile != null) {
			try {
				CtcIO.write(treeViewer.getCtc(), file);
				LOG.info("CTC file successfully saved to {}", currentlyOpenedFile.getAbsolutePath());
			} catch (IOException e) {
				LOG.error("Error while saving CTC file to {}", currentlyOpenedFile.getAbsolutePath(), e);
			}
		}
	}

	// MenuListener

	@Override
	public void menuOpen() {
		windowsFileChooser.showOpenDialog();
	}

	@Override
	public void menuSave() {
		if (allowSelectionChange()) {
			onFileSaved(currentlyOpenedFile);
		}
	}

	@Override
	public void menuSaveAs() {
		if (allowSelectionChange()) {
			windowsFileChooser.showSaveDialog();
		}
	}

	@Override
	public void menuClose() {
		setMainText(SELECT_INFO);
		treeViewer.setCtc(null);
		currentlyOpenedFile = null;
	}

	@Override
	public void menuExit() {
		LOG.info("Shutting down MHW CTC Editor");
		System.exit(0);
	}

	@Override
	public void menuCopy() {
		// TODO: implement copy function
		LOG.info("Copy selection count: {}", treeViewer.getSelectionCount());
	}

	@Override
	public void menuPaste() {
		// TODO implement paste function
		LOG.info("Paste selection count: {}", treeViewer.getSelectionCount());
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
		if (allowSelectionChange()) {
			showUnknownFields = !showUnknownFields;
			Config.setShowUnknownFields(showUnknownFields);
			Config.save();
			if (getMainPanel() instanceof CtcHeaderEditorPanel) {
				CtcHeaderEditorPanel panel = (CtcHeaderEditorPanel) getMainPanel();
				setMainPanel(new CtcHeaderEditorPanel(panel.getObject(), this));
			} else if (getMainPanel() instanceof CtcChainEditorPanel) {
				CtcChainEditorPanel panel = (CtcChainEditorPanel) getMainPanel();
				setMainPanel(new CtcChainEditorPanel(panel.getObjectSet(), this));
			} else if (getMainPanel() instanceof CtcBoneEditorPanel) {
				CtcBoneEditorPanel panel = (CtcBoneEditorPanel) getMainPanel();
				setMainPanel(new CtcBoneEditorPanel(panel.getObjectSet(), this));
			}
		}
		return showUnknownFields;
	}

	// SelectionListener

	@Override
	public void onHeaderSelected(CtcHeader header) {
		setMainPanel(new CtcHeaderEditorPanel(header, this));
	}

	@Override
	public void onChainSelected(Set<CtcChain> chains) {
		setMainPanel(new CtcChainEditorPanel(chains, this));
	}

	@Override
	public void onBoneSelected(Set<CtcBone> bones) {
		setMainPanel(new CtcBoneEditorPanel(bones, this));
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
					"You have unapplied changes, do you want to apply them now?", "Unapplied changes",
					JOptionPane.YES_NO_CANCEL_OPTION);
			if (showConfirmDialog == JOptionPane.YES_OPTION) {
				getMainPanel().onApplyClicked();
			} else if (showConfirmDialog == JOptionPane.NO_OPTION) {
				getMainPanel().onResetClicked();
			} else {
				return false;
			}
		}
		return true;
	}

	// RepaintTreeCallback

	@Override
	public void repaintTree() {
		treeViewer.repaint();
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
