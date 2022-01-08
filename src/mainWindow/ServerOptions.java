package mainWindow;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import objects.Gear;
import world.Map;
import world.Player;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JCheckBox;

public class ServerOptions extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8524391025134300365L;
	private JPanel contentPane;
	private JTextField p1name;
	private Color p1Color=Color.red;
	private ImageIcon image1=null;
	private JCheckBox music=null;

	public void CloseFrame()
	{
	    super.dispose();
	}
	ServerOptions o=null;
	public ServerOptions()
	{
		o=this;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 400, 515);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblMapSize = new JLabel("Map Size:");
		lblMapSize.setBounds(6, 68, 100, 16);
		contentPane.add(lblMapSize);
		
		JLabel lblX = new JLabel("X:");
		lblX.setBounds(131, 68, 12, 16);
		contentPane.add(lblX);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth()-12;
		double height = screenSize.getHeight()-195;
		
		JSpinner xSpinner = new JSpinner();
		xSpinner.setModel(new SpinnerNumberModel(world.Map.width, 100, width, 1));
		xSpinner.setBounds(142, 62, 66, 28);
		contentPane.add(xSpinner);
		
		JLabel lblY = new JLabel("Y:");
		lblY.setBounds(216, 68, 12, 16);
		contentPane.add(lblY);
		
		JSpinner ySpinner = new JSpinner();
		ySpinner.setModel(new SpinnerNumberModel(world.Map.height, 100, height, 1));
		ySpinner.setBounds(228, 62, 66, 28);
		contentPane.add(ySpinner);
		
		JLabel lblMaxFeed = new JLabel("Max Feed:");
		lblMaxFeed.setBounds(6, 96, 66, 16);
		contentPane.add(lblMaxFeed);
		JCheckBox checkBox = new JCheckBox("");

		JSpinner feedSpinner = new JSpinner();
		feedSpinner.setBounds(141, 90, 67, 28);
		feedSpinner.setModel(new SpinnerNumberModel(Map.maxFeed, 10, 1000, 1));
		contentPane.add(feedSpinner);
		
		JLabel lblGearCoefficent = new JLabel("Gear Coefficient:");
		lblGearCoefficent.setBounds(6, 124, 112, 16);
		contentPane.add(lblGearCoefficent);
		
		JSpinner kSpinner = new JSpinner();
		kSpinner.setBounds(142, 118, 66, 28);
		kSpinner.setModel(new SpinnerNumberModel(Gear.k, 1, 20, 1));
		contentPane.add(kSpinner);
		
		JLabel lblNewLabel = new JLabel("Speed Coefficient:");
		lblNewLabel.setBounds(6, 152, 124, 16);
		contentPane.add(lblNewLabel);
		
		JSpinner speedSpinner = new JSpinner();
		speedSpinner.setBounds(142, 146, 66, 28);
		speedSpinner.setModel(new SpinnerNumberModel(Player.C, 100, 9000, 1));
		contentPane.add(speedSpinner);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(6, 321, 288, 12);
		contentPane.add(separator);
		
		JLabel lblPlayer = new JLabel("Host Player:");
		lblPlayer.setBounds(6, 204, 85, 16);
		contentPane.add(lblPlayer);
		
		p1name = new JTextField();
		p1name.setText(Player.name1);
		p1name.setBounds(143, 259, 151, 28);
		contentPane.add(p1name);
		p1name.setColumns(10);
		
		JLabel lblName = new JLabel("Name:");
		lblName.setBounds(6, 265, 40, 16);
		contentPane.add(lblName);
		
		JLabel lblColor = new JLabel("Color:");
		lblColor.setBounds(6, 237, 40, 16);
		contentPane.add(lblColor);
		
		JLabel color1Lbl = new JLabel("");
		color1Lbl.setOpaque(true);
		color1Lbl.setBackground(p1Color);
		color1Lbl.setBounds(58, 237, 85, 16);
		contentPane.add(color1Lbl);
		
		JButton color1Btn = new JButton("Choose Color");
		color1Btn.setBounds(142, 232, 152, 29);
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
				Player.c1=p1Color;
			}
		});
		contentPane.add(color1Btn);
		JLabel lblImage = new JLabel("myImage:");
		lblImage.setBounds(6, 293, 49, 16);
		contentPane.add(lblImage);
		
		JButton img1Btn = new JButton("Choose myImage");
		img1Btn.setBounds(142, 288, 152, 29);
		contentPane.add(img1Btn);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(6, 410, 288, 12);
		contentPane.add(separator_1);
		
		JButton okBtn = new JButton("OK");
		okBtn.setIcon(mainWindow.MainServerWindow.okIcon);
		okBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				world.Map.width =((Number)xSpinner.getValue()).intValue();
				world.Map.height=((Number)ySpinner.getValue()).intValue();
				Map.maxFeed=((Number)feedSpinner.getValue()).intValue();
				Gear.k=((Number)kSpinner.getValue()).intValue();
				Player.C=((Number)speedSpinner.getValue()).intValue();
				Player.name1=p1name.getText();
				Map.play=music.isSelected();
				Map.playWithMouse=checkBox.isSelected();
				CloseFrame();
			}
		});
		
		okBtn.setBounds(6, 434, 288, 50);
		contentPane.add(okBtn);
		
		JLabel img1Lbl = new JLabel("");
		img1Lbl.setOpaque(true);
		if (Player.image1!=null)
		{
			ImageIcon icon = new ImageIcon(Player.image1.getImage().getScaledInstance(img1Lbl.getWidth(), img1Lbl.getHeight(), Image.SCALE_DEFAULT)); 
			img1Lbl.setIcon(icon);
		}
		img1Lbl.setBounds(58, 259, 85, 50);
		contentPane.add(img1Lbl);

		
		JLabel lblPlayMusic = new JLabel("Play Music:");
		lblPlayMusic.setBounds(6, 350, 76, 16);
		contentPane.add(lblPlayMusic);
		
		music = new JCheckBox("");
		music.setBounds(142, 346, 28, 23);
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
		musicBtn.setBounds(170, 345, 124, 29);
		contentPane.add(musicBtn);
		
		JButton btnLoad = new JButton("Load");
		btnLoad.setIcon(mainWindow.MainServerWindow.loadIcon);
		btnLoad.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				JFileChooser fc = new JFileChooser();
			    FileNameExtensionFilter filter1 = new FileNameExtensionFilter(
			            "Users' Data", "usersdata");
			    fc.addChoosableFileFilter(filter1);
			    fc.setApproveButtonText("Load");
			    fc.setAcceptAllFileFilterUsed(false);
				int returnVal = fc.showOpenDialog(o);
				if (returnVal==0)
				{
					File file = fc.getSelectedFile();
					if (file.getAbsolutePath().endsWith("usersdata"))
					{						
						serializer.Load.loadFile(file);
					}
					JOptionPane.showMessageDialog(MainServerWindow.mainPanel, "The Informations of Users Loaded Successfully.", "Data Imported", JOptionPane.PLAIN_MESSAGE, MainServerWindow.successIcon);
				}
				CloseFrame();
			}
		});
		btnLoad.setBounds(306, 1, 85, 483);
		contentPane.add(btnLoad);
		
		JLabel lblServerIpAddress = new JLabel("Server IP Address:");
		lblServerIpAddress.setBounds(6, 6, 112, 16);
		contentPane.add(lblServerIpAddress);
		
		JLabel ipLbl = new JLabel("New label");
		ipLbl.setBounds(142, 6, 152, 16);
		contentPane.add(ipLbl);
		ipLbl.setText(mainWindow.MainServerWindow.IP);
		
		JSeparator separator_3 = new JSeparator();
		separator_3.setBounds(6, 19, 288, 12);
		contentPane.add(separator_3);
		
		JLabel lblServerSettings = new JLabel("Server Settings:");
		lblServerSettings.setBounds(6, 34, 112, 16);
		contentPane.add(lblServerSettings);
		
		JSeparator separator_4 = new JSeparator();
		separator_4.setBounds(6, 180, 288, 12);
		contentPane.add(separator_4);
		
		checkBox.setBounds(142, 381, 128, 23);
		contentPane.add(checkBox);
		
		JLabel label = new JLabel("Play with mouse:");
		label.setBounds(6, 382, 111, 16);
		contentPane.add(label);
		img1Btn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{			
				JFileChooser fc = new JFileChooser();
				FileFilter imageFilter = new FileNameExtensionFilter(
					    "myImage files", ImageIO.getReaderFileSuffixes());
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
					Player.image1=image1;
					if (Player.image1!=null)
					{
						ImageIcon icon = new ImageIcon(Player.image1.getImage().getScaledInstance(img1Lbl.getWidth(), img1Lbl.getHeight(), Image.SCALE_DEFAULT)); 
						img1Lbl.setIcon(icon);
					}
				}
			}
		});
	}
}
