/*
 * PaymentTypeSelectionDialog.java
 *
 * Created on August 25, 2006, 3:47 PM
 */

package com.floreantpos.ui.dialog;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.IconFactory;
import com.floreantpos.model.PaymentType;
import com.floreantpos.swing.PosButton;

/**
 *
 * @author  MShahriar
 */
public class PaymentTypeSelectionDialog extends POSDialog {
	PaymentType selectedPaymentType;
	private PaymentSelectionButton btnCash;
	/*private PaymentSelectionButton btnGiftCert;*/
	private PaymentSelectionButton btnCard;

	/** Creates new form PaymentTypeSelectionDialog */
	public PaymentTypeSelectionDialog() {
		super();
		setTitle("Select payment type");
		
		initComponents();
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
	private void initComponents() {
		JPanel content = new JPanel(new MigLayout("gap 5px 20px, fill"));
		content.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		JPanel genericPanel = new JPanel(new GridLayout(1, 0, 15, 15));
		btnCash = new PaymentSelectionButton(PaymentType.CASH);
		genericPanel.add(btnCash, "grow,wrap");
		
		/*btnGiftCert = new PaymentSelectionButton(PaymentType.GIFT_CERTIFICATE);
		genericPanel.add(btnGiftCert);
		content.add(genericPanel, "height 60px, wrap, growx");*/
		
		btnCard = new PaymentSelectionButton(PaymentType.CARD);
		genericPanel.add(btnCard);
		content.add(genericPanel, "height 60px, wrap, growx");
		
		JPanel onlinePanel = new JPanel(new GridLayout(1, 0, 10, 10));
		onlinePanel.add(new PaymentSelectionButton(PaymentType.MENULOG));
		onlinePanel.add(new PaymentSelectionButton(PaymentType.EATNOW));
		onlinePanel.add(new PaymentSelectionButton(PaymentType.DELIVERY_HERO));
		
		onlinePanel.setBorder(new CompoundBorder(new TitledBorder("Online Payment"), new EmptyBorder(10, 10, 10, 10)));
		content.add(onlinePanel, "wrap, height 110px, growx");
		
		/*JPanel debitCardPanel = new JPanel(new GridLayout(1, 0, 10, 10));
		debitCardPanel.add(new PaymentSelectionButton(PaymentType.DEBIT_VISA));
		debitCardPanel.add(new PaymentSelectionButton(PaymentType.DEBIT_MASTER_CARD));
		
		debitCardPanel.setBorder(new CompoundBorder(new TitledBorder("DEBIT CARD"), new EmptyBorder(10, 10, 10, 10)));
		content.add(debitCardPanel, "wrap, height 110px, growx");*/
		
		PosButton cancel = new PosButton("CANCEL");
		cancel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				setCanceled(true);
				dispose();
			}
		});
		
		content.add(cancel, "alignx center, gaptop 20px");
		
		add(content);

		pack();
	}// </editor-fold>//GEN-END:initComponents

	public PaymentType getSelectedPaymentType() {
		return selectedPaymentType;
	}

	// End of variables declaration//GEN-END:variables
	
	class PaymentSelectionButton extends PosButton implements ActionListener {
		PaymentType paymentType;
		
		public PaymentSelectionButton(PaymentType p) {
			paymentType = p;
			
			if (p.getImageFile() != null) {
				setIcon(IconFactory.getIcon("/ui_icons/", "" + p.getImageFile()));
			}
			else {
				setText(p.getDisplayString());
			}
			
			addActionListener(this);
			setEnabled(paymentType.isSupported());
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			selectedPaymentType = paymentType;
			setCanceled(false);
			dispose();
		}
	}
	
	public void setCashButtonVisible(boolean visible) {
		btnCash.setVisible(visible);
		btnCard.setVisible(visible);
	}
}
