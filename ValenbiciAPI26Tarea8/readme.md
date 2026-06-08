# ValenbiciAPI26Tarea8

## Descripción
Proyecto Java Maven que conecta con una base de datos MariaDB alojada en la nube de Amazon AWS (RDS).

## Tecnologías utilizadas
- Java 17
- Maven
- MySQL Connector J 9.2.0
- Amazon AWS RDS (MariaDB)

## Configuración de la base de datos
- **Host:** databasedmp.csrbipfghawt.us-east-1.rds.amazonaws.com
- **Puerto:** 3306
- **Base de datos:** starwars
- **Usuario:** admin

## Funcionamiento
El programa se conecta a la base de datos de StarWars alojada en AWS y muestra el listado de películas. El usuario puede elegir una película y se mostrarán los personajes que aparecen en ella.