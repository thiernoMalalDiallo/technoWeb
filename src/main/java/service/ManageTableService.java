
import DAO.UnSql2oModel;


public static class ManageTableService{

    private static UnSql2oModel dao = new UnSql2oModel();

    public static void initDB(){

        dao.dropTable(table);
        dao.createTableElement();
        dao.createTablePossede();
        dao.createTableTag();
    }
}