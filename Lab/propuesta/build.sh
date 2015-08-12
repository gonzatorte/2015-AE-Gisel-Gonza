#!/bin/bash

# cat informe.tex | sed -n "s/[á]/\\\'a/ p" > informe.tex.tmp

./clean.sh
pdflatex informe.tex
pdflatex informe.tex
bibtex informe
pdflatex informe.tex
pdflatex informe.tex
./clean.sh
