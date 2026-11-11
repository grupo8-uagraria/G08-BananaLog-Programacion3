package ec.edu.uagraria.bananalog.dominio;

/**
 * Record inmutable que representa un contenedor refrigerado de exportación.
 * Principio S: solo modela los datos del contenedor.
 *
 * @param codigo              código de trazabilidad único
 * @param numeroContenedor    identificador físico del contenedor (p.ej. "MSCU1234567")
 * @param capacidadCajas      capacidad máxima en cajas
 * @param temperaturaCelsius  temperatura actual en °C
 */
public record Contenedor(
        String codigo,
        String numeroContenedor,
        int capacidadCajas,
        double temperaturaCelsius
) { }
