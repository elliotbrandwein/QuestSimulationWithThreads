
public class Main{
	// finally, all the main method does is create the Main class and start that thread
	public static void main(String[] args) throws Exception
	{
		 MainThread questAdventure = new MainThread();
		 System.out.println("we made a Main object and are about to start all the threads");
		 questAdventure.start();
	}
	
}
