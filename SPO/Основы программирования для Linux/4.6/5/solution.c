#include <sys/types.h>
#include <sys/ipc.h>
#include <sys/shm.h>
#include <stdio.h>
#include <stdlib.h>

#define SHMSZ 1000

int main(int argc, char *argv[]) {
  int shmid[3], *shm[3];
  key_t key = ftok(argv[0], argc);

  if ((shmid[0] = shmget(key, SHMSZ, IPC_CREAT | 0666)) < 0
      || (shmid[1] = shmget(atol(argv[1]), SHMSZ, IPC_CREAT | 0666)) < 0
      || (shmid[2] = shmget(atol(argv[2]), SHMSZ, IPC_CREAT | 0666)) < 0)
  {
    perror("shmget");
    exit(1);
  }

  if ((shm[0] = shmat(shmid[0], NULL, 0)) == NULL
      || (shm[1] = shmat(shmid[1], NULL, 0)) == NULL
      || (shm[2] = shmat(shmid[2], NULL, 0)) == NULL)
  {
    perror("shmat");
    exit(1);
  }

  for (int i = 0; i < 100; i++) {
    shm[0][i] = shm[1][i] + shm[2][i];
  }

  printf("%d\n", key);

  return 0;
}
