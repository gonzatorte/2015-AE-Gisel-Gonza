#!/bin/bash

for i in $(seq 1 20) ; do
	./gen 100 50 1 $i
	mv test.in "problems/test_${i}.in"
done

for i in $(seq 1 20) ; do
	./ejercicio2 "problems/test_${i}.in"
done
