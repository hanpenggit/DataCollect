package com.sinodata.h2;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
public class DBDao {

    @Autowired
    DataSource dataSource;

    public int deleteJobInfo(int id){
        int result=0;
        Connection conn=null;
        PreparedStatement ps=null;
        try {
            conn=dataSource.getConnection();
            ps = conn.prepareStatement("delete from T_JOB where ID = ?");
            ps.setInt(1,id);
            result=ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            close(conn,ps,null);
            return result;
        }
    }

    public int editCellJob(int id,String field,String value){
        int result=0;
        Connection conn=null;
        PreparedStatement ps=null;
        try {
            conn=dataSource.getConnection();
            ps = conn.prepareStatement("update T_JOB set "+field+" = '"+value+"' where ID = ?");
            ps.setInt(1,id);
            result=ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            close(conn,ps,null);
            return result;
        }
    }

    public int editJobInfo(HttpServletRequest request){
        //id为空则新增记录,否则修改记录
        String id=request.getParameter("ID");
        boolean updateflag=false;
        if(StringUtils.isNotBlank(id)){
            updateflag=true;
        }
        StringBuilder updateParam=new StringBuilder();
        List<String> values=new ArrayList<>();
        StringBuilder insertParam=new StringBuilder();
        StringBuilder insertParam_q=new StringBuilder();
        Enumeration<String> parameterNames = request.getParameterNames();
        while(parameterNames.hasMoreElements()){
            String key = parameterNames.nextElement();
            if(key.equals("ID")){
                continue;
            }
            String value=request.getParameter(key);
            if(updateflag){
                updateParam.append(key).append("=").append("?,");
            }else{
                insertParam.append(key).append(",");
                insertParam_q.append("?,");
            }
            values.add(value);
        }
        int result=0;
        Connection conn=null;
        PreparedStatement ps=null;
        try {
            conn=dataSource.getConnection();
            int param_index=1;
            if(updateflag){
                StringBuilder sr=new StringBuilder();
                ps = conn.prepareStatement("update T_JOB set "+updateParam.substring(0,updateParam.length()-1)+" where ID = ?");
                for(String value:values){
                    ps.setObject(param_index++,value);
                }
                ps.setInt(param_index++, Integer.parseInt(id));
            }else{
                ps = conn.prepareStatement("insert into T_JOB ("+insertParam.substring(0,insertParam.length()-1)+") values ("+insertParam_q.substring(0,insertParam_q.length()-1)+") ");
                for(String value:values){
                    ps.setObject(param_index++,value);
                }
            }
            result=ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            close(conn,ps,null);
            return result;
        }
    }

    public int countJobInfo(Map<String,String> searchParam){
        int total=0;
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            conn=dataSource.getConnection();
            String where=packWhere(searchParam);
            ps = conn.prepareStatement("select count(1) from T_JOB "+where);
            rs = ps.executeQuery();
            while(rs.next()){
                total=rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            close(conn,ps,rs);
            return total;
        }
    }

    public List<Object> selectJobInfo(int pageIndex, int limit, Map<String,String> searchParam){
        List<Object> records=new ArrayList<Object>();
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            int start=(pageIndex-1)*limit;
            conn=dataSource.getConnection();
            String where=packWhere(searchParam);
            ps = conn.prepareStatement("select ID,UNITNAME,S_IP,S_SID,S_PORT,S_ULOGIN,S_PLOGIN,S_OWNER,S_TABLE,S_MVLOG,T_IP,T_SID,T_PORT,T_ULOGIN,T_PLOGIN,T_OWNER,T_TABLE,T_PK,T_COLS,JOB_ID,INPUT_COND,MVIEW_COND,S_COLS,S_PK,CREATEDATE from T_JOB "+where+" limit ?,?");
            ps.setInt(1,start);
            ps.setInt(2,limit);
            rs = ps.executeQuery();
            while(rs.next()){
                LinkedHashMap<String,Object> record=new LinkedHashMap<String,Object>();
                record.put("ID",rs.getString("ID"));
                record.put("UNITNAME",rs.getString("UNITNAME"));
                record.put("S_IP",rs.getString("S_IP"));
                record.put("S_SID",rs.getString("S_SID"));
                record.put("S_PORT",rs.getString("S_PORT"));
                record.put("S_ULOGIN",rs.getString("S_ULOGIN"));
                record.put("S_PLOGIN",rs.getString("S_PLOGIN"));
                record.put("S_OWNER",rs.getString("S_OWNER"));
                record.put("S_TABLE",rs.getString("S_TABLE"));
                record.put("S_MVLOG",rs.getString("S_MVLOG"));
                record.put("T_IP",rs.getString("T_IP"));
                record.put("T_SID",rs.getString("T_SID"));
                record.put("T_PORT",rs.getString("T_PORT"));
                record.put("T_ULOGIN",rs.getString("T_ULOGIN"));
                record.put("T_PLOGIN",rs.getString("T_PLOGIN"));
                record.put("T_OWNER",rs.getString("T_OWNER"));
                record.put("T_TABLE",rs.getString("T_TABLE"));
                record.put("T_PK",rs.getString("T_PK"));
                record.put("T_COLS",rs.getString("T_COLS"));
                record.put("JOB_ID",rs.getString("JOB_ID"));
                record.put("INPUT_COND",rs.getString("INPUT_COND"));
                record.put("MVIEW_COND",rs.getString("MVIEW_COND"));
                record.put("S_COLS",rs.getString("S_COLS"));
                record.put("S_PK",rs.getString("S_PK"));
                record.put("CREATEDATE",rs.getString("CREATEDATE"));
                records.add(record);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }finally {
            close(conn,ps,rs);
            return records;
        }
    }

    public Map<String,String> getJobInfo(int id){
        Map<String,String> record=new HashMap<String,String>();
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            conn=dataSource.getConnection();
            ps = conn.prepareStatement("select ID,UNITNAME,S_IP,S_SID,S_PORT,S_ULOGIN,S_PLOGIN,S_OWNER,S_TABLE,S_MVLOG,T_IP,T_SID,T_PORT,T_ULOGIN,T_PLOGIN,T_OWNER,T_TABLE,T_PK,T_COLS,JOB_ID,INPUT_COND,MVIEW_COND,S_COLS,S_PK from T_JOB where ID=?");
            ps.setInt(1,id);
            rs = ps.executeQuery();
            while(rs.next()){
                record.put("ID",rs.getString("ID"));
                record.put("UNITNAME",rs.getString("UNITNAME"));
                record.put("S_IP",rs.getString("S_IP"));
                record.put("S_SID",rs.getString("S_SID"));
                record.put("S_PORT",rs.getString("S_PORT"));
                record.put("S_ULOGIN",rs.getString("S_ULOGIN"));
                record.put("S_PLOGIN",rs.getString("S_PLOGIN"));
                record.put("S_OWNER",rs.getString("S_OWNER"));
                record.put("S_TABLE",rs.getString("S_TABLE"));
                record.put("S_MVLOG",rs.getString("S_MVLOG"));
                record.put("T_IP",rs.getString("T_IP"));
                record.put("T_SID",rs.getString("T_SID"));
                record.put("T_PORT",rs.getString("T_PORT"));
                record.put("T_ULOGIN",rs.getString("T_ULOGIN"));
                record.put("T_PLOGIN",rs.getString("T_PLOGIN"));
                record.put("T_OWNER",rs.getString("T_OWNER"));
                record.put("T_TABLE",rs.getString("T_TABLE"));
                record.put("T_PK",rs.getString("T_PK"));
                record.put("T_COLS",rs.getString("T_COLS"));
                record.put("JOB_ID",rs.getString("JOB_ID"));
                record.put("INPUT_COND",rs.getString("INPUT_COND"));
                record.put("MVIEW_COND",rs.getString("MVIEW_COND"));
                record.put("S_COLS",rs.getString("S_COLS"));
                record.put("S_PK",rs.getString("S_PK"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }finally {
            close(conn,ps,rs);
            return record;
        }
    }

    public String packWhere(Map<String,String> searchParam){
        Set<Map.Entry<String, String>> entries = searchParam.entrySet();
        Iterator<Map.Entry<String, String>> it = entries.iterator();
        StringBuilder where=new StringBuilder("where 1=1");
        while(it.hasNext()){
            Map.Entry<String, String> next = it.next();
            String key=next.getKey();
            String value=next.getValue();
            if(StringUtils.isNotBlank(value)){
                where.append(" and ").append(key).append(" like '%").append(value).append("%'");
            }
        }
        return where.toString();
    }

    private void close(Connection conn,PreparedStatement ps,ResultSet rs){
        if(rs!=null){
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(ps!=null){
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(conn!=null){
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
