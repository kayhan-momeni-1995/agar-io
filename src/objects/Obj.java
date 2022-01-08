package objects;

import java.awt.Graphics;
import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;

public abstract class Obj implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8962404131685453619L;
	public static ArrayList<Obj> all = new ArrayList<Obj>();
	Point center= new Point (100, 100);
	
	public Obj(double x, double y)
	{
		Point center = new Point ();
		center.setLocation(x, y);
		this.center=center;
		all.add(this);
	}
	public void setCenter (double x, double y)
	{
		Point center = new Point();
		center.setLocation(x,  y);
		this.center=center;
	}
	public void moveCenter(double dx, double dy)
	{
		double width = world.Map.width;
		double height = world.Map.height;
		
		Point newCenter = new Point();
		if (this.center.getX()+dx > this.getRadius() && this.center.getX()+this.getRadius()< width
			&& this.center.getY()+dy > this.getRadius() && this.center.getY()+this.getRadius()<height)
		{
			newCenter.setLocation(this.center.getX()+dx, this.center.getY()+dy);
			this.center=newCenter;
		}
		else if (this.center.getX()+dx < this.getRadius())
			setCenter(2+this.getRadius(), getCenter().getY());
		else if (this.center.getX()+this.getRadius() > width)
			setCenter(width-2-getRadius(), getCenter().getY());
		else if (this.center.getY()+dy < this.getRadius())
			setCenter (getCenter().getX(), this.getRadius()+2);
		else if (this.center.getY()+this.getRadius()>height)
			setCenter (getCenter().getX(), height-getRadius()-2);
	}
	public Point getCenter()
	{
		return center;
	}
	
	public abstract double getRadius();
	public abstract void destroy();
	public abstract int getType();
	public abstract double getArea();
	public abstract boolean isIn(Point p);
	public abstract void Render(Graphics G);
}
