package ec.edu.uagraria.bananalog.dominio;

/**
 * Record inmutable que representa un insumo de empaque (cajas, fundas, flejes, etc.).
 * Principio S: solo modela los datos del insumo.
 *
 * @param codigo        código de trazabilidad único
 * @param nombre        nombre descriptivo del insumo
 * @param stockActual   unidades disponibles en bodega
 * @param stockMinimo   umbral mínimo de reposición
 */
public record InsumoEmpaque(
        String codigo,
        String nombre,
        int stockActual,
        int stockMinimo
) { }
