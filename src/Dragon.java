import java.util.Random;
public class Dragon extends Thread
{

	public static long time = System.currentTimeMillis();
	private MainThread mainThread;
	private Adventurer topPriorityAdventurer;
	
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
				if(playGame())
				{
					playerHasWon(topPriorityAdventurer);
				}
				else
				{
					// if the player just lost then we set their priority to max and pick that player again. 
					topPriorityAdventurer.setPriority(MAX_PRIORITY);
					topPriorityAdventurer=mainThread.pickPlayer();
					if(playGame())playerHasWon(topPriorityAdventurer);
				}

			}
			
		}
		msg(" has terminated because there are no more adventurers"+"\n");
	}
	private void playerHasWon(Adventurer adv)
	{
		msg("has lost to player"+adv.getName());
		adv.setPriority(NORM_PRIORITY);
		adv.isNoLongerWaitingForDragon();
		adv.interrupt();	
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
