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

public class PowerDown extends Obj
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6564549834723332776L;
	//type: 3 | 4
	//Cutter -->3
	//Cleaner-->4
	public static ArrayList<PowerDown> all = new ArrayList<PowerDown>();
	private int myType;
	private double radius=20;
	private ObjectDestroyer a=null;
	private Checker c=null;

	public PowerDown(double x, double y)
	{
		super(x, y);
		double i= Math.random();
		if (i<0.5)
			myType=3;
		else
			myType=4;
		all.add(this);
		a = new ObjectDestroyer(this, 3000);
		a.start();
		c= new Checker();
		c.start();
	}
	
	@Override
	public int getType()
	{
		return myType;
	}

	@Override
	public double getArea()
	{
		return Math.PI*radius*radius;
	}

	@Override
	public boolean isIn(Point p)
	{
		if (Point.distance(p.getX(), p.getY(), this.center.getX(), this.center.getY())<=radius)
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
			File pathToFile = new File("powerDown.png");
		    bImage = ImageIO.read(pathToFile);
		} catch (IOException ex) {
		    ex.printStackTrace();
		}
		ImageIcon image = new ImageIcon(bImage);
		G.drawImage(image.getImage(), (int)(this.center.getX()-radius), (int)(this.center.getY()-radius), (int)(2*radius), (int)(2*radius), null);
	}

	@Override
	public double getRadius()
	{
		return radius;
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
			a.stop();
			c.stop();
		}
	}
	class Checker extends Thread implements Serializable
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 343004362754846998L;

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
							< (this.getRadius()+c.getRadius()) )
					{
						if (this.getType()==3)
						{
							int k=c.getPlayer().myParts.size();
							for (int j=0; j<k; j++)
							{
								c.getPlayer().myParts.get(j).devide(7000);
							}
						}
						else
						{
							if (c.getPlayer().godMode || c.getPlayer().speed)
							{
								c.getPlayer().unsetGodMode();
								c.getPlayer().unsetSpeedMode();
							}
							else
							{
								int k=c.getPlayer().myParts.size();
								for (int j=0; j<k; j++)
								{
									c.getPlayer().myParts.get(j).devide(7000);
								}
							}
						}
						this.destroy();
					}
			}
		}
	}
}
