package com.sinodata.h2;

import com.sinodata.excel.ExcelOperate;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.io.*;
import java.sql.*;
import java.util.List;
import java.util.Map;

@Deprecated
public class DBTest {
    public static void main(String[] args) throws SQLException, IOException {
        String createSql=readFile();
//        Map<String,Object> m = ExcelOperate.createSqls();
        Connection conn = ConnectionPool.getInstance().getConnection();
        crateTable(conn,createSql);
        conn.setAutoCommit(false);
//        exeSql(conn,m);
        conn.commit();
        count(conn,"select count(1) from T_JOB");
        conn.close();

//        log("hanpeng");
//        isInfoExits();
    }

    public static String readFile() {
        String fileName="E:/20190618老本装系统备份文件/hanpeng/t_job.txt";
        File file = new File(fileName);
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bf = new BufferedReader(fileReader);
            String line = "";
            String content = "";
            while ((line = bf.readLine()) != null) {
                content += line;
            }
            bf.close();
            fileReader.close();
            return content;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void crateTable(Connection conn,String sql){
        Statement stmt = null;
        try {
            DatabaseMetaData meta = conn.getMetaData();
            ResultSet rsTables = meta.getTables(null, null, "T_JOB",
                    new String[] { "TABLE" });
            if (!rsTables.next()) {
                stmt = conn.createStatement();
                stmt.execute(sql);
                stmt.close();
            }else{
                stmt = conn.createStatement();
                stmt.execute("drop table T_JOB");
                stmt.execute(sql);
                stmt.close();
                System.out.println("Table \"T_JOB\" already exists ");
            }
            rsTables.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void count(Connection conn,String sql){
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            ps = conn.prepareStatement(sql);
            rs=ps.executeQuery();
            while(rs.next()){
                System.out.println(rs.getInt(1)+"  rows");
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            closeConnection(null,ps,rs);
        }
    }

    public static void exeSql(Connection conn,Map<String,Object> m) {
        List<String> sqls= (List<String>) m.get("s");
        String [][] data= (String[][]) m.get("v");
        for(int i=0;i<sqls.size();i++){
            String sql=sqls.get(i);
            PreparedStatement stmt = null;
            try {
                stmt = conn.prepareStatement(sql);
                System.out.println(data[i][0]);
                for(int j=0;j<23;j++){
                    stmt.setString(j+1,data[i][j]);
                }
                stmt.execute();
            }catch (SQLException e) {
                e.printStackTrace();
            }finally {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    public static void log(String log) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement("INSERT INTO joblog (logcontent) VALUES(?)");
            stmt.setString(1, log);
            stmt.execute();
        }catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn, stmt, null);
        }
    }

    public static boolean isInfoExits(){
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement("SELECT id,logcontent,createdate FROM joblog");
            rs = stmt.executeQuery();
            while(rs.next()){
                System.out.println(rs.getInt(1));
                System.out.println(rs.getString(2));
                System.out.println(rs.getTimestamp(3));
            }
        }catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn, stmt, rs);
            return true;
        }
    }

    private static void closeConnection(Connection conn, Statement stmt,ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
