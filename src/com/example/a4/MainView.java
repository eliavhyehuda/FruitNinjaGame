/**
 * CS349 Winter 2014
 * Assignment 4 Demo Code
 * Jeff Avery & Michael Terry
 */
package com.example.a4;

import android.content.Context;
import android.graphics.*;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

/*
 * View of the main game area.
 * Displays pieces of fruit, and allows players to slice them.
 */
public class MainView extends View implements Observer {
    private final Model model;
    private final MouseDrag drag = new MouseDrag();
    private final Paint paint = new Paint();
    private boolean mouseIsPressed = false;
    private Handler handler = new Handler();
    private final Runnable animation = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (model.getShapes().size() == 0) {
    			model.createNewFruits();
    		}
    		for (Fruit s: model.getShapes()){
    			s.nextView();
    			if(s.isOutOfView()){
    				model.remove(s);
    				model.decreaseLivesLeft();
    				System.out.println("1");
    				if(this.checkLivesLeft()) {
    					System.out.println("5");
    					handler.removeCallbacks(this);
    					return;
    				}
    			}
    		}
    		
    		model.initObservers();
    		//update();
			//handler.removeCallbacks(this);
			//return;
			
			handler.postDelayed(this, 5);
		}
		
		private boolean checkLivesLeft() {
    		if (model.getLivesLeft() <= 0) {
    			model.setGameState(Model.GameState.GAMEOVER);
    			return true;
    		}
    		return false;
    	}
		
		
	};

	
	private final Runnable timer = new Runnable() {
		
		@Override
		public void run() {
			model.increaseElapsedTime();
			handler.postDelayed(this, 1000);
    	}
	};
	
	
	
	
	
	
    // Constructor
    MainView(Context context, Model m) {
        super(context);

        // register this view with the model
        model = m;
        model.addObserver(this);

        // TODO BEGIN CS349
        // test fruit, take this out before handing in!
        //Fruit f1 = new Fruit(new float[] {0, 20, 20, 0, 40, 0, 60, 20, 60, 40, 40, 60, 20, 60, 0, 40});
        //f1.translate(100, 100);
        //f1.initVelocity(0.3, -1.9);
        //model.add(f1);
        

        //Fruit f2 = new Fruit(new float[] {0, 20, 20, 0, 40, 0, 60, 20, 60, 40, 40, 60, 20, 60, 0, 40});
        //f2.translate(200, 200);
        //f2.initVelocity(0.1, -2.1);
        //model.add(f2);
        
        
        handler.post(animation);
        
        handler.post(timer);
        
        
        model.initObservers();
        // TODO END CS349
        
        // add controller
        // capture touch movement, and determine if we intersect a shape
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Log.d(getResources().getString(R.string.app_name), "Touch down");
                    	mouseIsPressed = true;
                        if(model.getGameState() == Model.GameState.NEWGAME) {
                        	model.setGameState(Model.GameState.RUNNING);
                        	//model.notifyObservers();
                        	handler.post(timer);
                        	handler.post(animation);
                        }
                        if(model.getGameState() == Model.GameState.GAMEOVER) {
                        	model.setGameState(Model.GameState.RUNNING);
                        	model.resetGame();
                        	//model.notifyObservers();
                        	handler.post(timer);
                        	handler.post(animation);
                        }
                    	
                        drag.start(event.getX(), event.getY());
                        drag.curr(event.getX(), event.getY());
                        break;

                    case MotionEvent.ACTION_UP:
                        // Log.d(getResources().getString(R.string.app_name), "Touch release");
                        drag.stop(event.getX(), event.getY());
                        drag.curr(event.getX(), event.getY());
                        mouseIsPressed = false;
                        // find intersected shapes
                        Iterator<Fruit> i = model.getShapes().iterator();
                        while(i.hasNext()) {
                            Fruit s = i.next();
                            if (s.intersects(drag.getStart(), drag.getEnd())) {
                            	//s.setFillColor(Color.RED);
                                try {
                                    Fruit[] newFruits = s.split(drag.getStart(), drag.getEnd());

                                    // TODO BEGIN CS349
                                    // you may want to place the fruit more carefully than this
                                    newFruits[0].translate(0, -10);
                                    newFruits[1].translate(0, +10);
                                    // TODO END CS349
                                    model.add(newFruits[0]);
                                    model.add(newFruits[1]);

                                    // TODO BEGIN CS349
                                    // delete original fruit from model
                                    // TODO END CS349

                                } catch (Exception ex) {
                                    Log.e("fruit_ninja", "Error: " + ex.getMessage());
                                }
                            } else {
                                //s.setFillColor(Color.BLUE);
                            }
                            invalidate();
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                    	drag.curr(event.getX(), event.getY());
                }
                return true;
            }
        });
    }

    // inner class to track mouse drag
    // a better solution *might* be to dynamically track touch movement
    // in the controller above
    class MouseDrag {
        private float startx, starty;
        private float currentx, currenty;
        private float endx, endy;

        protected PointF getStart() { return new PointF(startx, starty); }
        protected PointF getEnd() { return new PointF(endx, endy); }
        protected PointF getCurrent() { return new PointF(currentx, currenty); }

        protected void start(float x, float y) {
            this.startx = x;
            this.starty = y;
        }

        protected void stop(float x, float y) {
            this.endx = x;
            this.endy = y;
        }
        
        protected void curr(float x, float y) {
        	this.currentx = x;
        	this.currenty = y;
        }
        
        public void draw(Canvas canvas){
        	int[] x = { (int) drag.getStart().x, (int) drag.getCurrent().x, (int) drag.getCurrent().x, (int) drag.getStart().y};
        	int[] y = { (int) drag.getStart().y-1, (int) drag.getCurrent().y-1, (int) drag.getCurrent().y, (int) drag.getStart().y};
        	paint.setColor(Color.WHITE);
        	paint.setStrokeWidth(3);
        	paint.setStyle(Paint.Style.STROKE);
        	Path p = new Path();
        	p.moveTo(x[0], y[0]);
        	p.lineTo(x[1], y[1]);
        	//for(int i=1; i < x.length; i++){
        	//	p.lineTo(x[i],y[i]);
        	//}
        	canvas.drawPath(p, paint);
        	
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // draw background
        setBackgroundColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        
        
        
        if(model.getGameState() == Model.GameState.NEWGAME) {
        	setBackgroundColor(Color.BLACK);
        	paint.setColor(Color.WHITE);
        	paint.setTextSize(80);
        	canvas.drawText("Shape", 50, 100, paint);
        	canvas.drawText("Ninja", 150, 180, paint);
        	paint.setTextSize(20);
        	canvas.drawText("click to start", 240, 400, paint);
        	handler.removeCallbacks(timer);
        	handler.removeCallbacks(animation);
        	return;
        }
        
        if(model.getGameState() == Model.GameState.GAMEOVER) {
        	setBackgroundColor(Color.BLACK);
        	paint.setColor(Color.WHITE);
        	paint.setTextSize(80);
        	canvas.drawText("Game Over", 50, 150, paint);
        	paint.setTextSize(20);
        	canvas.drawText("Click to restart the game!", 200, 400, paint);
        	return;
        }
        
        
        

        // draw all pieces of fruit
        for (Fruit s : model.getShapes()) {
            s.draw(canvas);
        }
        
        if(mouseIsPressed) drag.draw(canvas);
        //canvas.draw
    }

    @Override
    public void update(Observable observable, Object data) {
        invalidate();
    }
}
