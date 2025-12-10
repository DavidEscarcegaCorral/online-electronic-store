# online-electronic-store

Aplicación modular Java (Maven) para una tienda online especializada en armado de PCs con validación de compatibilidad y gestión de ventas.

## Requisitos
- Java JDK 23+
- Maven 3.6+
- MongoDB 4.x+ (corriendo en localhost:27017)

## Arquitectura del Proyecto
La aplicación utiliza una arquitectura en capas modular con los siguientes subsistemas:

### Módulos de Datos
- `dominio_datos` — Entidades de dominio mapeadas a MongoDB (por ejemplo `ProductoEntidad`, `PedidoEntidad`, `ConfiguracionEntidad`).
- `dao_datos` — Capa de acceso a datos: `ProductoDAO`, `PedidoDAO`, `CarritoDAO`, `ConexionMongoDB`.

### Módulos de Negocio
- `dto_negocios` — DTOs (ComponenteDTO, EnsamblajeDTO, CarritoDTO, ItemCarritoDTO, ClienteDTO, etc.).
- `objetos_negocio` — Lógica de negocio central.
- `negocio_configuracion` — Subsistema que gestiona el flujo de configuración paso a paso y expone una fachada (`ConfiguracionFacade`).
- `negocio_venta` — Subsistema de ventas y venta, con su fachada (`VentaFacade` o similar).
- `negocio_armarPC` — Validación de compatibilidad entre componentes y revalidación de ensamblaje (`ArmadoFacade`).

### Módulo de Presentación
- `presentacion` — Interfaz gráfica en Swing. Contiene panels por pasos, cards de productos, panel lateral de resumen y `MenuOpcionesPanel`.

## Flujo de configuración y reglas de negocio
- Flujo guiado por pasos: Categoría → Marca → Producto(s) → Evaluar Configuración.
- Persistencia de selección en catálogos: cada pantalla de catálogo recuerda la selección realizada y la restaura al volver.
- Las cards (categoría, marca, producto) funcionan en modo toggle: hacer clic selecciona; volver a hacer clic quita la selección.
- El borde de selección para cards de categoría y marca de CPU es morado (mismo estilo que cards de producto).

### Validaciones al avanzar
- Al seleccionar una categoría, el sistema valida que exista una cantidad mínima de productos en la base de datos que permitan conformar una configuración válida. Si no existen suficientes productos no se permite avanzar y se muestra un mensaje explicativo.
- Componentes obligatorios para considerar una configuración válida y poder guardarla:
  - Procesador
  - Tarjeta madre
  - Memoria RAM
  - Gabinete
  - Tarjeta de video
  - Fuente de poder
  - Disipador
- Los demás componentes son opcionales y no bloquean la navegación.
- La navegación (botones de avanzar/retroceder y botones del menú lateral) está habilitada sólo cuando el paso actual tiene una selección válida; al volver atrás, la habilitación se basa en el estado de la selección en ese paso (si existe selección permanece habilitado).

## Presentación y panel lateral
- En el paso "Evaluar configuración" el panel lateral derecho se reemplaza por `MenuOpcionesPanel` (en vez del panel `Resumen`).
- `MenuOpcionesPanel.totalCard` muestra exactamente el mismo precio total del ensamblaje que el `Resumen`.
- `MenuOpcionesPanel` contiene:
  - Un botón "Guardar configuración" (por encima del `totalCard`) que persiste la entidad `ConfiguracionEntidad` en la DB cuando se cumplen los requisitos mínimos.
  - Si el usuario intenta guardar sin cumplir los requisitos, aparecerá un `JDialog` explicando por qué no se puede guardar.
  - Un botón adicional que añade la configuración actual al venta del cliente (singleton). Al añadir la configuración al venta, el ensamblaje actual se limpia (se borran todas las selecciones en la UI y en el objeto ensamblaje).

## Carrito y persistencia
- El venta es una entidad manejada como singleton por cliente; actualmente el flujo añade configuraciones como objetos en el venta (sin proceso de pago implementado todavía).
- Guardar configuración persiste la entidad `ConfiguracionEntidad` en la colección correspondiente de MongoDB.
- Nombre y representación en la UI:
  - Asegúrate de que el campo nombre de la configuración se asigne antes de persistir si deseas que se muestre correctamente en el venta y en la UI.
  - El `precio unitario` en la vista de venta debe reflejar el precio del ensamblaje; si aparece un texto genérico como "Configuración PC" revisa dónde se setea la descripción y el cálculo del precio antes de persistir.

## Errores comunes y soluciones rápidas
- `java: cannot find symbol: class ConfiguracionEntidad`:
  - Verifica la existencia de `ConfiguracionEntidad` en `dominio_datos/src/main/java/entidades/` y que su declaración de paquete coincide con `entidades`. Recompila el módulo.

- `java: cannot find symbol: method entrySet() location: interface java.util.List<dto.ComponenteDTO>`:
  - `entrySet()` es de `Map`. Para listas usa iteración con `for`, `forEach` o `ListIterator`.

- `java: cannot find symbol: method getPanelContenido() location: compartido.FramePrincipal`:
  - Se añadió el getter `getPanelContenido()` en `compartido.FramePrincipal`. Usa la clase actualizada y recompila `presentacion`.

- `armadoFacade already declared`:
  - Evita redeclaraciones; inyecta o comparte la instancia desde el control de presentación.

- Problemas de paquetes en entidades o DTOs:
  - Corrige la declaración de paquete o los imports y recompila el módulo correspondiente.

## Buenas prácticas y recomendaciones
- Control de presentación centralizado: usar un `ControlPresentacion` (naming) que actúe como puente entre la UI y los subsistemas (negocio_configuracion, negocio_venta, negocio_armarPC) para mantener responsabilidades claras.
- El control de navegación puede implementar una interfaz (`ControlNavegacion`) para facilitar pruebas y desacoplar la lógica de UI.
- Mantener la gestión de componentes Swing (add/remove/revalidate/repaint) centralizada en `FramePrincipal`.
- Evitar campos públicos; usar getters/setters o métodos de acceso controlados.

## Instrucciones de compilación y ejecución
1. Desde la raíz del proyecto:

   mvn clean install

## Registro de cambios
- Integración de persistencia de selección en catálogos y toggle de selección en cards.
- Validación de disponibilidad mínima al elegir categoría.
- Control de habilitado/deshabilitado de navegación basado en selección por paso.
- Unificación del color de borde de selección (morado) para categorías y marca de CPU.
- `MenuOpcionesPanel`: botones para guardar configuración y añadir al venta; `totalCard` sincronizado con resumen.
- Añadido `getPanelContenido()` en `compartido.FramePrincipal` para acceder de forma controlada al panel de contenido.

---
