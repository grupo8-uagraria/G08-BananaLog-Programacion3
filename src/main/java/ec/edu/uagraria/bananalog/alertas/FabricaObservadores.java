package ec.edu.uagraria.bananalog.alertas;

import java.util.List;

/**
 * Fábrica de observadores del motor de alertas BananaLog.
 *
 * <p>Switch exhaustivo sin {@code default}: el compilador garantiza
 * que todos los {@link TipoObservador} están cubiertos.</p>
 */
public final class FabricaObservadores {

    private FabricaObservadores() { }

    /**
     * Crea un observador del tipo solicitado.
     *
     * @param tipo     tipo de observador
     * @param bitacora lista compartida usada por el observador BITACORA
     * @return observador listo para suscribirse al motor
     */
    public static ObservadorAlertas crear(TipoObservador tipo, List<Alerta> bitacora) {
        return switch (tipo) {
            case CONSOLA -> alerta -> System.out.printf(
                    "[ALERTA][%s] %s → %s%n",
                    alerta.tipo(), alerta.codigoLote(), alerta.mensaje());

            case BITACORA -> bitacora::add;

            case SMS_SIMULADO -> alerta -> System.out.printf(
                    "[SMS-SIM] Enviando alerta %s del lote %s al supervisor%n",
                    alerta.tipo(), alerta.codigoLote());
        };
    }
}
