// WOuld this even work? :/

package Parser;

import java.io.BufferedReader;
//import java.io.DataInputStream;
//import java.io.InputStreamReader;

public class ReadLN {
	//static DataInputStream in;
	public static String read(BufferedReader in)							//just a function to read the line (saves on writing it everywhere, or having a massive while loop
	{
		//BufferedReader d = new BufferedReader(new InputStreamReader(in)); // original method was deprecated.
		String Nextline = new String();
		
		try
		{
			/*if (in.available() !=0)
			{
				Nextline = in.readLine();
			}
			else
			{
				System.err.println("Unexpected File end");
				System.exit(-1);
			}*/
			
			Nextline = in.readLine();
			if (Nextline == null)
			{
				System.err.println("Unexpected File end");
				System.exit(-1);
			}
			
		}
		catch (Exception e)
		{
			System.err.println("Error Reading File Line");
		}
		
		return Nextline;
		
	}
}
