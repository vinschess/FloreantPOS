package com.floreantpos.bo.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JTabbedPane;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.report.DeliveryHeroReportView;

public class DeliveryHeroReportAction extends AbstractAction {

	public DeliveryHeroReportAction() {
		super(com.floreantpos.POSConstants.DELIVERY_HERO_REPORT);
	}

	public DeliveryHeroReportAction(String name) {
		super(name);
	}

	public DeliveryHeroReportAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		BackOfficeWindow window = com.floreantpos.util.POSUtil.getBackOfficeWindow();
		JTabbedPane tabbedPane = window.getTabbedPane();
		
		DeliveryHeroReportView reportView = null;
		int index = tabbedPane.indexOfTab(com.floreantpos.POSConstants.DELIVERY_HERO_REPORT);
		if (index == -1) {
			reportView = new DeliveryHeroReportView();
			tabbedPane.addTab(com.floreantpos.POSConstants.DELIVERY_HERO_REPORT, reportView);
		}
		else {
			reportView = (DeliveryHeroReportView) tabbedPane.getComponentAt(index);
		}
		tabbedPane.setSelectedComponent(reportView);
	}

}
