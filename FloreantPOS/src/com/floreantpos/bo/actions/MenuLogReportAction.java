package com.floreantpos.bo.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JTabbedPane;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.report.MenuLogReportView;

public class MenuLogReportAction extends AbstractAction {

	public MenuLogReportAction() {
		super(com.floreantpos.POSConstants.MENU_LOG_REPORT);
	}

	public MenuLogReportAction(String name) {
		super(name);
	}

	public MenuLogReportAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		BackOfficeWindow window = com.floreantpos.util.POSUtil.getBackOfficeWindow();
		JTabbedPane tabbedPane = window.getTabbedPane();
		
		MenuLogReportView reportView = null;
		int index = tabbedPane.indexOfTab(com.floreantpos.POSConstants.MENU_LOG_REPORT);
		if (index == -1) {
			reportView = new MenuLogReportView();
			tabbedPane.addTab(com.floreantpos.POSConstants.MENU_LOG_REPORT, reportView);
		}
		else {
			reportView = (MenuLogReportView) tabbedPane.getComponentAt(index);
		}
		tabbedPane.setSelectedComponent(reportView);
	}

}
