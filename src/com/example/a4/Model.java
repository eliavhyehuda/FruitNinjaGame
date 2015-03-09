/**
 * CS349 Winter 2014
 * Assignment 4 Demo Code
 * Jeff Avery & Michael Terry
 */
package com.example.a4;

import android.graphics.Color;
import android.util.Log;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

/*
 * Class the contains a list of fruit to display.
 * Follows MVC pattern, with methods to add observers,
 * and notify them when the fruit list changes.
 */
public class Model extends Observable {
    // List of fruit that we want to display
    private ArrayList<Fruit> shapes = new ArrayList<Fruit>();

    // 3 different game states ( for later use)
 	public enum GameState {
 		NEWGAME, RUNNING, GAMEOVER
 	};
 	
 	
 	// Game Start state
 	public String gameName = "Eliav's Game";
 	private int score = 0;
 	private int livesLeft = 5;
 	private int elapsedTime = 0;
 	private GameState state = GameState.NEWGAME;
    
    
    // Constructor
    Model() {
        shapes.clear();
    }

    // Model methods
    // You may need to add more methods here, depending on required functionality.
    // For instance, this sample makes to effort to discard fruit from the list.
    public void add(Fruit s) {
        shapes.add(s);
        setChanged();
        notifyObservers();
    }
    
    private void notifyObservers1(){
    	setChanged();
    	notifyObservers();
    }

    public void remove(Fruit s) {
        shapes.remove(s);
    }

    public ArrayList<Fruit> getShapes() {
        return (ArrayList<Fruit>) shapes.clone();
    }

    // MVC methods
    // Basic MVC methods to bind view and model together.
    public void addObserver(Observer observer) {
        super.addObserver(observer);
    }

    // a helper to make it easier to initialize all observers
    public void initObservers() {
        setChanged();
        notifyObservers();
    }

    @Override
    public synchronized void deleteObserver(Observer observer) {
        super.deleteObserver(observer);
        setChanged();
        notifyObservers();
    }

    @Override
    public synchronized void deleteObservers() {
        super.deleteObservers();
        setChanged();
        notifyObservers();
    }
    
    
 // ** Score related Methods **
	  
 	public int getScore() {
 		return score;
 	}
 	  
 	public void resetScore() {
 		score = 0;
 	}
 	  
 	public void increaseScore() {
 		score++;
 		this.notifyObservers1();
 	}
 	
 	// ** LivesLeft Related Methods **
 	  
 	public void decreaseLivesLeft() {
 		livesLeft--;
 		this.notifyObservers1();
 	}
 	  
 	public int getLivesLeft() {
 		return livesLeft; 
 	}
 	  
 	public void resetLivesLeft() {
 		livesLeft = 5;
 		this.notifyObservers1();
 	}
 	
 	// Elapsed Time related Methods
 	  
 	public int getElapsedTime() {
 		return elapsedTime;
 	}
 	  
 	public void resetElapsedTime() {
 		elapsedTime = 0;
 	}
 	 
 	public void increaseElapsedTime() {
 		elapsedTime++;
 		this.notifyObservers1();
 	}
 	
 	//Game State related methods
 	  
 	public GameState getGameState() {
 		return state;
 	}
 	  
 	public void setGameState(GameState state) {
 		this.state = state;
 		this.notifyObservers1();
 	}
 	  
 	  
 	//Fruit Related Methods
 	
 	public void createNewFruits(){
 		if (elapsedTime < 5){
 			this.add(this.fruitHouse());
 		}
 		else if (elapsedTime >= 5 && elapsedTime < 15){
 			this.add(this.fruitHouse());
 			this.add(this.fruitHouse());
 		}
 		else{
 			this.add(this.fruitHouse());
 			this.add(this.fruitHouse());
 			this.add(this.fruitHouse());
 		}
 		this.notifyObservers1();
 	}
 	
 	private Fruit fruitHouse() {
 		float startPosition = this.getRandomStartPosition();
 		Fruit f = this.shapeHouse();
 		f.setFillColor(this.colorHouse());
 		f.translate(startPosition, 580);
 		f.initVelocity(this.getRandomHorizontalVelocity(startPosition), this.getRandomVerticalVelocity(startPosition));
 		return f;
 	}

 	private double getRandomVerticalVelocity(double startPosition) {
 		Random random = new Random();
 		double diff = (13.0 - 10.0) * random.nextDouble();
 		  return -(12.0 + diff);
 	}

 	private double getRandomHorizontalVelocity(double startPosition) {
 		Random random = new Random();
 		double difference = (0.4 - 0.2) * random.nextDouble();
 		  if(startPosition < 225)
 			  return 0.2 + difference;
 		  else 
 			  return -(0.2 + difference);
 	}

 	private Float getRandomStartPosition() {
 		Random random = new Random();
 		return random.nextFloat() * 400;
 	}

 	private Fruit shapeHouse() {
 		Random random = new Random();
 		switch (random.nextInt(5)) {
 		case 0:
 			return new Fruit(new float[] {0, 30, 30, 0, 60, 0, 90, 30, 90, 60, 60, 90, 30, 90, 0, 60});
 		case 1:
 			return new Fruit(new float[] {0, 30, 90, 30, 90, 60, 0, 60});
 		case 2:
 			return new Fruit(new float[] {30, 0, 60, 0, 60, 90, 30, 90});
 		case 3:
 			return new Fruit(new float[] {30, 0, 60, 0, 90, 60, 0, 60});
 		default:
 			return new Fruit(new float[] {45, 0, 80, 90, 10, 90});
 		}
 	}
 	
 	private int colorHouse() {
 		Random random = new Random();
 		switch (random.nextInt(6)){
 		case 0:
 			return Color.BLUE;
 		case 1:
 			return Color.YELLOW;
 		case 2:
 			return Color.GREEN;
 		case 3:
 			return Color.RED;
 		case 4:
 			return Color.MAGENTA;
 		case 5:
 			return Color.LTGRAY;
 		default:
 			return Color.CYAN;
 		}
 	}
 	
 	public void resetGame() {
 		score = 0;
 		livesLeft = 5;
 		elapsedTime = 0;
 		for (Fruit s: this.getShapes()) {
 			this.remove(s);
 		}
 		this.notifyObservers();
 	}
 		
    
    
    
}
