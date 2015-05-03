#!/bin/bash

for i in $(seq 1 20) ; do
	./gen 100 50 1 $i
	mv test.in "problems/test_${i}.in"
done

for i in $(seq 1 20) ; do
	./ejercicio2 "problems/test_${i}.in" "${i}" 2> problems/test_${i}.err > problems/test_${i}.out
	mv solucion.in problems/solucion_${i}.in
	MAX=$(tail -n 1 problems/test_${i}.in)
	RES=$(cat problems/solucion_${i}.in | sed -n '3 p')
	if [ $RES -gt $MAX ]; then
		echo "PROBLEMA EN ${i}"
	fi
done
