#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

#define SIZE 1024

char path[SIZE], line[SIZE];

int get_ppid(int pid) {
  sprintf(path, "/proc/%d/status", pid);
  FILE *file = fopen(path, "rb");
  int n = 0;
  while (n != 7) {
    n++;
    fgets(line, SIZE, file);
  }
  fclose(file);
  sscanf(line, "PPid: %d", &pid);
  return pid;
}

int main(int argc, char *argv[]) {
  int pid = atoi(argv[1]);
  while (pid) {
    printf("%d\n", pid);
    pid = get_ppid(pid);
  }
  return 0;
}
