package com.luismichu.greyadventure.Manager;

import com.badlogic.gdx.Gdx;
import java.sql.*;

public class MyDatabaseManager {
    private static Connection connection;

    private MyDatabaseManager(){}

    public static void drop(){
        try {
            loadDatabase();

            String sql = "DROP TABLE IF EXISTS gameStates";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.executeUpdate();

            sql = "CREATE TABLE IF NOT EXISTS gameStates (position int primary key, name text, level int)";
            statement = connection.prepareStatement(sql);
            statement.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void insert(GameState gameState){
        try {
            loadDatabase();

            String sql = "CREATE TABLE IF NOT EXISTS gameStates (position int primary key, name text, level int)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.executeUpdate();

            sql = "INSERT INTO gameStates (position, name, level) VALUES (?, ?, ?)";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, gameState.position);
            statement.setString(2, gameState.name);
            statement.setInt(3, gameState.level);
            statement.executeUpdate();

            statement.close();
            if (connection != null)
                connection.close();
        } catch (SQLException cnfe) {
            cnfe.printStackTrace();
        }
    }

    public static void update(int position, int level){
        try {
            loadDatabase();

            String sql = "CREATE TABLE IF NOT EXISTS gameStates (position int primary key, name text, level int)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.executeUpdate();

            sql = "UPDATE gameStates SET level = (?) WHERE position = (?)";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, level);
            statement.setInt(2, position);
            statement.executeUpdate();

            statement.close();

            if (connection != null)
                connection.close();
        } catch (SQLException cnfe) {
            cnfe.printStackTrace();
        }
    }

    public static void delete(int position){
        try {
            loadDatabase();

            String sql = "CREATE TABLE IF NOT EXISTS gameStates (position int primary key, name text, level int)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.executeUpdate();

            sql = "DELETE FROM gameStates WHERE position = (?)";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, position);
            statement.executeUpdate();

            statement.close();

            if (connection != null)
                connection.close();
        } catch (SQLException cnfe) {
            cnfe.printStackTrace();
        }
    }

    public static GameState read(int position){
        try {
            loadDatabase();

            String sql = "SELECT position, name, level FROM gameStates WHERE position = (?) ";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, position);
            ResultSet result = statement.executeQuery();

            if(result.isClosed())
                return null;

            GameState gameState = new GameState(result.getInt("position"),
                                                result.getString("name"),
                                                result.getInt("level"));

            statement.close();
            result.close();
            connection.close();

            return gameState;

        } catch (SQLException cnfe) {
            cnfe.printStackTrace();
        }

        return null;
    }

    private static void loadDatabase(){
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + Gdx.files.internal("gameStates.db"));
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}