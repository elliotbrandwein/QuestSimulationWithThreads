import java.util.ArrayList;

public class Dragon extends Thread {
public static long time = System.currentTimeMillis();
	
	private MainThread mainThread;
	private Adventurer playedLast;
	private Adventurer topPriorityAdventurer;
	private boolean rematch =false;
	
	public void msg(String m)
	{
	System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+":"+m);
	}
	
	public Dragon(MainThread parentThread)
	{
		mainThread=parentThread;
		setName("Dragon");
	}
	public void run()
	{
		msg("has been made");
		while(mainThread.stillLiveAdventurer())
		{
	
		}
	}
/*	
	private Adventurer pickPlayer()
	{
		{
			ArrayList<Adventurer> gameLine = mainThread.getDragonLine();
			int lineLength = gameLine.size();
			for(int i=0;i<lineLength; i++)
			{
				if(i==0)topPriorityAdventurer=gameLine.get(i);
				else if (gameLine.get(i).getPriority()>topPriorityAdventurer.getPriority() && gameLine.get(i).getIsWaitingForDragon())
				{
					topPriorityAdventurer=gameLine.get(i);
				}
			}
			playedLast=topPriorityAdventurer;
			return topPriorityAdventurer;
		}
	}

	private void playGame()
	{
		Adventurer player = pickPlayer();
		player.interrupt();		
	}
	*/
}
