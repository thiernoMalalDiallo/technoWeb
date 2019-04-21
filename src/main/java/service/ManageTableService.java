
import DAO.DAO;


public static class ManageTableService{

    private static DAO dao = new DAO();

    public static void initDB(){

        model.dropTable("ELEMENT");
        model.dropTable("POSSEDE");
        model.dropTable("TAG");

        model.createTableElement();
        model.createTablePossede();
        model.createTableTag();
    }
}