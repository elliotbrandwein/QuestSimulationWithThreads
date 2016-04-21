import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;

public class Clerk extends Thread{
	private static Object shopObject= new Object();
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
		

	private void helpCustomers()
	{
		synchronized(shopObject){
			if(mainThread.isShopLineEmpty()!=true)
			{
				msg("is about to help a waiting customer");
				mainThread.getNextInShopLine().getAssistance();
				msg("has helped the customer");
				
			}
		}
	}
}
