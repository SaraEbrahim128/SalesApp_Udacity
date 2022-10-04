
package model;

import java.util.ArrayList;
import java.util.Date;
import view.MainFrameSIG;


public class InvoiceModelSIG {
    private int invoiceNumSIG;
    private Date date;
    private String customerName;
    private ArrayList<LineModelSIG> lines;

    public InvoiceModelSIG(int invoiceNumSIG, Date date, String customerName) {
        this.invoiceNumSIG = invoiceNumSIG;
        this.date = date;
        this.customerName = customerName;
    }

    public int getInvoiceNum() {
        return invoiceNumSIG;
    }

    public void setInvoiceNum(int invoiceNumSIG) {
        this.invoiceNumSIG = invoiceNumSIG;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public ArrayList<LineModelSIG> getLines() {
        if(lines == null){
        lines=new ArrayList<>();
        }
        return lines;
    }

    public void setLines(ArrayList<LineModelSIG> lines) {
        this.lines = lines;
    }

    public double getTotalInvoice(){
        double total = 0.0;
        for(LineModelSIG line:getLines()){
        total += line.getTotalLine();
        }
        return total;
    }
    public String toCSV(){
        return invoiceNumSIG+","+MainFrameSIG.dFormat.format(date)+","+customerName;
    }
}


