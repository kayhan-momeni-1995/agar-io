package world;

import java.io.Serializable;
import java.util.ArrayList;
import objects.*;

public class GameInfos implements Serializable
{
	private static final long serialVersionUID = -8843205535195254625L;
	public boolean isFinished=false;
	public String winnerUsername="";
	private ArrayList<Feed> feeds = new ArrayList<Feed>();
	private ArrayList<Circle> circles = new ArrayList<Circle>();
	private ArrayList<PowerDown> powerDowns = new ArrayList<PowerDown>();
	private ArrayList<PowerUp> powerUps = new ArrayList<PowerUp>();
	private ArrayList<Player> players = new ArrayList<Player>();
	private ArrayList<Gear> gears = new ArrayList<Gear>();
	private ArrayList<Obj> objects = new ArrayList<Obj>();
	
	public GameInfos()
	{
		feeds = new ArrayList<Feed>();
		circles = new ArrayList<Circle>();
		powerDowns = new ArrayList<PowerDown>();
		powerUps = new ArrayList<PowerUp>();
		players = new ArrayList<Player>();
		gears = new ArrayList<Gear>();
		objects = new ArrayList<Obj>();
		
		circles=Circle.all;
		feeds=Feed.all;
		gears=Gear.all;
		objects=Obj.all;
		powerDowns=PowerDown.all;
		powerUps=PowerUp.all;
		players=Player.all;
		if (mainWindow.MainServerWindow.mainPanel!=null)
			isFinished=mainWindow.MainServerWindow.mainPanel.finished;
		if (isFinished)
		{
			winnerUsername=Circle.all.get(0).getName();
		}
	}
	
	public void act()
	{		
		//Circle.all.clear();
		Circle.all=circles;
		
		//Feed.all.clear();
		Feed.all=feeds;
		
		//Gear.all.clear();
		Gear.all=gears;
		
		//PowerDown.all.clear();
		PowerDown.all=powerDowns;
		
		//PowerUp.all.clear();
		PowerUp.all=powerUps;
		
		//Player.all.clear();
		Player.all=players;
		
		//Obj.all.clear();
		Obj.all=objects;
	}

}
