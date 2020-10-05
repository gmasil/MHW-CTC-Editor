package de.gmasil.mhw.ctceditor.ui;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import javax.swing.JMenu;
import javax.swing.JMenuBar;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
class EditorMenuBarIT {
	@Test
	void testMenuBarEntries() {
		Config.setShowConsole(true);
		JMenuBar menu = new EditorMenuBar(null);
		assertThat(menu.getMenuCount(), is(equalTo(2)));
		assertThat(menu.getMenu(0).getText(), is(equalTo("File")));
		assertThat(menu.getMenu(1).getText(), is(equalTo("View")));
	}

	@Test
	void testFileMenuEntries() {
		Config.setShowConsole(true);
		JMenuBar menu = new EditorMenuBar(null);
		JMenu fileMenu = findMenu(menu, "File");
		assertThat(fileMenu.getMenuComponentCount(), is(equalTo(6)));
	}

	@Test
	void testViewMenuEntries() {
		Config.setShowConsole(true);
		JMenuBar menu = new EditorMenuBar(null);
		JMenu fileMenu = findMenu(menu, "View");
		assertThat(fileMenu.getMenuComponentCount(), is(equalTo(2)));
	}

	private JMenu findMenu(JMenuBar menu, String text) {
		for (int i = 0; i < menu.getMenuCount(); i++) {
			if (text.equals(menu.getMenu(i).getText())) {
				return menu.getMenu(i);
			}
		}
		return null;
	}
}
