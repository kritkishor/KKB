package minesweeper;
import java.util.Random;
import java.util.Scanner;

/* PLEASE READ THIS:
 * THIS PROGRAM HAS ALL THE FUNCTIONS FROM BASIC AGENT EXCEPT THE SOLVING METHOD: ITS IMPROVISED HERE
 * 
 */




// Class to create a basic agent
public class Improved {
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
	boolean isComplete[][];
//	boolean isMine[][];
	int neighboring_mines[][];
	int random_locations;
	// Conditions given in write up
	boolean constraint1; // true if total mines(clue) - revealed mines = unexplored mines
	boolean constraint2; // true if 
	
	int dim;
	Random random_generator;
	
	int explosions; // KEEPS TRACK OF THE EXPLOSIONS
	int safely_identified; // Keeps track of SAFELY IDENTIFIED mines
	
	// Constructor for the basic agent: takes the dimension and number of mines as parameters
	public Improved(int d, int n) {
		
		isComplete = new boolean[d][d];
		random_generator = new Random();
		dim = d;
		// KB (initialization):
		isIdentified = new boolean[d][d];
	//	isMine = new boolean[d][d];
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
		MineSweeper improved_agent = new MineSweeper(d,n);
		

		isMine = improved_agent.isMine;
		neighboring_mines = improved_agent.neighboring_mines;
		explosions = 0;
		safely_identified = 0;
		
		neighbors = improved_agent.neighbors;
		for(int row = 0; row<d; row++)
		{
			for(int col =0; col<d; col++)
			{
				unexplored_neighbors[row][col] = neighbors[row][col];
			}
		}
	}
	
	public boolean update_variables(int row,int col)
	{
		if((count == dim*dim) || isComplete[row][col]  || !isSafe[row][col])
		{
			return false;
		}
		
		int prev = unexplored_neighbors[row][col];
		
		if(isSafe[row][col] && !isComplete[row][col] )
		{
			System.out.println("UPDATING VARIABLES OF: " + " [ " + row + " , " + col + " ]");
				unexplored_neighbors[row][col] = 0;
				safe_neighbors[row][col] = 0;
				identified_mines[row][col] = 0;	
			
			if((row-1)>=0)  //if UP exists
			
			 {
				 
				 if(!isIdentified[row-1][col])
				 {
					
					 unexplored_neighbors[row][col]++;
				 }
				 else
				 { System.out.println("IS  EXPLORED: " + " [ " + (row-1) + " , " + col + " ]");
					 if(isSafe[row-1][col])
					 {
						 safe_neighbors[row][col]++;
					 }
					 else
					 {
						 identified_mines[row][col]++;
					 }
				 }
				 
			 }
			 
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
		System.out.println("UNEXPLORED: " + unexplored_neighbors[row][col]);
		System.out.println("SAFE: " + safe_neighbors[row][col]);
		if(unexplored_neighbors[row][col] == 0)
		{
			isComplete[row][col] = true;
		}
		if(!isComplete[row][col] && (prev != unexplored_neighbors[row][col]))
		{
			return true;
		}
		return false;
		
	/*	if(((row-1)>=0) && (isSafe[row-1][col]) && (unexplored_neighbors[row-1][col] != (neighbors[row-1][col] - safe_neighbors[row-1][col] - identified_mines[row-1][col]) )) //if UP exists
		 {
			explore_location(row-1,col);
		 }
		 
		 if(((row+1)<dim) && isSafe[row+1][col]  && (unexplored_neighbors[row+1][col] != (neighbors[row+1][col] - safe_neighbors[row+1][col] - identified_mines[row+1][col]) )) // if DOWN exists
		 {
			 explore_location(row+1,col);
		 }
			
		 if(((col-1)>=0) && (isSafe[row][col-1])  && (unexplored_neighbors[row][col-1] != (neighbors[row][col-1] - safe_neighbors[row][col-1] - identified_mines[row][col-1]) )) //LEFT
		 {
			 explore_location(row,col-1);
		
		 }
		 if(((col+1)<dim) && (isSafe[row][col+1])  && (unexplored_neighbors[row][col+1] != (neighbors[row][col+1] - safe_neighbors[row][col+1] - identified_mines[row][col+1]) )) //RIGHT
		 {
			 explore_location(row,col+1);
		 }
		 if(((row+1)<dim) && ((col-1)>=0) && (unexplored_neighbors[row+1][col-1] != (neighbors[row+1][col-1] - safe_neighbors[row+1][col-1] - identified_mines[row+1][col-1]) )) // DOWN LEFT
		 {
			 explore_location(row+1,col-1);
		 }
		 if(((row+1)<dim) && ((col+1)<dim) && (isSafe[row+1][col+1]) && (unexplored_neighbors[row+1][col+1] != (neighbors[row+1][col+1] - safe_neighbors[row+1][col+1] - identified_mines[row+1][col+1]) )) // DOWN RIGHT
		 {
			 explore_location(row+1,col+1);
		 }
		 if(((row-1)>=0) && ((col-1)>=0) && (isSafe[row-1][col-1])  && (unexplored_neighbors[row-1][col-1] != (neighbors[row-1][col-1] - safe_neighbors[row-1][col-1] - identified_mines[row-1][col-1]) )) // UPPER LEFT
		 {
			explore_location(row-1,col-1);
		
		 }
		 if(((row-1)>=0) && ((col+1)<dim) && (isSafe[row-1][col+1])  && (unexplored_neighbors[row-1][col+1] != (neighbors[row-1][col+1] - safe_neighbors[row-1][col+1] - identified_mines[row-1][col+1]) )) // UPPER RIGHT
		 {
			 
			 explore_location(row-1,col+1);
		 }
		*/
		
	
	}
	public boolean update_neighborhood(int row,int col)
	{
		boolean retval;
		int ct = 0;
		
		if(((row-1)>=0) && isSafe[row-1][col] && !isComplete[row-1][col])  //if UP exists
			
		 {
			System.out.println("UPDATING UP OF: " + "[ " + row + " , " + col + " ]");
			retval = update_variables(row-1,col);
			if(retval)
			{
				check_constraints(row-1,col);
				ct++;
			}
			
		 }
		 
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
		 if(ct > 0)
		 {
			 return true;
		 }
		 return false;
		
	}
	public void explore_location(int row, int col)
	{
		
		System.out.println("EXPLORE AT: " + "[ " + row + " , " + col + " ]");
		
		
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
			
		 update_neighborhood(row,col);
		}
			
			
		
		
		
		
		
		if(count == dim*dim)
		{
			return;
		}
		
		
	
		
		 
		
		
	// Update the number unexplored neighbors for this location accordingly,
		// i.e. if a neighbor of this location is not explored, increment the number of unexplored neighbors for this location
		// Increment the number of mines identified/explored for all existing neighbors of this location if it is not safe,
		// Increment the number of safe neighbors for them otherwise:
		
		if(isSafe[row][col] && !isComplete[row][col])
			{
			 update_variables(row,col);
			}
		
		
		 
			 System.out.println("Neighbors AT:  " + "[ " + row + " , " + col + " ]" + " = " + neighbors[row][col]);
			 System.out.println("Identified Mines AT:  " + "[ " + row + " , " + col + " ]" + " = " + identified_mines[row][col]);
			 System.out.println("Unexplored Neighbors AT:  " + "[ " + row + " , " + col + " ]" + " = " + unexplored_neighbors[row][col]);
			 System.out.println("Safe Neighbors AT:  " + "[ " + row + " , " + col + " ]" + " = " + safe_neighbors[row][col]);
			
			
				 	check_constraints(row,col);
				 
			
			 
	
	
	
	}
		
	
			
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
		
		 if(isSafe[row][col] && !isComplete[row][col])
		 {
			// System.out.println("clue: " + clue[row][col]);
			 if( (clue[row][col]-identified_mines[row][col]) != (unexplored_neighbors[row][col]) ) 
			 {
				 
				 constraint1 = false;
			 }
			 else {
				if((clue[row][col]==0) || (clue[row][col]==identified_mines[row][col])) { constraint1=false; }
			 	else { 
			 		System.out.println("CONDN1 MET AT: " + "[ " + row + " , " + col + " ]");
			 		 System.out.println("Neighbors AT:  " + "[ " + row + " , " + col + " ]" + " = " + neighbors[row][col]);
					 System.out.println("Identified Mines AT:  " + "[ " + row + " , " + col + " ]" + " = " + identified_mines[row][col]);
					 System.out.println("Unexplored Neighbors AT:  " + "[ " + row + " , " + col + " ]" + " = " + unexplored_neighbors[row][col]);
					 System.out.println("Safe Neighbors AT:  " + "[ " + row + " , " + col + " ]" + " = " + safe_neighbors[row][col]);
			 		
			 		constraint1 = true; }
			 }
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
				*/
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
		
		 if((constraint1 == false) && (constraint2 == false))
		 {
			 System.out.println("NO CONDITIONS MET");
		 }
		 
		 if(constraint1 || constraint2)
			{
			 	
			 	
			 	
			 	
		/*	 	if(constraint1)
					
				{ 
			 		identified_mines[row][col] += unexplored_neighbors[row][col];
				 	
				 	
				}
			 	if(constraint2)
			 	{
			 		safe_neighbors[row][col] += unexplored_neighbors[row][col];
				 	
				 	
			 	}
			 	unexplored_neighbors[row][col] = 0;
			*/	 if((row-1)>=0 && !isIdentified[row-1][col]) //if UP exists and has not yet been explored
				 {
				System.out.println("UPDATING UP OF: " + "[ " + row + " , " + col + " ]");
				unexplored_neighbors[row][col]--;
				//	 if(constraint1 || constraint2) // it is not safe, every hidden neighbor is mine
					// {
					 count++;
						 if(constraint1)
								
							{ 
							 	
							 	isIdentified[row-1][col] = true;
								isSafe[row-1][col] = false;
								safely_identified++;
								
							}
						 if(constraint2)
								
							{ 
							 	
							 	isIdentified[row-1][col] = true;
								isSafe[row-1][col] = true;
								clue[row-1][col] = neighboring_mines[row-1][col];
							}
					// update_neighborhood(row-1,col);
						
				//	 }
					 
				 }
				 if((row+1)<dim && !isIdentified[row+1][col] ) // if DOWN exists
				 {
					 System.out.println("UPDATING DOWN OF: " + "[ " + row + " , " + col + " ]");
					 unexplored_neighbors[row][col]--;
			//		 if(constraint1 || constraint2)
				//	 {
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
					//	 retval = update_neighborhood(row+1,col);
						 
				//	 }
				 }
				  if((col-1)>=0 && !isIdentified[row][col-1]) //LEFT
				 {	System.out.println("UPDATING LEFT OF: " + "[ " + row + " , " + col + " ]");
					  count++;
				 unexplored_neighbors[row][col]--;
			//		  if(constraint1 || constraint2)
				//		 {
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
					//	 update_neighborhood(row,col-1);
						
						//		 }
				 }
				  if((col+1)<dim && !isIdentified[row][col+1] ) //RIGHT
				 {	System.out.println("UPDATING RIGHT OF: " + "[ " + row + " , " + col + " ]");
					  count++;
				 unexplored_neighbors[row][col]--;
				//	  if(constraint1 || constraint2)
					//	 {
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
						//  update_neighborhood(row,col+1);
						  
				//		 }
				 }
				  if((row+1)<dim && (col-1)>=0 && !isIdentified[row+1][col-1] )  // DOWN LEFT
				 {count++;
				 System.out.println("UPDATING DOWN LEFT OF: " + "[ " + row + " , " + col + " ]");
				 unexplored_neighbors[row][col]--;
			//		  if(constraint1 || constraint2)
			//			 {
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
						//  update_neighborhood(row+1,col-1);
				//		 }
				 }
				 if((row+1)<dim && (col+1)<dim && !isIdentified[row+1][col+1]) // DOWN RIGHT
				 {count++;
				 System.out.println("UPDATING DOWN RIGHT OF: " + "[ " + row + " , " + col + " ]");
				 unexplored_neighbors[row][col]--;
			//		 if(constraint1 || constraint2)
				//	 {
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
					//	 update_neighborhood(row+1,col+1);
				//	 }
				 }
				 if((row-1)>=0 && (col-1)>=0 && !isIdentified[row-1][col-1])  // UPPER LEFT
				 {count++;
				 System.out.println("UPDATING UPPER LEFT OF: " + "[ " + row + " , " + col + " ]");
				 unexplored_neighbors[row][col]--;
			//		 if(constraint1 || constraint2)
				//	 {
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
					//	 update_neighborhood(row-1,col-1);
						 
				//	 }
				 }
				 if((row-1)>=0 && (col+1)<dim && !isIdentified[row-1][col+1]) // UPPER RIGHT
				 {count++;
				 System.out.println("UPDATING UPPER RIGHT OF: " + "[ " + row + " , " + col + " ]");
				 unexplored_neighbors[row][col]--;
				//	 if(constraint1 || constraint2)
				//	 {
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
						// update_neighborhood(row-1,col+1);
				//	 }
				 }
				 System.out.println("COMPLETED: " + "[ " + row + " , " + col + " ]");
				isComplete[row][col] = true;
				
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
	
	// IMPROVED SOLVER
	// MAKES EDUCATED GUESS WHERE TO EXPLORE FIRST
	// DETAILS IN WRITE-UP
	public void solve()
	{
		int row,col;
		
		 while((count != (dim*dim)))
		 {
			 
			 
			 // WE WANT TO EXPLORE THE CORNERS FIRST :O
			 // corners might be least probable locations for mines, Details in write-up
			 
		 if(!isIdentified[0][0])
			 {
				 explore_location(0,0);
			 }
			 
			 if(clue[0][0] == 1)
			 {
				 while(unexplored_neighbors[0][0] != 0)
				 {
					 row = random_generator.nextInt(2);
					 col = random_generator.nextInt(2);
					 if(!isIdentified[row][col])
					 {
						 explore_location(row,col);
					 }
				 }
			 }
				 
			 if(!isIdentified[dim-1][0])
			 {
				 explore_location(dim-1,0);
			 }
			 
			 if(clue[dim-1][0] == 1)
			 {
				 if(!isIdentified[dim-1][1])
					 
				 {
					 explore_location(dim-1,1);
				 }
				 if(!isIdentified[dim-2][0])
				 {
					 explore_location(dim-2,0);
				 }
			 }
			 
			 if(!isIdentified[0][dim-1])
			 {
				 explore_location(0,dim-1);
			 }
			 if(clue[0][dim-1] == 1)
			 {
				 if(!isIdentified[0][dim-2])
					 
				 {
					 explore_location(0,dim-2);
				 }
				 if(!isIdentified[1][dim-1])
				 {
					 explore_location(1,dim-1);
				 }
			 }
			 if(!isIdentified[dim-1][dim-1])
			 {
				 explore_location(dim-1,dim-1);
			 }
			 if(clue[dim-1][dim-1] == 1)
			 {
				 if(!isIdentified[dim-2][dim-1])
					 
				 {
					 explore_location(dim-2,dim-1);
				 }
				 if(!isIdentified[dim-1][dim-2])
				 {
					 explore_location(1,dim-1);
				 }
			 }
			 
			 
			 row = random_generator.nextInt(dim);
			 col = random_generator.nextInt(dim);
			System.out.println("TRYING RANDOM");
		 if(!isIdentified[row][col])
				 
			 {
				random_locations++;
				 System.out.println("EXPLORED RANDOMLY AT: [ " + row + " , " + col + "]");
				 explore_location(row,col);
				 
			 }
		// check all the visited locations
		 // if the location has probability (clue/unexplored locations) less than 3/8, we want to explore the neighbors first!
		 for(int i =0; i<dim; i++)
		 {
			 for(int j=0; j<dim; j++)
			 {
				 if(isSafe[i][j] && !isComplete[i][j])
				 {
					 if((clue[i][j] / unexplored_neighbors[i][j]) <= (3/8))
					 {
						 row = i;
						 col = j;
						 if((row-1)>=0 && !isIdentified[row-1][col]) //if UP exists
						 {
							 explore_location(row-1,col);
						 }
						 
						 if((row+1)<dim && !isIdentified[row=1][col]) // if DOWN exists
						 {
							 explore_location(row+1,col);
						 }
							
						 if((col-1)>=0 && !isIdentified[row][col-1] ) //LEFT
						 {
							 explore_location(row,col-1);
						
						 }
						 if((col+1)<dim && !isIdentified[row][col+1] ) //RIGHT
						 {
							 explore_location(row,col+1);
						 }
						 if((row+1)<dim && (col-1)>=0  && !isIdentified[row+1][col-1])  // DOWN LEFT
						 {
							 explore_location(row+1,col-1);
						 }
						 if((row+1)<dim && (col+1)<dim && !isIdentified[row+1][col+1]) // DOWN RIGHT
						 {
							 explore_location(row+1,col+1);
						 }
						 if((row-1)>=0 && (col-1)>=0 && !isIdentified[row-1][col-1])  // UPPER LEFT
						 {
							 explore_location(row-1,col-1);
						
						 }
						 if((row-1)>=0 && (col+1)<dim && !isIdentified[row-1][col+1]) // UPPER RIGHT
						 {
							 explore_location(row-1,col+1);
						
						 }
					 }
				 }
			 }
		 }
			 	
		 }
		
		System.out.println();
		System.out.println();
	}
	public static void main(String[] args)
	{
		
		Scanner sc = new Scanner(System.in);
		System.out.println("ENTER DIMENSION: ");
		int d = sc.nextInt();
		System.out.println("ENTER NUMBER OF MINES: ");
		int n = sc.nextInt();
		sc.close();
	
		if(n > (d*d))
		{
			System.out.println("BAD INPUT! :(");
			return;
		}
		
		
	
		
		
		Improved my_agent = new Improved(d,n);
		
		System.out.println();
		System.out.println("INPUT: ");
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
		my_agent.solve();
		System.out.println("LOG: ");
		
		
		System.out.println();
		
	
	
		int a=0,b=0;
			
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
			 
			 
			

	
				 
				 
				 



			 
			 