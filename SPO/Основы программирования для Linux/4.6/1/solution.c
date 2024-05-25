#include <stdio.h>

char cmd[1024];

int main(int argc, char *argv[]) {
  sprintf(cmd, "%s %s", argv[1], argv[2]);
  
  FILE *f = popen(cmd, "r");
  if (f != NULL) {
    int count = 0;
    char c;
    while ((c = fgetc(f)) != EOF) {
      if(c == '0') count++;
    }
    printf("%d\n", count);    
    fclose(f);
  }
  return 0;
}
