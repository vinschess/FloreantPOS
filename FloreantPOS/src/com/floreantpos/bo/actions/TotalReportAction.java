package com.floreantpos.bo.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JTabbedPane;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.report.TotalReportView;

public class TotalReportAction extends AbstractAction {

	public TotalReportAction() {
		super(com.floreantpos.POSConstants.TOTAL_REPORT);
	}

	public TotalReportAction(String name) {
		super(name);
	}

	public TotalReportAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		BackOfficeWindow window = com.floreantpos.util.POSUtil.getBackOfficeWindow();
		JTabbedPane tabbedPane = window.getTabbedPane();
		
		TotalReportView reportView = null;
		int index = tabbedPane.indexOfTab(com.floreantpos.POSConstants.TOTAL_REPORT);
		if (index == -1) {
			reportView = new TotalReportView();
			tabbedPane.addTab(com.floreantpos.POSConstants.TOTAL_REPORT, reportView);
		}
		else {
			reportView = (TotalReportView) tabbedPane.getComponentAt(index);
		}
		tabbedPane.setSelectedComponent(reportView);
	}

}
