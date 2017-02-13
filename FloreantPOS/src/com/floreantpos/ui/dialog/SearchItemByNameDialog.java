package com.floreantpos.ui.dialog;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JSeparator;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;


import com.floreantpos.POSConstants;
import com.floreantpos.swing.PosButton;
import com.floreantpos.swing.QwertyKeyPad;
import com.floreantpos.ui.TitlePanel;

public class SearchItemByNameDialog extends POSDialog implements ActionListener {
	private String defaultValue = "";

	private TitlePanel titlePanel;
	private JTextField tfText;

	private boolean floatingPoint;
	private PosButton posButton_1;

	public SearchItemByNameDialog() {
		init();
	}

	private void init() {
		setResizable(false);

		Container contentPane = getContentPane();

		MigLayout layout = new MigLayout("fillx", "[60px,fill][60px,fill][60px,fill]", "[][][][][]");
		contentPane.setLayout(layout);

		titlePanel = new TitlePanel();
		contentPane.add(titlePanel, "spanx ,growy,height 60,wrap");

		tfText = new JTextField();
		tfText.setText(String.valueOf(defaultValue));
		tfText.setFont(tfText.getFont().deriveFont(Font.BOLD, 24));
		//tfText.setEditable(false);
		tfText.setFocusable(true);
		tfText.requestFocus();
		tfText.setBackground(Color.WHITE);
		//tfText.setHorizontalAlignment(JTextField.RIGHT);
		contentPane.add(tfText, "span 2, grow");

		PosButton posButton = new PosButton(POSConstants.CLEAR_ALL);
		posButton.setFocusable(false);
		posButton.setMinimumSize(new Dimension(25, 23));
		posButton.addActionListener(this);
		contentPane.add(posButton, "growy,height 55,wrap");

		QwertyKeyPad keyPad = new QwertyKeyPad();
		contentPane.add(keyPad, "newline, grow, span, h 300!, gaptop 10");
		
		contentPane.add(new JSeparator(JSeparator.HORIZONTAL), "newline, grow, span, gaptop 10px");

		posButton = new PosButton(POSConstants.OK);
		posButton.setFocusable(false);
		posButton.addActionListener(this);
		contentPane.add(posButton, "skip 1,grow");

		posButton_1 = new PosButton(POSConstants.CANCEL);
		posButton_1.setFocusable(false);
		posButton_1.addActionListener(this);
		contentPane.add(posButton_1, "grow");
	}

	private void doOk() {
		/*if (!validate(tfText.getText())) {
			POSMessageDialog.showError(this, POSConstants.INVALID_NUMBER);
			return;
		}*/
		setCanceled(false);
		dispose();
	}

	private void doCancel() {
		setCanceled(true);
		dispose();
	}

	private void doClearAll() {
		tfText.setText(String.valueOf(defaultValue));
	}

	private void doClear() {
		String s = tfText.getText();
		if (s.length() > 1) {
			s = s.substring(0, s.length() - 1);
		}
		else {
			s = String.valueOf(defaultValue);
		}
		tfText.setText(s);
	}

	private void doInsertNumber(String number) {
		String s = tfText.getText();
		double d = 0;

		try {
			d = Double.parseDouble(s);
		} catch (Exception x) {
		}

		if (d == 0) {
			tfText.setText(number);
			return;
		}

		s = s + number;
		if (!validate(s)) {
			POSMessageDialog.showError(this, POSConstants.INVALID_NUMBER);
			return;
		}
		tfText.setText(s);
	}

	private void doInsertDot() {
		//if (isFloatingPoint() && tfText.getText().indexOf('.') < 0) {
		String string = tfText.getText() + ".";
		if (!validate(string)) {
			POSMessageDialog.showError(this, POSConstants.INVALID_NUMBER);
			return;
		}
		tfText.setText(string);
		//}
	}

	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();

		if (POSConstants.CANCEL.equalsIgnoreCase(actionCommand)) {
			doCancel();
		}
		else if (POSConstants.OK.equalsIgnoreCase(actionCommand)) {
			doOk();
		}
		else if (actionCommand.equals(POSConstants.CLEAR_ALL)) {
			doClearAll();
		}
		else if (actionCommand.equals(POSConstants.CLEAR)) {
			doClear();
		}
		else if (actionCommand.equals(".")) {
			doInsertDot();
		}
		else {
			doInsertNumber(actionCommand);
		}

	}

	private boolean validate(String str) {
		if (isFloatingPoint()) {
			try {
				Double.parseDouble(str);
			} catch (Exception x) {
				return false;
			}
		}
		else {
			try {
				Integer.parseInt(str);
			} catch (Exception x) {
				return false;
			}
		}
		return true;
	}

	public void setTitle(String title) {
		titlePanel.setTitle(title);

		super.setTitle(title);
	}

	public void setDialogTitle(String title) {
		super.setTitle(title);
	}

	public String getValue() {
		return tfText.getText();
	}

	public void setValue(double value) {
		if (value == 0) {
			tfText.setText("0");
		}
		else if (isFloatingPoint()) {
			tfText.setText(String.valueOf(value));
		}
		else {
			tfText.setText(String.valueOf((int) value));
		}
	}

	public boolean isFloatingPoint() {
		return floatingPoint;
	}

	public void setFloatingPoint(boolean decimalAllowed) {
		this.floatingPoint = decimalAllowed;
	}

	public static void main(String[] args) {
		SearchItemByNameDialog dialog2 = new SearchItemByNameDialog();
		dialog2.pack();
		dialog2.setVisible(true);
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
		tfText.setText(String.valueOf(defaultValue));
	}

	public static String takeIntInput(String title) {
		SearchItemByNameDialog dialog = new SearchItemByNameDialog();
		dialog.setTitle(title);
		dialog.pack();
		dialog.open();

		if (dialog.isCanceled()) {
			return "";
		}

		return  dialog.getValue();
	}

	public static String takeDoubleInput(String title, String dialogTitle, double initialAmount) {
		SearchItemByNameDialog dialog = new SearchItemByNameDialog();
		dialog.setFloatingPoint(true);
		dialog.setValue(initialAmount);
		dialog.setTitle(title);
		dialog.setDialogTitle(dialogTitle);
		dialog.pack();
		dialog.open();

		if (dialog.isCanceled()) {
			return "";
		}

		return dialog.getValue();
	}

	public static String show(Component parent, String title, double initialAmount) {
		SearchItemByNameDialog dialog2 = new SearchItemByNameDialog();
		dialog2.setFloatingPoint(true);
		dialog2.setTitle(title);
		dialog2.pack();
		dialog2.setLocationRelativeTo(parent);
		dialog2.setValue(initialAmount);
		dialog2.setVisible(true);

		if (dialog2.isCanceled()) {
			return "";
		}

		return dialog2.getValue();
	}
}
