package com.tennis.controller;

public class Player {
	private String Name;
	public int score;
	public int setsWon;
	public int gamesWon;
	public boolean winner;
	public int tiebreakScore;
	
	public Player(String Name) {
		this.Name = Name;
		winner = false;
	}
	
	public String getName() {
		return Name;
	}
	
	public void setName(String Name) {
		this.Name = Name;
	}

	public int getSetsWon() {
		return setsWon;
	}

	public void setSetsWon(int setsWon) {
		this.setsWon = setsWon;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getGamesWon() {
		return gamesWon;
	}

	public void setGamesWon(int gamesWon) {
		this.gamesWon = gamesWon;
	}
	
	public void incrementTiebreakScore() {
        tiebreakScore++;
    }

    public int getTiebreakScore() {
        return tiebreakScore;
    }
    
    public void reset() {
    	this.score = 0;
    	this.setsWon = 0;
    	this.gamesWon = 0;
    }
}
