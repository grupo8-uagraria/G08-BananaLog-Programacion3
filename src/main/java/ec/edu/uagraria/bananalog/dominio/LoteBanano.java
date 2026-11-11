package ec.edu.uagraria.bananalog.dominio;

import java.time.LocalDate;

/**
 * Record inmutable que representa un lote de banano en exportación.
 * Principio S: solo modela los datos del lote, sin lógica de negocio.
 *
 * @param codigo           código de trazabilidad único
 * @param cajasTotales     total de cajas declaradas en el lote
 * @param cajasContadas    cajas contadas físicamente en bodega
 * @param certificado      true si el lote posee certificación (Global G.A.P., etc.)
 * @param fechaCosecha     fecha en que se cosechó el banano
 * @param diasTransito     días transcurridos desde la cosecha hasta ahora
 */
public record LoteBanano(
        String codigo,
        int cajasTotales,
        int cajasContadas,
        boolean certificado,
        LocalDate fechaCosecha,
        int diasTransito
) { }
