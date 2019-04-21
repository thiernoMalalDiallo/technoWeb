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
     *
     * @param composant
     * @return
     */
    public boolean add(ToDo composant){
        if(children.add(composant)){
            return true;
        }else{
            return false;
        }
    }

    /**
     *
     * @param composant
     * @return
     */
    public boolean remove(ToDo composant){
        if(children.remove(composant)){
            return true;
        }else{
            return false;
        }
    }

    /**
     *
     * @return
     */
    public List<ToDo> getListe() {
        return children;
    }

    /**
     *
     * @param liste
     */
    public void setListe(List<ToDo> liste) {
        this.children = liste;
    }

    /**
     *
     * @param sql
     * @param liste
     * @param id
     * @return
     */
    public static List<ToDo> rechercheFils(DAO sql, List<ToDo> liste, int id){
        List<ToDo> l = new ArrayList<>();
        for(int i : sql.getAllPossede(id)){
            for (ToDo a: liste) {
                if(a.getId() == i){
                    l.add(a);
                }
            }
        }
        return l;
    }

    /**
     *
     * @param sql
     * @param liste
     * @param id
     * @return
     */
    public static List<ToDo> recherchePere(DAO sql, List<ToDo> liste, int id){
        List<ToDo> l = new ArrayList<>();
        for(int i : sql.getAllPossedant(id)){
            for (ToDo a: liste) {
                if(a.getId() == i){
                    l.add(a);
                }
            }
            //l.add(liste.get(liste.indexOf(i)));
        }
        return l;
    }

    @Override
    public String toString(){
        StringBuffer result = new StringBuffer();
        String vals = "\n" + "</br>Titre: " + super.getTitre() + "\n" +
                "</br>Description: " + super.getDescription() + "\n" +
                //"</br>Liste d'éléments: " + listElement + "\n" +
                "\t" + "</br>Est composée: </br>" + children +
                "</br>";
        return vals;
    }
}
