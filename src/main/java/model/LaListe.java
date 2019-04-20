package model;

import DAO.UnSql2oModel;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class LaListe extends AListe{
    //
    private List<AListe> children = new ArrayList<>();

    /**
     *
     * @param composant
     * @return
     */
    public boolean add(AListe composant){
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
    public boolean remove(AListe composant){
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
    public List<AListe> getListe() {
        return children;
    }

    /**
     *
     * @param liste
     */
    public void setListe(List<AListe> liste) {
        this.children = liste;
    }

    /**
     *
     * @param sql
     * @param liste
     * @param id
     * @return
     */
    public static List<AListe> rechercheFils(UnSql2oModel sql, List<AListe> liste,int id){
        List<AListe> l = new ArrayList<>();
        for(int i : sql.getAllPossede(id)){
            for (AListe a: liste) {
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
    public static List<AListe> recherchePere(UnSql2oModel sql, List<AListe> liste, int id){
        List<AListe> l = new ArrayList<>();
        for(int i : sql.getAllPossedant(id)){
            for (AListe a: liste) {
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
