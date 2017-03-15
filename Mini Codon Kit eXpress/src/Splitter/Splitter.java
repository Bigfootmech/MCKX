package Splitter;
// Use names to align/group O (algorithm) or originals (organism)
// If doesn't exist, option to pick a different one or abort
//
// Check for blank genecode
//
// Have a better (faster - can be 5 times) "look up" algorithm
//
// add choose "random" optimized support
//add randomisation, rather than always picking number 1
//check for stupidity such as overlap > maxlength, or whatever
//
//fiddle with choosing how to split the code? 2 or 3 times?
//
// X // only check for overlap if you need to overlap (ie: for the first and last string, don't have to overlap. 
// Sol: // intrinsic in code now.
//
//add case 2, random -n or -o string


import java.io.BufferedReader;
//import java.io.DataInputStream;
import java.io.File;
//import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class Splitter {

	
	private static ArrayList<String> Splittingup (String genecode,int MaxLength,int Overlap,String prefix,String suffix)							//just a function to read the line (saves on writing it everywhere, or having a massive while loop
	{
		ArrayList<String> donesplitting = new ArrayList<String>();
		String toSplit = (prefix + genecode + suffix).toUpperCase();
		donesplitting.addAll(resplit(toSplit,MaxLength,Overlap));
		
		return donesplitting;
	}
	
	
	
	private static ArrayList<String> resplit (String toSplit,int MaxLength,int Overlap)							//just a function to read the line (saves on writing it everywhere, or having a massive while loop
	{
		ArrayList<String> donesplitting = new ArrayList<String>();
		
		if (toSplit.length() <= MaxLength)
		{
			System.out.print("Out ");
			donesplitting.add(toSplit);
		}
		else
		{
			System.out.print("Split ");
			int Splitsize = (int) Math.ceil(toSplit.length()/2);
			donesplitting.addAll(resplit(toSplit.substring(0, (int) ((int) Splitsize+Math.floor(Overlap/2))),MaxLength,Overlap)); // fiddling with size of overlap, so it doesn't break if the overlap entered isn't even. 0 --> smaller overlap, and larger overlap --> end form the entire thing with the odd overlap accounted for.
			donesplitting.addAll(resplit(toSplit.substring((int) (Splitsize+1-Math.ceil(Overlap/2))),MaxLength,Overlap)); // (Splitsize+1) comes after splitsize
		}
		return donesplitting;
	}

	
	/*private static String ReadLN (BufferedReader in)							//just a function to read the line (saves on writing it everywhere, or having a massive while loop
	{
		
		String Nextline = new String();
		
		try
		{
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
	*/
	
	
	
	
	private static ArrayList<String> Readgeneric (/*String FolderName,*/ String protein_id)							//just a function to read the line (saves on writing it everywhere, or having a massive while loop
	{
		String Nextline = "";
		String Store = "";
		int count = 1;
		ArrayList<String> Strings = new ArrayList<String>();					// Could be either optimised genetic code, OR originals
		File file=new File(/*FolderName + "/" + */protein_id +".txt");
	
		if (!file.exists()) 
		{
			System.out.println( "\n\r File: " + /*FolderName + "/" + */protein_id +".txt" + " doesn't exist." );
			System.exit(-1);
		}
		else 
		{
			try 
			{
	            //FileInputStream fstream = new FileInputStream(file);			// Open the file that is the first command line parameter
				//DataInputStream in = new DataInputStream(fstream); 				//Convert our input stream to a DataInputStream
				BufferedReader in = new BufferedReader(new FileReader(file)); // original method was deprecated.
				
				do
				{
					Nextline = Parser.ReadLN.read(in);								//read a line
		    		// if (!Nextline.equals("")) System.out.println(Nextline);
					if (Nextline.equals("Optimizations"))				//if optimized section, read through it
					{
						Nextline = Parser.ReadLN.read(in);							//ignore the *** line after opti
						Nextline = Parser.ReadLN.read(in);							//read first possible valid line
						while (!Nextline.equals("***"))					//keep reading until the end of the optimised area
						{
							Strings.add(Integer.toString(count)); 		//Add the number for each new valid line found
							Strings.add(Nextline);						//add it to the stack
							count=count+1;								//count up for each line
							Nextline = Parser.ReadLN.read(in);						//read new one in
						}
						Strings.add("Optend");							//Add marker to array
						count = 1;										//reset count to 1 for "original" genecodes found.
					}
					else if (Nextline.equals("ORGANISM:"))				//if you find out which organism, save it
					{
						Store = Parser.ReadLN.read(in);
					}
					else if (Nextline.equals("***"))					//for the end of each original gene block, clear Organism, so it's not written everywhere.
					{
						Store = "";
					}
					else if (Nextline.equals("genecode:"))				//when you reach a genecode
					{
						Nextline = Parser.ReadLN.read(in).toUpperCase();
						Strings.add(Integer.toString(count)); 
						Strings.add(Store);
						Strings.add(Nextline);							//save it to the array
						count=count+1;
					}
					else if (Nextline.equals("End")) 
					{
						break;
					}
				}while (!Nextline.equals("End"));								//Exit loop when you reach the end of file.
				
				
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		return Strings;
	}
	
	public static ArrayList<String> run(String args[])
	{
		//args[0] = file name to check
				//args[1] = -o or -n ie:select which optimised or normal codes you're using
				//args[2] = within that subset, which one you're taking
				//args[3] = MaxLength
				//args[4] = Overlap
				//args[5] = prefix -> could set to check for a tag if just want to change prefix or just suffix?
				//args[6] = suffix
				//args[7+] = error (case 8 onwards)
				
				
				ArrayList<String> Strings;
				int MaxLength = 800;
				int Overlap = 40;
				String prefix = "CAGCCTGCGGTCCGG";
				String suffix = "CGGGCGTCCCAGCGA";
				//String prefix = "";
				//String suffix = "";
				//ArrayList<String> decomposed;
				
		       switch (args.length){
		        default:
		        	System.err.print("\nAn incorrect number of arguments was entered. Run the class with no tags for help.");
		        	System.exit(-1);
		        case 0: // only file ran, no options
		        	System.out.print("\n\rFor arguments, either put in just the file name to view which genecode you want to select for breakdown. Or put in the file name, -o for optimized or -n for normal/original, and the integer correcponding to the one you would like to select.");
		        	System.exit(-1);
		        case 7: // all possible options entered
		        	//Prefix - suffix 
		        	prefix = args[5].toUpperCase();
		        	suffix = args[6].toUpperCase();
		        case 5:
		        	Overlap = Integer.valueOf(args[4]); //length of overlap
		        case 4:
		        	MaxLength = Integer.valueOf(args[3]);//length of seg max
		        case 3:
		        	Strings = Readgeneric(args[0]); // read in the whole file
		        	int i = Strings.indexOf("Optend"); //find where the split is
		        	if(args[1].equals("-o"))
		        	{
		        		if (Integer.valueOf(args[2]) > 0 && Integer.valueOf(args[2])*2 < (i)) // not fully checked {0 to Optend} // modifier *2 to catch string, not numbering
		        		{
		        			Strings = Splittingup((Strings.get(Integer.valueOf(args[2])*2)), MaxLength, Overlap, prefix, suffix); // modifier *2 to catch string, not numbering
		        			//can have a new variable, or can reuse old. No real difference
		        			//System.out.print((decomposed)); // Arrays.toString
		        		}
		        		else 
		        		{
		                	System.err.print("\n\rThat ("+i+") number -o genecode doesn't exist in this CDS file.");
		                	System.exit(-1);
		        		}
		        	}
		        	else if (args[1].equals("-n"))
		        	{
		        		if (Integer.valueOf(args[2]) > 0 && Integer.valueOf(args[2])*3 <= (Strings.size()-i)) // not fully checked {0 to stringlength-locationofOptend} // modifier *3 to catch string, not numbering or organism
		        		{
		        			Strings = Splittingup(Strings.get(i+(Integer.valueOf(args[2]))*3), MaxLength, Overlap, prefix, suffix); // modifier *3 to catch string, not numbering or organism
		                	
		        			//System.out.print((decomposed));
		        		}
		        		else 
		        		{
		                	System.err.print("\n\rThat ("+i+") number -n genecode doesn't exist in this CDS file.");
		                	System.exit(-1);
		        		}
		        		
		        	}
		        	else
		        	{
		            	System.out.print("\n\rYou have incorrectly selected whether you want -o Optimized genecode, or -n a normal/original one.");
		            	System.exit(-1);
		        	}
		        	
		        	break; // important clause there
		        	/////////
		        	
		        	//add case 2, a random n or o string?
		        	
		        case 1:	
		        	//File_read_name = args[0];
		        	//start = DEFAULT;
		        	//end = DEFAULT;
		        	//FolderName = DEFAULT;
		    		System.out.print("\n"+args[0]+"\n\n");
		            
		        	Strings = Readgeneric(args[0]);
		        	
		        	//pick a random one? :S
		        	if (Strings.get(0).equals("Optend")) // if there are no optimizations
		        	{
		        		Strings = Splittingup (Strings.get(3),MaxLength,Overlap,prefix,suffix); // split 1st non optimised, as no optimised (modifier 3, ie: optend, number, organism, genecode)
		        	}
		        	else
		        	{
		        		Strings = Splittingup (Strings.get(2),MaxLength,Overlap,prefix,suffix); // split 1st optimised (modifier 2, ie: optend, number, genecode)
		        	}
		            //no need for break
		        }
		        

		   		System.out.print("\n\rSplit:\n\r" + Strings);
		   		return Strings;
		       //return, or output file?
	}
	
	public static void main(String args[])
	{
		 // padding - if we want the strings or other things to be returned to a program, we use run, otherwise, use main.
		run(args);
	} // End main
	
	
	
	
	
}
