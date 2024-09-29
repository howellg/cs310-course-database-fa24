package edu.jsu.mcis.cs310.coursedb.dao;

import com.github.cliftonlabs.json_simple.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

public class RegistrationDAO {
    /**
    * Using this as a way to play with Javadoc before the team project
    * @since 1.0
    */
    private final DAOFactory daoFactory;
    private final String QUERYINSERT = "INSERT INTO registration (studentid, termid, crn) VALUES (?, ?, ?)";
    private final String QUERYDELETE = "DELETE FROM registration WHERE studentid = ? AND termid = ? AND crn = ?";
    private final String QUERYDELETEALL = "DELETE FROM registration WHERE termid = ? AND studentid = ?";    
    private final String QUERYSELECT = "SELECT * FROM registration WHERE studentid = ? AND termid = ? ORDER BY crn";
    RegistrationDAO(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    public boolean create(int studentid, int termid, int crn) {
        
        boolean result = false;
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            
            Connection conn = daoFactory.getConnection();
            
            if (conn.isValid(0)) {         
                //prep statement with insert statment 
                ps = conn.prepareStatement(QUERYINSERT);
                ps.setInt(1, studentid);
                ps.setInt(2, termid);
                ps.setInt(3, crn);

                //execute update
                int rowsAffected = ps.executeUpdate();

                //check if successful 
                result = (rowsAffected > 0);
            }
            
        }
        
        catch (Exception e) { e.printStackTrace(); }
        
        finally {
            
            if (rs != null) { try { rs.close(); } catch (Exception e) { e.printStackTrace(); } }
            if (ps != null) { try { ps.close(); } catch (Exception e) { e.printStackTrace(); } }
            
        }
        
        return result;
        
    }
/**
 * Deletes a registration record from the registration table based on the student ID, term ID, and CRN.
 * 
 * @param studentid the numeric ID of the student whose registration is to be deleted
 * @param termid the term ID for which the course registration is to be deleted
 * @param crn the course reference number for the section to be deleted
 * @return true if the deletion was successful, false otherwise
 * @since 3.0
 */
    public boolean delete(int studentid, int termid, int crn) {
        
        boolean result = false;
        
        PreparedStatement ps = null;
        
        try {
            
            Connection conn = daoFactory.getConnection();
            
            if (conn.isValid(0)) {
                //prep statement with QUERYDELETE
                ps = conn.prepareStatement(QUERYDELETE);
                ps.setInt(1, studentid);
                ps.setInt(2, termid);
                ps.setInt(3, crn);

                //execute update
                int rowsAffected = ps.executeUpdate();

                //check if successful
                result = (rowsAffected > 0);
                
            }
            
        }
        
        catch (Exception e) { e.printStackTrace(); }
        
        finally {

            if (ps != null) { try { ps.close(); } catch (Exception e) { e.printStackTrace(); } }
            
        }
        
        return result;
        
    }
    
    public boolean delete(int studentid, int termid) {
        
        boolean result = false;
        
        PreparedStatement ps = null;
        
        try {
            
            Connection conn = daoFactory.getConnection();
            
            if (conn.isValid(0)) {
                //from first delete, removed crn
                ps = conn.prepareStatement(QUERYDELETEALL);
                ps.setInt(1, studentid);
                ps.setInt(2, termid);
                
                int rowsAffected = ps.executeUpdate();
                
                result = (rowsAffected > 0);
                
            }
            
        }
        
        catch (Exception e) { e.printStackTrace(); }
        
        finally {

            if (ps != null) { try { ps.close(); } catch (Exception e) { e.printStackTrace(); } }
            
        }
        
        return result;
        
    }
/**
 * list()
 * Retrieves a list of course registrations for a specific student and term.
 * @param studentid the numeric ID of the student.
 * @param termid the term ID.
 * @return a JSON array string containing all course registrations for the specified student and term.
 * @implNote This method uses a prepared statement to query the database.
 * 
 * @see PreparedStatement
 * @see ResultSet
 */
    public String list(int studentid, int termid) {
        
        String result = null;
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        ResultSetMetaData rsmd = null;
        
        try {
            Connection conn = daoFactory.getConnection(); 
            if (conn.isValid(0)) {
                //prep statement
                ps = conn.prepareStatement(QUERYSELECT);
                ps.setInt(1, studentid);
                ps.setInt(2, termid);
                Boolean gotResult = ps.execute();
                
                if(gotResult){
                    rs = ps.getResultSet();
                    JsonArray resultArray = new JsonArray();
                    JsonObject stuID = new JsonObject();
                    JsonObject termID = new JsonObject();     
                    while(rs.next()){
                        stuID.put("studentid", rs.getInt(studentid));
                        termID.put("termid", rs.getInt(termid));
                        resultArray.add(stuID);
                        resultArray.add(termID);
                        result = resultArray.toString();
                    }
                }
            }      
        }
        
        catch (Exception e) { e.printStackTrace(); }
        
        finally {
            
            if (rs != null) { try { rs.close(); } catch (Exception e) { e.printStackTrace(); } }
            
            
        }
        
        return result;
        
    }
    
}
