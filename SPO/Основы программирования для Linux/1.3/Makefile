.PHONY: solution clean

solution: solution.c
	gcc $^ -fPIC -ldl -Wl,-rpath,. -o $@
clean:
	rm -rf *.o *.so