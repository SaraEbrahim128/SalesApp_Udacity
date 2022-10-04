/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

/**
 *
 * @author dell
 */
public class NewLineFrameSIG extends JDialog {

    private JLabel itemNamelbl;
    private JLabel ItemPricelbl;
    private JLabel countlbl;

    public JTextField itemNametxt;
    public JTextField itemPricetxt;
    public JTextField counttxt;

    private JButton okbtn;
    private JButton cancelbtn;

    public NewLineFrameSIG(MainFrameSIG frameUD) {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(4, 2));

        itemNamelbl = new JLabel("  Item Name :");
        ItemPricelbl = new JLabel("  Item Price :");
        countlbl = new JLabel("  Count :");
        
        itemNametxt = new JTextField(20);
        itemPricetxt = new JTextField(20);
        counttxt = new JTextField(20);

        okbtn = new JButton("Ok");
        cancelbtn = new JButton("Cancel");

        okbtn.addActionListener(frameUD.getMyListner());
        cancelbtn.addActionListener(frameUD.getMyListner());
        okbtn.setActionCommand("Ok");
        cancelbtn.setActionCommand("Cancel Adding");
        
        
        add(itemNamelbl);
        add(itemNametxt);
        add(ItemPricelbl);
        add(itemPricetxt);
        add(countlbl);
        add(counttxt);
        add(okbtn);
        add(cancelbtn);
        
        setModal(true);
        setLocation(200, 100);
        pack();
    }

}
