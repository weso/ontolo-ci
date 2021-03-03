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

### Requirements Overview
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

Cada vez que se produce un cambio sobre el repositorio de la ontología, ontolo-ci recoge ese cambio y ejecuta todas las pruebas que se encuentren definidas. Una vez terminado este proceso, ontolo-ci notifica los resultados directamente sobre el propio repositorio, así como en su propia página web. En ambos lugares podemos observar los resultados de cada una de las pruebas.

## Solution Strategy


## Building Block View


### Whitebox ontolo-ci

#### Contained Blackboxes

### Level 2


## Runtime View

## Deployment View

## Technical and Cross-cutting Concepts

### Technologies used


### Style guides

### Other dependencies


## Design Decisions

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
