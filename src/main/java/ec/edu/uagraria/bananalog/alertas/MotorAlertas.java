package ec.edu.uagraria.bananalog.alertas;

import ec.edu.uagraria.bananalog.dominio.LoteBanano;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Sujeto del patrón Observer: evalúa reglas sobre lotes y notifica observadores.
 *
 * <p>Principio S: solo coordina reglas y observadores, sin lógica de dominio.<br>
 * Principio O: se extiende agregando reglas/observadores, sin modificar esta clase.<br>
 * Principio D: depende de {@link ReglaAlerta} y {@link ObservadorAlertas} (abstracciones).<br>
 * Composición sobre herencia: compone listas de reglas y observadores.</p>
 */
public class MotorAlertas {

    private final List<ReglaAlerta> reglas = new ArrayList<>();
    private final List<ObservadorAlertas> observadores = new ArrayList<>();

    /** Registra una nueva regla de evaluación. */
    public void agregarRegla(ReglaAlerta regla) {
        reglas.add(regla);
    }

    /** Suscribe un observador para recibir alertas publicadas. */
    public void suscribir(ObservadorAlertas observador) {
        observadores.add(observador);
    }

    /**
     * Evalúa todas las reglas sobre el lote y notifica a los observadores
     * por cada alerta generada.
     *
     * @param lote lote de banano a evaluar
     * @return cantidad de alertas publicadas
     */
    public int procesar(LoteBanano lote) {
        int publicadas = 0;
        for (ReglaAlerta regla : reglas) {
            Optional<Alerta> alerta = regla.evaluar(lote);
            if (alerta.isPresent()) {
                observadores.forEach(o -> o.notificar(alerta.get()));
                publicadas++;
            }
        }
        return publicadas;
    }
}
