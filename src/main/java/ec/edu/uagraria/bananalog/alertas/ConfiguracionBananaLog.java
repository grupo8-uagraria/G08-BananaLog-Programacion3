package ec.edu.uagraria.bananalog.alertas;

/**
 * Configuración centralizada de umbrales del motor BananaLog.
 *
 * <p>Implementación Singleton mediante enum (Bloch, 2018, ítem 3).
 * Principio S: responsabilidad única de almacenar umbrales del dominio.<br>
 * Ningún umbral queda «quemado» dentro de las reglas; todas lo consultan aquí.</p>
 *
 * <h3>Justificación de umbrales (dominio bananero ecuatoriano)</h3>
 * <ul>
 *   <li><b>Descuadre de cajas ≥ 3 %</b>: tolerancia estándar de exportadoras
 *       como Dole/Chiquita para diferencias entre manifiesto y conteo físico.
 *       Superar el 3 % implica posibles pérdidas o fraude en bodega.</li>
 *   <li><b>Días de tránsito ≥ 15 días</b>: el banano verde tarda entre 12 y
 *       18 días en barco; pasados 14 días empieza el proceso de maduración
 *       acelerada. Umbral en 15 señala riesgo de fruta pasada al destino.</li>
 * </ul>
 */
public enum ConfiguracionBananaLog {
    INSTANCIA;

    /** Porcentaje máximo de diferencia entre cajas declaradas y contadas (3 %). */
    private double umbralDescuadrePorcentaje = 3.0;

    /** Días máximos de tránsito desde cosecha antes de considerar fruta pasada. */
    private int umbralDiasTransitoMaximo = 14;

    // ---- Getters y setters (para pruebas y configuración dinámica) ----------

    public double getUmbralDescuadrePorcentaje() {
        return umbralDescuadrePorcentaje;
    }

    public void setUmbralDescuadrePorcentaje(double umbral) {
        this.umbralDescuadrePorcentaje = umbral;
    }

    public int getUmbralDiasTransitoMaximo() {
        return umbralDiasTransitoMaximo;
    }

    public void setUmbralDiasTransitoMaximo(int dias) {
        this.umbralDiasTransitoMaximo = dias;
    }
}
