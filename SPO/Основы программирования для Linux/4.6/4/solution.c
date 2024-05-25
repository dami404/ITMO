
#include <sys/types.h>
#include <stdlib.h>
#include <stdio.h>
#include <signal.h>
#include <unistd.h>

int flag = 1;

void sigurg_handler(int signo) {
  printf("%d\n", getpid());
  flag = 0;
}

int main(void)
{
  pid_t pid = fork();

  if (pid == -1)
    return -1;

  if (pid != 0)
    exit(EXIT_SUCCESS);

  if (setsid() == -1)
    return -1;

  if (chdir("/") == -1)
    return -1;

  printf("%d\n", pid);

  close(0);
  close(1);
  close(2);

  usleep(500);
  signal(SIGURG, sigurg_handler);

  while (flag) { }
  return 0;
}
