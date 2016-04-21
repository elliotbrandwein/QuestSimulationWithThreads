import java.util.ArrayList;
import java.util.Random;
public class Dragon extends Thread {
public static long time = System.currentTimeMillis();
	
	private MainThread mainThread;
	private Adventurer playedLast;
	private Adventurer topPriorityAdventurer;
	private boolean rematch =false;
	int x=0;
	
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
		while(mainThread.checkForLivingThreads())
		{	
			if(mainThread.checkDragonLine())
			{
				topPriorityAdventurer=mainThread.pickPlayer();
				// This simulates playing the game with the adventurer.
				topPriorityAdventurer.isNoLongerWaitingForDragon();
				topPriorityAdventurer.interrupt();	

			}
			
		}
		msg(" has terminated because there are no more adventurers"+"\n");
	}
	private int getRandomInt()
	{
		Random randStone = new Random();
		int value = ((randStone.nextInt()));
		return value;
	}
	private boolean playGame()
	{
		boolean game=true;
		boolean output=true;
		while(game){
			int dragonDiceRoll=(getRandomInt()%6)+1;
			int humanDiceRoll =(getRandomInt()%6)+1;
			if(dragonDiceRoll>humanDiceRoll)
			{
				game=false;
				output=false;
			}
			if(dragonDiceRoll>humanDiceRoll)
			{
				game=false;
				output=true;
			}
		}
		return output;
	}
}
