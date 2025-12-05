# online-electronic-store — instrucciones rápidas

Este repositorio contiene una aplicación modular Java (Maven) mínima para un caso base de tienda online y armado de PCs. He añadido módulos mínimos para poder ejecutar un flujo de venta usando DAOs en memoria y un `Runner` de prueba.

Requisitos locales
- Java JDK (11+ recomendado, el POM usa compilación Java 23 pero puedes ajustar en tu entorno).
- Maven instalado y disponible en PATH (`mvn -v`).

Resumen de módulos añadidos/actualizados
- `dto_negocios` — DTOs en el paquete `dto.model` (ComponenteDTO, EnsamblajeDTO, CarritoDTO, ItemCarritoDTO, CompraDTO, MetodoPagoDTO, ClienteDTO).
- `dao_datos` — DAOs in-memory (ICatalogoDAO, IPedidoDAO, InMemoryCatalogoDAO, InMemoryPedidoDAO).
- `bo_negocio` — BOs (CarritoBO, PedidoBO, PagoBO).
- `negocio_venta` — Fachada de venta (IVentaFacade, VentaFacade) y un `Runner` de prueba en `debug.Runner`.
- `negocio_configuracion` — Fábrica simple (ConfiguracionFactory) para resolver fachadas (opcional).
- `presentacion/pom.xml` actualizado para depender de `negocio_configuracion` y `dto_negocios`.

Comandos útiles (PowerShell)
- Compilar todo:
```powershell
mvn -DskipTests clean install
```
- Ejecutar el runner de prueba (desde la raíz):
```powershell
mvn -pl negocio_venta exec:java
```
(El plugin `exec-maven-plugin` se añadió a `negocio_venta/pom.xml` con `debug.Runner` como `mainClass`.)

Qué hace el Runner
- Crea un carrito, agrega un item, crea un pedido (checkout), simula el pago con un mock y persiste el pedido en memoria. Imprime `Pago OK: true/false, pedidoId: <id>`.

Problemas comunes y solución rápida
- Si ves errores de "Duplicate class" relacionados con clases generadas en `target/generated-sources`:
  - Ejecuta `mvn clean` para eliminar `target` y recompila.
  - Si el proceso de generación vuelve a crear clases con paquete `dto`, considera ajustar/identificar el plugin que genera ese código o renombrar paquetes generados.

Siguientes pasos recomendados
- Añadir tests JUnit para los flujos "venta" y "armado".
- Implementar persistencia real (MongoDB) si deseas producción; por ahora está el mock in-memory.
- Revisar y ajustar la versión de Java en los POMs si tu JDK difiere (el `pom.xml` raíz usa `maven.compiler.source/target` = 23).

Si quieres, puedo:
- Añadir tests unitarios básicos.
- Implementar la integración con MongoDB (añadir dependencias y un DAO concreto).
- Crear un módulo `runner` separado si prefieres no poner `debug.Runner` dentro de `negocio_venta`.

Si encuentras errores al ejecutar `mvn`, pega la salida aquí y lo depuro contigo.

