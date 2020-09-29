package de.gmasil.mhw.ctceditor.ui;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import javax.swing.JMenu;
import javax.swing.JMenuBar;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mockito;

@TestInstance(Lifecycle.PER_CLASS)
public class EditorMenuBarTest {
	@Test
	public void testMenuBarEntries() {
		Config configMock = Mockito.mock(Config.class);
		Mockito.when(configMock.getShowConsole()).thenReturn(true);
		JMenuBar menu = new EditorMenuBar(null, configMock);
		assertThat(menu.getMenuCount(), is(equalTo(2)));
		assertThat(menu.getMenu(0).getText(), is(equalTo("File")));
		assertThat(menu.getMenu(1).getText(), is(equalTo("View")));
	}

	@Test
	public void testFileMenuEntries() {
		Config configMock = Mockito.mock(Config.class);
		Mockito.when(configMock.getShowConsole()).thenReturn(true);
		JMenuBar menu = new EditorMenuBar(null, configMock);
		JMenu fileMenu = findMenu(menu, "File");
		assertThat(fileMenu.getMenuComponentCount(), is(equalTo(6)));
	}

	@Test
	public void testViewMenuEntries() {
		Config configMock = Mockito.mock(Config.class);
		Mockito.when(configMock.getShowConsole()).thenReturn(true);
		JMenuBar menu = new EditorMenuBar(null, configMock);
		JMenu fileMenu = findMenu(menu, "View");
		assertThat(fileMenu.getMenuComponentCount(), is(equalTo(1)));
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
