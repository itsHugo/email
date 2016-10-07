/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hugopham.mailmoduledatabase;

import com.hugopham.mailmodules.ExtendedEmail;

import java.sql.SQLException;
import java.util.ArrayList;
import jodd.mail.MailAddress;

/**
 * Interface for CRUD Methods for the Email database
 *
 * @author 1334944
 */
public interface EmailDAO {

    //Create
    public int createEmail(ExtendedEmail email) throws SQLException;
    public int createEmailAddress(String email) throws SQLException;
    //Read

    public ArrayList<ExtendedEmail> findAll() throws SQLException;

    public ExtendedEmail findIDEmail(int id) throws SQLException;

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
