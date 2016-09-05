package com.floreantpos.entrytool;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;

import com.floreantpos.POSConstants;
import com.floreantpos.main.Application;
import com.floreantpos.model.CashTransaction;
import com.floreantpos.model.Customer;
import com.floreantpos.model.DeliveryHeroTransaction;
import com.floreantpos.model.EatNowTransaction;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.MenuItemModifierGroup;
import com.floreantpos.model.MenuLogTransaction;
import com.floreantpos.model.MenuModifier;
import com.floreantpos.model.OrderType;
import com.floreantpos.model.PaymentType;
import com.floreantpos.model.PosTransaction;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TicketItemModifier;
import com.floreantpos.model.TicketItemModifierGroup;
import com.floreantpos.model.dao.CustomerDAO;
import com.floreantpos.model.dao.MenuItemDAO;
import com.floreantpos.model.dao.MenuModifierDAO;
import com.floreantpos.report.ReceiptPrintService;
import com.floreantpos.services.PosTransactionService;


/**
 * 
 * @author Vinay Gupta
 *
 */
public class ParseReceipt {
	
	private static Logger logger = Logger.getLogger(ParseReceipt.class);
	
	public static void ReadFile(String fileName) throws Exception{
		
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		
		BufferedReader br = null;
		System.out.println(fileName);
		try {
			
			//Object to make entry in DB
			PosTransaction transaction = null;
			
			Ticket ticket = new Ticket();					
			
			MenuItem menuItem = null;			
			MenuItemDAO menuItemDao = new MenuItemDAO();
			
			MenuModifier menuModifier = null;
			MenuModifierDAO menuModifierDao = new MenuModifierDAO();
			
			MenuItemModifierGroup menuItemModifierGroup = null;
			
			Customer customer = new Customer();
			
			
			//Object for this method
			boolean isSiteNameRead = false;
			
			String sCurrentLine;
			
			Integer itemCode = null;
			Integer qty = null;
			

			br = new BufferedReader(new FileReader(fileName));

			while ((sCurrentLine = br.readLine()) != null) {
				sCurrentLine = sCurrentLine.replaceAll("\\x00", "").trim().toUpperCase();
				
				/*
				 * Read site name
				 */
				
				if(!isSiteNameRead){
					try{
						if(!sCurrentLine.equals("")){
							if(sCurrentLine.contains(POSConstants.ENTRYTOOL_MENULOG)){
								System.out.println("Transaction type : MENULOG");
								transaction = new MenuLogTransaction();
								transaction.setPaymentType(PaymentType.MENULOG.name());
							}else if(sCurrentLine.contains(POSConstants.ENTRYTOOL_EATNOW)){
								System.out.println("Transaction type : EATNOW");
								transaction = new EatNowTransaction();
								transaction.setPaymentType(PaymentType.EATNOW.name());
							}else if(sCurrentLine.contains(POSConstants.ENTRYTOOL_DELIVERYHERO)){
								System.out.println("Transaction type : DELIVERYHERO");
								transaction = new DeliveryHeroTransaction();
								transaction.setPaymentType(PaymentType.DELIVERY_HERO.name());
							} else{
								throw new Exception("Site didn't find");
							}
							isSiteNameRead = true;
						}else{
							continue;
						}
					}catch(Exception e){
						e.printStackTrace();
						e.printStackTrace(pw);
						logger.error("Error : "+sw);
					}
				}
				
				
				/*
				 * Order type TAKE_OUT or HOME_DELIVERY
				 */
				
				if(sCurrentLine.contains(POSConstants.ENTRYTOOL_ORDER_TYPE)){
					
					String orderType = sCurrentLine.substring(POSConstants.ENTRYTOOL_ORDER_TYPE.length()+1).trim();
					if(orderType.equalsIgnoreCase(POSConstants.ENTRYTOOL_ORDER_PICKUP)){
						
						System.out.println("Order Type : TAKE_OUT");
						ticket.setType(OrderType.TAKE_OUT);
					} else if(orderType.equalsIgnoreCase(POSConstants.ENTRYTOOL_ORDER_DELIVERY)){
					
						System.out.println("Order Type : HOME_DELIVERY");
						ticket.setType(OrderType.HOME_DELIVERY);
					}
				}
				
				
				/*
				 * If payment done by CASH
				 */
				if(sCurrentLine.contains(POSConstants.ENTRYTOOL_PAYMENTMETHOD)){
					
					while((sCurrentLine = br.readLine().replaceAll("\\x00", "").trim().toUpperCase()).equals(""));  // read until some text gets
					
					if(sCurrentLine.equalsIgnoreCase(POSConstants.ENTRYTOOL_CASH)){
						System.out.println("Pay through CASH, so Transaction type : CASH");
						transaction = new CashTransaction();
						transaction.setPaymentType(PaymentType.CASH.name());
					}
						
				}
				
				/*
				 * Delivery Address if Order Type is HOME_DELIVERY
				 */
				if(ticket.getType()!=null && ticket.getType().name().equalsIgnoreCase(OrderType.HOME_DELIVERY.name()) &&
						sCurrentLine.contains(POSConstants.ENTRYTOOL_DELIVERY_ADDRESS)){
				
					String address = null;
					String zipCode = null;
					String city = null;
					String state = null;
					String country = null;
					
					while(!(sCurrentLine = br.readLine().replaceAll("\\x00", "").trim()).toUpperCase().contains(POSConstants.ENTRYTOOL_CUSTOMER_DETAIL)){
						
						if(sCurrentLine.equals(""))
							continue;
						
						else if(address==null)
							address = sCurrentLine;
						
						else if(city==null){
							
							city = sCurrentLine.substring(0, sCurrentLine.indexOf(",")).trim();
							zipCode = sCurrentLine.substring(sCurrentLine.indexOf(",")+1).trim();
							try{
								Integer.parseInt(zipCode);
							} catch(NumberFormatException ne){
								logger.error("ZipCode : " + zipCode +" is not a number. Please check this.");
								System.err.println("ZipCode : " + zipCode +" is not a number. Please check this.");
								zipCode = null;
							}
						}
						
						else if(state==null){
							
							state = sCurrentLine.substring(0, sCurrentLine.indexOf(",")).trim();
							country = sCurrentLine.substring(sCurrentLine.indexOf(",")+1).trim();
							
						}
						
					}
							
					address = address + ", "+city + ", "+zipCode;
					customer.setAddress(address);
					customer.setCity(city);
					customer.setCountry(country);
					customer.setState(state);
					customer.setZipCode(zipCode);
				}
			
			/*
			 * Delivery Address if Order Type is HOME_DELIVERY
			 */
			if((sCurrentLine=sCurrentLine.toUpperCase()).contains(POSConstants.ENTRYTOOL_CUSTOMER_DETAIL)){
				
				String customerName = null;
				String mobileNumber = null;
				while(!(sCurrentLine = br.readLine().replaceAll("\\x00", "").trim()).toUpperCase().contains(POSConstants.ENTRYTOOL_ORDER_SUMMARY)){
					if(sCurrentLine.equals(""))
						continue;
					
					else if(sCurrentLine.toUpperCase().contains(POSConstants.ENTRYTOOL_NAME_START))
						customerName = sCurrentLine.substring(POSConstants.ENTRYTOOL_NAME_START.length()).trim();
					
					else if(sCurrentLine.toUpperCase().contains(POSConstants.ENTRYTOOL_MOBILE_START)){
						
						mobileNumber = sCurrentLine.substring(POSConstants.ENTRYTOOL_MOBILE_START.length()).trim();
						try{
							Integer.parseInt(mobileNumber);
						} catch(NumberFormatException ne){
							logger.error("mobileNumber : " + mobileNumber +" should be a number.");
							System.err.println("mobileNumber : " + mobileNumber +" should be a number.");
							mobileNumber = null;
						}
					}
					
				}
						
				System.out.println("Saving customer details...");
				customer.setName(customerName);
				customer.setTelephoneNo(mobileNumber);
				try{
					
					CustomerDAO customerDAO = CustomerDAO.getInstance();
					List<Customer> customers = customerDAO.findBy(mobileNumber, null, customerName);
					boolean isSaveRequire = true;
					
					if(ticket.getType()!=null && ticket.getType().name().equalsIgnoreCase(OrderType.HOME_DELIVERY.name())){
						
						for(Customer c : customers){
							
							if(c.getName()!=null && c.getName().equalsIgnoreCase(customerName)
									&& c.getTelephoneNo()!=null && c.getTelephoneNo().equalsIgnoreCase(mobileNumber)
									&& c.getAddress()!=null && c.getAddress().equalsIgnoreCase(customer.getAddress())){
								
								System.out.println("Customer information already present.");
								logger.debug("Customer information already present.");
								isSaveRequire = false;
								break;
							}
						}
						
					} else if(ticket.getType()!=null && ticket.getType().name().equalsIgnoreCase(OrderType.TAKE_OUT.name())){
						
						for(Customer c : customers){
							
							if(c.getName()!=null && c.getName().equalsIgnoreCase(customerName)
									&& c.getTelephoneNo()!=null && c.getTelephoneNo().equalsIgnoreCase(mobileNumber)){
								
								System.out.println("Customer information already present.");
								logger.debug("Customer information already present.");
								isSaveRequire = false;
								break;
							}
						}
						
					}
					
					if(isSaveRequire)
						customerDAO.saveOrUpdate(customer);
					
				}catch(Exception ex){
					ex.printStackTrace();
					ex.printStackTrace(pw);
					System.err.println("No able to save customer details!!!"+sw);
					logger.error("No able to save customer details!!!");
				}
				
			}
				
				/*
				 * Record all items
				 */
				if(sCurrentLine.equalsIgnoreCase(POSConstants.ENTRYTOOL_FOODITEMS)){
					
					
					while((sCurrentLine = br.readLine().replaceAll("\\x00", "").trim().toUpperCase())!=null &&
							!sCurrentLine.equalsIgnoreCase(POSConstants.ENTRYTOOL_DISCOUNTINFORMATION)){
						
						if(sCurrentLine.contains("{") && sCurrentLine.contains("}")){
							
							itemCode = Integer.parseInt((sCurrentLine.substring(sCurrentLine.indexOf("{")+1, sCurrentLine.indexOf("}")).trim()));
							
							/*
							 * Getting Menu Item, Menu Group and Menu Category
							 */
							menuItem = menuItemDao.get(itemCode);
							menuItem = menuItemDao.initialize(menuItem);

							TicketItem ticketItem = menuItem.convertToTicketItem();

							qty = Integer.parseInt(sCurrentLine.substring(0,sCurrentLine.indexOf(" ")));
							ticketItem.setItemCount(qty);
							
							/*
							 * Read MENU MODIFIERS
							 */
							while((sCurrentLine = br.readLine().replaceAll("\\x00", "").trim().toUpperCase())!=null &&
									!sCurrentLine.contains(POSConstants.ENTRYTOOL_MENUMODIFIER_STOP)){
								
								if(sCurrentLine.startsWith("*")){
									
									int indexOfStar = sCurrentLine.indexOf("*");
									int indexOfDollar = -1;
									if(sCurrentLine.contains("$"))
										indexOfDollar = sCurrentLine.indexOf("$");
									
									String menuModifierName = null;
									if(indexOfDollar!=-1)
										menuModifierName = sCurrentLine.substring(indexOfStar+1,indexOfDollar).trim();
									else
										menuModifierName = sCurrentLine.substring(indexOfStar+1).trim();
									menuModifier = menuModifierDao.getMenuModifierUsingName(menuModifierName).get(0);
									
									List<MenuItemModifierGroup> menuItemModifierGroups = menuItem.getMenuItemModiferGroups();
									boolean isMatchMenuItemModifier = false;
									
									for(MenuItemModifierGroup group : menuItemModifierGroups){
										if(group.getModifierGroup().getId().equals(menuModifier.getModifierGroup().getId())){
											menuItemModifierGroup = group;
											isMatchMenuItemModifier = true;
											menuModifier.setMenuItemModifierGroup(menuItemModifierGroup);
											break;
										}
									}
									if(isMatchMenuItemModifier && menuModifier!=null){
										TicketItemModifierGroup ticketItemModifierGroup = ticketItem.findTicketItemModifierGroup(menuModifier, true);
										int modifierCount = ticketItemModifierGroup.countItems(true);
										int maxModifier = ticketItemModifierGroup.getMaxQuantity();

										TicketItemModifier ticketItemModifier = ticketItemModifierGroup.findTicketItemModifier(menuModifier);

										if (ticketItemModifier == null) {
											TicketItemModifier m = ticketItemModifierGroup.addTicketItemModifier(menuModifier,
													modifierCount >= maxModifier ? TicketItemModifier.EXTRA_MODIFIER : TicketItemModifier.NORMAL_MODIFIER);
											
										}else{

											int modifierType = TicketItemModifier.MODIFIER_NOT_INITIALIZED;
											if (ticketItemModifier.getModifierType() != null) {
												modifierType = ticketItemModifier.getModifierType().intValue();
											}
											switch (modifierType) {
												case TicketItemModifier.MODIFIER_NOT_INITIALIZED:
													ticketItemModifier.setItemCount(ticketItemModifier.getItemCount() + 1);
													ticketItemModifier.setModifierType(TicketItemModifier.NORMAL_MODIFIER);
													break;
	
												case TicketItemModifier.NORMAL_MODIFIER:
												case TicketItemModifier.EXTRA_MODIFIER:
													ticketItemModifier.setItemCount(ticketItemModifier.getItemCount() + 1);
													break;
											}
										}
									}
								}
								
							}
							ticket.addToticketItems(ticketItem);
							ticketItem.setTicket(ticket);
						}
						
						
					}
					
					Application application = Application.getInstance();
					ticket.setPriceIncludesTax(application.isPriceIncludesTax());
					ticket.setTerminal(application.getTerminal());
					ticket.setOwner(Application.getCurrentUser());
					ticket.setShift(application.getCurrentShift());
					
					Calendar currentTime = Calendar.getInstance();
					ticket.setCreateDate(currentTime.getTime());
					ticket.setCreationHour(currentTime.get(Calendar.HOUR_OF_DAY));
					ticket.getDeliveryDate();
					ticket.calculatePrice();
					
					transaction.setTicket(ticket);
					transaction.setCaptured(true);
					transaction.setAmount(ticket.getDueAmount());
					transaction.setTenderAmount(transaction.getAmount());
					
					PosTransactionService transactionService = PosTransactionService.getInstance();
					transactionService.settleTicket(ticket, transaction);
					
					/*
					 * Create Kitchen Tickets
					 */
					ReceiptPrintService.printToKitchenForMyMoniotr(ticket);
				}
				
				
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

	}

}
