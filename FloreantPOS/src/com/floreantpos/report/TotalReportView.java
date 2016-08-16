package com.floreantpos.report;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JRViewer;

import org.jdesktop.swingx.JXDatePicker;

import com.floreantpos.POSConstants;
import com.floreantpos.model.util.DateUtil;
import com.floreantpos.report.service.ReportService;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.util.UiUtil;
import com.floreantpos.util.NumberUtil;

public class TotalReportView extends JPanel {
	private SimpleDateFormat fullDateFormatter = new SimpleDateFormat("yyyy MMM dd, hh:mm a");
	private SimpleDateFormat shortDateFormatter = new SimpleDateFormat("yyyy MMM dd");
	
	private JXDatePicker fromDatePicker = UiUtil.getCurrentMonthStart();
	private JXDatePicker toDatePicker = UiUtil.getCurrentMonthEnd();
	private JButton btnGo = new JButton(com.floreantpos.POSConstants.GO);
	private JPanel reportContainer;
	
	public TotalReportView() {
		super(new BorderLayout());
		
		JPanel topPanel = new JPanel(new MigLayout());
		
		topPanel.add(new JLabel(com.floreantpos.POSConstants.FROM + ":"), "grow");
		topPanel.add(fromDatePicker,"wrap");
		topPanel.add(new JLabel(com.floreantpos.POSConstants.TO + ":"), "grow");
		topPanel.add(toDatePicker,"wrap");
		topPanel.add(btnGo, "skip 1, al right");
		add(topPanel, BorderLayout.NORTH);
		
		JPanel centerPanel = new JPanel(new BorderLayout());
		centerPanel.setBorder(new EmptyBorder(0, 10,10,10));
		centerPanel.add(new JSeparator(), BorderLayout.NORTH);
		
		reportContainer = new JPanel(new BorderLayout());
		centerPanel.add(reportContainer);
		
		add(centerPanel);
		
		btnGo.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {
					viewReport();
				} catch (Exception e1) {
					POSMessageDialog.showError(TotalReportView.this, POSConstants.ERROR_MESSAGE, e1);
				}
			}
			
		});
	}
	
	private void viewReport() throws Exception {
		Date fromDate = fromDatePicker.getDate();
		Date toDate = toDatePicker.getDate();
		
		if(fromDate.after(toDate)) {
			POSMessageDialog.showError(com.floreantpos.util.POSUtil.getFocusedWindow(), com.floreantpos.POSConstants.FROM_DATE_CANNOT_BE_GREATER_THAN_TO_DATE_);
			return;
		}
		
		fromDate = DateUtil.startOfDay(fromDate);
		toDate = DateUtil.endOfDay(toDate);
		
		ReportService reportService = new ReportService();
		TotalReport report = reportService.getTotalReport(fromDate, toDate);
		
		HashMap<String, String> map = new HashMap<String, String>();
		ReportUtil.populateRestaurantProperties(map);
		map.put("reportTitle", "========= TOTAL REPORT ==========");
		map.put("fromDate", shortDateFormatter.format(fromDate));
		map.put("toDate", shortDateFormatter.format(toDate));
		map.put("reportTime", fullDateFormatter.format(new Date()));
		
		map.put("totalCash", NumberUtil.formatNumber(report.getTotalCash()));
		map.put("totalCashPaidAmount", NumberUtil.formatNumber(report.getTotalCashPaidAmount()));
		map.put("totalCashTips", NumberUtil.formatNumber(report.getTotalCashTips()));
		map.put("totalCashTotal", NumberUtil.formatNumber(report.getTotalCashPaidAmount()+report.getTotalCashTips()));
		map.put("totalCard", NumberUtil.formatNumber(report.getTotalCard()));
		map.put("totalCardPaidAmount", NumberUtil.formatNumber(report.getTotalCardPaidAmount()));
		map.put("totalCardTips", NumberUtil.formatNumber(report.getTotalCardTips()));
		map.put("totalCardTotal", NumberUtil.formatNumber(report.getTotalCardPaidAmount()+report.getTotalCardTips()));
		map.put("totalMenuLog", NumberUtil.formatNumber(report.getTotalMenuLog()));
		map.put("totalMenuLogPaidAmount", NumberUtil.formatNumber(report.getTotalMenuLogPaidAmount()));
		map.put("totalMenuLogTotal", NumberUtil.formatNumber(report.getTotalMenuLogPaidAmount()+report.getTotalMenuLogTips()));
		map.put("totalMenuLogTips", NumberUtil.formatNumber(report.getTotalMenuLogTips()));
		map.put("totalEatNow", NumberUtil.formatNumber(report.getTotalEatNow()));
		map.put("totalEatNowPaidAmount", NumberUtil.formatNumber(report.getTotalEatNowPaidAmount()));
		map.put("totalEatNowTips", NumberUtil.formatNumber(report.getTotalEatNowTips()));
		map.put("totalEatNowTotal", NumberUtil.formatNumber(report.getTotalEatNowPaidAmount()+report.getTotalEatNowTips()));
		map.put("totalDeliveryHero", NumberUtil.formatNumber(report.getTotalDeliveryHero()));
		map.put("totalDeliveryHeroPaidAmount", NumberUtil.formatNumber(report.getTotalDeliveryHeroPaidAmount()));
		map.put("totalDeliveryHeroTips", NumberUtil.formatNumber(report.getTotalDeliveryHeroTips()));
		map.put("totalDeliveryHeroTotal", NumberUtil.formatNumber(report.getTotalDeliveryHeroPaidAmount()+report.getTotalDeliveryHeroTips()));
		map.put("totalTrans", NumberUtil.formatNumber(report.getTotalTrans()));
		map.put("totalPaidAmount", NumberUtil.formatNumber(report.getTotalPaidAmount()));
		map.put("totalTips", NumberUtil.formatNumber(report.getTotalTips()));
		map.put("grandTotal", NumberUtil.formatNumber(report.getTotalPaidAmount()+report.getTotalTips()));
		map.put("days", String.valueOf((int) ((toDate.getTime() - fromDate.getTime()) * (1.15740741 * Math.pow(10, -8))) + 1));
		
		JasperReport jasperReport = ReportUtil.getReport("total-report");
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, new JREmptyDataSource());
		JRViewer viewer = new JRViewer(jasperPrint);
		reportContainer.removeAll();
		reportContainer.add(viewer);
		reportContainer.revalidate();
		
	}
}
