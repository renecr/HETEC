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
public class BuscarArticulos extends javax.swing.JDialog {

    static hetec.OracleBD oraclebd;
    static String usuario_g;
    int id;
    String codigo = "";
    String nombre = "";
    String existencia;
    String precio;
    int idEmp;
    int idLoc;
    int idLista;
    DefaultTableModel modelo;
    String origen_v;

    /**
     * Creates new form BuscarArticulos
     */
    public BuscarArticulos(java.awt.Frame parent, boolean modal, hetec.OracleBD oracle, int empresa, int localizacion, int lista, String origen, String usuario) {
        super(parent, modal);
        initComponents();
        oraclebd = oracle;
        usuario_g = usuario;
        this.setTitle("HETEC Buscar Articulos " + usuario_g);

        idEmp = empresa;
        idLoc = localizacion;
        idLista = lista;
        origen_v = origen;

        CodigoTxt.setDocument(new JTextFieldLimit(45, true));
        NombreTxt.setDocument(new JTextFieldLimit(250, true));
        CategoriaTxt.setDocument(new JTextFieldLimit(250, true));
        CodigoTxt.requestFocus();

        /*ESTILO*/
        //POSITION DE PANTALLA
        this.setLocationRelativeTo(null);
        //FONDO
        getContentPane().setBackground(Color.WHITE);
        //BORDE
        getRootPane().setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.gray));
        //FONDO PANEL
        panel1.setBackground(Color.white);
        //FONDO TABLA
        ArticulosTbl.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        ArticulosTbl.setOpaque(true);
        ArticulosTbl.setFillsViewportHeight(true);
        ArticulosTbl.setBackground(Color.white);
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
        ArticulosTbl = new javax.swing.JTable();
        panel1 = new java.awt.Panel();
        jLabel25 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        CodigoTxt = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        NombreTxt = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        CategoriaTxt = new javax.swing.JTextField();
        CancelarBtn = new javax.swing.JButton();
        AceptarBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setLocationByPlatform(true);
        setMaximumSize(null);
        setUndecorated(true);
        setPreferredSize(new java.awt.Dimension(800, 550));
        setSize(new java.awt.Dimension(800, 550));

        ArticulosTbl.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        ArticulosTbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Id", "Código", "Nombre", "Disponible", "Precio", "Categoria", "Fracciones", "Libre"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        ArticulosTbl.setToolTipText("");
        ArticulosTbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                ArticulosTblMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(ArticulosTbl);

        jLabel25.setFont(new java.awt.Font("Century Gothic", 1, 20)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(32, 116, 174));
        jLabel25.setText("BUSCAR ARTICULOS");

        jLabel18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/jlogo2.png"))); // NOI18N
        jLabel18.setPreferredSize(new java.awt.Dimension(118, 20));

        javax.swing.GroupLayout panel1Layout = new javax.swing.GroupLayout(panel1);
        panel1.setLayout(panel1Layout);
        panel1Layout.setHorizontalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel1Layout.createSequentialGroup()
                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel25)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panel1Layout.setVerticalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel25, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        CodigoTxt.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        CodigoTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                CodigoTxtKeyPressed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel1.setText("Codigo");

        jLabel2.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel2.setText("Descripcion");

        NombreTxt.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        NombreTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NombreTxtKeyPressed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel3.setText("Categoria");

        CategoriaTxt.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        CategoriaTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                CategoriaTxtKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(CodigoTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(NombreTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(CategoriaTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CodigoTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(NombreTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CategoriaTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 12, Short.MAX_VALUE))
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
            .addComponent(panel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(226, 226, 226))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 779, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(AceptarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(CancelarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(33, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 353, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CancelarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(AceptarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ArticulosTblMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ArticulosTblMousePressed
        if (evt.getClickCount() == 2 && !evt.isConsumed()) {
            evt.consume();
            aceptar();
        }
    }//GEN-LAST:event_ArticulosTblMousePressed

    private void CodigoTxtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_CodigoTxtKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cargarArticulos();
        }
    }//GEN-LAST:event_CodigoTxtKeyPressed

    private void NombreTxtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NombreTxtKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cargarArticulos();
        }
    }//GEN-LAST:event_NombreTxtKeyPressed

    private void CategoriaTxtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_CategoriaTxtKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cargarArticulos();
        }
    }//GEN-LAST:event_CategoriaTxtKeyPressed

    private void CancelarBtnKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_CancelarBtnKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            this.dispose();
        }
    }//GEN-LAST:event_CancelarBtnKeyPressed

    private void CancelarBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CancelarBtnActionPerformed
        this.dispose();
    }//GEN-LAST:event_CancelarBtnActionPerformed

    private void CancelarBtnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CancelarBtnMouseExited
        CancelarBtn.setBackground(new java.awt.Color(255,255,255));
        CancelarBtn.setForeground(new java.awt.Color(32, 116, 174));
    }//GEN-LAST:event_CancelarBtnMouseExited

    private void CancelarBtnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CancelarBtnMouseEntered
        CancelarBtn.setBackground(new java.awt.Color(32, 116, 174));
        CancelarBtn.setForeground(new java.awt.Color(255,255,255));
    }//GEN-LAST:event_CancelarBtnMouseEntered

    private void AceptarBtnKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AceptarBtnKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            aceptar();
        }
    }//GEN-LAST:event_AceptarBtnKeyPressed

    private void AceptarBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AceptarBtnActionPerformed
        aceptar();
    }//GEN-LAST:event_AceptarBtnActionPerformed

    private void AceptarBtnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_AceptarBtnMouseExited
        AceptarBtn.setBackground(new java.awt.Color(255,255,255));
        AceptarBtn.setForeground(new java.awt.Color(32, 116, 174));
    }//GEN-LAST:event_AceptarBtnMouseExited

    private void AceptarBtnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_AceptarBtnMouseEntered
        AceptarBtn.setBackground(new java.awt.Color(32, 116, 174));
        AceptarBtn.setForeground(new java.awt.Color(255,255,255));
    }//GEN-LAST:event_AceptarBtnMouseEntered

//Método para el botón Aceptar.
    public void aceptar() {
        if (ArticulosTbl.getSelectedRow() > -1) {
            id = (int) Integer.parseInt(ArticulosTbl.getValueAt(ArticulosTbl.getSelectedRow(), 0).toString());
            codigo = (String) (ArticulosTbl.getValueAt(ArticulosTbl.getSelectedRow(), 1));
            nombre = (String) (ArticulosTbl.getValueAt(ArticulosTbl.getSelectedRow(), 2));
            existencia = (String) (ArticulosTbl.getValueAt(ArticulosTbl.getSelectedRow(), 3));
            precio = (String) (ArticulosTbl.getValueAt(ArticulosTbl.getSelectedRow(), 4));
            this.dispose();
        }
    }

    //Cargar la Tabla de Compras Creadas.
    public void cargarArticulos() {
        if (CodigoTxt.getText().equals("") && NombreTxt.getText().equals("") && CategoriaTxt.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Debe indicar algun filtro", "Buscar Articulos", JOptionPane.INFORMATION_MESSAGE);
        } else {
            String codigo_v, nombre_v, categoria_v;
            codigo_v = "%" + CodigoTxt.getText() + "%";
            nombre_v = "%" + NombreTxt.getText() + "%";
            categoria_v = "%" + CategoriaTxt.getText() + "%";
            modelo = new tableModel();
            for (int i = 0; i < 8; i++) {
                modelo.addColumn(ArticulosTbl.getColumnName(i));
            }
            ArticulosTbl.setModel(modelo);
            configurarTabla();
            oraclebd.getArticulos(modelo, idEmp, idLoc, idLista, codigo_v, nombre_v, categoria_v, origen_v);
        }
    }

    //Método para Configurar Tamaño de Columnas, Propiedad de No Cambiar Tamaño y Posición de Texto en las Celdas.
    public void configurarTabla() {
        modelo = new DefaultTableModel();
        for (int i = 0; i < 8; i++) {
            modelo.addColumn(ArticulosTbl.getColumnName(i));
        }
        ArticulosTbl.setModel(modelo);
        ArticulosTbl.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        //ID
        ArticulosTbl.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(0);
        ArticulosTbl.getTableHeader().getColumnModel().getColumn(0).setMinWidth(0);
        ArticulosTbl.getColumn(ArticulosTbl.getColumnName(0)).setPreferredWidth(0);
        //ArticulosTbl.getColumn(ArticulosTbl.getColumnName(0)).setResizable(false);
        //Codigo
        ArticulosTbl.getColumn(ArticulosTbl.getColumnName(1)).setPreferredWidth(100);
        //ArticulosTbl.getColumn(ArticulosTbl.getColumnName(1)).setResizable(false);
        //Descripcion
        ArticulosTbl.getColumn(ArticulosTbl.getColumnName(2)).setPreferredWidth(300);
        //ArticulosTbl.getColumn(ArticulosTbl.getColumnName(2)).setResizable(false);
        //Disponible
        ArticulosTbl.getColumn(ArticulosTbl.getColumnName(3)).setPreferredWidth(100);
        //ArticulosTbl.getColumn(ArticulosTbl.getColumnName(3)).setResizable(false);
        //Precio
        ArticulosTbl.getColumn(ArticulosTbl.getColumnName(4)).setPreferredWidth(100);
        //ArticulosTbl.getColumn(ArticulosTbl.getColumnName(4)).setResizable(false);
        //Categoria
        ArticulosTbl.getColumn(ArticulosTbl.getColumnName(5)).setPreferredWidth(100);
        //ArticulosTbl.getColumn(ArticulosTbl.getColumnName(5)).setResizable(false);
        //Fracciones
        ArticulosTbl.getTableHeader().getColumnModel().getColumn(6).setMaxWidth(0);
        ArticulosTbl.getTableHeader().getColumnModel().getColumn(6).setMinWidth(0);
        ArticulosTbl.getColumn(ArticulosTbl.getColumnName(6)).setPreferredWidth(0);
        //ArticulosTbl.getColumn(ArticulosTbl.getColumnName(6)).setResizable(false);        
        //Precio Libre
        ArticulosTbl.getTableHeader().getColumnModel().getColumn(7).setMaxWidth(0);
        ArticulosTbl.getTableHeader().getColumnModel().getColumn(7).setMinWidth(0);
        ArticulosTbl.getColumn(ArticulosTbl.getColumnName(7)).setPreferredWidth(0);
        //ArticulosTbl.getColumn(ArticulosTbl.getColumnName(7)).setResizable(false);        
    }

    public class tableModel extends DefaultTableModel {

        tableModel() {

        }

        @Override
        public boolean isCellEditable(int row, int cols) {
            return false;
        }
    }

    public int getId() {
        return id;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getExistencia() {
        return existencia;
    }

    public String getPrecio() {
        return precio;
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
            java.util.logging.Logger.getLogger(BuscarArticulos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BuscarArticulos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BuscarArticulos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BuscarArticulos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                BuscarArticulos dialog = new BuscarArticulos(new javax.swing.JFrame(), true, null, -1, -1, -1, null, null);
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
    private javax.swing.JTable ArticulosTbl;
    private javax.swing.JButton CancelarBtn;
    private javax.swing.JTextField CategoriaTxt;
    private javax.swing.JTextField CodigoTxt;
    private javax.swing.JTextField NombreTxt;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private java.awt.Panel panel1;
    // End of variables declaration//GEN-END:variables
}
