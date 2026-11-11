package ec.edu.uagraria.bananalog.dominio;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Utilidad para generar códigos de trazabilidad únicos.
 * Principio S: responsabilidad única de generación de códigos.
 */
public final class Trazable {

    private static final AtomicInteger contador = new AtomicInteger(1000);
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("yyyyMMdd");

    private Trazable() { }

    /**
     * Genera un código de trazabilidad con prefijo, fecha y número secuencial.
     *
     * @param prefijo p.ej. "LB", "CON", "INS"
     * @return código único, p.ej. "LB-20261110-1001"
     */
    public static String generarCodigo(String prefijo) {
        String fecha = LocalDateTime.now().format(FORMATO);
        return prefijo + "-" + fecha + "-" + contador.getAndIncrement();
    }
}
