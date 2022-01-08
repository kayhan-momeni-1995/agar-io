package objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Gear extends Obj
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1992913184758887910L;
	//type: 2
	public static ArrayList<Gear> all = new ArrayList<Gear>();
	private double radius;
	public static int k=6;
	private Checker c=null;
	
	public Gear(double x, double y)
	{
		super(x, y);
		if (k<=1)
			radius = 10;
		else
			radius = Math.random()*(10*k-10)+10;
		all.add(this);
		ObjectDestroyer a = new ObjectDestroyer(this, 10000);
		a.start();
		c = new Checker();
		c.start();
	}
	public double getRadius()
	{
		return radius;
	}
	
	@Override
	public int getType()
	{
		return 2;
	}

	@Override
	public double getArea()
	{
		return Math.PI*radius*radius;
	}

	@Override
	public boolean isIn(Point p)
	{
		if (Point.distance(p.getX(), p.getY(), this.center.getX(), this.center.getY())<=this.radius)
			return true;
		else
			return false;
	}

	@Override
	public void Render(Graphics G)
	{
		Image bImage=null;
		try
		{
		    File pathToFile = new File("gear.png");
		    bImage = ImageIO.read(pathToFile);
		} catch (IOException ex) {
		    ex.printStackTrace();
		}
		ImageIcon image = new ImageIcon(bImage);
		
		G.drawImage(image.getImage(), (int)(this.center.getX()-this.radius),
				   (int)(this.center.getY()-this.radius),
				   (int)(2*this.radius),
				   (int)(2*this.radius),
				   null);
	}
	@SuppressWarnings("deprecation")
	@Override
	public void destroy()
	{
		synchronized(Color.black)
		{
			if (Obj.all.contains(this))
				Obj.all.remove(this);
			if (all.contains(this))
				all.remove(this);
			c.stop();
		}
	}
	class Checker extends Thread implements Serializable
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = -2586777379278468843L;

		@Override
		public void run()
		{
			while (true)
				check();
		}
	}
	public void check()
	{
		synchronized(Color.black)
		{
			Circle c=null;
			for (int i =0; i<Circle.all.size(); i++)
			{
				c=Circle.all.get(i);
				if (c!=null)
					if (Point.distance(this.getCenter().getX(), this.getCenter().getY(), c.getCenter().getX(), c.getCenter().getY())
							< (this.getRadius()+c.getRadius()) && c.getArea() > this.getArea() && !c.getPlayer().godMode)
					{
						c.devide(10000);;
						this.destroy();
					}
			}
		}
	}

}
