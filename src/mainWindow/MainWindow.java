package mainWindow;

import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class MainWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4051531026803667443L;
	private JPanel contentPane;

	public void CloseFrame()
	{
	    super.dispose();
	}
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow frame = new MainWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainWindow() {
		setTitle("Welcome");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 213, 356);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblWelcome = new JLabel("How would you prefer to play?");
		lblWelcome.setBounds(6, 6, 438, 16);
		contentPane.add(lblWelcome);
		
		JRadioButton rdbtnPlayTheGame = new JRadioButton("Play the game as a server");
		JRadioButton rdbtnPlayTheGame_1 = new JRadioButton("Play the game as a client");

		rdbtnPlayTheGame.setBounds(6, 69, 190, 23);
		rdbtnPlayTheGame.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (rdbtnPlayTheGame.isSelected())
				{
					rdbtnPlayTheGame_1.setSelected(false);
				}
				else
				{
					rdbtnPlayTheGame.setSelected(true);
					rdbtnPlayTheGame_1.setSelected(false);
				}
			}
			
		});
		contentPane.add(rdbtnPlayTheGame);
		
		rdbtnPlayTheGame_1.setSelected(true);
		rdbtnPlayTheGame_1.setBounds(6, 34, 190, 23);
		rdbtnPlayTheGame_1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (rdbtnPlayTheGame_1.isSelected())
				{
					rdbtnPlayTheGame.setSelected(false);
				}
				else
				{
					rdbtnPlayTheGame_1.setSelected(true);
					rdbtnPlayTheGame.setSelected(false);
				}
			}
			
		});
		contentPane.add(rdbtnPlayTheGame_1);
		
		JPanel panel = new JPanel();
		
		panel.setBounds(30, 104, 150, 150);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel label = new JLabel("");
		label.setOpaque(true);
		Image image1=null;
		try {
			image1 = ImageIO.read(new File("agario.png"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		label.setBounds(6, 6, 138, 138);
		ImageIcon icon = new ImageIcon(image1.getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_DEFAULT)); 
		label.setIcon(icon);
		panel.add(label);
		
		JButton btnNewButton = new JButton("OK");
		btnNewButton.setIcon(mainWindow.MainClientWindow.okIcon);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				if (rdbtnPlayTheGame_1.isSelected())
				{
					MainClientWindow m = new MainClientWindow();
					m.run();
					CloseFrame();
				}
				else
				{
					MainServerWindow m = new MainServerWindow();
					m.run();
					CloseFrame();
				}
				
			}
		});
		btnNewButton.setBounds(6, 266, 201, 50);
		contentPane.add(btnNewButton);
	}
}
