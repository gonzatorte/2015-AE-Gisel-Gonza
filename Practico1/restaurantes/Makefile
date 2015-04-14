
# Malva Configuration.
MALLBA_DIR=/home/gonzalo/Documentos/AlgEvol/Lab/Recursos/malva

# MPI library
MPI_BIN=/usr/bin

# Malva information
MALLBA_INC=${MALLBA_DIR}/src
MALLBA_LIB=${MALLBA_DIR}/lib
MALLBA_SRC=${MALLBA_DIR}/src
MALLBA_REP=${MALLBA_DIR}/rep

# Run variables
CXX=$(MPI_BIN)/mpic++

# Other options: -g for debugging and -pg for profiling
CPPFLAGS=-I$(MALLBA_INC) -Wno-deprecated -O3
LDFLAGS=-L$(MALLBA_LIB)
LOADLIBES=-lmallba -lm

default:
	@echo "Make what? (all, libs, skeletons,  clean)"

all:
	(cd inc ; rm -f Mallba)
	(cd inc ; ./inc.env)
	(cd src ; make all)
	(cd lib ; make all)
	(cd rep ; make all)

libs:
	(cd inc ; rm -f Mallba)
	(cd inc ; ./inc.env)
	(cd src ; make all)
	(cd lib ; make all)

skeletons:
	(cd rep ; make all)

clean:
	(cd inc ; rm -f Mallba)
	(cd src ; make clean)
	(cd lib ; make clean)
	(cd rep ; make clean)

all: main

clean:
	rm -f MainLan MainSeq MainWan *.o *% *~

main: newGA.req.o newGA.pro.o main.o
	$(CXX) $(LDFLAGS) $^ $(LOADLIBES) $(CPPFLAGS) -o $@

run:
	./main newGA.cfg ../../ProblemInstances/ONEMAX-instances/onemax10.txt res/sol.txt