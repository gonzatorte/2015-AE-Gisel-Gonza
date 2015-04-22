import pdb

# pdb.set_trace()

genoma = [1]
costo = [15.1]
obj = 15.2


costo_total = 0.0;
costo_peor = 0.0;

for i in range(0, len(genoma)):
	unit_cost = costo[i];
	maximo_var = int(obj / unit_cost);
	costo_peor = costo_peor + maximo_var * unit_cost;
	costo_total = costo_total + unit_cost * genoma[i];


distancia_objetivo = abs(costo_total - obj);
fitness = 1 / (distancia_objetivo + 1);
# fitness = costo_peor - distancia_objetivo;

print fitness;

