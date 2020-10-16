/**
 * MHW-CTC-Editor
 * Copyright © 2020 Simon Oelerich (simon.oelerich@gmasil.de)
 *
 * This file is part of MHW-CTC-Editor.
 *
 * MHW-CTC-Editor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MHW-CTC-Editor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MHW-CTC-Editor. If not, see <https://www.gnu.org/licenses/>.
 */
package de.gmasil.mhw.ctceditor.ui;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.gmasil.mhw.ctceditor.CtcEditorStarter;
import de.gmasil.mhw.ctceditor.ui.api.MenuListener;

public class EditorMenuBar extends JMenuBar {
	private static final String CHECK_BOX_CHECKED = "check_box_checked";
	private static final String CHECK_BOX_BLANK = "check_box_blank";
	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	private static final int MENU_HEIGHT = 26;
	private static final int ICON_SIZE = 18;

	public EditorMenuBar(MenuListener listener) {
		// top menu file
		JMenu menuFile = new JMenu("File");
		menuFile.setMnemonic(KeyEvent.VK_F);
		add(menuFile);
		addOpenMenu(menuFile, listener);
		addSaveMenu(menuFile, listener);
		addSaveAsMenu(menuFile, listener);
		addCloseMenu(menuFile, listener);
		menuFile.addSeparator();
		addExitMenu(menuFile, listener);
		// top menu edit
		JMenu menuEdit = new JMenu("Edit");
		menuEdit.setMnemonic(KeyEvent.VK_E);
		add(menuEdit);
		addCopyMenu(menuEdit, listener);
		addPasteMenu(menuEdit, listener);
		addDeleteMenu(menuEdit, listener);
		// top menu find
		JMenu menuFind = new JMenu("Find");
		menuEdit.setMnemonic(KeyEvent.VK_F);
		add(menuFind);
		addFindBoneFunctionIdMenu(menuFind, listener);
		addFindDuplucateBoneFunctionIdsMenu(menuFind, listener);
		// top menu view
		JMenu menuView = new JMenu("View");
		menuView.setMnemonic(KeyEvent.VK_V);
		add(menuView);
		addShowConsoleMenu(menuView, listener);
		addShowUnknownFieldsMenu(menuView, listener);
		// top menu about
		JMenu menuAbout = new JMenu("Help");
		menuAbout.setMnemonic(KeyEvent.VK_H);
		add(menuAbout);
		addAboutMenu(menuAbout);
	}

	// File

	private void addOpenMenu(JMenu menuFile, MenuListener listener) {
		JMenuItem menuOpen = createJMenuItem("Open ...");
		menuOpen.setMnemonic(KeyEvent.VK_O);
		menuOpen.setAccelerator(KeyStroke.getKeyStroke("control O"));
		menuOpen.addActionListener(event -> listener.menuOpen());
		setIcon(menuOpen, "folder");
		menuFile.add(menuOpen);
	}

	private void addSaveMenu(JMenu menuFile, MenuListener listener) {
		JMenuItem menuSave = createJMenuItem("Save");
		menuSave.setMnemonic(KeyEvent.VK_S);
		menuSave.setAccelerator(KeyStroke.getKeyStroke("control S"));
		menuSave.addActionListener(event -> listener.menuSave());
		setIcon(menuSave, "save");
		menuFile.add(menuSave);
	}

	private void addSaveAsMenu(JMenu menuFile, MenuListener listener) {
		JMenuItem menuSaveAs = createJMenuItem("Save as ...");
		menuSaveAs.setMnemonic(KeyEvent.VK_A);
		menuSaveAs.setAccelerator(KeyStroke.getKeyStroke("control shift S"));
		menuSaveAs.setDisplayedMnemonicIndex(5);
		menuSaveAs.addActionListener(event -> listener.menuSaveAs());
		menuFile.add(menuSaveAs);
	}

	private void addCloseMenu(JMenu menuFile, MenuListener listener) {
		JMenuItem menuClose = createJMenuItem("Close");
		menuClose.setMnemonic(KeyEvent.VK_C);
		menuClose.setAccelerator(KeyStroke.getKeyStroke("control W"));
		menuClose.addActionListener(event -> listener.menuClose());
		setIcon(menuClose, "close");
		menuFile.add(menuClose);
	}

	private void addExitMenu(JMenu menuFile, MenuListener listener) {
		JMenuItem menuExit = createJMenuItem("Exit");
		menuExit.setMnemonic(KeyEvent.VK_X);
		menuExit.setAccelerator(KeyStroke.getKeyStroke("control Q"));
		menuExit.addActionListener(event -> listener.menuExit());
		setIcon(menuExit, "exit");
		menuFile.add(menuExit);
	}

	// Edit

	private void addCopyMenu(JMenu menuEdit, MenuListener listener) {
		JMenuItem menuCopy = createJMenuItem("Copy");
		menuCopy.setMnemonic(KeyEvent.VK_C);
		menuCopy.setAccelerator(KeyStroke.getKeyStroke("control C"));
		menuCopy.addActionListener(event -> listener.menuCopy());
		setIcon(menuCopy, "copy");
		menuEdit.add(menuCopy);
	}

	private void addPasteMenu(JMenu menuEdit, MenuListener listener) {
		JMenuItem menuPaste = createJMenuItem("Paste");
		menuPaste.setMnemonic(KeyEvent.VK_P);
		menuPaste.setAccelerator(KeyStroke.getKeyStroke("control V"));
		menuPaste.addActionListener(event -> listener.menuPaste());
		setIcon(menuPaste, "paste");
		menuEdit.add(menuPaste);
	}

	private void addDeleteMenu(JMenu menuEdit, MenuListener listener) {
		JMenuItem menuDelete = createJMenuItem("Delete");
		menuDelete.setMnemonic(KeyEvent.VK_D);
		menuDelete.setAccelerator(KeyStroke.getKeyStroke("DELETE"));
		menuDelete.addActionListener(event -> listener.menuDelete());
		setIcon(menuDelete, "close");
		menuEdit.add(menuDelete);
	}

	// Find

	private void addFindBoneFunctionIdMenu(JMenu menuFind, MenuListener listener) {
		JMenuItem menuFindBoneFunctionId = createJMenuItem("Bone Function ID");
		menuFindBoneFunctionId.setMnemonic(KeyEvent.VK_B);
		menuFindBoneFunctionId.setAccelerator(KeyStroke.getKeyStroke("control F"));
		menuFindBoneFunctionId.addActionListener(event -> listener.menuFindBoneFunctionID());
		setIcon(menuFindBoneFunctionId, "search");
		menuFind.add(menuFindBoneFunctionId);
	}

	private void addFindDuplucateBoneFunctionIdsMenu(JMenu menuFind, MenuListener listener) {
		JMenuItem menuFindDuplicateBoneFunctionIds = createJMenuItem("Duplicate Bone Function IDs");
		menuFindDuplicateBoneFunctionIds.setMnemonic(KeyEvent.VK_D);
		menuFindDuplicateBoneFunctionIds.setAccelerator(KeyStroke.getKeyStroke("control D"));
		menuFindDuplicateBoneFunctionIds.addActionListener(event -> listener.menuFindDuplicateBoneFunctionIDs());
		setIcon(menuFindDuplicateBoneFunctionIds, "search-file");
		menuFind.add(menuFindDuplicateBoneFunctionIds);
	}

	// View

	private void addShowConsoleMenu(JMenu menuView, MenuListener listener) {
		JMenuItem menuConsole = createJMenuItem("Show console");
		menuConsole.setMnemonic(KeyEvent.VK_C);
		menuConsole.addActionListener(event -> {
			if (listener.menuToggleConsole()) {
				setIcon(menuConsole, CHECK_BOX_CHECKED);
			} else {
				setIcon(menuConsole, CHECK_BOX_BLANK);
			}
		});
		if (Config.getShowConsole()) {
			setIcon(menuConsole, CHECK_BOX_CHECKED);
		} else {
			setIcon(menuConsole, CHECK_BOX_BLANK);
		}
		menuView.add(menuConsole);
	}

	private void addShowUnknownFieldsMenu(JMenu menuView, MenuListener listener) {
		JMenuItem menuUnknownFields = createJMenuItem("Show unknown fields");
		menuUnknownFields.setMnemonic(KeyEvent.VK_U);
		menuUnknownFields.addActionListener(event -> {
			if (listener.menuToggleUnknownFields()) {
				setIcon(menuUnknownFields, CHECK_BOX_CHECKED);
			} else {
				setIcon(menuUnknownFields, CHECK_BOX_BLANK);
			}
		});
		if (Config.getShowUnknownFields()) {
			setIcon(menuUnknownFields, CHECK_BOX_CHECKED);
		} else {
			setIcon(menuUnknownFields, CHECK_BOX_BLANK);
		}
		menuView.add(menuUnknownFields);
	}

	// help

	private void addAboutMenu(JMenu menuHelp) {
		JMenuItem menuAbout = createJMenuItem("About");
		menuAbout.setMnemonic(KeyEvent.VK_A);
		menuAbout.addActionListener(event -> {
			JOptionPane.showMessageDialog(EditorMenuBar.this,
					"MHW CTC Editor\nVersion: " + CtcEditorStarter.getVersion() + "\nRevision: "
							+ CtcEditorStarter.getRevision(),
					"About", JOptionPane.YES_OPTION, UIManager.getIcon("OptionPane.informationIcon"));
		});
		setIcon(menuAbout, "help");
		menuHelp.add(menuAbout);
	}

	// helpers

	private JMenuItem createJMenuItem(String text) {
		JMenuItem menuItem = new SizedMenuItem(text);
		menuItem.setMinimumSize(new Dimension(0, MENU_HEIGHT));
		menuItem.setMaximumSize(new Dimension(9999, MENU_HEIGHT));
		return menuItem;
	}

	private void setIcon(JMenuItem menu, String name) {
		try {
			InputStream stream = getClass().getResourceAsStream("/img/" + name + ".png");
			BufferedImage inputImage = ImageIO.read(stream);
			BufferedImage resizedImage = new BufferedImage(ICON_SIZE, ICON_SIZE, inputImage.getType());
			Graphics2D g2d = resizedImage.createGraphics();
			g2d.drawImage(inputImage, 0, 0, ICON_SIZE, ICON_SIZE, null);
			g2d.dispose();
			ImageIcon icon = new ImageIcon(resizedImage);
			menu.setIcon(icon);
		} catch (Exception e) {
			LOG.warn("Could not load menu icon, ignoring", e);
		}
	}

	private static class SizedMenuItem extends JMenuItem {
		public SizedMenuItem(String text) {
			super(text);
		}

		@Override
		public Dimension getPreferredSize() {
			Dimension preferred = super.getPreferredSize();
			Dimension minimum = getMinimumSize();
			Dimension maximum = getMaximumSize();
			preferred.width = Math.min(Math.max(preferred.width, minimum.width), maximum.width);
			preferred.height = Math.min(Math.max(preferred.height, minimum.height), maximum.height);
			return preferred;
		}
	}
}
