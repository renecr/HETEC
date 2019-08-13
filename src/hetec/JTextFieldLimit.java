/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hetec;

import javax.swing.text.*;

/**
 *
 * @author Richard
 */
public class JTextFieldLimit extends PlainDocument {

    private int limite_j;
    private boolean mayuscula_j = false;

    public JTextFieldLimit(int limite_p) {
        super();
        limite_j = limite_p;
    }

    public JTextFieldLimit(int limite_p, boolean mayuscula_p) {
        super();
        limite_j = limite_p;
        mayuscula_j = mayuscula_p;
    }

    public void insertString(int offset, String str, AttributeSet attr)
            throws BadLocationException {
        if (str == null) {
            return;
        }

        if ((getLength() + str.length()) <= limite_j) {
            if (mayuscula_j) {
                str = str.toUpperCase();
            }
            super.insertString(offset, str, attr);
        }
    }
}
