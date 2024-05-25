#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

#define SIZE 1024

char path[SIZE];
char line[SIZE];

int main()
{
  sprintf(path, "/proc/%d/status", getpid());

  FILE *file = fopen(path, "rb");
  
  int iteration = 0;
  while (iteration != 7) { 
    iteration++;
    fgets(line, SIZE, file);
  }

  int pid;
  sscanf(line, "PPid:	%d\n", &pid);
  printf("%d\n", pid);

  return 0;
}
