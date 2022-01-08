package world;

import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;

import objects.Circle;

public class Player implements Serializable
{
	private static final long serialVersionUID = 9132657220015784009L;
	public static int C=500;
	public static ArrayList<Player> all = new ArrayList<Player>();
	public ArrayList<Circle> myParts = new ArrayList<Circle>();
	private String name="";
	private ImageIcon image=null;
	private	Color color;
	private double V;
	public boolean speed=false;
	public boolean godMode=false;
	private int motionX=0, motionY=0;
	private double vx, vy;
	private String tmpName=null;
	private Color tmpColor=null;
	public static String name1 = "Player 1";
	public static ImageIcon image1=null;
	public static Color c1=Color.red;
	public Mover moveMe = new Mover();
	Updater updateMe = new Updater();
	public double ang = -1;
	private String username="";

	public Player(String name, String username, double x, double y, Color color, ImageIcon img)
	{
		this.name=name;
		this.color=color;
		this.image=img;
		this.username=username;
		tmpColor=color;
		tmpName=name;
		myParts.add(new Circle(this, x, y, (double)20));
		moveMe.start();
		updateMe.start();
		V=C/(this.theBiggestPart().getArea());
		all.add(this);	
	}

	public String getName()
	{
		return name;
	}
	public Color getColor()
	{
		return color;
	}
	public ImageIcon getImage()
	{
		return image;
	}
	public void goTo(double x, double y)
	{
		double dx = x - this.myParts.get(0).getCenter().getX();
		double dy = -y + this.myParts.get(0).getCenter().getY();
		ang = Math.atan(dy/dx)+(2*Math.PI);
		if (dx<0)
		{
			ang+=Math.PI;
		}
	}
	private Circle theBiggestPart()
	{
		if (myParts.size()==0)
			return null;
		Iterator<Circle> it = myParts.iterator();
		Circle c;
		Circle c2=null;
		double area=0;
		while (it.hasNext())
		{
			c=it.next();
			if (c.getArea()>area)
			{
				c2=c;
				area=c.getArea();
			}
		}
		return c2;
	}
	public void Render (Graphics G)
	{
		Iterator<Circle> it = myParts.iterator();
		while (it.hasNext())
			it.next().Render(G);
	}
	public class Mover extends Thread implements Serializable
	{
		private static final long serialVersionUID = -8539844052083376859L;

		@Override
		public void run()
		{
			while (true)
			{
				for (int i = myParts.size()-1; i>=0; i--)
				{
					myParts.get(i).moveCenter(vy/2, -vx/2);
				}
				try
				{
					TimeUnit.MILLISECONDS.sleep(10);
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}

			}
		}
	}
	public class Updater extends Thread implements Serializable
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = -2403755383845408687L;

		@Override
		public void run()
		{
			while (true)
			{
				synchronized(Color.black)
				{
					speedUpdated();
				}
			}
		}
	}
	
	private void speedUpdated()
	{
		Circle theBiggestPart = theBiggestPart();
		if (theBiggestPart==null)
		{
			V=0;
		}
		else
		{
			if (speed)
				V=C/Math.sqrt(theBiggestPart.getArea())+1.4;	
			else
				V=(C/Math.sqrt(theBiggestPart.getArea())+1.4)/2;	

		}
		if (ang==-1)
		{
			if (motionX*motionY==0)
			{
				vx=(motionX*V);
				vy=(motionY*V);
			}
			else
			{
				vx= (0.65*V*motionX);
				vy= (0.65*V*motionY);
			}
		}
		else
		{
			vx = V*Math.cos(-ang+Math.PI/2);
			vy = V*Math.sin(-ang+Math.PI/2);
		}
	}
	public void setName(String name)
	{
		this.name=name;
		Iterator<Circle> it = myParts.iterator();
		while (it.hasNext())
			it.next().setName(name);
	}
	public void setColor(Color c)
	{
		this.color=c;
		Iterator<Circle> it = myParts.iterator();
		while (it.hasNext())
			it.next().setColor(c);
	}
	public void setGodMode(int ms)
	{
		Thread gm = new Thread (new Runnable(){

			@Override
			public void run()
			{
				godMode=true;
				tmpName = getName();
				tmpColor = getColor();
				setColor(Color.LIGHT_GRAY);
				setName("God");
				try {
					TimeUnit.MILLISECONDS.sleep(ms);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				godMode=false;
				setColor(tmpColor);
				setName(tmpName);
			}
		});
		gm.start();
	}
	public void unsetGodMode()
	{
		godMode=false;
		setColor(tmpColor);
		setName(tmpName);
	}
	public void collectParts()
	{
		if (myParts.size()<2)
			return;
		synchronized(Color.black)
		{
			int size = myParts.size();
			for(int i=size-1; i>0; i--)
			{
				myParts.get(0).increaseArea(myParts.get(i).getArea());
				myParts.get(i).destroy();
			}
		}
	}
	public void setSpeedMode(int ms)
	{
		Thread spd=new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				speed=true;
				try
				{
					TimeUnit.MILLISECONDS.sleep(ms);
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				speed=false;
			}
		});
		spd.start();
	}
	public void unsetSpeedMode()
	{
		speed=false;
	}

	public void setGoingUp()
	{
		synchronized(Color.black)
		{
			Iterator<Circle> it = myParts.iterator();
			while (it.hasNext())
				it.next().moveCenter(0, -3);
		}
		motionX=1;
	}
	public void unsetGoingUp()
	{
		motionX=0;
	}
	public void setGoingDown()
	{
		synchronized(Color.black)
		{
			Iterator<Circle> it = myParts.iterator();
			while (it.hasNext())
				it.next().moveCenter(0, 3);
		}
		motionX=-1;
	}
	public void unsetGoingDown()
	{
		motionX=0;
	}
	public void setGoingRight()
	{
		synchronized(Color.black)
		{
			Iterator<Circle> it = myParts.iterator();
			while (it.hasNext())
				it.next().moveCenter(3, 0);
		}
		motionY=1;
	}
	public void unsetGoingRight()
	{
		motionY=0;
	}
	public void setGoingLeft()
	{
		synchronized(Color.black)
		{
			Iterator<Circle> it = myParts.iterator();
			while (it.hasNext())
				it.next().moveCenter(-3, 0);
		}
		motionY=-1;
	}

	public void devide(int ms)
	{
		int size = myParts.size();
		if (size>=4)
			return;
		for (int i=size-1; i>=0; i--)
		{
			myParts.get(i).devide(ms);
		}
	}
	public void unsetGoingLeft()
	{
		motionY=0;
	}
	public String getUsername()
	{
		return this.username;
	}
	public static int indexOf(String username)
	{
		int result=-1;
		synchronized(Color.GREEN)
		{
			for (int i=0; i<all.size(); i++)
			{
				if (all.get(i).getUsername().equals(username))
				{
					result=i;
					break;
				}
			}
		}
		return result;
	}
}
