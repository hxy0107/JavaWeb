import java.sql.*;

/**
 * Created by xianyu.hxy on 2015/7/20.
 */
public class ConnectTest {
    public static final String DRIVER = "org.gjt.mm.mysql.Driver";
    public static final String USER = "root";
    public static final String PASS = "1";
    public static final String URL = "jdbc:mysql://localhost:3306";
    public static final int PAGESIZE = 5;
    static int pageCount;
    static int curPage = 1;
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName(DRIVER);
        Connection con = DriverManager.getConnection(URL, USER, PASS);
        String sql = "SELECT * FROM app_info.info;";
        PreparedStatement stat = con.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        ResultSet rs = stat.executeQuery();
        rs.last();
        int size = rs.getRow();
        pageCount = (size%PAGESIZE==0)?(size/PAGESIZE):(size/PAGESIZE+1);


        if(curPage>=pageCount) curPage = pageCount;
        boolean flag = rs.absolute((curPage-1)*PAGESIZE+1);
        int count=1;
        do{
            if(count>=PAGESIZE)break;
            int empno = rs.getInt(1);
            String ename = rs.getString(2);

            count++;

        }while(rs.next());
        con.close();
    }




}
