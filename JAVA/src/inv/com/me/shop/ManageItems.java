/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inv.com.me.shop;

import java.awt.print.PrinterException;
import java.io.File;
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
public class ManageItems extends javax.swing.JFrame {
    
    /**
     * Creates new form ManageItems
     */
    DefaultComboBoxModel unitModel = new DefaultComboBoxModel(); //new DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" });
    String[] dispUnit;
    int unitComboIndex = 0;
    static DefaultTableModel model;
    String selectedID = "";

    public ManageItems() {

        initComponents();
        this.getContentPane().setBackground(java.awt.Color.decode("#0041C2"));
        updationPanel.setVisible(false);
        File filePath = new File(Constants.STOCK_FILE);
        if (filePath.exists()) {
            JSONParser parser = new JSONParser();

            JSONArray array = JsonClass.getJsonArray(Constants.STOCK_FILE);

            for (int i = 0; i < array.size(); i++) {
                try {
                    Object o = parser.parse(array.get(i).toString());
                    JSONObject obj = (JSONObject) o;
                    model.addRow(new Object[]{obj.get("id"), obj.get("name"), obj.get("purchasePrice"), obj.get("salePrice"), obj.get("carret"), obj.get("remaining"), obj.get("unit")});
                } catch (ParseException ex) {
                    Logger.getLogger(ManageItems.class.getName()).log(Level.SEVERE, null, ex);
                }

                //model.addRow(new Object[] {"one","two","three","four","five"});
            }
        }

        //unit model definition
        File unitFilepath = new File(Constants.UNIT_FILE);
        if (unitFilepath.exists()) {

            JSONParser parser = new JSONParser();

            JSONArray array = JsonClass.getJsonArray(Constants.UNIT_FILE);
            dispUnit = new String[array.size()];

            for (int i = 0; i < array.size(); i++) {
                try {
                    Object o = parser.parse(array.get(i).toString());
                    JSONObject obj = (JSONObject) o;
                    //model.addRow(new Object[] {obj.get("id"),obj.get("name"),obj.get("purchasePrice"),obj.get("salePrice"), obj.get("unit")});
                    unitModel.addElement(obj.get(Integer.toString(i + 1)));
                    dispUnit[i] = obj.get(Integer.toString(i + 1)).toString();
                } catch (ParseException ex) {
                    Logger.getLogger(ManageItems.class.getName()).log(Level.SEVERE, null, ex);
                }

                //model.addRow(new Object[] {"one","two","three","four","five"});
            }
            if (array.size() > 0) {
                salePriceUnitTv.setText(dispUnit[0]);
                purchPriceUnitTv.setText(dispUnit[0]);
            }
        } else {
            unitModel.addElement("No unit in the list");
        }
        itemTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                updationPanel.setVisible(true);
                if (itemTable.getSelectedRow() != -1) {
                    selectedID = itemTable.getValueAt(itemTable.getSelectedRow(), 0).toString();
                    System.out.println(selectedID);
                }
            }
        });
        setVisible(true);
    }

    public static void deleteItemFromStock(String cid) {
        JSONArray array = JsonClass.getJsonArray(Constants.STOCK_FILE);
        JSONArray newArray = new JSONArray();
        for (int i = 0; i < array.size(); i++) {
            JSONObject obj = (JSONObject) array.get(i);
            String gotId = obj.get("id").toString();
            if (!gotId.equals(cid)) {
                newArray.add(obj);
            }
        }
        FileWriter.write(Constants.STOCK_FILE, newArray.toString());
        refreshPage();
    }

    public static void refreshPage() {
        model.setNumRows(0);
        JSONArray array = JsonClass.getJsonArray(Constants.STOCK_FILE);
        for (int i = 0; i < array.size(); i++) {
            JSONObject obj = (JSONObject) array.get(i);
            model.addRow(new Object[]{obj.get("id"), obj.get("name"), obj.get("purchasePrice"), obj.get("salePrice"), obj.get("carret"), obj.get("remaining"), obj.get("unit")});
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
        jLabel2 = new javax.swing.JLabel();
        nameTv = new javax.swing.JTextField();
        salePriceTv = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        PurchasePriceTv = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        UnitTv = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        carretField = new javax.swing.JTextField();
        salePriceUnitTv = new javax.swing.JLabel();
        purchPriceUnitTv = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        itemTable = new javax.swing.JTable();
        updationPanel = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Manage / Add New Items");
        setAutoRequestFocus(false);
        setLocation(new java.awt.Point(100, 30));
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(153, 153, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Please Specify Item's Detail(s)", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 3, 18))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Tahoma", 2, 18)); // NOI18N
        jLabel1.setText("Name");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setText("Sale Price");

        nameTv.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        salePriceTv.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        salePriceTv.setText("0");

        jLabel3.setFont(new java.awt.Font("Tahoma", 2, 18)); // NOI18N
        jLabel3.setText("Purchase Price");

        PurchasePriceTv.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        PurchasePriceTv.setText("0");
        PurchasePriceTv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PurchasePriceTvActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 2, 18)); // NOI18N
        jLabel4.setText("Unit");

        UnitTv.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        UnitTv.setModel(unitModel);
        UnitTv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UnitTvActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 2, 18)); // NOI18N
        jLabel5.setText("Carret");

        carretField.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        salePriceUnitTv.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        salePriceUnitTv.setText("/ unit");

        purchPriceUnitTv.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        purchPriceUnitTv.setText("/ unit");

        jButton1.setFont(new java.awt.Font("Tahoma", 2, 18)); // NOI18N
        jButton1.setText("Add Item To Stock");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE))
                        .addGap(20, 20, 20)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(nameTv)
                            .addComponent(UnitTv, 0, 236, Short.MAX_VALUE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(PurchasePriceTv, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(31, 31, 31)
                                        .addComponent(purchPriceUnitTv, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(salePriceTv, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(24, 24, 24)
                                        .addComponent(salePriceUnitTv, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(carretField, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(nameTv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(UnitTv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addComponent(jLabel3))
                            .addComponent(PurchasePriceTv, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(purchPriceUnitTv)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(salePriceUnitTv)
                            .addComponent(jLabel2)
                            .addComponent(salePriceTv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(carretField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(98, 98, 98)
                .addComponent(jButton1)
                .addContainerGap(392, Short.MAX_VALUE))
        );

        model = new DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Name", "Purchase Price/Unit", "Sale Price/Unit","Carret","In Stock", "Unit"
            }
        ){
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        };
        itemTable.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        itemTable.setModel(model);
        itemTable.setRowHeight(40);
        if (itemTable.getColumnModel().getColumnCount() > 0) {
            itemTable.getColumnModel().getColumn(0).setMaxWidth(120);
            itemTable.getColumnModel().getColumn(0).setMinWidth(120);
            itemTable.getColumnModel().getColumn(0).setPreferredWidth(120);
            itemTable.getColumnModel().getColumn(1).setMaxWidth(250);
            itemTable.getColumnModel().getColumn(1).setMinWidth(250);
            itemTable.getColumnModel().getColumn(1).setPreferredWidth(250);
        }
        jScrollPane1.setViewportView(itemTable);

        jButton2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton2.setText("Edit / See Details");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton3.setText("Delete This Item From Stock");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout updationPanelLayout = new javax.swing.GroupLayout(updationPanel);
        updationPanel.setLayout(updationPanelLayout);
        updationPanelLayout.setHorizontalGroup(
            updationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(updationPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addGap(149, 149, 149)
                .addComponent(jButton3)
                .addGap(92, 92, 92))
        );
        updationPanelLayout.setVerticalGroup(
            updationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, updationPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(updationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 429, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1281, Short.MAX_VALUE)
                    .addComponent(updationPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(30, 30, 30))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 717, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(updationPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(29, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void PurchasePriceTvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PurchasePriceTvActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_PurchasePriceTvActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        String name = nameTv.getText().toString();
        String purchasePrice = PurchasePriceTv.getText().toString();
        String SalePrice = salePriceTv.getText().toString();
        String unit = UnitTv.getSelectedItem().toString();
        String carret = carretField.getText().toString();

        if (name.equals("")) {
            AlertMessage mes = new AlertMessage("Please provide name of the product");
        } else if (purchasePrice.equals("0") || purchasePrice.equals("")) {
            AlertMessage mes = new AlertMessage("Please provide Purchase Price of the product");
        } else if (SalePrice.equals("0") || SalePrice.equals("")) {
            AlertMessage mes = new AlertMessage("Please provide Sale Price of the product");
        } else if (carret.equals("") || !Helper.priceFormatCheck(carret)) {
            AlertMessage mes = new AlertMessage("Carret field is empty or format is not corret");
        } else if (unit.equals("")) {
            AlertMessage mes = new AlertMessage("Please provide Unit of the Product");
        } else if (isProductNameExist(name)) {
            AlertMessage mes = new AlertMessage("An item with the same name already exist..!!, Please try other name");
        } else {
            JSONArray array = JsonClass.getJsonArray(Constants.STOCK_FILE);

            int row = array.size() + 1;

            /*HashMap map = new HashMap();
             map.put("id", row);
             map.put("name", name);
             map.put("pp", purchasePrice);
             map.put("sp", SalePrice);
             map.put("unit", unit);
             map.put("remaining", 0);
             map.put("carret", carret);*/
            JSONObject map = new JSONObject();
            String id = getNewId();
            map.put("id", id);
            map.put("name", name);
            map.put("purchasePrice", purchasePrice);
            map.put("salePrice", SalePrice);
            map.put("unit", unit);
            map.put("remaining", 0);
            map.put("carret", carret);

            model.addRow(new Object[]{id, name, purchasePrice, SalePrice, carret, 0, unit});

            //String newRow = JsonClass.encoding(map);
            array.add(map);

            //array.add(newRow);
            FileWriter.write(Constants.STOCK_FILE, array.toString());
            nameTv.setText("");
            PurchasePriceTv.setText("");
            salePriceTv.setText("");
            carretField.setText("");
            AlertMessage msg = new AlertMessage("Item '" + name + "' added to your stock successfully !!!");
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    public static boolean isProductNameExist(String s) {
        JSONArray array = JsonClass.getJsonArray(Constants.STOCK_FILE);
        for (int i = 0; i < array.size(); i++) {
            JSONObject obj = (JSONObject) array.get(i);
            String name = obj.get("name").toString();
            if (s.equals(name)) {
                return true;
            }
        }
        return false;
    }

    public String getNewId() {
        String id = "";
        int newId = 1;
        File f = new File(Constants.ID_FILE);
        if (f.exists()) {
            JSONArray arr = JsonClass.getJsonArray(Constants.ID_FILE);
            if (arr.size() > 0) {
                JSONObject obj = (JSONObject) arr.get(0);
                int lastId = Integer.parseInt(obj.get("lastStockId").toString());
                newId = lastId + 1;
                obj.put("lastStockId", Integer.toString(newId));
                JSONArray a = new JSONArray();
                a.add(obj);
                FileWriter.write(Constants.ID_FILE, a.toString());
                id = String.format("STK%05d", newId);
            }
        } else {
            newId = 1;
            JSONArray arr = new JSONArray();
            JSONObject obj = new JSONObject();
            obj.put("lastStockId", Integer.toString(1));
            arr.add(obj);
            FileWriter.write(Constants.ID_FILE, arr.toString());
            id = String.format("STK%05d", 1);
        }

        return id;
    }

    private void UnitTvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UnitTvActionPerformed
        unitComboIndex = UnitTv.getSelectedIndex();
        if (new File(Constants.UNIT_FILE).exists()) {
            salePriceUnitTv.setText(dispUnit[unitComboIndex]);
            purchPriceUnitTv.setText(dispUnit[unitComboIndex]);
        }
    }//GEN-LAST:event_UnitTvActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        AuthPasswordCheck pc = new AuthPasswordCheck("Go For Editing", 1, selectedID);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        AuthPasswordCheck pc = new AuthPasswordCheck("Delete Now", 2, selectedID);
    }//GEN-LAST:event_jButton3ActionPerformed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField PurchasePriceTv;
    private javax.swing.JComboBox UnitTv;
    private javax.swing.JTextField carretField;
    private javax.swing.JTable itemTable;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField nameTv;
    private javax.swing.JLabel purchPriceUnitTv;
    private javax.swing.JTextField salePriceTv;
    private javax.swing.JLabel salePriceUnitTv;
    private javax.swing.JPanel updationPanel;
    // End of variables declaration//GEN-END:variables
}
