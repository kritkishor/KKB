package minesweeper;
import java.util.*;
import java.util.Random;
public class MineSweeper {
	// generating a mine environment as a 2D array of dimension = d*d and number of mines = n
	
			int mine[][];   // Declaring the array for mine table
			boolean isMine[][]; // Boolean variable to know if a location is a mine
			int neighboring_mines[][]; // Number of neighboring mines at a particular location
			int neighbors[][]; // Number of neighbors for each location
	//Constructor to generate minesweeper as a 2D array of dimension d*d and n number of mines
	public MineSweeper(int d, int n) {
		
		mine = new int[d][d]; // Creating the array
		neighboring_mines = new int[d][d];
		isMine = new boolean[d][d];
		neighbors = new int[d][d];
		// Assign n random locations to be a mine :
		int count = 0;
 		Random rand = new Random();  // Instance of Random class (imported from java.util.Random)
		while(count!=n) // while we do not have sufficient number of mines
		{
			// Getting two random integers less than d ( from 0 to d-1) and 
			int random_row = rand.nextInt(d);  
			int random_col = rand.nextInt(d);
			// Assigning the random location as a mine if it is not already a mine
			if(!isMine[random_row][random_col])
			{
				isMine[random_row][random_col] = true;
				count++; // increment the number of mines
			}
			// if it is already a mine
			else
			{
				continue; // continue generating until we get a safe location
			}
			
		}
		// Now providing the total number neighboring mines at a location and storing it in the mine array
		count=0;
		// for all locations in the table
		int i,j;
		for(i=0;i<d;i++)
		{
			for(j=0; j<d; j++)
			{
				// if the location is not a mine, and its neighboring locations exist and have a mine, increment the number of mines 
				// Note: Java initializes integer array to 0. So, we do not need to worry about having random values after declaration.
				if(!isMine[i][j]) 
					{
						if((i-1)>=0 && isMine[i-1][j]) //UP
						{
							neighboring_mines[i][j]++;
						}
						if((i+1)<d && isMine[i+1][j]) //DOWN
						{
							neighboring_mines[i][j]++;
						}
						if((j-1)>=0 && isMine[i][j-1]) //LEFT
						{
							neighboring_mines[i][j]++;
						}
						if((j+1)<d && isMine[i][j+1]) //RIGHT
						{
							neighboring_mines[i][j]++; // = neighboring_mines[i][j] + 1;
						}
						if((i+1)<d && (j-1)>=0 && isMine[i+1][j-1])  // DOWN LEFT
						{
							neighboring_mines[i][j]++; // = neighboring_mines[i][j] + 1;
						}
						if((i+1)<d && (j+1)<d && isMine[i+1][j+1]) // DOWN RIGHT
						{
							neighboring_mines[i][j]++; // = neighboring_mines[i][j] +1;
						}
						if((i-1)>=0 && (j-1)>=0 && isMine[i-1][j-1])  // UPPER LEFT
						{
							neighboring_mines[i][j]++; // = neighboring_mines[i][j] + 1;
						}
						if((i-1)>=0 && (j+1)<d && isMine[i-1][j+1]) // UPPER RIGHT
						{
							neighboring_mines[i][j]++; // = neighboring_mines[i][j] + 1;
						}
						
					}
			}
		}
		for(int row = 0; row<d; row++)
		{
			for(int col =0; col<d; col++)
			{
				if((row-1)>=0) //if UP exists
				{
					neighbors[row][col]++;
					
				}
				if((row+1)<d ) // if DOWN exists
				{
					neighbors[row][col]++;
					
				}
				 if((col-1)>=0 ) //LEFT
				{
					 neighbors[row][col]++;
					
				}
				 if((col+1)<d ) //RIGHT
				{
					 neighbors[row][col]++;
					 
				}
				 if((row+1)<d && (col-1)>=0 )  // DOWN LEFT
				{
					 neighbors[row][col]++;
					
				}
				if((row+1)<d && (col+1)<d) // DOWN RIGHT
				{
					neighbors[row][col]++;
					
				}
				if((row-1)>=0 && (col-1)>=0)  // UPPER LEFT
				{
					neighbors[row][col]++;
					
				}
				if((row-1)>=0 && (col+1)<d) // UPPER RIGHT
				{
					neighbors[row][col]++;
					
				}
				
			}
		}
		
	}

	
	
	public static void main(String[] args) {
		// Inputing dimension and number of mines from the user
		
		System.out.println("Enter the dimension: ");
		Scanner sc = new Scanner(System.in);
		int d = sc.nextInt();
		System.out.println("Enter the number of Mines: ");
		int n = sc.nextInt();
		sc.close();
		if(n > (d*d))
		{
			System.out.println("BAD INPUT! :(");
			return;
		}
		
		
		
		MineSweeper table = new MineSweeper(d,n);
		
				
		
		
		// PRINTING THE BOARD 
		// 'M' if there is Mine
		// CLUE OTHERWISE
		for(int i=0; i<d; i++)
		{
			System.out.print('|');
			for(int j=0;j<d;j++)
			{
				
				if(table.isMine[i][j])
				{
					System.out.print('M');
				}
				else
				{
					System.out.print(table.neighboring_mines[i][j]);
				}
				System.out.print('|');
			}
			
			
			System.out.println();
		}
			sc.close();
			/*		int p[][] = new int[d][d];
		int i = d/2 - 1;
		int j = d/2 - 1;
		
		System.out.println(table.isMine[i][j]);
		System.out.println(table.neighboring_mines[i][j]);
		if(!table.isMine[i][j]) 
		{
			if((i-1)>=0) //UP
			{
				p[i-1][j]++;
			}
			if((i+1)<d) //DOWN
			{
				p[i+1][j]++;
			}
			if((j-1)>=0 ) //LEFT
			{
				p[i][j-1]++;
			}
			if((j+1)<d) //RIGHT
			{
				p[i][j+1]++;
			}
			if((i+1)<d && (j-1)>=0 )  // DOWN LEFT
			{
				p[i+1][j-1]++;
			}
			if((i+1)<d && (j+1)<d) // DOWN RIGHT
			{
				p[i+1][j+1]++;
			}
			if((i-1)>=0 && (j-1)>=0)  // UPPER LEFT
			{
				p[i-1][j-1]++;
			}
			if((i-1)>=0 && (j+1)<d) // UPPER RIGHT
			{
				p[i-1][j+1]++;
			}
			
		}
		int count = 0;
		
	while(count!=(d*d - n))
		{
			
		}
		
	*/	
	}

}
