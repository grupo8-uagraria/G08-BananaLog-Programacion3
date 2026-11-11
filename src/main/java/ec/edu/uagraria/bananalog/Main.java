package ec.edu.uagraria.bananalog;

import ec.edu.uagraria.bananalog.alertas.*;
import ec.edu.uagraria.bananalog.dominio.LoteBanano;
import ec.edu.uagraria.bananalog.fabrica.FabricaProductos;
import ec.edu.uagraria.bananalog.reglas.Reglas;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Punto de entrada — demuestra la salida del motor de alertas BananaLog.
 *
 * <p>Captura de esta salida forma parte del PDF de evidencia (Moodle).</p>
 */
public class Main {

    public static void main(String[] args) {

        System.out.println("=== BananaLog G08 — Motor de Alertas ===\n");

        // ---- Crear motor y registrar reglas --------------------------------
        MotorAlertas motor = new MotorAlertas();
        motor.agregarRegla(Reglas.descuadreCajas());
        motor.agregarRegla(Reglas.certificacionFaltante());
        motor.agregarRegla(Reglas.frutaPasada());

        // ---- Crear observadores vía fábrica ---------------------------------
        List<Alerta> bitacora = new ArrayList<>();
        motor.suscribir(FabricaObservadores.crear(TipoObservador.CONSOLA,     bitacora));
        motor.suscribir(FabricaObservadores.crear(TipoObservador.BITACORA,    bitacora));
        motor.suscribir(FabricaObservadores.crear(TipoObservador.SMS_SIMULADO, bitacora));

        // ---- Lote 1: sin problemas -----------------------------------------
        LoteBanano lote1 = FabricaProductos.crearLoteBanano(
                1000, 1000, true, LocalDate.now().minusDays(10), 10);
        System.out.println("--- Procesando: " + lote1.codigo() + " (lote OK) ---");
        int alertas1 = motor.procesar(lote1);
        System.out.println("Alertas publicadas: " + alertas1 + "\n");

        // ---- Lote 2: descuadre + sin certificación + fruta pasada ----------
        LoteBanano lote2 = FabricaProductos.crearLoteBanano(
                1000, 940, false, LocalDate.now().minusDays(18), 18);
        System.out.println("--- Procesando: " + lote2.codigo() + " (lote con problemas) ---");
        int alertas2 = motor.procesar(lote2);
        System.out.println("Alertas publicadas: " + alertas2 + "\n");

        // ---- Resumen bitácora ----------------------------------------------
        System.out.println("=== Bitácora (" + bitacora.size() + " registros) ===");
        bitacora.forEach(a -> System.out.println(
                "  [" + a.tipo() + "] " + a.codigoLote() + " — " + a.mensaje()));
    }
}
