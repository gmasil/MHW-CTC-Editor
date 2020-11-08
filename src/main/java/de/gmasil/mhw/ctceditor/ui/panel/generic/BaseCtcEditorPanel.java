/**
 * MHW-CTC-Editor
 * Copyright © 2020 gmasil.de
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
package de.gmasil.mhw.ctceditor.ui.panel.generic;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import de.gmasil.mhw.ctceditor.ui.api.DataChangedCallback;
import net.miginfocom.swing.MigLayout;

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
		getMainPanel().setLayout(new MigLayout("wrap 3"));
		JScrollPane scrollMainPanel = new JScrollPane(getMainPanel());
		scrollMainPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.GRAY));
		scrollMainPanel.setMinimumSize(new Dimension(50, 50));
		scrollMainPanel.setPreferredSize(new Dimension(500, 500));
		scrollMainPanel.getVerticalScrollBar().setUnitIncrement(6);
		scrollMainPanel.getHorizontalScrollBar().setUnitIncrement(6);
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
		// apply button
		JButton btnApply = new JButton("Apply");
		btnApply.addActionListener(e -> {
			onApplyClicked();
		});
		controlPanel.add(btnApply);
		// reset button
		JButton btnReset = new JButton("Reset");
		btnReset.addActionListener(e -> {
			onResetClicked();
		});
		controlPanel.add(btnReset);
		add(controlPanel, BorderLayout.PAGE_END);
	}

	public void onApplyClicked() {
		// can be overwritten by subclasses
	}

	public void onResetClicked() {
		// can be overwritten by subclasses
	}

	@Override
	public boolean hasDataChanged() {
		return false;
	}
}
