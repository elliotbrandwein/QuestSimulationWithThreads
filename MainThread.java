import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

public class MainThread extends Thread{
	private Clerk[] clerks;
	private Adventurer[] adventurers;
	private Boolean[] adventurersThatStillNeedTreasure;
	private Boolean[] need_assistance;
	private LinkedQueue<Adventurer> shopLine = new LinkedQueue<Adventurer>();
	private Dragon dragon;
	private boolean shopLineLock=false;
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
		//num_adv=getInput("how many adventurers do you want?",0);
		//num_clerk=getInput("how many clerks do you want?",1);
		//num_fortuneSize=getInput("whats the fortune size?",2);
		
		num_adv=adv_num;
		num_fortuneSize=fortune_size;
		
		
		
		// next we make the arrays where we will store are the clerk and adventurer threads
		clerks= new Clerk[num_clerk];
		adventurers = new Adventurer[num_adv];
		adventurersThatStillNeedTreasure= new Boolean[num_adv];
		need_assistance = new Boolean[num_adv];
		 
		/* now we make all the clerks,adventurers and the dragon
		 * the shared variables are stored in this class, so we pass it to each thread we make	 
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
		System.out.println("end of the main constructor");
	}
	public void initThreads()
	{
		System.out.println("we are in the initThreads method");	
		
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
		synchronized(this){
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
			System.out.println(adv.getName() +" has entered the shop line");
		}
	}
	public synchronized boolean checkForLivingThreads()
	{
		for(int i=0;i<num_adv;i++){
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
	/*
	 *  
	private synchronized void shopUnlock()
	{
		shopLineLock=false;
	}
	private synchronized void shopLock()
	{
	 while(shopLineLock){}
	 shopLineLock=true;
	}
	
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
	
	// this method takes in the user input for how many clerks they want or how many adventurers they want
	private int getInput(String question, int version) throws IOException
	{
		Scanner sc = new Scanner(System.in);
		boolean getInput=true;
		int output=0;
		while(getInput)
		{
			System.out.print(question);
		   
		    	output=sc.nextInt();
		    if(output<1) System.out.println("that was too low of a number"+"\n");
			else getInput=false;
		}
		return output;
	}
	
	
	*/
}
