package objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;

import world.Player;

public class Circle extends Obj implements Comparable<Circle>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7609386870167166176L;
	//type: 0
	public static ArrayList<Circle> all = new ArrayList<Circle>();
	private String name;
	private Color color;
	private double radius;
	private ImageIcon image=null;
	private Player player=null;
	private Checker c=null;
	private Circle parent=null;
	private CenterSetter cs=null;
	private boolean isAte=false;
	private ObjectDestroyer a=null;
	Circle c1=null;
	public boolean isTerminated=false;

	public Circle(Player p, double x, double y, double radius)
	{
		super(x, y);
		this.name=p.getName();
		this.color=p.getColor();
		this.radius=radius;
		this.image=p.getImage();
		this.player=p;
		c = new Checker();
		c.start();
		all.add(this);
	}
	public Circle(Player p, double radius, Circle parent, int ms)
	{
		this(p, parent.getCenter().getX()+parent.getRadius()+radius, parent.getCenter().getY(), radius);
		this.parent=parent;
		cs = new CenterSetter();
		cs.start();
		a = new ObjectDestroyer(this, ms);
		a.start();
	}
	public Circle getParent()
	{
		return parent;
	}
	class CenterSetter extends Thread implements Serializable
	{
		private static final long serialVersionUID = -4818915215368401264L;

		@SuppressWarnings("deprecation")
		@Override
		public void run()
		{
			while (true)
			{
				if (parent.isAte)
				{
					parent=parent.parent;
				}
				if (parent==null)
				{
					a.stop();
					cs.stop();
				}
				System.out.print("");
				synchronized(Color.black)
				{
					int size=getPlayer().myParts.size();
					if (size>1)
					{
						for (int i=1; i<size; i++)
						{
							getPlayer().myParts.get(i).setCenter(getPlayer().myParts.get(i-1).getCenter().getX()+
																 getPlayer().myParts.get(i-1).getRadius()+
																 getPlayer().myParts.get(i).getRadius(),
																 getPlayer().myParts.get(i-1).getCenter().getY());
						}
					}
				}
			}
		}
	}
	public Player getPlayer()
	{
		return player;
	}
	public void setName(String name)
	{
		this.name=name;
	}
	public String getName()
	{
		return name;
	}
	
	public void setRadius(double radius)
	{
		this.radius=radius;
	}
	public double getRadius()
	{
		return radius;
	}
	
	public void setColor(Color color)
	{
		this.color=color;
	}
	public Color getColor()
	{
		return color;
	}
	
	@Override
	public int getType()
	{
		return 0;
	}

	public void increaseArea(double deltaA)
	{
		this.radius=Math.sqrt((this.getArea()+deltaA)/Math.PI);

	}
	@Override
	public double getArea()
	{
		return (Math.PI*radius*radius);
	}
	
	@Override
	public boolean isIn(Point p)
	{
		if (Point.distance(p.getX(), p.getY(), center.getX(), center.getY())<=this.radius)
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
		Image img = null;
		if (image!=null)
			img=image.getImage();
		G.drawImage(img, (int)(this.center.getX()-(Math.sqrt(2)*this.radius/2)),
						   (int)(this.center.getY()-(Math.sqrt(2)*this.radius/2)),
						   (int)(Math.sqrt(2)*this.radius),
						   (int)(Math.sqrt(2)*this.radius),
						   null);

		G.setColor(Color.LIGHT_GRAY);
		java.awt.Font f = new java.awt.Font ("Arial", java.awt.Font.PLAIN, 10);
		G.setFont(f);
		G.drawString(this.getName(),(int)(this.getCenter().getX()-(5*this.getName().length()/2)),
				(int)(this.getCenter().getY()));		
	}

	@SuppressWarnings("deprecation")
	@Override
	public void destroy()
	{
		if (!isTerminated)
		{
			isTerminated=true;
			if (a!=null)
			{
				a.stop();
			}
			synchronized(Color.black)
			{
					if (Obj.all.contains(this))
						Obj.all.remove(this);
					if (all.contains(this))
						all.remove(this);
					if (player.myParts.contains(this))
						player.myParts.remove(this);
					c.stop();
					if (cs!=null)
						cs.stop();
			}
		}
	}

	@Override
	public int compareTo(Circle o)
	{
		if (this.getArea()>o.getArea())
			return -1;
		else if (this.getArea()==o.getArea())
			return 0;
		else
			return +1;
	}
	public void devide(int ms)
	{
		synchronized(Color.black)
		{
			this.radius*=0.707;
			c1 = new Circle (player, radius, this, ms);
			player.myParts.add(c1);
		}
		Thread t = new Thread (new Runnable(){
			@Override
			public void run()
			{
				try {
					TimeUnit.MILLISECONDS.sleep(3*ms);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				synchronized(Color.black)
				{
					c1.getPlayer().collectParts();
				}
			}
		});
		t.start();
	}
	
	class Checker extends Thread implements Serializable
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = -4050090409691172106L;

		@Override
		public void run()
		{
			while (true)
			{
				check();
			}
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
				if (c.getPlayer()==this.getPlayer())
					continue;
				else
					if (Point.distance(this.getCenter().getX(), this.getCenter().getY(), c.getCenter().getX(), c.getCenter().getY())
						< (this.getRadius()+c.getRadius()) && this.getArea()<c.getArea() && !this.getPlayer().godMode)
					{
						c.increaseArea(this.getArea());
						this.isAte=true;
						this.destroy();
						i--;
					}
			}
			
			Feed f=null;
			for (int i =0; i<Feed.all.size(); i++)
			{
				f=Feed.all.get(i);
				if (f!=null)
				{
					if (Point.distance(this.getCenter().getX(), this.getCenter().getY(), f.getCenter().getX(), f.getCenter().getY())
					< (this.getRadius()+f.getRadius()))
					{
						this.increaseArea(f.getArea());
						f.destroy();
					}
				}
			}
		}	
	}

}
