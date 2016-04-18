import java.util.ArrayList;
import java.util.Random;


public class Adventurer extends Thread {
	// the variable list that each Adventurer needs
	public static long time = System.currentTimeMillis();
	private int foretuneSize = 5;
	private int adventurerId;
	private int stones;
	private int rings;
	private int chains;
	private int earrings;
	private int magicalRings,magicalNecklases,magicalEarrings;
	private static ArrayList<Boolean> need_assistance = new ArrayList<Boolean>();
	private MainThread mainThread;
	private boolean isWaitingForDragon=false;
	
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
		else foretuneSize=fortune_Size;
	}

	public void msg(String m)
	{
	System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+":"+m);
	}
	
	public void run()
	{
		msg("has started");
		while(checkFortune()){	
			if(checkForCraftableItems())
			{
				goToShop();
				shop();
			}
			//goToDragonsCave();
			stones=10;
			rings=10;
			chains=10;
			earrings=10;
		}
		// write method for checking if there are other adventurers and then join them if they exist
		mainThread.setAliveAdventurers(adventurerId);
		msg("is done "+"\n");
	}
	
	private boolean checkFortune()
	{
		if(magicalRings>=foretuneSize && magicalNecklases>=foretuneSize && magicalEarrings>=foretuneSize) return false;
		return true;
	}
	
	private boolean checkForCraftableItems()
	{
		if(stones>0 && rings>0) return true;			// he can create a magic ring
		else if (stones>0 && chains>0) return true;		// he can create a magic necklace
		else if(stones>1 && earrings>1)return true;		// he can create a magic pair of earrings
		else return false;								// no items could be crafted
	}
	
	
	synchronized private void goToShop()
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
			System.out.println("the sleeptime for gotoshop failed");
			e.printStackTrace();
		}	
	}


	synchronized private void shop()
	{
		msg("has entered the shop");
		need_assistance.set(adventurerId, true);
		mainThread.joinShopLine(this);
		msg("is now busy-waiting");
		while(need_assistance.get(adventurerId)){}		// this is the busy-wait, waiting for the clerk at the shop
		msg("has got help");
		makeMagicItems();
		msg("has left the shop");
	}
	public void getAssistance(){ need_assistance.set(adventurerId,false);}
	
	synchronized private void makeMagicItems()
	{
		while(checkForCraftableItems())
		{
			if(stones>0 && rings>0)
			{
				magicalRings++;
				rings--;
				stones--;
				msg("now has "+magicalRings+" magical ring(s)");
			}
			if(stones>0 && chains>0)
			{
				stones--;
				chains--;
				magicalNecklases++;
				msg("now has "+magicalNecklases+" magical necklase(s)");
			}
		    if(earrings>1 && stones>1)
			{
				earrings=earrings-2;
				stones=stones-2;
				magicalEarrings++;
				msg("now has "+magicalEarrings+" set(s) of magical earrings");
			}
		}
	}
	
	private void givetreasure(Adventurer adventurer)
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
}