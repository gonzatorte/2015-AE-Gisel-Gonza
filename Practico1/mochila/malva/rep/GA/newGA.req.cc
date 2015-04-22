#ifndef INC_REQ_newGA
#define INC_REQ_newGA
#include "newGA.hh"
#include <math.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

skeleton newGA
{

	// Problem ---------------------------------------------------------------

	Problem::Problem ():_dimension(0),_pesos(NULL),_costos(NULL),_capacidad(0)
	{}

	ostream& operator<< (ostream& os, const Problem& pbm)
	{
		os << endl << endl << "Number of Variables " << pbm._dimension
		   << endl;

		//Imprimo los costos de los aperitivos
		os<<"Costos: "<<endl;
		for (int i=0;i<pbm._dimension;i++){
			os<< "\t" << pbm._costos[i]<<",";
		}
		os<<endl;
		os<<"Pesos: "<<endl;
		for (int i=0;i<pbm._dimension;i++){
			os<< "\t" << pbm._pesos[i]<<",";
		}
		os<<endl;
		os<<"Capacidad: " << pbm._capacidad <<endl;
		os<<endl;
		return os;
	}

	istream& operator>> (istream& is, Problem& pbm)
	{
		char buffer[MAX_BUFFER];
		int i;

		is.getline(buffer,MAX_BUFFER,'\n');
		sscanf(buffer,"%d",&pbm._dimension);
		pbm._costos = new float [pbm._dimension]; 
		pbm._pesos = new float [pbm._dimension]; 
		for (int i=0; i<pbm._dimension; i++){
			is.getline(buffer,MAX_BUFFER,'\n');
			int ind;
			sscanf(buffer,"%d %f %f", &ind, &(pbm._costos[i]), &(pbm._pesos[i]));
			// float aux;
			// sscanf(buffer,"%d %f %f", &ind, &aux, &aux);
			// sscanf(buffer,"%d %f %f", &ind, &(pbm._costos[ind]), &(pbm._pesos[ind]));
		}
		is.getline(buffer,MAX_BUFFER,'\n');
		sscanf(buffer,"%d",&pbm._capacidad);
		return is;
	}

	float * Problem::costos() const{
		return _costos;
	}

	float * Problem::pesos() const{
		return _pesos;
	}

	float * Problem::capacidad() const{
		return _capacidad;
	}

	bool Problem::operator== (const Problem& pbm) const
	{
		if (_dimension!=pbm.dimension()) return false;
		return true;
	}

	bool Problem::operator!= (const Problem& pbm) const
	{
		return !(*this == pbm);
	}

	Direction Problem::direction() const
	{
		//return maximize;
		return minimize;
	}

	int Problem::dimension() const
	{
		return _dimension;
	}

	Problem::~Problem()
	{
		delete [] _costos;
		delete [] _pesos;
	}

	// Solution --------------------------------------------------------------

	Solution::Solution (const Problem& pbm):_pbm(pbm),_var(pbm.dimension())
	{}

	const Problem& Solution::pbm() const
	{
		return _pbm;
	}

	Solution::Solution(const Solution& sol):_pbm(sol.pbm())
	{
		*this=sol;
	}

	istream& operator>> (istream& is, Solution& sol)
	{
		for (int i=0;i<sol.pbm().dimension();i++)
			is >> sol._var[i];
		return is;
	}

	ostream& operator<< (ostream& os, const Solution& sol)
	{
		for (int i=0;i<sol.pbm().dimension();i++)
			os << " " << sol._var[i];
		return os;
	}

	NetStream& operator << (NetStream& ns, const Solution& sol)
	{
		for (int i=0;i<sol._var.size();i++)
			ns << sol._var[i];
		return ns;
	}

	NetStream& operator >> (NetStream& ns, Solution& sol)
	{
		for (int i=0;i<sol._var.size();i++)
			ns >> sol._var[i];
		return ns;
	}

 	Solution& Solution::operator= (const Solution &sol)
	{
		_var=sol._var;
		return *this;
	}

	bool Solution::operator== (const Solution& sol) const
	{
		if (sol.pbm() != _pbm) return false;
		for(int i = 0; i < _var.size(); i++)
			if(_var[i] != sol._var[i]) return false;
		return true;
	}

	bool Solution::operator!= (const Solution& sol) const
	{
		return !(*this == sol);
	}

	void Solution::initialize()
	{
		// ToDo
		for (int i=0;i<_pbm.dimension();i++){
			double unit_cost = this->pbm().costos()[i];
			int maximo_var = ceil(this->pbm().costo_objetivo() / unit_cost);
			// cout << "Testing 2 " << maximo_var << endl;
			_var[i]=rand_int(0,maximo_var);
		}
		// cout << "Individuo" << endl;
		// for (int i=0;i<_pbm.dimension();i++){
		// 	cout << _var[i];
		// }
		// cout << endl;
	}

	double Solution::_fitness_fun(Rarray<int> & genoma, float * costos, float * pesos, float capacidad)
	{
		double costo_total = 0.0;
		double costo_peor = 0.0;

		for (int i=0;i<genoma.size();i++){
			double unit_cost = costos[i];
			int maximo_var = ceil(obj / unit_cost);
			costo_peor = costo_peor + (maximo_var * unit_cost);
			costo_total = costo_total + unit_cost * genoma[i];
		}
		// cout << "B Testing: " << endl;

		double distancia_objetivo = costo_total - obj;
		if (distancia_objetivo < 0)
			distancia_objetivo = -distancia_objetivo;
		// cout << costo_total << endl;
		// cout << obj << endl;
		// cout << distancia_objetivo << endl;
		double fitness = 1 / (distancia_objetivo + 1);
		// double fitness = costo_peor - distancia_objetivo;
		// return fitness;
		return distancia_objetivo;
	}

	double Solution::fitness ()
	{
		// float costos [] = {15.2,18.1,5.1};
		// Rarray<int> genoma = Rarray<int>(3);
		// genoma[0] = 0;
		// genoma[1] = 0;
		// genoma[2] = 3;
		// cout << "B Testing: " << endl;
		// cout << _fitness_fun(genoma, costos, 15.2) << endl;
		// cout << "E Testing: " << endl;
		return _fitness_fun(_var, pbm().costos(), pbm().pesos(), pbm().capacidad());
	}

	char *Solution::to_String() const
	{
		return (char *)_var.get_first();
	}

	void Solution::to_Solution(char *_string_)
	{
		int *ptr=(int *)_string_;
		for (int i=0;i<_pbm.dimension();i++)
		{
			_var[i]=*ptr;
			ptr++;
		}
	}

	unsigned int Solution::size() const
	{
		return (_pbm.dimension() * sizeof(int));
	}


	int& Solution::var(const int index)
	{
		return _var[index];
	}


	Rarray<int>& Solution::array_var()
	{
		return _var;
	}

	Solution::~Solution()
	{}

	// UserStatistics -------------------------------------------------------

	UserStatistics::UserStatistics ()
	{}

	ostream& operator<< (ostream& os, const UserStatistics& userstat)
	{
		os << "\n---------------------------------------------------------------" << endl;
		os << "                   STATISTICS OF TRIALS                   	 " << endl;
		os << "------------------------------------------------------------------" << endl;

		for (int i=0;i< userstat.result_trials.size();i++)
		{
			os << endl
			   << userstat.result_trials[i].trial
			   << "\t" << userstat.result_trials[i].best_cost_trial
			   << "\t\t" << userstat.result_trials[i].worst_cost_trial
			   << "\t\t\t" << userstat.result_trials[i].nb_evaluation_best_found_trial
			   << "\t\t\t" << userstat.result_trials[i].nb_iteration_best_found_trial
			   << "\t\t\t" << userstat.result_trials[i].time_best_found_trial
			   << "\t\t" << userstat.result_trials[i].time_spent_trial;
		}
		os << endl << "------------------------------------------------------------------" << endl;
		return os;
	}

	UserStatistics& UserStatistics::operator= (const UserStatistics& userstats)
	{
		result_trials=userstats.result_trials;
		return (*this);
	}

	void UserStatistics::update(const Solver& solver)
	{
		if( (solver.pid()!=0) || (solver.end_trial()!=true)
		  || ((solver.current_iteration()!=solver.setup().nb_evolution_steps())
		       && !terminateQ(solver.pbm(),solver,solver.setup())))
			return;

		struct user_stat *new_stat;

		if ((new_stat=(struct user_stat *)malloc(sizeof(struct user_stat)))==NULL)
			show_message(7);
		new_stat->trial         		 		 = solver.current_trial();
		new_stat->nb_evaluation_best_found_trial = solver.evaluations_best_found_in_trial();
		new_stat->nb_iteration_best_found_trial  = solver.iteration_best_found_in_trial();
		new_stat->worst_cost_trial     		 	 = solver.worst_cost_trial();
		new_stat->best_cost_trial     		 	 = solver.best_cost_trial();
		new_stat->time_best_found_trial		 	 = solver.time_best_found_trial();
		new_stat->time_spent_trial 		 		 = solver.time_spent_trial();

		result_trials.append(*new_stat);
	}

	void UserStatistics::clear()
	{
		result_trials.remove();
	}

	UserStatistics::~UserStatistics()
	{
		result_trials.remove();
	}

// Intra_operator  --------------------------------------------------------------

	Intra_Operator::Intra_Operator(const unsigned int _number_op):_number_operator(_number_op),probability(NULL)
	{}

	unsigned int Intra_Operator::number_operator() const
	{
		return _number_operator;
	}

	Intra_Operator *Intra_Operator::create(const unsigned int _number_op)
	{
		switch (_number_op)
		{
			case 0: return new Crossover;break;
			case 1: return new Mutation();break;
		}
	}

	ostream& operator<< (ostream& os, const Intra_Operator& intra)
	{
		switch (intra.number_operator())
		{
			case 0: os << (Crossover&)intra;break;
			case 1: os << (Mutation&)intra;break;
		}
		return os;
	}

	Intra_Operator::~Intra_Operator()
	{}

//  Crossover:Intra_operator -------------------------------------------------------------

	Crossover::Crossover():Intra_Operator(0)
	{
		probability = new float[1];
	}

	void Crossover::cross(Solution& sol1,Solution& sol2) const // dadas dos soluciones de la poblacion, las cruza
	{
		//Usamos cruzamiento de 1 punto (SPX)
		int i=0;
		Rarray<int> aux(sol1.pbm().dimension());
		aux=sol2.array_var();

		int limit=rand_int(0,sol1.pbm().dimension()-1);

		for (i=0;i<limit;i++)
			sol2.var(i)=sol1.var(i);
		for (i=0;i<limit;i++)
			sol1.var(i)=aux[i];
	}

	void Crossover::execute(Rarray<Solution*>& sols) const
	{
		for (int i=0;i+1<sols.size();i=i+2)
		 	if (rand01()<=probability[0]) cross(*sols[i],*sols[i+1]);
	}

	ostream& operator<< (ostream& os, const Crossover&  cross)
	{
		 os << "Crossover." << " Probability: "
                    << cross.probability[0]
		    << endl;
		 return os;
	}

	void Crossover::RefreshState(const StateCenter& _sc) const
	{
		_sc.set_contents_state_variable("_crossover_probability",(char *)probability,1,sizeof(float));
	}

	void Crossover::UpdateFromState(const StateCenter& _sc)
	{
		 unsigned long nbytes,length;
		 _sc.get_contents_state_variable("_crossover_probability",(char *)probability,nbytes,length);
	}

	void Crossover::setup(char line[MAX_BUFFER])
	{
		int op;
		sscanf(line," %d %f ",&op,&probability[0]);
		assert(probability[0]>=0);
	}

	Crossover::~Crossover()
	{
		delete [] probability;
	}

	//  Mutation: Sub_operator -------------------------------------------------------------

	Mutation::Mutation():Intra_Operator(1)
	{
		probability = new float[2];
	}

	void Mutation::mutate(Solution& sol) const
	{
		for (int i=0;i<sol.pbm().dimension();i++)
		{
			if (rand01()<=probability[1])
			{
				double unit_cost = sol.pbm().costo_aperitivos()[i];
				int maximo_var = ceil(sol.pbm().costo_objetivo() / unit_cost);
				sol.var(i)=rand_int(0,maximo_var);
			}
		}
	}

	void Mutation::execute(Rarray<Solution*>& sols) const
	{
		for (int i=0;i<sols.size();i++)
			if(rand01() <= probability[0])	mutate(*sols[i]);
	}

	ostream& operator<< (ostream& os, const Mutation&  mutation)
	{
		os << "Mutation." << " Probability: " << mutation.probability[0]
		   << " Probability1: " << mutation.probability[1]
		   << endl;
		return os;
	}

	void Mutation::setup(char line[MAX_BUFFER])
	{
		int op;
		sscanf(line," %d %f %f ",&op,&probability[0],&probability[1]);
		assert(probability[0]>=0);
		assert(probability[1]>=0);
	}

	void Mutation::RefreshState(const StateCenter& _sc) const
	{
		_sc.set_contents_state_variable("_mutation_probability",(char *)probability,2,sizeof(probability));
	}

	void Mutation::UpdateFromState(const StateCenter& _sc)
	{
		unsigned long nbytes,length;
		_sc.get_contents_state_variable("_mutation_probability",(char *)probability,nbytes,length);
	}

	Mutation::~Mutation()
	{
		delete [] probability;
	}

// StopCondition_1 -------------------------------------------------------------------------------------

	StopCondition_1::StopCondition_1():StopCondition()
	{}

	bool StopCondition_1::EvaluateCondition(const Problem& pbm,const Solver& solver,const SetUpParams& setup)
	{
		return (false);
	}

	StopCondition_1::~StopCondition_1()
	{}

	//------------------------------------------------------------------------
	// Specific methods ------------------------------------------------------
	//------------------------------------------------------------------------

	bool terminateQ (const Problem& pbm, const Solver& solver,
			 const SetUpParams& setup)
	{
		StopCondition_1 stop;
		return stop.EvaluateCondition(pbm,solver,setup);
	}
}
#endif

