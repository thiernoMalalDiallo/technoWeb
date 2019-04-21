package model;

import DAO.DAO;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class TodoList extends ToDo {
    //
    private List<ToDo> children = new ArrayList<>();

    /**
     * @param sql
     * @param liste
     * @param id
     * @return
     */
    public static List<ToDo> rechercheFils(DAO sql, List<ToDo> liste, int id) {
        List<ToDo> l = new ArrayList<>();
        for (int i : DAO.getAllPossede(id)) {
            for (ToDo a : liste) {
                if (a.getId() == i) {
                    l.add(a);
                }
            }
        }
        return l;
    }

    /**
     * @param sql
     * @param liste
     * @param id
     * @return
     */
    public static List<ToDo> recherchePere(DAO sql, List<ToDo> liste, int id) {
        List<ToDo> l = new ArrayList<>();
        for (int i : DAO.getAllPossedant(id)) {
            for (ToDo a : liste) {
                if (a.getId() == i) {
                    l.add(a);
                }
            }
            //l.add(liste.get(liste.indexOf(i)));
        }
        return l;
    }

    /**
     * @param composant
     * @return
     */
    public boolean add(ToDo composant) {
        return children.add(composant);
    }

    /**
     * @param composant
     * @return
     */
    public boolean remove(ToDo composant) {
        return children.remove(composant);
    }

    /**
     * @return
     */
    public List<ToDo> getListe() {
        return children;
    }

    /**
     * @param liste
     */
    public void setListe(List<ToDo> liste) {
        this.children = liste;
    }

    @Override
    public String toString() {
        StringBuffer result = new StringBuffer();
        String vals = "\n" + "</br>Titre: " + super.getTitre() + "\n" +
                "</br>Description: " + super.getDescription() + "\n" +
                //"</br>Liste d'éléments: " + listElement + "\n" +
                "\t" + "</br>Est composée: </br>" + children +
                "</br>";
        return vals;
    }
}
