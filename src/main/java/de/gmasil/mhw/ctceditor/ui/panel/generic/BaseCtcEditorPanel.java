package de.gmasil.mhw.ctceditor.ui.panel.generic;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import de.gmasil.mhw.ctceditor.ui.api.DataChangedCallback;

public class BaseCtcEditorPanel extends JPanel implements DataChangedCallback {
	private JPanel mainPanel = new JPanel();

	/**
	 * Creates a {@link BaseCtcEditorPanel} without title and without apply button
	 */
	public BaseCtcEditorPanel() {
		this(null, false);
	}

	/**
	 * Creates a {@link BaseCtcEditorPanel} with given title and applicability
	 * 
	 * @param title
	 * @param applicable
	 */
	public BaseCtcEditorPanel(String title, boolean applicable) {
		setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));
		setLayout(new BorderLayout());
		getMainPanel().setLayout(new GridLayout(0, 3, 10, 10));
		JScrollPane scrollMainPanel = new JScrollPane(mainPanel);
		scrollMainPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.GRAY));
		scrollMainPanel.setMinimumSize(new Dimension(50, 50));
		scrollMainPanel.setPreferredSize(new Dimension(500, 500));
		scrollMainPanel.getVerticalScrollBar().setUnitIncrement(6);
		add(scrollMainPanel, BorderLayout.CENTER);
		if (title != null) {
			setTitle(title);
		}
		if (applicable) {
			addApplyButtons();
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

	private void addApplyButtons() {
		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		JButton btnApply = new JButton("Apply");
		btnApply.addActionListener(e -> {
			onApplyClicked();
		});
		JButton btnReset = new JButton("Reset");
		btnApply.addActionListener(e -> {
			onResetClicked();
		});
		controlPanel.add(btnApply);
		controlPanel.add(btnReset);
		add(controlPanel, BorderLayout.PAGE_END);
	}

	public void onApplyClicked() {
		// can be overwritten be subclasses
	}

	public void onResetClicked() {
		// can be overwritten be subclasses
	}

	@Override
	public boolean hasDataChanged() {
		return false;
	}
}
