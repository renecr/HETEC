/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hetec;

/**
 *
 * @author Richard
 */
public class Item {

    private int id_i;
    private String descripcion_i;

    public Item(int id_p, String descricion_p) {
        id_i = id_p;
        descripcion_i = descricion_p;
    }

    public int getId() {
        return id_i;
    }

    public String getDescription() {
        return descripcion_i;
    }

    public String toString() {
        return descripcion_i;
    }
}
