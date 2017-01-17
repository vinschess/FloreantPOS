package com.floreantpos.demo;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.model.KitchenTicket;

public class KitchenTicketListPanel extends JPanel {
	private Set<KitchenTicket> existingTickets = new HashSet<KitchenTicket>();
	private KitchenTicket subTicket;
	
	public KitchenTicketListPanel() {
		super(new MigLayout("filly"));
	}

	public boolean addTicket(KitchenTicket ticket, KitchenDisplayView kitchenDisplayView) {
		if(existingTickets.contains(ticket)) {
			return false;
		}
		
		existingTickets.add(ticket);
		
		/**
		 *@author vinay.gupta
		 *
		 * Adding new table for same ticket if items are more than specified total items
		 */
		//int breakPoint = Integer.parseInt(POSConstants.KITCHEN_BREAK_POINT);
		JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
		BorderLayout layout = (BorderLayout) kitchenDisplayView.getLayout();
		
		/*
		 * 1st 100 --> Horizontal Scroll Bar Size
		 * 2nd 100 --> DONE and VOID button space
		 * 3rd 100 --> Spaces between components
		 * 4th 50 --> Row height of ticket table 
		 */
		int breakPoint = (int) ((topFrame.getHeight()-100-100-100)/50);
		int totalSubTable = (ticket.getTicketItems().size()-1)/breakPoint;
		if(totalSubTable >= 1){
			createSubTicketsToDisplay(totalSubTable, breakPoint, ticket);
		}else{
			super.add(new KitchenTicketView(ticket, true, null, null), "growy, width pref!");
		}
		
		return true;
	}
	
	@Override
	public void remove(Component comp) {
		if(comp instanceof KitchenTicketView) {
			KitchenTicketView view = (KitchenTicketView) comp;
			existingTickets.remove(view.getTicket());
		}
		
		super.remove(comp);
	}
	
	@Override
	public void removeAll() {
		existingTickets.clear();
		
		Component[] components = getComponents();
		for (Component component : components) {
			if (component instanceof KitchenTicketView) {
				KitchenTicketView kitchenTicketView = (KitchenTicketView) component;
				kitchenTicketView.stopTimer();
			}
		}
		
		super.removeAll();
	}
	
	private void createSubTicketsToDisplay(int totalSubTable, int breakPoint, KitchenTicket ticket){
		List<KitchenTicketView> kitchenTicketViews = new ArrayList<KitchenTicketView>();
		for(int i=0 ; i<=totalSubTable ; i++){
			int listFromIndex = i*breakPoint;
			int listToIndex = (i==totalSubTable)?ticket.getTicketItems().size():(i+1)*breakPoint;
			
			List<com.floreantpos.model.KitchenTicketItem> ticketItems = ticket.getTicketItems().subList(listFromIndex, listToIndex);
			subTicket = new KitchenTicket(ticket);
			subTicket.setTicketItems(ticketItems);
			if(i==totalSubTable){
				super.add(new KitchenTicketView(ticket, true, subTicket,kitchenTicketViews), "growy, width pref!");
			}else{
				KitchenTicketView kitchenTicketView = new KitchenTicketView(ticket, false, subTicket, null);
				kitchenTicketViews.add(kitchenTicketView);
				super.add(kitchenTicketView, "growy, width pref!");
			}
		}
	}
}
