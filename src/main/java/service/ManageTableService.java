
import DAO.DAO;


public static class ManageTableService{

    private static DAO dao = new DAO();

    public static void initDB(){

        dao.dropTable(table);
        dao.createTableElement();
        dao.createTablePossede();
        dao.createTableTag();
    }
}