package de.gmasil.mhw.ctceditor.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;
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
import javax.swing.tree.DefaultMutableTreeNode;

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
	private static final double DEFAULT_DEVIDIER_LOCATION_LEFT_RIGHT = 0.5D;
	private static final double DEFAULT_DEVIDIER_LOCATION_TOP_BOTTOM = 0.7D;

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

	public CtcEditor(List<String> fileNames) {
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

		// set icons
		setApplicationIcons();

		this.setVisible(true);
		updateConsole();

		// set default divider locations
		splitTreeAndMain.setDividerLocation(DEFAULT_DEVIDIER_LOCATION_LEFT_RIGHT);
		splitTopBottom.setDividerLocation(DEFAULT_DEVIDIER_LOCATION_TOP_BOTTOM);

		if (fileNames.size() > 1) {
			LOG.warn("Currently it is only possible to open one file at a time, opening first file only");
		}
		if (!fileNames.isEmpty()) {
			onFileOpened(new File(fileNames.get(0)));
		}
	}

	private void setApplicationIcons() {
		String[] fileNames = new String[] { "/img/app-icon-20.png", "/img/app-icon-40.png" };
		List<Image> images = new LinkedList<>();
		for (String fileName : fileNames) {
			try {
				images.add(ImageIO.read(getClass().getResourceAsStream(fileName)));
			} catch (Exception e) {
				LOG.warn("Could not load app icon {}", fileName);
			}
		}
		setIconImages(images);
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
			treeViewer.setCtcChanged(false);
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
				treeViewer.setCtcChanged(false);
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
		treeViewer.setCtcChanged(false);
		currentlyOpenedFile = null;
	}

	@Override
	public void menuExit() {
		if (allowSelectionChange()) {
			if (treeViewer.isCtcChanged()) {
				int showConfirmDialog = JOptionPane.showConfirmDialog(this,
						"You have unsaved changes to your CTC file, do you really want to exit?", "Unsaved changes",
						JOptionPane.YES_NO_OPTION);
				if (showConfirmDialog != JOptionPane.YES_OPTION) {
					return;
				}
			}
			LOG.info("Shutting down MHW CTC Editor");
			System.exit(0);
		}
	}

	@Override
	public void menuCopy() {
		// TODO: cleanup
		if (treeViewer.getSelectionCount() == 0) {
			JOptionPane.showMessageDialog(this, "You have to select an object in the CTC tree view",
					"Unsupported Operation", JOptionPane.OK_OPTION);
		} else if (treeViewer.getSelectionCount() == 1) {
			Object lastPathComponent = ((DefaultMutableTreeNode) treeViewer.getSelectionPath().getLastPathComponent())
					.getUserObject();
			String serialized = SerializeUtils.serialize(lastPathComponent);
			StringSelection selection = new StringSelection(serialized);
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(selection, selection);
		} else {
			JOptionPane.showMessageDialog(this, "It is not possible to copy multiple objects", "Unsupported Operation",
					JOptionPane.OK_OPTION);
		}
	}

	@Override
	public void menuPaste() {
		// TODO: cleanup
		// TODO: make it possible to paste CTC bones
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		try {
			Transferable t = clipboard.getContents(null);
			if (t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
				String data = (String) t.getTransferData(DataFlavor.stringFlavor);
				Object deserialized = SerializeUtils.deserialize(data);
				if (deserialized != null) {
					if (deserialized instanceof CtcChain) {
						CtcChain chain = (CtcChain) deserialized;
						if (treeViewer.getSelectionCount() == 1) {
							Object lastPathComponent = ((DefaultMutableTreeNode) treeViewer.getSelectionPath()
									.getLastPathComponent()).getUserObject();
							if (lastPathComponent instanceof CtcChain) {
								CtcChain before = (CtcChain) lastPathComponent;
								List<CtcChain> chains = treeViewer.getCtc().getChains();
								int index = -1;
								for (int i = 0; i < chains.size(); i++) {
									if (chains.get(i) == before) {
										index = i;
										break;
									}
								}
								if (index >= 0) {
									chains.add(index + 1, chain);
									treeViewer.getCtc().recalculate();
									treeViewer.refreshTree();
									treeViewer.setCtcChanged(true);
									LOG.info("CTC chain was pasted successfully");
								} else {
									LOG.error("Error while finding the position to paste CTC chain");
									JOptionPane.showMessageDialog(this,
											"Error while finding the position to paste CTC chain",
											"Unsupported Operation", JOptionPane.OK_OPTION);
								}
							} else {
								LOG.warn("It is only possible to paste a CTC chain when another CTC chain is selected");
								JOptionPane.showMessageDialog(this,
										"It is only possible to paste a CTC chain when another CTC chain is selected",
										"Unsupported Operation", JOptionPane.OK_OPTION);
							}
						} else {
							JOptionPane.showMessageDialog(this,
									"It is only possible to paste when exactly one object is selected",
									"Unsupported Operation", JOptionPane.OK_OPTION);
						}
					} else {
						LOG.warn("Currently it is only possible to paste CTC chains");
						JOptionPane.showMessageDialog(this, "Currently it is only possible to paste CTC chains",
								"Unsupported Operation", JOptionPane.OK_OPTION);
					}
				} else {
					LOG.warn("Pasted data is not a CTC object");
					JOptionPane.showMessageDialog(this, "Pasted data is not a CTC object", "Unsupported Operation",
							JOptionPane.OK_OPTION);
				}
			}
		} catch (Exception e) {
			LOG.error("Error while performing clipboard operation", e);
		}
	}

	@Override
	public void menuDelete() {
		if (treeViewer.getSelectionCount() == 0) {
			JOptionPane.showMessageDialog(this, "You have to select an object in the CTC tree view",
					"Unsupported Operation", JOptionPane.OK_OPTION);
		} else if (treeViewer.getSelectionCount() == 1) {
			Object lastPathComponent = ((DefaultMutableTreeNode) treeViewer.getSelectionPath().getLastPathComponent())
					.getUserObject();
			if (lastPathComponent instanceof CtcChain) {
				CtcChain chain = (CtcChain) lastPathComponent;
				treeViewer.getCtc().getChains().remove(chain);
				treeViewer.getCtc().recalculate();
				treeViewer.refreshTree();
				treeViewer.setCtcChanged(true);
			} else {
				JOptionPane.showMessageDialog(this, "Currently it is only possible to delete CTC chains",
						"Unsupported Operation", JOptionPane.OK_OPTION);
			}
		} else {
			JOptionPane.showMessageDialog(this, "It is not possible to delete multiple objects",
					"Unsupported Operation", JOptionPane.OK_OPTION);
		}
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
				setMainPanel(new CtcHeaderEditorPanel(panel.getObject(), this, treeViewer));
			} else if (getMainPanel() instanceof CtcChainEditorPanel) {
				CtcChainEditorPanel panel = (CtcChainEditorPanel) getMainPanel();
				setMainPanel(new CtcChainEditorPanel(panel.getObjectSet(), this, treeViewer));
			} else if (getMainPanel() instanceof CtcBoneEditorPanel) {
				CtcBoneEditorPanel panel = (CtcBoneEditorPanel) getMainPanel();
				setMainPanel(new CtcBoneEditorPanel(panel.getObjectSet(), this, treeViewer));
			}
		}
		return showUnknownFields;
	}

	// SelectionListener

	@Override
	public void onHeaderSelected(CtcHeader header) {
		setMainPanel(new CtcHeaderEditorPanel(header, this, treeViewer));
	}

	@Override
	public void onChainSelected(Set<CtcChain> chains) {
		setMainPanel(new CtcChainEditorPanel(chains, this, treeViewer));
	}

	@Override
	public void onBoneSelected(Set<CtcBone> bones) {
		setMainPanel(new CtcBoneEditorPanel(bones, this, treeViewer));
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
				splitTopBottom.setDividerLocation(DEFAULT_DEVIDIER_LOCATION_TOP_BOTTOM);
				showConsoleOnStartup = true;
			}
		} else {
			consoleHeight = getHeight() - splitTopBottom.getDividerLocation();
			setContentPane(splitTreeAndMain);
			refreshUI();
		}
	}
}
