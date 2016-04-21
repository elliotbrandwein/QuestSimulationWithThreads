
import java.util.ArrayList;


public class MainThread extends Thread{
	private Clerk[] clerks;
	private Adventurer[] adventurers;
	private Boolean[] adventurersThatStillNeedTreasure;
	private Boolean[] need_assistance;
	private LinkedQueue<Adventurer> shopLine = new LinkedQueue<Adventurer>();
	private ArrayList<Adventurer> dragonLine=new ArrayList<Adventurer>();
	private Dragon dragon;
	private int num_adv=0;
	private int num_clerk=2;
	private int num_fortuneSize=0;
	
	// the run method calls the start method on all the other threads the main thread has created
	public void run()
	{
		System.out.println("we are in the main run");
		initThreads();
		System.out.println("and the mainThread is done");
		return;
	}
	
	// this is the constructor for the Main class
	public MainThread(int adv_num, int fortune_size) throws Exception
	{ 
		//  first we get the input that was passed to the constructor
		num_adv=adv_num;
		num_fortuneSize=fortune_size;
		
		// next we make the arrays where we will store the clerk and adventurer threads, as well as other shared variables
		clerks= new Clerk[num_clerk];
		adventurers = new Adventurer[num_adv];
		adventurersThatStillNeedTreasure= new Boolean[num_adv];
		need_assistance = new Boolean[num_adv];
		 
		/* now we create all the clerks,adventurers and the dragon
		 * the shared variables are stored in this class, so we pass a pointer to this class to all the threads we make 
		 */
		for(int i =0; i<num_adv;i++)
		{
			adventurers[i]= new Adventurer(i,num_fortuneSize,this);
			adventurersThatStillNeedTreasure[i]=true;
			need_assistance[i]=false;
		}
		for(int i =0; i<num_clerk;i++)
		{
			clerks[i]= new Clerk(i,this);
		}
		dragon=new Dragon(this);
	}
	public void initThreads()
	{	
		for(int i=0; i<num_adv;i++)
		{
			adventurers[i].start();
		}
		for(int i=0;i<num_clerk;i++)
		{
			clerks[i].start();
		}
		dragon.start();
	}
	
	public boolean needAssistance(int id){
		boolean x;
		synchronized(this)
		{
			x=need_assistance[id];
		}
		return x;
	}
	
	public void setAssistance(int id)
	{
		synchronized(this)
		{
		need_assistance[id]=true;
		}
	}
	public void gotAssitance(int id){
		synchronized(this)
		{
			need_assistance[id]=false;
		}
	}
	public boolean isShopLineEmpty()
	{
		boolean x;
		synchronized(shopLine)
		{
		 x = shopLine.isEmpty();
		}
		return x;
	}
	public Adventurer getNextInShopLine()
	{
		Adventurer x;
		synchronized(shopLine)
		{
			x= shopLine.dequeue();
		}
		return x;
	}
	public void joinShopLine(Adventurer adv){
		synchronized(shopLine)
		{
			shopLine.enqueue(adv);
		}
	}
	public synchronized boolean checkForLivingThreads()
	{
		for(int i=0;i<num_adv;i++)
		{
			if(adventurers[i].isAlive()) return true;
		}
		return false;
	}
	public synchronized boolean stillLiveAdventurer()
	{
		for(int i=0; i<num_adv;i++)
		{
			if (adventurersThatStillNeedTreasure[i]==true)return true;
		}
		return false;
	}
	public void setAliveAdventurers (int i)
	{
		 adventurersThatStillNeedTreasure[i]=false;
	}
	public synchronized Adventurer getAliveAdventurer()
	{
		for(int i=1;i<num_adv;i++)
		{
			if(adventurersThatStillNeedTreasure[i]) return adventurers[i];	
		}
		return adventurers[0];
	}
	public int joinDragonLine(Adventurer adventurer)
	{
		int x;
		synchronized(dragonLine)
		{
			x=dragonLine.size();
			dragonLine.add(adventurer);
		}
		return x;
	}
	public Adventurer pickPlayer() 
	{
		synchronized(adventurers)
		{
			Adventurer topPlayer= new Adventurer();
			for(int i =0;i<num_adv;i++)
			{
				if(topPlayer.getPriority()<=adventurers[i].getPriority() && adventurers[i].isWaitingForDragon())
				{
					topPlayer=adventurers[i];
				}
			}
			topPlayer.setPriority(NORM_PRIORITY);
			return topPlayer;
		}
	}
	public boolean checkDragonLine()
	{
		for(int i =0;i<num_adv;i++)
		{
			if(adventurers[i].isAlive() && adventurers[i].isWaitingForDragon())return true;
		}
		return false;
	}

	public void leaveDragonLine(int threadNumber) {
		synchronized(dragonLine)
		{
			dragonLine.remove(threadNumber);
		}
		
	}
	
}
