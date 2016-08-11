package com.floreantpos.bo.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JTabbedPane;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.report.EatNowReportView;

public class EatNowReportAction extends AbstractAction {

	public EatNowReportAction() {
		super(com.floreantpos.POSConstants.EAT_NOW_REPORT);
	}

	public EatNowReportAction(String name) {
		super(name);
	}

	public EatNowReportAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		BackOfficeWindow window = com.floreantpos.util.POSUtil.getBackOfficeWindow();
		JTabbedPane tabbedPane = window.getTabbedPane();
		
		EatNowReportView reportView = null;
		int index = tabbedPane.indexOfTab(com.floreantpos.POSConstants.EAT_NOW_REPORT);
		if (index == -1) {
			reportView = new EatNowReportView();
			tabbedPane.addTab(com.floreantpos.POSConstants.EAT_NOW_REPORT, reportView);
		}
		else {
			reportView = (EatNowReportView) tabbedPane.getComponentAt(index);
		}
		tabbedPane.setSelectedComponent(reportView);
	}

}
