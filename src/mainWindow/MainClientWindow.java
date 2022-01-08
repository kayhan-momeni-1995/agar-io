package mainWindow;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;

import world.GraphicsEngine;
import world.GraphicsReceiver;

import java.net.Socket;

public class MainClientWindow
{
	public static GraphicsEngine mainPanel=null;
	static JPanel contentPane;
	public static ImageIcon saveIcon = new ImageIcon("save.png");
	public static ImageIcon loadIcon = new ImageIcon("load.png");
	public static ImageIcon okIcon = new ImageIcon("ok.png");
	public static ImageIcon agarIcon = new ImageIcon("agar.png");
	public static ImageIcon startIcon = new ImageIcon("start.png");
	public static ImageIcon pauseIcon = new ImageIcon("pause.png");
	public static ImageIcon ClientOptionsIcon = new ImageIcon("Options.png");
	public static ImageIcon successIcon = new ImageIcon("tick.png");
	public static ImageIcon warningIcon = new ImageIcon("warning.png");
	public static ImageIcon searchIcon = new ImageIcon("search.png");
	public static ImageIcon signupIcon = new ImageIcon("signup.png");
	public static ImageIcon loginIcon = new ImageIcon("login.png");
	public static Socket socket=null;
	public static ObjectInputStream ois=null;
	public static ObjectOutputStream oos=null;



	
	public static Socket mainSocket = null;
    static boolean saveMode=false;
    public static JButton menu;



    
	/**
	 * @wbp.parser.entryPoint
	 */
	public void run ()
	{
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight()-104;
	
		JFrame mainWindow = new JFrame("Agario");
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.setResizable(false);
		mainWindow.setBounds(0, 0, (int)width, (int)height);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		mainWindow.setContentPane(contentPane);
		mainWindow.setIconImage(agarIcon.getImage());
		contentPane.setLayout(null);
		
		JSeparator separator1 = new JSeparator();
		separator1.setBounds(6, (int)height-76, (int)width-12, 12);
		contentPane.add(separator1);
		
		JSeparator separator2 = new JSeparator();
		separator2.setBounds(6, 6, (int)width-12, 12);
		contentPane.add(separator2);
		
		JPanel subPanel = new JPanel();
		subPanel.setBounds(6, (int)height-70, (int)width-12, 45);
		contentPane.add(subPanel);
		subPanel.setLayout(new GridLayout(1, 0, 0, 0));

		menu = new JButton("Menu");

		menu.setIcon(ClientOptionsIcon);
		subPanel.add(menu);
		
		menu.addActionListener(new ActionListener()
		{
			@SuppressWarnings("deprecation")
			@Override
			public void actionPerformed(ActionEvent e)
			{	
				if (!menu.getText().equals("Run if server started"))
				{
					ClientOptions o = new ClientOptions();
					o.show();
				}
				else
				{
					menu.setEnabled(false);
					mainPanel = new GraphicsEngine();
					world.Map.isStarted=true;
					mainPanel.setBounds(6, 17, world.Map.width, world.Map.height);
					contentPane.add(mainPanel);
					
					GraphicsEngine.started=true;
					
					try
					{
						new GraphicsReceiver();
					}
					catch (IOException e1) {
						e1.printStackTrace();
					}
					mainPanel.requestFocus();
				}
			}
		});
		
		mainWindow.setVisible(true);
	}
}
