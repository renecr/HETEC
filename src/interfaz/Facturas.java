package interfaz;

//awt
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Font;

//swing
import javax.swing.table.DefaultTableModel;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

//text
import java.text.NumberFormat;

//util
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

//Propios
import hetec.*;
import static interfaz.PagoContado.oraclebd;
import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class Facturas extends javax.swing.JFrame {

    static hetec.OracleBD oraclebd;
    static String usuario_g;
    int idArt;
    boolean fraccion_b, precio_b;
    DefaultTableModel modelo;
    double[] total = new double[4]; //Subtotal, Monto, Impuesto, Total
    String preciou, preciof;

    public Facturas(hetec.OracleBD oracle, String usuario) {
        initComponents();
        oraclebd = oracle;
        usuario_g = usuario;
        this.setTitle("HETEC Ventas " + usuario_g);
        configurarTabla();
        //Llenado de ComboBox Empresas.        
        oraclebd.getEmpresas(EmpresaCB);
        fraccion_b = false;
        FraccionTxt.setEnabled(false);
        precio_b = false;
        PrecioTxt.setEnabled(false);
        /*ESTILO*/
        //POSITION DE PANTALLA
        this.setLocationRelativeTo(null);
        //FONDO
        getContentPane().setBackground(Color.WHITE);
        //BORDE
        getRootPane().setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.gray));
        //FONDO PANEL
        panel1.setBackground(Color.white);
        //FONDO COMBO BOX
        EmpresaCB.setBackground(Color.white);
        CajaCB.setBackground(Color.white);
        DepartamentoCB.setBackground(Color.white);
        MonedaCB.setBackground(Color.white);
        LocalizacionCB.setBackground(Color.white);
        TipoTransaccionCB.setBackground(Color.white);
        ListaCB.setBackground(Color.white);
        TipoDescCB.setBackground(Color.white);
        //FONDO TABLA
        FacturasTbl.setOpaque(true);
        FacturasTbl.setFillsViewportHeight(true);
        FacturasTbl.setBackground(Color.white);
        
        /*TERMINA ESTILO*/

        Action limpiarAction = new AbstractAction("Limpiar F4") {
            @Override
            public void actionPerformed(ActionEvent evt) {
                int seleccion = JOptionPane.showOptionDialog(
                        null, // Componente padre
                        "Los datos no guardados se perdarán, ¿desea limpiar?", //Mensaje
                        "Limpiar", // Título
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null, // null para icono por defecto.
                        new Object[]{"Si", "No"}, // null para YES, NO y CANCEL
                        "No");
                if (seleccion != -1) {
                    if ((seleccion + 1) == 1) {
                        limpiar();
                    }
                }
            }
        };
        String key = "Limpiar";
        LimpiarBtn.setAction(limpiarAction);
        limpiarAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_F4);
        LimpiarBtn.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0), key);
        LimpiarBtn.getActionMap().put(key, limpiarAction);

        Action salirAction = new AbstractAction("Salir F2") {
            @Override
            public void actionPerformed(ActionEvent evt) {
                int seleccion = JOptionPane.showOptionDialog(
                        null, // Componente padre
                        "Los datos no guardados se perdarán, ¿desea salir?", //Mensaje
                        "Salir", // Título
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null, // null para icono por defecto.
                        new Object[]{"Si", "No"}, // null para YES, NO y CANCEL
                        "No");
                if (seleccion != -1) {
                    if ((seleccion + 1) == 1) {
                        System.exit(0);
                    }
                }
            }
        };
        key = "Salir";
        CancelarBtn.setAction(salirAction);
        salirAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_F2);
        CancelarBtn.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0), key);
        CancelarBtn.getActionMap().put(key, salirAction);

        Action registrarAction = new AbstractAction("Registrar F8") {
            @Override
            public void actionPerformed(ActionEvent evt) {
                int seleccion = JOptionPane.showOptionDialog(
                        null, // Componente padre
                        "¿desea registrar?", //Mensaje
                        "Registrar", // Título
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null, // null para icono por defecto.
                        new Object[]{"Si", "No"}, // null para YES, NO y CANCEL
                        "No");
                if (seleccion != -1) {
                    if ((seleccion + 1) == 1) {
                        registrar();
                    }
                }

            }
        };
        key = "Registrar";
        RegistrarBtn.setAction(registrarAction);
        registrarAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_F8);
        RegistrarBtn.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_F8, 0), key);
        RegistrarBtn.getActionMap().put(key, registrarAction);

        Action buscarAction = new AbstractAction("Buscar F3") {
            @Override
            public void actionPerformed(ActionEvent evt) {
                buscarTrans();
            }
        };
        key = "Buscar";
        BuscarTransBtn.setAction(buscarAction);
        buscarAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_F3);
        BuscarTransBtn.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0), key);
        BuscarTransBtn.getActionMap().put(key, buscarAction);

        FocusListener resaltatxt = new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
                Color lightBlue = new Color(240, 240, 240, 255);
                e.getComponent().setBackground(lightBlue);
                JTextField textField = (JTextField) e.getComponent();
                textField.setText(textField.getText());
                textField.selectAll();
            }

            @Override
            public void focusLost(FocusEvent e) {
                e.getComponent().setBackground(Color.WHITE);
            }
        };

        FocusListener resaltacb = new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
                Color lightBlue = new Color(240, 240, 240, 255);
                e.getComponent().setBackground(lightBlue);
            }

            @Override
            public void focusLost(FocusEvent e) {
                e.getComponent().setBackground(Color.WHITE);
            }
        };

        DetalleTxt.addFocusListener(resaltatxt);
        DetalleTxt.setDocument(new JTextFieldLimit(250, true));
        ValorCambioTxt.addFocusListener(resaltatxt);
        ValorCambioTxt.setDocument(new JTextFieldLimit(10, true));
        OtrosCargosTxt.addFocusListener(resaltatxt);
        OtrosCargosTxt.setDocument(new JTextFieldLimit(20, true));
        EmpresaCB.addFocusListener(resaltacb);
        ClienteTxt.addFocusListener(resaltatxt);
        ClienteTxt.setDocument(new JTextFieldLimit(10, true));
        NombreTxt.addFocusListener(resaltatxt);
        NombreTxt.setDocument(new JTextFieldLimit(250, true));
        IdentificacionTxt.addFocusListener(resaltatxt);
        IdentificacionTxt.setDocument(new JTextFieldLimit(20, true));
        TipoTransaccionCB.addFocusListener(resaltacb);
        MonedaCB.addFocusListener(resaltacb);
        LocalizacionCB.addFocusListener(resaltacb);
        ListaCB.addFocusListener(resaltacb);
        DepartamentoCB.addFocusListener(resaltacb);
        CajaCB.addFocusListener(resaltacb);
        CodigoArtTxt.addFocusListener(resaltatxt);
        CodigoArtTxt.setDocument(new JTextFieldLimit(45, true));
        DescripcionArtTxt.addFocusListener(resaltatxt);
        DescripcionArtTxt.setDocument(new JTextFieldLimit(250, true));
        CantidadTxt.addFocusListener(resaltatxt);
        CantidadTxt.setDocument(new JTextFieldLimit(20, true));
        FraccionTxt.addFocusListener(resaltatxt);
        FraccionTxt.setDocument(new JTextFieldLimit(20, true));
        PrecioTxt.addFocusListener(resaltatxt);
        PrecioTxt.setDocument(new JTextFieldLimit(20, true));
        TipoDescCB.addFocusListener(resaltacb);
        ValorTxt.addFocusListener(resaltatxt);
        ValorTxt.setDocument(new JTextFieldLimit(20, true));
        limpiar();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        FacturasTbl = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        CodigoArtTxt = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        SubLbl = new javax.swing.JLabel();
        DescripcionArtTxt = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        PorcentLbl = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        MontoDescLbl = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        CantidadTxt = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        ImpLbl = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        TotLbl = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        ExistenciaLbl = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        PrecioTxt = new javax.swing.JTextField();
        TipoDescCB = new javax.swing.JComboBox();
        jLabel21 = new javax.swing.JLabel();
        ValorTxt = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        IncluirBtn = new javax.swing.JButton();
        jLabel36 = new javax.swing.JLabel();
        FraccionTxt = new javax.swing.JTextField();
        BuscarArtBtn = new javax.swing.JButton();
        panel1 = new java.awt.Panel();
        jLabel38 = new javax.swing.JLabel();
        MenuBtn = new javax.swing.JButton();
        jLabel37 = new javax.swing.JLabel();
        LimpiarBtn = new javax.swing.JButton();
        CancelarBtn = new javax.swing.JButton();
        RegistrarBtn = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        DescuentoLbl = new javax.swing.JLabel();
        SubtotalLbl = new javax.swing.JLabel();
        TotalLbl = new javax.swing.JLabel();
        ImpuestoLbl = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        TransaccionLbl = new javax.swing.JLabel();
        BuscarTransBtn = new javax.swing.JButton();
        ProformaLbl = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        EmpresaCB = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        LocalizacionCB = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        TipoTransaccionCB = new javax.swing.JComboBox();
        jLabel14 = new javax.swing.JLabel();
        MonedaCB = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        ValorCambioTxt = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        DepartamentoCB = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        CajaCB = new javax.swing.JComboBox();
        jLabel9 = new javax.swing.JLabel();
        ClienteTxt = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        NombreTxt = new javax.swing.JTextField();
        BuscarClteBtn = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        IdentificacionTxt = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        TipoLbl = new javax.swing.JLabel();
        CrearClteBtn = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        VencimientoLbl = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        OtrosCargosTxt = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        ListaCB = new javax.swing.JComboBox();
        jLabel11 = new javax.swing.JLabel();
        DetalleTxt = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        DisponibleLbl = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        PlazoLbl = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("HETEC Facturas");
        setMinimumSize(new java.awt.Dimension(1024, 550));
        setSize(new java.awt.Dimension(1024, 550));

        FacturasTbl.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        FacturasTbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Linea", "Articulo", "Código", "Descripción", "Cantidad", "Fraccion", "Precio", "Subtotal", "% Desc.", "Descuento", "Impuesto", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        FacturasTbl.setCellSelectionEnabled(true);
        FacturasTbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                FacturasTblMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(FacturasTbl);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setForeground(new java.awt.Color(32, 116, 174));
        jPanel1.setMaximumSize(new java.awt.Dimension(800, 200));
        jPanel1.setMinimumSize(new java.awt.Dimension(800, 200));
        jPanel1.setPreferredSize(new java.awt.Dimension(800, 200));

        jLabel19.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel19.setText("Artículo:");

        CodigoArtTxt.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        CodigoArtTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                CodigoArtTxtKeyPressed(evt);
            }
        });

        jLabel26.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel26.setText("Subtotal:");

        SubLbl.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        SubLbl.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        SubLbl.setText("0");

        DescripcionArtTxt.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        DescripcionArtTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DescripcionArtTxtKeyPressed(evt);
            }
        });

        jLabel27.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel27.setText("% Descuento:");

        PorcentLbl.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        PorcentLbl.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        PorcentLbl.setText("0");

        jLabel29.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel29.setText("Descuento:");

        MontoDescLbl.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        MontoDescLbl.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        MontoDescLbl.setText("0");

        jLabel20.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel20.setText("Unidades:");

        jLabel32.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel32.setText("Descripcion:");

        CantidadTxt.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        CantidadTxt.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        CantidadTxt.setText("0");
        CantidadTxt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                CantidadTxtFocusLost(evt);
            }
        });
        CantidadTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                CantidadTxtKeyPressed(evt);
            }
        });

        jLabel31.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel31.setText("Impuesto:");

        ImpLbl.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        ImpLbl.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        ImpLbl.setText("0");

        jLabel33.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel33.setText("Total:");

        TotLbl.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        TotLbl.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        TotLbl.setText("0");

        jLabel34.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel34.setText("Disponible:");

        ExistenciaLbl.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        ExistenciaLbl.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        ExistenciaLbl.setText("0");

        jLabel35.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel35.setText("Precio:");

        PrecioTxt.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        PrecioTxt.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        PrecioTxt.setText("0");
        PrecioTxt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                PrecioTxtFocusLost(evt);
            }
        });
        PrecioTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PrecioTxtKeyPressed(evt);
            }
        });

        TipoDescCB.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        TipoDescCB.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Porcentaje", "Monto" }));
        TipoDescCB.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TipoDescCBKeyPressed(evt);
            }
        });

        jLabel21.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel21.setText("Tipo Descuento:");

        ValorTxt.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        ValorTxt.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        ValorTxt.setText("0");
        ValorTxt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                ValorTxtFocusLost(evt);
            }
        });
        ValorTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ValorTxtKeyPressed(evt);
            }
        });

        jLabel24.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel24.setText("Valor:");

        IncluirBtn.setBackground(new java.awt.Color(32, 116, 174));
        IncluirBtn.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        IncluirBtn.setForeground(new java.awt.Color(255, 255, 255));
        IncluirBtn.setText("Incluir");
        IncluirBtn.setBorder(null);
        IncluirBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        IncluirBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                IncluirBtnMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                IncluirBtnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                IncluirBtnMouseExited(evt);
            }
        });
        IncluirBtn.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                IncluirBtnKeyPressed(evt);
            }
        });

        jLabel36.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel36.setText("Fracciones:");

        FraccionTxt.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        FraccionTxt.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        FraccionTxt.setText("0");
        FraccionTxt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                FraccionTxtFocusLost(evt);
            }
        });
        FraccionTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                FraccionTxtKeyPressed(evt);
            }
        });

        BuscarArtBtn.setBackground(new java.awt.Color(255, 255, 255));
        BuscarArtBtn.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        BuscarArtBtn.setForeground(new java.awt.Color(32, 116, 174));
        BuscarArtBtn.setText("Buscar F6");
        BuscarArtBtn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(32, 116, 174)));
        BuscarArtBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        BuscarArtBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BuscarArtBtnMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                BuscarArtBtnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                BuscarArtBtnMouseExited(evt);
            }
        });
        BuscarArtBtn.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BuscarArtBtnKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel24)
                            .addComponent(ValorTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(7, 7, 7)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel26)
                            .addComponent(SubLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(PorcentLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel27))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(MontoDescLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel29))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ImpLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel31))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel33)
                            .addComponent(TotLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(IncluirBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(CodigoArtTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel19))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel32)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(BuscarArtBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(DescripcionArtTxt))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ExistenciaLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel34))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(CantidadTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel20))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(FraccionTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel36))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel35)
                                .addGap(77, 77, 77)
                                .addComponent(jLabel21))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(PrecioTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(TipoDescCB, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(116, 116, 116))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel19)
                            .addComponent(jLabel32)
                            .addComponent(BuscarArtBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel34)
                            .addComponent(jLabel20)
                            .addComponent(jLabel36))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(CodigoArtTxt)
                            .addComponent(DescripcionArtTxt)
                            .addComponent(ExistenciaLbl)
                            .addComponent(CantidadTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(FraccionTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel35)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(PrecioTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(TipoDescCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel21))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel24)
                        .addComponent(jLabel26)
                        .addComponent(jLabel27)
                        .addComponent(jLabel29)
                        .addComponent(jLabel31))
                    .addComponent(jLabel33, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(PorcentLbl, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(SubLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ValorTxt))
                    .addComponent(IncluirBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(MontoDescLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(TotLbl, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE)
                        .addComponent(ImpLbl, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        panel1.setName(""); // NOI18N

        jLabel38.setFont(new java.awt.Font("Century Gothic", 1, 20)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(32, 116, 174));
        jLabel38.setText("VENTAS");

        MenuBtn.setBackground(new java.awt.Color(255, 255, 255));
        MenuBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/menu_icon.png"))); // NOI18N
        MenuBtn.setToolTipText("Menu");
        MenuBtn.setBorder(null);
        MenuBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        MenuBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                MenuBtnMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                MenuBtnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                MenuBtnMouseExited(evt);
            }
        });

        jLabel37.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/jlogo2.png"))); // NOI18N
        jLabel37.setPreferredSize(new java.awt.Dimension(118, 20));

        LimpiarBtn.setBackground(new java.awt.Color(255, 255, 255));
        LimpiarBtn.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        LimpiarBtn.setForeground(new java.awt.Color(32, 116, 174));
        LimpiarBtn.setText("Limpiar (F4)");
        LimpiarBtn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(32, 116, 174)));
        LimpiarBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        LimpiarBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                LimpiarBtnMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                LimpiarBtnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                LimpiarBtnMouseExited(evt);
            }
        });

        CancelarBtn.setBackground(new java.awt.Color(255, 255, 255));
        CancelarBtn.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        CancelarBtn.setForeground(new java.awt.Color(32, 116, 174));
        CancelarBtn.setText("Salir (F2)");
        CancelarBtn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(32, 116, 174)));
        CancelarBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        CancelarBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                CancelarBtnMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                CancelarBtnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                CancelarBtnMouseExited(evt);
            }
        });

        RegistrarBtn.setBackground(new java.awt.Color(255, 255, 255));
        RegistrarBtn.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        RegistrarBtn.setForeground(new java.awt.Color(32, 116, 174));
        RegistrarBtn.setText("Registrar (F8)");
        RegistrarBtn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(32, 116, 174)));
        RegistrarBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        RegistrarBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                RegistrarBtnMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                RegistrarBtnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                RegistrarBtnMouseExited(evt);
            }
        });

        javax.swing.GroupLayout panel1Layout = new javax.swing.GroupLayout(panel1);
        panel1.setLayout(panel1Layout);
        panel1Layout.setHorizontalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel1Layout.createSequentialGroup()
                .addContainerGap(32, Short.MAX_VALUE)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel1Layout.createSequentialGroup()
                        .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(53, 53, 53))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel1Layout.createSequentialGroup()
                        .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LimpiarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panel1Layout.createSequentialGroup()
                                .addComponent(MenuBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel38))
                            .addComponent(CancelarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(RegistrarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(29, 29, 29))))
        );
        panel1Layout.setVerticalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel1Layout.createSequentialGroup()
                .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(MenuBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LimpiarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addComponent(CancelarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addComponent(RegistrarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jPanel2.setMaximumSize(new java.awt.Dimension(200, 200));
        jPanel2.setMinimumSize(new java.awt.Dimension(200, 200));
        jPanel2.setPreferredSize(new java.awt.Dimension(200, 200));

        jLabel28.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(32, 116, 174));
        jLabel28.setText("Total:");

        jLabel25.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(32, 116, 174));
        jLabel25.setText("Impuesto:");

        jLabel22.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(32, 116, 174));
        jLabel22.setText("Subtotal:");

        jLabel23.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(32, 116, 174));
        jLabel23.setText("Descuento:");

        DescuentoLbl.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        DescuentoLbl.setForeground(new java.awt.Color(32, 116, 174));
        DescuentoLbl.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        DescuentoLbl.setText("0");

        SubtotalLbl.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        SubtotalLbl.setForeground(new java.awt.Color(32, 116, 174));
        SubtotalLbl.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        SubtotalLbl.setText("0");
        SubtotalLbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                SubtotalLblMouseClicked(evt);
            }
        });

        TotalLbl.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        TotalLbl.setForeground(new java.awt.Color(32, 116, 174));
        TotalLbl.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        TotalLbl.setText("0");

        ImpuestoLbl.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        ImpuestoLbl.setForeground(new java.awt.Color(32, 116, 174));
        ImpuestoLbl.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        ImpuestoLbl.setText("0");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel22, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel23, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel25, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel28, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(ImpuestoLbl, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
                    .addComponent(DescuentoLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(SubtotalLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(TotalLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(20, 20, 20))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(SubtotalLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel23)
                    .addComponent(DescuentoLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel25)
                    .addComponent(ImpuestoLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel28)
                    .addComponent(TotalLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel1.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel1.setText("Transacción:");

        TransaccionLbl.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        TransaccionLbl.setText("0");

        BuscarTransBtn.setBackground(new java.awt.Color(255, 255, 255));
        BuscarTransBtn.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        BuscarTransBtn.setForeground(new java.awt.Color(32, 116, 174));
        BuscarTransBtn.setText("Buscar F3");
        BuscarTransBtn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(32, 116, 174)));
        BuscarTransBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        BuscarTransBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BuscarTransBtnMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                BuscarTransBtnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                BuscarTransBtnMouseExited(evt);
            }
        });
        BuscarTransBtn.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BuscarTransBtnKeyPressed(evt);
            }
        });

        ProformaLbl.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        ProformaLbl.setText("0");

        jLabel2.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel2.setText("Proforma:");

        EmpresaCB.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        EmpresaCB.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                EmpresaCBItemStateChanged(evt);
            }
        });
        EmpresaCB.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                EmpresaCBKeyPressed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel5.setText("Empresa:");

        LocalizacionCB.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        LocalizacionCB.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                LocalizacionCBKeyPressed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel4.setText("Localización:");

        TipoTransaccionCB.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        TipoTransaccionCB.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TipoTransaccionCBKeyPressed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel14.setText("Tipo Transaccion:");

        MonedaCB.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        MonedaCB.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                MonedaCBKeyPressed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel7.setText("Moneda:");

        ValorCambioTxt.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        ValorCambioTxt.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        ValorCambioTxt.setText("0");
        ValorCambioTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ValorCambioTxtKeyPressed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel10.setText("Valor de Cambio:");

        DepartamentoCB.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        DepartamentoCB.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                DepartamentoCBItemStateChanged(evt);
            }
        });
        DepartamentoCB.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DepartamentoCBKeyPressed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel6.setText("Departamento:");

        CajaCB.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        CajaCB.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                CajaCBKeyPressed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel9.setText("Caja:");

        ClienteTxt.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        ClienteTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ClienteTxtKeyPressed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel8.setText("Cliente:");

        jLabel30.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel30.setText("Nombre:");

        NombreTxt.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        NombreTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NombreTxtKeyPressed(evt);
            }
        });

        BuscarClteBtn.setBackground(new java.awt.Color(255, 255, 255));
        BuscarClteBtn.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        BuscarClteBtn.setForeground(new java.awt.Color(32, 116, 174));
        BuscarClteBtn.setText("Buscar F6");
        BuscarClteBtn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(32, 116, 174)));
        BuscarClteBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        BuscarClteBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BuscarClteBtnMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                BuscarClteBtnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                BuscarClteBtnMouseExited(evt);
            }
        });
        BuscarClteBtn.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BuscarClteBtnKeyPressed(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel15.setText("Identificacion:");

        IdentificacionTxt.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        IdentificacionTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                IdentificacionTxtKeyPressed(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel17.setText("Tipo");

        TipoLbl.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N

        CrearClteBtn.setBackground(new java.awt.Color(255, 255, 255));
        CrearClteBtn.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        CrearClteBtn.setForeground(new java.awt.Color(32, 116, 174));
        CrearClteBtn.setText("Crear");
        CrearClteBtn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(32, 116, 174)));
        CrearClteBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        CrearClteBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                CrearClteBtnMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                CrearClteBtnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                CrearClteBtnMouseExited(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel18.setText("Vencimiento");

        VencimientoLbl.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N

        jLabel12.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel12.setText("Otros Cargos:");

        OtrosCargosTxt.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        OtrosCargosTxt.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        OtrosCargosTxt.setText("0");
        OtrosCargosTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                OtrosCargosTxtKeyPressed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel13.setText("Lista:");

        ListaCB.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        ListaCB.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ListaCBKeyPressed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel11.setText("Detalle:");

        DetalleTxt.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        DetalleTxt.setText("FACTURA");
        DetalleTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DetalleTxtKeyPressed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(32, 116, 174));
        jLabel3.setText("Disponible CxC:");

        DisponibleLbl.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        DisponibleLbl.setForeground(new java.awt.Color(32, 116, 174));
        DisponibleLbl.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        DisponibleLbl.setText("0");

        jLabel16.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(32, 116, 174));
        jLabel16.setText("Plazo:");

        PlazoLbl.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        PlazoLbl.setForeground(new java.awt.Color(32, 116, 174));
        PlazoLbl.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        PlazoLbl.setText("0");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel1)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel14)
                                            .addComponent(TipoTransaccionCB, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(7, 7, 7)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel7)
                                            .addComponent(MonedaCB, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(TransaccionLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(BuscarTransBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(ProformaLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(ValorCambioTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel5)
                                    .addComponent(DepartamentoCB, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(EmpresaCB, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(LocalizacionCB, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(CajaCB, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel4)
                                            .addComponent(jLabel9))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 47, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(ClienteTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel8)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel30)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(NombreTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(BuscarClteBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel15)
                                    .addComponent(IdentificacionTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel17)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(TipoLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(CrearClteBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(DisponibleLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(77, 77, 77)
                                .addComponent(jLabel16)
                                .addGap(11, 11, 11)
                                .addComponent(PlazoLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel18)
                                    .addComponent(VencimientoLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(2, 2, 2)
                                        .addComponent(jLabel12))
                                    .addComponent(OtrosCargosTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel13)
                                    .addComponent(ListaCB, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(DetalleTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel11))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(panel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(6, 6, 6)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(ProformaLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(BuscarTransBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(TransaccionLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel10)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(ValorCambioTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addComponent(jLabel14)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(TipoTransaccionCB, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addComponent(jLabel5)
                                        .addGap(6, 6, 6)
                                        .addComponent(EmpresaCB, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addGap(6, 6, 6)
                                        .addComponent(LocalizacionCB, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addComponent(jLabel6)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(DepartamentoCB, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addComponent(jLabel9)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(CajaCB, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(35, 35, 35)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel7)
                                        .addGap(29, 29, 29))
                                    .addComponent(MonedaCB, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ClienteTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel30)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel15)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(IdentificacionTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(NombreTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(BuscarClteBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel17)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(TipoLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(CrearClteBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel18)
                                    .addComponent(jLabel12))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(OtrosCargosTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(VencimientoLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addGap(29, 29, 29))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(ListaCB, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(DetalleTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(PlazoLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(DisponibleLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel16))))
                    .addComponent(panel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 163, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BuscarTransBtnKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BuscarTransBtnKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            buscarTrans();
        }
    }//GEN-LAST:event_BuscarTransBtnKeyPressed

    private void BuscarTransBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BuscarTransBtnMouseClicked
        // TODO add your handling code here:
        buscarTrans();
    }//GEN-LAST:event_BuscarTransBtnMouseClicked

    private void EmpresaCBItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_EmpresaCBItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED) {
        }
        {
            Item item = (Item) EmpresaCB.getSelectedItem();
            oraclebd.getLocalizaciones(LocalizacionCB, item.getId(), usuario_g);
            oraclebd.getDepartamentos(DepartamentoCB, item.getId(), usuario_g);
            oraclebd.getMonedas(MonedaCB, item.getId());
            oraclebd.getfacTipos(TipoTransaccionCB, item.getId());
            oraclebd.getListas(ListaCB, item.getId());
            //Item dep = (Item) DepartamentoCB.getSelectedItem();
            //oraclebd.getCajas(CajaCB, item.getId(), dep.getId());
            ClienteTxt.requestFocus();
        }
    }//GEN-LAST:event_EmpresaCBItemStateChanged

    private void DetalleTxtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DetalleTxtKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            CodigoArtTxt.requestFocus();
        }
    }//GEN-LAST:event_DetalleTxtKeyPressed

    private void ValorCambioTxtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ValorCambioTxtKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            DepartamentoCB.requestFocus();
        }
    }//GEN-LAST:event_ValorCambioTxtKeyPressed

    private void OtrosCargosTxtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_OtrosCargosTxtKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            DetalleTxt.requestFocus();
        }
    }//GEN-LAST:event_OtrosCargosTxtKeyPressed

    private void EmpresaCBKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_EmpresaCBKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            LocalizacionCB.requestFocus();
        }
    }//GEN-LAST:event_EmpresaCBKeyPressed

    private void ClienteTxtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ClienteTxtKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (ClienteTxt.getText().length() == 0) {
                BuscarClteBtn.requestFocus();
            } else {
                String cliente[] = oraclebd.getCliente(Integer.valueOf(ClienteTxt.getText()));
                if (cliente[0] != null) {
                    NombreTxt.setText(cliente[0]);
                    IdentificacionTxt.setText(cliente[1]);
                    PlazoLbl.setText(cliente[2]);
                    DisponibleLbl.setText(cliente[3]);
                    NombreTxt.requestFocus();
                } else {
                    ClienteTxt.requestFocus();
                }

            }
        }
        if (evt.getKeyCode() == KeyEvent.VK_F6) {
            buscarCliente();
        }
    }//GEN-LAST:event_ClienteTxtKeyPressed

    private void BuscarClteBtnKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BuscarClteBtnKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            buscarCliente();
        }
    }//GEN-LAST:event_BuscarClteBtnKeyPressed

    private void BuscarClteBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BuscarClteBtnMouseClicked
        // TODO add your handling code here:
        buscarCliente();
    }//GEN-LAST:event_BuscarClteBtnMouseClicked

    private void NombreTxtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NombreTxtKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            IdentificacionTxt.requestFocus();
        }
    }//GEN-LAST:event_NombreTxtKeyPressed

    private void IdentificacionTxtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_IdentificacionTxtKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            OtrosCargosTxt.requestFocus();
        }
    }//GEN-LAST:event_IdentificacionTxtKeyPressed

    private void MonedaCBKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_MonedaCBKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            ValorCambioTxt.requestFocus();
        }
    }//GEN-LAST:event_MonedaCBKeyPressed

    private void LocalizacionCBKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_LocalizacionCBKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            TipoTransaccionCB.requestFocus();
        }
    }//GEN-LAST:event_LocalizacionCBKeyPressed

    private void ListaCBKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ListaCBKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            MonedaCB.requestFocus();
        }
    }//GEN-LAST:event_ListaCBKeyPressed

    private void DepartamentoCBKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DepartamentoCBKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            CajaCB.requestFocus();
        }
    }//GEN-LAST:event_DepartamentoCBKeyPressed

    private void CajaCBKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_CajaCBKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            ClienteTxt.requestFocus();
        }
    }//GEN-LAST:event_CajaCBKeyPressed

    private void CodigoArtTxtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_CodigoArtTxtKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (CodigoArtTxt.getText().equals("")) {
                buscarArt();
            } else {
                cargarArt();
            }
        }
        if (evt.getKeyCode() == KeyEvent.VK_F6) {
            buscarArt();
        }
    }//GEN-LAST:event_CodigoArtTxtKeyPressed

    private void DescripcionArtTxtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DescripcionArtTxtKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            CantidadTxt.requestFocus();
        }
    }//GEN-LAST:event_DescripcionArtTxtKeyPressed

    private void BuscarArtBtnKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BuscarArtBtnKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            buscarArt();
        }
    }//GEN-LAST:event_BuscarArtBtnKeyPressed

    private void CantidadTxtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_CantidadTxtKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (fraccion_b == true) {
                FraccionTxt.requestFocus();
            } else if (precio_b == true) {
                PrecioTxt.requestFocus();
            } else {
                TipoDescCB.requestFocus();
            }
        }
    }//GEN-LAST:event_CantidadTxtKeyPressed

    private void TipoDescCBKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TipoDescCBKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            ValorTxt.requestFocus();
        }
    }//GEN-LAST:event_TipoDescCBKeyPressed

    private void ValorTxtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ValorTxtKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            IncluirBtn.requestFocus();
        }
    }//GEN-LAST:event_ValorTxtKeyPressed

    private void IncluirBtnKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_IncluirBtnKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            try {
                incluirDetalle();
            } catch (MyException e) {
                JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_IncluirBtnKeyPressed

    private void IncluirBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_IncluirBtnMouseClicked
        // TODO add your handling code here:
        try {
            incluirDetalle();
        } catch (MyException e) {
            JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_IncluirBtnMouseClicked

    private void LimpiarBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LimpiarBtnMouseClicked
        // TODO add your handling code here:
        int seleccion = JOptionPane.showOptionDialog(
                this, // Componente padre
                "Los datos no guardados se perdarán, ¿desea limpiar?", //Mensaje
                "Limpiar", // Título
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, // null para icono por defecto.
                new Object[]{"Si", "No"}, // null para YES, NO y CANCEL
                "No");
        if (seleccion != -1) {
            if ((seleccion + 1) == 1) {
                limpiar();
            }
        }
    }//GEN-LAST:event_LimpiarBtnMouseClicked

    private void BuscarArtBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BuscarArtBtnMouseClicked
        // TODO add your handling code here:
        buscarArt();
    }//GEN-LAST:event_BuscarArtBtnMouseClicked

    private void TipoTransaccionCBKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TipoTransaccionCBKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            MonedaCB.requestFocus();
        }
    }//GEN-LAST:event_TipoTransaccionCBKeyPressed

    private void PrecioTxtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PrecioTxtKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            preciou = PrecioTxt.getText();
            preciof = PrecioTxt.getText();
            TipoDescCB.requestFocus();
        }
    }//GEN-LAST:event_PrecioTxtKeyPressed

    private void FraccionTxtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_FraccionTxtKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (precio_b == true) {
                PrecioTxt.requestFocus();
            } else {
                TipoDescCB.requestFocus();
            }

        }
    }//GEN-LAST:event_FraccionTxtKeyPressed

    private void CantidadTxtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_CantidadTxtFocusLost
        // TODO add your handling code here:
        actualizarLinea();
    }//GEN-LAST:event_CantidadTxtFocusLost

    private void FraccionTxtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_FraccionTxtFocusLost
        // TODO add your handling code here:
        actualizarLinea();
    }//GEN-LAST:event_FraccionTxtFocusLost

    private void PrecioTxtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_PrecioTxtFocusLost
        // TODO add your handling code here:
        actualizarLinea();
    }//GEN-LAST:event_PrecioTxtFocusLost

    private void ValorTxtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_ValorTxtFocusLost
        // TODO add your handling code here:
        actualizarLinea();
    }//GEN-LAST:event_ValorTxtFocusLost

    private void MenuBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MenuBtnMouseClicked
        // TODO add your handling code here:
        int seleccion = JOptionPane.showOptionDialog(
                this, // Componente padre
                "Los datos no guardados se perdarán, ¿desea salir?", //Mensaje
                "Salir", // Título
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, // null para icono por defecto.
                new Object[]{"Si", "No"}, // null para YES, NO y CANCEL
                "No");
        if (seleccion != -1) {
            if ((seleccion + 1) == 1) {
                JFrame Menu = new Menu(oraclebd, usuario_g);
                Menu.setVisible(true);
                this.dispose();
            }
        }
    }//GEN-LAST:event_MenuBtnMouseClicked

    private void CancelarBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CancelarBtnMouseClicked
        // TODO add your handling code here:
        int seleccion = JOptionPane.showOptionDialog(
                null, // Componente padre
                "Los datos no guardados se perdarán, ¿desea salir?", //Mensaje
                "Salir", // Título
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, // null para icono por defecto.
                new Object[]{"Si", "No"}, // null para YES, NO y CANCEL
                "No");
        if (seleccion != -1) {
            if ((seleccion + 1) == 1) {
                System.exit(0);
            }
        }
    }//GEN-LAST:event_CancelarBtnMouseClicked

    private void RegistrarBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_RegistrarBtnMouseClicked
        // TODO add your handling code here:
        int seleccion = JOptionPane.showOptionDialog(
                this, // Componente padre
                "¿desea registrar?", //Mensaje
                "Registrar", // Título
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, // null para icono por defecto.
                new Object[]{"Si", "No"}, // null para YES, NO y CANCEL
                "No");
        if (seleccion != -1) {
            if ((seleccion + 1) == 1) {
                registrar();
            }
        }
    }//GEN-LAST:event_RegistrarBtnMouseClicked

    private void FacturasTblMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_FacturasTblMousePressed
        // TODO add your handling code here:
        if (evt.getClickCount() == 2 && !evt.isConsumed()) {
            evt.consume();
            if (FacturasTbl.getSelectedRow() > -1) {
                idArt = Integer.parseInt(FacturasTbl.getValueAt(FacturasTbl.getSelectedRow(), 1).toString());
                CodigoArtTxt.setText(FacturasTbl.getValueAt(FacturasTbl.getSelectedRow(), 2).toString());
                DescripcionArtTxt.setText(FacturasTbl.getValueAt(FacturasTbl.getSelectedRow(), 3).toString());
                CantidadTxt.setText(FacturasTbl.getValueAt(FacturasTbl.getSelectedRow(), 4).toString());
                FraccionTxt.setText(FacturasTbl.getValueAt(FacturasTbl.getSelectedRow(), 5).toString());
                PrecioTxt.setText(FacturasTbl.getValueAt(FacturasTbl.getSelectedRow(), 6).toString());
                SubLbl.setText(FacturasTbl.getValueAt(FacturasTbl.getSelectedRow(), 7).toString());
                PorcentLbl.setText(FacturasTbl.getValueAt(FacturasTbl.getSelectedRow(), 8).toString());
                MontoDescLbl.setText(FacturasTbl.getValueAt(FacturasTbl.getSelectedRow(), 9).toString());
                ImpLbl.setText(FacturasTbl.getValueAt(FacturasTbl.getSelectedRow(), 10).toString());
                TotLbl.setText(FacturasTbl.getValueAt(FacturasTbl.getSelectedRow(), 11).toString());
                oraclebd.borrarDetalleFAC(Integer.parseInt(FacturasTbl.getValueAt(FacturasTbl.getSelectedRow(), 0).toString()));

                configurarTabla();
                for (int i = 0; i < 4; i++) {
                    total[i] = 0;
                }
                oraclebd.getDetallesFact(modelo, Integer.parseInt(TransaccionLbl.getText()), total);
                NumberFormat formatoNumero = NumberFormat.getNumberInstance();
                SubtotalLbl.setText(String.valueOf(formatoNumero.format(total[0])));
                DescuentoLbl.setText(String.valueOf(formatoNumero.format(total[1])));
                ImpuestoLbl.setText(String.valueOf(formatoNumero.format(total[2])));
                TotalLbl.setText(String.valueOf(formatoNumero.format(total[3])));
                CodigoArtTxt.requestFocus();
            }
        }
    }//GEN-LAST:event_FacturasTblMousePressed

    private void BuscarTransBtnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BuscarTransBtnMouseEntered
        BuscarTransBtn.setBackground(new java.awt.Color(32, 116, 174));
        BuscarTransBtn.setForeground(new java.awt.Color(255, 255, 255));
    }//GEN-LAST:event_BuscarTransBtnMouseEntered

    private void BuscarTransBtnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BuscarTransBtnMouseExited
        BuscarTransBtn.setBackground(new java.awt.Color(255, 255, 255));
        BuscarTransBtn.setForeground(new java.awt.Color(32, 116, 174));
    }//GEN-LAST:event_BuscarTransBtnMouseExited

    private void BuscarClteBtnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BuscarClteBtnMouseEntered
        BuscarClteBtn.setBackground(new java.awt.Color(32, 116, 174));
        BuscarClteBtn.setForeground(new java.awt.Color(255, 255, 255));
    }//GEN-LAST:event_BuscarClteBtnMouseEntered

    private void BuscarClteBtnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BuscarClteBtnMouseExited
        BuscarClteBtn.setBackground(new java.awt.Color(255, 255, 255));
        BuscarClteBtn.setForeground(new java.awt.Color(32, 116, 174));
    }//GEN-LAST:event_BuscarClteBtnMouseExited

    private void BuscarArtBtnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BuscarArtBtnMouseEntered
        BuscarArtBtn.setBackground(new java.awt.Color(32, 116, 174));
        BuscarArtBtn.setForeground(new java.awt.Color(255, 255, 255));
    }//GEN-LAST:event_BuscarArtBtnMouseEntered

    private void BuscarArtBtnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BuscarArtBtnMouseExited
        BuscarArtBtn.setBackground(new java.awt.Color(255, 255, 255));
        BuscarArtBtn.setForeground(new java.awt.Color(32, 116, 174));
    }//GEN-LAST:event_BuscarArtBtnMouseExited

    private void IncluirBtnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_IncluirBtnMouseEntered
        IncluirBtn.setForeground(new java.awt.Color(32, 116, 174));
        IncluirBtn.setBackground(new java.awt.Color(255, 255, 255));
    }//GEN-LAST:event_IncluirBtnMouseEntered

    private void IncluirBtnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_IncluirBtnMouseExited
        IncluirBtn.setForeground(new java.awt.Color(255, 255, 255));
        IncluirBtn.setBackground(new java.awt.Color(32, 116, 174));
    }//GEN-LAST:event_IncluirBtnMouseExited

    private void LimpiarBtnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LimpiarBtnMouseEntered
        LimpiarBtn.setBackground(new java.awt.Color(32, 116, 174));
        LimpiarBtn.setForeground(new java.awt.Color(255, 255, 255));
    }//GEN-LAST:event_LimpiarBtnMouseEntered

    private void LimpiarBtnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LimpiarBtnMouseExited
        LimpiarBtn.setBackground(new java.awt.Color(255, 255, 255));
        LimpiarBtn.setForeground(new java.awt.Color(32, 116, 174));
    }//GEN-LAST:event_LimpiarBtnMouseExited

    private void CancelarBtnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CancelarBtnMouseEntered
        CancelarBtn.setBackground(new java.awt.Color(32, 116, 174));
        CancelarBtn.setForeground(new java.awt.Color(255, 255, 255));
    }//GEN-LAST:event_CancelarBtnMouseEntered

    private void CancelarBtnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CancelarBtnMouseExited
        CancelarBtn.setBackground(new java.awt.Color(255, 255, 255));
        CancelarBtn.setForeground(new java.awt.Color(32, 116, 174));
    }//GEN-LAST:event_CancelarBtnMouseExited

    private void RegistrarBtnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_RegistrarBtnMouseEntered
        RegistrarBtn.setBackground(new java.awt.Color(32, 116, 174));
        RegistrarBtn.setForeground(new java.awt.Color(255, 255, 255));
    }//GEN-LAST:event_RegistrarBtnMouseEntered

    private void RegistrarBtnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_RegistrarBtnMouseExited
        RegistrarBtn.setBackground(new java.awt.Color(255, 255, 255));
        RegistrarBtn.setForeground(new java.awt.Color(32, 116, 174));
    }//GEN-LAST:event_RegistrarBtnMouseExited

    private void MenuBtnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MenuBtnMouseEntered
        MenuBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/menuhover_icon.png"))); // NOI18N
        jLabel38.setForeground(new java.awt.Color(253, 141, 42));
    }//GEN-LAST:event_MenuBtnMouseEntered

    private void MenuBtnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MenuBtnMouseExited
        MenuBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/menu_icon.png"))); // NOI18N
        jLabel38.setForeground(new java.awt.Color(32, 116, 174));
    }//GEN-LAST:event_MenuBtnMouseExited

    private void CrearClteBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CrearClteBtnMouseClicked
        // TODO add your handling code here:
        Item item;
        item = (Item) EmpresaCB.getSelectedItem();
        CrearCliente clientesFrm = new CrearCliente(this, true, oraclebd, item.getId(), usuario_g);
        clientesFrm.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent ev) {
                //ClienteTxt.setText(String.valueOf(clientesFrm.getCodigo()));
                ClienteTxt.requestFocus();
            }
        });
        clientesFrm.setVisible(true);
    }//GEN-LAST:event_CrearClteBtnMouseClicked

    private void CrearClteBtnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CrearClteBtnMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_CrearClteBtnMouseEntered

    private void CrearClteBtnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CrearClteBtnMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_CrearClteBtnMouseExited

    private void DepartamentoCBItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_DepartamentoCBItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED) {
        }
        {
            Item item = (Item) EmpresaCB.getSelectedItem();
            Item dep = (Item) DepartamentoCB.getSelectedItem();
            oraclebd.getCajas(CajaCB, item.getId(), dep.getId());
            ClienteTxt.requestFocus();
        }
    }//GEN-LAST:event_DepartamentoCBItemStateChanged

    private void SubtotalLblMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SubtotalLblMouseClicked
        // TODO add your handling code here:
       JOptionPane.showMessageDialog(null,"Monto" + SubtotalLbl.getText());
    }//GEN-LAST:event_SubtotalLblMouseClicked
    public void cargarArt() {
        Item item;
        item = (Item) EmpresaCB.getSelectedItem();
        String selectedItem = String.valueOf(item.getId());
        item = (Item) LocalizacionCB.getSelectedItem();
        String selectedLoc = String.valueOf(item.getId());
        item = (Item) ListaCB.getSelectedItem();
        String selectedLista = String.valueOf(item.getId());
        String codigo[] = oraclebd.valArtMascara(CodigoArtTxt.getText(), Integer.valueOf(selectedItem), Integer.valueOf(selectedLista));
        CodigoArtTxt.setText(codigo[0]);
        String articulo[] = oraclebd.getArticulo(CodigoArtTxt.getText(), Integer.valueOf(selectedItem), Integer.valueOf(selectedLoc), Integer.valueOf(selectedLista), "FAC");
        if (articulo[0] != null) {
            idArt = Integer.parseInt(articulo[0]);
            DescripcionArtTxt.setText(articulo[1]);
            ExistenciaLbl.setText(articulo[2]);
            PrecioTxt.setText(articulo[3]);
            preciou = articulo[3];
            preciof = articulo[6];
            if (Integer.parseInt(articulo[4]) > 0) {
                fraccion_b = true;
                FraccionTxt.setEnabled(true);
            } else {
                FraccionTxt.setText("0");
                fraccion_b = false;
                FraccionTxt.setEnabled(false);
            }
            if (articulo[5].equals("S")) {
                precio_b = true;
                PrecioTxt.setEnabled(true);
            } else {
                precio_b = false;
                PrecioTxt.setEnabled(false);
            }
            actualizarLinea();
            CantidadTxt.setText(codigo[1]);
            if (oraclebd.getParametro(Integer.valueOf(selectedItem), "FAC", "INCLUYE AUTOMATICO ARTICULO CON MASCARA").equals("S")) {
                try {
                    incluirDetalle();
                } catch (MyException e) {
                    JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                CantidadTxt.requestFocus();
            }
        } else {
            CodigoArtTxt.requestFocus();
        }
    }

//Método para actualizar los datos de Subtotal, % Descuento y Descuento de la linea de compra.
    public void actualizarLinea() {
        double[] sub = new double[5]; //Subtotal, Porcentaje, Monto, Impuesto, Total
        if (!DescripcionArtTxt.getText().isEmpty()) {
            if (!CantidadTxt.getText().isEmpty() && NumberUtils.isNumber(CantidadTxt.getText())) {
                if (!PrecioTxt.getText().isEmpty() && NumberUtils.isNumber(PrecioTxt.getText())) {
                    if (Integer.parseInt(FraccionTxt.getText()) > 0) {
                        sub[0] = Double.parseDouble(FraccionTxt.getText()) * Double.parseDouble(preciof);
                    } else {
                        sub[0] = Double.parseDouble(CantidadTxt.getText()) * Double.parseDouble(preciou);
                    }
                    NumberFormat formatoNumero = NumberFormat.getNumberInstance();
                    SubLbl.setText(String.valueOf(formatoNumero.format(sub[0])));
                    if (!ValorTxt.getText().isEmpty() && NumberUtils.isNumber(ValorTxt.getText())) {
                        if (TipoDescCB.getSelectedItem().equals("Porcentaje")) {
                            sub[1] = Double.parseDouble(ValorTxt.getText());
                            sub[2] = sub[0] * sub[1] / 100;
                            PorcentLbl.setText(String.valueOf(sub[1]));
                            MontoDescLbl.setText(String.valueOf(formatoNumero.format(sub[2])));
                        } else {
                            sub[2] = Double.parseDouble(ValorTxt.getText());
                            sub[1] = 100 * sub[2] / sub[0];
                            MontoDescLbl.setText(String.valueOf(formatoNumero.format(sub[2])));
                            PorcentLbl.setText(String.valueOf(sub[1]));
                        }
                        Item item;
                        item = (Item) EmpresaCB.getSelectedItem();
                        String empresa = String.valueOf(item.getId());
                        sub[3] = (sub[0] - sub[2]) * oraclebd.getImpuesto(Integer.parseInt(empresa), idArt) / 100;
                        ImpLbl.setText(String.valueOf(formatoNumero.format(sub[3])));
                        sub[4] = sub[0] - sub[2] + sub[3];
                        TotLbl.setText(String.valueOf(formatoNumero.format(sub[4])));
                    }
                }
            }
        }
    }

    //Método del Botón Buscar Artículos.
    public void buscarArt() {
        Item item;
        item = (Item) EmpresaCB.getSelectedItem();
        String selectedItem = String.valueOf(item.getId());
        item = (Item) LocalizacionCB.getSelectedItem();
        String selectedLoc = String.valueOf(item.getId());
        item = (Item) ListaCB.getSelectedItem();
        String selectedLista = String.valueOf(item.getId());
        BuscarArticulos articulosFrm = new BuscarArticulos(this, true, oraclebd, Integer.valueOf(selectedItem), Integer.valueOf(selectedLoc), Integer.valueOf(selectedLista), "FAC", usuario_g);
        articulosFrm.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent ev) {
                idArt = articulosFrm.getId();
                CodigoArtTxt.setText(articulosFrm.getCodigo());
                cargarArt();
            }
        });
        articulosFrm.setVisible(true);
    }

    //Método para incluir una línea en la factura de compra.
    public void incluirDetalle() throws MyException {
        try {
            crearTrans();
            if (CodigoArtTxt.getText().isEmpty() || DescripcionArtTxt.getText().isEmpty()) {
                throw new MyException("Articulo Invalido");
            }
            if (CantidadTxt.getText().isEmpty() || !NumberUtils.isNumber(CantidadTxt.getText())) {
                throw new MyException("Cantidad Invalida");
            }
            if (FraccionTxt.getText().isEmpty() || !NumberUtils.isNumber(FraccionTxt.getText())) {
                throw new MyException("Fraccion Invalida");
            }
            if (PrecioTxt.getText().isEmpty() || !NumberUtils.isNumber(PrecioTxt.getText())) {
                throw new MyException("Precio Invalido");
            }
            if (ValorTxt.getText().isEmpty() || !NumberUtils.isNumber(ValorTxt.getText())) {
                throw new MyException("Valor Invalido");
            }
            Item item;
            item = (Item) EmpresaCB.getSelectedItem();
            String selectedItem = String.valueOf(item.getId());
            int id = oraclebd.incluirDetalleFAC(Integer.parseInt(TransaccionLbl.getText()), Integer.parseInt(selectedItem),
                    idArt, DescripcionArtTxt.getText(), Double.parseDouble(CantidadTxt.getText()),
                    Integer.parseInt(FraccionTxt.getText()), Double.parseDouble(PrecioTxt.getText()),
                    Double.parseDouble(PorcentLbl.getText()), "N");
            if (id > -1) {
                configurarTabla();
                for (int i = 0; i < 4; i++) {
                    total[i] = 0;
                }
                //Totales
                oraclebd.getDetallesFact(modelo, Integer.parseInt(TransaccionLbl.getText()), total);
                NumberFormat formatoNumero = NumberFormat.getNumberInstance();
                SubtotalLbl.setText(String.valueOf(formatoNumero.format(total[0])));
                DescuentoLbl.setText(String.valueOf(formatoNumero.format(total[1])));
                ImpuestoLbl.setText(String.valueOf(formatoNumero.format(total[2])));
                TotalLbl.setText(String.valueOf(formatoNumero.format(total[3])));

                CodigoArtTxt.setText("");
                idArt = -1;
                DescripcionArtTxt.setText("");
                CantidadTxt.setText("0");
                FraccionTxt.setText("0");
                fraccion_b = false;
                FraccionTxt.setEnabled(false);
                PrecioTxt.setText("0");
                precio_b = false;
                PrecioTxt.setEnabled(false);
                TipoDescCB.setSelectedItem(TipoDescCB.getItemAt(0));
                ValorTxt.setText("0");
                SubLbl.setText("0");
                PorcentLbl.setText("0");
                MontoDescLbl.setText("0");
                ImpLbl.setText("0");
                TotLbl.setText("0");
                CodigoArtTxt.requestFocus();
            }
        } catch (MyException e) {
            JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    //Método del Botón Crear Transacciones.

    public void crearTrans() throws MyException {
        try {
            Item item;
            item = (Item) EmpresaCB.getSelectedItem();
            String empresa = String.valueOf(item.getId());
            item = (Item) TipoTransaccionCB.getSelectedItem();
            String tipo = String.valueOf(item.getId());
            item = (Item) MonedaCB.getSelectedItem();
            String moneda = String.valueOf(item.getId());
            item = (Item) LocalizacionCB.getSelectedItem();
            String localizacion = String.valueOf(item.getId());
            item = (Item) ListaCB.getSelectedItem();
            String lista = String.valueOf(item.getId());
            item = (Item) DepartamentoCB.getSelectedItem();
            String departamento = String.valueOf(item.getId());
            item = (Item) CajaCB.getSelectedItem();
            String caja = String.valueOf(item.getId());

            if (StringUtils.isEmpty(empresa)) {
                throw new MyException("Empresa Invalida");
            }

            if (StringUtils.isEmpty(tipo)) {
                throw new MyException("Tipo de Transaccion Invalido");
            }

            if (StringUtils.isEmpty(moneda)) {
                throw new MyException("Moneda Invalida");
            }

            if (StringUtils.isEmpty(localizacion)) {
                throw new MyException("Localizacion Invalida");
            }

            if (StringUtils.isEmpty(departamento)) {
                throw new MyException("Departamento Invalido");
            }

            if (StringUtils.isEmpty(caja)) {
                throw new MyException("Caja Invalida");
            }

            if (StringUtils.isEmpty(ClienteTxt.getText()) || StringUtils.isEmpty(NombreTxt.getText()) || StringUtils.isEmpty(IdentificacionTxt.getText())) {
                throw new MyException("Cliente Invalido");
            }

            if (StringUtils.isEmpty(ValorCambioTxt.getText()) || !NumberUtils.isNumber(ValorCambioTxt.getText())) {
                throw new MyException("Valor de Cambio Invalido");
            }

            if (StringUtils.isEmpty(OtrosCargosTxt.getText()) || !NumberUtils.isNumber(OtrosCargosTxt.getText())) {
                throw new MyException("Otros Cargos Invalido");
            }

            if (StringUtils.isEmpty(DetalleTxt.getText())) {
                throw new MyException("Detalle Invalido");
            }

            if (TransaccionLbl.getText().equals("0")) {
                int id = oraclebd.crearFactura(Integer.valueOf(ProformaLbl.getText()), Integer.valueOf(empresa), Integer.valueOf(localizacion),
                        Integer.valueOf(departamento), Integer.valueOf(ClienteTxt.getText()), Integer.valueOf(lista),
                        Integer.valueOf(moneda), Integer.valueOf(caja), Integer.valueOf(tipo), Integer.valueOf(PlazoLbl.getText()),
                        DetalleTxt.getText(), Integer.valueOf(ValorCambioTxt.getText()),
                        Integer.valueOf(OtrosCargosTxt.getText()), NombreTxt.getText(), IdentificacionTxt.getText());
                if (id == -1) {
                    JOptionPane.showMessageDialog(null, "Se produjo un error al insertar, intente nuevamente.", "Error de Inserción", JOptionPane.ERROR_MESSAGE);
                } else {
                    oraclebd.actualizausuario(id, 1, usuario_g);
                    TransaccionLbl.setText(String.valueOf(id));
                    EmpresaCB.setEnabled(false);
                    CodigoArtTxt.requestFocus();
                }
            } else {
                oraclebd.modificarFactura(Integer.valueOf(TransaccionLbl.getText()), Integer.valueOf(empresa), Integer.valueOf(localizacion),
                        Integer.valueOf(departamento), Integer.valueOf(ClienteTxt.getText()), NombreTxt.getText(), Integer.valueOf(lista),
                        Integer.valueOf(moneda), Integer.valueOf(caja), Integer.valueOf(tipo),
                        Integer.valueOf(PlazoLbl.getText()), DetalleTxt.getText(), Integer.valueOf(ValorCambioTxt.getText()),
                        Integer.valueOf(OtrosCargosTxt.getText()));
            }
        } catch (MyException e) {
            JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Método del Botón Buscar Proveedores.
    public void buscarCliente() {
        Item item = (Item) EmpresaCB.getSelectedItem();
        BuscarClientes clientesFrm = new BuscarClientes(this, true, oraclebd, item.getId(), usuario_g);
        clientesFrm.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent ev) {
                if (clientesFrm.getId() > -1) {
                    ClienteTxt.setText(String.valueOf(clientesFrm.getId()));
                    NombreTxt.setText(clientesFrm.getNombre());
                    IdentificacionTxt.setText(clientesFrm.getIdentificacion());
                    PlazoLbl.setText(String.valueOf(clientesFrm.getPlazo()));
                    NombreTxt.requestFocus();
                } else {
                    ClienteTxt.requestFocus();
                }
            }
        });
        clientesFrm.setVisible(true);
    }

    //Método del Botón Buscar Transacciónes Creadas.
    public void buscarTrans() {
        BuscarFacturas facturasFrm = new BuscarFacturas(this, true, oraclebd, usuario_g);
        facturasFrm.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent ev) {
                if (facturasFrm.getId() > -1) {
                    llenarDatos(facturasFrm.getId());
                }
            }
        });
        facturasFrm.setVisible(true);
    }

//Método para Completar los datos de la Transaccion seleccionada en la Ventana Transacciónes Creadas.
    public void llenarDatos(int transaccion_p) {
        configurarTabla();
        String[] datos = oraclebd.getFacturaCreada(transaccion_p);
        TransaccionLbl.setText(datos[0]);
        ProformaLbl.setText(datos[1]);
        DetalleTxt.setText(datos[2]);
        ValorCambioTxt.setText(datos[3]);
        OtrosCargosTxt.setText(datos[4]);
        //EmpresaCB.setSelectedItem(Integer.valueOf(datos[5]));
        setSelectedValue(EmpresaCB, Integer.valueOf(datos[5]));
        ClienteTxt.setText(datos[6]);
        NombreTxt.setText(datos[7]);
        IdentificacionTxt.setText(datos[8]);
        //TipoTransaccionCB.setSelectedItem(Integer.valueOf(datos[9]));
        setSelectedValue(TipoTransaccionCB, Integer.valueOf(datos[9]));
        //MonedaCB.setSelectedItem(Integer.valueOf(datos[10]));
        setSelectedValue(MonedaCB, Integer.valueOf(datos[10]));
        //LocalizacionCB.setSelectedItem(Integer.valueOf(datos[11]));
        setSelectedValue(LocalizacionCB, Integer.valueOf(datos[11]));
        //ListaCB.setSelectedItem(Integer.valueOf(datos[12]));
        setSelectedValue(ListaCB, Integer.valueOf(datos[12]));
        //DepartamentoCB.setSelectedItem(Integer.valueOf(datos[13]));
        setSelectedValue(DepartamentoCB, Integer.valueOf(datos[13]));
        TipoLbl.setText(datos[14]);
        PlazoLbl.setText(datos[15]);
        VencimientoLbl.setText(datos[16]);
        CajaCB.setSelectedItem(Integer.valueOf(datos[17]));
        DisponibleLbl.setText(datos[18]);

        //Totales
        oraclebd.getDetallesFact(modelo, Integer.valueOf(datos[0]), total);
        NumberFormat formatoNumero = NumberFormat.getNumberInstance();
        SubtotalLbl.setText(String.valueOf(formatoNumero.format(total[0])));
        DescuentoLbl.setText(String.valueOf(formatoNumero.format(total[1])));
        ImpuestoLbl.setText(String.valueOf(formatoNumero.format(total[2])));
        TotalLbl.setText(String.valueOf(formatoNumero.format(total[3])));
        EmpresaCB.setEnabled(false);
    }

//Método del Botón Limpiar.
    public void limpiar() {
        TransaccionLbl.setText("0");
        ProformaLbl.setText("0");
        DetalleTxt.setText("FACTURA");
        ValorCambioTxt.setText("0");
        OtrosCargosTxt.setText("0");
        EmpresaCB.setSelectedItem(EmpresaCB.getItemAt(0));
        ClienteTxt.setText("");
        NombreTxt.setText("");
        IdentificacionTxt.setText("");
        TipoTransaccionCB.setSelectedItem(TipoTransaccionCB.getItemAt(0));
        MonedaCB.setSelectedItem(MonedaCB.getItemAt(0));
        LocalizacionCB.setSelectedItem(LocalizacionCB.getItemAt(0));
        ListaCB.setSelectedItem(ListaCB.getItemAt(0));
        DepartamentoCB.setSelectedItem(DepartamentoCB.getItemAt(0));
        DisponibleLbl.setText("0");
        PlazoLbl.setText("0");
        TipoLbl.setText("");
        VencimientoLbl.setText("");
        CajaCB.setSelectedItem(CajaCB.getItemAt(0));
        CodigoArtTxt.setText("");
        DescripcionArtTxt.setText("");
        ExistenciaLbl.setText("0");
        CantidadTxt.setText("0");
        FraccionTxt.setText("0");
        fraccion_b = false;
        FraccionTxt.setEnabled(false);
        PrecioTxt.setText("0");
        precio_b = false;
        PrecioTxt.setEnabled(false);
        TipoDescCB.setSelectedItem(TipoDescCB.getItemAt(0));
        ValorTxt.setText("0");
        SubLbl.setText("0");
        PorcentLbl.setText("0");
        MontoDescLbl.setText("0");
        ImpLbl.setText("0");
        TotLbl.setText("0");
        SubtotalLbl.setText("0");
        DescuentoLbl.setText("0");
        ImpuestoLbl.setText("0");
        TotalLbl.setText("0");
        idArt = -1;
        configurarTabla();
        for (int i = 0; i < 4; i++) {
            total[i] = 0;
        }
        ClienteTxt.requestFocus();
        EmpresaCB.setEnabled(true);
    }

    public void registrar() {
        if (!TransaccionLbl.getText().equals("0")) {
            if (FacturasTbl.getRowCount() > 0) {
                boolean continuar_v = true;
                String cliente[] = oraclebd.getCliente(Integer.valueOf(ClienteTxt.getText()));
                Item item;
                item = (Item) TipoTransaccionCB.getSelectedItem();
                int tipo_v = item.getId();
                String concre_v = oraclebd.getTipoFac(tipo_v);
                //Si el cliente tiene limite y la factura es de contado, pregunta si 
                //desea continuar
                if (Double.parseDouble(cliente[3]) > 0 && concre_v.equals("CON")) {
                    int seleccion = JOptionPane.showOptionDialog(
                            this, // Componente padre
                            "El cliente es de credito pero la factura de contado ¿Continuar?", //Mensaje
                            "Credito/Contado", // Título
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null, // null para icono por defecto.
                            new Object[]{"Si", "No"}, // null para YES, NO y CANCEL
                            "No");
                    if (seleccion == JOptionPane.NO_OPTION) {
                        continuar_v = false;
                    }
                }

                if (continuar_v == true) {
                    if (oraclebd.registrarFactura(Integer.parseInt(TransaccionLbl.getText()))) {
                        JOptionPane mensaje = new JOptionPane();
                        oraclebd.actualizausuario(Integer.parseInt(TransaccionLbl.getText()), 2, usuario_g);
                        mensaje.showMessageDialog(null, "Factura registrada exitosamente", "Registro Factura", JOptionPane.INFORMATION_MESSAGE);
                        item = (Item) EmpresaCB.getSelectedItem();
                        int empresa_v = item.getId();
                        if (concre_v.equals("CON")) {
                            if (oraclebd.getParametro(empresa_v, "FAC", "REGISTRO AUTOMATICO MODO DE PAGO").equals("S")) {
                                registraPago();
                                oraclebd.imprimirFactura(Integer.parseInt(TransaccionLbl.getText()));
                            } else {
                                PagoContado pagoFrm = new PagoContado(this, true, oraclebd, Integer.parseInt(TransaccionLbl.getText()), empresa_v, usuario_g);
                                pagoFrm.setVisible(true);
                            }
                        } else {
                            oraclebd.imprimirFactura(Integer.parseInt(TransaccionLbl.getText()));
                        }

                        limpiar();
                    }
                }

            } else {
                JOptionPane.showMessageDialog(null, "No existen artículos asociados a la Factura para Registrar.", "Error de Registro", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "No existe una Factura para Registrar.", "Error de Registro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void registraPago() {
        // TODO add your handling code here:
        Double efectivo_v = 0.0, otros_v = 0.0, cambio_v = 0.0, tarjeta_v = 0.0, cheque_v = 0.0, nota_v = 0.0, deposito_v = 0.0, total_v = 0.0;
        efectivo_v = oraclebd.getTotalFac(Integer.parseInt(TransaccionLbl.getText()));
        Item item;
        item = (Item) MonedaCB.getSelectedItem();
        int moneda_v = item.getId();
        oraclebd.cancelaFactura(Integer.parseInt(TransaccionLbl.getText()), efectivo_v,
                otros_v, moneda_v,
                cambio_v, tarjeta_v,
                cheque_v, nota_v,
                deposito_v, "");
    }
//Crear y Configurar la Tabla.

    
    public void configurarTabla() {
        modelo = new DefaultTableModel();
        
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        for (int i = 0; i < 12; i++) {
            modelo.addColumn(FacturasTbl.getColumnName(i));
        }
        FacturasTbl.setModel(modelo);
        TableCellRenderer render = new TableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                NumberFormat formatoNumero = NumberFormat.getNumberInstance();
                JLabel lbl = new JLabel(value == null ? "" : formatoNumero.format(Double.parseDouble(value.toString())));
                lbl.setHorizontalAlignment(SwingConstants.RIGHT);
                lbl.setFont(new Font("Tahoma", Font.PLAIN, 10));
                return lbl;
            }
        };
          
        FacturasTbl.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
       
        //Linea
        FacturasTbl.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(0);
        FacturasTbl.getTableHeader().getColumnModel().getColumn(0).setMinWidth(0);
        FacturasTbl.getColumn(FacturasTbl.getColumnName(0)).setPreferredWidth(0);
        //FacturasTbl.getColumn(FacturasTbl.getColumnName(0)).setResizable(false);
        //Articulo
        FacturasTbl.getTableHeader().getColumnModel().getColumn(1).setMaxWidth(0);
        FacturasTbl.getTableHeader().getColumnModel().getColumn(1).setMinWidth(0);
        FacturasTbl.getColumn(FacturasTbl.getColumnName(1)).setPreferredWidth(0);
        //FacturasTbl.getColumn(FacturasTbl.getColumnName(1)).setResizable(false);        
      /* Habilitar para darle tamanos especificos a las columnas
        //Codigo
        FacturasTbl.getColumn(FacturasTbl.getColumnName(2)).setPreferredWidth(60);
        //FacturasTbl.getColumn(FacturasTbl.getColumnName(2)).setResizable(false);
        //Descripcion
        FacturasTbl.getColumn(FacturasTbl.getColumnName(3)).setPreferredWidth(400);
        //FacturasTbl.getColumn(FacturasTbl.getColumnName(3)).setResizable(false);
        //Cantidad
        FacturasTbl.getColumn(FacturasTbl.getColumnName(4)).setPreferredWidth(70);
        //FacturasTbl.getColumn(FacturasTbl.getColumnName(4)).setResizable(false);
        FacturasTbl.getColumn(FacturasTbl.getColumnName(4)).setCellRenderer(render);
        //Fraccion
        FacturasTbl.getColumn(FacturasTbl.getColumnName(5)).setPreferredWidth(70);
        //FacturasTbl.getColumn(FacturasTbl.getColumnName(5)).setResizable(false);
        FacturasTbl.getColumn(FacturasTbl.getColumnName(5)).setCellRenderer(render);
        //Precio
        FacturasTbl.getColumn(FacturasTbl.getColumnName(6)).setPreferredWidth(60);
        //FacturasTbl.getColumn(FacturasTbl.getColumnName(6)).setResizable(false);
        //Subtotal
        FacturasTbl.getColumn(FacturasTbl.getColumnName(7)).setPreferredWidth(60);
        //FacturasTbl.getColumn(FacturasTbl.getColumnName(7)).setResizable(false);
        FacturasTbl.getColumn(FacturasTbl.getColumnName(7)).setCellRenderer(render);
        //% Descuento
        FacturasTbl.getColumn(FacturasTbl.getColumnName(8)).setPreferredWidth(60);
        //FacturasTbl.getColumn(FacturasTbl.getColumnName(8)).setResizable(false);
        //Descuento
        FacturasTbl.getColumn(FacturasTbl.getColumnName(9)).setPreferredWidth(60);
        //FacturasTbl.getColumn(FacturasTbl.getColumnName(9)).setResizable(false);
        FacturasTbl.getColumn(FacturasTbl.getColumnName(9)).setCellRenderer(render);
        //Impuesto
        FacturasTbl.getColumn(FacturasTbl.getColumnName(10)).setPreferredWidth(60);
        //FacturasTbl.getColumn(FacturasTbl.getColumnName(10)).setResizable(false);
        FacturasTbl.getColumn(FacturasTbl.getColumnName(10)).setCellRenderer(render);
        //Total
        FacturasTbl.getColumn(FacturasTbl.getColumnName(11)).setPreferredWidth(60);
        //FacturasTbl.getColumn(FacturasTbl.getColumnName(11)).setResizable(false);
        FacturasTbl.getColumn(FacturasTbl.getColumnName(11)).setCellRenderer(render);
        */
    }

    public class tableModel extends DefaultTableModel {

        tableModel() {

        }

        @Override
        public boolean isCellEditable(int row, int cols) {
            return false;
        }
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
            java.util.logging.Logger.getLogger(Facturas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Facturas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Facturas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Facturas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Facturas(null, null).setVisible(true);
            }
        });

    }

    public void setSelectedValue(JComboBox comboBox, int value) {
        Item item;
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            item = (Item) comboBox.getItemAt(i);
            if (item.getId() == value) {
                comboBox.setSelectedIndex(i);
            }
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BuscarArtBtn;
    private javax.swing.JButton BuscarClteBtn;
    private javax.swing.JButton BuscarTransBtn;
    private javax.swing.JComboBox CajaCB;
    private javax.swing.JButton CancelarBtn;
    private javax.swing.JTextField CantidadTxt;
    private javax.swing.JTextField ClienteTxt;
    private javax.swing.JTextField CodigoArtTxt;
    private javax.swing.JButton CrearClteBtn;
    private javax.swing.JComboBox DepartamentoCB;
    private javax.swing.JTextField DescripcionArtTxt;
    private javax.swing.JLabel DescuentoLbl;
    private javax.swing.JTextField DetalleTxt;
    private javax.swing.JLabel DisponibleLbl;
    private javax.swing.JComboBox EmpresaCB;
    private javax.swing.JLabel ExistenciaLbl;
    private javax.swing.JTable FacturasTbl;
    private javax.swing.JTextField FraccionTxt;
    private javax.swing.JTextField IdentificacionTxt;
    private javax.swing.JLabel ImpLbl;
    private javax.swing.JLabel ImpuestoLbl;
    private javax.swing.JButton IncluirBtn;
    private javax.swing.JButton LimpiarBtn;
    private javax.swing.JComboBox ListaCB;
    private javax.swing.JComboBox LocalizacionCB;
    private javax.swing.JButton MenuBtn;
    private javax.swing.JComboBox MonedaCB;
    private javax.swing.JLabel MontoDescLbl;
    private javax.swing.JTextField NombreTxt;
    private javax.swing.JTextField OtrosCargosTxt;
    private javax.swing.JLabel PlazoLbl;
    private javax.swing.JLabel PorcentLbl;
    private javax.swing.JTextField PrecioTxt;
    private javax.swing.JLabel ProformaLbl;
    private javax.swing.JButton RegistrarBtn;
    private javax.swing.JLabel SubLbl;
    private javax.swing.JLabel SubtotalLbl;
    private javax.swing.JComboBox TipoDescCB;
    private javax.swing.JLabel TipoLbl;
    private javax.swing.JComboBox TipoTransaccionCB;
    private javax.swing.JLabel TotLbl;
    private javax.swing.JLabel TotalLbl;
    private javax.swing.JLabel TransaccionLbl;
    private javax.swing.JTextField ValorCambioTxt;
    private javax.swing.JTextField ValorTxt;
    private javax.swing.JLabel VencimientoLbl;
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
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private java.awt.Panel panel1;
    // End of variables declaration//GEN-END:variables
}
