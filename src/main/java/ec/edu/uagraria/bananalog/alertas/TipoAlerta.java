package ec.edu.uagraria.bananalog.alertas;

/**
 * Tipos de alerta del motor BananaLog (G08).
 *
 * <ul>
 *   <li>DESCUADRE_CAJAS: diferencia entre cajas declaradas y contadas supera el umbral.</li>
 *   <li>CERTIFICACION_FALTANTE: el lote no cuenta con certificación de exportación.</li>
 *   <li>FRUTA_PASADA: los días de tránsito superan el límite permitido para exportación.</li>
 * </ul>
 */
public enum TipoAlerta {
    DESCUADRE_CAJAS,
    CERTIFICACION_FALTANTE,
    FRUTA_PASADA
}
