#define _POSIX_C_SOURCE 200809L
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <netdb.h>
#include <signal.h>
#include <pthread.h>
#include <ctype.h>
#define BACKLOG 5

int running = 1;
int list_size = 5;

typedef struct{
	char **id;
	char **key;
	int elems;
}list_t;
list_t my_list;
pthread_mutex_t lock;
// the argument we will pass to the connection-handler threads
struct connection {
    struct sockaddr_storage addr;
    socklen_t addr_len;
    int fd;
};

int server(char *port);
void *echo(void *arg);
int enlist(char *id, char *key);
char* delist(char *id);
char* search(char *id);
/*
void handle(int sig)
{
	printf("CAUGHT SIGNAL\n");
	for(int i=0; i<my_list.elems; i++)
	{
		free(my_list.id[i]);
		free(my_list.key[i]);
	}
	free(my_list.id);
	free(my_list.key);
}
*/
int main(int argc, char **argv)
{
//	signal(SIGINT,handle);
	if (argc != 2) {
		printf("Usage: %s [port]\n", argv[0]);
		exit(EXIT_FAILURE);
	}
	if(pthread_mutex_init(&lock,NULL) != 0)
	{
		printf("MUTEX INIT FAILED\n");
		exit(EXIT_FAILURE);
	}
	my_list.elems = 0;
	my_list.id = malloc(list_size * sizeof(char *));
	my_list.key = malloc(list_size * sizeof(char *));

    (void) server(argv[1]);
	
    pthread_mutex_destroy(&lock);
    
    return EXIT_SUCCESS;
}

void handler(int signal)
{
	running = 0;
}


int server(char *port)
{
    struct addrinfo hint, *info_list, *info;
    struct connection *con = NULL;
    int error, sfd;
    pthread_t tid;

    // initialize hints
    memset(&hint, 0, sizeof(struct addrinfo));
    hint.ai_family = AF_UNSPEC;
    hint.ai_socktype = SOCK_STREAM;
    hint.ai_flags = AI_PASSIVE;
    	// setting AI_PASSIVE means that we want to create a listening socket

    // get socket and address info for listening port
    // - for a listening socket, give NULL as the host name (because the socket is on
    //   the local host)
    error = getaddrinfo(NULL, port, &hint, &info_list);
    if (error != 0) {
        fprintf(stderr, "getaddrinfo: %s\n", gai_strerror(error));
        return -1;
    }

    // attempt to create socket
    for (info = info_list; info != NULL; info = info->ai_next) {
        sfd = socket(info->ai_family, info->ai_socktype, info->ai_protocol);

        // if we couldn't create the socket, try the next method
        if (sfd == -1) {
            continue;
        }

        // if we were able to create the socket, try to set it up for
        // incoming connections;
        //
        // note that this requires two steps:
        // - bind associates the socket with the specified port on the local host
        // - listen sets up a queue for incoming connections and allows us to use accept
        if ((bind(sfd, info->ai_addr, info->ai_addrlen) == 0) &&
            (listen(sfd, BACKLOG) == 0)) {
            break;
        }

        // unable to set it up, so try the next method
        close(sfd);
    }

    if (info == NULL) {
        // we reached the end of result without successfuly binding a socket
	if(con != NULL)
	{
		free(con);
	}
	
        fprintf(stderr, "Could not bind\n");
        return -1;
    }

    freeaddrinfo(info_list);

	struct sigaction act;
	act.sa_handler = handler;
	act.sa_flags = 0;
	sigemptyset(&act.sa_mask);
	sigaction(SIGINT, &act, NULL);

	sigset_t mask;

	sigemptyset(&mask);
	sigaddset(&mask, SIGINT);


    // at this point sfd is bound and listening
    printf("Waiting for connection\n");
	while (running) {
    	// create argument struct for child thread
		con = malloc(sizeof(struct connection));
        con->addr_len = sizeof(struct sockaddr_storage);
        	// addr_len is a read/write parameter to accept
        	// we set the initial value, saying how much space is available
        	// after the call to accept, this field will contain the actual address length

        // wait for an incoming connection
        con->fd = accept(sfd, (struct sockaddr *) &con->addr, &con->addr_len);
        	// we provide
        	// sfd - the listening socket
        	// &con->addr - a location to write the address of the remote host
        	// &con->addr_len - a location to write the length of the address
        	//
        	// accept will block until a remote host tries to connect
        	// it returns a new socket that can be used to communicate with the remote
        	// host, and writes the address of the remote hist into the provided location

        // if we got back -1, it means something went wrong
        if (con->fd == -1) {
            perror("accept");
            continue;
        }

        // temporarily block SIGINT (child will inherit mask)
        error = pthread_sigmask(SIG_BLOCK, &mask, NULL);
        if (error != 0) {
        	fprintf(stderr, "sigmask: %s\n", strerror(error));
        	abort();
        }

		// spin off a worker thread to handle the remote connection
        error = pthread_create(&tid, NULL, echo, con);

		// if we couldn't spin off the thread, clean up and wait for another connection
        if (error != 0) {
            fprintf(stderr, "Unable to create thread: %d\n", error);
            close(con->fd);
            free(con);
            continue;
        }

		// otherwise, detach the thread and wait for the next connection request
        pthread_detach(tid);

        // unblock SIGINT
        error = pthread_sigmask(SIG_UNBLOCK, &mask, NULL);
        if (error != 0) {
        	fprintf(stderr, "sigmask: %s\n", strerror(error));
        	abort();
        }

    }

	puts("No longer listening.");
	for(int i = 0; i<my_list.elems; i++)
	{
		free(my_list.id[i]);
		free(my_list.key[i]);
	}
	free(my_list.id);
	free(my_list.key);
	free(con);
	pthread_detach(pthread_self());
	exit(EXIT_SUCCESS);
	pthread_exit(NULL);
	
    // never reach here
    return 0;
}
int enlist(char *id, char *key)
{
	printf("ENLIST %s %s\n",id,key);
	if(search(id) == NULL)
	{
		my_list.id[my_list.elems] = malloc(strlen(id)+1);
		strcpy(my_list.id[my_list.elems],id);
		my_list.key[my_list.elems] = malloc(strlen(key)+1);
		strcpy(my_list.key[my_list.elems],key);
		my_list.elems++;
		if(my_list.elems == list_size)
		{
			list_size = list_size + 10;
			my_list.id = realloc(my_list.id,list_size * sizeof(char *));
			my_list.key = realloc(my_list.key,list_size * sizeof (char *));
		}
	
		if(strcmp(my_list.id[my_list.elems - 1],id) == 0 && strcmp(my_list.key[my_list.elems - 1],key) == 0)
		{
		
			return 1;
		}
	}
	else
	{
		for(int i =0; i<my_list.elems; i++)
		{
			if(strcmp(my_list.id[i],id) == 0)
			{
				free(my_list.key[i]);
				my_list.key[i] = malloc(strlen(key)+1);
				strcpy(my_list.key[i],key);
				return 1;
			}
		}
	}
	return 0;
	
}
char* search(char *id)
{
	printf("SEARCHE: %s\n",id);
	int i = 0;
	if(my_list.elems == 0)
	{
		return NULL;
	}
	for(i = 0; i<my_list.elems; i++)
	{
		if(strcmp(my_list.id[i],id) == 0)
		{

			return my_list.key[i];
		}
	}
	return NULL;

}
char* delist(char *id)
{
	printf("DELIST: %s\n",id);
	if(search(id) == NULL)
	{
		return NULL;
	}

	int i = 0, j = 0;

	for(i=0; i<my_list.elems; i++)
	{
		if(strcmp(my_list.id[i],id) == 0)
		{
			break;
		}
	}
	char *t1 = my_list.id[i];
	char *t2 = my_list.key[i];
//	char res[strlen(my_list.key[i])+1];
//	strcpy(res,t2);
	for(j=i; j<my_list.elems-1; j++)
	{
		my_list.id[j] = my_list.id[j+1];
		my_list.key[j] = my_list.key[j+1];		
	}
//	free(my_list.id[my_list.elems-1]);
//	free(my_list.key[my_list.elems-1]);
	my_list.id[my_list.elems-1] = NULL;
	my_list.key[my_list.elems-1] = NULL;
	my_list.elems--;
	free(t1);
	return t2;
	

}
#define BUFSIZE 8

void *echo(void *arg)
{
    char host[100], port[10], buf[BUFSIZE + 1];
    struct connection *c = (struct connection *) arg;
    int error, nread;

	// find out the name and port of the remote host
    error = getnameinfo((struct sockaddr *) &c->addr, c->addr_len, host, 100, port, 10, NI_NUMERICSERV);
    	// we provide:
    	// the address and its length
    	// a buffer to write the host name, and its length
    	// a buffer to write the port (as a string), and its length
    	// flags, in this case saying that we want the port as a number, not a service name
    if (error != 0) {
        fprintf(stderr, "getnameinfo: %s", gai_strerror(error));
        close(c->fd);
        return NULL;
    }

    printf("[%s:%s] connection\n", host, port);

int itr = 1;
char op[5];
char ch;
int arrsize = 5,count = 0;
char *length = malloc(arrsize);
int err = 0;
int len;
int keylen;
char* key = NULL;
char *id = NULL;
int res = 0;
char *retval;
FILE *fp = fdopen(c->fd,"w");
    while ((nread = read(c->fd, buf, BUFSIZE)) > 0) {
        buf[nread] = '\0';
        printf("[%s:%s] read %d bytes |%s|\n", host, port, nread, buf);
     /*   if(itr == 1)
        {
          if (nread == 4) {
            if(strcmp(buf,"SET\n") == 0 || strcmp(buf,"GET\n")==0 || strcmp(buf,"DEL\n") == 0)
            {
              itr++;
              strcpy(op,buf);
            }
            else
            {
              printf("BAD MESSAGE");
              break;
            }
          }
        }
        else
        {
          for(i=0; i<nread; i++)
          {
            if(buf[i] == '\n')
            
          }
        }
*/
	if(nread == 4 && ((strcmp(buf,"GET\n") == 0) || (strcmp(buf,"SET\n") == 0) || strcmp(buf,"DEL\n") == 0))
	{
		strcpy(op,buf);
	//	printf("recieved: %s",op);
		do
		{
			nread = read(c->fd,&ch,1);
		//	printf("read %c\n",ch);
			if(nread != 1)
			{
//				printf("nread != 1\n");
				break;
			}
			
			count++;
			if(length == NULL)
			{
				arrsize = 5;
				length = malloc(arrsize);
			}
			else if(count == arrsize)
			{
				arrsize = arrsize + 5;
				length = realloc(length,arrsize);
			}
			if(!isdigit(ch) && ch != '\n' && itr == 1)
			{
				err = 1;
			//	printf("isspace ch\n");
				fprintf(fp,"%s","ERR\nBAD\n");
				fflush(fp);
				break;
			}
			else if(ch == '\n')
			{
				length[count-1] = '\0';
				if(itr == 2)
				{
					if(count > len-2)
					{
						err = 1;
			//			printf("BAD LENGTH\n");
						fprintf(fp,"%s","ERR\nLEN\n");
						fflush(fp);
						break;
					}
			//		printf("FOUND id: %s \n",length);
					if(id == NULL)
					{
						id = malloc(strlen(length)+1);
					}
					strcpy(id,length);
					free(length);
	//				printf("ID: %s \t \n",id);
					length = NULL;
					count = 0;
					itr = 1;
					break;
				}
				if(itr == 1)
				{
	//				printf("FOund length : %s \n", length);
					len = atoi(length);		
					free(length);
					length = NULL;
					count = 0;
					if(strcmp(op,"SET\n") == 0)
					{
						itr = 2;
					}
					else
					{
						break;
					}
				}

			

			}
			/*
			else if(count == 1 && isspace(ch))
			{
				printf("bad\n");
				fprintf(fp,"%s","ERR
						*/
			else
			{
				length[count-1] = ch;
			}



			
		}while(nread > 0);
		if(err==1)
		{
			printf("BAD MESSAGE\n");
			if(length!=NULL)
			{
				free(length);
				length =NULL;
			}
			if(id!=NULL)
			{
				free(id);
				id = NULL;
			}
			if(key != NULL)
			{
				free(key);
				key = NULL;
			}
			break;
		}
		
		if(strcmp(op,"SET\n") == 0)
		{
			keylen = len - strlen(id) - 1;
			if(key == NULL)
			{
				key = malloc(keylen+1);
			}
			nread = read(c->fd,key,keylen);
			if(nread != keylen || key[keylen-1] != '\n')
			{
				printf("BAD key\n");
				fprintf(fp,"%s","ERR\nLEN\n");
				fflush(fp);
				break;
			}
			else
			{
				key[keylen-1] = '\0';
	//			printf("%s\n",key);
			}
		}
		else
		{
			if(id == NULL)
			{
				id = malloc(len+1);	
			}
			nread = read(c->fd,id,len);
			if(id[len-1] != '\n' || nread != len)
			{
			//	printf("BAD id\n");
				fprintf(fp,"%s","ERR\nLEN\n");
				fflush(fp);
				break;
			}
			id[len-1] = '\0';
			printf("%s\n",id);
		}
		
		pthread_mutex_lock(&lock);
		if(strcmp(op,"SET\n") == 0)
		{
		//	printf("op: %s id : %s key : %s\n ",op,id,key);
			res = enlist(id,key);
			if(res == 1)
			{
	//			printf("OKS\n");
				fprintf(fp,"%s","OKS\n");
				fflush(fp);
				
			
			}
			else
			{
				fprintf(fp,"%s","ERR\nSRV\n");
				fflush(fp);
				break;
			}	
		}
		else
		{
			
		//	printf("op: %s id: %s \n",op,id);
			if(strcmp(op,"GET\n") == 0)
			{
				retval = search(id);
				if(retval == NULL)
				{

	//				printf("KNF\n");
					fprintf(fp,"%s","KNF\n");
					fflush(fp);
				}
				else
				{
	//				printf("OKG\n%ld\n%s\n",strlen(retval)+1,retval);
					fprintf(fp,"%s%ld\n%s\n","OKG\n",strlen(retval)+1,retval);
					fflush(fp);
				}
			}
			else if(strcmp(op,"DEL\n") == 0)
			{
				retval = delist(id);
				if(retval == NULL)
				{
	//				printf("KNF\n");
					fprintf(fp,"%s","KNF\n");
					fflush(fp);
				}
				else
				{
	//				printf("OKD\n%ld\n%s\n",strlen(retval)+1,retval);
					fprintf(fp,"%s%ld\n%s\n","OKD\n",strlen(retval)+1,retval);
					fflush(fp);
					free(retval);
					
					
					
				}
				

			}
		}
		pthread_mutex_unlock(&lock);

	}
	else
	{
		
	//	printf("BAD MESSAGE\n");
		fprintf(fp,"%s","ERR\nBAD\n");
		break;

	}
	if(id != NULL)
	{
		free(id);
		id = NULL;
	}
	if(key != NULL)
	{
		free(key);
		key = NULL;
	}


    }

    printf("[%s:%s] got EOF\n", host, port);
    if(length != NULL)
    {
	    free(length);
	    length = NULL;

    }
    if(id != NULL)
    {
	    free(id);
	    id = NULL;
    }
    if(key != NULL)
    {
	    free(key);
	    key = NULL;
    }
    fclose(fp);
    close(c->fd);
    free(c);
    return NULL;
}

