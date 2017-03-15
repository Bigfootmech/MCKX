/*
 * OPTIONAL: More refined search pattern/better placement for existing CDS blocks
 * GRRRRRR Make option work straight away rather than after 2 goes <------- odd bug, can't seem to fix :S
 * Add to existing entries with the same genecode?
 * Possible error here OR somewhere else, first? genecode not showing up :S
 * Error for not able to write to file????
*/

package Parser;

import java.io.BufferedReader;
//import java.io.DataInputStream;
//import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.File;
import java.util.ArrayList; 


public class CDS_block {
	public int start;
	public int end;
	public String organism;
	public boolean complement;
	public String gene;
	public String locus_tag;
	public String EC_number;
	public String note;
	public int codon_start;
	public int transl_table;
	public String product;
	public String protein_id;
	public String db_xref;
	public String translation;
	public String DNA_norm;
	public CDS_block nextblock;
	
	public CDS_block() 			//initializer / constructor
	{
        start = -1;
        end = -1;
        complement = false;
        gene = "";
    	locus_tag = "";
    	EC_number ="";
    	note = "";
    	codon_start = -1;
    	transl_table = -1;
    	product = "";
    	protein_id = "";
    	db_xref = "";
    	translation = "";
    	DNA_norm = "";
    	nextblock = null;
	}	
	
	public String question() 							//if Complement, reverse the DNA_Norm
	{
		String genecode = DNA_norm; // default genecode = DNA_norm (the way it is in the file
		if (complement){            //unless it's the complement
			genecode = new StringBuffer(DNA_norm).reverse().toString();
		}
		//else genecode = DNA_norm;
		return genecode;
	}	
	
	
	public void add (String loc, String var) {
		if (loc.equals("gene")){						//Looks inelegant, but is basic and purpose built, "safe", and very compatible.
    		gene =  var ; 
    	}else if (loc.equals("locus_tag")){
    		locus_tag =  var ; 
    	}else if (loc.equals("EC_number")){
    		EC_number =  var ; 
    	}else if (loc.equals("note")){
    		note =  var;
    	}else if (loc.equals("codon_start")){
    		codon_start = Integer.parseInt( var ); 
    	}else if (loc.equals("transl_table")){
    		transl_table = Integer.parseInt( var ); 
    	}else if (loc.equals("product")){
    		product =  var ; 
    	}else if (loc.equals("protein_id")){
    		protein_id = var; 
    	}else if (loc.equals("db_xref")){
    		db_xref = var; 
    	}else if (loc.equals("translation")){
    		translation = var; 
    	}else {																	//if it's none of them
    		System.err.println("The condition " + loc + " isn't accounted for, please tell the developer(s).");
    	}
	}
	public void equals (CDS_block temp) {
        	start = temp.start;
        	end = temp.end;
        	complement = temp.complement;
    		gene =  temp.gene ; 
    		locus_tag =  temp.locus_tag ; 
    		EC_number =  temp.EC_number ; 
    		note =  temp.note;
    		codon_start = temp.codon_start; 
    		transl_table = temp.transl_table; 
    		product =  temp.product ; 
    		protein_id = temp.protein_id; 
    		db_xref = temp.db_xref; 
    		translation = temp.translation;
    		DNA_norm = temp.DNA_norm;
    		nextblock = temp.nextblock;
    		
	}
	
	public String printClass (String FolderName, String option, boolean guiUse, Main.Main instance){
		//check for existing.
		Boolean loop=false;
		File file=new File(FolderName + "/" + protein_id +".txt");
		if (!file.exists()) {
			RawOutput(file);
		}
		else {
			if (option.equals("na"))                //not logical order, but "order of least work"
			{
			}
			else if (option.equals("owa"))
			{
				RawOutput(file);
			}
			else if (option.equals("ya"))
			{
				ReadWrite(file);
			}
			else
			{
				do
				{
					try 
					{       //check for main initialization?
						
                                                if (guiUse)
                                                {
                                                    option = instance.optionSelect(FolderName, protein_id);
                                                }
                                                else
                                                {
                                                    InputStreamReader inread = new InputStreamReader(System.in);
                                                    BufferedReader inbuff = new BufferedReader(inread);
                                                    System.out.println( "\n\r File: " + FolderName + "/" + protein_id +".txt" + " already exists. Would you like to attempt to add to it? y = yes, n = no, ya = yes to all, na = no to all, ow = overwrite, or owa = overwrite all" );
                                                    option = inbuff.readLine();
                                                }
                                                //option = Main.Main.getOptions();
                                                
                                                
                                                
                                                
						loop = false;
						/*switch(line){ //Would be nice to use, but requires 1.7 :/
						
						}*/
                                                if (option.equals("n") || option.equals("na"))
						{
						}
						else if (option.equals("ow") || option.equals("owa"))
						{
							RawOutput(file);
						}
                                                else if (option.equals("y") || option.equals("ya"))
						{
							ReadWrite(file);
						}
						else{
							System.out.println("\n\rYou have Enterd : \"" + option + "\" that is not a valid selection.");
							loop = true;
						}
						
					} 
					catch (IOException e) 
					{
						e.printStackTrace();
					}
					
					
				} while(loop);
			}
			
		}
		return option;
	
	
	
	}

	public void RawOutput (File file){
		try {
			FileOutputStream prtCDS = new FileOutputStream (file);
			new PrintStream(prtCDS).println ("***");
			new PrintStream(prtCDS).println ("protein_id:");
			new PrintStream(prtCDS).println (protein_id);
			new PrintStream(prtCDS).println ("translation:");
			new PrintStream(prtCDS).println (translation);
			new PrintStream(prtCDS).println ("***");
			new PrintStream(prtCDS).println ("Optimizations");
			new PrintStream(prtCDS).println ("***");
			new PrintStream(prtCDS).println ("***");
			new PrintStream(prtCDS).println ("Originals");
			new PrintStream(prtCDS).println ("***");
			new PrintStream(prtCDS).println ("locus_tag:");
			new PrintStream(prtCDS).println (locus_tag);
			new PrintStream(prtCDS).println ("ORGANISM:");
			new PrintStream(prtCDS).println (organism);
			new PrintStream(prtCDS).println ("gene:");
			new PrintStream(prtCDS).println (gene);
			new PrintStream(prtCDS).println ("note:");
			new PrintStream(prtCDS).println (note);
			new PrintStream(prtCDS).println ("codon_start:");
			new PrintStream(prtCDS).println (codon_start);
			new PrintStream(prtCDS).println ("transl_table:");
			new PrintStream(prtCDS).println (transl_table);
			new PrintStream(prtCDS).println ("product:");
			new PrintStream(prtCDS).println (product);
			new PrintStream(prtCDS).println ("db_xref:");
			new PrintStream(prtCDS).println (db_xref);
			new PrintStream(prtCDS).println ("genecode:");
			new PrintStream(prtCDS).println (question());
			new PrintStream(prtCDS).println ("***");
			new PrintStream(prtCDS).println ("End");
			new PrintStream(prtCDS).println ("***");
			prtCDS.close(); // Close  output stream
		}
		catch (Exception e) 																	// Catches any error conditions
		{
			System.err.println ("Unable to write to file " + "CDS_" + locus_tag + ".txt");
			//System.exit(-1);
		}
				
	}
	public void ReadWrite (File file){
		String Line = "";
		String Line2 = "";
		String Line3 = "";
		String Line4 = "";
		boolean There = false;
		
		ArrayList<String> filetowrite = new ArrayList<String>();
		int Section = 0;
		try 
		{
			//String compname = FolderName + "/" + protein_id +".txt";
			//FileInputStream fstream  = new FileInputStream( file );
			//DataInputStream in = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new FileReader(file)); // original method was deprecated.
//////////////////////////////////
			while (Section < 3){
				Line = ReadLN.read(in);
				if(!There)
				{
					if (Line.equals("***"))
					{
						Line2 = ReadLN.read(in);
						if (Line2.equals("End"))
						{
							Section = 4;
							filetowrite.add(Line);
							filetowrite.add ("locus_tag:");
							filetowrite.add (locus_tag);
							filetowrite.add ("ORGANISM:");
							filetowrite.add (organism);
							filetowrite.add ("gene");
							filetowrite.add (gene);
							filetowrite.add ("note");
							filetowrite.add (note);
							filetowrite.add  ("codon_start:");
							filetowrite.add (String.valueOf(codon_start));
							filetowrite.add  ("transl_table:");
							filetowrite.add (String.valueOf(transl_table)); 
							filetowrite.add ("product:");
							filetowrite.add (product);
							filetowrite.add ("db_xref:");
							filetowrite.add (db_xref);
							filetowrite.add ("genecode:");
							filetowrite.add (question());
							filetowrite.add(Line);
							filetowrite.add(Line2);
							filetowrite.add(Line);
						}
						//else if (Line2.equals("locus_tag:"))
						else if (Line2.equals("genecode:"))
						{
							Line3 = ReadLN.read(in);
							//if (Line3.equals(locus_tag))
							if (Line3.equals(question()))
							{
								There = true;
							}
							filetowrite.add(Line);
							filetowrite.add(Line2);
							filetowrite.add(Line3);
						}
						else if (Line2.equals("***"))
						{
							Line3 = ReadLN.read(in);
							if (Line3.equals("End"))
							{
								Section = 4;
								filetowrite.add(Line);
								filetowrite.add ("locus_tag:");
								filetowrite.add (locus_tag);
								filetowrite.add ("ORGANISM:");
								filetowrite.add (organism);
								filetowrite.add ("gene");
								filetowrite.add (gene);
								filetowrite.add ("note");
								filetowrite.add (note);
								filetowrite.add  ("codon_start:");
								filetowrite.add (String.valueOf(codon_start));
								filetowrite.add  ("transl_table:");
								filetowrite.add (String.valueOf(transl_table)); 
								filetowrite.add ("product:");
								filetowrite.add (product);
								filetowrite.add ("db_xref:");
								filetowrite.add (db_xref);
								filetowrite.add ("genecode:");
								filetowrite.add (question());
								filetowrite.add(Line);
								filetowrite.add(Line2);
								filetowrite.add(Line3);
								filetowrite.add(Line);
							}
							//else if (Line3.equals("locus_tag:"))
							else if (Line3.equals("genecode:"))
							{
								Line4 = ReadLN.read(in);
								//if (Line4.equals(locus_tag))
								if (Line4.equals(question()))
								{
									There = true;
								}
								filetowrite.add(Line2);
								filetowrite.add(Line3);
								filetowrite.add(Line4);
							}
							else if (Line3.equals("Optimizations")||Line3.equals("Originals"))
							{
									filetowrite.add(Line);
									filetowrite.add(Line2);
									filetowrite.add(Line3);
							}
							else System.out.println("Something else has come up after a *** ***. Ask for help from your Admin/developer");
						}
						else 
						{
							filetowrite.add(Line); 
							filetowrite.add(Line2); 
						}
					}
					else 
					{
						filetowrite.add(Line); 
					}
				}
				else if (Line.equals("End"))
				{
					Section = 4;
					filetowrite.add(Line);
					filetowrite.add("***");
				}
				
				else filetowrite.add(Line);
				//System.out.println("\n\r " + filetowrite + "\n\r");  //(filetowrite.get(filetowrite.size()-1));
				
			}
			
			
			
			////////////////////////////////////////////////////////////////
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
		
		
		try {
			FileOutputStream prtCDS = new FileOutputStream (file);
			for (int i = 0; i<filetowrite.size(); i++){
				new PrintStream(prtCDS).println (filetowrite.get(i));
			}
			prtCDS.close(); // Close  output stream
		}
		catch (Exception e) 																	// Catches any error conditions
		{
			System.err.println ("Unable to write to file " + "CDS_" + locus_tag + ".txt");
			//System.exit(-1);
		}

	}
}