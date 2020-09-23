package de.gmasil.mhw.ctceditor.ui;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class EditorMenuBar extends JMenuBar {
	public EditorMenuBar(MenuListener listener) {
		JMenu menuFile = new JMenu("File");
		add(menuFile);
		JMenuItem menuOpen = new JMenuItem("Open");
		menuOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		menuOpen.addActionListener(event -> {
			listener.menuOpen();
		});
		menuFile.add(menuOpen);
		JMenuItem menuClose = new JMenuItem("Close");
		menuClose.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK));
		menuClose.addActionListener(event -> {
			listener.menuClose();
		});
		menuFile.add(menuClose);
	}
}
