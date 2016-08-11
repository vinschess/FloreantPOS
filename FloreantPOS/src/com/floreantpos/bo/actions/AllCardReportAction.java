package com.floreantpos.bo.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JTabbedPane;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.report.AllCardReportView;

public class AllCardReportAction extends AbstractAction {

	public AllCardReportAction() {
		super(com.floreantpos.POSConstants.CARD_REPORT);
	}

	public AllCardReportAction(String name) {
		super(name);
	}

	public AllCardReportAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		BackOfficeWindow window = com.floreantpos.util.POSUtil.getBackOfficeWindow();
		JTabbedPane tabbedPane = window.getTabbedPane();
		
		AllCardReportView reportView = null;
		int index = tabbedPane.indexOfTab(com.floreantpos.POSConstants.CARD_REPORT);
		if (index == -1) {
			reportView = new AllCardReportView();
			tabbedPane.addTab(com.floreantpos.POSConstants.CARD_REPORT, reportView);
		}
		else {
			reportView = (AllCardReportView) tabbedPane.getComponentAt(index);
		}
		tabbedPane.setSelectedComponent(reportView);
	}

}
