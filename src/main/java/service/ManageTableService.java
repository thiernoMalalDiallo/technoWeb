package service;

import DAO.DAO;


public class ManageTableService {

    private static DAO dao = new DAO();

    public static void initDB() {

        DAO.dropTable("ELEMENT");
        DAO.dropTable("POSSEDE");
        DAO.dropTable("TAG");

        DAO.createTableElement();
        DAO.createTablePossede();
        DAO.createTableTag();
    }
}