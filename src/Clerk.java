import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;

public class Clerk extends Thread{
	public static long time = System.currentTimeMillis();
	private MainThread mainThread;
	
	public void msg(String m)
	{
		System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+":"+m);
	}
	
	public Clerk(int id, MainThread parentThread) throws Exception
	{
		setName("Clerk-"+(id+1));
		mainThread=parentThread;
	}
	
	public void run()
	{
		msg("has been made");
		while(mainThread.stillLiveAdventurer())
		{
			helpCustomers();	
		}
	}
		

	private synchronized void helpCustomers()
	{
		if(mainThread.getShopLine().isEmpty()!=true)
		{
			msg("is about to help a waiting customer");
			LinkedQueue<Adventurer> customers= mainThread.getShopLine();
			customers.dequeue().getAssistance();
		}
	    
	}
}
