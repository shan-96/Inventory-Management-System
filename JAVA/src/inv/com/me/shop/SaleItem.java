/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inv.com.me.shop;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/* @author shantanu */
public class SaleItem extends javax.swing.JFrame {

    String myPhone = "";
    String myTin = "";
    float vatPercentage = 1;
    float oldMetalCost = 0;
    /**
     * Creates new form SaleItem
     */
    /*
     new javax.swing.table.DefaultTableModel(
     new Object [][] {

     },
     new String [] {
     "Sr No.", "Item Name", "Quantity", "Caret", "Price/Unit", "Unit Of Measurement", "Labout Cost / Unit", "Total"
     }
     )
     */
    JSONArray array = new JSONArray();
    float finalBill = 0;
    int index;
    boolean noItem = false;
    DefaultTableModel tableModel = new DefaultTableModel();

    DefaultTableModel stockItemModel;
    String dispUnit[];
    String availableQty[];
    String weight;
    String salePrice[];
    String itemCarret[];
    String stockID[];
    String s[];
    String selectedId;
    int selected = 0;

    //new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" })
    public SaleItem() {
        initComponents();
        this.getContentPane().setBackground(java.awt.Color.decode("#0041C2"));
        oldMetalPanel.setVisible(false);

        dicountText.setVisible(false);
        discountField.setVisible(false);
        JSONArray parray = JsonClass.getJsonArray(Constants.PREFERENCES_FILE);
        for (int i = 0; i < parray.size(); i++) {
            JSONParser parser = new JSONParser();
            try {
                Object o = parser.parse(parray.get(i).toString());
                JSONObject obj = (JSONObject) o;
                myPhone = obj.get("phone").toString();
                myTin = obj.get("tin").toString();
                vatPercentage = Float.parseFloat(obj.get("vat").toString());
            } catch (ParseException ex) {
                Logger.getLogger(SaleItem.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //System.out.println(myPhone+"    "+myTin+"end");

        File filePath = new File(Constants.STOCK_FILE);
        if (filePath.exists()) {
            JSONParser parser = new JSONParser();

            JSONArray array = JsonClass.getJsonArray(Constants.STOCK_FILE);
            s = new String[array.size()];
            dispUnit = new String[array.size()];
            availableQty = new String[array.size()];      //remaining  
            salePrice = new String[array.size()];
            itemCarret = new String[array.size()];
            stockID = new String[array.size()];

            for (int i = 0; i < array.size(); i++) {
                try {
                    Object o = parser.parse(array.get(i).toString());
                    JSONObject obj = (JSONObject) o;
                    //model.addRow(new Object[] {obj.get("id"),obj.get("name"),obj.get("purchasePrice"),obj.get("salePrice"), obj.get("unit")});
                    s[i] = obj.get("name").toString();

                    dispUnit[i] = obj.get("unit").toString();
                    availableQty[i] = obj.get("remaining").toString();
                    salePrice[i] = obj.get("salePrice").toString();
                    itemCarret[i] = obj.get("carret").toString();
                    stockID[i] = obj.get("id").toString();
                } catch (ParseException ex) {
                    Logger.getLogger(ManageItems.class.getName()).log(Level.SEVERE, null, ex);
                }

                //model.addRow(new Object[] {"one","two","three","four","five"});
            }
            //model = new DefaultComboBoxModel(s);
            availableTv.setText(availableQty[index]);
            salePriceField.setText(salePrice[index]);
            //unitTv.setText(dispUnit[index]);
            unitTv.setText(dispUnit[index]);
            unitSaleField.setText("/ " + dispUnit[index]);
            labourCostFieldUnit.setText("/ " + dispUnit[index]);
            SaleItemCarret.setText(itemCarret[index]);
            fillItemTable();

        } else {
            //String s[] = {"No item in the stock"};
            noItem = true;

        }
        
        jTable2.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (jTable2.getSelectedRow() != -1) {
                    itemNameTv.setText(jTable2.getValueAt(jTable2.getSelectedRow(), 1).toString());
                    selected = 1;
                    String selectedId = jTable2.getValueAt(jTable2.getSelectedRow(), 0).toString();
                    saleItemQuantity.setText("");
                    LabourCostField.setText("");

                    //index = SaleItemName.getSelectedIndex();
                    for (int i = 0; i < stockID.length; i++) {
                        if (stockID[i].equals(selectedId)) {
                            index = i;
                            break;
                        }
                    }

                    if (new File(Constants.STOCK_FILE).exists()) {
                        availableTv.setText(availableQty[index]);
                        salePriceField.setText(salePrice[index]);
                        unitTv.setText(dispUnit[index]);
                        unitSaleField.setText("/ " + dispUnit[index]);
                        labourCostFieldUnit.setText("/ " + dispUnit[index]);
                        SaleItemCarret.setText(itemCarret[index]);
                        setWeight(selectedId);
                    }
                }
            }
        });
        setVisible(true);
    }

    public void setWeight(String id) {
        JSONArray array = JsonClass.getJsonArray(Constants.PURCHASE_RECORD_FILE);
        for (int i = 0; i < array.size(); i++) {
            JSONObject obj = (JSONObject) array.get(i);
            String cid = obj.get("stockID").toString();
            if (cid.equals(id)) {
                weight = obj.get("quantity").toString();
                weightTv.setText(weight);
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        jTextField1 = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        availableTv = new javax.swing.JLabel();
        salePriceField = new javax.swing.JTextField();
        BillDate = new datechooser.beans.DateChooserCombo();
        unitTv = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        SaleItemCarret = new javax.swing.JTextField();
        unitSaleField = new javax.swing.JLabel();
        LabourCostField = new javax.swing.JTextField();
        labourCostFieldUnit = new javax.swing.JLabel();
        saleItemQuantity = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        itemNameTv = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        weightTv = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        CustomerNameField = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        CustomerPhoneField = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        CustomerAddField = new javax.swing.JTextArea();
        jLabel11 = new javax.swing.JLabel();
        finalBillTv = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        totalItemTv = new javax.swing.JLabel();
        discountCombo = new javax.swing.JCheckBox();
        discountField = new javax.swing.JTextField();
        dicountText = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        oldMetalPanel = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        oldMetalName = new javax.swing.JTextField();
        oldMetalWeight = new javax.swing.JTextField();
        oldMetalRate = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        oldMetalCheckBox = new javax.swing.JCheckBox();

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jLabel1.setText("jLabel1");

        jTextField1.setText("jTextField1");

        jLabel19.setText("jLabel19");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Sale Item");
        setLocation(new java.awt.Point(270, 20));
        setResizable(false);

        jPanel2.setBackground(new java.awt.Color(153, 153, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Sale Item", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 3, 18))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setText("Date");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel3.setText("Item Name");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel4.setText("Pieces");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel5.setText("Available Quantity");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel6.setText("Sale Price");

        availableTv.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        availableTv.setText("0");

        salePriceField.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        BillDate.setCalendarPreferredSize(new java.awt.Dimension(350, 250));

        unitTv.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        unitTv.setText("N/A");

        jButton1.setFont(new java.awt.Font("Tahoma", 2, 18)); // NOI18N
        jButton1.setText("Add Item To Bill");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel17.setText("Labout Cost/Unit");

        jLabel18.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel18.setText("Carret");

        SaleItemCarret.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        unitSaleField.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        unitSaleField.setText("/ Unit");

        LabourCostField.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        labourCostFieldUnit.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        labourCostFieldUnit.setText("/ Unit");

        saleItemQuantity.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel12.setText("Weight");

        jTable2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        stockItemModel = new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Stock Id", "Item Name"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        };
        jTable2.setModel(stockItemModel);
        jTable2.setRowHeight(40);
        jTable2.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(jTable2);
        if (jTable2.getColumnModel().getColumnCount() > 0) {
            jTable2.getColumnModel().getColumn(0).setMinWidth(150);
            jTable2.getColumnModel().getColumn(0).setPreferredWidth(150);
            jTable2.getColumnModel().getColumn(0).setMaxWidth(150);
        }

        itemNameTv.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        itemNameTv.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                itemNameTvKeyReleased(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel7.setText("pieces");

        weightTv.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        weightTv.setText("wt");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jButton1)
                                .addGap(30, 30, 30))
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(availableTv, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jLabel18))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(SaleItemCarret, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(BillDate, javax.swing.GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE)
                                        .addComponent(itemNameTv)))
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(weightTv, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(saleItemQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(unitTv, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                            .addComponent(jLabel17)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(LabourCostField, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(salePriceField, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(unitSaleField)
                                        .addComponent(labourCostFieldUnit))))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 474, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(BillDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(itemNameTv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(SaleItemCarret, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(saleItemQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(unitTv, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(weightTv))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(availableTv))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel6)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(salePriceField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(unitSaleField)))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(LabourCostField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labourCostFieldUnit))
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 417, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(153, 153, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Bill Details", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 3, 18))); // NOI18N

        tableModel = new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sr No.", "Item Name", "Weight","Pieces", "Caret", "Price/Unit",  "Labout Cost / Unit", "Total"
            }
        ){
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false,  false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        };
        jTable1.setModel(tableModel);
        jTable1.setRowHeight(50);
        jTable1.setFont(new Font("Tahoma",Font.BOLD,16));
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setMaxWidth(60);
        }
        jTable1.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(jTable1);

        jLabel8.setFont(new java.awt.Font("Tahoma", 2, 18)); // NOI18N
        jLabel8.setText("Customer's Details");

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel9.setText("Name      ");

        CustomerNameField.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel15.setText("Phone No.  ");

        CustomerPhoneField.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel16.setText("Address      ");

        CustomerAddField.setColumns(20);
        CustomerAddField.setFont(new java.awt.Font("Monospaced", 0, 14)); // NOI18N
        CustomerAddField.setRows(5);
        jScrollPane2.setViewportView(CustomerAddField);

        jLabel11.setFont(new java.awt.Font("Tahoma", 2, 24)); // NOI18N
        jLabel11.setText("Total Amount :");

        finalBillTv.setFont(new java.awt.Font("Tahoma", 2, 24)); // NOI18N
        finalBillTv.setText("0.00");

        jLabel13.setFont(new java.awt.Font("Tahoma", 2, 24)); // NOI18N
        jLabel13.setText("Rs.");

        jButton2.setFont(new java.awt.Font("Tahoma", 3, 18)); // NOI18N
        jButton2.setText("PRINT");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Tahoma", 2, 24)); // NOI18N
        jLabel10.setText("Total Item :");

        totalItemTv.setFont(new java.awt.Font("Tahoma", 2, 24)); // NOI18N
        totalItemTv.setText("0");

        discountCombo.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        discountCombo.setText("Discount");
        discountCombo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                discountComboItemStateChanged(evt);
            }
        });

        discountField.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        dicountText.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        dicountText.setText("Discount Amount : ");

        jSeparator1.setForeground(new java.awt.Color(255, 255, 255));
        jSeparator1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        oldMetalPanel.setBackground(new java.awt.Color(153, 153, 255));

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel14.setText("Metal Name       ");

        jLabel20.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel20.setText("Weight            ");

        jLabel21.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel21.setText("Rate");

        oldMetalName.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        oldMetalWeight.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        oldMetalRate.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jLabel23.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel23.setText("gram");

        jLabel24.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel24.setText("/ gram");

        javax.swing.GroupLayout oldMetalPanelLayout = new javax.swing.GroupLayout(oldMetalPanel);
        oldMetalPanel.setLayout(oldMetalPanelLayout);
        oldMetalPanelLayout.setHorizontalGroup(
            oldMetalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(oldMetalPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(oldMetalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(oldMetalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(oldMetalName, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(oldMetalPanelLayout.createSequentialGroup()
                        .addGroup(oldMetalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(oldMetalRate, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                            .addComponent(oldMetalWeight, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(18, 18, 18)
                        .addGroup(oldMetalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel23)
                            .addComponent(jLabel24))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        oldMetalPanelLayout.setVerticalGroup(
            oldMetalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(oldMetalPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(oldMetalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(oldMetalName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(oldMetalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(oldMetalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(oldMetalWeight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel23))
                    .addComponent(jLabel20))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(oldMetalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(oldMetalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(oldMetalRate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel24))
                    .addComponent(jLabel21))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        oldMetalCheckBox.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        oldMetalCheckBox.setText("Old Metal Exchange");
        oldMetalCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                oldMetalCheckBoxItemStateChanged(evt);
            }
        });
        oldMetalCheckBox.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                oldMetalCheckBoxStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(58, 58, 58)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(discountCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(totalItemTv, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(57, 57, 57)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(finalBillTv, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 28, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(dicountText)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(discountField, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(51, 51, 51)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29))))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jSeparator1))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel16)
                                            .addComponent(jLabel15))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(CustomerPhoneField)
                                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 352, Short.MAX_VALUE)))
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(CustomerNameField, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(197, 197, 197)
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(oldMetalPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(87, 87, 87)
                                .addComponent(oldMetalCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(30, 30, 30)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(CustomerNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel15)
                            .addComponent(CustomerPhoneField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(oldMetalCheckBox)
                        .addGap(18, 18, 18)
                        .addComponent(oldMetalPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(totalItemTv)
                    .addComponent(jLabel11)
                    .addComponent(jLabel13)
                    .addComponent(finalBillTv))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(discountCombo)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton2)
                        .addComponent(discountField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(dicountText)))
                .addGap(28, 28, 28))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap(14, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String billDate = BillDate.getText();
        //String itemName = SaleItemName.getSelectedItem().toString();
        String itemName = itemNameTv.getText();
        String itemCarret = SaleItemCarret.getText();
        String quantity = saleItemQuantity.getText();
        String salePrice = salePriceField.getText();
        String labourCost = LabourCostField.getText();

        int sn = tableModel.getRowCount() + 1;

        if (noItem) {
            AlertMessage msg = new AlertMessage("No Item available in the stock");
        } else if (billDate.equals("")) {
            AlertMessage msg = new AlertMessage("Please select a suitable date");
        } else if (itemName.equals("") || selected == 0) {
            AlertMessage msg = new AlertMessage("Please select the product from list");
        } else if (itemCarret.equals("")) {
            AlertMessage msg = new AlertMessage("Please Enter the Carret of the product");
        } else if (quantity.equals("")) {
            AlertMessage msg = new AlertMessage("Please Enter the quantity of the product");
        } else if (salePrice.equals("")) {
            AlertMessage msg = new AlertMessage("Please Enter the Sale Price of the product");
        } else if (labourCost.equals("")) {
            AlertMessage msg = new AlertMessage("Please Enter the Labour Cost of the product");
        } else if (!Helper.priceFormatCheck(salePrice)) {
            AlertMessage msg = new AlertMessage("Sale Price Format is not correct");
        } else if (!Helper.priceFormatCheck(labourCost)) {
            AlertMessage msg = new AlertMessage("Labour Cost Format is not correct");
        } else if (!Helper.isInt(quantity)) {
            AlertMessage msg = new AlertMessage("Quantity Format is not correct, 'pieces' should be an integer");
        } else if (Float.parseFloat(availableQty[index]) < Float.parseFloat(quantity)) {
            AlertMessage msg = new AlertMessage("Availability is less than ordered quantity");
        } else {
            float p = Float.parseFloat(salePrice);
            float l = Float.parseFloat(labourCost);
            float q = Float.parseFloat(quantity);
            float wt = Float.parseFloat(weight);
            float total = (p + l) * q * wt;
            finalBill += total;
            tableModel.addRow(new Object[]{Integer.toString(sn),
                itemName,
                weight + " " + dispUnit[index],
                quantity,
                itemCarret,
                salePrice,
                labourCost,
                Float.toString(total)});
            finalBillTv.setText(Float.toString(finalBill + (finalBill / 100) * vatPercentage));
            totalItemTv.setText(Integer.toString(tableModel.getRowCount()));
            updateStock(quantity);

            HashMap map = new HashMap();
            map.put("sn", sn);
            map.put("itemName", itemName);
            map.put("weight", weight);
            map.put("quantity", quantity);
            map.put("carret", itemCarret);
            map.put("salePrice", salePrice);
            map.put("unit", dispUnit[index]);
            map.put("labourCost", labourCost);
            map.put("total", total);
            map.put("stockID", stockID[index]);
            map.put("billDate", billDate);
            addToJsonArray(map);
            //writeToSaleFile(); 
            availableTv.setText("0");
            salePriceField.setText("");
            unitTv.setText("Unit");
            unitSaleField.setText("/ " + "unit");
            labourCostFieldUnit.setText("/ " + "unit");
            SaleItemCarret.setText("");
            weightTv.setText("");
            itemNameTv.setText("");
            LabourCostField.setText("");
            saleItemQuantity.setText("");
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    public void addToJsonArray(HashMap map) {
        JSONObject obj = new JSONObject();
        obj.putAll(map);
        array.add(obj);

    }

    public void updateStock(String q) {
        File filePath = new File(Constants.STOCK_FILE);
        if (filePath.exists()) {
            JSONParser parser = new JSONParser();

            JSONArray array = JsonClass.getJsonArray(Constants.STOCK_FILE);
            JSONArray newArray = new JSONArray();

            for (int i = 0; i < array.size(); i++) {
                try {
                    Object o = parser.parse(array.get(i).toString());
                    JSONObject obj = (JSONObject) o;
                    //model.addRow(new Object[] {obj.get("id"),obj.get("name"),obj.get("purchasePrice"),obj.get("salePrice"), obj.get("unit")});
                    if (obj.get("id").toString().equals(stockID[index])) {
                        String r = obj.get("remaining").toString();
                        float fr = Float.parseFloat(r);
                        fr -= Float.parseFloat(q);

                        obj.put("remaining", fr);
                        availableQty[index] = Float.toString(fr);
                        availableTv.setText(availableQty[index]);
                        newArray.add(obj);
                    } else {
                        newArray.add(obj);
                    }

                } catch (ParseException ex) {
                    Logger.getLogger(ManageItems.class.getName()).log(Level.SEVERE, null, ex);
                }

                //model.addRow(new Object[] {"one","two","three","four","five"});
            }
            FileWriter.write(Constants.STOCK_FILE, newArray.toString());
        } else {
            AlertMessage msg = new AlertMessage("Please firts add Items to your stock..!!");
        }
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        String customerName = CustomerNameField.getText();
        String customerPhone = CustomerPhoneField.getText();
        String customerAddress = CustomerAddField.getText();
        String billDate = BillDate.getText();
        float discountAmt = 0;

        if (customerName.equals("")) {
            AlertMessage msg = new AlertMessage("Please specify Customer Name");
        } else if (customerPhone.equals("")) {
            AlertMessage msg = new AlertMessage("Please specify Customer's Phone No.");
        } else if (customerAddress.equals("")) {
            AlertMessage msg = new AlertMessage("Please specify Customer's Address");
        } else if (tableModel.getRowCount() < 1) {
            AlertMessage msg = new AlertMessage("No Item in the Bill");
        } else {
            JSONArray mainArray = JsonClass.getJsonArray(Constants.SALE_RECORD_FILE);
            JSONObject obj = new JSONObject();
            obj.put("customerName", customerName);
            obj.put("customerPhone", customerPhone);
            obj.put("customerAddress", customerAddress);
            obj.put("billDate", billDate);
            obj.put("totalAmount", finalBill * (1 + vatPercentage / 100));

            int custCount = mainArray.size() + 1;
            String customerUniqueId = "DKJ" + String.format("%05d", custCount);
            obj.put("customerID", customerUniqueId);

            if (discountCombo.isSelected()) {
                if (!Helper.priceFormatCheck(discountField.getText())) {
                    AlertMessage msg = new AlertMessage("Discount amount format is not acceptable");
                } else {
                    if (discountField.getText().equals("")) {
                        obj.put("discount", Integer.toString(0));
                    } else {
                        discountAmt = Float.parseFloat(discountField.getText());
                        obj.put("discount", discountAmt);
                    }
                }
            } else {
                obj.put("discount", Integer.toString(0));
            }

            /*array.add(customerName);
             array.add(customerPhone);
             array.add(customerAddress);*/
            //array.add(obj);
            array.add(0, obj);

            mainArray.add(array);

            FileWriter.write(Constants.SALE_RECORD_FILE, mainArray.toString());

            if (oldMetalCheckBox.isSelected()) {
                String name = oldMetalName.getText();
                String wt = oldMetalWeight.getText();
                String rate = oldMetalRate.getText();
                if (name.equals("")) {
                    AlertMessage msg = new AlertMessage("Please enter the name of the old metal");
                } else if (wt.equals("")) {
                    AlertMessage msg = new AlertMessage("Please enter the weight of the old metal");
                } else if (rate.equals("")) {
                    AlertMessage msg = new AlertMessage("Please enter the rate of the old metal");
                } else if (!Helper.priceFormatCheck(wt)) {
                    AlertMessage msg = new AlertMessage("Weight of the old metal is not acceptable");
                } else if (!Helper.priceFormatCheck(rate)) {
                    AlertMessage msg = new AlertMessage("Rate of the old metal is not acceptable");
                } else {
                    oldMetalCost = Float.parseFloat(wt) * Float.parseFloat(rate);
                    writeToOldMetalFile(customerName, customerUniqueId, name, wt, rate, oldMetalCost);
                }
            }

            entryOnHistoryFile(customerName, customerPhone, customerUniqueId, customerAddress);

            printTheBill(array, customerName, customerPhone, customerUniqueId, discountAmt);

        }
    }//GEN-LAST:event_jButton2ActionPerformed

    public void writeToOldMetalFile(String cname, String cid, String mname, String mwt, String mrate, float mcost) {
        JSONArray array = JsonClass.getJsonArray(Constants.OLD_METAL_FILE);
        JSONObject obj = new JSONObject();
        obj.put("customerId", cid);
        obj.put("customerName", cname);
        obj.put("metalName", mname);
        obj.put("metalWeight", mwt);
        obj.put("metalRate", mrate);
        obj.put("unit", "gram");
        obj.put("totalCost", Float.toString(mcost));
        array.add(obj);
        FileWriter.write(Constants.OLD_METAL_FILE, array.toString());
    }

    public void entryOnHistoryFile(String name, String phone, String id, String address) {
        JSONArray mainArray = JsonClass.getJsonArray(Constants.TRANS_HISTORY_FILE);
        JSONArray array = new JSONArray();
        JSONObject obj = new JSONObject();
        obj.put("customerName", name);
        obj.put("customerPhone", phone);
        obj.put("customerAddress", address);
        obj.put("customerId", id);
        array.add(obj);
        mainArray.add(array);

        FileWriter.write(Constants.TRANS_HISTORY_FILE, mainArray.toString());
    }
    private void discountComboItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_discountComboItemStateChanged
        if (discountCombo.isSelected()) {
            dicountText.setVisible(true);
            discountField.setVisible(true);
        } else {
            dicountText.setVisible(false);
            discountField.setVisible(false);
        }
    }//GEN-LAST:event_discountComboItemStateChanged

    private void itemNameTvKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_itemNameTvKeyReleased
        availableTv.setText("0");
        salePriceField.setText("");
        unitTv.setText("Unit");
        unitSaleField.setText("/ " + "unit");
        labourCostFieldUnit.setText("/ " + "unit");
        SaleItemCarret.setText("");
        weightTv.setText("");
        String name = itemNameTv.getText();
        updateItemTable(name);
        selected = 0;
    }//GEN-LAST:event_itemNameTvKeyReleased

    private void oldMetalCheckBoxStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_oldMetalCheckBoxStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_oldMetalCheckBoxStateChanged

    private void oldMetalCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_oldMetalCheckBoxItemStateChanged
        if (oldMetalCheckBox.isSelected()) {
            oldMetalPanel.setVisible(true);
        } else {
            oldMetalPanel.setVisible(false);
        }
    }//GEN-LAST:event_oldMetalCheckBoxItemStateChanged

    public void updateItemTable(String keyword) {
        stockItemModel.setNumRows(0);
        for (int i = 0; i < stockID.length; i++) {
            if (s[i].toLowerCase().trim().startsWith(keyword.toLowerCase().trim())) {
                stockItemModel.addRow(new Object[]{stockID[i], s[i]});
            }
        }
    }

    public void fillItemTable() {
        if (new File(Constants.STOCK_FILE).exists()) {
            for (int i = 0; i < stockID.length; i++) {
                stockItemModel.addRow(new Object[]{stockID[i], s[i]});
            }
        }
    }

    public void printTheBill(JSONArray array, String a, String b, String id, float discAmt) {
        String path = "";
        //System.out.println(array.toString());
        JSONParser parser = new JSONParser();
        Object o;
        try {
            Document document = new Document();
            path = Helper.getFilePath() + "\\" + id + "_bill.pdf";

            PdfWriter.getInstance(document, new FileOutputStream(path));                    //change the name of the file according to the cudtomer id
            document.open();

            Paragraph shopName = new Paragraph(Constants.SHOP_NAME, FontFactory.getFont(FontFactory.TIMES_BOLD, 16, Font.BOLD, BaseColor.BLACK));
            //shopName.setAlignment(Element.ALIGN_CENTER);
            Image image;
            float[] cw = new float[]{3f, 6f};
            PdfPTable shopNameTable = new PdfPTable(2);
            shopNameTable.setWidths(cw);
            shopNameTable.setWidthPercentage(100);

            PdfPCell cl;
            try {
                image = Image.getInstance(Constants.SHOP_LOGO_URL);
                image.scaleAbsolute(55, 40);
                cl = new PdfPCell(image);
                cl.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cl.disableBorderSide(Rectangle.BOX);
                shopNameTable.addCell(cl);
                //document.add(image);

            } catch (BadElementException ex) {
                Logger.getLogger(SaleItem.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(SaleItem.class.getName()).log(Level.SEVERE, null, ex);
            }

            cl = new PdfPCell(shopName);
            cl.setHorizontalAlignment(Element.ALIGN_LEFT);
            cl.disableBorderSide(Rectangle.BOX);
            shopNameTable.addCell(cl);

            document.add(shopNameTable);

            //document.add(shopName);
            Paragraph address = new Paragraph(Constants.SHOP_ADDRESS, FontFactory.getFont(FontFactory.TIMES, 10, Font.ITALIC, BaseColor.BLACK));
            address.setAlignment(Element.ALIGN_CENTER);
            document.add(address);

            //adding image of the shop 
            /*try {
             Image image = Image.getInstance("heading.png");
             image.scaleAbsolute(530, 50);
             image.setAlignment(Element.ALIGN_CENTER);
             document.add(image);
             } catch (BadElementException ex) {
             Logger.getLogger(SaleItem.class.getName()).log(Level.SEVERE, null, ex);
             } catch (IOException ex) {
             Logger.getLogger(SaleItem.class.getName()).log(Level.SEVERE, null, ex);
             }*/
            //adding header including TIN and date
            PdfPTable tbl = new PdfPTable(2);
            tbl.setWidthPercentage(100);
            PdfPCell cel;
            if (!myTin.equals("")) {
                cel = new PdfPCell(new Phrase("TIN/VAT - " + myTin, FontFactory.getFont(FontFactory.TIMES_ROMAN, 10)));
            } else {
                cel = new PdfPCell(new Phrase("", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10)));
            }
            cel.setHorizontalAlignment(Element.ALIGN_LEFT);
            cel.disableBorderSide(Rectangle.BOX);
            tbl.addCell(cel);
            /*Paragraph p = new Paragraph("Date - " + getDateFormat() + "    " + getTimeFormat(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10));
             Paragraph p1 = new Paragraph("Phone : 1234567890", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10));
             Phrase t = new Phrase();*/
            cel = new PdfPCell(new Phrase("Date - " + getDateFormat() + "    " + getTimeFormat(BillDate.getSelectedDate().getTime()), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10)));
            cel.disableBorderSide(Rectangle.BOX);
            cel.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tbl.addCell(cel);

            cel = new PdfPCell();
            cel.disableBorderSide(Rectangle.BOX);
            cel.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tbl.addCell(cel);

            if (myPhone.equals("")) {
                cel = new PdfPCell(new Phrase("", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10)));
            } else {
                cel = new PdfPCell(new Phrase("Phone : " + myPhone, FontFactory.getFont(FontFactory.TIMES_ROMAN, 10)));
            }
            cel.disableBorderSide(Rectangle.BOX);
            cel.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tbl.addCell(cel);

            document.add(tbl);

            try {
                //adding customer details
                o = parser.parse(array.get(0).toString());
                JSONObject obj = (JSONObject) o;

                Paragraph cid = new Paragraph("Customer Id -             " + id, FontFactory.getFont(FontFactory.TIMES_ITALIC, 12));
                document.add(cid);
                Paragraph cname = new Paragraph("Name -                      " + obj.get("customerName"), FontFactory.getFont(FontFactory.TIMES_ITALIC, 12));
                document.add(cname);
                Paragraph cphone = new Paragraph("Phone -                     " + obj.get("customerPhone"), FontFactory.getFont(FontFactory.TIMES_ITALIC, 12));
                document.add(cphone);
                Paragraph caddress = new Paragraph("Address -                  " + obj.get("customerAddress"), FontFactory.getFont(FontFactory.TIMES_ITALIC, 12));
                document.add(caddress);
            } catch (ParseException ex) {
                Logger.getLogger(SaleItem.class.getName()).log(Level.SEVERE, null, ex);
            }

            //adding horizontal line
            Paragraph p = new Paragraph();
            for (int i = 0; i < 130; i++) {
                p.add("-");
            }
            document.add(p);
            document.add(new Paragraph(" "));

            float[] columnWidths = new float[]{1f, 5f, 3f, 3f, 2f, 3f, 3f, 4f};
            PdfPTable table = new PdfPTable(8);
            table.setWidthPercentage(100);
            table.setWidths(columnWidths);

            table.addCell("S No.");
            table.addCell("Item Name");
            table.addCell("Weight");
            table.addCell("Quantity");
            table.addCell("Carret");
            table.addCell("Price");
            table.addCell("Labour Cost");
            table.addCell("Amount");

            /*PdfPCell cell = new PdfPCell(new Paragraph("Title"));
             cell.setColspan(2);
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setBackgroundColor(BaseColor.GREEN);
             table.addCell(cell);*/
            try {
                for (int i = 1; i < array.size(); i++) {
                    o = parser.parse(array.get(i).toString());
                    JSONObject obj = (JSONObject) o;

                    PdfPCell pc = new PdfPCell();

                    table.addCell(obj.get("sn").toString());
                    table.addCell(obj.get("itemName").toString());
                    table.addCell(obj.get("weight") + " " + obj.get("unit"));
                    table.addCell(obj.get("quantity").toString());
                    table.addCell(obj.get("carret").toString());
                    table.addCell(obj.get("salePrice").toString());
                    table.addCell(obj.get("labourCost") + "");
                    table.addCell(obj.get("total").toString());
                }
            } catch (ParseException ex) {
                Logger.getLogger(SaleItem.class.getName()).log(Level.SEVERE, null, ex);
            }

            int oldTicked = 0;
            if (oldMetalCheckBox.isSelected()) {
                oldTicked = 1;
                if (array.size() < 16) {
                    PdfPCell c = new PdfPCell();
                    for (int i = 0; i < 16 - array.size(); i++) {
                        table.addCell(Integer.toString(array.size() + i));
                        table.addCell(c);
                        table.addCell(c);
                        table.addCell(c);
                        table.addCell(c);
                        table.addCell(c);
                        table.addCell(c);
                        table.addCell(c);
                    }
                }
                PdfPCell c = new PdfPCell(new Paragraph("In Exchange"));
                c.setColspan(2);
                c.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(c);
                c = new PdfPCell(new Paragraph("Weight"));
                table.addCell(c);
                c = new PdfPCell(new Paragraph("Rate"));
                table.addCell(c);
                c = new PdfPCell();
                table.addCell(c);
                table.addCell(c);
                table.addCell(c);
                table.addCell(c);

                c = new PdfPCell(new Paragraph(oldMetalName.getText()));
                c.setColspan(2);
                c.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(c);
                c = new PdfPCell(new Paragraph(oldMetalWeight.getText() + " gram"));
                table.addCell(c);
                c = new PdfPCell(new Paragraph(oldMetalRate.getText() + " / gram"));
                table.addCell(c);
                c = new PdfPCell();
                table.addCell(c);
                table.addCell(c);
                c = new PdfPCell(new Paragraph("Cost "));
                c.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(c);
                c = new PdfPCell(new Paragraph(Float.toString(oldMetalCost)));
                table.addCell(c);
                finalBill -= oldMetalCost;
            } else {
                if (array.size() < 21) {
                    PdfPCell c = new PdfPCell();
                    for (int i = 0; i < 21 - array.size(); i++) {
                        table.addCell(Integer.toString(array.size() + i));
                        table.addCell(c);
                        table.addCell(c);
                        table.addCell(c);
                        table.addCell(c);
                        table.addCell(c);
                        table.addCell(c);
                        table.addCell(c);
                    }
                }
            }

            /*table.addCell("");
             table.addCell("");
             table.addCell("");
             table.addCell("");
             table.addCell("");
             table.addCell("VAT");*/
            PdfPCell cell = new PdfPCell(new Paragraph("VAT     "));
            cell.setColspan(7);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            table.addCell(Float.toString((finalBill / 100) * vatPercentage));

            /*table.addCell("");
             table.addCell("");
             table.addCell("");
             table.addCell("");
             table.addCell("");*/
            if (discountCombo.isSelected()) {
                cell = new PdfPCell(new Paragraph("Discount     "));
                cell.setColspan(7);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(cell);
                table.addCell(Float.toString(discAmt));
            }

            /*table.addCell("");
             table.addCell("");
             table.addCell("");
             table.addCell("");
             table.addCell("");*/
            cell = new PdfPCell(new Paragraph("Total     "));
            cell.setColspan(7);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            table.addCell(Float.toString(finalBill + (finalBill / 100) * vatPercentage - discAmt));

            document.add(table);

            //adding terms and conditions
            File file = new File(Constants.TERMS_AND_CONDITIONS_FILE);
            if (file.exists()) {
                document.add(new Paragraph("Terms and Conditions : ", FontFactory.getFont(FontFactory.TIMES_ITALIC, 12)));
                document.add(new Paragraph(FileWriter.read(Constants.TERMS_AND_CONDITIONS_FILE), FontFactory.getFont(FontFactory.TIMES_ITALIC, 10)));
            }

            Paragraph sign = new Paragraph("Authorized Signature", FontFactory.getFont(FontFactory.TIMES_ITALIC, 12));
            sign.setAlignment(Element.ALIGN_RIGHT);
            document.add(sign);

            Paragraph msg = new Paragraph("THANKS FOR VISIT", FontFactory.getFont(FontFactory.TIMES_BOLD, 14));
            msg.setAlignment(Element.ALIGN_CENTER);
            document.add(msg);

            document.close();
        } catch (DocumentException ex) {
            Logger.getLogger(SaleItem.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SaleItem.class.getName()).log(Level.SEVERE, null, ex);
        }
        printWithPrinter(path);

        //Main.notifTv.setText("<html>Amount to pay :<br>      "+finalBill+"</html>");
        Main.setCustDetails(a, id);
        Main.setBillAmt(finalBill + (finalBill / 100) * vatPercentage, discAmt);

        dispose();
    }

    public String getDateFormat() {
        SimpleDateFormat y = new SimpleDateFormat("dd/MM/yyyy");
        Date date = Calendar.getInstance().getTime();
        return y.format(date);
    }

    public String getTimeFormat(Date b) {

        SimpleDateFormat y = new SimpleDateFormat("hh:mm a");
        //Date date = Calendar.getInstance().getTime();
        return y.format(b);
    }

    public void printWithPrinter(String path) {
        //The desktop api can help calling other applications in our machine
        //and also many other features...
        Desktop desktop = Desktop.getDesktop();
        try {
            //desktop.print(new File("DocXfile.docx"));
            desktop.print(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private datechooser.beans.DateChooserCombo BillDate;
    private javax.swing.JTextArea CustomerAddField;
    private javax.swing.JTextField CustomerNameField;
    private javax.swing.JTextField CustomerPhoneField;
    private javax.swing.JTextField LabourCostField;
    private javax.swing.JTextField SaleItemCarret;
    private javax.swing.JLabel availableTv;
    private javax.swing.JLabel dicountText;
    private javax.swing.JCheckBox discountCombo;
    private javax.swing.JTextField discountField;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JLabel finalBillTv;
    private javax.swing.JTextField itemNameTv;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel labourCostFieldUnit;
    private javax.swing.JCheckBox oldMetalCheckBox;
    private javax.swing.JTextField oldMetalName;
    private javax.swing.JPanel oldMetalPanel;
    private javax.swing.JTextField oldMetalRate;
    private javax.swing.JTextField oldMetalWeight;
    private javax.swing.JTextField saleItemQuantity;
    private javax.swing.JTextField salePriceField;
    private javax.swing.JLabel totalItemTv;
    private javax.swing.JLabel unitSaleField;
    private javax.swing.JLabel unitTv;
    private javax.swing.JLabel weightTv;
    // End of variables declaration//GEN-END:variables
}
