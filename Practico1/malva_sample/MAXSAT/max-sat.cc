

istream& operator>> (istream& is, Problem& pbm)
{
      int l,n;

      is >> pbm._numvar >> pbm._numclause >> pbm._lenclause;
      n = pbm._lenclause;
      pbm._clauses = new int*[pbm._numclause];
      // read clauses
      for (int i = 0; i < pbm._numclause; i++)
      {
          pbm._clauses[i] = new int[n];
          for(int j = 0; j < n;j++)
          {
              is >> l;
              pbm._clauses[i][j] = l;
          }
          is >> l;
      }
}



void Solution::initialize()
{
	for (int i=0;i<_pbm.numvar();i++)
		_var[i]=rand_int(0,1);
}


double Solution::fitness()
{
	double fitness = 0.0;
	int acum = 0;
	for(int i = 0; i < _pbm.numclause(); i++)
	{
		int *rl = _pbm.clause(i);
		acum = 0;
		for(int j = 0; (j < _pbm.lenclause()) && (acum != 1);j++)
		{
			if( ((rl[j] < 0) && (_var[(int)abs(rl[j])-1] == 0))
			|| ((rl[j] > 0) && (_var[rl[j]-1] == 1)) )
				acum = 1;
		}
		fitness += acum;
	}
	return fitness;
}
