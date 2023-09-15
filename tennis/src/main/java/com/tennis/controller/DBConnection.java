package com.tennis.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.cj.protocol.Resultset;

public class DBConnection {
    public Connection cnx = null;

    public DBConnection() {
        try {
            this.cnx = DriverManager.getConnection("jdbc:mysql://localhost:3306/tennis",
                      "ankush",
                      "Das@15112001");
            System.out.println("Databse connection established");
        }catch(SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void insertIntoPlayer(String PlayerName) throws SQLException {
    	String insertQuery = "INSERT INTO players (playerName) VALUES (?)";
    	try(PreparedStatement statement  = cnx.prepareStatement(insertQuery)){
    		statement.setString(1, PlayerName);
    		
    		statement.executeUpdate();
    	}catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    public int getPlayerIdByName(String playerName) throws SQLException {
        int playerId = -1; // Default value in case player is not found
        String query = "SELECT player_id FROM players WHERE playerName = ?";
        
        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setString(1, playerName);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    playerId = resultSet.getInt("player_id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return playerId;
    }


    public void insertMatches(String player1, String player2) throws SQLException {
        String insertQuery = "INSERT INTO matches (player1_id, player2_id,matchWinner_id) VALUES (?,?,?)";
        try (PreparedStatement statement = cnx.prepareStatement(insertQuery)) {
            // Assuming you have a method to fetch player IDs by names
            int player1Id = getPlayerIdByName(player1);
            int player2Id = getPlayerIdByName(player2);
//            int matchWinnerID = getPlayerIdByName(matchWinner);

            statement.setInt(1, player1Id);
            statement.setInt(2, player2Id);
            statement.setInt(3, 1);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public int findMatchIdByPlayers(String player1Name, String player2Name) throws SQLException {
        int matchId = -1; // Default value if no match is found
        String query = "SELECT match_id FROM matches WHERE (player1_id = ? AND player2_id = ?) OR (player1_id = ? AND player2_id = ?)";
        
        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            int player1Id = getPlayerIdByName(player1Name);
            int player2Id = getPlayerIdByName(player2Name);

            statement.setInt(1, player1Id);
            statement.setInt(2, player2Id);
            statement.setInt(3, player2Id);
            statement.setInt(4, player1Id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    matchId = resultSet.getInt("match_id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return matchId;
    }
    
    public void updateMatchWinner(String player1Name , String player2Name, String matchWinner) throws SQLException {
    	 String updateQuery = "UPDATE matches SET matchWinner_id = ? WHERE match_id = ?";
    	 try (PreparedStatement statement = cnx.prepareStatement(updateQuery)) {
    		 	
    		 int match_id = findMatchIdByPlayers(player1Name, player2Name);
    		 int matchWinnerId = getPlayerIdByName(matchWinner);
    		 
    	        statement.setInt(1, matchWinnerId);
    	        statement.setInt(2, match_id);

    	        statement.executeUpdate();
    	    } catch (SQLException e) {
    	        e.printStackTrace();
    	    }
    }
    
    
    
    public void insertPoints(String player1Name , String player2Name, String round_winner_name , String Comment , int p1Score , int p2Score , int p1gameScore , int p2gameScore , int p1setScore , int p2setScore , String currServPlayer) throws SQLException {
        String insertQuery = "INSERT INTO Points (match_id,round_winner_id, comment, p1Score, p2Score, p1gameScore, p2gameScore, p1setScore, p2setScore, currServPlayer) VALUES (?, ?, ?,?, ?, ?, ?, ?, ?,?)";
        try (PreparedStatement statement = cnx.prepareStatement(insertQuery)) {

        	int round_winner_id = getPlayerIdByName(round_winner_name);
        	int currServPlayer_id = getPlayerIdByName(currServPlayer);
        	int match_id = findMatchIdByPlayers(player1Name, player2Name);
        	
        	statement.setInt(1, match_id);
            statement.setInt(2, round_winner_id);
            statement.setString(3, Comment);
            statement.setInt(4, p1Score);
            statement.setInt(5, p2Score);
            statement.setInt(6, p1gameScore);
            statement.setInt(7, p2gameScore);
            statement.setInt(8, p1setScore);
            statement.setInt(9, p2setScore);
            statement.setInt(10, currServPlayer_id);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getPlayerNameById(int playerId) throws SQLException {
        String playerName = null; // Default value if no player is found
        String query = "SELECT playerName FROM players WHERE player_id = ?";
        
        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setInt(1, playerId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    playerName = resultSet.getString("playerName");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return playerName;
    }


    public String getMatchWinnerName(String p1, String p2) {
        try {
            PreparedStatement show = cnx.prepareStatement(
                "SELECT m.matchWinner_id FROM matches m WHERE m.match_id = ?");
            
            int matchId = findMatchIdByPlayers(p1, p2);
            show.setInt(1, matchId);

            ResultSet rs = show.executeQuery();

            if (rs.next()) { // Check if there's a result
                int matchWinner_id = rs.getInt("matchWinner_id");
                return getPlayerNameById(matchWinner_id);
            } else {
                // Handle the case when no match winner is found
                return "No match winner found for the specified match";
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the SQLException appropriately (e.g., logging, returning an error message)
            return "An error occurred while fetching match winner name";
        }
    }
    
    public ResultSet getPointsTableData() {
        try {
            PreparedStatement show = cnx.prepareStatement("SELECT * FROM Points");
            ResultSet rs = show.executeQuery();
            return rs; // Return the ResultSet directly

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the SQLException appropriately (e.g., logging, throwing a custom exception)
        }
        
        return null; // Return null if an exception occurs
    }
}