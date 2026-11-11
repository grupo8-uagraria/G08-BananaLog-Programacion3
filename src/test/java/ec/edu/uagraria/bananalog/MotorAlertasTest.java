package ec.edu.uagraria.bananalog;

import ec.edu.uagraria.bananalog.alertas.*;
import ec.edu.uagraria.bananalog.dominio.Contenedor;
import ec.edu.uagraria.bananalog.dominio.InsumoEmpaque;
import ec.edu.uagraria.bananalog.dominio.LoteBanano;
import ec.edu.uagraria.bananalog.fabrica.FabricaProductos;
import ec.edu.uagraria.bananalog.reglas.Reglas;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas JUnit 5 — Taller 4 BananaLog G08.
 *
 * <p>Cubre fábrica (pruebas 10), alertas con disparo y caso límite (pruebas 11-12)
 * y comportamiento del motor (pruebas adicionales).</p>
 */
@DisplayName("BananaLog G08 — Fábrica y Motor de Alertas")
class MotorAlertasTest {

    // Resets de configuración antes de cada prueba para independencia
    @BeforeEach
    void resetearConfiguracion() {
        ConfiguracionBananaLog.INSTANCIA.setUmbralDescuadrePorcentaje(3.0);
        ConfiguracionBananaLog.INSTANCIA.setUmbralDiasTransitoMaximo(14);
    }

    // =========================================================================
    // PRUEBA 10 — FÁBRICA
    // =========================================================================

    @Test
    @DisplayName("Prueba 10a: FabricaProductos crea LoteBanano con código autogenerado")
    void fabricaCreaLoteBananoConCodigo() {
        LoteBanano lote = FabricaProductos.crearLoteBanano(
                500, 500, true, LocalDate.now(), 5);
        assertNotNull(lote.codigo(), "El código no debe ser nulo");
        assertTrue(lote.codigo().startsWith("LB-"), "El código debe empezar con LB-");
        assertEquals(500, lote.cajasTotales());
        assertTrue(lote.certificado());
    }

    @Test
    @DisplayName("Prueba 10b: FabricaProductos crea Contenedor con código autogenerado")
    void fabricaCreaContenedorConCodigo() {
        Contenedor c = FabricaProductos.crearContenedor("MSCU1234567", 1200, 13.5);
        assertNotNull(c.codigo());
        assertTrue(c.codigo().startsWith("CON-"));
        assertEquals("MSCU1234567", c.numeroContenedor());
        assertEquals(1200, c.capacidadCajas());
    }

    @Test
    @DisplayName("Prueba 10c: FabricaProductos crea InsumoEmpaque con código autogenerado")
    void fabricaCreaInsumoEmpaqueConCodigo() {
        InsumoEmpaque insumo = FabricaProductos.crearInsumoEmpaque("Cajas cartón 22 kg", 800, 200);
        assertNotNull(insumo.codigo());
        assertTrue(insumo.codigo().startsWith("INS-"));
        assertEquals("Cajas cartón 22 kg", insumo.nombre());
        assertEquals(800, insumo.stockActual());
    }

    @Test
    @DisplayName("Prueba 10d: Dos lotes creados tienen códigos diferentes")
    void dosLotesCodigosDiferentes() {
        LoteBanano a = FabricaProductos.crearLoteBanano(100, 100, true, LocalDate.now(), 5);
        LoteBanano b = FabricaProductos.crearLoteBanano(200, 200, true, LocalDate.now(), 7);
        assertNotEquals(a.codigo(), b.codigo());
    }

    // =========================================================================
    // PRUEBA 11 — ALERTA: DESCUADRE_CAJAS
    // =========================================================================

    @Test
    @DisplayName("Prueba 11a: descuadreCajas() se dispara cuando diferencia supera el umbral")
    void descuadreCajasDisparaConDiferenciaAltaM() {
        // 1000 declaradas, 940 contadas → 6% > 3% umbral
        LoteBanano lote = FabricaProductos.crearLoteBanano(
                1000, 940, true, LocalDate.now(), 5);
        var resultado = Reglas.descuadreCajas().evaluar(lote);
        assertTrue(resultado.isPresent(), "Debe disparar alerta de descuadre");
        assertEquals(TipoAlerta.DESCUADRE_CAJAS, resultado.get().tipo());
    }

    @Test
    @DisplayName("Prueba 11b: descuadreCajas() NO se dispara en el umbral exacto")
    void descuadreCajasNoDisparaEnUmbralExacto() {
        // Umbral = 3%; 1000 declaradas, 970 contadas → exactamente 3%
        ConfiguracionBananaLog.INSTANCIA.setUmbralDescuadrePorcentaje(3.0);
        LoteBanano lote = FabricaProductos.crearLoteBanano(
                1000, 970, true, LocalDate.now(), 5);
        var resultado = Reglas.descuadreCajas().evaluar(lote);
        assertTrue(resultado.isEmpty(), "En el umbral exacto NO debe disparar");
    }

    @Test
    @DisplayName("Prueba 11c: descuadreCajas() respeta umbral configurado dinámicamente")
    void descuadreCajasRespetaUmbralDinamico() {
        ConfiguracionBananaLog.INSTANCIA.setUmbralDescuadrePorcentaje(10.0);
        // 6% de diferencia → bajo el nuevo umbral de 10%, no dispara
        LoteBanano lote = FabricaProductos.crearLoteBanano(
                1000, 940, true, LocalDate.now(), 5);
        assertTrue(Reglas.descuadreCajas().evaluar(lote).isEmpty());
    }

    // =========================================================================
    // PRUEBA 12 — ALERTA: CERTIFICACION_FALTANTE
    // =========================================================================

    @Test
    @DisplayName("Prueba 12a: certificacionFaltante() se dispara cuando lote no está certificado")
    void certificacionFaltanteDisparaConLoteNoCertificado() {
        LoteBanano lote = FabricaProductos.crearLoteBanano(
                500, 500, false, LocalDate.now(), 5);
        var resultado = Reglas.certificacionFaltante().evaluar(lote);
        assertTrue(resultado.isPresent(), "Debe disparar alerta de certificación");
        assertEquals(TipoAlerta.CERTIFICACION_FALTANTE, resultado.get().tipo());
    }

    @Test
    @DisplayName("Prueba 12b: certificacionFaltante() NO se dispara cuando lote está certificado")
    void certificacionFaltanteNoDisparaConLoteCertificado() {
        LoteBanano lote = FabricaProductos.crearLoteBanano(
                500, 500, true, LocalDate.now(), 5);
        var resultado = Reglas.certificacionFaltante().evaluar(lote);
        assertTrue(resultado.isEmpty(), "Con certificación no debe disparar");
    }

    // =========================================================================
    // PRUEBA 13 — ALERTA: FRUTA_PASADA
    // =========================================================================

    @Test
    @DisplayName("Prueba 13a: frutaPasada() se dispara cuando días de tránsito superan umbral")
    void frutaPasadaDisparaCuandoDiasSuperanUmbral() {
        // Umbral = 14 días; lote con 18 días → dispara
        LoteBanano lote = FabricaProductos.crearLoteBanano(
                500, 500, true, LocalDate.now().minusDays(18), 18);
        var resultado = Reglas.frutaPasada().evaluar(lote);
        assertTrue(resultado.isPresent(), "Debe disparar alerta de fruta pasada");
        assertEquals(TipoAlerta.FRUTA_PASADA, resultado.get().tipo());
    }

    @Test
    @DisplayName("Prueba 13b: frutaPasada() NO se dispara en el umbral exacto")
    void frutaPasadaNoDisparaEnUmbralExacto() {
        ConfiguracionBananaLog.INSTANCIA.setUmbralDiasTransitoMaximo(14);
        // Exactamente 14 días → NO dispara (condición es estrictamente mayor)
        LoteBanano lote = FabricaProductos.crearLoteBanano(
                500, 500, true, LocalDate.now().minusDays(14), 14);
        var resultado = Reglas.frutaPasada().evaluar(lote);
        assertTrue(resultado.isEmpty(), "En el umbral exacto de días NO debe disparar");
    }

    @Test
    @DisplayName("Prueba 13c: frutaPasada() respeta umbral configurado dinámicamente")
    void frutaPasadaRespetaUmbralDinamico() {
        ConfiguracionBananaLog.INSTANCIA.setUmbralDiasTransitoMaximo(20);
        LoteBanano lote = FabricaProductos.crearLoteBanano(
                500, 500, true, LocalDate.now().minusDays(18), 18);
        assertTrue(Reglas.frutaPasada().evaluar(lote).isEmpty(),
                "Con umbral 20 días, 18 días no debe disparar");
    }

    // =========================================================================
    // PRUEBA 14 — MOTOR: notifica a todos los observadores
    // =========================================================================

    @Test
    @DisplayName("Prueba 14a: motor notifica a todos los observadores suscritos")
    void motorNotificaATodosLosObservadores() {
        MotorAlertas motor = new MotorAlertas();
        motor.agregarRegla(Reglas.certificacionFaltante());

        List<Alerta> receptor1 = new ArrayList<>();
        List<Alerta> receptor2 = new ArrayList<>();
        motor.suscribir(receptor1::add);
        motor.suscribir(receptor2::add);

        // Lote sin certificación → 1 alerta a 2 observadores
        LoteBanano lote = FabricaProductos.crearLoteBanano(
                100, 100, false, LocalDate.now(), 5);
        int publicadas = motor.procesar(lote);

        assertEquals(1, publicadas, "Debe publicar exactamente 1 alerta");
        assertEquals(1, receptor1.size(), "El primer observador debe recibir 1 alerta");
        assertEquals(1, receptor2.size(), "El segundo observador debe recibir 1 alerta");
    }

    @Test
    @DisplayName("Prueba 14b: motor con tres reglas dispara tres alertas en lote problemático")
    void motorDisparaTresAlertasEnLoteProblematico() {
        MotorAlertas motor = new MotorAlertas();
        motor.agregarRegla(Reglas.descuadreCajas());
        motor.agregarRegla(Reglas.certificacionFaltante());
        motor.agregarRegla(Reglas.frutaPasada());

        List<Alerta> bitacora = new ArrayList<>();
        motor.suscribir(bitacora::add);

        // Lote con los tres problemas a la vez
        LoteBanano lote = FabricaProductos.crearLoteBanano(
                1000, 940, false, LocalDate.now().minusDays(20), 20);

        int publicadas = motor.procesar(lote);
        assertEquals(3, publicadas, "Las tres alertas deben dispararse");
        assertEquals(3, bitacora.size());
    }

    @Test
    @DisplayName("Prueba 14c: motor no dispara ninguna alerta en lote correcto")
    void motorNingunAlertaEnLoteCorrecto() {
        MotorAlertas motor = new MotorAlertas();
        motor.agregarRegla(Reglas.descuadreCajas());
        motor.agregarRegla(Reglas.certificacionFaltante());
        motor.agregarRegla(Reglas.frutaPasada());

        List<Alerta> bitacora = new ArrayList<>();
        motor.suscribir(bitacora::add);

        // Lote perfecto
        LoteBanano lote = FabricaProductos.crearLoteBanano(
                1000, 1000, true, LocalDate.now(), 10);

        int publicadas = motor.procesar(lote);
        assertEquals(0, publicadas);
        assertTrue(bitacora.isEmpty());
    }

    // =========================================================================
    // PRUEBA 15 — FÁBRICA DE OBSERVADORES
    // =========================================================================

    @Test
    @DisplayName("Prueba 15: FabricaObservadores crea observador BITACORA funcional")
    void fabricaObservadoresCreaBitacora() {
        List<Alerta> bitacora = new ArrayList<>();
        ObservadorAlertas obs = FabricaObservadores.crear(TipoObservador.BITACORA, bitacora);

        Alerta alerta = new Alerta(TipoAlerta.FRUTA_PASADA, "LB-TEST-001", "Prueba");
        obs.notificar(alerta);

        assertEquals(1, bitacora.size());
        assertEquals(TipoAlerta.FRUTA_PASADA, bitacora.get(0).tipo());
    }
}
