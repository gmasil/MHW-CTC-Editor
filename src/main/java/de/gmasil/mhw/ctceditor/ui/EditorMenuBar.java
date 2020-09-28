package de.gmasil.mhw.ctceditor.ui;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class EditorMenuBar extends JMenuBar {
	private static final int MENU_HEIGHT = 26;
	private static final int ICON_SIZE = 18;

	public EditorMenuBar(MenuListener listener, Config config) {
		// top menu file
		JMenu menuFile = new JMenu("File");
		menuFile.setMnemonic(KeyEvent.VK_F);
		add(menuFile);
		// file menu open
		{
			JMenuItem menuOpen = createJMenuItem("Open ...");
			menuOpen.setMnemonic(KeyEvent.VK_O);
			menuOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
			menuOpen.addActionListener(event -> {
				listener.menuOpen();
			});
			setIcon(menuOpen, "folder");
			menuFile.add(menuOpen);
		}
		// file menu save
		{
			JMenuItem menuSave = createJMenuItem("Save");
			menuSave.setMnemonic(KeyEvent.VK_S);
			menuSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
			menuSave.addActionListener(event -> {
				listener.menuSave();
			});
			setIcon(menuSave, "save");
			menuFile.add(menuSave);
		}
		// file menu save as
		{
			JMenuItem menuSaveAs = createJMenuItem("Save as ...");
			menuSaveAs.setMnemonic(KeyEvent.VK_A);
			menuSaveAs.setDisplayedMnemonicIndex(5);
			menuSaveAs.addActionListener(event -> {
				listener.menuSaveAs();
			});
			menuFile.add(menuSaveAs);
		}
		// file menu close
		{
			JMenuItem menuClose = createJMenuItem("Close");
			menuClose.setMnemonic(KeyEvent.VK_C);
			menuClose.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK));
			menuClose.addActionListener(event -> {
				listener.menuClose();
			});
			setIcon(menuClose, "close");
			menuFile.add(menuClose);
		}
		// separator
		menuFile.addSeparator();
		// file menu exit
		{
			JMenuItem menuExit = createJMenuItem("Exit");
			menuExit.setMnemonic(KeyEvent.VK_X);
			menuExit.addActionListener(event -> {
				listener.menuExit();
			});
			setIcon(menuExit, "exit");
			menuFile.add(menuExit);
		}
		// top menu view
		JMenu menuView = new JMenu("View");
		menuView.setMnemonic(KeyEvent.VK_V);
		add(menuView);
		// view menu console
		{
			JMenuItem menuConsole = createJMenuItem("Show console");
			menuConsole.setMnemonic(KeyEvent.VK_C);
			menuConsole.addActionListener(event -> {
				if (listener.menuToggleConsole()) {
					setIcon(menuConsole, "check_box_checked");
				} else {
					setIcon(menuConsole, "check_box_blank");
				}
			});
			if (config.getShowConsole()) {
				setIcon(menuConsole, "check_box_checked");
			} else {
				setIcon(menuConsole, "check_box_blank");
			}
			menuView.add(menuConsole);
		}
	}

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
