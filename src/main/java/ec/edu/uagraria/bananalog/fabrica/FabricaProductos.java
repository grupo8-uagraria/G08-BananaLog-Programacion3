package ec.edu.uagraria.bananalog.fabrica;

import ec.edu.uagraria.bananalog.dominio.Contenedor;
import ec.edu.uagraria.bananalog.dominio.InsumoEmpaque;
import ec.edu.uagraria.bananalog.dominio.LoteBanano;
import ec.edu.uagraria.bananalog.dominio.Trazable;

import java.time.LocalDate;

/**
 * Fábrica de productos del dominio BananaLog.
 *
 * <p>Principio S: responsabilidad única de instanciar productos del dominio.<br>
 * Principio O: agregar un tipo solo toca TipoProducto y este switch.<br>
 * Principio D: el resto del sistema depende de esta fábrica, no de los constructores.</p>
 *
 * <p>Switch exhaustivo sin {@code default}: si se agrega un TipoProducto y no se
 * añade aquí, el compilador (JDK 21 con switch expression) produce un error.</p>
 */
public final class FabricaProductos {

    private FabricaProductos() { }

    // -------------------------------------------------------------------------
    // Métodos de creación por tipo
    // -------------------------------------------------------------------------

    /**
     * Crea un {@link LoteBanano} con los datos proporcionados.
     *
     * @param cajasTotales  cajas declaradas
     * @param cajasContadas cajas contadas físicamente
     * @param certificado   si tiene certificación vigente
     * @param fechaCosecha  fecha de cosecha
     * @param diasTransito  días de tránsito desde cosecha
     * @return lote con código autogenerado
     */
    public static LoteBanano crearLoteBanano(int cajasTotales,
                                             int cajasContadas,
                                             boolean certificado,
                                             LocalDate fechaCosecha,
                                             int diasTransito) {
        return new LoteBanano(
                Trazable.generarCodigo("LB"),
                cajasTotales, cajasContadas,
                certificado, fechaCosecha, diasTransito
        );
    }

    /**
     * Crea un {@link Contenedor} con los datos proporcionados.
     *
     * @param numeroContenedor   identificador físico
     * @param capacidadCajas     capacidad máxima
     * @param temperaturaCelsius temperatura actual
     * @return contenedor con código autogenerado
     */
    public static Contenedor crearContenedor(String numeroContenedor,
                                             int capacidadCajas,
                                             double temperaturaCelsius) {
        return new Contenedor(
                Trazable.generarCodigo("CON"),
                numeroContenedor, capacidadCajas, temperaturaCelsius
        );
    }

    /**
     * Crea un {@link InsumoEmpaque} con los datos proporcionados.
     *
     * @param nombre       nombre del insumo
     * @param stockActual  unidades disponibles
     * @param stockMinimo  umbral de reposición
     * @return insumo con código autogenerado
     */
    public static InsumoEmpaque crearInsumoEmpaque(String nombre,
                                                    int stockActual,
                                                    int stockMinimo) {
        return new InsumoEmpaque(
                Trazable.generarCodigo("INS"),
                nombre, stockActual, stockMinimo
        );
    }

    // -------------------------------------------------------------------------
    // Método genérico con enum — switch exhaustivo SIN default
    // -------------------------------------------------------------------------

    /**
     * Crea un producto usando el enum {@link TipoProducto}.
     * Switch exhaustivo: el compilador fuerza cubrir todos los casos.
     *
     * @param tipo    tipo de producto a crear
     * @param params  parámetros variables según el tipo (ver cada case)
     * @return objeto del dominio correspondiente
     */
    public static Object crear(TipoProducto tipo, Object... params) {
        return switch (tipo) {
            case LOTE_BANANO -> crearLoteBanano(
                    (int)    params[0],
                    (int)    params[1],
                    (boolean)params[2],
                    (LocalDate) params[3],
                    (int)    params[4]
            );
            case CONTENEDOR -> crearContenedor(
                    (String) params[0],
                    (int)    params[1],
                    (double) params[2]
            );
            case INSUMO_EMPAQUE -> crearInsumoEmpaque(
                    (String) params[0],
                    (int)    params[1],
                    (int)    params[2]
            );
        };
    }
}
