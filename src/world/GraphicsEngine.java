package world;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.io.Serializable;


public class GraphicsEngine extends Map implements Serializable
{
	private static final long serialVersionUID = -4549213858915766718L;
	
	public static boolean started=false;
	public PaintListener listener = new PaintListener();
	Player p1;
	public static Repainter repainter=null;
	public GraphicsEngine()
	{
		addMouseListener(listener);
		addMouseMotionListener(listener);
		setFocusable(true);
		addKeyListener(listener);
		addFocusListener(listener);
		repainter = new Repainter();
		repainter.start();
		initialize();
	}
	public class Repainter extends Thread implements Serializable
	{
		private static final long serialVersionUID = -6672249858049424756L;

		@Override
		public void run()
		{
			while (true)
			{
				if (mainWindow.ClientOptions.serverIP.length()>=1)
				{
					//System.out.println(Feed.all.size());
				}
				repaint();
			}
		}
	}
	class PaintListener implements MouseListener, MouseMotionListener, KeyListener, FocusListener, Serializable
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 8860484323399524069L;

		@Override
		public void focusGained(FocusEvent e)
		{
			// TODO Auto-generated method stub
		}

		@Override
		public void focusLost(FocusEvent e)
		{
			// TODO Auto-generated method stub
		}

		@Override
		public void keyTyped(KeyEvent e)
		{
			// TODO Auto-generated method stub
		}

		public void keyPressed(KeyEvent e)
		{
			if (!Map.playWithMouse)
			{
				if (e.getKeyCode()==KeyEvent.VK_S)
				{
					if (mainWindow.MainClientWindow.socket!=null)
					{
						try
						{
							mainWindow.MainClientWindow.oos.writeObject("s");
						}
						catch (IOException e1)
						{
							e1.printStackTrace();
						}
					}
					else
					{
						mainWindow.MainServerWindow.serverPlayer.setGoingDown();
					}
				}
				if (e.getKeyCode()==KeyEvent.VK_D)
				{
					if (mainWindow.MainClientWindow.socket!=null)
					{
						try
						{
							mainWindow.MainClientWindow.oos.writeObject("d");
						}
						catch (IOException e1)
						{
							e1.printStackTrace();
						}
					}
					else
					{
						mainWindow.MainServerWindow.serverPlayer.setGoingRight();
					}
				}
				if (e.getKeyCode()==KeyEvent.VK_A)
				{
					if (mainWindow.MainClientWindow.socket!=null)
					{
						try
						{
							mainWindow.MainClientWindow.oos.writeObject("a");
						}
						catch (IOException e1)
						{
							e1.printStackTrace();
						}
					}
					else
					{
						mainWindow.MainServerWindow.serverPlayer.setGoingLeft();
					}
				}
				if (e.getKeyCode()==KeyEvent.VK_W)
				{
					if (mainWindow.MainClientWindow.socket!=null)
					{
						try
						{
							mainWindow.MainClientWindow.oos.writeObject("w");
						}
						catch (IOException e1)
						{
							e1.printStackTrace();
						}
					}
					else
					{
						mainWindow.MainServerWindow.serverPlayer.setGoingUp();
					}
				}
			}
		}

		@Override
		public void keyReleased(KeyEvent e)
		{
			if (!Map.playWithMouse)
			{
				if (e.getKeyCode()==KeyEvent.VK_S)
				{
					if (mainWindow.MainClientWindow.socket!=null)
					{
						try
						{
							mainWindow.MainClientWindow.oos.writeObject("!s");
						}
						catch (IOException e1)
						{
							e1.printStackTrace();
						}
					}
					else
					{
						mainWindow.MainServerWindow.serverPlayer.unsetGoingDown();
					}
				}
				if (e.getKeyCode()==KeyEvent.VK_D)
				{
					if (mainWindow.MainClientWindow.socket!=null)
					{
						try
						{
							mainWindow.MainClientWindow.oos.writeObject("!d");
						}
						catch (IOException e1)
						{
							e1.printStackTrace();
						}
					}
					else
					{
						mainWindow.MainServerWindow.serverPlayer.unsetGoingRight();
					}
				}
				if (e.getKeyCode()==KeyEvent.VK_A)
				{
					if (mainWindow.MainClientWindow.socket!=null)
					{
						try
						{
							mainWindow.MainClientWindow.oos.writeObject("!a");
						}
						catch (IOException e1)
						{
							e1.printStackTrace();
						}
					}
					else
					{
						mainWindow.MainServerWindow.serverPlayer.unsetGoingLeft();
					}
				}
				if (e.getKeyCode()==KeyEvent.VK_W)
				{
					if (mainWindow.MainClientWindow.socket!=null)
					{
						try
						{
							mainWindow.MainClientWindow.oos.writeObject("!w");
						}
						catch (IOException e1)
						{
							e1.printStackTrace();
						}
					}
					else
					{
						mainWindow.MainServerWindow.serverPlayer.unsetGoingUp();
					}
				}
			}
			if (e.getKeyCode()==KeyEvent.VK_F)
			{
				if (mainWindow.MainClientWindow.socket!=null)
				{
					try
					{
						mainWindow.MainClientWindow.oos.writeObject("f");
					}
					catch (IOException e1)
					{
						e1.printStackTrace();
					}
				}
				else
				{
					mainWindow.MainServerWindow.serverPlayer.devide(3000);
				}
			}
		}
		@Override
		public void mouseDragged(MouseEvent e)
		{
			if (Map.playWithMouse)
			{
				if (mainWindow.MainClientWindow.socket!=null)
				{
					try
					{
						mainWindow.MainClientWindow.oos.writeObject(Double.toString(e.getX()));
						mainWindow.MainClientWindow.oos.writeObject(Double.toString(e.getY()));
					}
					catch (IOException e1)
					{

					}
				}
				else
				{
					mainWindow.MainServerWindow.serverPlayer.goTo(e.getX(), e.getY());
				}

			}
				
		}

		@Override
		public void mouseMoved(MouseEvent e)
		{
			// TODO Auto-generated method stub
		}

		@Override
		public void mouseClicked(MouseEvent e)
		{
			// TODO Auto-generated method stub
		}

		@Override
		public void mousePressed(MouseEvent e)
		{
			if (Map.playWithMouse)
			{
				if (mainWindow.MainClientWindow.socket!=null)
				{
					try
					{
						mainWindow.MainClientWindow.oos.writeObject(Double.toString(e.getX()));
						mainWindow.MainClientWindow.oos.writeObject(Double.toString(e.getY()));
					}
					catch (IOException e1)
					{
					}
				}
				else
				{
					mainWindow.MainServerWindow.serverPlayer.goTo(e.getX(), e.getY());
				}

			}
		}

		@Override
		public void mouseReleased(MouseEvent e)
		{
			if (Map.playWithMouse)
			{
				if (mainWindow.MainClientWindow.socket!=null)
				{
					try
					{
						mainWindow.MainClientWindow.oos.writeObject("-1");
					}
					catch (IOException e1)
					{
						e1.printStackTrace();
					}
				}
				else
				{
					mainWindow.MainServerWindow.serverPlayer.ang=-1;
				}
			}
		}

		@Override
		public void mouseEntered(MouseEvent e)
		{
			// TODO Auto-generated method stub
		}

		@Override
		public void mouseExited(MouseEvent e)
		{
			// TODO Auto-generated method stub
		}
	}

}
