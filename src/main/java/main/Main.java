package main;

import DAO.UnSql2oModel;
import controleur.MainControleur;
import model.*;
import org.h2.jdbcx.JdbcDataSource;

import javax.sql.DataSource;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * initialisation base de donné
 */
public class Main {
    /**
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        String url = "jdbc:h2:./listout";
        JdbcDataSource datasource = new JdbcDataSource();
        datasource.setURL(url);
        DataSource ds = datasource;
        UnSql2oModel model = new UnSql2oModel(ds);

        model.dropTable("ELEMENT");
        model.dropTable("POSSEDE");
        model.dropTable("TAG");

        model.createTableElement();
        model.createTablePossede();
        model.createTableTag();

        String ss = "2018-12-29";
        SimpleDateFormat ssdf = new SimpleDateFormat("yyyy-MM-dd");
        Date ddd = ssdf.parse(ss);
        Date dd = new Date();
        System.out.println(ddd.toString());
        int id = model.insertTableElement(1, 1, "2018-12-15", "2018-12-16", "toto au berceau", "toto essai1",0);
        int id2 = model.insertTableElement(2, 1, "2018-12-19", "2018-12-20", "toto au berceaux", "toto essai2",0);
        LaListe list_e = new LaListe();
        list_e.setListe(model.getAllElement());
        UnElement el = model.getElement(1);
        System.out.println("----- " + el + " -----");
        //ListeComposite lc = new ListeComposite();
        //----- Affichage de tous les éléments
        final String[] vals = {""};
        list_e.getListe().forEach(e -> {
            System.out.println(e);
            vals[0] += e;
        });
        String finalVals = vals[0];

        //----- Affichage d'une liste en particulier
        final String[] vals2 = {""};
        String finalVals2 = vals2[0];

        String s = "2018-12-29";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d = sdf.parse(s);
        el.setDateDerModif(d);
        model.updateElement(1, el.getId(),el.getDateCreation(), el.getDateDerModif(), el.getTitre(), el.getDescription(),0);
        list_e.setListe(model.getAllElement());
        //----- Affichage d'une sous-liste
        final String[] vals3 = {""};
        String finalVals3 = vals3[0];
        try{
            MainControleur main = new MainControleur(model,list_e);
            main.main(args);
        }catch (Exception e){
            System.err.println("ERREUR INIT SERVEUR"+e);
        }
    }
}
