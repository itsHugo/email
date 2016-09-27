/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mailmoduledatabase;

import com.hugopham.mailmodules.ExtendedEmail;

import java.sql.SQLException;
import java.util.ArrayList;
import jodd.mail.MailAddress;

/**
 * Interface for CRUD MEthods
 * @author 1334944
 */
public interface EmailDAO {
    //Create
    public int create(ExtendedEmail email) throws SQLException;
    //Read
    public ArrayList<ExtendedEmail> findAll() throws SQLException;
    public ExtendedEmail findID(int id) throws SQLException;
    public ArrayList<ExtendedEmail> 
        findEmailsFor(MailAddress emailAddress) throws SQLException;
    public ArrayList<ExtendedEmail> 
        findEmailsFrom(MailAddress emailAddress) throws SQLException;
    //Update
    public int update(ExtendedEmail email) throws SQLException;
    public int updateFolder(ExtendedEmail email, String folder) throws SQLException;
    //Delete
    public int delete(int ID) throws SQLException;
}
