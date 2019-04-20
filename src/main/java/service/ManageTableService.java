
import DAO.UnSql2oModel;


public static class ManageTable{

    private static UnSql2oModel dao = new UnSql2oModel();
    
    public static void dropTable(String table){
        
        dao.dropTable(table);
    }

    public static void createTableElement(){
        
        dao.createTableElement();
    }

   
    public static void createTablePossede(){
        
        dao.createTablePossede();
    }

    
    public static void createTableTag(){
        
        dao.createTableTag();
    }
}