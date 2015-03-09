/**
 * CS349 Winter 2014
 * Assignment 4 Demo Code
 * Jeff Avery
 */
package com.example.a4;
import android.graphics.*;
import android.util.Log;

/**
 * Class that represents a Fruit. Can be split into two separate fruits.
 */
public class Fruit {
    private Path path = new Path();
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Matrix transform = new Matrix();

    
    private boolean newFruit = true;
    private boolean isCuttable = true;
    
    private double dx = 0;
    private double dy = 0;
    
    private double dt = 0;
    
    private double dyNew = 0;
    private double gravity = 1.3;
    private int count = 0;
    
    
    
    
    public void initVelocity(double x, double y) {
    	dx = x;
    	dy = y;
    }
    
    public void nextView() {
    	dyNew = (dy + (gravity * Math.sqrt(dt)));
    	double xTranslate = dx;
    	double yTranslate = dyNew;
    	dt++;
    	this.translate((float) xTranslate, (float)yTranslate);
    }
    
    public boolean isOutOfView() { 	
    	
    	//Matrix matrix = this.getTransform();
    	//int yMin = matrix.MTRANS_Y;
    	
    	/*
    	if (count == 500){
    		return true;
    	}
    	else
    		count++;
    		return false;
    	
    	*/
    	
    	Matrix matrix = this.getTransform();
    	float[] values = new float[9];
    	matrix.getValues(values);
    	float yMin = values[Matrix.MTRANS_Y];
    	
    	//int yMin = matrix.MTRANS_Y;
    	//Path fruit = this.getTransformedPath();
    	//Region region = new Region();
    	//boolean test = region.setPath(fruit, new Region(0,0,0,0));
    	//Rect fBounds = region.getBounds();
        //int yMin = fBounds.top;
        	
        if (yMin < 580 && newFruit){
        	System.out.println("False1");
        	newFruit = false;
       	}
       	if(yMin > 580 && !newFruit){
       		System.out.println("True2");
       		return true;
       	}
       	else{
       		//System.out.print("false2");
       		return false;
        }
       	
    }
    
    
    /**
     * A fruit is represented as Path, typically populated 
     * by a series of points 
     */
    Fruit(float[] points) {
        init();
        this.path.reset();
        this.path.moveTo(points[0], points[1]);
        for (int i = 2; i < points.length; i += 2) {
            this.path.lineTo(points[i], points[i + 1]);
        }
        this.path.moveTo(points[0], points[1]);
    }

    Fruit(Region region) {
        init();
        this.path = region.getBoundaryPath();
    }

    Fruit(Path path) {
        init();
        this.path = path;
    }

    private void init() {
        this.paint.setColor(Color.BLUE);
        this.paint.setStrokeWidth(5);
    }

    /**
     * The color used to paint the interior of the Fruit.
     */
    public int getFillColor() { return paint.getColor(); }
    public void setFillColor(int color) { paint.setColor(color); }

    /**
     * The width of the outline stroke used when painting.
     */
    public double getOutlineWidth() { return paint.getStrokeWidth(); }
    public void setOutlineWidth(float newWidth) { paint.setStrokeWidth(newWidth); }

    /**
     * Concatenates transforms to the Fruit's affine transform
     */
    public void rotate(float theta) { transform.postRotate(theta); }
    public void scale(float x, float y) { transform.postScale(x, y); }
    public void translate(float tx, float ty) { transform.postTranslate(tx, ty); }

    /**
     * Returns the Fruit's affine transform that is used when painting
     */
    public Matrix getTransform() { return transform; }

    /**
     * The path used to describe the fruit shape.
     */
    public Path getTransformedPath() {
        Path originalPath = new Path(path);
        Path transformedPath = new Path();
        originalPath.transform(transform, transformedPath);
        return transformedPath;
    }

    /**
     * Paints the Fruit to the screen using its current affine
     * transform and paint settings (fill, outline)
     */
    public void draw(Canvas canvas) {
        // TODO BEGIN CS349
        // tell the shape to draw itself using the matrix and paint parameters
        // TODO END CS349
    	Path fruit = this.getTransformedPath();
    	paint.setColor(this.getFillColor());
    	//paint.setStyle(style);
    	canvas.drawPath(fruit, paint);
    }

    /**
     * Tests whether the line represented by the two points intersects
     * this Fruit.
     */
    public boolean intersects(PointF p1, PointF p2) {
        // TODO BEGIN CS349
        // calculate angle between points
        // rotate and flatten points passed in 
        // rotate path and create region for comparison
        // TODO END CS349
    	
    	
    	
    	
        return false;
    }

    /**
     * Returns whether the given point is within the Fruit's shape.
     */
    public boolean contains(PointF p1) {
        Region region = new Region();
        boolean valid = region.setPath(getTransformedPath(), new Region());
        return valid && region.contains((int) p1.x, (int) p1.y);
    }

    /**
     * This method assumes that the line represented by the two points
     * intersects the fruit. If not, unpredictable results will occur.
     * Returns two new Fruits, split by the line represented by the
     * two points given.
     */
    public Fruit[] split(PointF p1, PointF p2) {
    	Path topPath = null;
    	Path bottomPath = null;
    	// TODO BEGIN CS349
        // calculate angle between points
        // rotate and flatten points passed in
        // rotate region
        // define region masks and use to split region into top and bottom
        // TODO END CS349
        if (topPath != null && bottomPath != null) {
           return new Fruit[] { new Fruit(topPath), new Fruit(bottomPath) };
        }
        return new Fruit[0];
    }
}
