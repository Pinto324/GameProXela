/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI.Cajero;

import Controladores.CClientes;
import Controladores.CProductos;
import Controladores.CVentas;
import Objetos.DetalleVentas;
import Objetos.clientes;
import Objetos.productoVenta;
import UI.Utilidades;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Stack;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author branp
 */
public class Venta extends javax.swing.JDialog {

    private CClientes controlador = new CClientes();
    private CVentas controladorVentas = new CVentas();
    private String[] datos;
    private ArrayList<productoVenta> infoProducto;
    private ArrayList<clientes> infoCliente;   
    private Stack<DetalleVentas> Carrito = new Stack();
    private double PrecioTotal = 0.00;
    /**
     * Creates new form RellenarI
     */
    public Venta(java.awt.Frame parent, boolean modal, String[] da) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(null);
        datos = da;  
        llenarcomboClientes();
        estadoInicial();
        jLaberFactura.setText(""+controladorVentas.numeroFactura());   
        llenarTabla();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelFondo = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabelPrecio = new javax.swing.JLabel();
        jPanelBuscar = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabelNit = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabelNombre = new javax.swing.JLabel();
        jCheckBoxPuntos = new javax.swing.JCheckBox();
        jLabelPuntos = new javax.swing.JLabel();
        jLabelAgregarProductos = new javax.swing.JLabel();
        jComboBoxProducto = new javax.swing.JComboBox<>();
        jPanelAgregarCarrito = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLaberNoFac = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jComboBoxUnidades = new javax.swing.JComboBox<>();
        jPanelFinalizarCompra = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLaberFactura = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        jPanelFondo.setBackground(new java.awt.Color(102, 204, 255));
        jPanelFondo.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Roboto Medium", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(102, 204, 255));
        jLabel1.setText("Venta");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(265, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(223, 223, 223))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanelFondo.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 10, 580, 50));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(102, 204, 255));
        jLabel3.setText("Buscar cliente:");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 30, -1, 28));

        jLabelPrecio.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabelPrecio.setForeground(new java.awt.Color(102, 204, 255));
        jLabelPrecio.setText(" ");
        jPanel2.add(jLabelPrecio, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 150, -1, -1));

        jPanelBuscar.setBackground(new java.awt.Color(102, 204, 255));
        jPanelBuscar.setForeground(new java.awt.Color(255, 255, 255));
        jPanelBuscar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanelBuscar.setEnabled(false);
        jPanelBuscar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanelBuscarMouseClicked(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Buscar");
        jLabel5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel5MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanelBuscarLayout = new javax.swing.GroupLayout(jPanelBuscar);
        jPanelBuscar.setLayout(jPanelBuscarLayout);
        jPanelBuscarLayout.setHorizontalGroup(
            jPanelBuscarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelBuscarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addContainerGap(10, Short.MAX_VALUE))
        );
        jPanelBuscarLayout.setVerticalGroup(
            jPanelBuscarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel2.add(jPanelBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 90, 60, 30));

        jLabel6.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(102, 204, 255));
        jLabel6.setText("C/F");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 10, -1, 16));

        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });
        jPanel2.add(jCheckBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 30, -1, -1));

        jComboBox1.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
                jComboBox1PopupMenuWillBecomeVisible(evt);
            }
        });
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });
        jPanel2.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 60, 200, -1));

        jLabelNit.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabelNit.setForeground(new java.awt.Color(102, 204, 255));
        jLabelNit.setText(" ");
        jPanel2.add(jLabelNit, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 70, -1, 28));

        jLabel8.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(102, 204, 255));
        jLabel8.setText("Nombre del cliente");
        jPanel2.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 10, -1, 28));

        jLabelNombre.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabelNombre.setForeground(new java.awt.Color(102, 204, 255));
        jLabelNombre.setText(" ");
        jPanel2.add(jLabelNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 30, -1, 28));

        jCheckBoxPuntos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxPuntosActionPerformed(evt);
            }
        });
        jPanel2.add(jCheckBoxPuntos, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 30, 50, -1));

        jLabelPuntos.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabelPuntos.setForeground(new java.awt.Color(102, 204, 255));
        jLabelPuntos.setText("Utilizar puntos");
        jPanel2.add(jLabelPuntos, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 10, 100, 16));

        jLabelAgregarProductos.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabelAgregarProductos.setForeground(new java.awt.Color(102, 204, 255));
        jLabelAgregarProductos.setText("Agregar producto");
        jPanel2.add(jLabelAgregarProductos, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 130, -1, 28));

        jComboBoxProducto.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                jComboBoxProductoPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        jComboBoxProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxProductoActionPerformed(evt);
            }
        });
        jPanel2.add(jComboBoxProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 160, 200, -1));

        jPanelAgregarCarrito.setBackground(new java.awt.Color(102, 204, 255));
        jPanelAgregarCarrito.setForeground(new java.awt.Color(255, 255, 255));
        jPanelAgregarCarrito.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanelAgregarCarrito.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanelAgregarCarritoMouseClicked(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Agregar a Carrito");
        jLabel9.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel9MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanelAgregarCarritoLayout = new javax.swing.GroupLayout(jPanelAgregarCarrito);
        jPanelAgregarCarrito.setLayout(jPanelAgregarCarritoLayout);
        jPanelAgregarCarritoLayout.setHorizontalGroup(
            jPanelAgregarCarritoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelAgregarCarritoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelAgregarCarritoLayout.setVerticalGroup(
            jPanelAgregarCarritoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel2.add(jPanelAgregarCarrito, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 190, 120, 30));

        jLabel10.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(102, 204, 255));
        jLabel10.setText("Nit");
        jPanel2.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 60, -1, -1));

        jLabel11.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(102, 204, 255));
        jLabel11.setText("Unidades");
        jPanel2.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 130, -1, -1));

        jLabel12.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(102, 204, 255));
        jLabel12.setText(" ");
        jPanel2.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 210, 80, -1));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jPanel2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 237, 720, 180));

        jLaberNoFac.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLaberNoFac.setForeground(new java.awt.Color(102, 204, 255));
        jLaberNoFac.setText("No Factura:");
        jPanel2.add(jLaberNoFac, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 210, -1, -1));

        jLabel14.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(102, 204, 255));
        jLabel14.setText("Precio total:");
        jPanel2.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 210, 80, -1));

        jPanel2.add(jComboBoxUnidades, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 150, 60, -1));

        jPanelFinalizarCompra.setBackground(new java.awt.Color(102, 204, 255));
        jPanelFinalizarCompra.setForeground(new java.awt.Color(255, 255, 255));
        jPanelFinalizarCompra.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanelFinalizarCompra.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanelFinalizarCompraMouseClicked(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Finalizar Compra");
        jLabel15.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel15.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel15MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanelFinalizarCompraLayout = new javax.swing.GroupLayout(jPanelFinalizarCompra);
        jPanelFinalizarCompra.setLayout(jPanelFinalizarCompraLayout);
        jPanelFinalizarCompraLayout.setHorizontalGroup(
            jPanelFinalizarCompraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelFinalizarCompraLayout.createSequentialGroup()
                .addComponent(jLabel15)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanelFinalizarCompraLayout.setVerticalGroup(
            jPanelFinalizarCompraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel2.add(jPanelFinalizarCompra, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 150, 110, 30));

        jLabel16.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(102, 204, 255));
        jLabel16.setText("Precio");
        jPanel2.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 130, -1, -1));

        jLaberFactura.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLaberFactura.setForeground(new java.awt.Color(102, 204, 255));
        jLaberFactura.setText(" ");
        jPanel2.add(jLaberFactura, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 210, -1, -1));

        jPanelFondo.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 740, 420));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("X");
        jLabel2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
        });
        jPanelFondo.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 0, 40, 29));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelFondo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelFondo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        this.dispose();

    }//GEN-LAST:event_jLabel2MouseClicked

    private void jLabel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseClicked
        funcionamientoBotonBuscar();
    }//GEN-LAST:event_jLabel5MouseClicked

    private void jPanelBuscarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelBuscarMouseClicked
        funcionamientoBotonBuscar();
    }//GEN-LAST:event_jPanelBuscarMouseClicked

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jCheckBoxPuntosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxPuntosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBoxPuntosActionPerformed

    private void jComboBoxProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxProductoActionPerformed
        jComboBoxUnidades.removeAllItems();
        for(int i = 0 ; i < infoProducto.get(jComboBoxProducto.getSelectedIndex()).getStock_estanteria(); i++){
            jComboBoxUnidades.addItem(String.valueOf(i+1));
        }
        jComboBoxUnidades.setEnabled(true);
        jLabelPrecio.setText(String.valueOf(infoProducto.get(jComboBoxProducto.getSelectedIndex()).getPrecio()));
    }//GEN-LAST:event_jComboBoxProductoActionPerformed

    private void jLabel9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel9MouseClicked
        agregarCarrito();
        
    }//GEN-LAST:event_jLabel9MouseClicked

    private void jPanelAgregarCarritoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelAgregarCarritoMouseClicked
        agregarCarrito();
    }//GEN-LAST:event_jPanelAgregarCarritoMouseClicked

    private void jComboBox1PopupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_jComboBox1PopupMenuWillBecomeVisible
        jPanelBuscar.setEnabled(true);
    }//GEN-LAST:event_jComboBox1PopupMenuWillBecomeVisible

    private void jComboBoxProductoPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_jComboBoxProductoPopupMenuWillBecomeInvisible
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxProductoPopupMenuWillBecomeInvisible

    private void jLabel15MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel15MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel15MouseClicked

    private void jPanelFinalizarCompraMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelFinalizarCompraMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanelFinalizarCompraMouseClicked
    public void funcionamientoBotonBuscar(){
        jLabelNombre.setText(infoCliente.get(jComboBox1.getSelectedIndex()).getNombre());
        jLabelNit.setText(infoCliente.get(jComboBox1.getSelectedIndex()).getNit());
        jPanelBuscar.setVisible(false);
        jComboBox1.setVisible(false);
        jLabel3.setVisible(false);
        if (!(infoCliente.get(jComboBox1.getSelectedIndex()).getTipoTarjeta().equals("0"))) {
            jLabelPuntos.setVisible(true);
            jCheckBoxPuntos.setVisible(true);
        }       
        jLabelAgregarProductos.setVisible(true);
        jComboBoxProducto.setVisible(true);
        jPanelAgregarCarrito.setVisible(true);
        jLabel11.setVisible(true);
        jLabel14.setVisible(true);
        jComboBoxUnidades.setVisible(true);
        jTable1.setVisible(true);
        rellenarDatosProductos();
    }

    public void llenarcomboClientes() {
        infoCliente = controlador.buscarClientes();
        for (int i = 0; i < infoCliente.size(); i++) {
            jComboBox1.addItem(infoCliente.get(i).getNit() + " " + infoCliente.get(i).getNombre());
        }
    }
    public void estadoInicial(){
    jCheckBoxPuntos.setVisible(false);
        jLabelPuntos.setVisible(false);
        jLabelAgregarProductos.setVisible(false);
        jComboBoxProducto.setVisible(false);
        jPanelAgregarCarrito.setVisible(false);
        jLabel11.setVisible(false);
        jLabel14.setVisible(false);
        jComboBoxUnidades.setVisible(false);
        jTable1.setVisible(false);
        jComboBoxUnidades.setEnabled(false);
        jPanelFinalizarCompra.setVisible(false);
    }
    
    public void agregarCarrito(){
        for(int i = 0 ; i < infoProducto.get(jComboBoxProducto.getSelectedIndex()).getStock_estanteria(); i++){
            jComboBoxUnidades.addItem(String.valueOf(i+1));
        }
        jComboBoxUnidades.setEnabled(true);
        int factura = controladorVentas.numeroFactura();
        int idProducto = infoProducto.get(jComboBoxProducto.getSelectedIndex()).getId_producto();
        int cantidad = jComboBoxUnidades.getSelectedIndex()+1;
        String nombre = infoProducto.get(jComboBoxProducto.getSelectedIndex()).getNombre();
        double precioU = Double.valueOf(jLabelPrecio.getText());
        DetalleVentas venta = new DetalleVentas(factura, idProducto,nombre ,cantidad, precioU);
        Carrito.push(venta);
        jPanelFinalizarCompra.setVisible(true);
        llenarTabla();
    }
           
    public void rellenarDatosProductos() {
        try {
            CProductos controlador = new CProductos();
            infoProducto = controlador.InformacionproductosParaVenta(Integer.valueOf(datos[4]));
            for (int i = 0; i < infoProducto.size(); i++) {
                jComboBoxProducto.addItem(infoProducto.get(i).getNombre());
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void llenarTabla(){
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Nombre Producto");
        model.addColumn("Precio unidad");
        model.addColumn("unidades");
        model.addColumn("Precio por todo");        
        jTable1.setModel(model);
        double preciot = 0.00;
        for (int i = 0; i < Carrito.size(); i ++) {
            double suma = Carrito.get(i).getCantidad() * Carrito.get(i).getPrecioU();
            Object[] fila = {Carrito.get(i).getNombreProducto(),Carrito.get(i).getPrecioU(),Carrito.get(i).getCantidad(),suma};
            model.addRow(fila);
            preciot += suma;
        }
        jTable1.setModel(model);
        jTable1.getColumnModel().getColumn(0).setPreferredWidth(100);
        jTable1.getColumnModel().getColumn(1).setPreferredWidth(50);
        jTable1.getColumnModel().getColumn(2).setPreferredWidth(50);
        jTable1.getColumnModel().getColumn(3).setPreferredWidth(100);
        PrecioTotal = preciot;
        jLabel12.setText("Q"+PrecioTotal);
    }
    
    public void FinalizarCompra(){
        
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBoxPuntos;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBoxProducto;
    private javax.swing.JComboBox<String> jComboBoxUnidades;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelAgregarProductos;
    private javax.swing.JLabel jLabelNit;
    private javax.swing.JLabel jLabelNombre;
    private javax.swing.JLabel jLabelPrecio;
    private javax.swing.JLabel jLabelPuntos;
    private javax.swing.JLabel jLaberFactura;
    private javax.swing.JLabel jLaberNoFac;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanelAgregarCarrito;
    private javax.swing.JPanel jPanelBuscar;
    private javax.swing.JPanel jPanelFinalizarCompra;
    private javax.swing.JPanel jPanelFondo;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
