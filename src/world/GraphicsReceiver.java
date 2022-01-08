package world;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import mainWindow.ClientOptions;
import mainWindow.MainClientWindow;

public class GraphicsReceiver
{
	Socket soc=null;
	ObjectInputStream ois=null;
	ObjectOutputStream oos = null;
	public GraphicsReceiver() throws UnknownHostException, IOException
	{
		soc = new Socket(ClientOptions.serverIP, 1345);
		oos = new ObjectOutputStream(soc.getOutputStream());
		ois = new ObjectInputStream(soc.getInputStream());
		Thread receiver = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				GameInfos gi = null;
				while(true)
				{
					try
					{						
						MainClientWindow.mainPanel.setBounds(MainClientWindow.mainPanel.getX(), MainClientWindow.mainPanel.getY(),
						 Integer.parseInt((String)ois.readObject()), Integer.parseInt((String)ois.readObject()));
						Map.width=MainClientWindow.mainPanel.getWidth();
						Map.height= MainClientWindow.mainPanel.getHeight();
						gi=(GameInfos)ois.readObject();
					} catch (ClassNotFoundException | IOException e) {
					}
					if (gi!=null)
					{
						if (!gi.isFinished)
						{
							gi.act();
						}
						else
						{
							mainWindow.MainClientWindow.mainPanel.finished=true;
							Map.winner=gi.winnerUsername;
						}
					}
					else
					{

					}
				}
			}
			
		});
		receiver.start();
	}
}
