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
		while(mainThread.checkForLivingThreads())
		{
			helpCustomers();	
		}
		msg("has terminated because there are no more adventurers"+"\n");
	}
		

	private synchronized void helpCustomers()
	{
		if(mainThread.isShopLineEmpty()!=true)
		{
			msg("is about to help a waiting customer");
			try
			{
			mainThread.getNextInShopLine().getAssistance();
			}
			catch(Exception e){}
			msg("has helped the customer");
			
		}
	    
	}
}
