package com.tennis.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class Game {
	public Player player1;
	public Player player2;
	public Player servingPlayer;
	public boolean GameOver;
	public String WinnerString = "";
	
	public Game(Player player1 , Player player2) {
		this.player1 = player1;
		this.player2 = player2;
		servingPlayer = player1;
		GameOver = false;
	}
	
	public void switchServingPlayer() {
        servingPlayer = (servingPlayer == player1) ? player2 : player1;
    }
	
//	 public void PlayerScore(Player p) {
//	        if (p == player1) {
//	            updatePlayerScore(player1);
//	        } else {
//	            updatePlayerScore(player2);
//	        }
//	        
//	        if (hasGameWinner()) {
//	            Player gameWinner = getGameWinner();
//	            gameWinner.gamesWon++;
//	            System.out.println(gameWinner.getName() + " won this game");
//	            player1.score = 0;
//	            player2.score = 0;
//
//	            if (hasSetWinner()) {
//	                Player setWinner = getSetWinner();
//	                setWinner.setsWon++;
////	                player1.gamesWon = 0;
////	                player2.gamesWon = 0;
//	                System.out.println(setWinner.getName() + " won the set");
//	                System.out.println("Set score is " + player1.getSetsWon() + " " + player2.getSetsWon());
//	            }
//	        }
//	    }
//
//	 public void updatePlayerScore(Player player) {
//		    int currentScore = player.getScore();
//
//		    if (currentScore < 40) {
//		        player.setScore(currentScore + 15);
//		 
////		    } else if (currentScore == 40) {
////		        if (player == servingPlayer) {
////		            player.setScore(45); // Advantage
////		        } else {
////		            if (servingPlayer.getScore() == 45) {
////		                servingPlayer.setScore(40); // Back to deuce
////		            }
////		            switchServingPlayer();
////		            player.setScore(40); // Back to 40
////		        }
//		    } else if (currentScore == 45) {
//		        if (player == servingPlayer) {
//		            player.setScore(0);
//		            servingPlayer.setScore(0); // Reset opponent's score to 0
//		            
//		            // Update games won if necessary
//		            player.gamesWon++;
//		            Checkwinner();
//		        } else {
//		            switchServingPlayer();
//		            player.setScore(40); // Back to 40
//		        }
//		    }
//
//		}
//	 
//	 public void Checkwinner() {
//	        if (player1.getSetsWon() == 3) {
//	            player1.winner = true;
//	            System.out.println("Game over");
//	        } else if (player2.getSetsWon() == 3) {
//	            player2.winner = true;
//	            System.out.println("Game over");
//	        }
//	        GameOver = (player1.winner || player2.winner);
//	    }
//	 
//	 public boolean isTiebreak() {
//	        return player1.getGamesWon() == 6 && player2.getGamesWon() == 6;
//	    }
//
//	 public void updateTiebreakScore(Player player) {
//	        player.incrementTiebreakScore();
//	        if (Math.max(player1.getTiebreakScore(), player2.getTiebreakScore()) >= 7 &&
//	            Math.abs(player1.getTiebreakScore() - player2.getTiebreakScore()) >= 2) {
//	            player.gamesWon++; // Player wins the tiebreaker and the set
//	        }
//	    }
//	
//	//check score difference 2 or not
//	public boolean hasGameWinner() {
//        int scoreDifference = Math.abs(player1.getScore() - player2.getScore());
//        return (player1.getScore() >= 4 || player2.getScore() >= 4) && scoreDifference >= 2;
//    }
//	
//	//it returns the winning player
//	public Player getGameWinner() {
//        return (player1.getScore() > player2.getScore()) ? player1 : player2;
//    }
//	
//	//check if we have a set winner
//    public boolean hasSetWinner() {
//        return player1.getGamesWon() >= 6 || player2.getGamesWon() >= 6;
//    }
//
//    //get who is the winner of the set
//    public Player getSetWinner() {
//        return (player1.getGamesWon() > player2.getGamesWon()) ? player1 : player2;
//    } 
    
    public void PlayerScore(Player player) throws SQLException {
        // Check if serving player should switch
        if ((player1.getScore() + player2.getScore()) % 2 == 0) {
            switchServingPlayer();
        }

        if (player == player1) {
            player1.score++;
        } else {
            player2.score++;
        }

        if (hasGameWinner()) {
            Player gameWinner = getGameWinner();
            gameWinner.gamesWon++;
            System.out.println(gameWinner.getName() + " won this game");
            player1.score = 0;
            player2.score = 0;

            if (hasSetWinner()) {
                Player setWinner = getSetWinner();
                setWinner.setsWon++;
                System.out.println(setWinner.getName() + " won the set");
                System.out.println("Set score is " + player1.getSetsWon() + " " + player2.getSetsWon());
                player1.setGamesWon(0);
                player2.setGamesWon(0);
            }
        }
        WinnerString = winner();
        updateScore();
    }

    public boolean hasGameWinner() {
        int scoreDifference = Math.abs(player1.getScore() - player2.getScore());
        return (player1.getScore() >= 4 || player2.getScore() >= 4) && scoreDifference >= 2;
    }

    public Player getGameWinner() {
        return (player1.getScore() > player2.getScore()) ? player1 : player2;
    }

    public boolean hasSetWinner() {
        return player1.getGamesWon() >= 6 || player2.getGamesWon() >= 6;
    }

    public Player getSetWinner() {
        return (player1.getGamesWon() > player2.getGamesWon()) ? player1 : player2;
    }
    
    public String winner() {
    	String player1Name = player1.getName();
    	String player2Name  = player2.getName();
    	
        if (player1.getSetsWon() == 3) {
            return player1Name + " is the winner of this match.";
        } else if (player2.getSetsWon() == 2) {
            return player2Name + " is the winner of this match.";
        } else {
            return "The match is ongoing.";
        }
    }
    
    public void reset() {
    	player1.reset();
    	player2.reset();
    }
    
    DBConnection dbc = new DBConnection();
    
    public void updateScore() throws SQLException {
    	System.out.println(player1.getName());
    	
//    	dbc.insertIntoPlayer(player1.getName());
//    	dbc.insertIntoPlayer(player2.getName());
//    	
//    	dbc.insertMatches(player1.getName(), player2.getName());
//    	
//    	dbc.insertPoints(player1.getName(), player2.getName(), player1.getName(), "wow", player1.getScore(), player2.getScore(), player1.getGamesWon(),player2.getGamesWon(), player1.getSetsWon(),player2.getSetsWon(), servingPlayer.getName());
//    	
//    	if (player1.getSetsWon() == 3) {
//            player1.winner = true;
//           dbc.updateMatchWinner(player1.getName(),player2.getName(), player1.getName());
//        } else if (player2.getSetsWon() == 3) {
//            player2.winner = true;
//            dbc.updateMatchWinner(player1.getName(),player2.getName(), player2.getName());
//            System.out.println("Game over");
//        }
    }
    
    
    public String convertScoreToString(int score) {
		switch(score) {
			case 0: 
				return "Love";
			case 1:
				return "15";
			case 2:
				return "30";
			case 3:
				return "40";
			case 4:
				return "Game";
			default:
				return "deuche";
		}
	}
    
    
    public List<String> getGameScore(){
//    	updateScore();
		List<String> matchScore = new ArrayList<String>();
		if(servingPlayer==player1) {
			matchScore.add(convertScoreToString(player1.score));
			matchScore.add(convertScoreToString(player2.score));
		}else {
			matchScore.add(convertScoreToString(player2.score));
			matchScore.add(convertScoreToString(player1.score));
		}
		return matchScore;
	}
}
