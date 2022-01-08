package mainWindow;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import world.Map;
import world.Player;
import world.PlayerData;

import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JCheckBox;
import javax.swing.ListSelectionModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JPasswordField;

public class ClientOptions extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8524391025134300365L;
	private JPanel contentPane;
	private JTextField p1name;
	private Color p1Color=Color.red;
	private ImageIcon image1=null;
	private JCheckBox music=null;
	public static String serverIP="";

	public void CloseFrame()
	{
	    super.dispose();
	}
    private final static int BUFFER_SIZE = 128000;
    private static File soundFile;
    private static AudioInputStream audioStream;
    private static AudioFormat audioFormat;
    private static SourceDataLine sourceLine;
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

	
	ClientOptions o=null;
	private JTextField fromIp;
	private JTextField toIp;
	private JPasswordField passwordField;
	private JTextField textField;
	private JPasswordField passwordField_1;
	
	private boolean serverListening(String host, int port)
	{
	    Socket s = null;
	    try
	    {
	        s = new Socket();
	        s.connect(new InetSocketAddress(host, port), 1);
	        return true;
	    }
	    catch (Exception e)
	    {
	        return false;
	    }
	    finally
	    {
	        if(s != null)
	            try {s.close();}
	            catch(Exception e){}
	    }
	}
	private String corrector(String input)
	{
		int a=input.length();
		String output="";
		for (int i=0; i<Math.min(a, 10); i++)
		{
			if (input.charAt(i)!=' ' && input.charAt(i)!='-' && input.charAt(i)!='_'&& input.charAt(i)!='.'&&input.charAt(i)!=',')
				output+=input.charAt(i);
			else
				break;
		}
		return output;
		
	}
	boolean firstSearch=true;
	private JTextField textField_1;
	public ClientOptions()
	{
		o=this;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(world.Map.width/2 - 210, 5, 420, 765);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel img1Lbl = new JLabel("");
		img1Lbl.setOpaque(true);
		img1Lbl.setBounds(61, 371, 60, 60);
		contentPane.add(img1Lbl);
		
		p1name = new JTextField();
		String hostname = "Guest";
		try
		{
		    InetAddress addr;
		    addr = InetAddress.getLocalHost();
		    hostname = addr.getHostName();
		}
		catch (UnknownHostException ex)
		{
		}
		p1name.setText(corrector(hostname));
		p1name.setBounds(82, 309, 109, 28);
		contentPane.add(p1name);
		p1name.setColumns(10);
		
		JCheckBox checkBox = new JCheckBox("");

		JLabel lblName = new JLabel("Name:");
		lblName.setBounds(16, 315, 49, 16);
		contentPane.add(lblName);
		
		JLabel lblColor = new JLabel("Color:");
		lblColor.setBounds(16, 343, 40, 16);
		contentPane.add(lblColor);
		
		DefaultListModel<String> Status_ListModel2=new DefaultListModel<>();
		JList<String> Status_list2 = new JList<String>(Status_ListModel2);
		Status_list2.setFixedCellWidth(400);
		

		JLabel color1Lbl = new JLabel("");
		color1Lbl.setOpaque(true);
		color1Lbl.setBackground(p1Color);
		color1Lbl.setBounds(61, 343, 21, 16);
		contentPane.add(color1Lbl);
		
		JButton color1Btn = new JButton("Choose");
		color1Btn.setBounds(82, 338, 109, 29);
		color1Btn.addActionListener(new ActionListener()
		{
			Color tmp = Color.red;
			public void actionPerformed(ActionEvent e)
			{
				tmp = JColorChooser.showDialog(getParent(), "Choose a color", p1Color);
				if (tmp == null)
					tmp=new Color (p1Color.getRGB());
				else
					p1Color=new Color(tmp.getRGB());
				color1Lbl.setBackground(p1Color);
			}
		});
		contentPane.add(color1Btn);
		JLabel lblImage = new JLabel("Image:");
		lblImage.setBounds(16, 392, 49, 16);
		contentPane.add(lblImage);

		JButton btnNewButton = new JButton("Search");
		
		JButton img1Btn = new JButton("Choose");
		img1Btn.setBounds(125, 371, 66, 60);
		contentPane.add(img1Btn);
		
		JButton okBtn = new JButton("OK");
		okBtn.setEnabled(false);
		okBtn.setIcon(mainWindow.MainClientWindow.okIcon);
		okBtn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Player.name1=p1name.getText();
				Map.play=music.isSelected();
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
				if (music.isSelected())
				{
					sound.start();
				}
				mainWindow.MainClientWindow.menu.setText("Run if server started");
				mainWindow.MainClientWindow.menu.setIcon(mainWindow.MainClientWindow.startIcon);
				mainWindow.MainClientWindow.menu.setEnabled(true);
				world.Map.playWithMouse=checkBox.isSelected();
				CloseFrame();
			}
		});
		
		okBtn.setBounds(6, 684, 408, 50);
		contentPane.add(okBtn);
		
		JLabel lblPlayMusic = new JLabel("Play Music:");
		lblPlayMusic.setBounds(6, 628, 76, 16);
		contentPane.add(lblPlayMusic);
		
		music = new JCheckBox("");
		music.setBounds(189, 621, 28, 23);
		contentPane.add(music);
		
		JButton musicBtn = new JButton("Choose Audio");
		musicBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				JFileChooser fc = new JFileChooser();
				FileFilter audioFilter = new FileNameExtensionFilter(
					    "WAV files", "wav");
			    fc.addChoosableFileFilter(audioFilter);
			    fc.setApproveButtonText("Choose");
			    fc.setAcceptAllFileFilterUsed(false);
				int returnVal = fc.showOpenDialog(o);
				if (returnVal==0)
				{
					
					File file = fc.getSelectedFile();
					world.Map.audio=file.getAbsolutePath();
				}
			}
		});
		musicBtn.setBounds(240, 621, 164, 29);
		contentPane.add(musicBtn);
		
		JLabel lblIpRange = new JLabel("IP Range:");
		lblIpRange.setBounds(6, 6, 61, 16);
		contentPane.add(lblIpRange);
		
		JLabel lblFrom = new JLabel("From:");
		lblFrom.setBounds(6, 34, 40, 16);
		contentPane.add(lblFrom);
		
		fromIp = new JTextField();
		fromIp.setText("192.168.1.1");
		fromIp.setBounds(58, 28, 134, 28);
		contentPane.add(fromIp);
		fromIp.setColumns(10);
		
		JLabel lblTo = new JLabel("To:");
		lblTo.setBounds(6, 62, 20, 16);
		contentPane.add(lblTo);
		
		toIp = new JTextField();
		toIp.setText("192.168.1.100");
		toIp.setBounds(58, 56, 134, 28);
		contentPane.add(toIp);
		toIp.setColumns(10);
		
		
		JPanel panel = new JPanel();
		panel.setBounds(233, 6, 171, 170);
		contentPane.add(panel);
		
		DefaultListModel<String> Status_ListModel=new DefaultListModel<>();
		JLabel lblAvailableServers = new JLabel("Available Servers:");
		panel.add(lblAvailableServers);
		JList<String> Status_list = new JList<String>(Status_ListModel);
		Status_list.setFixedCellWidth(160);
		Status_list.setEnabled(false);
		Status_ListModel.addElement("Search for the servers");
		
		JRadioButton rdbtnLogIn = new JRadioButton("Log in");
		JRadioButton rdbtnRegister = new JRadioButton("Sign up");
		
		JCheckBox chckbxLogInAs = new JCheckBox("Log in as a guest");
		chckbxLogInAs.setEnabled(false);
		chckbxLogInAs.setBounds(240, 303, 164, 23);
		
		contentPane.add(chckbxLogInAs);
		JButton btnNewButton_1 = new JButton("Sign up");
		btnNewButton_1.setIcon(MainClientWindow.signupIcon);
		btnNewButton_1.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e)
			{
				if (!btnNewButton_1.getText().equals("Confirm"))
				{
					String a = "";
					if (Status_list.getSelectedValue()!=null &&
					   (
					   (rdbtnRegister.isSelected() && textField_1.getText().length()>=1 && passwordField.getText().length()>=1 && p1name.getText().length()>=1)
				   	   ||(rdbtnLogIn.isSelected() && chckbxLogInAs.isSelected())
					   ||(rdbtnLogIn.isSelected() && textField.getText().length()>=1 && passwordField_1.getText().length()>=1)
					   ))
					{
						if (rdbtnRegister.isSelected())
						{
							PlayerData pd = new PlayerData(textField_1.getText(), String.valueOf(passwordField.getPassword()), p1name.getText(),
													   p1Color, image1);
					
							try
							{
								serverIP=Status_list.getSelectedValue();
								mainWindow.MainClientWindow.socket = new Socket(serverIP, 1342);
							}
							catch (IOException e1)
							{
								e1.printStackTrace();
							}

							try
							{
								mainWindow.MainClientWindow.oos = new ObjectOutputStream(mainWindow.MainClientWindow.socket.getOutputStream());
							}
							catch (IOException e1)
							{
								e1.printStackTrace();
							}

							try
							{
								mainWindow.MainClientWindow.oos.writeObject("1");
								mainWindow.MainClientWindow.oos.writeObject(pd);
							}
							catch (IOException e1)
							{
								e1.printStackTrace();
							}
							try
							{
								mainWindow.MainClientWindow.ois = new ObjectInputStream(mainWindow.MainClientWindow.socket.getInputStream());
							}
							catch (IOException e1)
							{
								e1.printStackTrace();
							}
						
							try
							{
								a+=(String)mainWindow.MainClientWindow.ois.readObject();
							}
							catch (ClassNotFoundException | IOException e1)
							{
								e1.printStackTrace();
							}
							Status_ListModel2.add(0, a);
						}
						else
						{
							if (!chckbxLogInAs.isSelected())
							{
								try
								{
									mainWindow.MainClientWindow.socket = new Socket(Status_list.getSelectedValue(), 1342);
								}
								catch (IOException e1)
								{
									e1.printStackTrace();
								}
								try
								{
									mainWindow.MainClientWindow.oos = new ObjectOutputStream(mainWindow.MainClientWindow.socket.getOutputStream());
								}
								catch (IOException e1)
								{
									e1.printStackTrace();
								}
								try
								{
									mainWindow.MainClientWindow.oos.writeObject("2");

									mainWindow.MainClientWindow.oos.writeObject(textField.getText());

									mainWindow.MainClientWindow.oos.writeObject(String.valueOf(passwordField_1.getPassword()));
								}
								catch (IOException e1)
								{
									e1.printStackTrace();
								}

								try
								{
									mainWindow.MainClientWindow.ois = new ObjectInputStream(mainWindow.MainClientWindow.socket.getInputStream());
								}
								catch (IOException e1)
								{
									e1.printStackTrace();
								}

								try
								{
									a+=(String)mainWindow.MainClientWindow.ois.readObject();
								}
								catch (ClassNotFoundException | IOException e1)
								{
									e1.printStackTrace();
								}
								Status_ListModel2.add(0, a);
							
								if (a.equals("Connected to the server successfully"))
								{
									String b="";
									try {
										b=(String)mainWindow.MainClientWindow.ois.readObject();
									} catch (ClassNotFoundException | IOException e1) {
										e1.printStackTrace();
									}
									Status_ListModel2.add(0, b);
									rdbtnRegister.setText("Edit");
									rdbtnLogIn.setEnabled(false);
									rdbtnRegister.setSelected(true);
									btnNewButton_1.setText("Confirm");
									PlayerData pd=null;
									try
									{
										pd = (PlayerData)mainWindow.MainClientWindow.ois.readObject();
									}
									catch (ClassNotFoundException | IOException e1)
									{
										e1.printStackTrace();
									}
									textField_1.setText(pd.getUsername());
									textField_1.setEnabled(false);
									passwordField.setText(pd.getPassword());
									p1name.setText(pd.getName());
									color1Lbl.setBackground(pd.getColor());
									p1Color=pd.getColor();
									image1=pd.getImage();
									ImageIcon icon=null;
									if (image1!=null)
										icon = new ImageIcon(image1.getImage().getScaledInstance(img1Lbl.getWidth(), img1Lbl.getHeight(), Image.SCALE_DEFAULT)); 
									img1Lbl.setIcon(icon);
									textField.setEnabled(false);
									passwordField_1.setEnabled(false);
									chckbxLogInAs.setEnabled(false);
									passwordField.setEnabled(true);
									p1name.setEnabled(true);
									color1Btn.setEnabled(true);
									color1Lbl.setVisible(true);
									img1Btn.setEnabled(true);
									img1Lbl.setVisible(true);
								}
							
							}
							else
							{
								try
								{
									mainWindow.MainClientWindow.socket = new Socket(Status_list.getSelectedValue(), 1342);
								}
								catch (IOException e1)
								{
									e1.printStackTrace();
								}
								try
								{
									mainWindow.MainClientWindow.oos = new ObjectOutputStream(mainWindow.MainClientWindow.socket.getOutputStream());
								}
								catch (IOException e1)
								{
									e1.printStackTrace();
								}
								try
								{
									mainWindow.MainClientWindow.oos.writeObject("3");
								}
								catch (IOException e1)
								{
									e1.printStackTrace();
								}
								try
								{
									mainWindow.MainClientWindow.ois = new ObjectInputStream(mainWindow.MainClientWindow.socket.getInputStream());
								}
								catch (IOException e1)
								{
									e1.printStackTrace();
								}
								try
								{
									a+=(String)mainWindow.MainClientWindow.ois.readObject();	
								}
								catch (ClassNotFoundException | IOException e1)
								{
									e1.printStackTrace();
								}
								Status_ListModel2.add(0, a);
							}
						}
					}
					else
					{
						JOptionPane.showMessageDialog(mainWindow.MainClientWindow.mainPanel, "Complete required informations", "Not Completed", JOptionPane.PLAIN_MESSAGE, mainWindow.MainClientWindow.warningIcon);
					}
					if (a.equals("Connected to the server successfully") && !btnNewButton_1.getText().equals("Confirm"))
					{
						mainWindow.MainClientWindow.menu.setEnabled(false);
						fromIp.setEnabled(false);
						toIp.setEnabled(false);
						btnNewButton.setEnabled(false);
						Status_list.setEnabled(false);
						rdbtnRegister.setEnabled(false);
						rdbtnLogIn.setEnabled(false);
						textField_1.setEnabled(false);
						passwordField.setEnabled(false);
						p1name.setEnabled(false);
						color1Btn.setEnabled(false);
						img1Btn.setEnabled(false);
						textField.setEnabled(false);
						passwordField_1.setEnabled(false);
						chckbxLogInAs.setEnabled(false);
						btnNewButton_1.setEnabled(false);
						okBtn.setEnabled(true);
					}
				}
				else
				{
					PlayerData pd = new PlayerData(textField_1.getText(), String.valueOf(passwordField.getPassword()), p1name.getText(),
							   p1Color, image1);

					try {
						mainWindow.MainClientWindow.oos.writeObject(pd);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					String a="";
					try {
						a = (String)mainWindow.MainClientWindow.ois.readObject();
					} catch (ClassNotFoundException | IOException e1) {
						e1.printStackTrace();
					}
					Status_ListModel2.add(0, a);
					btnNewButton_1.setEnabled(false);
					okBtn.setEnabled(true);
					passwordField.setEnabled(false);
					p1name.setEnabled(false);
					color1Btn.setEnabled(false);
					img1Btn.setEnabled(false);
				}
			}
		});
		btnNewButton_1.setBounds(240, 338, 164, 93);
		contentPane.add(btnNewButton_1);
		
		Status_list.setBounds(6, 5, 188, 128);
		Status_list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		Status_list.getSelectedIndex();
		panel.add(new JScrollPane(Status_list), BorderLayout.EAST);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(6, 443, 408, 142);
		contentPane.add(panel_1);
		
		panel_1.add(new JScrollPane(Status_list2), BorderLayout.EAST);
		btnNewButton.setIcon(MainClientWindow.searchIcon);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				String from = fromIp.getText();
				int from1=0, from2=0, from3=0, from4=0;				
				from1=Integer.parseInt(from.substring(0, from.indexOf(".")));
				from=from.substring(from.indexOf(".")+1);
				from2=Integer.parseInt(from.substring(0, from.indexOf(".")));
				from=from.substring(from.indexOf(".")+1);
				from3=Integer.parseInt(from.substring(0, from.indexOf(".")));
				from=from.substring(from.indexOf(".")+1);
				from4=Integer.parseInt(from);
				
				String to = toIp.getText();
				int to1=0, to2=0, to3=0, to4=0;				
				to1=Integer.parseInt(to.substring(0, to.indexOf(".")));
				to=to.substring(to.indexOf(".")+1);
				to2=Integer.parseInt(to.substring(0, to.indexOf(".")));
				to=to.substring(to.indexOf(".")+1);
				to3=Integer.parseInt(to.substring(0, to.indexOf(".")));
				to=to.substring(to.indexOf(".")+1);
				to4=Integer.parseInt(to);
				String IP=null;
				Status_ListModel.clear();
				for (int a=from1; a<=to1; a++)
				{
					for (int b=from2; b<=to2; b++)
					{
						for (int c=from3; c<=to3; c++)
						{
							for(int d=from4; d<=to4; d++)
							{
								IP=a+"."+b+"."+c+"."+d;								
								if (serverListening(IP, 1341))
								{
									Status_ListModel.addElement(IP);
								}
							    
							}
						}
					}
				}
				
				if (!Status_ListModel.isEmpty())
				{
					Status_list.setEnabled(true);
					Status_list.setSelectedIndex(0);
				}
				else
				{
					Status_list.setEnabled(false);
					Status_ListModel.addElement("No Server Available");
				}
				Status_ListModel2.add(0, "Search Completed.");
			}
		});
		btnNewButton.setBounds(6, 90, 185, 86);
		contentPane.add(btnNewButton);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(6, 188, 408, 12);
		contentPane.add(separator_1);
		
		rdbtnRegister.setSelected(true);
		rdbtnRegister.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if (rdbtnRegister.isSelected())
				{
					if (rdbtnRegister.isSelected())
					{
						rdbtnLogIn.setSelected(false);
					}
					textField.setEnabled(false);
					passwordField_1.setEnabled(false);
					p1name.setEnabled(true);
					color1Btn.setEnabled(true);
					img1Btn.setEnabled(true);
					passwordField.setEnabled(true);
					btnNewButton_1.setText("Sign up");
					btnNewButton_1.setIcon(mainWindow.MainClientWindow.signupIcon);
					color1Lbl.setVisible(true);
					img1Lbl.setVisible(true);
					textField_1.setEnabled(true);
					chckbxLogInAs.setEnabled(false);
					chckbxLogInAs.setSelected(false);

				}
				else
				{
					rdbtnRegister.setSelected(true);
				}
			}
		});
		rdbtnLogIn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if (rdbtnLogIn.isSelected())
				{
					if (rdbtnLogIn.isSelected())
					{
						rdbtnRegister.setSelected(false);
					}
					textField.setEnabled(true);
					passwordField_1.setEnabled(true);
					p1name.setEnabled(false);
					color1Btn.setEnabled(false);
					img1Btn.setEnabled(false);
					passwordField.setEnabled(false);
					btnNewButton_1.setText("Log in");
					btnNewButton_1.setIcon(mainWindow.MainClientWindow.loginIcon);

					img1Lbl.setVisible(false);
					color1Lbl.setVisible(false);
					textField_1.setEnabled(false);
					chckbxLogInAs.setEnabled(true);
				}
				else
				{
					rdbtnLogIn.setSelected(true);
				}

			}
		});
		rdbtnRegister.setBounds(6, 212, 85, 23);
		contentPane.add(rdbtnRegister);
		
		rdbtnLogIn.setBounds(240, 212, 76, 23);
		contentPane.add(rdbtnLogIn);
		
		chckbxLogInAs.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (chckbxLogInAs.isSelected())
				{
					textField.setEnabled(false);
					passwordField_1.setEnabled(false);
				}
				else
				{
					textField.setEnabled(true);
					passwordField_1.setEnabled(true);
				}
			}
			
		});
		JLabel lblNewLabel_1 = new JLabel("Password:");
		lblNewLabel_1.setBounds(16, 275, 66, 16);
		contentPane.add(lblNewLabel_1);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(83, 269, 109, 28);
		contentPane.add(passwordField);
		
		JLabel lblName_1 = new JLabel("Username:");
		lblName_1.setBounds(240, 247, 76, 16);
		contentPane.add(lblName_1);
		
		textField = new JTextField();
		textField.setEnabled(false);
		textField.setColumns(10);
		textField.setBounds(311, 241, 93, 28);
		contentPane.add(textField);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(240, 275, 66, 16);
		contentPane.add(lblPassword);
		
		passwordField_1 = new JPasswordField();
		passwordField_1.setEnabled(false);
		passwordField_1.setBounds(311, 269, 93, 28);
		contentPane.add(passwordField_1);
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setBounds(6, 597, 408, 12);
		contentPane.add(separator_2);
		
		JLabel lblNewLabel = new JLabel("Play with mouse:");
		lblNewLabel.setBounds(6, 656, 111, 16);
		contentPane.add(lblNewLabel);
		
		checkBox.setBounds(189, 652, 128, 23);
		contentPane.add(checkBox);
		
		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setBounds(16, 247, 66, 16);
		contentPane.add(lblUsername);
		
		textField_1 = new JTextField();
		textField_1.setText(corrector(hostname));
		textField_1.setColumns(10);
		textField_1.setBounds(83, 241, 109, 28);
		contentPane.add(textField_1);
		
		img1Btn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{			
				JFileChooser fc = new JFileChooser();
				FileFilter imageFilter = new FileNameExtensionFilter(
					    "ImageIcon files", ImageIO.getReaderFileSuffixes());
			    fc.addChoosableFileFilter(imageFilter);
			    fc.setApproveButtonText("Open");
			    fc.setAcceptAllFileFilterUsed(false);
				int returnVal = fc.showOpenDialog(o);
				if (returnVal==0)
				{
					
					File file = fc.getSelectedFile();
					image1=null;
					Image tmp=null;
					try
					{
					    File pathToFile = file;
					    tmp = ImageIO.read(pathToFile);
					    image1 = new ImageIcon(tmp);
					} catch (IOException ex) {
					    ex.printStackTrace();
					}
					if (image1!=null)
					{
						ImageIcon icon = new ImageIcon(image1.getImage().getScaledInstance(img1Lbl.getWidth(), img1Lbl.getHeight(), Image.SCALE_DEFAULT)); 
						img1Lbl.setIcon(icon);
					}
				}
			}
		});
	}
}
