package Parser;
//package Import;

/*
 * 
 * 

Optional: 
- Sluggish behavious -> remove ANY element not in use! ++ add a new length vector to represent place.
- Add an "add and immediately remove" clause.
- stop reading if no CDS captured / After the end?
- try/catch converting to ints in run
- do a prelim process of file in GUI, then have a slider and only pass numbers?
- ineffective use of gui?
- disable for the 10 max overlap
- preemptively select ya/owa/na from non UI 


known bugs/errors: 
Major: 
* The first genecode not being registered?
* 
None known, I ironed the kinks out so you'd be able to play with it. I'll test it more rigorously when I have more time.
However it does heavily depend on the files in being genbank format - anything else would throw it off I assume.

Minor:
I haven't accounted for any CDS region starting and ending on the same line - this would mean that it's less than 60 bases long, is that realistic?
aaand I haven't accounted for "massive overlaps" (start close to start of code, and keep going until near the end) - this would slow down the program, and I can "fix" it with a few more lines of code, and variables floating around.

Optional additions:

Warning to tell users that the specified numbers are outside of the genetic code in the file

COMMENT MOAR/ Change comments!!!
* 
* 
* 
* 
* 
* 
* 
* 
* --------------------------
18/4/2012 v 0.0.007
Finally managed to get the progress bar thing working
{
   You can see it update as it processes in the background
   It updates for all 3 stages (read CDS regions / retrieve their genecode / save to files), as well as showing the numbers
   for the first one, it just does an arbitrary loading animation, for the other two, it tells you the exact progress
   the cancel button works correctly now, effectively aborting the job, rather than just hiding the work.
   got the "yes/no/overwrite..." dialogue menu to pass stuff back correctly (additional point: Found out that using html in the "labels" you can get word wrapping)
}
Got the string array with all the file names to pass to the main GUI program -> this can be later saved to a file as (part of) the workspace.
Also added an "about" to the menus.

I still can't figure out how stuff is positioned or scaled, so it's a bit all over the place. However so far, the gui for the parser works, as well as the parser's "no gui" side still working.

Other to do stuff:
Test out other .gb files
Make a splitter gui
Make a dumb optimizer
Draw up a "workboard" format
Make a log error file
improve stuff/bugfix
* ---------------------------
*/


import java.io.BufferedReader;
//import java.io.DataInputStream;
import java.io.File;
//import java.io.FileInputStream;
import java.io.FileReader;
import java.util.ArrayList; 

public class Parser 
{
	
    private static int ProcTillOrig(BufferedReader in, CDS_list list, int startplc, int endplc, boolean guiUse, Main.Main instance) 
    //private static int ProcTillOrig(BufferedReader in, CDS_list list, String start, String end) 
    {
        //int startplc = 0;
        //int endplc = 0;
        boolean endq; // not sure which way is less commands, but this way it's more readable I think
        if (endplc < 0) {
            endq = true;
        } else {
            endq = false;
        }
        //if (!start.equals("start"))	startplc = Integer.parseInt(start);					//exploit the fact that start at start = line 0;
        //if (!end.equals("end")){
        //	endplc = Integer.parseInt(end);
        //	endq = false;
        //}
                
		
		
        int CDScount = 0;
        int printcount = 0;
		String organism = "";
                
                if (guiUse)
                {
                    instance.setVisible(true);
                    instance.settext("Reading CDS Blocks", "");  
                }
                else System.out.print ("\n\rReading CDS Blocks\n\r");
                
		while((!guiUse||!instance.worker.isCancelled()))
		{

		
			String Nextline = ReadLN.read(in);
			
			String delimiters = "[ ()]+";												//get rid of spaces
			String[] SplitLines = Nextline.split(delimiters);	
			//System.out.print ("\n\r"+ SplitLines[1] + "\n");	
			
			
			if (SplitLines.length >2)													// Safety Measure - check that you're allowed to read that far.
			{
				if (SplitLines[1].equals("CDS"))
				{																		// Start reading a CDS section
					String delimit = "[.]+";											//Dangerous to do in the normal reading - it crashes
					String[] StartEnd = SplitLines[SplitLines.length-1].split(delimit);	//If complement, then number of strings in SplitLines is == 4, if not, then 3, split these further

					if(Integer.parseInt( StartEnd[0] ) >= startplc && (endq || Integer.parseInt( StartEnd[1] ) <= endplc) )
					{
                                            ++CDScount;
                                            if (printcount < 100) printcount++;
                                            else printcount = 0;
                                            if (guiUse) 
                                            {
                                                //printcount = CDScount;
                                                //while(printcount > 100) printcount -=100; less error prone, but less efficient
                                                instance.setinter(CDScount);
                                            } 
                                            else 
                                            {
                                                System.out.print ("\r" + CDScount);
                                            }
						CDS_block temp = new CDS_block ();
						
						if (SplitLines.length == 4) temp.complement = true;
						temp.start = Integer.parseInt( StartEnd[0] );
						temp.end = Integer.parseInt( StartEnd[1] );
						temp.organism = organism;
						
						temp = Read(in, temp);
						list.insertEnd(temp);
					}
				}
				else if (SplitLines[1].equals("ORGANISM"))
				{
					//System.out.print ("\n\r Gotcha\n" + SplitLines[2] + SplitLines.length);	
					for (int i = 2; i < SplitLines.length; i++)							//For parts 2 to end-1, combine them to form the organism name.
					{
						organism = organism + SplitLines[i] + " ";
					}
                                        
                                        if (guiUse) 
                                        {
                                            instance.setTitle(organism);
                                        }
                                        else System.out.print ("\n\rThe organism is: " + organism + "\n\n\rCDS Count:\n\r");
				}
			}
			else if (SplitLines[0].equals("ORIGIN"))									// If you find the "ORIGIN" tag - start of the DNA, but no point looking for it if the CDS tag is already there.
			{
                            if (guiUse)
                            {
                               instance.setIntermediateOff(CDScount);
                               instance.settext("Reading the DNA sequences", "Overlap");
                            }
                            else System.out.print ("\n\rReading the DNA sequence\n");
                            break;
																		// CONDITION: You're in the DNA part of code now.
			}
		}
                return CDScount;
    	

    }
	
	private static CDS_block Read (BufferedReader in, CDS_block temp)			//Recurring function read - exits on finishing CDS block
    {
		String Nextline = ReadLN.read(in);
		
		String delimiters = "[/=\"]+";
		String[] LineDasm = Nextline.split(delimiters);

		
		if ( (!LineDasm[1].equals("codon_start") && !LineDasm[1].equals("transl_table")) && !Nextline.substring(Nextline.length()-1, Nextline.length()).equals("\"")){	// if the argument name isn't codon_start, or transl_table, then it should end in a quotation mark, or there's more to be tagged on. (the two that aren't in quotes are sufficiently short to not need tagons.
			LineDasm[2] = TagOn(in, LineDasm[2]);	//invoke the 'tag stuff on to the end' recurring function, reusing an existing variable
		}
		temp.add (LineDasm[1], LineDasm[2]); 	//Add the line (or prolongued line) to the CDS block we're currently working on.
		
		if (!LineDasm[1].equals("translation"))		//while it's not passed the end of the CDS block,
		{
			temp = Read(in, temp);					//keep reading
		}
		return temp;							//exit giving the "fully filled out" temp block
    }

	private static String TagOn (BufferedReader in, String tagline) 			//Recurring function tagon - if the next line needs to be tagged on
    {
		String Nextline = ReadLN.read(in);
		
		
		if (Nextline.substring(Nextline.length()-1, Nextline.length()).equals("\""))  //check if THIS is the last line, ie: look for a ' " '
		{
			tagline += Nextline.substring(21, Nextline.length()-1);
			return tagline;        	
		}
		else 																		//, if not...
		{
			tagline += Nextline.substring(21, Nextline.length());
			return TagOn(in, tagline);
		}
    }
	
	
    private static void ProcDNA(BufferedReader in, CDS_list list, int CDScount, boolean guiUse, Main.Main instance)
    {
	String Nextline = new String();
		
	ArrayList<String> tagline = new ArrayList<String>();  // ONLY AVAILABLE 1.5 AND LATER!!!
        tagline.add("");
        ArrayList<Boolean> tagon = new ArrayList<Boolean>();  // Boolean ONLY AVAILABLE 1.5 AND LATER!!!
        tagon.add(false);
        ArrayList<Boolean> tagend = new ArrayList<Boolean>();  // Boolean ONLY AVAILABLE 1.5 AND LATER!!!
        tagend.add(false);
        
        int tagmod = 0; // Effectively tagon.length... for now - save some cpu by using one variable *shrug*
        int readgene = 0;
		
        while (!((Nextline = ReadLN.read(in)).equals("//")) && (!guiUse||!instance.worker.isCancelled())) 
        {													//while it's not the end of the file (according to genbank format)...
        	if (tagmod > 10) System.exit(-1);                                                               //safety exit in case of a large overlap value (usually error)
        	
                //else if (tagmod > 2) System.out.print ("\n\r" + readgene + " | Overlap = " + tagmod);         //bacup in case overlap goes over 2, to make it obvious, and easier to bugfix
    		
                else if (guiUse)
                { 
                    instance.setinter2(readgene, tagmod);
                }
                else
                {
                    System.out.print ("\r" + readgene + " | Overlap = " + tagmod);				// Alerts and warnings
                }

        	//////////////////////////// Pre-processing of line ///////////////////////////////
        	String delimiters = "[ ]+";
        	String[] LineDasm = Nextline.split(delimiters);
        	String LineReAsm = "";
        	
        	for (int i = 2; i<LineDasm.length; i++){
    			LineReAsm += LineDasm[i];
    		}

        	int linecount=0;
        	for (int i=0; i<Nextline.length(); i++)
        	{
        		if (Nextline.charAt(i)=='a' || Nextline.charAt(i)=='t' || Nextline.charAt(i)=='g' || Nextline.charAt(i)=='c' || Nextline.charAt(i)=='u')
        			linecount++;
        	}
        	
        	/////////////////////  Add new CDS region to stack when found  ////////////////////////////////

    		if (readgene < CDScount)			//safety handle to look up
    		{
    			while ((list.at(readgene).start) < (Integer.parseInt( LineDasm[1] ) + linecount) && (!guiUse||!instance.worker.isCancelled())) //Look for new starts on this line
    			{
    																			// move up "number of CDS started reading" state
    					tagmod++; 																// Dynamic array length?
    					tagon.add(true);													//add to end of stack
    					tagend.add(false);
    					tagline.add("");													//add to end of stack
    					tagline.set(tagmod-1, LineReAsm.substring(		(list.at(readgene).start) - (Integer.parseInt( LineDasm[1] ))		, LineReAsm.length()));
        				readgene++; 
        				if (!(readgene < CDScount))	//safety handle to look up again if readgene's increased
        				{
        					break;
        				}
    			}
    		}
    		////////////////////// If there's something to read... //////////////////////////
    		
        	if (tagmod > 0)
        	{
        		/////////////////// Check if a CDS region ends on this line //////////////////
        		//tagend.clear();
        		for (int i = 0; i<tagmod;i++)			//rebuild tagend
        		{
        			if (tagon.get(i) == true)
        			{
        			if (list.at(readgene-tagmod+i).end >= Integer.parseInt( LineDasm[1] ) && list.at(readgene-tagmod+i).end < Integer.parseInt( LineDasm[1] ) + linecount)	//Check which overlap lines are ending
        				tagend.set(i, true);					// Check if stack number (i) ends on this line.
        			else tagend.set(i, false);
        			}
        		}
        		
        		/////////////////////////// Tag on as necessary //////////////////////////////
        		
        		for (int i = 0; i<tagmod;i++)
        		{					//processing - tag on as necessary
        			if (!tagend.get(i))								//If it's not the end of the tag,
        			{
        				if(tagon.get(i))							//check that this line actually needs to be tagged on.
        					tagline.set(i, tagline.get(i) + LineReAsm);
        			}
        			else											//Otherwise tag on, and close up.
        			{
        				tagline.set(i, tagline.get(i) + LineReAsm.substring(0,    (list.at(readgene-tagmod+i).end) - (Integer.parseInt( LineDasm[1] )) + 1    )); //Read from start, then read to, this is all -1 because readgene's already been ++ed
        				list.at(readgene-tagmod+i).DNA_norm = tagline.get(i);
        				tagon.set(i, false);
        				tagend.set(i, false);
        			}
        		}
        		
        		//////////////////////// Remove from stack as necessary //////////////////////////
        		
        		while((tagon.get(0) == false) && (tagmod > 0)) 			//while blank spaces at the start - POSTprocessing
        		{
        			tagon.remove(0); //remove from bottom of stack
        			tagline.remove(0);
        			tagend.remove(0);
        			tagmod--;
        		}
        	}
        	////////////////////////// If it's past all the reading to be done, check for labelled end ///////////////////
        }	// endwhile
                if (guiUse)
                { 
                    //
                }
                else System.out.print ("\n\rDone!");
    }

        public static ArrayList<String> run(String FileName, String args1, String args2, String FolderName, boolean guiUse, Main.Main instance)
	{
            
            
            CDS_list list = new CDS_list();

            //System.out.print("\n\r " + args[0] + " " + start + " " + end + " " + FolderName);
            int start = 0;
            int end = -1; // negatives = end of file.
            if (!args1.equals("start"))
                start = Integer.parseInt(args1);
            if (!args2.equals("end"))
                end = Integer.parseInt(args2);
            
                    //TODO : Try/catch if fail to convert to integers
            
            
            try
            {

                //FileInputStream fstream = new FileInputStream(args[0]);			// Open the file that is the first command line parameter
                            //DataInputStream in = new DataInputStream(fstream); 				//Convert our input stream to a DataInputStream

                BufferedReader in = new BufferedReader(new FileReader(FileName)); // original method was deprecated.

                File dir = new File(FolderName);
                dir.mkdir();	

                int CDScount = ProcTillOrig(in, list, start, end, guiUse, instance);
                ProcDNA(in, list, CDScount, guiUse, instance);
                if((!guiUse||!instance.worker.isCancelled())) // fast track code, and stop annoying popups - using the !guiUse detection first, because then it doesn't check a null value
                    list.printList(FolderName, guiUse, instance); //OH DEAR
                //instance.dispose();
                in.close(); // Close input stream
            } 
            catch (Exception e)
            {
                            System.err.println("File input error: " + e);
            }
            
            if(guiUse) return list.names();
            else return new ArrayList<String>(); // save on computing time, and confusion
            
        }
	
	public static void main(String args[])
	{
            	//System.out.println(args.length);
		
            String start = "start";
            String end = "end";
            String FolderName = "CDS_Folder";	//Where we're going to store all the CDS files
            Main.Main instance = null;

            switch (args.length)
            {
            default: 
                    System.err.print("\nIncorrect arguments. For more help, launch the class with no tags.");
                    System.exit(-1);
            case 2:
                    System.err.print("\nThe Program didn't understand what you're trying to do. The standard form is \"filename_to_scan start end foldername_to_output_to\". The start and end can be numbers, or the words start and end to signify which places in the file you want to scan.");
                    System.exit(-1);
            case 0:
                    System.err.print("\nYou have to input AT LEAST the file name from which to read, including the extension eg: \"sequence.gb\", without the quotation marks.\n\n\rThe standard form is \"filename_to_scan start end foldername_to_output_to\". The start and end can be numbers, or the words start and end to signify which places in the file you want to scan.");
                    System.exit(-1);
            case 4: 
                    //File_read_name = args[0];
                    //start = args[1];
                    //end = args[2];
                    FolderName = args[3]; // Need to make a var, otherwise args[3] would give an error if the args were less than 4
            case 3:	
                    //File_read_name = args[0];
                    start = args[1];
                    end = args[2];
                    //FolderName = DEFAULT;
            case 1:	
                    //File_read_name = args[0]; - don't need to declare
                    //start = DEFAULT;
                    //end = DEFAULT;
                    //FolderName = DEFAULT;
            }            
        
            run(args[0],start,end,FolderName, false, instance); // more visible what we're doing rather than sending []args through
	} // End main
} // End class Parser