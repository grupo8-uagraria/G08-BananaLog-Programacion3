package ec.edu.uagraria.bananalog.alertas;

import ec.edu.uagraria.bananalog.dominio.LoteBanano;

import java.util.Optional;

/**
 * Interfaz funcional de Strategy para reglas de alerta.
 *
 * <p>Principio O: se agregan nuevas reglas implementando esta interfaz,
 * sin modificar el MotorAlertas.<br>
 * Principio S: cada implementación tiene exactamente un motivo de cambio
 * (la lógica de una sola alerta).</p>
 */
@FunctionalInterface
public interface ReglaAlerta {
    /**
     * Evalúa un lote de banano y devuelve una alerta si se cumple la condición.
     *
     * @param lote lote a evaluar
     * @return {@link Optional} con la alerta, o vacío si no aplica
     */
    Optional<Alerta> evaluar(LoteBanano lote);
}
