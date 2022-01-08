package objects;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

import javax.swing.Timer;

public class ObjectDestroyer extends Thread implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2195707240840204749L;
	private Obj obj;
	private int ms;
	
	public ObjectDestroyer(Obj obj, int ms)
	{
		this.obj=obj;
		this.ms=ms;
	}
	@Override
	public void run()
	{

		Timer t = new Timer(ms, null);
		t.addActionListener(new ActionListener()
		{
			boolean s=false;
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (s==true)
				{
					if (obj.getType()==0 && ((Circle)(obj)).getParent()!=null && !((Circle)(obj)).isTerminated)
					{
						((Circle)(obj)).getParent().increaseArea(obj.getArea());
						synchronized(Color.black)
						{
							obj.destroy();
						}
					}
					t.stop();
				}
				s=true;
			}
		});
		t.start();
	}
}
