#!/bin/bash

./clean.sh
pdflatex informe.tex
pdflatex informe.tex
bibtex informe
pdflatex informe.tex
pdflatex informe.tex
./clean.sh
