/**
 * CS349 Winter 2014
 * Assignment 4 Demo Code
 * Jeff Avery & Michael Terry
 */
package com.example.a4;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.widget.TextView;

import com.example.a4complete.R;

import java.util.Observable;
import java.util.Observer;

/*
 * View to display the Title, and Score
 * Score currently just increments every time we get an update
 * from the model (i.e. a new fruit is added).
 */
public class TitleView extends TextView implements Observer {
	private final Model model;
	private int count = 0;
	private final Paint paint = new Paint();

    // Constructor requires model reference
    public TitleView(Context context, Model model) {
        super(context);
        this.model = model;

        // set width, height of this view
        this.setHeight(235);
        this.setWidth(MainActivity.displaySize.x);

        // register with model so that we get updates
        model.addObserver(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
    	//super.onDraw(canvas);
    	
    	if (model.getGameState() == Model.GameState.NEWGAME){
    		setBackgroundColor(Color.BLACK);
    		super.onDraw(canvas);
    	}
    	else{
    		setBackgroundColor(Color.WHITE);
    		super.onDraw(canvas);
    		paint.setTextSize(25);
    		paint.setColor(Color.BLACK);
    		canvas.drawText("Android Shape Ninja!                   Score: " + model.getScore(), 15, 35, paint);
    		//setTextSize(20);
    		//setText(getResources().getString(R.string.app_title) + "               Score: " + model.getScore());
    		this.drawLives(canvas);
    	}
		
        // TODO BEGIN CS349
        // add high score, anything else you want to display in the title
        // TODO END CS349
        //setBackgroundColor(Color.BLUE);
        //setTextSize(20);
        //setText(getResources().getString(R.string.app_title) + "               Score: " + model.getScore());
    }

    // Update from model
    // ONLY useful for testing that the view notifications work
    @Override
    public void update(Observable observable, Object data) {
        // TODO BEGIN CS349
        // do something more meaningful here
        // TODO END CS349
        count++;
        invalidate();
    }
    
    private void drawLives(Canvas canvas) {
    	paint.setTextSize(25);
    	paint.setColor(Color.BLACK);
    	canvas.drawText("Score:", 120, 97, paint);
    	
		paint.setColor(Color.RED);
		int start = 200;
		for(int i = 0; i < 5 - model.getLivesLeft(); i++) {
			canvas.drawText("O", start, 100, paint);
			start += 25;
		}
		paint.setColor(Color.GREEN);
		for(int i = 0; i < model.getLivesLeft(); i++) {
			canvas.drawText("O", start, 100, paint);
			start += 25;
		}
	}
}
