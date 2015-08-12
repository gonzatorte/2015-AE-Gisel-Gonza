
El scheduler es quien resuelve las cosas efectivamente. Comprende:
	implementación
	casos de prueba

El instance generator es quien habla con googleMapEngine api para generar mapas con distribuciones realistas de instancias. Comprende:
	implementación
	casos de prueba

El analizer posiblemente sean scripts de octave y bash para ejecutar el scheduler y hacer plots y cosas por el estilo en base a los resultados
	Incluye casos de ejemplo básicos


Por partes:
	Implementacion:
		La instancia del problema tiene que poder llegar de manera online
			tambien puede ser dada en el arranque, para debugear, como una serie de eventos de arribos
			definir formato para serializacion de estado... no será mucho mas dificil
		Investigar GoogleMatrixApi y GooglePlacesApi.
		Como hacer para la visualización de las soluciones? QGIS, KML, ó algo de Google?
		Definir formato de archivo de config para la config parametrica
		Profiling y optimizacion


Ver si hacer la parte del cluster de facultad o dejarlo como opcional el tema de las islas.

Formato de archivo de entrada de eventos
<Tasa de arribos>
<lista de eventos, arribos y resoluciones>
<orden de llegado del evento, tipo de evento (resolucion de pedido, llegada de pedido, abandono de mandadero, agregado de mandadero), lugar del evento>

RECOMENDACIONES DEL PROFE:
	EL Título debe ser descriptivo

	El abstract debe especificar el problema que se plantea resolver (o sea, el informe anterior no tenía pq tener abstract aún).
	La formulación matemática tal cual ahora está no es muy fina. Tomar en vez del minimo el promedio o algo así.

	Usar un torneo de tamaño pequeño y fijo, es la recomendación

	No nos compliquemos con el asunto de islas, puede ser dificil

	No se menciona el greedy con el que se va a comparar

	El análisis paramétrico no tiene pq ser tan detallado, fijar algunos a ojo y hacer el analisi sobre los otros.

	Agregar algunas correcciones en el informe:
		En la justificacion de pq usar AE:
			1) Debería mencionar que el usar metodos exactos para instancias de tamaño grande demora demasiado
			2) Esta mal la justificacion de usar inicializacion aleatorea
		Hablar de AE y no AG.