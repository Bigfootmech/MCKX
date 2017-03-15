package Parser;

import java.util.ArrayList;


public class CDS_list {
	private CDS_block head;

	public CDS_list(){
		head = null;
	}
	
	public int length (){
		if (head == null)
			return -1;				//if the list is empty, return -1 for the length
		int count = 1;
		CDS_block lookup = head;
		while(lookup.nextblock != null){
			lookup = lookup.nextblock;
			count++;
		}
		return count;
	}
	
	public CDS_block last(){
		CDS_block lookup = head;
		if (head == null) 
		{
			System.err.print ("\n\r The head is a null");
			return head;
		}
		while (lookup.nextblock != null)
		{
			lookup = lookup.nextblock;
		}
		return lookup;
	}
	
	public CDS_block at (int a){
		CDS_block lookup = head;
		
		if (a<0)
		{ 
			System.err.println("The lookup number cannot be negative!");
			return null;
		}
			
		while (a>0){
			lookup = lookup.nextblock;
			a--;
		}
			
		
		return lookup;
	}
	

	
	public void insertHead (CDS_block temp){
		temp.nextblock = head;
		head = temp;
	}

	public void insertEnd (CDS_block temp){
		if (head == null) 
		{
			temp.nextblock = head;
			head = temp;
		}
		else 
			last().nextblock = temp;
	}
	
	public void insertAt (CDS_block temp, int place){
		temp.nextblock = at(place + 1);
		at(place-1).nextblock = temp;
	}
	
	public void deleteAt (int place){
		at(place-1).nextblock = at(place+1);
	}
	
	public void deleteHead (){
		head = head.nextblock;
	}	
	
	public void deleteEnd (){
		last().nextblock = null;
	}
	/*
	public void printAt (int place, String FolderName){
		String option = "";
		at(place).printClass(FolderName, option);
	}
	*/
	/* Print FromTo ?? */
	
	public void printList (String FolderName, boolean guiUse, Main.Main instance){
		
                if (guiUse)
                { 
                    instance.settext("Outputting CDS Files", "");
                }
                else System.out.print("\n\rOutputting CDS Files\n");
		int count=1;
		if (head == null) 
		{
			System.err.println("There were no CDS regions in the list.");
			System.exit(-1);
		}
		CDS_block lookup = head;
		String option = "";
		do{
			option = lookup.printClass(FolderName, option, guiUse, instance);	//print the list
			if (guiUse)
                        { 
                            instance.setinter3(count++);
                        }
                        else System.out.print("\r" + count++);
                        lookup = lookup.nextblock;
		}while(lookup != null && (!guiUse||!instance.worker.isCancelled()));
	}
        
        public ArrayList<String> names (){
                ArrayList<String> Strings = new ArrayList<String>();
		System.out.print("\n\rPassing back file names\n");
		int count=1;
		if (head == null) 
		{
			System.err.println("There were no CDS regions in the list.");
			System.exit(-1);
		}
		CDS_block lookup = head;
                Strings.add(lookup.protein_id);
		//System.out.print("\r" + count);
		while(lookup.nextblock != null){
			lookup = lookup.nextblock;
			Strings.add(lookup.protein_id);
                        ++count;
			//System.out.print("\r" + count);
		}
                return Strings;
	}

	
}
