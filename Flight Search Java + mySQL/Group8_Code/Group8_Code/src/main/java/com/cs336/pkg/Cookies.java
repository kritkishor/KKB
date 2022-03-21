package com.cs336.pkg;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Cookies  {

	public Cookies()
	{
		
	}
	public Cookies(String username) throws IOException 
	{
		File usernameFile = new File("username.txt");
		FileWriter writer = new FileWriter(usernameFile);
		writer.write(username);
		writer.close();
	}
	public Cookies(String[][] search) throws IOException
	{
		File searchFile = new File("search.txt");
		FileWriter writer = new FileWriter(searchFile);
		
		for(int i=0; i<8; i++)
		{
			if(i != 1 && i != 2 )
			{
				writer.write(search[i][0] + "\n");
			}
			else 
			{
				int j = 0;
				while(search[i][j] != null && !search[i][j].isBlank())
				{
					writer.write(search[i][j++] + ","); 
				}
				writer.write("\n");
			}
			
		}
		
		writer.close();
	}
	public void flight1Cookies(String[] flights,String[] dates) throws IOException
	{
		File flightFile = new File("flight1.txt");
		FileWriter writer = new FileWriter(flightFile);
		String date;
		int i =0;
		for(String s : flights)
		{	
			date = dates[i++];
			writer.write(s+","+date+"\n");
		}
		writer.close();
	}
	public String[][] getFlight1Data() throws FileNotFoundException
	{
		String[][] res = new String[20][2];
		Scanner reader = new Scanner(new File("flight1.txt"));
		int i = 0;
		int k;
		String temp;
		while(reader.hasNext())
		{
			temp = reader.nextLine();
			k = temp.indexOf(',');
			res[i][0] = temp.substring(0,k);
			res[i][1] = temp.substring(k+1);
			i++;
		}
		return res;
	}
	public void flight2Cookies(String[] flights,String[] dates) throws IOException
	{
		File flightFile = new File("flight2.txt");
		FileWriter writer = new FileWriter(flightFile);
		String date;
		int i =0;
		for(String s : flights)
		{	
			date = dates[i++];
			writer.write(s+","+date+"\n");
		}
		writer.close();
	}
	public String[][] getFlight2Data() throws FileNotFoundException
	{
		String[][] res = new String[20][2];
		Scanner reader = new Scanner(new File("flight2.txt"));
		int i = 0;
		int k;
		String temp;
		while(reader.hasNext())
		{
			temp = reader.nextLine();
			k = temp.indexOf(',');
			res[i][0] = temp.substring(0,k);
			res[i][1] = temp.substring(k+1);
			i++;
		}
		return res;
	}
	public String[][] getSearchData() throws FileNotFoundException
	{
		String[][] res = new String[8][50];
		Scanner reader = new Scanner(new File("search.txt"));
		
		for(int i=0; i<8; i++)
		{
			if(i != 1 && i != 2)
			{
				res[i][0] = reader.nextLine();
			}
			else 
			{
				String line = reader.nextLine();
				int j = 0,k=3,l=0;
				while(k < line.length())
				{
					res[i][l] = line.substring(j,k);
					j = k+1;
					k = k+4;
					l++;
				}
			}
			
		}
		
		return res;
	}
	public String getCurrentUser() throws FileNotFoundException
	{
		Scanner reader = new Scanner(new File("username.txt"));
		return reader.nextLine();
	}
	
}
