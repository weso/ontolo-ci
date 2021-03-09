![](./images/logos_feder.png)

| Entregable     | Control de Versiones sobre ontologías OWL - Sistema de integración continua                           |
| -------------- | ------------------------------------------------------------ |
| Fecha          | 03/03/2021                                               |
| Proyecto       | [ASIO](https://www.um.es/web/hercules/proyectos/asio) (Arquitectura Semántica e Infraestructura Ontológica) en el marco de la iniciativa [Hércules](https://www.um.es/web/hercules/) para la Semántica de Datos de Investigación de Universidades que forma parte de [CRUE-TIC](https://tic.crue.org/hercules/) |
| Módulo         | Infraestructura Ontológica                                   |
| Tipo           | Método y Software                                        |
| Objetivo       | Este documento consiste en la especificación del sistema de sincronización continua de ontologías. |
| Estado         | **100%**  |
| Próximos pasos ||
|Repositorio de Software Asociado|https://github.com/HerculesCRUE/ontolo-ci |<!-- slide -->



# Módulo de Integración continua de ontologías 

## Introducción y objetivos 
Este documento incluye la documentación arquitectónica para el módulo de integración continua de ontologías, en adelante llamado ontolo-ci, que forma parte de la infraestructura ontológica del Proyecto Hércules.

La estructura de este documento sigue la plantilla  [arc42](https://arc42.org/) para la documentación de arquitecturas de software y sistemas.

### Resumen de Requisitos
El objetivo general de ontolo-ci es realizar una validación continua de las ontologías contenidas en un sistema de control de versiones mediante un sistema de test basado en [Shape Expressions](https://shex.io/).

Puede encontrar un análisis más completo de los requisitos del sistema en la sección __Análisis de requisitos__.

### Objetivos de calidad
En esta sección enumeraremos los objetivos de máxima calidad para la arquitectura del sistema:

| Prioridad | Objetivo | Escenario |
| ---- | ----------- | -------- |
| 1 | Fleibilidad |  Aunque la implementación inicial del sistema funcionará a través del sistema de control de versiones GitHub, se deberá poder extender a otros sistemas de control de versiones de manera sencilla.|
| 1 | Tolerancia a fallos | En caso de que falle uno de los componentes que rodean al sistema de integración continua, el sistema debe poder seguir funcionando y mantener los demás objetivos de calidad siempre que sea posible. |

### Stakeholders
 Role/Name   | Description                   | Expectations              |
| ----------- | ------------------------- | ------------------------- |
| Expertos de dominio | Usuario que modifica el contenido de la ontología a través de la interfaz de usuario proporcionada por el servicio de publicación de ontologías. | Cuando se realiza un cambio a través de la interfaz de usuario, el contenido de las ontologías en el sistema de control de versiones debe ser coherente con estos cambios. |
| Ingeniero de ontologías | Usuario que modifica el contenido de la ontología directamente desde el sistema de control de versiones. | Cuando se modifica la ontología sobre el sistema de control de versiones, el sistema de integración continua debe realizar una ejecución automática de los test que validan la ontología |


## Restricciones de Arquitectura
| Restricción | Descripción                            |
|:---------:|----------------------------------------|
|     R1    | El sistema debe ser desarrollado bajo la licencia [GNU General Public License v3.0](https://www.gnu.org/licenses/gpl-3.0.html). |
|     R2    | El sistema debe ser independiente de la plataforma y debe poder ejecutarse en los principales sistemas operativos  (Windows™, Linux, and Mac-OS™). |
|     R3    | El sistema debe poder ejecutarse desde la linea de comandos |
|     R4    | El sistema de versiones de control utilizado para almacenar las ontologías estará basado en git. |

## Visión del Sistema y Contexto
Los distintos módulos software que componen un proyecto suelen utilizar sistemas de integración continua que permiten mantener el software continuamente testeado ejecutando los test de manera automática cada vez que se produzcan cambios sobre el software.

La integración continua de software dispone de un gran ecosistema de herramientas como [Travis](https://travis-ci.com/plans) o [Circle-ci](https://circleci.com/) que nos permiten llevar a cabo dicho propósito. Sin embargo, a la hora de testear ontologías, no existe ninguna herramienta que nos permita realizar un proceso de integración continua de la misma. Es entonces cuando surge la necesidad de crear un sistema que cumnpla dicha necesidad.

Ontolo-ci es un sistema de integración continua de ontologías inspirado en [Travis](https://travis-ci.com/plans), que permite la ejecución de test para ontologías de manera automática sobre repositorios de GitHub. Las pruebas que realiza ontolo-ci sobre la ontología están basadas en test definidos mediante [Shape Expressions](https://shex.io/).

## Estrategía de solución
Para construir ontolo-ci y dar solución al problema planteado anteriormente es la creación de una API rest que sea capaz de:
 * Escuchar los cambios que se produzcan sobre el repositorio de la ontología y 
 * Llevar a cabo la ejecución de los test
 * Publicar los resultados obtenidos

Cada vez que se produce un cambio sobre el repositorio de la ontología, ontolo-ci recoge ese cambio y ejecuta todas las pruebas que se encuentren definidas. Una vez terminado este proceso, ontolo-ci notifica los resultados directamente sobre el propio repositorio, así como en su propia página web. En ambos lugares podemos observar los resultados de cada una de las pruebas.

## Vista de construcción en bloques
La vista de construcción en bloques muestra la descomposición estática del sistema en bloques de construcción (módulos, componentes, subsistemas, clases, interfaces, paquetes, bibliotecas, marcos, capas, particiones, niveles, funciones, macros, operaciones, estructuras de datos, ...) así como sus dependencias (relaciones, asociaciones, ...).

El siguiente diagrama muestra la descomposición estática de la construccióndel sistema en bloques:
![](./images/ontolo-ci-bbview.png)

Ahora describiremos brevemenete los elementos principales que componen esta vista.

### Whitebox ontolo-ci

#### Contained Blackboxes
Los siguientes componenetes han sido encontrados:
* listener: Este componente es el punto de entrada del subsistema que utilizarán otros sistemas externos. Para ello, ofrece una interfaz externa, denominada OnWebhook, que se encarga de recibir los datos sobre las actualizaciones de ontologías del sistema de control de versiones. Además, este componente es el encargado de crear objetos de tipo Build. Los objetos Build están presentes durante todo el ciclo de vida de ontolo-ci. Representan una ejecución de los tests sobre un estado concreto del repositorio de control de versiones. El listener es el encargado no solamente de crear estos objetos si no también de rellenarlos con la información que le llega del sistema de control de versiones.
* scheduler: Este componente es el encargado de decidir en que momento se deben ejecutar las builds. De tal manera que si le llegan muchas builds sea capaz de gestionar el orden en que estas se han de ejecutar.
* worker: Este componente es el encargado de llevar a cabo la ejecución de los test una vez la build ha sido rellenada con lost test oportunos.
* hub: El hub es el componente que se comunica con el sistema de control de versiones. Es el encargado de obtener los ficheros que se corresponden con la nueva build, así como de postear los resultados de los test en el sistema de control de versiones.
* api: La api expone un endpoint para ser consumido por la web. Devuelve los datos de las builds que ya ha finalizado.
* web: Este componente representa una página web donde se pueden ver los resultados de todas las builds que se han llevado a cabo.

### Level 2
En este nivel detallaremos cada uno de los bloques del sistema identificados anteriormente en el nivel 1.

#### Listener
* RepositoryRestListener: Define una interfaz para escuchar de los distintos repositorios de control de versiones.
* GitHubRestListener: Implementación de la interfaz RepositoryRestListener para el repositorio de control de versiones GitHub.

![](./images/ontolo-ci-listener.png)
#### Scheduler
* Scheduler: Define un contrato con el módulo listener. Permite añadir builds a la lista de builds que se deben ejecutar.
* SchedulerImpl: Implementación concreta del scheduler

![](./images/ontolo-ci-scheduler.png)
#### Api
* OntolociAPI: Define una interfaz para proveer las distintas builds ejecutadas en el sistema.
* SpringBootOntolociAPI: Implementa la interfaz OntolociAPI. Ofrece una API rest para ser consumida desde la web.

![](./images/ontolo-ci-api.png)
#### Worker
* Worker: Define un contrato para el scheduler.
* WorkerExecutor: Implementa la interfaz Worker. Es el encargado de comunicarse con el Hub para obtener los casos de prueba y para actualizar la información del repositorio una vez se ha llevado a cabo la validación, la cúal se delega en el worker secuencial.
* WorkerSequential: Es el encargado de llevar a cabo la validación de los casos de prueba de una build.

![](./images/ontolo-ci-worker-1.png)

* Build: Entidad que representa los casos de prueba a ejecutar de un repositorio concreto. Cada build tiene un identificador único, una colección de casos de prueba y una serie de metadatos. En los metadatos se encuentra toda la información relativa al repositorio donde se ha producido un cambio y donde se encuentran los casos de prueba.
* TestCase: Entidad que representa un caso de prueba. Cada caso de prueba está formado por: nombre, ontología, instancias, schema, shape map de entrada y shape map esperado.
* BuildResult: Entidad que representa los casos de prueba ejecutados de un repositorio concreto. Se encuentra formado por un identificador único, una colección de resultados de casos de prueba y una serie de metadatos. Entre los metadatos, además de los que contenía la build previamente, disponemos del tiempo de ejecución total de la build y shapeMaps resultantes y esperados. Por último, dispone de un estado que representa representa el estado de ejecución en el que se encuentra.
* BuildResultStatus: Enumerado que define los distintos estados que puede atravesar una BuildResult.
* TestCaseResult: Entidad que representa el resultado de un caso de prueba tras su validación. Dispone de una serie de metadatos donde se almacenan los tiempos de ejecución. Posee un estado que representa el estado de ejecución en el que se encuentra.
* TestCaseResultStatus: Enumerado que define los distintos estados que puede atravesar una TestCaseResult.
* ShapeMapResultValidation: Representación simplificada de una validación entre un nodo y una shape a modo de ShapeMap. Contiene el nodo, la shape, el estado (conformant o nonconformant) e información de la validación.
* PrefixNode: Entidad para representar un nodo prefijado.
* Validate: Realiza la validación de datos en rdf mediante Shape Expressions.
* ResultValidation: Objeto resultante de la validación realizada en la clase Validate. Contiene un shape map resultante y uno esperado.

![](./images/ontolo-ci-worker-2.png)

#### Hub
* Hub: Define un contrato para el worker.
* HubImplementation: Implementa las operaciones definidas en la interfaz Hub. Recibe un objeto RepositoryProvider y delega en él las llamadas a los sistemas de control de versiones.
* RepositoryProvider: Define una interfaz para cada una de las implementaciones concretas de sistemas de control de versiones, como por ejemplo GitHub.
* GitHubRepositoryProvider: Implementa la interfaz RepositoryProvider. Se encarga de realizar la comunicación entre el Hub y GitHub.
* HubBuild: Representa una build (definida anteriormente) adaptada al dominio del Hub.
* HubTestCase: Representa una caso de prueba (definido anteriormente) adaptada al dominio del Hub.
* Manifest: Entidad que representa el objeto manifest.json en el que se definen los casos de prueba.
* ManifestEntry: Entidad que representa cada uno de los casos de prueba definidos dentro del manifest.json.
* RepositoryConfiguration: Representa el fichero oci.yml donde se establece la configuración del repositorio que desee usar ontolo-ci.

![](./images/ontolo-ci-hub.png)

## Vista en tiempo de ejecución
En esta sección mostraremos un ejemplo de la vista del sistema en tiempo de ejecución, donde escucheros los cambios de un repositorio de GitHub y se realizará la validación de los casos de prueba oportunos.
### Modificación de la ontología en el repositorio de GitHub
El siguiente diagrama ilustra la secuencia de eventos que ocurren cuando se produce un cambio sobre la ontología de un repositorio de GitHub que use ontolo-ci.

![](./images/ontolo-ci-runTimeView.png)

## Vista de despliegue
La siguiente imagen describe la vista de despliegue del sistema. Se han identificado los siguientes elementos:
* Public Ontology Repo: Representan los repositorios de github que continen las ontologías y los test y que se encuentran conectados a github mediante el sistema de WebHooks de GitHub.
* Ontolo-ci: Este sistema será desplegado mediante docker. Por una parte dispondrá de una API rest escuchando en el puerto 8090 y por otro lado dispondrá de un cliente web en el puerto 8080.
* 
![](./images/ontolo-ci-main-schema.png)

## Conceptos técnicos y transversales
En esta sección especificaremos algunos de los conceptos técnicos del proyecto.

### Tecnologías usadas
Las tecnologías utilizadas en el desarrollo del sistema se enuncian a continuación:

#### Java
Java es un lenguaje de programación y una plataforma informática comercializada por primera vez en 1995 por Sun Microsystems. Java es rápido, seguro, fiable y orientado a objetos. Además, dispone de una gran comunidad y librerías. 

Uno de los motivos principales por el que se decidió usar java es debido a la existencia de un validador de Shape Expressions escrito en Scala y totalmente compatible con Java.
#### Springboot
Spring es un framework para el desarrollo de aplicaciones web y contenedor de inversión de control, de código abierto para la plataforma Java
Spring Boot permite compilar nuestras aplicaciones Web como un archivo . jar que podemos ejecutar como una aplicación Java normal (como alternativa a un archivo . war , que desplegaríamos en un servidor de aplicaciones como Tomcat).

Springboot es una de los frameworks más utilizados actualmente para la construcción de API REST en java. Es por eso que hemos decidido utilizarlo en el proyecto.



## Design Decisions
singleton, adapter...

## Risks and Technical Debts

## Tests
### Design phase

### Implementation phase


## Requirements analysis
### Functional requirements

### Non-functional requirements



## Annexes

### A: Integración de sistema de integración continua con GitHub

### B: System manual

#### Defining a webhook in the source repo


### Launching the app with Docker

## Glossary
