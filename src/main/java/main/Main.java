package main;

import DAO.DAO;
import controleur.Controller;
import model.*;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * initialisation base de donné
 */
public class Main {
    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        DAO model = new DAO();

        model.insertTableElement(3, 7, "2013-12-15", "2018-12-16", "papa va au marché", "kirikou",1);
        model.insertTableElement(8, 0, "2017-12-19", "2018-12-20", "j'ai faim", "tatat",0);
        TodoList list_e = new TodoList();
        list_e.setListe(model.getAllElement());
        Element el = model.getElement(1);

        final String[] vals = {""};
        list_e.getListe().forEach(e -> {
            vals[0] += e;
        });

        //----- Affichage d'une liste en particulier
        final String[] vals2 = {""};

        String s = "2018-12-29";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d = sdf.parse(s);
        el.setDateDerModif(d);
        model.updateElement(1, el.getId(), el.getDateCreation(), el.getDateDerModif(), el.getTitre(), el.getDescription(), 0);
        list_e.setListe(model.getAllElement());
        //----- Affichage d'une sous-liste
        final String[] vals3 = {""};
        try {
            Controller main = new Controller(model, list_e);
            main.router(args);
        } catch (Exception e) {
            System.err.println("ERREUR INIT SERVEUR" + e);
        }
    }
}
