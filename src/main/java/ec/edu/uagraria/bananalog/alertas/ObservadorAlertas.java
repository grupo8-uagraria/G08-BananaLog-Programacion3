package ec.edu.uagraria.bananalog.alertas;

/**
 * Interfaz funcional del patrón Observer.
 *
 * <p>Principio D: el motor depende de esta abstracción, no de implementaciones concretas.<br>
 * Al ser funcional, se puede usar con lambdas: {@code motor.suscribir(bitacora::add)}.</p>
 */
@FunctionalInterface
public interface ObservadorAlertas {
    /**
     * Recibe una alerta publicada por el motor.
     *
     * @param alerta alerta generada
     */
    void notificar(Alerta alerta);
}
