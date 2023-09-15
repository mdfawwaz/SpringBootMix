package com.notice.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBconnection {
    private PreparedStatement insert = null;
    private Connection cnx = null;

    public DBconnection() {
        try {
            this.cnx = DriverManager.getConnection("jdbc:mysql://localhost:3306/NoticeDB",
                      "ankush",
                      "Das@15112001");
        }catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveNotice(String heading, String body, String author) throws SQLException {
        String insertQuery = "INSERT INTO NoticeBoard (heading, body, author) VALUES (?, ?, ?)";
        System.out.println(cnx);
        try (PreparedStatement statement = cnx.prepareStatement(insertQuery)) {
            statement.setString(1, heading);
            statement.setString(2, body);
            statement.setString(3, author);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getNotice() {
        PreparedStatement show;
        try {
            show = cnx.prepareStatement("SELECT * FROM NoticeBoard ORDER BY sno DESC LIMIT 6");
            ResultSet rs = show.executeQuery();
            return rs;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}