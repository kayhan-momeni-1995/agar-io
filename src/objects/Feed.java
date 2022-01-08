package objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

public class Feed extends Obj
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3138180430204584956L;
	//type: 1
	public static ArrayList<Feed> all = new ArrayList<Feed>();
	private Color color;
	final private double radius = 4;
	private ObjectDestroyer a=null;
	public Feed(double x, double y)
	{
		super(x, y);
		int red = (int)(Math.random()*256);
		int green = (int)(Math.random()*256);
		int blue = (int)(Math.random()*256);
		color = new Color (red, green, blue, 200);
		all.add(this);
		a = new ObjectDestroyer(this, (int)((10000*Math.random())+10000));
		a.start();
	}
	
	public Color getColor()
	{
		return color;
	}
	
	public double getRadius()
	{
		return radius;
	}
	
	@Override
	public int getType()
	{
		return 1;
	}

	@Override
	public double getArea()
	{
		return Math.PI*radius*radius;
	}

	@Override
	public boolean isIn(Point p)
	{
		if (Point.distance(p.getX(), p.getY(), center.getX(), center.getY())<=radius)
			return true;
		else
			return false;
	}

	@Override
	public void Render(Graphics G)
	{
		G.setColor(this.getColor());
		G.fillOval((int)(this.getCenter().getX()-this.getRadius()),
				   (int)(this.getCenter().getY()-this.getRadius()),
				   (int)(2*this.getRadius()),
				   (int)(2*this.getRadius())
				   );		
	}

	@SuppressWarnings("deprecation")
	@Override
	public void destroy()
	{
		synchronized (Color.black)
		{
			if (Obj.all.contains(this))
				Obj.all.remove(this);
			if (all.contains(this))
				all.remove(this);	
			a.stop();
		}
	}
}
