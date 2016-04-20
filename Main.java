
public class Main{
	// finally, all the main method does is create the Main class and start that thread
	public static void main(String[] args) throws Exception
	{
		int num_adv=0;
		int num_fortuneSize=0;
		// first we see there are no arguments and if so, we use the defaults
		if (args.length == 0)
		{
			num_adv = 6;
			num_fortuneSize = 5;
		}
		else
		{
	        //if there are the wrong number of arguments we just quit
			if(args.length != 2)System.exit(0);
			
			// if there are arguments we check them and then use them if they are ints
			try
			{
				num_adv = Integer.parseInt(args[0]);
				num_fortuneSize = Integer.parseInt(args[1]);
			}
			catch (Exception e)
			{
				System.out.println("the argumets weren't integers");
				System.exit(0);
			}
			// then we make sure they are positive 
			if (num_adv < 1 || num_fortuneSize < 1)
			{
				System.out.println("the arguments weren't positive");
				System.exit(0);	
			}
		}
		 MainThread questAdventure = new MainThread(num_adv,num_fortuneSize);
		 System.out.println("we made a Main object and are about to start all the threads");
		 questAdventure.start();
	}
	
}
