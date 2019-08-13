package hetec;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Date;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
//import java.sql.DatabaseMetaData;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class OracleBD {

    Connection conexion_o;
    ResultSet resultado_o;
    CallableStatement cstmt_o;
    Statement instruccion_o;
    String usuario_v, clave_v, servidor_v, sid_v;
    //DatabaseMetaData mdata_v;

    public OracleBD() {
        cargar();
        if (usuario_v != null && usuario_v.length() > 0) {
            conectar();
        } else {
            JOptionPane.showMessageDialog(null, "Debe configurar la conexion");
        }
    }

    public void conectar() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            //String BaseDeDatos = "jdbc:oracle:thin:@" + servidor_v + ":1521:" + sid_v;
            //String BaseDeDatos = "jdbc:oracle:thin:@//" + servidor_v + ":1521/" + sid_v;
            String BaseDeDatos = "jdbc:oracle:thin:@//" + servidor_v + ":1521/" + sid_v;
            conexion_o = DriverManager.getConnection(BaseDeDatos, usuario_v, clave_v);
        } catch (ClassNotFoundException | SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Conectar", JOptionPane.ERROR_MESSAGE);
        }
    }

    /*public void cargar() {
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, e);
        }
        try {
            conexion_o = DriverManager.getConnection("jdbc:derby:hetecdb;create=true");
            mdata_v = conexion_o.getMetaData();
            instruccion_o = conexion_o.createStatement();
            resultado_o = mdata_v.getTables(conexion_o.getCatalog(), null, "GEN_CONEXION_TB", null);
            if (!resultado_o.next()) {
                instruccion_o.executeUpdate("CREATE TABLE GEN_CONEXION_TB("
                        + "CON_USUARIO VARCHAR(30) NOT NULL,"
                        + "CON_CLAVE VARCHAR(30) NOT NULL,"
                        + "CON_SERVIDOR VARCHAR(30) NOT NULL,"
                        + "CON_SID VARCHAR(30) NOT NULL"
                        + ")");
            }           
        } catch (SQLException e) {
            System.err.println(e);
        }finally{
            cerrar();
        }

        try {
            conexion_o = DriverManager.getConnection("jdbc:derby:hetecdb");
            instruccion_o = conexion_o.createStatement();
            resultado_o = instruccion_o.executeQuery("SELECT * FROM GEN_CONEXION_TB ORDER BY 1");

            while (rs.next()) {
                usuario_v = rs.getString("CON_USUARIO");
                clave_v = rs.getString("CON_CLAVE");
                servidor_v = rs.getString("CON_SERVIDOR");
                sid_v = rs.getString("CON_SID");
            }           
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }finally{
            cerrar();
        }
    }*/
    public void cargar() {
        try {

            File fXmlFile = new File("Conexion.xml");
            if (fXmlFile.exists()) {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(fXmlFile);
                usuario_v = doc.getElementsByTagName("Usuario").item(0).getTextContent();
                clave_v = doc.getElementsByTagName("Clave").item(0).getTextContent();
                servidor_v = doc.getElementsByTagName("Servidor").item(0).getTextContent();
                sid_v = doc.getElementsByTagName("SID").item(0).getTextContent();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void crearConexion(String usuario, String clave, String servidor, String sid) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("Hetec");

            Element e = doc.createElement("Usuario");
            e.appendChild(doc.createTextNode(usuario));
            rootElement.appendChild(e);

            e = doc.createElement("Clave");
            e.appendChild(doc.createTextNode(clave));
            rootElement.appendChild(e);

            e = doc.createElement("Servidor");
            e.appendChild(doc.createTextNode(servidor));
            rootElement.appendChild(e);

            e = doc.createElement("SID");
            e.appendChild(doc.createTextNode(sid));
            rootElement.appendChild(e);

            doc.appendChild(rootElement);
            Transformer tr = TransformerFactory.newInstance().newTransformer();
            tr.transform(new DOMSource(doc), new StreamResult(new File("Conexion.xml")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String valida_usuario(String usuario_p, String clave_p) {
        String resultado_v = "N";
        try {
            if (conexion_o == null || conexion_o.isClosed()) {
                conectar();
            }
            if (conexion_o != null && !conexion_o.isClosed()) {
                cstmt_o = conexion_o.prepareCall("{? = call seg_valida_usuario_v_fn(?, ?)}");
                cstmt_o.registerOutParameter(1, Types.VARCHAR);
                cstmt_o.setString(2, usuario_p);
                cstmt_o.setString(3, clave_p);
                cstmt_o.executeUpdate();
                resultado_v = cstmt_o.getString(1);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Valida Usuario", JOptionPane.ERROR_MESSAGE);
        } finally {
            cerrar();
        }
        return resultado_v;
    }

    public void getEmpresas(JComboBox combo) {
        try {
            if (conexion_o == null || conexion_o.isClosed()) {
                conectar();
            }
            if (conexion_o != null && !conexion_o.isClosed()) {
                instruccion_o = conexion_o.createStatement();
                ResultSet result_v = instruccion_o.executeQuery("SELECT EMP_ID, EMP_NOMBRE FROM GEN_EMPRESA_TB ORDER BY 2");
                combo.removeAllItems();
                while (result_v.next()) {
                    combo.addItem(new Item(result_v.getInt(1), result_v.getString(2)));
                }
                if (result_v != null) {
                    result_v.close();
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Empresas", JOptionPane.ERROR_MESSAGE);
        } finally {
            cerrar();
        }
    }

    public void getLocalizaciones(JComboBox combo, int id, String usuario_p) {
        try {
            if (conexion_o == null || conexion_o.isClosed()) {
                conectar();
            }
            if (conexion_o != null && !conexion_o.isClosed()) {
                instruccion_o = conexion_o.createStatement();
                resultado_o = instruccion_o.executeQuery("SELECT LOC_ID,LOC_DESCRIPCION FROM INV_LOCALIZACION_TB "
                        + " WHERE LOC_EMP_ID=" + id
                        + " AND NOT EXISTS (SELECT USE_LOC_ID FROM SEG_USUARIO_TB, SEG_USUARIO_EMP_TB "
                        + " WHERE USU_ID = USE_USU_ID AND USE_EMP_ID = LOC_EMP_ID AND USU_USUARIO = '" + usuario_p + "')"
                        + " UNION ALL"
                        + " SELECT LOC_ID,LOC_DESCRIPCION FROM INV_LOCALIZACION_TB"
                        + " WHERE LOC_EMP_ID=" + id
                        + " AND EXISTS (SELECT USE_LOC_ID FROM SEG_USUARIO_TB, SEG_USUARIO_EMP_TB "
                        + " WHERE USU_ID = USE_USU_ID AND USE_EMP_ID = LOC_EMP_ID AND USE_LOC_ID = LOC_ID AND USU_USUARIO = '" + usuario_p + "')");
                combo.removeAllItems();
                while (resultado_o.next()) {
                    combo.addItem(new Item(resultado_o.getInt(1), resultado_o.getString(2)));
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Localizaciones", JOptionPane.ERROR_MESSAGE);
        } finally {
            cerrar();
        }
    }

    public void getOrigen(JComboBox combo, int id) {
        try {
            if (conexion_o == null || conexion_o.isClosed()) {
                conectar();
            }
            if (conexion_o != null && !conexion_o.isClosed()) {
                instruccion_o = conexion_o.createStatement();
                resultado_o = instruccion_o.executeQuery("SELECT ORI_ID,ORI_CODIGO,ORI_DESCRIPCION FROM CON_ORIGEN_TB WHERE ORI_EMP_ID=" + id);
                combo.removeAllItems();
                while (resultado_o.next()) {
                    combo.addItem(new Item(resultado_o.getInt(1), resultado_o.getString(2)));
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Origen", JOptionPane.ERROR_MESSAGE);
        } finally {
            cerrar();
        }
    }

    public void getPeriodo(JComboBox combo, int id) {
        try {
            if (conexion_o == null || conexion_o.isClosed()) {
                conectar();
            }
            if (conexion_o != null && !conexion_o.isClosed()) {
                instruccion_o = conexion_o.createStatement();
                resultado_o = instruccion_o.executeQuery("SELECT PER_ID,(PER_PERIODO||' '||PER_ANO)DSP_VALUE "
                        + "FROM CON_PERIODO_TB WHERE PER_EMP_ID=" + id + " ORDER BY PER_PERIODO");
                combo.removeAllItems();
                while (resultado_o.next()) {
                    combo.addItem(new Item(resultado_o.getInt(1), resultado_o.getString(2)));
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Origen", JOptionPane.ERROR_MESSAGE);
        } finally {
            cerrar();
        }
    }

    public void getDepartamentos(JComboBox combo, int id, String usuario_p) {
        try {
            if (conexion_o == null || conexion_o.isClosed()) {
                conectar();
            }
            if (conexion_o != null && !conexion_o.isClosed()) {
                instruccion_o = conexion_o.createStatement();
                ResultSet result_v = instruccion_o.executeQuery("SELECT DEP_ID, DEP_DESCRIPCION FROM GEN_DEPARTAMENTO_TB "
                        + " WHERE DEP_EMP_ID=" + id
                        + " AND NOT EXISTS (SELECT USE_DEP_ID FROM SEG_USUARIO_TB, SEG_USUARIO_EMP_TB "
                        + " WHERE USU_ID = USE_USU_ID AND USE_EMP_ID = DEP_EMP_ID AND USU_USUARIO = '" + usuario_p + "')"
                        + " UNION ALL"
                        + " SELECT DEP_ID, DEP_DESCRIPCION FROM GEN_DEPARTAMENTO_TB"
                        + " WHERE DEP_EMP_ID=" + id
                        + " AND EXISTS (SELECT USE_DEP_ID FROM SEG_USUARIO_TB, SEG_USUARIO_EMP_TB "
                        + " WHERE USU_ID = USE_USU_ID AND USE_EMP_ID = DEP_EMP_ID AND USE_DEP_ID = DEP_ID AND USU_USUARIO = '" + usuario_p + "')");
                combo.removeAllItems();
                while (result_v.next()) {
                    combo.addItem(new Item(result_v.getInt(1), result_v.getString(2)));
                }
                if (result_v != null) {
                    result_v.close();
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Departamentos", JOptionPane.ERROR_MESSAGE);
        } finally {
            cerrar();
        }
    }

    public void getMonedas(JComboBox combo, int id) {
        try {
            if (conexion_o == null || conexion_o.isClosed()) {
                conectar();
            }
            if (conexion_o != null && !conexion_o.isClosed()) {
                instruccion_o = conexion_o.createStatement();
                resultado_o = instruccion_o.executeQuery("SELECT MON_ID, MON_DESCRIPCION FROM GEN_MONEDA_TB WHERE MON_EMP_ID=" + id);
                combo.removeAllItems();
                while (resultado_o.next()) {
                    combo.addItem(new Item(resultado_o.getInt(1), resultado_o.getString(2)));
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Monedas", JOptionPane.ERROR_MESSAGE);
        } finally {
            cerrar();
        }
    }

    public void getMonedaPago(JComboBox combo, int empresa, int factura) {
        try {
            if (conexion_o == null || conexion_o.isClosed()) {
                conectar();
            }
            if (conexion_o != null && !conexion_o.isClosed()) {
                instruccion_o = conexion_o.createStatement();
                resultado_o = instruccion_o.executeQuery("SELECT MON_ID, MON_DESCRIPCION FROM GEN_MONEDA_TB "
                        + " WHERE MON_EMP_ID=" + empresa + " AND MON_ID NOT IN (SELECT FAC_MON_ID FROM FAC_FACTURA_TB WHERE FAC_ID = " + factura + ")");
                combo.removeAllItems();
                while (resultado_o.next()) {
                    combo.addItem(new Item(resultado_o.getInt(1), resultado_o.getString(2)));
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Monedas", JOptionPane.ERROR_MESSAGE);
        } finally {
            cerrar();
        }
    }

    public boolean getListas(JComboBox combo, int id) {
        try {
            if (conexion_o == null || conexion_o.isClosed()) {
                conectar();
            }
            if (conexion_o != null && !conexion_o.isClosed()) {
                instruccion_o = conexion_o.createStatement();
                resultado_o = instruccion_o.executeQuery("SELECT LPR_ID, LPR_DESCRIPCION FROM INV_LISTA_PRECIOS_TB "
                        + "WHERE LPR_EMP_ID=" + id);
                combo.removeAllItems();
                while (resultado_o.next()) {
                    combo.addItem(new Item(resultado_o.getInt(1), resultado_o.getString(2)));
                }
                if (combo.getItemCount() > 0) {
                    return true;
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Listas", JOptionPane.ERROR_MESSAGE);
        } finally {
            cerrar();
        }
        return false;
    }

    public boolean getCajas(JComboBox combo, int id, int dep) {
        try {
            if (conexion_o == null || conexion_o.isClosed()) {
                conectar();
            }
            if (conexion_o != null && !conexion_o.isClosed()) {
                instruccion_o = conexion_o.createStatement();
                resultado_o = instruccion_o.executeQuery("SELECT CAJ_ID, CAJ_DESCRIPCION FROM GEN_CAJA_TB "
                        + "WHERE CAJ_EMP_ID=" + id
                        + " AND CAJ_DEP_ID=" + dep);
                combo.removeAllItems();
                while (resultado_o.next()) {
                    combo.addItem(new Item(resultado_o.getInt(1), resultado_o.getString(2)));
                }
                if (combo.getItemCount() > 0) {
                    return true;
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Cajas", JOptionPane.ERROR_MESSAGE);
        } finally {
            cerrar();
        }
        return false;
    }

    public void getUnidMed(JComboBox combo, int id) {
        try {
            if (conexion_o == null || conexion_o.isClosed()) {
                conectar();
            }
            if (conexion_o != null && !conexion_o.isClosed()) {
                instruccion_o = conexion_o.createStatement();
                resultado_o = instruccion_o.executeQuery("SELECT UME_ID, UME_DESCRIPCION FROM INV_UNIDAD_MEDIDA_TB WHERE UME_EMP_ID=" + id);
                combo.removeAllItems();
                while (resultado_o.next()) {
                    combo.addItem(new Item(resultado_o.getInt(1), resultado_o.getString(2)));
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Unidades de Medida", JOptionPane.ERROR_MESSAGE);
        } finally {
            cerrar();
        }
    }

    public void getCategoria(JComboBox combo, int id) {
        try {
            if (conexion_o == null || conexion_o.isClosed()) {
                conectar();
            }
            if (conexion_o != null && !conexion_o.isClosed()) {
                instruccion_o = conexion_o.createStatement();
                resultado_o = instruccion_o.executeQuery("SELECT CAT_ID, CAT_DESCRIPCION FROM INV_CATEGORIA_TB WHERE CAT_EMP_ID=" + id + " ORDER BY 2");
                combo.removeAllItems();
                while (resultado_o.next()) {
                    combo.addItem(new Item(resultado_o.getInt(1), resultado_o.getString(2)));
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Categorias", JOptionPane.ERROR_MESSAGE);
        } finally {
            cerrar();
        }
    }

    public void getMarca(JComboBox combo, int id) {
        try {
            if (conexion_o == null || conexion_o.isClosed()) {
                conectar();
            }
            if (conexion_o != null && !conexion_o.isClosed()) {
                instruccion_o = conexion_o.createStatement();
                resultado_o = instruccion_o.executeQuery("SELECT MAR_ID, MAR_DESCRIPCION FROM INV_MARCA_TB WHERE MAR_EMP_ID=" + id + " ORDER BY 2");
                combo.removeAllItems();
                while (resultado_o.next()) {
                    combo.addItem(new Item(resultado_o.getInt(1), resultado_o.getString(2)));
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Marcas", JOptionPane.ERROR_MESSAGE);
        } finally {
            cerrar();
        }
    }

    public boolean getinvTipos(JComboBox combo, int id) {
        try {
            if (conexion_o == null || conexion_o.isClosed()) {
                conectar();
            }
            if (conexion_o != null && !conexion_o.isClosed()) {
                instruccion_o = conexion_o.createStatement();
                resultado_o = instruccion_o.executeQuery("SELECT TIN_ID, TIN_DESCRIPCION FROM INV_TIPO_TRA_TB "
                        + "WHERE TIN_SUPERTIPO='COM' AND TIN_EMP_ID=" + id);
                combo.removeAllItems();
                while (resultado_o.next()) {
                    combo.addItem(new Item(resultado_o.getInt(1), resultado_o.getString(2)));
                }
                if (combo.getItemCount() > 0) {
                    return true;
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Tipos COM", JOptionPane.ERROR_MESSAGE);
        } finally {
            cerrar();
        }
        return false;
    }

    public boolean getfacTipos(JComboBox combo, int id) {
        try {
            if (conexion_o == null || conexion_o.isClosed()) {
                conectar();
            }
            if (conexion_o != null && !conexion_o.isClosed()) {
                instruccion_o = conexion_o.createStatement();
                resultado_o = instruccion_o.executeQuery("SELECT TFA_ID, TFA_DESCRIPCION FROM FAC_TIPO_TRA_TB "
                        + "WHERE TFA_SUPERTIPO IN ('FCO', 'FCR') AND TFA_EMP_ID=" + id + " ORDER BY TFA_DESCRIPCION");
                combo.removeAllItems();
                while (resultado_o.next()) {
                    combo.addItem(new Item(resultado_o.getInt(1), resultado_o.getString(2)));
                }
                if (combo.getItemCount() > 0) {
                    return true;
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Tipos FAC", JOptionPane.ERROR_MESSAGE);
        } finally {
            cerrar();
        }
        return false;
    }

    public String[] getProveedor(String id) {
        String[] datos_v = new String[2];
        try {
            if (conexion_o == null || conexion_o.isClosed()) {
                conectar();
            }
            if (conexion_o != null && !conexion_o.isClosed()) {
                instruccion_o = conexion_o.createStatement();
                resultado_o = instruccion_o.executeQuery("SELECT * FROM "
                        + " CXP_PROVEEDOR_VI WHERE PRO_ID=" + id);
                while (resultado_o.next()) {
                    datos_v[0] = resultado_o.getString("PRO_NOMBRE");
                    datos_v[1] = resultado_o.getString("PRO_LIMITE");
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Proveedor", JOptionPane.ERROR_MESSAGE);
        } finally {
            cerrar();
        }
        return datos_v;
    }

    public String[] getCliente(int id) {
        String[] datos_v = new String[5];
        try {
            if (conexion_o == null || conexion_o.isClosed()) {
                conectar();
            }
            if (conexion_o != null && !conexion_o.isClosed()) {
                instruccion_o = conexion_o.createStatement();
                resultado_o = instruccion_o.executeQuery("SELECT * FROM "
                        + " CXC_CLIENTE_VI WHERE CLI_ID=" + id);
                while (resultado_o.next()) {
                    datos_v[0] = resultado_o.getString("CLI_NOMBRE");
                    datos_v[1] = resultado_o.getString("CLI_IDENTIFICACION");
                    datos_v[2] = resultado_o.getString("CLI_PLAZO");
                    datos_v[3] = resultado_o.getString("CLI_DISPONIBLE");
                    datos_v[4] = resultado_o.getString("CLI_LIMITE");
                    return datos_v;
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Cliente", JOptionPane.ERROR_MESSAGE);
        } finally {
            cerrar();
        }
        return datos_v;
    }

    public void getProveedores(DefaultTableModel modelo, int id, String codigo, String nombre) {
        try {
            if (conexion_o == null || conexion_o.isClosed()) {
                conectar();
            }
            if (conexion_o != null && !conexion_o.isClosed()) {
                instruccion_o = conexion_o.createStatement();
                resultado_o = instruccion_o.executeQuery("SELECT * FROM CXP_PROVEEDOR_VI WHERE "
                        + " PRO_EMPRESA=" + id + " AND PRO_ID LIKE '" + codigo + "' AND UPPER(PRO_NOMBRE) LIKE '" + nombre + "' ORDER BY PRO_NOMBRE ASC");
                while (resultado_o.next()) {
                    Object[] row = {resultado_o.getInt("PRO_ID"), resultado_o.getString("PRO_NOMBRE"),
                        resultado_o.getString("PRO_IDENTIFICACION"), resultado_o.getInt("PRO_PLAZO")};
                    modelo.addRow(row);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Proveedores", JOptionPane.ERROR_MESSAGE);
        } finally {
            cerrar();
        }
    }

    public void getClientes(DefaultTableModel modelo, int id, String codigo, String nombre, String identificacion) {
        try {
            if (conexion_o == null || conexion_o.isClosed()) {
                conectar();
            }
            if (conexion_o != null && !conexion_o.isClosed()) {
                instruccion_o = conexion_o.createStatement();
                resultado_o = instruccion_o.executeQuery("SELECT * FROM CXC_CLIENTE_VI WHERE "
                        + " CLI_EMPRESA=" + id + " AND CLI_ID LIKE '" + codigo + "' AND UPPER(CLI_NOMBRE) LIKE '" + nombre + "' AND UPPER(CLI_IDENTIFICACION) LIKE '" + identificacion + "' ORDER BY CLI_NOMBRE ASC");
                while (resultado_o.next()) {
                    Object[] row = {resultado_o.getInt("CLI_ID"), resultado_o.getString("CLI_NOMBRE"),
                        resultado_o.getString("CLI_IDENTIFICACION"), resultado_o.getInt("CLI_PLAZO")};
                    modelo.addRow(row);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Clientes", JOptionPane.ERROR_MESSAGE);
        } finally {
            cerrar();
        }
    }

    public void getCuentas(DefaultTableModel modelo, int id, String codigo, String nombre) {
        try {
            if (conexion_o == null || conexion_o.isClosed()) {
                conectar();
            }
            if (conexion_o != null && !conexion_o.isClosed()) {
                instruccion_o = conexion_o.createStatement();
                resultado_o = instruccion_o.executeQuery("SELECT * FROM CON_CUENTA_VI WHERE "
                        + " CUE_EMP_ID=" + id + " AND CUE_CUENTA LIKE '" + codigo + "' AND UPPER(CUE_DESCRIPCION) LIKE '" + nombre + "' ORDER BY CUE_CUENTA ASC");
                while (resultado_o.next()) {
                    Object[] row = {resultado_o.getInt("CUE_ID"), resultado_o.getString("CUE_CUENTA"),
                        resultado_o.getString("CUE_DSP_CUENTA"), resultado_o.getString("CUE_DESCRIPCION"),
                        resultado_o.getString("CUE_DSP_GRUPO"), resultado_o.getString("CUE_DSP_NATURALEZA")};
                    modelo.addRow(row);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Clientes", JOptionPane.ERROR_MESSAGE);
        } finally {
            cerrar();
        }
    }

    public void getProyectos(DefaultTableModel modelo, int id, String codigo, String nombre) {
        try {
            if (conexion_o == null || conexion_o.isClosed()) {
                conectar();
            }
            if (conexion_o != null && !conexion_o.isClosed()) {
                instruccion_o = conexion_o.createStatement();
                resultado_o = instruccion_o.executeQuery("SELECT * FROM CON_PROYECTO_TB WHERE "
                        + " PRO_EMP_ID=" + id + " AND PRO_CODIGO LIKE '" + codigo + "' AND UPPER(PRO_DESCRIPCION) LIKE '" + nombre + "' ORDER BY PRO_CODIGO ASC");
                while (resultado_o.next()) {
                    Object[] row = {resultado_o.getInt("PRO_ID"), resultado_o.getString("PRO_CODIGO"),
                        resultado_o.getString("PRO_DESCRIPCION")};
                    modelo.addRow(row);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Proyectos", JOptionPane.ERROR_MESSAGE);
        } finally {
            cerrar();
        }
    }

    public void getTerceros(DefaultTableModel modelo, String codigo, String nombre) {
        try {
            if (conexion_o == null || conexion_o.isClosed()) {
                conectar();
            }
            if (conexion_o != null && !conexion_o.isClosed()) {
                instruccion_o = conexion_o.createStatement();
                resultado_o = instruccion_o.executeQuery("SELECT * FROM GEN_PERSONA_TB WHERE "
                        + " PER_IDENTIFICACION LIKE '" + codigo + "' AND UPPER(PER_NOMBRE) LIKE '" + nombre + "' ORDER BY PER_NOMBRE ASC");
                while (resultado_o.next()) {
                    Object[] row = {resultado_o.getInt("PER_ID"), resultado_o.getString("PER_IDENTIFICACION"),
                        resultado_o.getString("PER_NOMBRE")};
                    modelo.addRow(row);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Terceros", JOptionPane.ERROR_MESSAGE);
        } finally {
            cerrar();
        }
    }

    public void getAsientosCreados(DefaultTableModel modelo, int empresa) {
        try {
            if (conexion_o == null || conexion_o.isClosed()) {
                conectar();
            }
            if (conexion_o != null && !conexion_o.isClosed()) {
                instruccion_o = conexion_o.createStatement();
                resultado_o = instruccion_o.executeQuery("SELECT ASP_ID, ASP_NUMERO,"
                        + "TO_CHAR(ASP_FECHA, 'DD/MM/RRRR')DSP_FECHA,"
                        + "ASP_COD_ORIGEN, ASP_DSP_ORIGEN, ASP_DESCRIPCION,"
                        + "ASP_DSP_ESTADO, ASP_DSP_TIPO FROM CON_ASIENTOS_PENDIENTES_VI "
                        + "WHERE ASP_EMPRESA = " + empresa
                        + " ORDER BY ASP_FECHA, ASP_ID");
                while (resultado_o.next()) {
                    Object[] row = {resultado_o.getInt("ASP_ID"), resultado_o.getString("ASP_NUMERO"),
                        resultado_o.getString("DSP_FECHA"), resultado_o.getString("ASP_COD_ORIGEN"),
                        resultado_o.getString("ASP_DSP_ORIGEN"), resultado_o.getString("ASP_DESCRIPCION"),
                        resultado_o.getString("ASP_DSP_ESTADO"), resultado_o.getString("ASP_DSP_TIPO")
                    };
                    modelo.addRow(row);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Asientos", JOptionPane.ERROR_MESSAGE);
        } finally {
            cerrar();
        }
    }

    public void getComprasCreadas(DefaultTableModel modelo) {
        try {
            if (conexion_o == null || conexion_o.isClosed()) {
                conectar();
            }
            if (conexion_o != null && !conexion_o.isClosed()) {
                instruccion_o = conexion_o.createStatement();
                resultado_o = instruccion_o.executeQuery("SELECT ICP_ID, ICP_NUMERO,"
                        + "TO_CHAR(ICP_FECHA, 'DD/MM/RRRR')ICP_FECHA, ICP_DSP_EMPRESA,"
                        + "ICP_DSP_LOCALIZACION, ICP_DSP_DEPARTAMENTO, ICP_DSP_MONEDA,"
                        + "ICP_VALOR_CAMBIO, ICP_DSP_TIPO, ICP_PROVEEDOR,"
                        + "ICP_NOMBRE, ICP_DETALLE FROM INV_COMPRAS_PENDIENTES_VI");
                while (resultado_o.next()) {
                    Object[] row = {resultado_o.getInt("ICP_ID"), resultado_o.getString("ICP_NUMERO"),
                        resultado_o.getString("ICP_FECHA"), resultado_o.getInt("ICP_PROVEEDOR"),
                        resultado_o.getString("ICP_NOMBRE"), resultado_o.getString("ICP_DSP_TIPO"),
                        resultado_o.getString("ICP_DETALLE"), resultado_o.getString("ICP_DSP_MONEDA"),
                        resultado_o.getInt("ICP_VALOR_CAMBIO"), resultado_o.getString("ICP_DSP_EMPRESA"),
                        resultado_o.getString("ICP_DSP_LOCALIZACION"), resultado_o.getString("ICP_DSP_DEPARTAMENTO")
                    };
                    modelo.addRow(row);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Compras", JOptionPane.ERROR_MESSAGE);
        } finally {
            cerrar();
        }
    }

    public void getFacturasCreadas(DefaultTableModel modelo) {
        try {
            if (conexion_o == null || conexion_o.isClosed()) {
                conectar();
            }
            if (conexion_o != null && !conexion_o.isClosed()) {
                instruccion_o = conexion_o.createStatement();
                resultado_o = instruccion_o.executeQuery("SELECT * FROM FAC_FACTURAS_PENDIENTES_VI");
                while (resultado_o.next()) {
                    Object[] row = {resultado_o.getInt("FFP_ID"), resultado_o.getDate("FFP_FECHA"),
                        resultado_o.getInt("FFP_CLIENTE"), resultado_o.getString("FFP_NOMBRE"),
                        resultado_o.getString("FFP_IDENTIFICACION"), resultado_o.getString("FFP_TIPO"),
                        resultado_o.getString("FFP_DETALLE"), resultado_o.getString("FFP_DSP_MONEDA"),
                        resultado_o.getInt("FFP_VALOR_CAMBIO"), resultado_o.getString("FFP_DSP_EMPRESA"),
                        resultado_o.getString("FFP_DSP_LOCALIZACION"), resultado_o.getString("FFP_DSP_DEPARTAMENTO")
                    };
                    modelo.addRow(row);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Facturas", JOptionPane.ERROR_MESSAGE);
        } finally {
            cerrar();
        }
    }

    public String[] getCompraCreada(int id) {
        try {
            if (conexion_o == null || conexion_o.isClosed()) {
                conectar();
            }
            if (conexion_o != null && !conexion_o.isClosed()) {
                instruccion_o = conexion_o.createStatement();
                resultado_o = instruccion_o.executeQuery("SELECT ICP_ID, ICP_NUMERO,"
                        + "TO_CHAR(ICP_FECHA, 'DD/MM/RRRR') ICP_FECHA, ICP_EMPRESA,"
                        + "ICP_LOCALIZACION, ICP_DEPARTAMENTO, ICP_MONEDA,"
                        + "ICP_VALOR_CAMBIO, ICP_TIN_TIPO, ICP_PROVEEDOR,"
                        + "ICP_NOMBRE, ICP_DETALLE FROM INV_COMPRAS_PENDIENTES_VI WHERE ICP_ID=" + id);
                while (resultado_o.next()) {
                    String[] row = {String.valueOf(resultado_o.getInt("ICP_ID")),
                        resultado_o.getString("ICP_NUMERO"),
                        resultado_o.getString("ICP_FECHA"),
                        resultado_o.getString("ICP_EMPRESA"),
                        resultado_o.getString("ICP_LOCALIZACION"),
                        resultado_o.getString("ICP_DEPARTAMENTO"),
                        resultado_o.getString("ICP_MONEDA"),
                        String.valueOf(resultado_o.getInt("ICP_VALOR_CAMBIO")),
                        resultado_o.getString("ICP_TIN_TIPO"),
                        String.valueOf(resultado_o.getInt("ICP_PROVEEDOR")),
                        resultado_o.getString("ICP_NOMBRE"),
                        resultado_o.getString("ICP_DETALLE")};
                    return row;
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Compra", JOptionPane.ERROR_MESSAGE);
        } finally {
            cerrar();
        }
        return null;
    }

    public String[] getFacturaCreada(int id) {
        try {
            if (conexion_o == null || conexion_o.isClosed()) {
                conectar();
            }
            if (conexion_o != null && !conexion_o.isClosed()) {
                instruccion_o = conexion_o.createStatement();
                resultado_o = instruccion_o.executeQuery("SELECT * FROM FAC_FACTURAS_PENDIENTES_VI WHERE FFP_ID=" + id);
                while (resultado_o.next()) {
                    String[] row = {String.valueOf(resultado_o.getInt("FFP_ID")),
                        String.valueOf(resultado_o.getInt("FFP_PRO_ID")),
                        resultado_o.getString("FFP_DETALLE"),
                        String.valueOf(resultado_o.getInt("FFP_VALOR_CAMBIO")),
                        String.valueOf(resultado_o.getInt("FFP_OTROS_CARGOS")),
                        resultado_o.getString("FFP_EMPRESA"),
                        String.valueOf(resultado_o.getInt("FFP_CLIENTE")),
                        resultado_o.getString("FFP_NOMBRE"),
                        resultado_o.getString("FFP_IDENTIFICACION"),
                        resultado_o.getString("FFP_TFA_TIPO"),
                        resultado_o.getString("FFP_MONEDA"),
                        resultado_o.getString("FFP_LOCALIZACION"),
                        resultado_o.getString("FFP_LISTA"),
                        resultado_o.getString("FFP_DEPARTAMENTO"),
                        resultado_o.getString("FFP_TIPO"),
                        String.valueOf(resultado_o.getInt("FFP_PLAZO")),
                        String.valueOf(resultado_o.getDate("FFP_VENCIMIENTO")),
                        resultado_o.getString("FFP_CAJA"),
                        resultado_o.getString("FFP_DISPONIBLE")
                    };
                    return row;
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Factura", JOptionPane.ERROR_MESSAGE);
        } finally {
            cerrar();
        }
        return null;
    }

    public String[] getAsientoCreado(int id) {
        try {
            if (conexion_o == null || conexion_o.isClosed()) {
                conectar();
            }
            if (conexion_o != null && !conexion_o.isClosed()) {
                instruccion_o = conexion_o.createStatement();
                resultado_o = instruccion_o.executeQuery("SELECT "
                        + "ASP_ID, ASP_DSP_ESTADO, ASP_EMPRESA, ASP_NUMERO, "
                        + "TO_CHAR(ASP_FECHA, 'DD/MM/RRRR') ASP_FECHA, ASP_TIPO,"
                        + "ASP_ORIGEN, ASP_ORIGEN, ASP_DESCRIPCION "
                        + "FROM CON_ASIENTOS_PENDIENTES_VI WHERE ASP_ID=" + id);
                while (resultado_o.next()) {
                    String[] row = {String.valueOf(resultado_o.getInt("ASP_ID")),
                        resultado_o.getString("ASP_DSP_ESTADO"),
                        String.valueOf(resultado_o.getInt("ASP_EMPRESA")),
                        resultado_o.getString("ASP_NUMERO"),
                        resultado_o.getString("ASP_FECHA"),
                        resultado_o.getString("ASP_TIPO"),
                        String.valueOf(resultado_o.getInt("ASP_ORIGEN")),
                        resultado_o.getString("ASP_DESCRIPCION")};
                    return row;
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Asiento", JOptionPane.ERROR_MESSAGE);
        } finally {
            cerrar();
        }
        return null;
    }

    public void getDetallesComp(DefaultTableModel modelo, int id, double[] total) {
        try {
            if (conexion_o == null || conexion_o.isClosed()) {
                conectar();
            }
            if (conexion_o != null && !conexion_o.isClosed()) {
                instruccion_o = conexion_o.createStatement();
                resultado_o = instruccion_o.executeQuery("SELECT * FROM "
                        + " INV_TRANSACCION_DET_VI WHERE TRD_TRI_ID=" + id);
                while (resultado_o.next()) {
                    String afecta = "No";
                    if (resultado_o.getString("TRD_AFECTA_ULT_COSTO").equals("S")) {
                        afecta = "Sí";
                    }
                    Object[] row = {resultado_o.getInt("TRD_ID"), resultado_o.getInt("TRD_ART_ID"),
                        resultado_o.getString("TRD_CODIGO"),
                        resultado_o.getString("TRD_DESCRIPCION"), resultado_o.getDouble("TRD_CANTIDAD"),
                        resultado_o.getInt("TRD_CANTIDAD_BONIFICADA"), resultado_o.getString("TRD_UME"),
                        resultado_o.getDouble("TRD_MONTO_UNITARIO"), afecta, resultado_o.getDouble("TRD_SUBTOTAL"),
                        resultado_o.getDouble("TRD_PORCENTAJE_DESCUENTO"), resultado_o.getDouble("TRD_DESCUENTO"),
                        resultado_o.getDouble("TRD_IMPUESTO"), resultado_o.getDouble("TRD_TOTAL")};
                    //Subtotal, Monto, Impuesto, Total
                    total[0] += Double.parseDouble(row[9].toString());
                    total[1] += Double.parseDouble(row[11].toString());
                    total[2] += Double.parseDouble(row[12].toString());
                    total[3] += Double.parseDouble(row[13].toString());
                    modelo.addRow(row);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Detalle Compra", JOptionPane.ERROR_MESSAGE);
        } finally {
            cerrar();
        }
    }

    public void getDetallesFact(DefaultTableModel modelo, int id, double[] total) {
        try {
            if (conexion_o == null || conexion_o.isClosed()) {
                conectar();
            }
            if (conexion_o != null && !conexion_o.isClosed()) {
                instruccion_o = conexion_o.createStatement();
                resultado_o = instruccion_o.executeQuery("SELECT * FROM "
                        + " FAC_FACTURA_DET_VI WHERE FDE_FAC_ID=" + id);
                while (resultado_o.next()) {
                    Object[] row = {resultado_o.getInt("FDE_ID"), resultado_o.getString("FDE_ART_ID"),
                        resultado_o.getString("FDE_CODIGO"), resultado_o.getString("FDE_DESCRIPCION"),
                        resultado_o.getDouble("FDE_CANTIDAD"), resultado_o.getInt("FDE_CANTIDAD_FRACCION"),
                        resultado_o.getDouble("FDE_PRECIO"), resultado_o.getDouble("FDE_SUBTOTAL"),
                        resultado_o.getDouble("FDE_PORCENTAJE_DESCUENTO"), resultado_o.getDouble("FDE_DESCUENTO"),
                        resultado_o.getDouble("FDE_IMPUESTO"), resultado_o.getDouble("FDE_TOTAL")};
                    //Subtotal, Monto, Impuesto, Total
                    total[0] += Double.parseDouble(row[7].toString());
                    total[1] += Double.parseDouble(row[9].toString());
                    total[2] += Double.parseDouble(row[10].toString());
                    total[3] += Double.parseDouble(row[11].toString());
                    modelo.addRow(row);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Detalle Factura", JOptionPane.ERROR_MESSAGE);
        } finally {
            cerrar();
        }
    }

    public void getDetalleAsiento(DefaultTableModel modelo, int id, double[] total) {
        try {
            if (conexion_o == null || conexion_o.isClosed()) {
                conectar();
            }
            if (conexion_o != null && !conexion_o.isClosed()) {
                instruccion_o = conexion_o.createStatement();
                resultado_o = instruccion_o.executeQuery("SELECT * FROM "
                        + " CON_ASIENTO_DET_VI WHERE ADE_ASI_ID=" + id + " ORDER BY ADE_ID");
                while (resultado_o.next()) {
                    Object[] row = {resultado_o.getInt("ADE_ID"), resultado_o.getInt("ADE_CUE_ID"),
                        resultado_o.getString("ADE_CUENTA"),
                        resultado_o.getString("ADE_DSP_CUENTA"), resultado_o.getDouble("ADE_DEBITO"),
                        resultado_o.getDouble("ADE_CREDITO"), resultado_o.getDate("ADE_FECHA"),
                        resultado_o.getString("ADE_NUMERO"), resultado_o.getString("ADE_DETALLE"),
                        resultado_o.getString("ADE_PROYECTO"), resultado_o.getString("ADE_TERCERO")};
                    //Debito, Credito
                    total[0] += Double.parseDouble(row[4].toString());
                    total[1] += Double.parseDouble(row[5].toString());
                    modelo.addRow(row);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Detalle Asiento", JOptionPane.ERROR_MESSAGE);
        } finally {
            cerrar();
        }
    }

    public int crearCompra(int empresa, int localizacion, int tipo, int departamento,
            int moneda, int proveedor, int cliente, int factura, int nota, String documento,
            Date fecha, String estado, int cambio, String detalle, String origen) {

        int retorno = -1;
        try {
            if (conexion_o == null || conexion_o.isClosed()) {
                conectar();
            }
            if (conexion_o != null && !conexion_o.isClosed()) {
                cstmt_o = conexion_o.prepareCall("{ ? = call INV_INS_TRN_FN(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }");
                cstmt_o.registerOutParameter(1, Types.INTEGER);
                cstmt_o.setInt(2, empresa);
                cstmt_o.setInt(3, localizacion);
                cstmt_o.setInt(4, tipo);
                cstmt_o.setInt(5, departamento);
                cstmt_o.setInt(6, moneda);
                cstmt_o.setInt(7, proveedor);
                cstmt_o.setString(8, null);
                cstmt_o.setString(9, null);
                cstmt_o.setString(10, null);
                cstmt_o.setString(11, documento);
                cstmt_o.setDate(12, new java.sql.Date(fecha.getTime()));
                cstmt_o.setString(13, estado);
                cstmt_o.setInt(14, cambio);
                cstmt_o.setString(15, detalle);
                cstmt_o.setString(16, origen);
                cstmt_o.executeQuery();
                retorno = cstmt_o.getInt(1);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Crear Compra", JOptionPane.ERROR_MESSAGE);
        } finally {
            cerrar();
        }
        return retorno;
    }

    public int crearAsiento(int empresa, String numero, Date fecha, String tipo,
            String origen, String descripcion, String usuario) {
        int retorno = -1;
        try {
            if (conexion_o == null || conexion_o.isClosed()) {
                conectar();
            }
            if (conexion_o != null && !conexion_o.isClosed()) {
                cstmt_o = conexion_o.prepareCall("{ ? = call CON_INS_ASI_FN(?, ?, ?, ?, ?, ?, ?) }");
                cstmt_o.registerOutParameter(1, Types.INTEGER);
                cstmt_o.setInt(2, empresa);
                cstmt_o.setString(3, numero);
                cstmt_o.setDate(4, new java.sql.Date(fecha.getTime()));
                cstmt_o.setString(5, tipo);
                cstmt_o.setString(6, origen);
                cstmt_o.setString(7, descripcion);
                cstmt_o.setString(8, usuario);
                cstmt_o.executeQuery();
                retorno = cstmt_o.getInt(1);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Crear Asiento", JOptionPane.ERROR_MESSAGE);
        } finally {
            cerrar();
        }
        return retorno;
    }

    public int validaperiodo(String empresa, String periodo, Date fecha) {
        int retorno = -1;
        try {
            if (conexion_o == null || conexion_o.isClosed()) {
                conectar();
            }
            if (conexion_o != null && !conexion_o.isClosed()) {
                cstmt_o = conexion_o.prepareCall("{ ? = call CON_VALIDA_PER_FN(?, ?, ?) }");
                cstmt_o.registerOutParameter(1, Types.INTEGER);
                cstmt_o.setString(2, empresa);
                cstmt_o.setString(3, periodo);
                cstmt_o.setDate(4, new java.sql.Date(fecha.getTime()));
                cstmt_o.executeQuery();
                retorno = cstmt_o.getInt(1);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Valida Asiento", JOptionPane.ERROR_MESSAGE);
        } finally {
            cerrar();
        }
        return retorno;
    }

    public int crearArticulo(int empresa, int categoria, int unidad, int marca,
            String descripcion, String codigo, String estado, int minimo, int maximo,
            int descuento, String impuesto) {
        int retorno = -1;
        try {
            if (conexion_o == null || conexion_o.isClosed()) {
                conectar();
            }
            if (conexion_o != null && !conexion_o.isClosed()) {
                cstmt_o = conexion_o.prepareCall("{ ? = call INV_INS_ARTICULO_FN(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }");
                cstmt_o.registerOutParameter(1, Types.INTEGER);
                cstmt_o.setInt(2, empresa);
                cstmt_o.setInt(3, categoria);
                cstmt_o.setInt(4, unidad);
                cstmt_o.setInt(5, marca);
                cstmt_o.setString(6, descripcion);
                cstmt_o.setString(7, codigo);
                cstmt_o.setString(8, estado);
                cstmt_o.setInt(9, minimo);
                cstmt_o.setInt(10, maximo);
                cstmt_o.setInt(11, descuento);
                cstmt_o.setString(12, impuesto);
                cstmt_o.executeQuery();
                retorno = cstmt_o.getInt(1);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Crear Articulo", JOptionPane.ERROR_MESSAGE);
        } finally {
            cerrar();
        }
        return retorno;
    }

    public int crearCliente(String nombre, String tipodoc, String doc, Date fnac,
            String telefonos, String direccion, String sexo, String correo,
            String distrito, String cliente, String proveedor) {
        int retorno = -1;
        try {
            if (conexion_o == null || conexion_o.isClosed()) {
                conectar();
            }
            if (conexion_o != null && !conexion_o.isClosed()) {
                cstmt_o = conexion_o.prepareCall("{ ? = call GEN_INS_PERSONA_FN(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }");
                cstmt_o.registerOutParameter(1, Types.INTEGER);
                cstmt_o.setString(2, nombre);
                cstmt_o.setString(3, tipodoc);
                cstmt_o.setString(4, doc);
                //cstmt_o.setDate(5, new java.sql.Date(fnac.getTime()));
                cstmt_o.setDate(5, null);
                cstmt_o.setString(6, telefonos);
                cstmt_o.setString(7, direccion);
                cstmt_o.setString(8, sexo);
                cstmt_o.setString(9, correo);
                cstmt_o.setString(10, distrito);
                cstmt_o.setString(11, cliente);
                cstmt_o.setString(12, proveedor);
                cstmt_o.executeQuery();
                retorno = cstmt_o.getInt(1);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Crear Cliente", JOptionPane.ERROR_MESSAGE);
        } finally {
            cerrar();
        }
        return retorno;
    }

    public int crearFactura(int proforma, int empresa, int localizacion, int departamento,
            int cliente, int lista, int moneda, int caja, int tipo, int plazo,
            String detalle, int cambio, int otros, String nombre, String identificacion) {
        int retorno = -1;
        try {
            if (conexion_o == null || conexion_o.isClosed()) {
                conectar();
            }
            if (conexion_o != null && !conexion_o.isClosed()) {
                cstmt_o = conexion_o.prepareCall("{ ? = call FAC_INS_FACTURA_FN(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }");
                cstmt_o.registerOutParameter(1, Types.INTEGER);
                cstmt_o.setString(2, "");
                //cstmt_o.setInt(2, proforma);
                cstmt_o.setInt(3, empresa);
                cstmt_o.setInt(4, localizacion);
                cstmt_o.setInt(5, departamento);
                cstmt_o.setInt(6, cliente);
                cstmt_o.setInt(7, lista);
                cstmt_o.setInt(8, moneda);
                cstmt_o.setInt(9, caja);
                cstmt_o.setInt(10, tipo);
                cstmt_o.setDate(11, null);//fecha
                cstmt_o.setString(12, "0");//numero                
                cstmt_o.setInt(13, plazo);
                cstmt_o.setString(14, detalle);
                cstmt_o.setInt(15, cambio);
                cstmt_o.setString(16, "N");//Cambio Default
                cstmt_o.setInt(17, otros);
                cstmt_o.setString(18, nombre);
                cstmt_o.setString(19, identificacion);
                cstmt_o.executeQuery();
                retorno = cstmt_o.getInt(1);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Crear Factura", JOptionPane.ERROR_MESSAGE);
        } finally {
            cerrar();
        }
        return retorno;
    }

    public int incluirDetalleCOM(int transaccion, int articulo, double cantidad, int cant_bonificada,
            double monto, double porcentaje_descuento, double costo, int unidad, int linea_fac, String afecta_costo) {
        int retorno = -1;
        try {
            if (conexion_o == null || conexion_o.isClosed()) {
                conectar();
            }
            if (conexion_o != null && !conexion_o.isClosed()) {
                cstmt_o = conexion_o.prepareCall("{? = call INV_INCLUIR_TRN_FN(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }");
                cstmt_o.registerOutParameter(1, Types.NUMERIC);
                cstmt_o.setInt(2, transaccion);
                cstmt_o.setInt(3, articulo);
                cstmt_o.setDouble(4, cantidad);
                cstmt_o.setInt(5, cant_bonificada);
                cstmt_o.setInt(6, 0); //fraccion
                cstmt_o.setDouble(7, monto);
                cstmt_o.setDouble(8, porcentaje_descuento);
                cstmt_o.setDouble(9, costo);
                cstmt_o.setInt(10, unidad);
                cstmt_o.setString(11, null);
                cstmt_o.setString(12, afecta_costo);
                cstmt_o.execute();
                retorno = cstmt_o.getInt(1);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Incluir COM", JOptionPane.ERROR_MESSAGE);
        } finally {
            cerrar();
        }
        return retorno;
    }

    public int incluirDetalleASI(int asiento, int cuenta, double debito, double credito,
            Date fecha, String numero, int tercero, String detalle, int proyecto) {
        int retorno = -1;
        try {
            if (conexion_o == null || conexion_o.isClosed()) {
                conectar();
            }
            if (conexion_o != null && !conexion_o.isClosed()) {
                cstmt_o = conexion_o.prepareCall("{? = call CON_INCLUIR_ASI_FN(?, ?, ?, ?, ?, ?, ?, ?, ?) }");
                cstmt_o.registerOutParameter(1, Types.NUMERIC);
                cstmt_o.setInt(2, asiento);
                cstmt_o.setInt(3, cuenta);
                cstmt_o.setDouble(4, debito);
                cstmt_o.setDouble(5, credito);
                cstmt_o.setDate(6, new java.sql.Date(fecha.getTime()));
                cstmt_o.setString(7, numero);
                if (tercero > 0) {
                    cstmt_o.setInt(8, tercero);
                } else {
                    cstmt_o.setString(8, null);
                }
                cstmt_o.setString(9, detalle);
                if (proyecto > 0) {
                    cstmt_o.setInt(10, proyecto);
                } else {
                    cstmt_o.setString(10, null);
                }
                cstmt_o.execute();
                retorno = cstmt_o.getInt(1);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Incluir ASI", JOptionPane.ERROR_MESSAGE);
        } finally {
            cerrar();
        }
        return retorno;
    }

    public int incluirDetalleFAC(int transaccion, int empresa, int articulo, String descripcion, double cantidad,
            int fraccion, double precio, double porcentaje_descuento, String bonificacion) {
        int retorno = -1;
        try {
            if (conexion_o == null || conexion_o.isClosed()) {
                conectar();
            }
            if (conexion_o != null && !conexion_o.isClosed()) {
                cstmt_o = conexion_o.prepareCall("{ ? = call FAC_INCLUIR_FACTURA_FN(?, ?, ?, ?, ?, ?, ?, ?, ?) }");
                cstmt_o.registerOutParameter(1, Types.DOUBLE);
                cstmt_o.setInt(2, transaccion);
                cstmt_o.setInt(3, empresa);
                cstmt_o.setInt(4, articulo);
                cstmt_o.setString(5, descripcion);
                cstmt_o.setDouble(6, cantidad);
                cstmt_o.setInt(7, fraccion);
                cstmt_o.setDouble(8, precio);
                cstmt_o.setDouble(9, porcentaje_descuento);
                cstmt_o.setString(10, bonificacion);
                cstmt_o.execute();
                retorno = cstmt_o.getInt(1);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Incluir FAC", JOptionPane.ERROR_MESSAGE);
        } finally {
            cerrar();
        }
        return retorno;
    }

    public void getArticulos(DefaultTableModel modelo, int id, int loc, int lista, String codigo, String nombre,
            String categoria, String origen) {
        try {
            if (conexion_o == null || conexion_o.isClosed()) {
                conectar();
            }
            if (conexion_o != null && !conexion_o.isClosed()) {
                instruccion_o = conexion_o.createStatement();
                if (origen.equals("COM")) {
                    resultado_o = instruccion_o.executeQuery("SELECT ART_ID, ART_CODIGO, ART_DESCRIPCION, INV_STOCK_ART_FN(ART_ID, " + loc + ") ||'-'|| INV_STOCKF_ART_FN(ART_ID, " + loc + ") ART_CANTIDAD, "
                            + " 0 ART_PRECIO, CAT_DESCRIPCION ART_CATEGORIA, ART_FRACCIONES, ART_PRECIO_LIBRE "
                            + " FROM INV_ARTICULO_TB, INV_CATEGORIA_TB WHERE ART_CAT_ID = CAT_ID AND ART_EMP_ID=" + id
                            + " AND UPPER(ART_CODIGO) LIKE '" + codigo + "' AND UPPER(ART_DESCRIPCION) LIKE '" + nombre + "' AND UPPER(CAT_DESCRIPCION) LIKE '" + categoria + "' ORDER BY ART_DESCRIPCION");
                } else {
                    resultado_o = instruccion_o.executeQuery("SELECT ART_ID, ART_CODIGO, ART_DESCRIPCION, (SELECT ARL_CANTIDAD ||'-'|| ARL_CANTIDAD_FRACCION FROM INV_ARTICULO_LOCALIZACION_TB WHERE ARL_ART_ID=ART_ID AND ARL_LOC_ID=" + loc + ") ART_CANTIDAD, INV_PRECIO_ART_FN(" + lista + ", ART_ID) ART_PRECIO, "
                            + " CAT_DESCRIPCION ART_CATEGORIA, ART_FRACCIONES, ART_PRECIO_LIBRE "
                            + " FROM INV_ARTICULO_TB, INV_CATEGORIA_TB WHERE ART_CAT_ID = CAT_ID AND ART_EMP_ID=" + id
                            + " AND UPPER(ART_CODIGO) LIKE '" + codigo + "' AND UPPER(ART_DESCRIPCION) LIKE '" + nombre + "' AND UPPER(CAT_DESCRIPCION) LIKE '" + categoria + "' ORDER BY ART_DESCRIPCION");
                }
                while (resultado_o.next()) {
                    Object[] row = {resultado_o.getString("ART_ID"),
                        resultado_o.getString("ART_CODIGO"), resultado_o.getString("ART_DESCRIPCION"),
                        resultado_o.getString("ART_CANTIDAD"), resultado_o.getString("ART_PRECIO"),
                        resultado_o.getString("ART_CATEGORIA"), resultado_o.getString("ART_FRACCIONES"),
                        resultado_o.getString("ART_PRECIO_LIBRE")};
                    modelo.addRow(row);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Articulos", JOptionPane.ERROR_MESSAGE);
        } finally {
            cerrar();
        }
    }

    public String[] valArtMascara(String codigo, int emp, int lista) {
        String[] datos_v = new String[2];
        try {
            if (conexion_o == null || conexion_o.isClosed()) {
                conectar();
            }
            if (conexion_o != null && !conexion_o.isClosed()) {
                cstmt_o = conexion_o.prepareCall("{ call INV_CODIGO_MASCARA_PR(?, ?, ?, ?) }");
                cstmt_o.setInt(1, emp);
                cstmt_o.setInt(2, lista);
                cstmt_o.registerOutParameter(3, Types.VARCHAR);
                cstmt_o.setString(3, codigo);
                cstmt_o.registerOutParameter(4, Types.VARCHAR);
                cstmt_o.execute();
                datos_v[0] = cstmt_o.getString(3);
                datos_v[1] = cstmt_o.getString(4);
                return datos_v;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Articulo", JOptionPane.ERROR_MESSAGE);
        } finally {
            cerrar();
        }
        return datos_v;
    }

    public String getParametro(int empresa_p, String modulo_p, String parametro_p) {
        String resultado_v = "";
        try {
            if (conexion_o == null || conexion_o.isClosed()) {
                conectar();
            }
            if (conexion_o != null && !conexion_o.isClosed()) {
                cstmt_o = conexion_o.prepareCall("{? = call gen_parametro_fn(?, ?, ?)}");
                cstmt_o.registerOutParameter(1, Types.VARCHAR);
                cstmt_o.setInt(2, empresa_p);
                cstmt_o.setString(3, modulo_p);
                cstmt_o.setString(4, parametro_p);
                cstmt_o.executeUpdate();
                resultado_v = cstmt_o.getString(1);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Obtiene Parametro", JOptionPane.ERROR_MESSAGE);
        } finally {
            cerrar();
        }
        return resultado_v;
    }

    public String[] getArticulo(String codigo, int emp, int loc, int lista, String origen) {
        String[] datos_v = new String[7];
        try {
            if (conexion_o == null || conexion_o.isClosed()) {
                conectar();
            }
            if (conexion_o != null && !conexion_o.isClosed()) {
                instruccion_o = conexion_o.createStatement();
                if (origen.equals("COM")) {
                    resultado_o = instruccion_o.executeQuery("SELECT ART_ID, ART_DESCRIPCION, 0 ART_CANTIDAD, 0 ART_PRECIO, "
                            + " ART_FRACCIONES, ART_PRECIO_LIBRE, 0 ART_PRECIO_FRAC FROM INV_ARTICULO_TB "
                            + " WHERE ART_EMP_ID =" + emp + " AND ART_CODIGO='" + codigo + "'");
                } else {
                    resultado_o = instruccion_o.executeQuery("SELECT ART_ID, ART_DESCRIPCION, "
                            + " (SELECT ARL_CANTIDAD ||'-'||ARL_CANTIDAD_FRACCION FROM INV_ARTICULO_LOCALIZACION_TB WHERE ARL_ART_ID = ART_ID AND ARL_LOC_ID = " + loc + ") ART_CANTIDAD, "
                            + " INV_PRECIO_ART_FN(" + lista + ", ART_ID) ART_PRECIO, ART_FRACCIONES, ART_PRECIO_LIBRE,"
                            + " INV_PRECIOF_ART_FN(" + lista + ", ART_ID) ART_PRECIO_FRAC "
                            + " FROM INV_ARTICULO_TB WHERE ART_EMP_ID =" + emp + " AND ART_CODIGO='" + codigo + "'");
                }
                while (resultado_o.next()) {
                    datos_v[0] = resultado_o.getString("ART_ID");
                    datos_v[1] = resultado_o.getString("ART_DESCRIPCION");
                    datos_v[2] = resultado_o.getString("ART_CANTIDAD");
                    datos_v[3] = resultado_o.getString("ART_PRECIO");
                    datos_v[4] = resultado_o.getString("ART_FRACCIONES");
                    datos_v[5] = resultado_o.getString("ART_PRECIO_LIBRE");
                    datos_v[6] = resultado_o.getString("ART_PRECIO_FRAC");
                }
                return datos_v;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Articulo", JOptionPane.ERROR_MESSAGE);
        } finally {
            cerrar();
        }
        return datos_v;
    }

    public String[] getCuenta(String codigo, int emp) {
        String[] datos_v = new String[2];
        try {
            if (conexion_o == null || conexion_o.isClosed()) {
                conectar();
            }
            if (conexion_o != null && !conexion_o.isClosed()) {
                instruccion_o = conexion_o.createStatement();
                resultado_o = instruccion_o.executeQuery("SELECT CUE_ID, CUE_DESCRIPCION "
                        + "FROM CON_CUENTA_TB WHERE CUE_TIPO='D' AND CUE_CUENTA='" + codigo + "' AND CUE_EMP_ID=" + emp);
                while (resultado_o.next()) {
                    datos_v[0] = resultado_o.getString("CUE_ID");
                    datos_v[1] = resultado_o.getString("CUE_DESCRIPCION");
                }
                return datos_v;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Cuenta", JOptionPane.ERROR_MESSAGE);
        } finally {
            cerrar();
        }
        return datos_v;
    }

    public String[] getProyecto(String codigo, int emp) {
        String[] datos_v = new String[2];
        try {
            if (conexion_o == null || conexion_o.isClosed()) {
                conectar();
            }
            if (conexion_o != null && !conexion_o.isClosed()) {
                instruccion_o = conexion_o.createStatement();
                resultado_o = instruccion_o.executeQuery("SELECT PRO_ID, PRO_DESCRIPCION "
                        + "FROM CON_PROYECTO_TB WHERE PRO_CODIGO='" + codigo + "' AND PRO_EMP_ID=" + emp);
                while (resultado_o.next()) {
                    datos_v[0] = resultado_o.getString("PRO_ID");
                    datos_v[1] = resultado_o.getString("PRO_DESCRIPCION");
                }
                return datos_v;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Cuenta", JOptionPane.ERROR_MESSAGE);
        } finally {
            cerrar();
        }
        return datos_v;
    }

    public String[] getTercero(String codigo) {
        String[] datos_v = new String[2];
        try {
            if (conexion_o == null || conexion_o.isClosed()) {
                conectar();
            }
            if (conexion_o != null && !conexion_o.isClosed()) {
                instruccion_o = conexion_o.createStatement();
                resultado_o = instruccion_o.executeQuery("SELECT PER_ID, PER_NOMBRE "
                        + "FROM GEN_PERSONA_TB WHERE PER_ID=" + codigo);
                while (resultado_o.next()) {
                    datos_v[0] = resultado_o.getString("PER_ID");
                    datos_v[1] = resultado_o.getString("PER_NOMBRE");
                }
                return datos_v;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Cuenta", JOptionPane.ERROR_MESSAGE);
        } finally {
            cerrar();
        }
        return datos_v;
    }

    public double getImpuesto(int cliente, int articulo) {
        int retorno = -1;
        try {
            if (conexion_o == null || conexion_o.isClosed()) {
                conectar();
            }
            if (conexion_o != null && !conexion_o.isClosed()) {
                cstmt_o = conexion_o.prepareCall("{ ? = call INV_IMPUESTO_ART_FN(?, ?) }");
                cstmt_o.registerOutParameter(1, Types.DOUBLE);
                cstmt_o.setInt(2, cliente);
                cstmt_o.setInt(3, articulo);
                cstmt_o.executeQuery();
                retorno = cstmt_o.getInt(1);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Impuesto", JOptionPane.ERROR_MESSAGE);
        } finally {
            cerrar();
        }
        return retorno;
    }

    public double getCambio(int origen, int destino) {
        int retorno = -1;
        try {
            if (conexion_o == null || conexion_o.isClosed()) {
                conectar();
            }
            if (conexion_o != null && !conexion_o.isClosed()) {
                cstmt_o = conexion_o.prepareCall("{ ? = call GEN_CAMBIO_FN(?, ?) }");
                cstmt_o.registerOutParameter(1, Types.DOUBLE);
                cstmt_o.setInt(2, origen);
                cstmt_o.setInt(3, destino);
                cstmt_o.executeQuery();
                retorno = cstmt_o.getInt(1);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Cambio", JOptionPane.ERROR_MESSAGE);
        } finally {
            cerrar();
        }
        return retorno;
    }

    public int getMonedaBase(int empresa) {
        int retorno = -1;
        try {
            if (conexion_o == null || conexion_o.isClosed()) {
                conectar();
            }
            if (conexion_o != null && !conexion_o.isClosed()) {
                cstmt_o = conexion_o.prepareCall("{ ? = call GEN_MONEDA_BASE_FN(?) }");
                cstmt_o.registerOutParameter(1, Types.NUMERIC);
                cstmt_o.setInt(2, empresa);
                cstmt_o.executeQuery();
                retorno = cstmt_o.getInt(1);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Cambio", JOptionPane.ERROR_MESSAGE);
        } finally {
            cerrar();
        }
        return retorno;
    }

    public int modificarCompra(int id, int empresa, int localizacion, int tipo, int departamento,
            int moneda, int proveedor, int cliente, int factura, int nota, String documento,
            Date fecha, String estado, int cambio, String detalle, String origen) {
        int retorno = -1;
        try {
            if (conexion_o == null || conexion_o.isClosed()) {
                conectar();
            }
            if (conexion_o != null && !conexion_o.isClosed()) {
                cstmt_o = conexion_o.prepareCall("{ call INV_ACT_TRN_PR(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }");
                cstmt_o.setInt(1, id);
                cstmt_o.setInt(2, empresa);
                cstmt_o.setInt(3, localizacion);
                cstmt_o.setInt(4, tipo);
                cstmt_o.setInt(5, departamento);
                cstmt_o.setInt(6, moneda);
                cstmt_o.setInt(7, proveedor);
                cstmt_o.setString(8, null);
                cstmt_o.setString(9, null);
                cstmt_o.setString(10, null);
                cstmt_o.setDate(11, new java.sql.Date(fecha.getTime()));
                cstmt_o.setString(12, documento);
                cstmt_o.setString(13, estado);
                cstmt_o.setInt(14, cambio);
                cstmt_o.setString(15, detalle);
                cstmt_o.setString(16, origen);
                cstmt_o.executeQuery();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Modifica COM", JOptionPane.ERROR_MESSAGE);
        } finally {
            cerrar();
        }
        return retorno;
    }

    public int modificarAsiento(int id, int empresa, String numero, Date fecha, String tipo,
            String origen, String descripcion, String usuario) {
        int retorno = -1;
        try {
            if (conexion_o == null || conexion_o.isClosed()) {
                conectar();
            }
            if (conexion_o != null && !conexion_o.isClosed()) {
                cstmt_o = conexion_o.prepareCall("{ call CON_ACT_ASI_PR(?, ?, ?, ?, ?, ?, ?, ?) }");
                cstmt_o.setInt(1, id);
                cstmt_o.setInt(2, empresa);
                cstmt_o.setString(3, numero);
                cstmt_o.setDate(4, new java.sql.Date(fecha.getTime()));
                cstmt_o.setString(5, tipo);
                cstmt_o.setString(6, origen);
                cstmt_o.setString(7, descripcion);
                cstmt_o.setString(8, usuario);
                cstmt_o.executeQuery();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Modifica COM", JOptionPane.ERROR_MESSAGE);
        } finally {
            cerrar();
        }
        return retorno;
    }

    public int modificarFactura(int id, int empresa, int localizacion, int departamento, int cliente,
            String nombre, int lista, int moneda, int caja, int tipo, int plazo, String detalle,
            int cambio, int otros) {
        int retorno = -1;
        try {
            if (conexion_o == null || conexion_o.isClosed()) {
                conectar();
            }
            if (conexion_o != null && !conexion_o.isClosed()) {
                cstmt_o = conexion_o.prepareCall("{ call FAC_ACT_FACTURA_PR(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }");
                cstmt_o.setInt(1, id);
                cstmt_o.setInt(2, empresa);
                cstmt_o.setInt(3, localizacion);
                cstmt_o.setInt(4, departamento);
                cstmt_o.setInt(5, cliente);
                cstmt_o.setString(6, nombre);
                cstmt_o.setInt(7, lista);
                cstmt_o.setInt(8, moneda);
                cstmt_o.setInt(9, caja);
                cstmt_o.setInt(10, tipo);
                cstmt_o.setInt(11, plazo);
                cstmt_o.setString(12, detalle);
                cstmt_o.setInt(13, cambio);
                cstmt_o.setString(14, "N");//Cambio Default
                cstmt_o.setInt(15, otros);
                cstmt_o.executeQuery();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Modifica FAC", JOptionPane.ERROR_MESSAGE);
        } finally {
            cerrar();
        }
        return retorno;
    }

    public boolean borrarDetalleCOM(int id) {
        try {
            if (conexion_o == null || conexion_o.isClosed()) {
                conectar();
            }
            if (conexion_o != null && !conexion_o.isClosed()) {
                cstmt_o = conexion_o.prepareCall("{call INV_ELM_LINEA_PR(?) }");
                cstmt_o.setInt(1, id);
                cstmt_o.executeQuery();
                return true;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Borrar COM", JOptionPane.ERROR_MESSAGE);
        } finally {
            cerrar();
        }
        return false;
    }

    public boolean borrarDetalleFAC(int id) {
        try {
            if (conexion_o == null || conexion_o.isClosed()) {
                conectar();
            }
            if (conexion_o != null && !conexion_o.isClosed()) {
                cstmt_o = conexion_o.prepareCall("{call FAC_ELM_LINEA_PR(?) }");
                cstmt_o.setInt(1, id);
                cstmt_o.executeQuery();
                return true;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Borrar FAC", JOptionPane.ERROR_MESSAGE);
        } finally {
            cerrar();
        }
        return false;
    }

    public boolean registrarCompra(int id) {
        try {
            if (conexion_o == null || conexion_o.isClosed()) {
                conectar();
            }
            if (conexion_o != null && !conexion_o.isClosed()) {
                cstmt_o = conexion_o.prepareCall("{call INV_REG_TRANS_PR(?) }");
                cstmt_o.setInt(1, id);
                cstmt_o.executeQuery();
                return true;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Registrar COM", JOptionPane.ERROR_MESSAGE);
        } finally {
            cerrar();
        }
        return false;
    }

    public boolean autorizarAsiento(int id) {
        try {
            if (conexion_o == null || conexion_o.isClosed()) {
                conectar();
            }
            if (conexion_o != null && !conexion_o.isClosed()) {
                cstmt_o = conexion_o.prepareCall("{call CON_AUTORIZAR_PR(?) }");
                cstmt_o.setInt(1, id);
                cstmt_o.executeQuery();
                return true;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Autorizar ASI", JOptionPane.ERROR_MESSAGE);
        } finally {
            cerrar();
        }
        return false;
    }

    public boolean mayorizarAsiento(int id) {
        try {
            if (conexion_o == null || conexion_o.isClosed()) {
                conectar();
            }
            if (conexion_o != null && !conexion_o.isClosed()) {
                cstmt_o = conexion_o.prepareCall("{call CON_MAYORIZAR_PR(?) }");
                cstmt_o.setInt(1, id);
                cstmt_o.executeQuery();
                return true;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Mayorizar ASI", JOptionPane.ERROR_MESSAGE);
        } finally {
            cerrar();
        }
        return false;
    }

    public int reversarASI(int id_p, String usuario) {
        int retorno = -1;
        try {
            if (conexion_o == null || conexion_o.isClosed()) {
                conectar();
            }
            if (conexion_o != null && !conexion_o.isClosed()) {
                cstmt_o = conexion_o.prepareCall("{ ? = call CON_REVERSAR_FN(?, ?) }");
                cstmt_o.registerOutParameter(1, Types.INTEGER);
                cstmt_o.setInt(2, id_p);
                cstmt_o.setString(3, usuario);
                cstmt_o.executeQuery();
                retorno = cstmt_o.getInt(1);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Reversar Asiento", JOptionPane.ERROR_MESSAGE);
        } finally {
            cerrar();
        }
        return retorno;
    }

    public int duplicarASI(int id_p, String usuario) {
        int retorno = -1;
        try {
            if (conexion_o == null || conexion_o.isClosed()) {
                conectar();
            }
            if (conexion_o != null && !conexion_o.isClosed()) {
                cstmt_o = conexion_o.prepareCall("{ ? = call CON_DUPLICAR_FN(?, ?) }");
                cstmt_o.registerOutParameter(1, Types.INTEGER);
                cstmt_o.setInt(2, id_p);
                cstmt_o.setString(3, usuario);
                cstmt_o.executeQuery();
                retorno = cstmt_o.getInt(1);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Duplicar Asiento", JOptionPane.ERROR_MESSAGE);
        } finally {
            cerrar();
        }
        return retorno;
    }

    public String getTipoFac(int tipo_p) {
        String retorno = "SD";
        try {
            if (conexion_o == null || conexion_o.isClosed()) {
                conectar();
            }
            if (conexion_o != null && !conexion_o.isClosed()) {
                cstmt_o = conexion_o.prepareCall("{ ? = call FAC_TIPO_FACTURA_FN(?) }");
                cstmt_o.registerOutParameter(1, Types.VARCHAR);
                cstmt_o.setInt(2, tipo_p);
                cstmt_o.executeQuery();
                retorno = cstmt_o.getString(1);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Tipo Factura", JOptionPane.ERROR_MESSAGE);
        } finally {
            cerrar();
        }
        return retorno;
    }

    public String getTipoCom(int tipo_p) {
        String retorno = "SD";
        try {
            if (conexion_o == null || conexion_o.isClosed()) {
                conectar();
            }
            if (conexion_o != null && !conexion_o.isClosed()) {
                cstmt_o = conexion_o.prepareCall("{ ? = call INV_TIPO_COMPRA_FN(?) }");
                cstmt_o.registerOutParameter(1, Types.VARCHAR);
                cstmt_o.setInt(2, tipo_p);
                cstmt_o.executeQuery();
                retorno = cstmt_o.getString(1);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Tipo Compra", JOptionPane.ERROR_MESSAGE);
        } finally {
            cerrar();
        }
        return retorno;
    }

    public Double getTotalFac(int id_p) {
        Double retorno = 0.0;
        try {
            if (conexion_o == null || conexion_o.isClosed()) {
                conectar();
            }
            if (conexion_o != null && !conexion_o.isClosed()) {
                cstmt_o = conexion_o.prepareCall("{ ? = call FAC_TOTAL_FACTURA_FN(?) }");
                cstmt_o.registerOutParameter(1, Types.DOUBLE);
                cstmt_o.setInt(2, id_p);
                cstmt_o.executeQuery();
                retorno = cstmt_o.getDouble(1);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Total Factura", JOptionPane.ERROR_MESSAGE);
        } finally {
            cerrar();
        }
        return retorno;
    }

    public boolean registrarFactura(int id) {
        try {
            if (conexion_o == null || conexion_o.isClosed()) {
                conectar();
            }
            if (conexion_o != null && !conexion_o.isClosed()) {
                cstmt_o = conexion_o.prepareCall("{call FAC_REGISTRA_FACTURA_PR(?) }");
                cstmt_o.setInt(1, id);
                cstmt_o.executeQuery();
                return true;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Registrar FAC", JOptionPane.ERROR_MESSAGE);
        } finally {
            cerrar();
        }
        return false;
    }

    public boolean cancelaFactura(int id, double efectivo, double otro, int moneda,
            double cambio, double tarjeta, double cheque, double nota, double deposito,
            String numero) {
        try {
            if (conexion_o == null || conexion_o.isClosed()) {
                conectar();
            }
            if (conexion_o != null && !conexion_o.isClosed()) {
                cstmt_o = conexion_o.prepareCall("{call FAC_CANCELA_FACTURA_PR(?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }");
                cstmt_o.setInt(1, id);
                cstmt_o.setDouble(2, efectivo);
                cstmt_o.setDouble(3, otro);
                cstmt_o.setDouble(4, moneda);
                cstmt_o.setDouble(5, cambio);
                cstmt_o.setDouble(6, tarjeta);
                cstmt_o.setDouble(7, cheque);
                cstmt_o.setDouble(8, nota);
                cstmt_o.setDouble(9, deposito);
                cstmt_o.setString(10, numero);
                cstmt_o.executeQuery();
                return true;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Cancelar FAC", JOptionPane.ERROR_MESSAGE);
        } finally {
            cerrar();
        }
        return false;
    }

    public void imprimirFactura(int id) {
        try {
            if (conexion_o == null || conexion_o.isClosed()) {
                conectar();
            }
            if (conexion_o != null && !conexion_o.isClosed()) {
                cstmt_o = conexion_o.prepareCall("{call FAC_ARCHIVO_FACTURA_PR(?) }");
                cstmt_o.setInt(1, id);
                cstmt_o.executeQuery();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Imprimir FAC", JOptionPane.ERROR_MESSAGE);
        } finally {
            cerrar();
        }
    }

    public void actualizausuario(int id, int tipo, String usuario) {
        try {
            if (conexion_o == null || conexion_o.isClosed()) {
                conectar();
            }
            if (conexion_o != null && !conexion_o.isClosed()) {
                //factura creador
                if (tipo == 1) {
                    cstmt_o = conexion_o.prepareCall("UPDATE fac_factura_tb SET fac_creado_por = '"
                            + usuario + "' WHERE fac_id = " + id);
                    cstmt_o.executeQuery();
                    //factura modificador
                } else if (tipo == 2) {
                    cstmt_o = conexion_o.prepareCall("UPDATE fac_factura_tb SET fac_modificado_por = '"
                            + usuario + "' WHERE fac_id = " + id);
                    cstmt_o.executeQuery();
                    //compra creador
                } else if (tipo == 3) {
                    cstmt_o = conexion_o.prepareCall("UPDATE inv_transaccion_tb SET tri_creado_por = '"
                            + usuario + "' WHERE tri_id = " + id);
                    cstmt_o.executeQuery();
                    //compra modificador
                } else if (tipo == 4) {
                    cstmt_o = conexion_o.prepareCall("UPDATE inv_transaccion_tb SET tri_modificado_por = '"
                            + usuario + "' WHERE tri_id = " + id);
                    cstmt_o.executeQuery();
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Actualizar Usuario", JOptionPane.ERROR_MESSAGE);
        } finally {
            cerrar();
        }
    }

    private void cerrar() {
        try {
            if (cstmt_o != null) {
                cstmt_o.close();
            }
            if (resultado_o != null) {
                resultado_o.close();
            }
            if (instruccion_o != null) {
                instruccion_o.close();
            }
            /*if (conexion_o != null) {
                conexion_o.close();
            }*/
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Cerrar Conexiones", JOptionPane.ERROR_MESSAGE);
        }
    }
}
