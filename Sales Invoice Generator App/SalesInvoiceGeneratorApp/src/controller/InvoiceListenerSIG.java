/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import model.InvoiceModelSIG;
import model.InvoiceTableModelSIG;
import model.LineModelSIG;
import model.LineTableModelSIG;
import view.MainFrameSIG;
import view.NewInvoiceFrameSIG;
import view.NewLineFrameSIG;

public class InvoiceListenerSIG implements ActionListener, ListSelectionListener {

    private MainFrameSIG frame;
    private NewInvoiceFrameSIG newInvoiceFrame;
    private NewLineFrameSIG newLineFrame;

    public InvoiceListenerSIG(MainFrameSIG frame) {
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        switch (e.getActionCommand()) {
            case "Load File":
                loadFile(null, null);
                break;
            case "Save File":
                SaveFile();
                break;
            case "New":
                newInvoice();
                break;
            case "Delete":
                deleteInvoice();
                break;
            case "New Item":
                newItem();
                break;
            case "Delete Item":
                deleteItem();
                break;
            case "Create Header":
                createHeader();

                break;
            case "Cancel Header":
                cancelHeader();
                break;
            case "Ok":
                ok();
                break;
            case "Cancel Adding":
                cancelAdding();
                break;

        };
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        int selectedRow = frame.invTable.getSelectedRow();

        if (selectedRow > -1 && selectedRow < frame.getInvoices().size()) {

            InvoiceModelSIG inv = frame.getInvoices().get(selectedRow);

            frame.lblInvNum.setText("" + inv.getInvoiceNum());
            frame.lblInvDate.setText(MainFrameSIG.dFormat.format(inv.getDate()));
            frame.lblCusName.setText(inv.getCustomerName());
            frame.lblInvTotal.setText("" + inv.getTotalInvoice());

            List<LineModelSIG> lines = inv.getLines();
            frame.itemsTable.setModel(new LineTableModelSIG(lines));

        } else {
            frame.lblInvNum.setText("");
            frame.lblInvDate.setText("");
            frame.lblCusName.setText("");
            frame.lblInvTotal.setText("");

            frame.itemsTable.setModel(new LineTableModelSIG(new ArrayList<LineModelSIG>()));

        }
    }

    public void loadFile(String headerPath, String linePath) {

        frame.getInvoices().clear();

        File headerFile = null;
        File lineFile = null;
        if (headerPath == null) {
            JFileChooser fileChooser = new JFileChooser();
            // filter on csv files only
//            FileNameExtensionFilter filter = new FileNameExtensionFilter("", "csv");
//            fileChooser.setFileFilter(filter);
            int x = fileChooser.showOpenDialog(frame);
            if (x == JFileChooser.APPROVE_OPTION) {
                headerFile = fileChooser.getSelectedFile();
                x = fileChooser.showOpenDialog(frame);
                if (x == JFileChooser.APPROVE_OPTION) {
                    lineFile = fileChooser.getSelectedFile();
                } else {
                    JOptionPane.showMessageDialog(frame, "Second File Not Found Please Try Again Later", "Erorr Message", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "First File Not Found Please Try Again Later", "Erorr Message", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            headerFile = new File(headerPath);
            lineFile = new File(linePath);
        }
        if (headerFile != null && lineFile != null) {
            if (getExtension(lineFile).equals("csv") && getExtension(headerFile).equals("csv")) {
                try {

                    List<String> headerList = Files.lines(Paths.get(headerFile.getAbsolutePath())).collect(Collectors.toList());
                    List<String> lineList = Files.lines(Paths.get(lineFile.getAbsolutePath())).collect(Collectors.toList());

                    int v = 0;
                    int i = 0;
                    for (String headerSt : headerList) {
                        String[] row = headerSt.split(",");
                        String numString = row[0];
                        String dateString = row[1];
                        String customerName = row[2];

                        int num = Integer.parseInt(numString);
                        Date date = frame.dFormat.parse(dateString);

                        InvoiceModelSIG inv = new InvoiceModelSIG(num, date, customerName);
                        frame.getInvoices().add(inv);
                    }

                    for (String lineSt : lineList) {
                        String[] rowLine = lineSt.split(",");
                        String invNumString = rowLine[0];
                        String itemName = rowLine[1];
                        String itemPrice = rowLine[2];
                        String countString = rowLine[3];

                        int invNum = Integer.parseInt(invNumString);
                        int count = Integer.parseInt(countString);
                        double price = Double.parseDouble(itemPrice);

                        InvoiceModelSIG inv = getInvoiveByNum(invNum);
                        LineModelSIG line = new LineModelSIG(itemName, price, count, inv);
                        inv.getLines().add(line);
                        frame.invTable.setModel(new InvoiceTableModelSIG(frame.getInvoices()));

                    }

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(frame, "Wrong Date Format", "Erorr Message", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Please Choose a csv File", "Erorr Message", JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    private void SaveFile() {
        File headerFile = null;
        File lineFile = null;

        String invoicesData = "";
        String linesData = "";

        for (InvoiceModelSIG invoices : frame.getInvoices()) {
            invoicesData += invoices.toCSV();
            invoicesData += "\n";
            for (LineModelSIG line : invoices.getLines()) {
                linesData += line.toCSV();
                linesData += "\n";
            }
        }
        JFileChooser fileChooser = new JFileChooser();
        int x = fileChooser.showSaveDialog(frame);
        if (x == JFileChooser.APPROVE_OPTION) {
            headerFile = fileChooser.getSelectedFile();
            x = fileChooser.showSaveDialog(frame);
            if (x == JFileChooser.APPROVE_OPTION) {
                lineFile = fileChooser.getSelectedFile();
                if (getExtension(headerFile).equals("csv") && getExtension(lineFile).equals("csv")) {
                    try {
                        FileWriter fwHeader = new FileWriter(headerFile);
                        fwHeader.write(invoicesData);
                        fwHeader.flush();
                        fwHeader.close();
                        FileWriter fwLine = new FileWriter(lineFile);
                        fwLine.write(linesData);
                        fwLine.flush();
                        fwLine.close();
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(frame, "Error while saving", "Erorr Message", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Please Make Sure Files Format .CSV", "Erorr Message", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "File Not Found Please Try Again", "Erorr Message", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(frame, "File Not Found Please Try Again", "Erorr Message", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void newInvoice() {
        newInvoiceFrame = new NewInvoiceFrameSIG(frame);
        newInvoiceFrame.invNumtxt.setText(Integer.toString(getNextInvNum()));
        newInvoiceFrame.setVisible(true);

    }

    private void deleteInvoice() {
        int x = frame.invTable.getSelectedRow();
        if (x != -1) {
            frame.getInvoices().remove(x);
            ((InvoiceTableModelSIG) frame.invTable.getModel()).fireTableDataChanged();
        }

    }

    private void newItem() {
        newLineFrame = new NewLineFrameSIG(frame);
        newLineFrame.setVisible(true);
    }

    private void deleteItem() {
        int selectedRow = frame.itemsTable.getSelectedRow();
        if (selectedRow != -1) {
            int hRow = frame.invTable.getSelectedRow();
            LineTableModelSIG lineTableModel = (LineTableModelSIG) frame.itemsTable.getModel();
            lineTableModel.getLines().remove(selectedRow);
            lineTableModel.fireTableDataChanged();
            ((InvoiceTableModelSIG) frame.invTable.getModel()).fireTableDataChanged();
            frame.invTable.setRowSelectionInterval(hRow, hRow);

        }
    }

    private InvoiceModelSIG getInvoiveByNum(int num) {

        for (InvoiceModelSIG inv : frame.getInvoices()) {
            if (num == inv.getInvoiceNum()) {
                return inv;
            }
        }
        return null;
    }

    private void cancelHeader() {
        newInvoiceFrame.setVisible(false);
        newInvoiceFrame.dispose();
    }

    private void createHeader() {

        String dateStr = (String) newInvoiceFrame.datetxt.getText();
        String customerName = (String) newInvoiceFrame.cusNametxt.getText();
        newInvoiceFrame.setVisible(false);
        newInvoiceFrame.dispose();
        try {
            Date date = frame.dFormat.parse(dateStr);
            InvoiceModelSIG inv = new InvoiceModelSIG(getNextInvNum(), date, customerName);
            frame.getInvoices().add(inv);

        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(frame, "Erorr Date Format (dd-MM-yyy)", "Error", JOptionPane.ERROR_MESSAGE);
        }

        frame.invTable.setModel(new InvoiceTableModelSIG(frame.getInvoices()));

        ((InvoiceTableModelSIG) frame.invTable.getModel()).fireTableDataChanged();
    }

    private int getNextInvNum() {
        int num = 1;
        for (InvoiceModelSIG inv : frame.getInvoices()) {
            if (inv.getInvoiceNum() > num) {
                num = inv.getInvoiceNum();
            }
        }
        return num + 1;
    }

    private void ok() {
        int selectedRow = frame.invTable.getSelectedRow();
        if (selectedRow != -1) {
            String itemName = newLineFrame.itemNametxt.getText();
            String itemPrice = newLineFrame.itemPricetxt.getText();
            String itemCount = newLineFrame.counttxt.getText();

            newLineFrame.setVisible(false);
            newLineFrame.dispose();

            double price = Double.parseDouble(itemPrice);
            int count = Integer.parseInt(itemCount);

            InvoiceModelSIG inv = frame.getInvoices().get(selectedRow);

            LineModelSIG line = new LineModelSIG(itemName, price, count, inv);

            inv.getLines().add(line);
            frame.invTable.setModel(new InvoiceTableModelSIG(frame.getInvoices()));
            ((InvoiceTableModelSIG) frame.invTable.getModel()).fireTableDataChanged();
        }
    }

    private void cancelAdding() {
        newLineFrame.setVisible(false);
        newLineFrame.dispose();
    }

    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }

}
