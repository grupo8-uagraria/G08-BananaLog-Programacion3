package ec.edu.uagraria.bananalog.reglas;

import ec.edu.uagraria.bananalog.alertas.Alerta;
import ec.edu.uagraria.bananalog.alertas.ConfiguracionBananaLog;
import ec.edu.uagraria.bananalog.alertas.ReglaAlerta;
import ec.edu.uagraria.bananalog.alertas.TipoAlerta;

import java.util.Optional;

/**
 * Fábrica de reglas de alerta para el sistema BananaLog (G08).
 *
 * <p>Principio S: cada método produce exactamente una regla con un único motivo de cambio.<br>
 * Principio O: agregar una alerta nueva no modifica las reglas existentes.<br>
 * Principio D: depende de {@link ReglaAlerta} (abstracción), no de clases concretas.</p>
 *
 * <p>Los umbrales se leen de {@link ConfiguracionBananaLog#INSTANCIA},
 * por lo que nunca aparecen como literales dentro de la lógica.</p>
 */
public final class Reglas {

    private Reglas() { }

    // -------------------------------------------------------------------------
    // Alerta 1: DESCUADRE_CAJAS
    // -------------------------------------------------------------------------

    /**
     * Dispara {@link TipoAlerta#DESCUADRE_CAJAS} cuando la diferencia porcentual
     * entre cajas declaradas y contadas <em>supera</em> el umbral configurado.
     *
     * <p>Fórmula: {@code |declaradas - contadas| / declaradas * 100 > umbral%}.</p>
     */
    public static ReglaAlerta descuadreCajas() {
        return lote -> {
            if (lote.cajasTotales() == 0) return Optional.empty();
            double diferencia = Math.abs(lote.cajasTotales() - lote.cajasContadas());
            double porcentaje = diferencia / lote.cajasTotales() * 100.0;
            double umbral = ConfiguracionBananaLog.INSTANCIA.getUmbralDescuadrePorcentaje();
            if (porcentaje > umbral) {
                return Optional.of(new Alerta(
                        TipoAlerta.DESCUADRE_CAJAS,
                        lote.codigo(),
                        String.format("Descuadre de %.1f %% supera el umbral de %.1f %% "
                                + "(declaradas: %d, contadas: %d)",
                                porcentaje, umbral, lote.cajasTotales(), lote.cajasContadas())
                ));
            }
            return Optional.empty();
        };
    }

    // -------------------------------------------------------------------------
    // Alerta 2: CERTIFICACION_FALTANTE
    // -------------------------------------------------------------------------

    /**
     * Dispara {@link TipoAlerta#CERTIFICACION_FALTANTE} cuando el lote
     * no tiene certificación de exportación.
     *
     * <p>No hay umbral numérico: es un estado booleano del lote.</p>
     */
    public static ReglaAlerta certificacionFaltante() {
        return lote -> {
            if (!lote.certificado()) {
                return Optional.of(new Alerta(
                        TipoAlerta.CERTIFICACION_FALTANTE,
                        lote.codigo(),
                        "El lote no cuenta con certificación de exportación vigente"
                ));
            }
            return Optional.empty();
        };
    }

    // -------------------------------------------------------------------------
    // Alerta 3: FRUTA_PASADA
    // -------------------------------------------------------------------------

    /**
     * Dispara {@link TipoAlerta#FRUTA_PASADA} cuando los días de tránsito del lote
     * <em>superan</em> el umbral configurado.
     *
     * <p>El umbral por defecto es 14 días (ver justificación en
     * {@link ConfiguracionBananaLog}).</p>
     */
    public static ReglaAlerta frutaPasada() {
        return lote -> {
            int umbral = ConfiguracionBananaLog.INSTANCIA.getUmbralDiasTransitoMaximo();
            if (lote.diasTransito() > umbral) {
                return Optional.of(new Alerta(
                        TipoAlerta.FRUTA_PASADA,
                        lote.codigo(),
                        String.format("Lote lleva %d días en tránsito, supera el límite de %d días",
                                lote.diasTransito(), umbral)
                ));
            }
            return Optional.empty();
        };
    }
}
