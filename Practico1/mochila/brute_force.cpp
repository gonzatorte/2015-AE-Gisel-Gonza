#include <stdio.h>
#include <iostream>

using namespace std;

void resolve(unsigned int size, int * & padres, int * & pesos, int * & costos, int & peso_res, int & costo_res)
{
	cout << "exec:" << size << " " << peso_res << endl;
	if (size == 0)
	{
		if (peso_res < 0)
		{
			costo_res = -10000;
		}
	}
	else
	{
		int peso_res2 = peso_res;
		int costo_res2 = costo_res;
		resolve(size-1, padres, pesos, costos, peso_res2, costo_res2);

		int peso_res1 = peso_res - pesos[size-1];
		int costo_res1 = costo_res + costos[size-1];
		if (peso_res1 >= 0)
		{
			resolve(size-1, padres, pesos, costos, peso_res1, costo_res1);
			if (costo_res1 > costo_res2)
			{
				padres[size-1] = 1;
				costo_res = costo_res1;
			}
			else
			{
				padres[size-1] = 0;
				costo_res = costo_res2;
			}
		}
		else
		{
			padres[size-1] = 0;
			costo_res = costo_res2;
		}
	}
}

int main (int argc, char** argv){
	int peso_limite = 12;
	int costo_res = 0;
	// const int pesos [] = {5,5,3};
	// const int costos [] = {10,12,8};

	int * pesos = new int [3];
	pesos[0] = 5;
	pesos[1] = 5;
	pesos[2] = 3;
	int * costos = new int [3];
	costos[0] = 10;
	costos[1] = 12;
	costos[2] = 8;

	int * padres = new int [3];

	resolve(3, padres, pesos, costos, peso_limite, costo_res);

	cout << peso_limite << endl;
	cout << costo_res << endl;
	for (int i=0 ; i < 3; i++)
		cout << padres[i] << " - ";
	cout << endl;

	return 0;
}
