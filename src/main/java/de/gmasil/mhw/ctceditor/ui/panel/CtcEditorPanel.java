package de.gmasil.mhw.ctceditor.ui.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import de.gmasil.mhw.ctceditor.ui.api.DataChangedCallback;

public class CtcEditorPanel extends JPanel implements DataChangedCallback {
	private JPanel mainPanel = new JPanel();

	/**
	 * Creates a {@link CtcEditorPanel} without title and without save button
	 */
	public CtcEditorPanel() {
		this(null, false);
	}

	/**
	 * Creates a {@link CtcEditorPanel} with given title and savability
	 * 
	 * @param title
	 * @param saveable
	 */
	public CtcEditorPanel(String title, boolean saveable) {
		setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));
		setLayout(new BorderLayout());
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		JScrollPane scrollMainPanel = new JScrollPane(mainPanel);
		scrollMainPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.GRAY));
		scrollMainPanel.setMinimumSize(new Dimension(50, 50));
		scrollMainPanel.setPreferredSize(new Dimension(500, 500));
		add(scrollMainPanel, BorderLayout.CENTER);
		if (title != null) {
			setTitle(title);
		}
		if (saveable) {
			addSaveButtons();
		}
	}

	public JPanel getMainPanel() {
		return mainPanel;
	}

	public void setTitle(String title) {
		JPanel titlePanel = new JPanel();
		titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		JLabel label = new JLabel(title);
		label.setFont(new Font(label.getFont().getName(), Font.BOLD, (int) (label.getFont().getSize() * 1.2f)));
		titlePanel.add(label);
		add(titlePanel, BorderLayout.PAGE_START);
	}

	private void addSaveButtons() {
		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		JButton btnSave = new JButton("Apply");
		JButton btnReset = new JButton("Reset");
		controlPanel.add(btnSave);
		controlPanel.add(btnReset);
		add(controlPanel, BorderLayout.PAGE_END);
	}

	@Override
	public boolean hasDataChanged() {
		return false;
	}
}
