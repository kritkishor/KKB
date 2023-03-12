package minesweeper;
import java.util.Random;
import java.util.Scanner;
// Class to create a basic agent
public class Basic_Agent {
	// Knowledge Base (declaration):
	boolean isIdentified[][]; // stores 'true' if the location is visited, 'false' otherwise
	boolean isMine[][]; // stores 'true' if the location is a mine, 'false' otherwise
	boolean isSafe[][]; // stores 'true' if the location is safe, 'false' otherwise
	int clue[][]; // stores the clue(number of neighboring mines) after the location is safely accessed
	int safe_neighbors[][]; // stores the number of safe neighbors for a location
	int identified_mines[][]; // stores the number of identified neighboring mines around a location
	int unexplored_neighbors[][]; // stores the number of unexplored/unvisited neighbors around a location
	int neighbors[][]; // total number of neighbors of a location
	int count; // No of locations explored
	boolean isComplete[][]; // Stores if all the neighbors of a location has been explored or not (UNEXPLORED_NEIGHBORS = 0)
//	boolean isMine[][];
	int neighboring_mines[][]; // Stores the number of neighboring mines at a location for easy access
	int random_locations;    // Keeping track of how many locations were visited randomly
	// Conditions given in write up
	boolean constraint1; // true if total mines(clue) - revealed mines = unexplored mines
	boolean constraint2; // true if true if neighbors - clue - revealed safe neighbors = unexplored mines
	
	int dim; // dimension of the board
	Random random_generator; // To get instance of random generator class
	
	int explosions; // KEEPS TRACK OF THE EXPLOSIONS
	int safely_identified; // Keeps track of SAFELY IDENTIFIED mines
	
	// Constructor for the basic agent: takes the dimension and number of mines as parameters
	public Basic_Agent(int d, int n) {
		
		// KB (initialization):
		
		isComplete = new boolean[d][d];                    
		random_generator = new Random();
		dim = d;
		
		isIdentified = new boolean[d][d];

		isSafe = new boolean[d][d];
		clue = new int[d][d];
		safe_neighbors = new int[d][d];
		identified_mines = new int[d][d];
		unexplored_neighbors = new int[d][d];
		neighbors = new int[d][d];
		random_locations = 0;
		isMine = new boolean[d][d];
		neighboring_mines = new int[d][d];
		
		// Generate a board/table for basic agent to solve (with dimensions & number of mines):
		MineSweeper basic_agent = new MineSweeper(d,n);
		
// Copy all the required information to the knowledge base, Some information are not directly accessed by the program but stored for avoiding tedious static reference
		neighbors = basic_agent.neighbors;
		isMine = basic_agent.isMine; // LIke this one (is not available to the program unless we reveal a location)
		neighboring_mines = basic_agent.neighboring_mines; // and this one too
		//Initialization:
		explosions = 0;   
		safely_identified = 0;
		
		// Unexplored neighbors are all neighbors at START
		for(int row = 0; row<d; row++)
		{
			for(int col =0; col<d; col++)
			{
				unexplored_neighbors[row][col] = neighbors[row][col];
			}
		}
	}
	
	
	// This method updates the knowledge base, i.e. updates each variable of a location in the knowledge base with revealed information
	public boolean update_variables(int row,int col)
	{
		// if we have revealed everything, or if a location has been completely explored(all neighbors explored) or if it is a mine, no need to keep information anymore
		if((count == dim*dim) || isComplete[row][col]  || !isSafe[row][col])
		{
			return false;
		}
		
		// get the number of unexplored neighbors for variables
		int prev = unexplored_neighbors[row][col];
		
		if(isSafe[row][col] && !isComplete[row][col] )
		{
			// printing is done to see step by step progression
			System.out.println("UPDATING VARIABLES OF: " + " [ " + row + " , " + col + " ]");
				
			
			//RESET everything and update 
				unexplored_neighbors[row][col] = 0;
				safe_neighbors[row][col] = 0;
				identified_mines[row][col] = 0;	
			// Traverse through the existing neighbors of location,  check if it is safe, if safe, u
			if((row-1)>=0)  //if UP exists
			
			 {
				
				 if(!isIdentified[row-1][col]) //if a neighbor has not been identified,
				 {
					
					 unexplored_neighbors[row][col]++;  // Increment the number of unexplored neighbors
				 }
				 else  // If the neighbor has been identified
				 { System.out.println("IS  EXPLORED: " + " [ " + (row-1) + " , " + col + " ]");       // Just to see if the program is making healthy choices when checking constraints
					 if(isSafe[row-1][col])       // If the neighbor is safe, increment the number of safe neighbors
					 {
						 safe_neighbors[row][col]++; 
					 }
					 else  // If not safe, increment the number of identified mines
					 {
						 identified_mines[row][col]++;
					 }
				 }
				 
			 }
			 // Same process for every existing neighbor
			 if((row+1)<dim) // if DOWN exists
			 {
				 
				 if(!isIdentified[row+1][col])
				 {
					 unexplored_neighbors[row][col]++;
				 }
				 else
				 {
					 System.out.println("IS  EXPLORED: " + " [ " + (row+1) + " , " + col + " ]");
					 if(isSafe[row+1][col])
					 {
						 safe_neighbors[row][col]++;
					 }
					 else
					 {
						 identified_mines[row][col]++; 
					 }
				 }
			 }
				
			 if((col-1)>=0 )//LEFT
			 {
				 
				 if(!isIdentified[row][col-1])
				 {
					 unexplored_neighbors[row][col]++;
				 }
				 else
				 {
					 System.out.println("IS  EXPLORED: " + " [ " + (row) + " , " + (col-1) + " ]");
					 if(isSafe[row][col-1])
					 {
						 safe_neighbors[row][col]++;
					 }
					 
					 else
					 {
						 identified_mines[row][col]++;
					 }
				 }
			 }
			 if((col+1)<dim ) //RIGHT
			 {
				 
				 if(!isIdentified[row][col+1])
				 {
					 unexplored_neighbors[row][col]++;
				 }
				 else
				 {
					 System.out.println("IS  EXPLORED: " + " [ " + (row) + " , " + (col+1) + " ]");
					 if(isSafe[row][col+1])
					 {
						 safe_neighbors[row][col]++;
					 }
				
					 else
					 {
						 identified_mines[row][col]++;
					 }
				 }
			 }
			 if ( ((row+1)<dim) && ((col-1)>=0) )  // DOWN LEFT
			 {
				 
				 if(!isIdentified[row+1][col-1])
				 {
					 unexplored_neighbors[row][col]++;
				 }
				 else
				 {
					 System.out.println("IS  EXPLORED: " + " [ " + (row+1) + " , " + (col-1) + " ]");
					 if(isSafe[row+1][col-1])
					 {
						 safe_neighbors[row][col]++;
					 }
					 else
					 {
						 identified_mines[row][col]++;
					 }
				 }
			 }
			 if(((row+1)<dim) && ((col+1)<dim) ) // DOWN RIGHT
			 {
				 
				if(!isIdentified[row+1][col+1])
				{
					unexplored_neighbors[row][col]++;
				}
				else
				{
					 System.out.println("IS  EXPLORED: " + " [ " + (row+1) + " , " + (col+1) + " ]");
					if(isSafe[row+1][col+1])
					{
						safe_neighbors[row][col]++;
					}
					else
					{
						identified_mines[row][col]++;
					}
				}
			 }
			 if( ((row-1)>=0) && ((col-1)>=0) ) // UPPER LEFT
			 {
				
				 if(!isIdentified[row-1][col-1])
				 {
					 unexplored_neighbors[row][col]++;
				 }
				 else
				 {
					 System.out.println("IS  EXPLORED: " + " [ " + (row-1) + " , " + (col-1) + " ]");
					 if(isSafe[row-1][col-1])
					 {
						 safe_neighbors[row][col]++;
					 }
					 else
					 {
						 identified_mines[row][col]++;
					 }
				 }
			 }
			 if( ((row-1)>=0) && ((col+1)<dim) ) // UPPER RIGHT
			 {
				 
				 if(!isIdentified[row-1][col+1])
				 {
					 unexplored_neighbors[row][col]++;
				 }
				 else
				 {
					 System.out.println("IS  EXPLORED: " + " [ " + (row-1) + " , " + (col+1) + " ]");
					 if(isSafe[row-1][col+1])
					 {
						 safe_neighbors[row][col]++;
					 }
					 else
					 {
						 identified_mines[row][col]++;
					 }
				 }
			 }
				
			 
				}
		System.out.println("UNEXPLORED: " + unexplored_neighbors[row][col]); // BOOK KEEPING
		System.out.println("SAFE: " + safe_neighbors[row][col]); // BOOK KEEPING
		
		// At this point,
		// If we find that the neighbor is complete mark it complete
		if(unexplored_neighbors[row][col] == 0)
		{
			isComplete[row][col] = true;
		}
		
		// If we see that it is incomplete, and additional information was obtained by this process,
		// return true
		if(!isComplete[row][col] && (prev != unexplored_neighbors[row][col]))
		{
			return true;
		}
		return false;
		
	
		
	
	}
	
	
	//This method updates the neighborhood of a location,
	// It updates all the neighbors once we get the reveal new relevant information about that location 
	public boolean update_neighborhood(int row,int col)
	{
		boolean retval;    // returns true if their was actually an update
		int ct = 0; // counter to help retval
		
		
		// If a location exists, is not a mine (safe) and is not yet complete.
		if(((row-1)>=0) && isSafe[row-1][col] && !isComplete[row-1][col])  //if UP exists
			
		 {
			
			System.out.println("UPDATING UP OF: " + "[ " + row + " , " + col + " ]");  // BOOK KEEPING
			retval = update_variables(row-1,col);  // TRY TO UPDATE THE VARIABLES for this location and see if we had gotten new information

			
			// If we got new informations, we might be able to satisfy the constraints!
			if(retval)
 			{
				check_constraints(row-1,col);
				ct++;
			}
			
		 }
		 
		// SAME PROCESS for all existing neighbors
		 if(((row+1)<dim) && isSafe[row+1][col] && !isComplete[row+1][col]) // if DOWN exists
		 {
			 System.out.println("UPDATING DOWN OF: " + "[ " + row + " , " + col + " ]");
			retval = update_variables(row+1,col);
			if(retval)
			{
				check_constraints(row+1,col);
				ct++;
			}
			
		 }
			
		 if(((col-1)>=0 ) && isSafe[row][col-1] && !isComplete[row][col-1])//LEFT
		 {
			 System.out.println("UPDATING LEFT OF: " + "[ " + row + " , " + col + " ]");
			 retval = update_variables(row,col-1);
			if(retval)
			{
				check_constraints(row,col-1);
				ct++;
			}
			
		 }
		 if(((col+1)<dim ) && isSafe[row][col+1] && !isComplete[row][col+1]) //RIGHT
		 {
			 System.out.println("UPDATING RIGHT OF: " + "[ " + row + " , " + col + " ]");
			 retval = update_variables(row,col+1);
			 if(retval)
			 {
				 check_constraints(row,col+1);
				 ct++;
			 }
		 }
		 if( ( ((row+1)<dim) && ((col-1)>0) ) && isSafe[row+1][col-1] && !isComplete[row+1][col-1])  // DOWN LEFT
		 {
			 System.out.println("UPDATING DOWN LEFT OF: " + "[ " + row + " , " + col + " ]");
			  retval = update_variables(row+1,col-1);
			  if(retval)
			  {
				  check_constraints(row+1,col-1);
				  ct++;
			  }
		 }
		 if((((row+1)<dim) && ((col+1)<dim) ) && isSafe[row+1][col+1] && !isComplete[row+1][col+1]) // DOWN RIGHT
		 {
			 System.out.println("UPDATING DOWN RIGHT OF: " + "[ " + row + " , " + col + " ]");
			 retval = update_variables(row+1,col+1);
			 if(retval)
			 {
				 check_constraints(row+1,col+1);
				 ct++;
			 }
			
		 }
		 if(( ((row-1)>=0) && ((col-1)>=0) ) && isSafe[row-1][col-1] && !isComplete[row-1][col-1]) // UPPER LEFT
		 {
			 System.out.println("UPDATING UPPER LEFT OF: " + "[ " + row + " , " + col + " ]");
			 retval = update_variables(row-1,col-1);
			 if(retval)
			 {
				 check_constraints(row-1,col-1);
				 ct++;
			 }
			
		 }
		 if( (((row-1)>=0) && ((col+1)<dim) ) && isSafe[row-1][col+1] && !isComplete[row-1][col+1]) // UPPER RIGHT
		 {
			 System.out.println("UPDATING UPPER RIGHT OF: " + "[ " + row + " , " + col + " ]");
			 retval = update_variables(row-1,col+1);
			if(retval)
			{
				check_constraints(row-1,col+1);
				ct++;
			}
		 }
		 
		 // if we were able to make any new changes in the neighborhood (satisfy constraints), return TRUE
		 if(ct > 0)
		 {
			 return true;
		 }
		 // Otherwise, the update was not worth it :(
		 return false;
		
	}
	
	
	// This method explores/reveals a location,
	public void explore_location(int row, int col)
	{
		
		System.out.println("EXPLORE AT: " + "[ " + row + " , " + col + " ]"); // BOOK KEEPING
		
		
		// If it is a new/unidentified location
		if(!isIdentified[row][col])
		{
			System.out.println("NOT YET IDENTIFIED: " + "[ " + row + " , " + col + " ]");
			isIdentified[row][col] = true; // Now this location is explored/identified
			count++;
			// If this location is not a mine, set it safe, and get the clue:
			if(!isMine[row][col])
			{
				System.out.println("SAFE AT: " + "[ " + row + " , " + col + " ]");
				isSafe[row][col] = true;
				clue[row][col] = neighboring_mines[row][col];
				
			}
		 
			// if it is a mine, set it unsafe
			else
			{
				System.out.println("EXPLOSION AT " + "[ " + row + " , " + col + " ]");
				explosions++;
				isSafe[row][col] = false;
			}
			// GIVE THIS INFORMATION TO ITS NEIGHBORS!
			 update_neighborhood(row,col);
		}
			
			
		
		
		
		
		// IF WE HAVE EXPLORED EVEYTHING, HOORAY! We are done. :)
		if(count == dim*dim)
		{
			return;
		}
		
		
	
		
		 
		
		
	// WE MIGHT HAVE SOME CHANGES AFTER ADDING THIS INFORMATION TO OUR KNOWLEDGE BASE !
		
		if(isSafe[row][col] && !isComplete[row][col])
			{
				 update_variables(row,col);
			}
		
		
		 
			 System.out.println("Neighbors AT:  " + "[ " + row + " , " + col + " ]" + " = " + neighbors[row][col]);
			 System.out.println("Identified Mines AT:  " + "[ " + row + " , " + col + " ]" + " = " + identified_mines[row][col]);
			 System.out.println("Unexplored Neighbors AT:  " + "[ " + row + " , " + col + " ]" + " = " + unexplored_neighbors[row][col]);
			 System.out.println("Safe Neighbors AT:  " + "[ " + row + " , " + col + " ]" + " = " + safe_neighbors[row][col]);
			
			 // CHECK CONSTRAINTS WITH UPDATES!
			
				 	check_constraints(row,col);
				 
			
			 
	
	
	
	}
		
	// THIS METHOD DOES THE CONSTRAINT CHECKING PROCESS
			
	public void check_constraints(int row,int col)
	{
		if(unexplored_neighbors[row][col] == 0)
		{
			return;
		}
		
		constraint1 = false;
		constraint2 = false;
		System.out.println("CHECK CONDN AT: " + "[ " + row + " , " + col + " ]");
		//constraint1
		// clue = clue[row][col]
		// revealed mines = identified_mines[row][col]
		// hidden neighbors = unexplored_neighbors[row][col]
		// number of neighbors = neighbors[row][col]
		// revealed safe neighbors = safe_neighbors[row][col]
		
		//IF A LOCATION IS SAFE AND NOT COMPLETE, CHECK THE CONSTRAINTS THERE, 
		// WE MIGHT GET SOMETHING !!
		 if(isSafe[row][col] && !isComplete[row][col])
		 {
			// System.out.println("clue: " + clue[row][col]);
			 if( (clue[row][col]-identified_mines[row][col]) != (unexplored_neighbors[row][col]) ) 
			 {
				 
				 constraint1 = false;
			 }
			 else {
				 // This is basically the first condition
				if((clue[row][col]==0) || (clue[row][col]==identified_mines[row][col])) { constraint1=false; }
			 	else { 
			 		// JUST BOOK KEEPING TO SEE PLAY BY PLAY PROGRESSION
			 		System.out.println("CONDN1 MET AT: " + "[ " + row + " , " + col + " ]");
			 		 System.out.println("Neighbors AT:  " + "[ " + row + " , " + col + " ]" + " = " + neighbors[row][col]);
					 System.out.println("Identified Mines AT:  " + "[ " + row + " , " + col + " ]" + " = " + identified_mines[row][col]);
					 System.out.println("Unexplored Neighbors AT:  " + "[ " + row + " , " + col + " ]" + " = " + unexplored_neighbors[row][col]);
					 System.out.println("Safe Neighbors AT:  " + "[ " + row + " , " + col + " ]" + " = " + safe_neighbors[row][col]);
			 		// THE CONSTRAINT WAS TRUE!
			 		constraint1 = true; }
			 }
		
			 // IGNORE THIS PLEASE
			 /********************************************************************************************************************************************
			 /*	if((( (neighbors[row][col] - clue[row][col] - safe_neighbors[row][col]) != unexplored_neighbors[row][col])) && (clue[row][col] != 0)) 
			{
				 System.out.println("Neighbors AT:  " + "[ " + row + " , " + col + " ]" + " = " + neighbors[row][col]);
				 System.out.println("Identified Mines AT:  " + "[ " + row + " , " + col + " ]" + " = " + identified_mines[row][col]);
				 System.out.println("Unexplored Neighbors AT:  " + "[ " + row + " , " + col + " ]" + " = " + unexplored_neighbors[row][col]);
				 System.out.println("Safe Neighbors AT:  " + "[ " + row + " , " + col + " ]" + " = " + safe_neighbors[row][col]);
			 constraint2=false; 
		    }
			else {
				System.out.println("CONDN2 MET AT: " + "[ " + row + " , " + col + " ]");
				System.out.println("Neighbors AT:  " + "[ " + row + " , " + col + " ]" + " = " + neighbors[row][col]);
				 System.out.println("Identified Mines AT:  " + "[ " + row + " , " + col + " ]" + " = " + identified_mines[row][col]);
				 System.out.println("Unexplored Neighbors AT:  " + "[ " + row + " , " + col + " ]" + " = " + unexplored_neighbors[row][col]);
				 System.out.println("Safe Neighbors AT:  " + "[ " + row + " , " + col + " ]" + " = " + safe_neighbors[row][col]);
				constraint2 =true; 
				}
				***********************************************************************************************************************************************/
			 // Basically the second condition and book keeping
			 	if((neighbors[row][col] - clue[row][col] - safe_neighbors[row][col]  ) != unexplored_neighbors[row][col] )
			 	{
			 		
			 		constraint2 = false;
			 	}
			 	else
			 	{
			 		System.out.println("CONDN2 MET AT: " + "[ " + row + " , " + col + " ]");
					System.out.println("Neighbors AT:  " + "[ " + row + " , " + col + " ]" + " = " + neighbors[row][col]);
					 System.out.println("Identified Mines AT:  " + "[ " + row + " , " + col + " ]" + " = " + identified_mines[row][col]);
					 System.out.println("Unexplored Neighbors AT:  " + "[ " + row + " , " + col + " ]" + " = " + unexplored_neighbors[row][col]);
					 System.out.println("Safe Neighbors AT:  " + "[ " + row + " , " + col + " ]" + " = " + safe_neighbors[row][col]);
					constraint2 = true; 
			 	}
			 	
		 }
		// OOPSSS WE COULD NOT INFER ANYTHING! :(
		 if((constraint1 == false) && (constraint2 == false))
		 {
			 System.out.println("NO CONDITIONS MET");
		 }
		 // IF WE SATISFIED ANY CONSTRAINT
		 if(constraint1 || constraint2)
			{
			 	
			 	
			 	
		// UPDATE THE KNOWLEDGE BASE:
			 
			 // FOR ALL EXISTING UNIDENTIFIED NEIGHBORS,
			 if((row-1)>=0 && !isIdentified[row-1][col]) //if UP exists and has not yet been explored
				 {
				System.out.println("UPDATING UP OF: " + "[ " + row + " , " + col + " ]");
				unexplored_neighbors[row][col]--;
				//	 if(constraint1 || constraint2) // it is not safe, every hidden neighbor is mine
					// {
					 count++;
						 if(constraint1)  // if the first condition was satisfied
								
							{ 
							 	
							 	isIdentified[row-1][col] = true;  // mark it revealed
								isSafe[row-1][col] = false; // mark it unsafe
								safely_identified++;  // increase the number of safely identified mines
								
							}
						 if(constraint2)  // EVEN BETTER, IF second codition was met,
								
							{ 
							 	
							 	isIdentified[row-1][col] = true;  // mark it identified
								isSafe[row-1][col] = true;  // safe
								clue[row-1][col] = neighboring_mines[row-1][col];  // AND REVEAL THE CLUE!!!!!
							}
					
					 
				 }
			 // Same for every neighbors
				 if((row+1)<dim && !isIdentified[row+1][col] ) // if DOWN exists
				 {
					 System.out.println("UPDATING DOWN OF: " + "[ " + row + " , " + col + " ]");
					 unexplored_neighbors[row][col]--;
			
					 count++;
						 if(constraint1)
								
							{ 
								isIdentified[row+1][col] = true;
								isSafe[row+1][col] = false;
								safely_identified++;
								
							}
						 if(constraint2)
								
							{ 
							 	
							 	isIdentified[row+1][col] = true;
								isSafe[row+1][col] = true;
								clue[row+1][col] = neighboring_mines[row+1][col];
							}
					
				 }
				  if((col-1)>=0 && !isIdentified[row][col-1]) //LEFT
				 {	System.out.println("UPDATING LEFT OF: " + "[ " + row + " , " + col + " ]");
					  count++;
				 unexplored_neighbors[row][col]--;
									  if(constraint1)
								
							{ 
								isIdentified[row][col-1] = true;
								isSafe[row][col-1] = false;
								safely_identified++;
								
							}
						  if(constraint2)
								
							{ 
							 	
							 	isIdentified[row][col-1] = true;
								isSafe[row][col-1] = true;
								clue[row][col-1] = neighboring_mines[row][col-1];
							}
					
				 }
				  if((col+1)<dim && !isIdentified[row][col+1] ) //RIGHT
				 {	System.out.println("UPDATING RIGHT OF: " + "[ " + row + " , " + col + " ]");
					  count++;
				 unexplored_neighbors[row][col]--;
				
						  if(constraint1)
								
							{ 
							  isIdentified[row][col+1] = true;
								isSafe[row][col+1] = false;
								
								safely_identified++;
							}
						  if(constraint2)
								
							{ 
							 	
							 	isIdentified[row][col+1] = true;
								isSafe[row][col+1] = true;
								clue[row][col+1] = neighboring_mines[row][col+1];
							}
						
				 }
				  if((row+1)<dim && (col-1)>=0 && !isIdentified[row+1][col-1] )  // DOWN LEFT
				 {count++;
				 System.out.println("UPDATING DOWN LEFT OF: " + "[ " + row + " , " + col + " ]");
				 unexplored_neighbors[row][col]--;
			
						  if(constraint1)
								
							{ 
								
							  isIdentified[row+1][col-1] = true;
								isSafe[row+1][col-1] = false;
								safely_identified++;
							}
						  if(constraint2)
								
							{ 
							 	
							 	isIdentified[row+1][col-1] = true;
								isSafe[row+1][col-1] = true;
								clue[row+1][col-1] = neighboring_mines[row+1][col-1];
							}
						
				 }
				 if((row+1)<dim && (col+1)<dim && !isIdentified[row+1][col+1]) // DOWN RIGHT
				 {count++;
				 System.out.println("UPDATING DOWN RIGHT OF: " + "[ " + row + " , " + col + " ]");
				 unexplored_neighbors[row][col]--;
			
						 if(constraint1)
								
							{ 
							 isIdentified[row+1][col+1] = true;
								isSafe[row+1][col+1] = false;
								safely_identified++;
							}
						 if(constraint2)
								
							{ 
							 	
							 	isIdentified[row+1][col+1] = true;
								isSafe[row+1][col+1] = true;
								clue[row+1][col+1] = neighboring_mines[row+1][col+1];
							}
					
				 }
				 if((row-1)>=0 && (col-1)>=0 && !isIdentified[row-1][col-1])  // UPPER LEFT
				 {count++;
				 System.out.println("UPDATING UPPER LEFT OF: " + "[ " + row + " , " + col + " ]");
				 unexplored_neighbors[row][col]--;
			
						 if(constraint1)
								
							{ 
								
							 	isIdentified[row-1][col-1] = true;
								isSafe[row-1][col-1] = false;
								safely_identified++;
							}
						 if(constraint2)
								
							{ 
							 	
							 	isIdentified[row-1][col-1] = true;
								isSafe[row-1][col-1] = true;
								clue[row-1][col-1] = neighboring_mines[row-1][col-1];
							}
				
				 }
				 if((row-1)>=0 && (col+1)<dim && !isIdentified[row-1][col+1]) // UPPER RIGHT
				 {count++;
				 System.out.println("UPDATING UPPER RIGHT OF: " + "[ " + row + " , " + col + " ]");
				 unexplored_neighbors[row][col]--;
				
						 if(constraint1)
								
							{ 
								
							 	isIdentified[row-1][col+1] = true;
								isSafe[row-1][col+1] = false;
								safely_identified++;
							}
						 if(constraint2)
								
							{ 
							 	
							 	isIdentified[row-1][col+1] = true;
								isSafe[row-1][col+1] = true;
								clue[row-1][col+1] = neighboring_mines[row-1][col+1];
							}
					
				 }
				 System.out.println("COMPLETED: " + "[ " + row + " , " + col + " ]"); // WOW!
				isComplete[row][col] = true; // MARK THE LOCATION COMPLETE
				// AND TELL ALL NEIGHBORS TO ANNOUNCE THEIR NEIGHBORS THAT THEY HAVE BEEN REVEALED!!!!
				if((row-1)>=0) //if UP exists
				 {
					 update_neighborhood(row-1,col);
				 }
				 
				 if((row+1)<dim ) // if DOWN exists
				 {
					 update_neighborhood(row+1,col);
				 }
					
				 if((col-1)>=0 ) //LEFT
				 {
					 update_neighborhood(row,col-1);
				
				 }
				 if((col+1)<dim ) //RIGHT
				 {
					 update_neighborhood(row,col+1);
				 }
				 if((row+1)<dim && (col-1)>=0 )  // DOWN LEFT
				 {
					 update_neighborhood(row+1,col-1);
				 }
				 if((row+1)<dim && (col+1)<dim) // DOWN RIGHT
				 {
					 update_neighborhood(row+1,col+1);
				 }
				 if((row-1)>=0 && (col-1)>=0)  // UPPER LEFT
				 {
					 update_neighborhood(row-1,col-1);
				
				 }
				 if((row-1)>=0 && (col+1)<dim) // UPPER RIGHT
				 {
					 update_neighborhood(row-1,col+1);
				
				 }
				 
			}
		 
			
		 	
	 

	}
	
	// The solving method:
	public void solve()
	{
		// Until all locations are visited
		
		 while((count != (dim*dim)))
		 {
			 // we generate a random location index first
			int row = random_generator.nextInt(dim);
			 int col = random_generator.nextInt(dim);
			System.out.println("TRYING RANDOM");
		 if(!isIdentified[row][col]) // if the location is not identified
				 
			 {
			 // explore, update , infer, DO EVERYTHING U CAN
				random_locations++;
				 System.out.println("EXPLORED RANDOMLY AT: [ " + row + " , " + col + "]");
				 explore_location(row,col);
				
			 }
		
			 	// THIS PROGRAM WILL DEDUCE ALL THE POSSIBLE INFORMATION THAT THE CONSTRAINT CAN MAKE US DEDUCE AND GO BACK TO RANDOM GENERATION IF THE BOARD HAS
		 //NOT BEEN SOLVED YET
		 }
		
		System.out.println();
		System.out.println();
	}
	public static void main(String[] args)
	{
		// scanner for user input
		Scanner sc = new Scanner(System.in);
		System.out.println("ENTER DIMENSION: "); 
		int d = sc.nextInt();
		System.out.println("ENTER NUMBER OF MINES: ");
		int n = sc.nextInt();
		sc.close(); // close the scanned
		
		if(n > (d*d))
		{
			System.out.println("BAD INPUT! :(");
			return;
		}
		
		
		
	
		
		
		Basic_Agent my_agent = new Basic_Agent(d,n);  // LETS GET OUR BASIC AGENT!
		
		System.out.println();
		System.out.println("INPUT: ");   // PRINT INPUT FROM ORIGINAL DATA: NOT VISIBLE TO OUR AGENT UNTIL LOCATIONS ARE REVEALED
		for(int i=0; i<d; i++)
		{
			for(int j=0; j<d; j++)
			{
				if(my_agent.isMine[i][j])
				{
					System.out.print('M');
				}
				else
				{
					System.out.print(my_agent.neighboring_mines[i][j]);
				}
				System.out.print('\t');
			}
			System.out.println();
			
		}
		my_agent.solve(); // ASK OUR AGENT TO SOLVE THIS,, EASY PEASY!!
		System.out.println("LOG: ");  // HERE's THE PROGRESSION
		
		
		System.out.println();
		
	
		int a=0,b=0;
			// GET OUTPUT
		System.out.println("OUTPUT: ");
		for(int i=0; i<d; i++)
		{
			for(int j=0; j<d; j++)
			{
				if(my_agent.isSafe[i][j])
				{
					System.out.print(my_agent.clue[i][j]);
				}
				else
				{
					System.out.print('M');
				}
				if(my_agent.isIdentified[i][j])
				{
					b++;
				}
				if(my_agent.isSafe[i][j] != my_agent.isMine[i][j])
				{
					a++;
				}
				System.out.print('\t');
			}
			System.out.println();
			
		}
		// BOOK KEEPING
		System.out.println();
		System.out.println();
		System.out.println("Correct: " + a);
		System.out.println("Identified: " + b);
		System.out.println("Count: " + my_agent.count);
		System.out.println("Randoms: " + my_agent.random_locations);
		
		System.out.println("Explosions: " + my_agent.explosions);
		System.out.println("Safely Identified: " + my_agent.safely_identified);

	}
		
}	
			 
			 
			

	
				 
				 
				 



			 
			 