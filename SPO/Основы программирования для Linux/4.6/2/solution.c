#include <fcntl.h>
#include <stdio.h>
#include <sys/select.h>
#include <unistd.h>

int main(void) {
  int rc, maxfd, fd[2], sum[2] = {0, 0};
  struct timeval tm = {5, 0};
  char buff[32];
  fd_set watchset; /* fd для чтения */
  fd_set inset; /* обновляется select() */
  /* открыть оба канала */
  if ((fd[0] = open("in1", O_RDONLY | O_NONBLOCK)) < 0 ||
    (fd[1] = open("in2", O_RDONLY | O_NONBLOCK)) < 0) {
    perror("open");
    return 1;
  }
  /* начать чтение из обоих файловых дескрипторов*/
  FD_ZERO(&watchset);
  FD_SET(fd[0], &watchset);
  FD_SET(fd[1], &watchset);
  /* найти максимальный файловый дескриптор */
  maxfd = fd[0] > fd[1] ? fd[0] : fd[1];
  /* пока наблюдаем за одним из fd[0] или fd[1] */
  while (FD_ISSET(fd[0], &watchset)
      || FD_ISSET(fd[1], &watchset)) {
    /* здесь копируем watchset, потому что select() обновляет его */
    inset = watchset;
    if (select(maxfd + 1, &inset, NULL, NULL, &tm) < 0) {
      perror("select");
      return 1;
    }
    /* проверить, какой их файловых дескрипторов
     * готов для чтения из него */
    for (int i = 0; i < 2; i++) {
      if (FD_ISSET(fd[i], &inset)) {
        /* fd[i] готов для чтения, двигаться дальше... */
        rc = read(fd[i], buff, sizeof(char));
        if (rc < 0) {
          perror("read");
          return 1;
        } else if (!rc) {
          /* этот канал закрыт, не пытаться
           * читать из него снова */
          close(fd[i]);
          FD_CLR(fd[i], &watchset);
        } else {
          buff[rc] = '\0';
          sscanf(buff, "%d", &rc);
          sum[i] += rc;
        }
      }
    }
  }
  printf("%d\n", sum[0] + sum[1]);
  return 0;
}
