package com.floreantpos.bo.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JTabbedPane;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.report.CashReportView;

public class CashReportAction extends AbstractAction {

	public CashReportAction() {
		super(com.floreantpos.POSConstants.CASH_REPORT);
	}

	public CashReportAction(String name) {
		super(name);
	}

	public CashReportAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		BackOfficeWindow window = com.floreantpos.util.POSUtil.getBackOfficeWindow();
		JTabbedPane tabbedPane = window.getTabbedPane();
		
		CashReportView reportView = null;
		int index = tabbedPane.indexOfTab(com.floreantpos.POSConstants.CASH_REPORT);
		if (index == -1) {
			reportView = new CashReportView();
			tabbedPane.addTab(com.floreantpos.POSConstants.CASH_REPORT, reportView);
		}
		else {
			reportView = (CashReportView) tabbedPane.getComponentAt(index);
		}
		tabbedPane.setSelectedComponent(reportView);
	}

}
