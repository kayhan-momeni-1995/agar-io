package world;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import mainWindow.MainServerWindow;

public class GraphicsSender
{
	Thread connectorThread = null;
	ArrayList<Thread> ActionThreads = null;
	
	public GraphicsSender() throws IOException
	{
		ActionThreads= new ArrayList<Thread>();
    	connectorThread = new Thread(new Runnable()
    	{
			ServerSocket connectorSocket = new ServerSocket(1345);

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
								catch (ClassNotFoundException | IOException e)
								{
									e.printStackTrace();
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
		Thread sender = new Thread (new Runnable(){
			@Override
			public void run()
			{
				while (true)
				{
					synchronized(Color.black)
					{
						GameInfos gi=null;
						while (!Map.isStarted)
						{
							try {
								TimeUnit.MILLISECONDS.sleep(50);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						gi=new GameInfos();
						try
						{
							oos.writeObject(Integer.toString(MainServerWindow.mainPanel.getWidth()));
							oos.writeObject(Integer.toString(MainServerWindow.mainPanel.getHeight()));
							oos.writeObject(gi);
							oos.reset();
						} catch (Exception e)
						{
							try {
								soc.close();
								
								break;
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						e.printStackTrace();
						}
					}
					try {
						TimeUnit.MILLISECONDS.sleep(30);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			
		});
		sender.start();
	}

}
