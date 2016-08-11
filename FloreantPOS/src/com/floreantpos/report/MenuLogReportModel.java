package com.floreantpos.report;

import java.util.List;

import com.floreantpos.bo.ui.explorer.ListTableModel;
import com.floreantpos.model.MenuLogTransaction;
import com.floreantpos.util.NumberUtil;

public class MenuLogReportModel extends ListTableModel<MenuLogTransaction> {
	
	public MenuLogReportModel(List<MenuLogTransaction> datas) {
		super(new String[] {"ticketId", "subtotal", "tips", "total"}, datas);
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		MenuLogTransaction transaction = getRowData(rowIndex);
		
		switch (columnIndex) {
			case 0:
				return String.valueOf(transaction.getTicket().getId());
				
			/*case 1:
				return transaction.getCardType();
				
			case 2:
				return transaction.getCardAuthCode();*/
				
			case 1:
				return NumberUtil.formatNumber(transaction.getAmount() - transaction.getTipsAmount());
				
			case 2:
				return NumberUtil.formatNumber(transaction.getTipsAmount());
				
			case 3:
				return NumberUtil.formatNumber(transaction.getAmount());
		}
		
		return null;
	}

}
