# BananaLog — G08 | Programación 3 | UAE

**Asignatura:** Programación 3 (Orientada a Objetos Avanzada) — Código 120323UP34  
**Grupo:** G08 — BananaLog  
**Paralelo:** C  
**Período:** 2026 – 2027 CII  
**Docente:** Ing. Hamilton Francisco Villamar Barros, MSc.

**Integrantes:**
| Nombre | Rol en Taller 4 |
|---|---|
| Verónica Anchundia | Responsable de dominio y fábrica |
| Brithany Rivera | Responsable de alertas (reglas + motor) |
| Michael Caicedo | Responsable de alertas (observadores) |
| Narciso Galarza | Responsable de pruebas y documentación |
| Alfonso Olvera | Responsable de pruebas y documentación |

---

## Taller 4 — Paso 2: Diagnóstico del código (antes de la refactorización)

| Pregunta | Respuesta del grupo |
|---|---|
| ¿Qué clase tiene más de un motivo para cambiar? ¿Cuáles son? | La clase original `LoteBanano` mezclaba: (1) modelo de datos del dominio, (2) generación del código de trazabilidad y (3) validación de estado. Tres motivos de cambio distintos. |
| ¿Dónde se usa `new` directamente sobre los records del dominio? | En `Main` y en los métodos de prueba: `new LoteBanano(...)`, `new Contenedor(...)`, `new InsumoEmpaque(...)` aparecían dispersos sin pasar por ninguna fábrica. |
| ¿Hay umbrales o tarifas escritos como números sueltos dentro de una regla? | Sí: el porcentaje de descuadre aceptado (3 %) y los días máximos de tránsito (14 días) estaban como literales `3.0` y `14` directamente en la condición `if`. |
| ¿Hay alguna herencia que exista solo para reutilizar código? | No había jerarquía de herencia en el proyecto original, pero la gestión de alertas estaba mezclada en el mismo bloque de procesamiento de lotes, violando SRP. |

---

## Tabla SOLID — Aplicación en el Proyecto BananaLog

| Principio | Letra | Clase concreta que lo aplica | Cómo lo cumple |
|---|---|---|---|
| Single Responsibility | **S** | `Reglas.descuadreCajas()` | Solo evalúa una condición de negocio (descuadre de cajas); la notificación y el almacenamiento son responsabilidad de los observadores. |
| Open/Closed | **O** | `MotorAlertas` | Se puede agregar una cuarta alerta implementando `ReglaAlerta` y llamando a `agregarRegla(...)`, sin tocar ni una línea de `MotorAlertas`. |
| Liskov Substitution | **L** | `FabricaObservadores` | Los tres observadores (`CONSOLA`, `BITACORA`, `SMS_SIMULADO`) son intercambiables: el motor los trata a todos como `ObservadorAlertas` sin saber cuál es. |
| Interface Segregation | **I** | `ReglaAlerta` y `ObservadorAlertas` | Dos interfaces mínimas de un solo método; ningún cliente implementa métodos que no usa. |
| Dependency Inversion | **D** | `MotorAlertas` | Depende de `ReglaAlerta` y `ObservadorAlertas` (abstracciones), jamás de `Reglas.descuadreCajas()` ni de `ObservadorConsola` directamente. |

---

## Justificación de Umbrales

### Descuadre de Cajas → 3 %
El estándar de exportadoras bananeras ecuatorianas (Dole, Chiquita, Favorita) tolera hasta un 3 % de diferencia entre el manifiesto y el conteo físico en bodega. Superar ese umbral indica posibles pérdidas por manipulación indebida, robo o error de registro. Se decidió usar 3 % como valor por defecto configurable en `ConfiguracionBananaLog.INSTANCIA`, permitiendo ajuste dinámico sin recompilar.

### Días de Tránsito → 14 días
El banano verde ecuatoriano demora entre 12 y 18 días en barco hacia Europa o EE. UU. Pasados 14 días el proceso de maduración se acelera. Se establece el umbral en 14 días para alertar con margen, antes de que la fruta llegue demasiado madura al destino. Configurable en `ConfiguracionBananaLog.INSTANCIA.setUmbralDiasTransitoMaximo(...)`.

### Certificación Faltante → booleano
No tiene umbral numérico: es un requisito binario de exportación. Sin certificación (Global G.A.P., Fairtrade, etc.) el lote no puede salir del país. La alerta se genera siempre que `certificado == false`.

---

## Estructura de Paquetes

```
ec.edu.uagraria.bananalog
├── dominio        → LoteBanano, Contenedor, InsumoEmpaque, Trazable
├── fabrica        → TipoProducto, FabricaProductos
├── reglas         → Reglas (descuadreCajas, certificacionFaltante, frutaPasada)
└── alertas        → TipoAlerta, Alerta, ReglaAlerta, ObservadorAlertas,
                     MotorAlertas, TipoObservador, FabricaObservadores,
                     ConfiguracionBananaLog
```

---

## Instrucciones de Ejecución

```bash
# Compilar y ejecutar todas las pruebas
mvn clean verify

# Ejecutar la demo principal
mvn compile exec:java -Dexec.mainClass="ec.edu.uagraria.bananalog.Main"
```

---

## Uso de IA — Declaración de Honestidad Académica

Se utilizó asistencia de IA (Claude) en el **Paso 4 y 5** para generar el esqueleto inicial de las clases `FabricaProductos`, `MotorAlertas` y los métodos de `Reglas`. El código fue revisado por todos los integrantes, se verificó la compilación con `mvn clean verify`, y cada miembro del grupo está en capacidad de explicar el motor de alertas y la fábrica si el docente lo solicita. Las pruebas unitarias fueron construidas manualmente por los responsables de pruebas.

---

## Diagrama UML

Ver: `docs/uml-unidad1.png`

El diagrama incluye:
- **Implementación**: `Reglas` → `ReglaAlerta`; `FabricaObservadores` → `ObservadorAlertas`
- **Composición**: `MotorAlertas` ◆→ `ReglaAlerta` y `ObservadorAlertas`
- **Agregación**: `MotorAlertas` ◇→ `Alerta`
- **Dependencia**: `FabricaProductos` ···→ `Trazable`
