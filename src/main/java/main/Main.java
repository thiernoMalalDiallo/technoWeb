package main;

import DAO.DAO;
import controleur.Controller;
import model.Element;
import model.TodoList;
import org.apache.log4j.BasicConfigurator;
import org.h2.jdbcx.JdbcDataSource;

import javax.sql.DataSource;
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
        BasicConfigurator.configure();
        String url = "jdbc:h2:./listout";
        JdbcDataSource datasource = new JdbcDataSource();
        datasource.setURL(url);
        DataSource ds = datasource;
        DAO model = new DAO();

        DAO.dropTable("ELEMENT");
        DAO.dropTable("POSSEDE");
        DAO.dropTable("TAG");

        DAO.createTableElement();
        DAO.createTablePossede();
        DAO.createTableTag();

        String ss = "2018-12-29";
        SimpleDateFormat ssdf = new SimpleDateFormat("yyyy-MM-dd");
        Date ddd = ssdf.parse(ss);
        Date dd = new Date();
        int id = DAO.insertTableElement(1, 1, "2018-12-15", "2018-12-16", "toto au berceau", "toto essai1", 0);
        int id2 = DAO.insertTableElement(2, 1, "2018-12-19", "2018-12-20", "toto au berceaux", "toto essai2", 0);

        TodoList list_e = new TodoList();
        list_e.setListe(DAO.getAllElement());
        Element el = DAO.getElement(1);
        //ListeComposite lc = new ListeComposite();
        //----- Affichage de tous les éléments
        final String[] vals = {""};
        list_e.getListe().forEach(e -> {
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
        DAO.updateElement(1, el.getId(), el.getDateCreation(), el.getDateDerModif(), el.getTitre(), el.getDescription(), 0);
        list_e.setListe(DAO.getAllElement());
        //----- Affichage d'une sous-liste
        final String[] vals3 = {""};
        String finalVals3 = vals3[0];
        try {
            Controller main = new Controller(model, list_e);
            main.routes(args);
        } catch (Exception e) {
            System.err.println("ERREUR INIT SERVEUR" + e);
        }
    }
}
