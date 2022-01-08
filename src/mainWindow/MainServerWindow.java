package mainWindow;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import world.GraphicsEngine;
import world.GraphicsSender;
import world.Map;
import world.Player;

public class MainServerWindow
{
	public static GraphicsEngine mainPanel=null;
	static JPanel contentPane;
	public static ImageIcon saveIcon = new ImageIcon("save.png");
	public static ImageIcon loadIcon = new ImageIcon("load.png");
	public static ImageIcon okIcon = new ImageIcon("ok.png");
	public static ImageIcon agarIcon = new ImageIcon("agar.png");
	public static ImageIcon startIcon = new ImageIcon("start.png");
	public static ImageIcon pauseIcon = new ImageIcon("pause.png");
	public static ImageIcon optionsIcon = new ImageIcon("options.png");
	public static ImageIcon successIcon = new ImageIcon("tick.png");
	public static ImageIcon warningIcon = new ImageIcon("warning.png");
	public static String IP = null;

	
    private final static int BUFFER_SIZE = 128000;
    private static File soundFile;
    private static AudioInputStream audioStream;
    private static AudioFormat audioFormat;
    private static SourceDataLine sourceLine;
    static boolean saveMode=false;
    public static JButton start;
    public static JButton menu;
    public static Player serverPlayer=null;
    
	public void run ()
	{
		try
		{
			new ServerListener();
			new GraphicsSender();
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
		
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
		
		start = new JButton("Start");
		start.setIcon(startIcon);
		subPanel.add(start);
		menu = new JButton("Menu");

		start.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{	
				mainPanel = new GraphicsEngine();
				world.Map.isStarted=true;
				mainPanel.setBounds(6, 17, world.Map.width, world.Map.height);
				contentPane.add(mainPanel);
				
				GraphicsEngine.started=true;
				start.setEnabled(false);
				serverPlayer=new Player(Player.name1, "Server", Map.width*Math.random(), Map.height*Math.random(), Player.c1, Player.image1);

				Thread sound = new Thread (new Runnable(){
					@Override
					public void run()
					{
						while (true)
						{
							playSound(world.Map.audio);
						}
					}
				});
				if (world.Map.play)
					sound.start();
				menu.setText("Save");
				menu.setIcon(saveIcon);
				saveMode=true;
				mainPanel.requestFocus();
			}
		});
		
		menu.setIcon(optionsIcon);
		subPanel.add(menu);
		
		menu.addActionListener(new ActionListener()
		{
			@SuppressWarnings("deprecation")
			@Override
			public void actionPerformed(ActionEvent e)
			{	
				if (!saveMode)
				{
					ServerOptions o = new ServerOptions();
					o.show();
				}
				else
				{
					JFileChooser fc = new JFileChooser();
					FileNameExtensionFilter filter = new FileNameExtensionFilter("Users' Data", "usersdata");
					fc.setFileFilter(filter);
					fc.setAcceptAllFileFilterUsed(false);
					int returnVal = fc.showSaveDialog(MainServerWindow.mainPanel);
					if (returnVal==0)
					{
						File file = fc.getSelectedFile();
						String file_name = file.toString();
						if (!file_name.endsWith(".usersdata"))
							file_name += ".usersdata";
						serializer.Save.saveTo(new File(file_name));
						JOptionPane.showMessageDialog(MainServerWindow.mainPanel, "The Informations of Users Saved Successfully.", "Data Exported", JOptionPane.PLAIN_MESSAGE, successIcon);
					}
					mainPanel.requestFocus();					
				}
			}
		});
		
		mainWindow.setVisible(true);
	}

	  public static void playSound(String filename)
	  {

	       String strFilename = filename;

	       try {
	           soundFile = new File(strFilename);
	        } catch (Exception e) {
	            e.printStackTrace();
	            System.exit(1);
	        }

	        try {
	            audioStream = AudioSystem.getAudioInputStream(soundFile);
	        } catch (Exception e){
	            e.printStackTrace();
	            System.exit(1);
	        }

	        audioFormat = audioStream.getFormat();

	        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
	        try {
	            sourceLine = (SourceDataLine) AudioSystem.getLine(info);
	            sourceLine.open(audioFormat);
	        } catch (LineUnavailableException e) {
	            e.printStackTrace();
	            System.exit(1);
	        } catch (Exception e) {
	            e.printStackTrace();
	            System.exit(1);
	        }

	        sourceLine.start();

	        int nBytesRead = 0;
	        byte[] abData = new byte[BUFFER_SIZE];
	        while (nBytesRead != -1) {
	            try {
	                nBytesRead = audioStream.read(abData, 0, abData.length);
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	            if (nBytesRead >= 0) {
	                @SuppressWarnings("unused")
	                int nBytesWritten = sourceLine.write(abData, 0, nBytesRead);
	            }
	        }

	        sourceLine.drain();
	        sourceLine.close();
	 }
}
