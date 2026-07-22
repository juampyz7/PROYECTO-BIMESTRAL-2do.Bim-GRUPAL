# StreamFlow — Sistema de Gestión de Contenido y Suscripciones

Sistema académico desarrollado en Java para gestionar el catálogo de contenido de una plataforma de streaming (películas, series, documentales y tipos de contenido definidos por el propio usuario) y las suscripciones de sus usuarios, aplicando arquitectura MVC, principios SOLID y persistencia con SQLite.

## Integrantes

- Juan Pablo Loja
- Javier Solano

## Características principales

- Registro y gestión de contenido multimedia con herencia y polimorfismo (`Pelicula`, `Serie`, `Documental`).
- **Contenido extensible en tiempo de ejecución**: el usuario puede definir tipos de contenido completamente nuevos (por ejemplo, un Podcast o un AudioLibro) desde el propio menú, sin modificar el código fuente del sistema.
- Registro de usuarios identificados por cédula (clave única).
- Registro, consulta y eliminación de suscripciones, con cálculo automático de costo mensual según la calidad contratada.
- Recomendación de contenido por género.
- Persistencia real en base de datos SQLite mediante JDBC.
- Suite de pruebas automatizadas con JUnit 5.

## Tecnologías utilizadas

| Tecnología | Uso |
|---|---|
| Java 17 | Lenguaje principal |
| Maven | Gestión de dependencias y build |
| JUnit 5 | Pruebas automatizadas |
| SQLite JDBC (org.xerial) | Persistencia de datos |
| NetBeans | IDE de desarrollo |

## Arquitectura

El proyecto sigue el patrón **MVC**, organizado en los siguientes paquetes:

- com.streamflow.modelo -> Entidades del dominio (Contenido, Usuario, Suscripcion, etc.)
- com.streamflow.dao -> Acceso a datos (interfaces + implementaciones en memoria y SQLite)
- com.streamflow.servicio -> Lógica de negocio (cálculo de suscripciones, recomendaciones)
- com.streamflow.controlador -> Orquestación entre la vista y las capas inferiores
- com.streamflow.vista -> Menú de consola
- com.streamflow.config -> Configuración de conexión a la base de datos

## Principios SOLID aplicados

- **SRP**: cada capa (persistencia, lógica de negocio, presentación) vive en su propio paquete.
- **OCP**: nuevos tipos de contenido se agregan mediante `ContenidoTipoMapeador` y `ContenidoGenerico`, sin modificar `ContenidoDAOSQLite` ni ningún servicio existente.
- **LSP**: cualquier subclase de `Contenido` es procesada de forma polimórfica por `RecomendacionServicio` sin necesidad de conocer su tipo concreto.
- **ISP**: interfaces DAO separadas por entidad (`ContenidoDAO`, `UsuarioDAO`, `SuscripcionDAO`) en vez de una interfaz genérica compartida.
- **DIP**: controladores y servicios dependen de interfaces, nunca de implementaciones concretas de persistencia.

## Cómo ejecutar el proyecto

### Requisitos previos

- JDK 17 o superior
- Maven 3.8+

### Pasos

```bash
git clone https://github.com/juampyz7/PROYECTO-BIMESTRAL-2do.Bim-GRUPAL.git
cd PROYECTO-BIMESTRAL-2do.Bim-GRUPAL
mvn clean install
mvn exec:java
```

La base de datos `streamflow.db` se genera automáticamente en la raíz del proyecto la primera vez que se ejecuta.

### Correr las pruebas

```bash
mvn test
```

## Estrategia de ramas

- **main**: versión estable e integrada del sistema.
- **paquetes**: reestructuración y organización de la base del proyecto.
- **test**: desarrollo de las pruebas unitarias con JUnit 5.

## Documentación adicional

- Informe técnico: análisis de SOLID, patrones de diseño y evidencias de ejecución.
- Diagramas UML (clases y secuencia) disponibles en la carpeta correspondiente del repositorio.