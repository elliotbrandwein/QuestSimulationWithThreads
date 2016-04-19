import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

public class MainThread extends Thread{
	private Clerk[] clerks;
	private Adventurer[] adventurers;
	private Boolean[] aliveAdventurers;
	private LinkedQueue<Adventurer> shopLine = new LinkedQueue<Adventurer>();
	private Dragon dragon;
	private boolean shopLineLock=false;
	private int num_adv=6;
	private int num_clerk=2;
	private int num_fortuneSize=5;
	
	// the run method calls the start method on all the other threads the main thread has created
	public void run()
	{
		System.out.println("we are in the main run");
		initThreads();
		System.out.println("and the mainThread is done");
		return;
	}
	// this is the constructor for the Main class
	public MainThread() throws Exception
	{ 
		//first we get the input from the user on how many clerks and adventurers we will be making	
		num_adv=getInput("how many adventurers do you want?");
		num_clerk=getInput("how many clerks do you want?");
		
		// next we make the arrays where we will store are the clerk and adventurer threads
		clerks= new Clerk[num_clerk];
		adventurers = new Adventurer[num_adv];
		aliveAdventurers= new Boolean[num_adv];
		 
		/* now we make all the clerks,adventurers and the dragon
		 * the shared variables are stored in this class, so we pass it to each thread we make	 
		 */
		for(int i =0; i<num_adv;i++)
		{
			adventurers[i]= new Adventurer(i,num_fortuneSize,this);
			aliveAdventurers[i]=true;
		}
		for(int i =0; i<num_clerk;i++)
		{
			clerks[i]= new Clerk(i,this);
		}
		dragon=new Dragon(this);
		System.out.println("end of the main constructor");
	}
	public void initThreads()
	{
		System.out.println("we are in the initThreads method");	
		for(int i=0;i<num_clerk;i++)
		{
			clerks[i].start();
		}
		for(int i=0; i<num_adv;i++)
		{
			adventurers[i].start();
		}
		dragon.start();
	}
	
	// this method takes in the user input for how many clerks they want or how many adventurers they want
	private int getInput(String question) throws IOException
	{
		Scanner sc = new Scanner(System.in);
		boolean getInput=true;
		int output=0;
		while(getInput)
		{
			System.out.print(question);
		    try
		    {
		    	output=sc.nextInt();
		    }
		    catch (NumberFormatException e)
		    {
		    	System.out.println("that wasn't a number, try that again");
		    }
		    if(output<1) System.out.println("that was too low of a number"+"\n");
			else getInput=false;
		}
		return output;
	}
	
	
	public synchronized boolean  isShopLineEmpty()
	{
		shopLock();
		boolean x = shopLine.isEmpty();
		shopUnlock();
		return x;
	}
	private synchronized void shopUnlock()
	{
		shopLineLock=false;
	}
	private synchronized void shopLock()
	{
	 while(shopLineLock){}
	 shopLineLock=true;
	}
	public synchronized Adventurer getNextInShopLine()
	{
		shopLock();
		Adventurer x= shopLine.dequeue();
		shopUnlock();
		return x;
	}
	public synchronized void joinShopLine(Adventurer adv){
		shopLock();
		shopLine.enqueue(adv);
		System.out.println(adv.getName() +" has entered the shop line");
		shopUnlock();
	}
	public synchronized boolean stillLiveAdventurer()
	{
		
		for(int i=0; i<num_adv;i++)
		{
			if (aliveAdventurers[i]==true)return true;
		}
		return false;
	}
	 public void setAliveAdventurers (int i)
	{
		aliveAdventurers[i]=false;
	}
	public synchronized Adventurer getAliveAdventurer()
	{
		for(int i=1;i<num_adv;i++)
		{
		if(aliveAdventurers[i]) return adventurers[i];	
		}
		return adventurers[0];
	}
	
	/*
	synchronized public void addToDragonLine(Adventurer adventurer)
	{
		dragonLock();
		dragonLine.add(adventurer);
		dragonUnlock();
	}
	private synchronized void dragonUnlock()
	{
		dragonLineLock=false;
	}
	private synchronized void dragonLock()
	{
	 while(dragonLineLock){}
	 dragonLineLock=true;
	}
	synchronized public ArrayList<Adventurer> getDragonLine()
	{
		dragonLock();
		ArrayList<Adventurer> dLine=dragonLine;
		dragonUnlock();
		return dLine;
	}
	synchronized public boolean hasDragonLine()
	{	
		dragonLock();
		boolean lock = dragonLine.isEmpty();
		if(lock){
			dragonUnlock();
			return false;
		}
		else{
			dragonUnlock();
			return true;
		}
	}
	synchronized public boolean getPlayerJustLost()
	{
	return playerJustLost;	
	}
	public boolean didDragonWin()
	{
		if(playerJustLost)
		{
		setPlayerJustLost(false);	
		return true;
		}
		return false;
	}
	public void setPlayerJustLost(boolean x)
	{
		playerJustLost=x;
	}
	*/
}
