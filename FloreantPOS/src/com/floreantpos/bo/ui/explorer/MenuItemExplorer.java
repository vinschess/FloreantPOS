package com.floreantpos.bo.ui.explorer;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JScrollPane;

import org.hibernate.criterion.Order;
import org.jdesktop.swingx.JXTable;

import com.floreantpos.POSConstants;
import com.floreantpos.bo.ui.BOMessageDialog;
import com.floreantpos.main.Application;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.MenuItemModifierGroup;
import com.floreantpos.model.MenuItemShift;
import com.floreantpos.model.dao.MenuItemDAO;
import com.floreantpos.swing.BeanTableModel;
import com.floreantpos.swing.TransparentPanel;
import com.floreantpos.ui.dialog.BeanEditorDialog;
import com.floreantpos.ui.dialog.ConfirmDeleteDialog;
import com.floreantpos.ui.model.MenuItemForm;
import com.floreantpos.util.POSUtil;

public class MenuItemExplorer extends TransparentPanel {

	private JXTable table;
	private BeanTableModel<MenuItem> tableModel;

	public MenuItemExplorer() {
		tableModel = new BeanTableModel<MenuItem>(MenuItem.class);
		tableModel.addColumn(POSConstants.ID.toUpperCase(), "id");
		tableModel.addColumn(POSConstants.NAME.toUpperCase(), "name");
		tableModel.addColumn(POSConstants.TRANSLATED_NAME.toUpperCase(), "translatedName");
		tableModel.addColumn(POSConstants.PRICE.toUpperCase() + " (" + Application.getCurrencySymbol() + ")", "price");
		tableModel.addColumn(POSConstants.VISIBLE.toUpperCase(), "visible");
		tableModel.addColumn(POSConstants.DISCOUNT.toUpperCase() + "(%)", "discountRate");
		tableModel.addColumn(POSConstants.FOOD_GROUP.toUpperCase(), "parent");
		tableModel.addColumn(POSConstants.TAX.toUpperCase(), "tax");
		tableModel.addColumn(POSConstants.SORT_ORDER.toUpperCase(), "sortOrder");
		tableModel.addColumn(POSConstants.BUTTON_COLOR.toUpperCase(), "buttonColor");

		tableModel.addRows(MenuItemDAO.getInstance().findAll());
		
		table = new JXTable(tableModel);

		setLayout(new BorderLayout(5, 5));
		add(new JScrollPane(table));
		
		add(createButtonPanel(), BorderLayout.SOUTH);
	}

	private TransparentPanel createButtonPanel() {
		ExplorerButtonPanel explorerButton = new ExplorerButtonPanel();
		JButton editButton = explorerButton.getEditButton();
		JButton addButton = explorerButton.getAddButton();
		JButton deleteButton = explorerButton.getDeleteButton();
		JButton copyButton = explorerButton.getCopyButton();

		editButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int index = table.getSelectedRow();
					if (index < 0)
						return;

					index = table.convertRowIndexToModel(index);
					
					MenuItem menuItem = tableModel.getRow(index);
					menuItem = MenuItemDAO.getInstance().initialize(menuItem);
					
					tableModel.setRow(index, menuItem);
					MenuItemForm editor = new MenuItemForm(menuItem, true);
					editor.setEditMode(true);
					BeanEditorDialog dialog = new BeanEditorDialog(editor);
					dialog.open();
					if (dialog.isCanceled())
						return;

					table.repaint();
				} catch (Throwable x) {
					BOMessageDialog.showError(POSConstants.ERROR_MESSAGE, x);
				}
			}

		});
		
		copyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int index = table.getSelectedRow();
					if (index < 0)
						return;

					index = table.convertRowIndexToModel(index);
					
					MenuItem menuItem = tableModel.getRow(index);
					menuItem = MenuItemDAO.getInstance().initialize(menuItem);
					
					MenuItem copiedMenuItem = (MenuItem) POSUtil.deepClone(menuItem);
					// Find last id
					List<MenuItem> menuItems = MenuItemDAO.getInstance().findAll(Order.desc(MenuItem.PROP_ID));
					if(menuItems != null && menuItems.size()>0)
						copiedMenuItem.setId(menuItems.get(0).getId()+1);
					else
						copiedMenuItem.setId(1);
					
					List<MenuItemModifierGroup> groups = copiedMenuItem.getMenuItemModiferGroups();
					List<MenuItemModifierGroup> newGroups = new ArrayList<MenuItemModifierGroup>();
					if(groups != null && groups.size()>0){
						for(MenuItemModifierGroup group : groups) {
							group.setId(null);
							group.setParentMenuItem(copiedMenuItem.getId());
							newGroups.add(group);
						}
					}
					copiedMenuItem.setMenuItemModiferGroups(newGroups);
					
					List<MenuItemShift> shifts = copiedMenuItem.getShifts();
					List<MenuItemShift> newShifts = new ArrayList<MenuItemShift>();
					if(shifts != null && shifts.size()>0){
						for(MenuItemShift shift : shifts) {
							shift.setId(null);
							newShifts.add(shift);
						}
					}
					copiedMenuItem.setShifts(newShifts);
					
					
					MenuItemForm editor = new MenuItemForm(copiedMenuItem);
					editor.setEditMode(false);
					BeanEditorDialog dialog = new BeanEditorDialog(editor, com.floreantpos.POSConstants.COPY_MENU_ITEM);
					dialog.open();
					if (dialog.isCanceled())
						return;
					
					MenuItem foodItem = (MenuItem) editor.getBean();
					table.repaint();
					tableModel.addRow(foodItem);
				} catch (Throwable x) {
					BOMessageDialog.showError(POSConstants.ERROR_MESSAGE, x);
				}
			}

		});

		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					MenuItemForm editor = new MenuItemForm();
					editor.setEditMode(false);
					BeanEditorDialog dialog = new BeanEditorDialog(editor);
					dialog.open();
					if (dialog.isCanceled())
						return;
					MenuItem foodItem = (MenuItem) editor.getBean();
					tableModel.addRow(foodItem);
				} catch (Throwable x) {
					BOMessageDialog.showError(POSConstants.ERROR_MESSAGE, x);
				}
			}

		});

		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int index = table.getSelectedRow();
					if (index < 0)
						return;
					
					index = table.convertRowIndexToModel(index);

					if (ConfirmDeleteDialog.showMessage(MenuItemExplorer.this, POSConstants.CONFIRM_DELETE, POSConstants.DELETE) != ConfirmDeleteDialog.NO) {
						MenuItem item = tableModel.getRow(index);
						MenuItemDAO foodItemDAO = new MenuItemDAO();
						foodItemDAO.delete(item);
						
						tableModel.removeRow(index);
					}
				} catch (Throwable x) {
					BOMessageDialog.showError(POSConstants.ERROR_MESSAGE, x);
				}
			}

		});

		TransparentPanel panel = new TransparentPanel();
		panel.add(addButton);
		panel.add(copyButton);
		panel.add(editButton);
		panel.add(deleteButton);
		return panel;
	}
}
