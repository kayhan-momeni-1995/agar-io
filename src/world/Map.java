package world;

import java.awt.Color;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.io.Serializable;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import javax.swing.JPanel;

import objects.Circle;
import objects.Feed;
import objects.Gear;
import objects.PowerDown;
import objects.PowerUp;
public class Map extends JPanel implements Serializable
{	
	private static final long serialVersionUID = 4844511194273675936L;
	public static String winner = "";
	static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	public static int width  = (int)screenSize.getWidth()-12;
	public static int height = (int)screenSize.getHeight()-195;
	public static int maxFeed = 100;
	public static String audio = "defaultAudio.wav";
	public static boolean play = false;
	public Thread t;
	public Thread t2;
	public static boolean isStarted=false;
	public static boolean playWithMouse=false;

	public boolean finished=false;

	
	public void initialize()
	{
		if (mainWindow.MainClientWindow.socket==null)
		{
			for (int i=0; i<maxFeed; i++)
				new Feed(Math.random()*width, Math.random()*height);
			t = new Thread (new Runnable(){

				@Override
				public void run()
				{
					while(true)
					{
						synchronized(Color.black)
						{
							if (Feed.all.size()<maxFeed)
							{
								new Feed(Math.random()*width, Math.random()*height);
							}
						}
					}
				}
			});
			t.start();
			t2 = new Thread (new Runnable(){
				@Override
				public void run()
				{
					while (true)
					{
						synchronized(Color.black)
						{
							if (Gear.all.size()<2)
								new Gear(Math.random()*width, Math.random()*height);
							if (Math.random()*100 < 2)
								new PowerDown(Math.random()*width, Math.random()*height);
							if (Math.random()*100 < 1)
								new PowerUp(Math.random()*width, Math.random()*height);
						}
						try
						{
							TimeUnit.MILLISECONDS.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			});
			t2.start();
		}
	}
	public void paintComponent(Graphics G)
	{
		super.paintComponent(G);
		Render(G);
	}
	
	@SuppressWarnings("deprecation")
	public void Render(Graphics G)
	{
		if (!world.GraphicsEngine.started)
		{
			G.setColor(Color.white);
			G.fillRect(0, 0, width, height);
			G.setColor(Color.darkGray);
			G.drawString(Circle.all.get(0).getName()+" is the winner", 5, 20);
			GraphicsEngine.started=false;
		}
		else
		{
			G.setColor(new Color(255,255,255));
			G.fillRect(0, 0, this.getWidth(), this.getHeight());
			G.setColor(Color.BLACK);
			G.drawRect(0, 0, this.getWidth(), this.getHeight());
			if (finished)
			{
				G.setColor(Color.darkGray);
				G.drawString(winner+" is the winner", 5, 20);
			}
			
			////////////////////////////
			synchronized (Color.black)
			{
				Iterator<Feed> it1 = Feed.all.iterator();
				while (it1.hasNext())
				{
					Feed f = it1.next();
					f.Render(G);
				}
		
				Iterator<PowerDown> it2 = PowerDown.all.iterator();
				while (it2.hasNext())
				{
					it2.next().Render(G);
				}
		
				Iterator<PowerUp> it3 = PowerUp.all.iterator();
				while (it3.hasNext())
				{
					it3.next().Render(G);
				}
		
				Iterator<Player> it4 = Player.all.iterator();
				while (it4.hasNext())
				{
					it4.next().Render(G);
				}
		
				Iterator<Gear> it5 = Gear.all.iterator();
				while (it5.hasNext())
				{
					it5.next().Render(G);
				}

				if (Player.all.size()>1 && Circle.all.size()>0 && !finished)
				{
					synchronized(Color.black)
					{
					boolean b=true;
					for (int i=0; i<Circle.all.size(); i++)
					{
						if (Circle.all.get(i).getPlayer()!=Circle.all.get(0).getPlayer())
							b=false;
					}
					if (b&&!finished)
					{
						finished=true;
						Iterator<Player> it6 = Player.all.iterator();
						while (it6.hasNext())
						{
							it6.next().Render(G);
						}
						if (t!=null)
							t.stop();
						if (t2!=null)
							t2.stop();
						G.fillRect(0, 0, width, height);
						GraphicsEngine.started=false;
						if (mainWindow.MainClientWindow.menu != null)
							mainWindow.MainClientWindow.menu.setEnabled(false);
						
					}}
				}
			}
		}
	}
}