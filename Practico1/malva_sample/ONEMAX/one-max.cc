

void Solution::initialize()
{
	for (int i=0;i<_pbm.dimesion();i++)
		_var[i]=rand_int(0,1);
}


double Solution::fitness()
{
	double fitness = 0.0;

	for (int i=0;i<_var.size();i++)
		fitness += _var[i];

	return fitness;
}
