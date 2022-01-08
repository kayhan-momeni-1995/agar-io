package world;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.ImageIcon;


public class PlayerData implements Serializable
{

	private static final long serialVersionUID = 2546956705802283221L;
	public static ArrayList<PlayerData> all = new ArrayList<PlayerData>();
	private String name=null;
	private String user=null;
	private String password=null;
	private Color color=null;
	private ImageIcon img=null;
	public PlayerData(String user,String pass, String name, Color col, ImageIcon img)
	{
		this.name=name;
		this.user=user;
		this.password=pass;
		this.color=col;
		this.img=img;
		all.add(this);
	}
	public String getName()
	{
		return this.name;
	}
	public String getUsername()
	{
		return user;
	}
	public Color getColor()
	{
		return this.color;
	}
	public String getPassword()
	{
		return this.password;
	}
	public ImageIcon getImage()
	{
		return this.img;
	}
	public void setName(String name)
	{
		this.name=name;
	}
	public void setColor (Color c)
	{
		this.color=c;
	}
	public void setImage (ImageIcon img)
	{
		this.img=img;
	}
	public void setPassword (String password)
	{
		this.password=password;
	}
	public static int indexOf(String username)
	{
		int result=-1;
		synchronized(Color.GREEN)
		{
			for (int i=0; i<all.size(); i++)
			{
				if (all.get(i).getUsername().equals(username))
				{
					result=i;
					break;
				}
			}
		}
		return result;
	}
}
