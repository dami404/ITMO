#include <stdio.h>
#include <stdlib.h>
#include <signal.h>
#include <unistd.h>

int sig1 = 0;
int sig2 = 0;

void sigusr1_handler(int signo) { sig1++; }  
void sigusr2_handler(int signo) { sig2++; }

void sigterm_handler(int signo) {
  printf("%d %d\n", sig1, sig2);
  exit(0);
}

int main(void) 
{
  signal(SIGUSR1, sigusr1_handler);
  signal(SIGUSR2, sigusr2_handler);
  signal(SIGTERM, sigterm_handler);
  while (1) {
    usleep(5000);
  }
  return 0;
}
