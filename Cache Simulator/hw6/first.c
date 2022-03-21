#include <stdio.h>
#include <stdlib.h>
#include<string.h>
#include<math.h>
int miss;
int hit;
int read;
struct line** cache;
unsigned long int count;
int write;
typedef struct line
{
	unsigned long int tag;
	int valid;
	unsigned long int time;
}line;

void rfifo(unsigned long int tagIndex,unsigned long int setIndex,int assoc)
{

	int i,j,low;
	for(i=0;i<assoc;i++)
	{
		if(cache[setIndex][i].valid==0)
		{
			miss++;
			read++;
			count++;
			cache[setIndex][i].valid=1;
			cache[setIndex][i].tag=tagIndex;
			cache[setIndex][i].time=count;
	
			return;
		}
		else
		{
			if(cache[setIndex][i].tag==tagIndex)
			{	
				hit++;
				return;
			}
			
			if(i==(assoc-1))
			{
				miss++;
				read++;
				low=0;
				for(j=0;j<assoc;j++)
				{
					if(cache[setIndex][j].time<=cache[setIndex][low].time)
					{
						low=j;
					}	
				}
			
				cache[setIndex][low].valid=1;
	       			cache[setIndex][low].tag=tagIndex;
	      			count++;
	        		cache[setIndex][low].time=count;
	        		return;
			}
		
		
		
		
		}
	}

	return;

}
line** createCache(int setValue,int assoc)
{
 	 int i,j;
	 cache=(line**)malloc(setValue*sizeof(line*));
 	 for(i=0;i<setValue;i++)
	  {
	  	cache[i]=(line*)malloc((assoc)*sizeof(line));
	  }

 	 for(i=0;i<setValue;i++)
 	 {
  	 	for(j=0;j<assoc;j++)
  	 	{
        		cache[i][j].valid=0;

   		}
 	 }
 	 return cache;
}

void wfifo(unsigned long int tagIndex,unsigned long int setIndex,int assoc)
{

	int i,j,low;

	for(i=0;i<assoc;i++)
	{
		if(cache[setIndex][i].valid==0)
		{
			miss++;
			read++;
			write++;
			count++;
			cache[setIndex][i].valid=1;
			cache[setIndex][i].tag=tagIndex;
			cache[setIndex][i].time=count;
			return;
		}
		else
		{
			if(cache[setIndex][i].tag==tagIndex)
			{
				hit++;
				write++;
				return;
			}

			if(i==(assoc-1))
			{
				miss++;
				read++;
				write++;
				low=0;
				for(j=0;j<assoc;j++)
				{
					if(cache[setIndex][j].time<=cache[setIndex][low].time)
					{
						low=j;
					}
				}


				cache[setIndex][low].valid=1;
	       			cache[setIndex][low].tag=tagIndex;
	       			count++;
	       			cache[setIndex][low].time=count;
	       			return;
			}

		}


	}

	return;

}
void empty(int setValue, int assoc)
{
	int i,j;
	for(i=0;i<setValue;i++)
	{
		for(j=0;j<assoc;j++)
		{
			cache[i][j].tag=0;
			cache[i][j].valid=0;
			cache[i][j].time=0;

		}	
	}
	miss=0;
	hit=0;
	read=0;
	write=0;
	count=0;
}
void rlru(unsigned long int tagIndex,unsigned long int setIndex,int assoc)
{

	int i,j,low;

	for(i=0;i<assoc;i++)
	{
		if(cache[setIndex][i].valid==0)
		{
			miss++;
			read++;
			count++;
			cache[setIndex][i].valid=1;
			cache[setIndex][i].tag=tagIndex;
			cache[setIndex][i].time=count;
			return;
		}
		else
		{
			if(cache[setIndex][i].tag==tagIndex)
			{
				hit++;
				count++;
				cache[setIndex][i].time=count;
				return;
			}

			if(i==(assoc-1))
			{
				miss++;
				read++;
				low=0;
				for(j=0;j<assoc;j++)
				{
					if(cache[setIndex][j].time<=cache[setIndex][low].time)
					{
						low=j;
					}
				}
				cache[setIndex][low].valid=1;
	       			cache[setIndex][low].tag=tagIndex;
	       			count++;
	       	 		cache[setIndex][low].time=count;
				return;
			}
		}
	}
	return;
}

void wlru(unsigned long int tagIndex,unsigned long int setIndex,int assoc)
{

	int i,j,low;

	for(i=0;i<assoc;i++)
	{
		if(cache[setIndex][i].valid==0)
		{
			miss++;
			read++;
			write++;
			count++;
			cache[setIndex][i].valid=1;
			cache[setIndex][i].tag=tagIndex;
			cache[setIndex][i].time=count;

			return;
		}
		else
		{
			if(cache[setIndex][i].tag==tagIndex)
			{
				hit++;
				write++;
				count++;
				cache[setIndex][i].time=count;
				return;
			}

			if(i==(assoc-1))
			{
				miss++;
				read++;
				write++;
				low=0;
				for(j=0;j<assoc;j++)
				{

					if(cache[setIndex][j].time<=cache[setIndex][low].time)
					{
						low=j;
					}
				}
		

				cache[setIndex][low].valid=1;
	        		cache[setIndex][low].tag=tagIndex;
	        		count++;
	       			cache[setIndex][low].time=count;
	        		return;
			}




		}
	}	

	return;

}

int main(int argc, char* argv[])
{
	int cacheSize=atoi(argv[1]);
 	 int blockSize=atoi(argv[4]);
 	 int n;//set assoc;
 	 int setValue;
 	 int assoc;

 	 int b;
 	 int s;
 	 char work;
 	 unsigned long int address;
 	 unsigned long int setMask;
  	 unsigned long int tagIndex;
 	 unsigned long int setIndex;
 	 if(argv[3][0]=='f')
  	{

  		FILE* fp;
  		fp=fopen(argv[5],"r");
	
  		if(fp==NULL)
  		{

    			printf("cannot find tracefile with that name\n");
    			return 0;

 	 	}	
		if(argv[2][0]=='d')
		{                                    //direct map

			assoc=1;
			setValue=cacheSize/blockSize;

		} 	
		else if(argv[2][5]!=':')
		{					//fullassoc
			setValue=1;
			assoc=cacheSize/blockSize;
		}
		else
		{					//n way associat chache

    			sscanf(argv[2],"assoc:%d",&n);
    			assoc=n;
    			setValue=cacheSize/blockSize/n;

 	 	}	



		b=log(blockSize)/log(2);
		s=log(setValue)/log(2);
		setMask=((1<<s)-1);
		cache=createCache(setValue,assoc);
		while(fscanf(fp, "%*x: %c %lx", &work, &address)==2)
		{
			setIndex=(address>>b)&setMask;
			tagIndex=address>>(b+s);
			if(work=='R')
			{

				rfifo(tagIndex,setIndex,assoc);
			}
			else if(work=='W')
			{
				wfifo(tagIndex,setIndex,assoc);
			}


		}	

		fclose(fp);


  		fp=fopen(argv[5],"r");

  		if(fp==NULL)
		{

    			printf("cannot find tracefile with that name\n");
    			return 0;

	  	}		
											
		printf("Memory reads: %d\nMemory writes: %d\nCache hits: %d\nCache misses: %d\n",read,write,hit,miss);
	}

	else if(argv[3][0]=='l')
	{
	
  		FILE* fp;
  		fp=fopen(argv[5],"r");

  		if(fp==NULL)
		{

    			printf("cannot find tracefile with that name\n");
    			return 0;

  		}
  		if(argv[2][0]=='d')
  		{							//direct map

			assoc=1;	
			setValue=cacheSize/blockSize;

		}	
		else if(argv[2][5]!=':')
		{						//fullassoc
			setValue=1;
			assoc=cacheSize/blockSize;
		}
		else
		{			//n way associat chache
	
    			sscanf(argv[2],"assoc:%d",&n);
    			assoc=n;
    			setValue=cacheSize/blockSize/n;

  		}	
		b=log(blockSize)/log(2);
		s=log(setValue)/log(2);
		setMask=((1<<s)-1);
		cache=createCache(setValue,assoc);
		while(fscanf(fp, "%*x: %c %lx", &work, &address)==2)
		{

			setIndex=(address>>b)&setMask;
			tagIndex=address>>(b+s);
			if(work=='R')
			{

				rlru(tagIndex,setIndex,assoc);


			}
			else if(work=='W')
			{
				wlru(tagIndex,setIndex,assoc);
			}	


		}	

		fclose(fp);


  		fp=fopen(argv[5],"r");

  		if(fp==NULL)
  		{

    			printf("cannot find tracefile with that name\n");
    			return 0;

  		}
 		printf("Memory reads: %d\nMemory writes: %d\nCache hits: %d\nCache misses: %d\n",read,write,hit,miss);
		empty(setValue,assoc);
	}
}
