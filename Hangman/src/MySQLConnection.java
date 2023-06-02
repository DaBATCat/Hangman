package org.app.utils;

import java.sql.*;

public class MySQLConnection {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        //String url = "jdbc:mysql://localhost:3306/hangman";
        //String username = "root";
        //String password = "root8";
        //try{
        //    Connection connection = DriverManager.getConnection(url, username, password);
        //}
        //catch (SQLException e){
        //    e.printStackTrace();
        //}
        String url = "jdbc:mysql://localhost:3306/hangman";
        String username = "root";
        String password = "root8";
        Class.forName("com.mysql.cj.jdbc.Driver");

        Connection connection = DriverManager.getConnection(url, username, password);
        Statement statement = connection.createStatement();

        ResultSet resultSet = statement.executeQuery("select * from words;");
        while(resultSet.next()){
            System.out.println(resultSet.getInt(1) + " " + resultSet.getString(2));
        }
        connection.close();
    }
    /*public static void test() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:base.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            statement.executeUpdate("DROP TABLE IF EXISTS terminalroot");
            statement.executeUpdate("CREATE TABLE terminalroot (id INTEGER, name STRING");
            statement.executeUpdate("INSERT INTO terminalroot VALUES(1,'Test 1')");
            statement.executeUpdate("INSERT INTO terminalroot VALUES(2,'Test 2')");
            ResultSet rs = statement.executeQuery("SELECT * FROM terminalroot");
            while(rs.next()){
                System.out.println("Name: " + rs.getString("name"));
                System.out.println("ID: " + rs.getString("id"));
            }
        }
        catch (SQLException e){
            System.err.println(e.getMessage());
        }

        finally {
            try {
                if (connection != null){
                    connection.close();
                }
            }
            catch (SQLException e){
                System.err.println(e.getMessage());
            }
        }
    }
    */
}
