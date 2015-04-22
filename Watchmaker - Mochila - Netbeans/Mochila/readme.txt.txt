Para ejecutar el proyecto: descargar el IDE Netbeans, abrir el proyecto y presionar "Run".

Para la implementaci�n se codificaron 4 clases Java:

MapColoring:
Esta clase posee la funci�n Main, y es la encargada de crear el "engine" evolutivo y
de ejecutar la evoluci�n.
Se utiliza la clase MapColoringFactory para crear mapas al azar.
Se crean dos operadores evolutivos: MapColoringMutation
(implementado en la clase del mismo nombre) y el cruzamiento ListCrossover (implementado 
en el Framework Watchmaker). Por otro lado se asocia la funci�n de fitness implmentada en
la clase MapEvaluator, y se elige una selecci�n por Ruleta y un generador de n�meros 
aleatorios (ambos implementados en el Framework, bajo los nombres de RouletteWheelSelection
y MersenneTwisterRNG correspondientemente). Por �ltimo se agrega un EvolutionLogger, que
imprime el mejor fitness de cada generaci�n.

MapColoringFactory:
Esta clase es la encargada de generar una poblaci�n inicial de mapas. Esto se realiza asignando
un color random a cada zona.

MapColoringMutation:
Esta clase es la encargada de mutar un mapa. Para ello, se elije una zona al azar dentro 
del individuo a mutar y se cambia su color asociado, tambi�n de manera aleatoria. 

MapEvaluator: 
Esta clase es la encargada de evaluar el mapa. El primer paso que realiza es cargar el
archivo de adyacencias entre zonas en una lista de listas (donde la primer lista se 
corresponde a una zona y la segunda posee sus adyacencias). En segundo lugar, implementa la
funci�n getFitness, la cual recibe un individuo como par�metro y devuelve su valor de fitness.
Para ello, cuenta cada adyacencia entre zonas del mismo color y retorna dicho valor. 

