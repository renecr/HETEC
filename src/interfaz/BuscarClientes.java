/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaz;

//awt
import java.awt.Color;
import java.awt.event.KeyEvent;

//swing
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

//Propios
import hetec.*;
import javax.swing.ImageIcon;

/**
 *
 * @author Richard
 */
public class BuscarClientes extends javax.swing.JDialog {

    static hetec.OracleBD oraclebd;
    static String usuario_g;
    int idEmp;
    DefaultTableModel modelo;
    int id = -1;
    String nombre = "";
    String identificacion = "";
    int plazo;

    /**
     * Creates new form BuscarClientes
     */
    public BuscarClientes(java.awt.Frame parent, boolean modal, hetec.OracleBD oracle, int empresa, String usuario) {
        super(parent, modal);
        initComponents();
        oraclebd = oracle;
        usuario_g = usuario;
        this.setTitle("HETEC Buscar Clientes " + usuario_g);

        idEmp = empresa;
        CodigoTxt.requestFocus();

        CodigoTxt.setDocument(new JTextFieldLimit(10, true));
        IdentificacionTxt.setDocument(new JTextFieldLimit(20, true));
        NombreTxt.setDocument(new JTextFieldLimit(250, true));
        CodigoTxt.requestFocus();

        /*ESTILO*/
        //POSITION DE PANTALLA
        this.setLocationRelativeTo(null);
        //FONDO
        getContentPane().setBackground(Color.WHITE);
        //BORDE
        getRootPane().setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.gray));
        //FONDO PANEL
        jPanel1.setBackground(Color.white);
        //FONDO TABLA
        ClientesTbl.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        ClientesTbl.setOpaque(true);
        ClientesTbl.setFillsViewportHeight(true);
        ClientesTbl.setBackground(Color.white);
        /*TERMINA ESTILO*/
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        ClientesTbl = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        CodigoTxt = new javax.swing.JTextField();
        IdentificacionTxt = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        NombreTxt = new javax.swing.JTextField();
        CancelarBtn = new javax.swing.JButton();
        AceptarBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setPreferredSize(new java.awt.Dimension(800, 550));
        setResizable(false);
        setSize(new java.awt.Dimension(800, 550));

        ClientesTbl.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        ClientesTbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Código", "Nombre", "Identificacion", "Plazo"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        ClientesTbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                ClientesTblMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(ClientesTbl);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel25.setFont(new java.awt.Font("Century Gothic", 1, 20)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(32, 116, 174));
        jLabel25.setText("BUSCAR CLIENTES");

        jLabel18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/jlogo2.png"))); // NOI18N
        jLabel18.setPreferredSize(new java.awt.Dimension(118, 20));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel25)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setPreferredSize(new java.awt.Dimension(426, 54));

        jLabel1.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel1.setText("Codigo");

        CodigoTxt.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        CodigoTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                CodigoTxtKeyPressed(evt);
            }
        });

        IdentificacionTxt.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        IdentificacionTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                IdentificacionTxtKeyPressed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel3.setText("Identificacion");

        jLabel2.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel2.setText("Nombre");

        NombreTxt.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        NombreTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NombreTxtKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(CodigoTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(IdentificacionTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(NombreTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(244, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(IdentificacionTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(CodigoTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(NombreTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(0, 9, Short.MAX_VALUE))
        );

        CancelarBtn.setBackground(new java.awt.Color(255, 255, 255));
        CancelarBtn.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        CancelarBtn.setForeground(new java.awt.Color(32, 116, 174));
        CancelarBtn.setText("Cancelar");
        CancelarBtn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(32, 116, 174)));
        CancelarBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        CancelarBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                CancelarBtnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                CancelarBtnMouseExited(evt);
            }
        });
        CancelarBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CancelarBtnActionPerformed(evt);
            }
        });
        CancelarBtn.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                CancelarBtnKeyPressed(evt);
            }
        });

        AceptarBtn.setBackground(new java.awt.Color(255, 255, 255));
        AceptarBtn.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        AceptarBtn.setForeground(new java.awt.Color(32, 116, 174));
        AceptarBtn.setText("Aceptar");
        AceptarBtn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(32, 116, 174)));
        AceptarBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        AceptarBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                AceptarBtnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                AceptarBtnMouseExited(evt);
            }
        });
        AceptarBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AceptarBtnActionPerformed(evt);
            }
        });
        AceptarBtn.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                AceptarBtnKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 770, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 18, Short.MAX_VALUE))
                            .addComponent(jScrollPane1))))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(AceptarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(CancelarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 331, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CancelarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(AceptarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ClientesTblMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ClientesTblMousePressed
        if (evt.getClickCount() == 2 && !evt.isConsumed()) {
            evt.consume();
            aceptar();
        }
    }//GEN-LAST:event_ClientesTblMousePressed

    private void CancelarBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CancelarBtnActionPerformed
        this.dispose();
    }//GEN-LAST:event_CancelarBtnActionPerformed

    private void CancelarBtnKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_CancelarBtnKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            this.dispose();
        }
    }//GEN-LAST:event_CancelarBtnKeyPressed

    private void AceptarBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AceptarBtnActionPerformed
        aceptar();
    }//GEN-LAST:event_AceptarBtnActionPerformed

    private void AceptarBtnKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AceptarBtnKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            aceptar();
        }
    }//GEN-LAST:event_AceptarBtnKeyPressed

    private void CodigoTxtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_CodigoTxtKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cargarClientes();
        }
    }//GEN-LAST:event_CodigoTxtKeyPressed

    private void IdentificacionTxtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_IdentificacionTxtKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cargarClientes();
        }
    }//GEN-LAST:event_IdentificacionTxtKeyPressed

    private void NombreTxtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NombreTxtKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cargarClientes();
        }
    }//GEN-LAST:event_NombreTxtKeyPressed

    private void CancelarBtnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CancelarBtnMouseEntered
        CancelarBtn.setBackground(new java.awt.Color(32, 116, 174));
        CancelarBtn.setForeground(new java.awt.Color(255,255,255)); 
    }//GEN-LAST:event_CancelarBtnMouseEntered

    private void CancelarBtnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CancelarBtnMouseExited
        CancelarBtn.setBackground(new java.awt.Color(255,255,255));
        CancelarBtn.setForeground(new java.awt.Color(32, 116, 174));  
    }//GEN-LAST:event_CancelarBtnMouseExited

    private void AceptarBtnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_AceptarBtnMouseEntered
       AceptarBtn.setBackground(new java.awt.Color(32, 116, 174));
        AceptarBtn.setForeground(new java.awt.Color(255,255,255)); 
    }//GEN-LAST:event_AceptarBtnMouseEntered

    private void AceptarBtnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_AceptarBtnMouseExited
         AceptarBtn.setBackground(new java.awt.Color(255,255,255));
        AceptarBtn.setForeground(new java.awt.Color(32, 116, 174));  
    }//GEN-LAST:event_AceptarBtnMouseExited

//Cargar la Tabla de Compras Creadas.
    public void cargarClientes() {
        if (CodigoTxt.getText().equals("") && NombreTxt.getText().equals("") && IdentificacionTxt.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Debe indicar algun filtro", "Buscar Clientes", JOptionPane.INFORMATION_MESSAGE);
        } else {
            String codigo_v, nombre_v, identificacion_v;
            codigo_v = "%" + CodigoTxt.getText() + "%";
            nombre_v = "%" + NombreTxt.getText() + "%";
            identificacion_v = "%" + IdentificacionTxt.getText() + "%";
            modelo = new tableModel();
            for (int i = 0; i < 4; i++) {
                modelo.addColumn(ClientesTbl.getColumnName(i));
            }
            ClientesTbl.setModel(modelo);
            configurarTabla();
            oraclebd.getClientes(modelo, idEmp, codigo_v, nombre_v, identificacion_v);
        }
    }

    //Método para Configurar Tamaño de Columnas, Propiedad de No Cambiar Tamaño y Posición de Texto en las Celdas.
    public void configurarTabla() {
        modelo = new DefaultTableModel();
        for (int i = 0; i < 4; i++) {
            modelo.addColumn(ClientesTbl.getColumnName(i));
        }
        ClientesTbl.setModel(modelo);
        ClientesTbl.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        //Id
        ClientesTbl.getColumn(ClientesTbl.getColumnName(0)).setPreferredWidth(100);
        //ClientesTbl.getColumn(ClientesTbl.getColumnName(0)).setResizable(false);
        //Nombre
        ClientesTbl.getColumn(ClientesTbl.getColumnName(1)).setPreferredWidth(300);
        //ClientesTbl.getColumn(ClientesTbl.getColumnName(1)).setResizable(false);
        //Identificacion
        ClientesTbl.getColumn(ClientesTbl.getColumnName(2)).setPreferredWidth(100);
        //ClientesTbl.getColumn(ClientesTbl.getColumnName(2)).setResizable(false);
        //Plazo
        ClientesTbl.getColumn(ClientesTbl.getColumnName(3)).setPreferredWidth(100);
        //ClientesTbl.getColumn(ClientesTbl.getColumnName(3)).setResizable(false);
    }

    public class tableModel extends DefaultTableModel {

        tableModel() {

        }

        @Override
        public boolean isCellEditable(int row, int cols) {
            return false;
        }
    }

//Método para el botón Aceptar.
    public void aceptar() {
        if (ClientesTbl.getSelectedRow() > -1) {
            id = (Integer) (ClientesTbl.getValueAt(ClientesTbl.getSelectedRow(), 0));
            nombre = (String) (ClientesTbl.getValueAt(ClientesTbl.getSelectedRow(), 1));
            identificacion = (String) (ClientesTbl.getValueAt(ClientesTbl.getSelectedRow(), 2));
            plazo = (Integer) (ClientesTbl.getValueAt(ClientesTbl.getSelectedRow(), 3));
            this.dispose();
        }
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public int getPlazo() {
        return plazo;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(BuscarClientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BuscarClientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BuscarClientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BuscarClientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                BuscarClientes dialog = new BuscarClientes(new javax.swing.JFrame(), true, null, -1, null);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AceptarBtn;
    private javax.swing.JButton CancelarBtn;
    private javax.swing.JTable ClientesTbl;
    private javax.swing.JTextField CodigoTxt;
    private javax.swing.JTextField IdentificacionTxt;
    private javax.swing.JTextField NombreTxt;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
