/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import view.MainFrameSIG;

/**
 *
 * @author dell
 */
public class InvoiceTableModelSIG extends AbstractTableModel{
    private String[] col={"Invoice Number","Date","Customer Name","Total"};
    private List<InvoiceModelSIG> invoices;
    
    public InvoiceTableModelSIG(List<InvoiceModelSIG> invoices){
        this.invoices=invoices;
    }

    @Override
    public int getRowCount() {
        return invoices.size();
    }

    @Override
    public int getColumnCount() {
        return col.length;
    }

    @Override
    public String getColumnName(int column) {
        return col[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        InvoiceModelSIG inv = invoices.get(rowIndex);
        switch(columnIndex){
        case 0: return inv.getInvoiceNum();
        case 1: return MainFrameSIG.dFormat.format(inv.getDate());
        case 2: return inv.getCustomerName();
        case 3: return inv.getTotalInvoice();
                }
        return "";
    }
    
}
