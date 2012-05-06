/*
 Copyright 2012 Kevin J. Jones (http://www.kevinjjones.co.uk)

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

/*
 * This is not currently in use. Just saving in case I want to use later.
 */
package uk.co.kevinjjones;

import java.awt.Color;
import java.awt.Component;
import javax.swing.table.TableCellRenderer;
import uk.co.kevinjjones.RunManager.Run;

public class FuelView extends javax.swing.JPanel {

    private Run _run;
    private double _fuelData[][] = null;
    private int _fuelCountData[][] = null;
    private int _mapMinB;
    private int _mapMaxB;
    private int _rpmMinB;
    private int _rpmMaxB;
    private static final int maxRpm = 20000;
    private static final int maxMapKpa = 400;
    private static final int maxMapPsi = 60;
    private static final int maxTPS = 100;
    private static final int mapStep = 5;
    private static final int mapMax = 400;
    private static final int mapBuckets = mapMax / mapStep;
    private static final int rpmStep = 500;
    private static final int rpmMax = 20000;
    private static final int rpmBuckets = rpmMax / rpmStep;
    private static final int minSample = 1;

    /*
    private static final int mapStep=5; 
    private static final int mapMax=400; 
    private static final int mapBuckets=mapMax/mapStep;
    private static final int rpmStep=500;
    private static final int rpmMax=20000;
    private static final int rpmBuckets=rpmMax/rpmStep;
    private static final int minSample=1;
     */
    /** Creates new form FuelView */
    public FuelView(Run run) {
        _run = run;
        initComponents();
        populateDisplay();
    }

    private void populateDisplay() {
        
        /*

        // Load the data
        for (int i = 0; i < _run.length(); i++) {
            recordData(_run.rpm(i), _run.tps(i), _run.map(i), _run.lambda(i));
        }

        // Now setup the table
        DefaultTableModel model = (DefaultTableModel) _fuelTable.getModel();
        _mapMinB = 0;
        while (!hasFuelMap(_mapMinB)) {
            _mapMinB++;
        }

        _mapMaxB = mapBuckets - 1;
        while (!hasFuelMap(_mapMaxB)) {
            _mapMaxB--;
        }

        ArrayList<String> colIds = new ArrayList<String>();
        colIds.add("RPM/MAP");
        for (int m = _mapMinB; m <= _mapMaxB; m++) {
            colIds.add(Integer.toString(m * mapStep));
        }
        model.setColumnIdentifiers(colIds.toArray());
        
        TableColumnModel columns = new DefaultTableColumnModel();
        for (int count = model.getColumnCount(), i = 0; i < count; i++) {
            TableColumn c = new TableColumn(i);
            c.setHeaderValue(model.getColumnName(i));
            c.setMinWidth(16);
            c.setPreferredWidth(32);
            columns.addColumn(c);
        }
        _fuelTable.setColumnModel(columns);

        _rpmMinB = 0;
        while (!hasFuelRPM(_rpmMinB)) {
            _rpmMinB++;
        }

        _rpmMaxB = rpmBuckets - 1;
        while (!hasFuelRPM(_rpmMaxB)) {
            _rpmMaxB--;
        }

        while (model.getRowCount() != 0) {
            model.removeRow(0);
        }
        for (int r = _rpmMinB; r <= _rpmMaxB; r++) {
            ArrayList<String> rowData = new ArrayList<String>();
            rowData.add(Integer.toString(r * rpmStep));

            for (int m = _mapMinB; m <= _mapMaxB; m++) {
                if (hasFuel(r, m)) {
                    double f = ((double) (int) (getFuel(r, m) * 100)) / 100;
                    rowData.add(new Double(f).toString());
                } else {
                    rowData.add("");
                }
            }

            model.addRow(rowData.toArray());
        }
        * 
        */
    }

    private void recordData(double rpm, double tps, double map, double lamb) {

        if (_fuelData == null) {
            _fuelData = new double[rpmBuckets][mapBuckets]; //rpm x map
            _fuelCountData = new int[rpmBuckets][mapBuckets];
        }

        if (lamb == 0) {
            return;
        }

        int mapSlot = (int) ((((double) map) + mapStep / 2) / mapStep);
        int rpmSlot = (int) ((((double) rpm) + rpmStep / 2) / rpmStep);
        assert (mapSlot < mapBuckets);
        assert (rpmSlot < rpmBuckets);

        _fuelData[rpmSlot][mapSlot] += lamb;
        _fuelCountData[rpmSlot][mapSlot] += 1;
    }

    public double getFuel(int rpmSlot, int mapSlot) {
        assert (mapSlot < mapBuckets);
        assert (rpmSlot < rpmBuckets);
        return (_fuelData[rpmSlot][mapSlot] / _fuelCountData[rpmSlot][mapSlot]);
    }

    public boolean hasFuelMap(int mapSlot) {
        for (int r = 0; r < rpmBuckets; r++) {
            if (_fuelCountData[r][mapSlot] >= minSample) {
                return true;
            }
        }
        return false;
    }

    public boolean hasFuelRPM(int rpmSlot) {
        for (int m = 0; m < mapBuckets; m++) {
            if (_fuelCountData[rpmSlot][m] >= minSample) {
                return true;
            }
        }
        return false;
    }

    public boolean hasFuel(int rpmSlot, int mapSlot) {
        return (_fuelCountData[rpmSlot][mapSlot] >= minSample);
    }

    private Color getCellColor(int row, int column) {
        int mapB = (column - 1) + _mapMinB;
        int rpmB = row + _rpmMinB;

        int c = _fuelCountData[rpmB][mapB];
        if (c > 15) {
            c = 15;
        }
        return new Color(255, 255 - (16 * c), 255 - (16 * c));
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jScrollPane1 = new javax.swing.JScrollPane();
        _fuelTable = new javax.swing.JTable() {
            public Component prepareRenderer(
                TableCellRenderer renderer, int row, int column)
            {
                Component c = super.prepareRenderer(renderer, row, column);
                c.setBackground(getCellColor(row,column));
                return c;
            }

        };
        jTextField1 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jButton1 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        _fuelTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        _fuelTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jScrollPane1.setViewportView(_fuelTable);

        jTextField1.setToolTipText("Enter RPM values to use in the table, e.g. 500,1000,1500 etc..");

        jLabel1.setText("RPM Step");

        jLabel2.setText("Load (TPS or MAP) Step");

        jTextField2.setToolTipText("Enter TPS or MAP values to use in the table, e.g. 10,20,30 ...");

        buttonGroup1.add(jCheckBox1);
        jCheckBox1.setText("AFR/Lambda Ratio");
        jCheckBox1.setToolTipText("This colours the cell according to AFR/Lambda Ratio");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jCheckBox2);
        jCheckBox2.setText("Cell Use Frequency");
        jCheckBox2.setToolTipText("This colours the cell according to how often it is used");
        jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox2ActionPerformed(evt);
            }
        });

        jButton1.setText("Update Table");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel3.setText("Use Color for");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBox1)
                        .addGap(0, 0, 0)
                        .addComponent(jCheckBox2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField2)
                            .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 340, Short.MAX_VALUE)))
                .addContainerGap())
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 593, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jLabel3)
                    .addComponent(jCheckBox1)
                    .addComponent(jCheckBox2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable _fuelTable;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables
}
