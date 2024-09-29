package edu.jsu.mcis.cs310.coursedb.dao;

import java.sql.*;
import com.github.cliftonlabs.json_simple.*;
import java.util.ArrayList;

public class DAOUtility {
    
    public static final int TERMID_FA24 = 1;
    
    public static String getResultSetAsJson(ResultSet rs) {
        
        JsonArray records = new JsonArray();
        
        try {
    
        if (rs != null) {
            //get column names and count
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (rs.next()) {
                JsonObject jsonObject = new JsonObject();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnLabel(i);
                    Object columnValue = rs.getObject(i);

                    //put column name and value into json object
                    jsonObject.put(columnName, columnValue);
                }
                //add the json object to json array
                records.add(jsonObject);
            }
        }
    }    
        
        catch (Exception e) {
            e.printStackTrace();
        }
        return Jsoner.serialize(records); 
    }
    
}
