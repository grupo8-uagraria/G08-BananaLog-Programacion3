package ec.edu.uagraria.bananalog.alertas;

import java.time.LocalDateTime;

/**
 * Valor inmutable que representa una alerta generada por el motor.
 * Principio S: solo transporta datos de la alerta.
 *
 * @param tipo      categoría de la alerta
 * @param codigoLote código del lote que la originó
 * @param mensaje   descripción legible del problema detectado
 * @param timestamp momento en que se generó la alerta
 */
public record Alerta(
        TipoAlerta tipo,
        String codigoLote,
        String mensaje,
        LocalDateTime timestamp
) {
    /** Constructor conveniente que registra la fecha/hora actual. */
    public Alerta(TipoAlerta tipo, String codigoLote, String mensaje) {
        this(tipo, codigoLote, mensaje, LocalDateTime.now());
    }
}
