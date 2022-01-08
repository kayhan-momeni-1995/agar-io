package serializer;

import java.io.Serializable;
import java.util.ArrayList;

import world.PlayerData;

class ProgramData implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5682097095965992856L;
	ArrayList<PlayerData> playerDatas;

	public ProgramData()
	{
		playerDatas = PlayerData.all;
	}
	public void updateProgram()
	{
		if (playerDatas.size()>0)
		for (int i=0; i<playerDatas.size(); i++)
		{
			PlayerData p=playerDatas.get(i);
			new PlayerData(p.getUsername(), p.getPassword(), p.getName(), p.getColor(), p.getImage());
		}
	}
}
