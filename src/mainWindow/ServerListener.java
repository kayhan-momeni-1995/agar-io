package mainWindow;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import world.Player;
import world.PlayerData;

public class ServerListener
{
	Thread connectorThread = null;
	ArrayList<Thread> ActionThreads = null;
	
	public ServerListener() throws IOException
	{
		ActionThreads= new ArrayList<Thread>();
    	Thread listener = new Thread (new Runnable(){
			@SuppressWarnings("resource")
			@Override
			public void run()
			{
				ServerSocket ss = null;
				try {
					ss = new ServerSocket(1341);
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					ss.getInetAddress();
					mainWindow.MainServerWindow.IP=InetAddress.getLocalHost().getHostAddress();
				} catch (UnknownHostException e1)
				{
					e1.printStackTrace();
				}
				while (true)
				{
					try {
						ss.accept();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
    		
    	});
    	listener.start();
    	connectorThread = new Thread(new Runnable()
    	{
			ServerSocket connectorSocket = new ServerSocket(1342);
			@Override
			public void run()
			{
				while (true)
				{
					try
					{
						final Socket soc = connectorSocket.accept();
						final Thread actionThread = new Thread (new Runnable()
						{
							ObjectInputStream ois = null;
							ObjectOutputStream oos = null;

							@Override
							public void run()
							{
								try
								{
									ois = new ObjectInputStream(soc.getInputStream());
									oos = new ObjectOutputStream(soc.getOutputStream());
								}
								catch (IOException e)
								{
									e.printStackTrace();
								}
								try
								{
									runProtocol(ois, oos, soc);
								}
								catch (Exception e)
								{
									try {
										soc.close();
									} catch (IOException e1) {
										e1.printStackTrace();
									}
								}
							}
						});
						actionThread.start();
						ActionThreads.add(actionThread);
						
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
			}
			
    	});
    	connectorThread.start();
	}
	private void runProtocol (ObjectInputStream ois, ObjectOutputStream oos, Socket soc) throws ClassNotFoundException, IOException
	{
		boolean connected=false;
		String command="";
		command = (String)ois.readObject();
		Player player = null;
		if (command.equals("1"))
		{
			PlayerData pd = (PlayerData)ois.readObject();
			int index = PlayerData.indexOf(pd.getUsername());
			if (index==-1)
			{
				synchronized(Color.GREEN)
				{
					PlayerData.all.add(pd);
				}
				player = new Player (pd.getName(), pd.getUsername(), Math.random()*world.Map.width, Math.random()*world.Map.height, pd.getColor(), pd.getImage());
				connected=true;
				oos.writeObject("Connected to the server successfully");
			}
			else
			{
				oos.writeObject("The username <"+pd.getUsername()+"> has already been taken");
			}
		}
		else if (command.equals("2"))
		{
			String username  = (String)ois.readObject();
			String password  = (String)ois.readObject();
			int indexInData  = PlayerData.indexOf(username);
			int indexInPlayer= Player.indexOf(username);
			if (indexInPlayer>-1)
				oos.writeObject("The username is already logged in");
			else
			{
				if (indexInData!=-1 && PlayerData.all.get(indexInData).getPassword().equals(password))
				{
					PlayerData pd = PlayerData.all.get(indexInData);
					connected=true;
					oos.writeObject("Connected to the server successfully");
					oos.writeObject("You can now edit your information");
					oos.writeObject(pd);
					PlayerData tmp = (PlayerData)ois.readObject();
					PlayerData.all.get(indexInData).setPassword(tmp.getPassword());
					PlayerData.all.get(indexInData).setName(tmp.getName());
					PlayerData.all.get(indexInData).setColor(tmp.getColor());
					PlayerData.all.get(indexInData).setImage(tmp.getImage());
					oos.writeObject("Your information updated successfully");
					player = new Player (pd.getName(), pd.getUsername(), Math.random()*world.Map.width, Math.random()*world.Map.height, pd.getColor(), pd.getImage());
				}
				else
				{
					oos.writeObject("Username or Password is incorrect");
				}
			}
		}
		else if (command.equals("3"))
		{
			int red   = (int)(Math.random()*255);
			int green = (int)(Math.random()*255);
			int blue  = (int)(Math.random()*255);
			
			player = new Player("Guest", "", Math.random()*world.Map.width, Math.random()*world.Map.height, new Color(red, green, blue), null);
			connected=true;
			oos.writeObject("Connected to the server successfully");
		}

		if (connected)
		{
			String order="";
			while(true)
			{
				order=(String)ois.readObject();
				if (world.Map.isStarted)
				{
					if (order.equals("w"))
					{
						player.setGoingUp();
					}
					else if (order.equals("!w"))
					{
						player.unsetGoingUp();
					}
					else if (order.equals("a"))
					{
						player.setGoingLeft();
					}
					else if (order.equals("!a"))
					{
						player.unsetGoingLeft();
					}
					else if (order.equals("s"))
					{
						player.setGoingDown();
					}
					else if (order.equals("!s"))
					{
						player.unsetGoingDown();
					}
					else if (order.equals("d"))
					{
						player.setGoingRight();
					}
					else if (order.equals("!d"))
					{
						player.unsetGoingRight();
					}
					else if (order.equals("f"))
					{
						player.devide(3000);
					}
					else
					{
						if (order.equals("-1"))
							player.ang=-1;
						else
						{
							double x = Double.parseDouble(order);
							double y = Double.parseDouble((String)ois.readObject());
							player.goTo(x, y);
						}
					}
				}
			}
		}
	}

}
