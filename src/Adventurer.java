import java.util.ArrayList;
import java.util.Random;


public class Adventurer extends Thread
{
	// the variable list that each Adventurer needs
	public static long time = System.currentTimeMillis();
	private int fortuneSize = 5;
	private int adventurerId;
	private int stones;
	private int rings;
	private int chains;
	private int earrings;
	private int magicalRings,magicalNecklases,magicalEarrings;
	private static ArrayList<Boolean> need_assistance = new ArrayList<Boolean>();
	private MainThread mainThread;
	private boolean isWaitingForDragon=false;
	private int errorCheck=0;
	
	//constructor will set the fortuneSize,adventurerId,stones,rings,chains,earring,mainThread, and need_assitance variables
	public Adventurer(int id, int fortuneSize, MainThread parentThread) throws Exception
	{
		stones=getRandomInt()%4;
		rings=getRandomInt()%4;
		chains=getRandomInt()%4;
		earrings=getRandomInt()%4;
		adventurerId=id;				    
		need_assistance.add(false);// The assistance array initializes to false
		setName("Adventurer-"+(id+1));
		setfortuneSize(fortuneSize);
		mainThread= parentThread;
	}
	
	// This constructor is used in the mainThread so create an Adventurer pointer
	public Adventurer() {
	
	}
	
	private int getRandomInt()
	{
		Random randStone = new Random();
		int value = ((randStone.nextInt()));
		return value;
	}
	
	private void setfortuneSize(int fortune_Size) throws Exception
	{
		if(fortune_Size<1)
		{
			System.out.println("thats not a valid fortune size, the program will now throw an Exception");
			Exception e = new Exception("bad fortune size");
			throw e;
		}
		else fortuneSize=fortune_Size;
	}

	public void msg(String m)
	{
	System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+":"+m);
	}
	
	public void run()
	{
		msg("has started");
		while(checkFortune())
		{	
			if(checkForCraftableItems())
			{
				goToShop();
				shop();
			}
			// this if is so that we don't go to the dragon one last time after the adventurer makes his last piece of treasure
			if(checkFortune())goToDragonsCave();
		}
		
		mainThread.setAliveAdventurers(adventurerId);
		try {
			joinAdventurers();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		msg("is done "+"\n");
	}
	
	private void goToDragonsCave()
	{
		isWaitingForDragon=true;
		msg("has gone to the dragon's cave ");
		try
		{
			sleep(100000);
		} 
		catch (InterruptedException e)
		{
			// the adventurer will only wake if they win. they will then yield 
			giveTreasure(this);	
			// the yield doesn't seem to make the thread lower in priority, but I was told to use it so I am
			yield();
		}
		
		
	// trying to use the if with the isInterttuped method didnt seem to work, so I run the the code in the catch above
	/*	if(isInterrupted())
		{
			msg("has woke");
			giveTreasure(this);	// simulates playing the dragon for now
		}
	*/	
		
	}
	
	private synchronized void joinAdventurers() throws InterruptedException
	{
		if(mainThread.stillLiveAdventurer())
		{
			msg("has joined another adventurer"+"\n");
			mainThread.getAliveAdventurer().join();
		}
		else{
			msg("is the last one, so it will terminate and being the cascading termination"+"\n");
		}
	}
	
	private boolean checkFortune()
	{
		if(magicalRings>=fortuneSize && magicalNecklases>=fortuneSize && magicalEarrings>=fortuneSize) return false;
		return true;
	}
	
	//this will only return true if the adventurer can craft treasure for something that he needs more of
	private boolean checkForCraftableItems()
	{
		if(stones>0 && rings>0 && magicalRings<fortuneSize) return true;	
		else if (stones>0 && chains>0 && magicalNecklases<fortuneSize) return true;		
		else if(stones>1 && earrings>1  && magicalEarrings<fortuneSize)return true;
		else return false;						
	}
	
	private void goToShop()
	{
		msg("is going to the shop");
		// this will make the thread sleep anywhere from 0 to 4 seconds
		Random randTime= new Random();
		Long sleeptime=Math.abs(randTime.nextLong());
		try
		{
			Thread.sleep(sleeptime%4000);
		} 
		catch (InterruptedException e) {
			System.out.println("the sleeptime for goToShop failed");
			e.printStackTrace();
		}	
	}

	private synchronized void shop()
	{
		msg("has entered the shop");
		need_assistance.set(adventurerId, true);
		mainThread.joinShopLine(this);
		msg("is now busy-waiting");
		while(need_assistance.get(adventurerId))
		{
			// this is the busy-wait, with an error checker so i can see if a thread is stuck in the busy-wait
			errorCheck++;
			if(errorCheck%1000000==0)msg("is stuck in the busy wait");
		}		
		msg("is no longer busy-waiting");
		makeMagicItems();
		msg("has left the shop");
	}
	
	public void getAssistance()
	{ 
		need_assistance.set(adventurerId,false);
	}
	
	private void makeMagicItems()
	{
		while(checkForCraftableItems())
		{
			if(stones>0 && rings>0 && magicalRings<fortuneSize)
			{
				magicalRings++;
				rings--;
				stones--;
				msg("now has "+magicalRings+" magical ring(s)");
			}
			if(stones>0 && chains>0 && magicalNecklases<fortuneSize)
			{
				stones--;
				chains--;
				magicalNecklases++;
				msg("now has "+magicalNecklases+" magical necklase(s)");
			}
		    if(earrings>1 && stones>1 && magicalEarrings<fortuneSize)
			{
				earrings=earrings-2;
				stones=stones-2;
				magicalEarrings++;
				msg("now has "+magicalEarrings+" set(s) of magical earrings");
			}
		}
	}
	
	private void giveTreasure(Adventurer adventurer)
	{
		int prize=getRandomInt()%4;
		switch(prize)
		{
		case 0: stones++;
		break;
		case 1: rings++;
		break;
		case 2: earrings++;
		break;
		case 3: chains++;
		break;
		}
		
	}
	
	public boolean isWaitingForDragon()
	{
		return isWaitingForDragon;
	}
	
	public void isNoLongerWaitingForDragon()
	{
		isWaitingForDragon=false;
	}
}