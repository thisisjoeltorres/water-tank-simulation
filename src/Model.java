public class Model {
    private double nivel = 0.0;
    private double setPoint = 0.7;
    private boolean valvulaAbierta = false;
    private boolean valvulaSeguridadAbierta = true;
    private int interaccionesDeSeguridad = 0;
    private boolean consumoActivo = false;
    private double tiempo = 0;
    private boolean simulando = true;
    private boolean modoAutomatico = true;
    public static final double MAX_LEVEL = 1;
    public static final double MIN_LEVEL = 0.00;

    public double getNivel() {
        return nivel;
    }

    public int getErrorSimulationCounter() { return interaccionesDeSeguridad; }

    public void increaseErrorCounter() { interaccionesDeSeguridad++; }

    public double getSetPoint() {
        return setPoint;
    }

    public void setSetPoint(double setPoint) {
        this.setPoint = setPoint;
    }

    public boolean isValvulaSeguridadAbierta() {
        return valvulaSeguridadAbierta;
    }

    public boolean isValvulaAbierta() {
        return valvulaAbierta;
    }

    public void setValvulaAbierta(boolean valvulaAbierta) {
        this.valvulaAbierta = valvulaAbierta;
    }

    public void setValvulaSeguridadAbierta(boolean valvulaSeguridadAbierta) {
        this.valvulaSeguridadAbierta = valvulaSeguridadAbierta;
    }

    public void setSimulando(boolean simulando) {
        this.simulando = simulando;
    }

    public boolean isConsumoActivo() {
        return consumoActivo;
    }

    public void setConsumoActivo(boolean consumoActivo) {
        this.consumoActivo = consumoActivo;
    }

    public boolean isModoAutomatico() {
        return modoAutomatico;
    }

    public void setModoAutomatico(boolean modoAutomatico) {
        this.modoAutomatico = modoAutomatico;
    }

    public double funcionConsumo(double tiempo) {
        double consumo = 0.004 + 0.01 * Math.sin(tiempo);
        return consumo > 0 ? consumo : 0;
    }

    public boolean estaLleno() {
        return nivel >= MAX_LEVEL - 0.05;
    }

    public boolean estaVacio() {
        return nivel <= MIN_LEVEL;
    }

    public void cerrarValvulaEntrada() {
        valvulaAbierta = false;
    }

    public void cerrarValvulaSeguridad() {
        valvulaSeguridadAbierta = false;
    }

    public void cerrarValvulaSalida() {
        consumoActivo = false;
    }

    // Actualización según el modo
    public void actualizar() {
        tiempo += 0.2;
        if (!simulando) return;

        // Detectar si el modo automatico esta activado.

        if (modoAutomatico) {
            valvulaAbierta = nivel < setPoint;
        }

        // Incrementar valor de nivel de acuerdo al estado de la valvula de entrada y seguridad.

        if (valvulaAbierta && valvulaSeguridadAbierta) {
            nivel += 0.01;
            if (nivel > 1.0) nivel = 1.0;
        }

        // Activar tuberia de salida cuando el tanque pase del setPoint.

        if (modoAutomatico) {
            if (nivel >= setPoint) consumoActivo = true;
        }

        if (consumoActivo) {
            double consumo = funcionConsumo(tiempo);
            nivel -= consumo;
            if (nivel < 0) nivel = 0;
        }

        // Sistema de Seguridad

        // Control automático de la válvula de seguridad
        if (nivel >= MAX_LEVEL - 0.05) {
            if (interaccionesDeSeguridad < 4) {
                valvulaSeguridadAbierta = false;
            }
        } else if (nivel <= MAX_LEVEL - 0.1) {
            valvulaSeguridadAbierta = true;
        }
    }

    public void abrirValvula() {
        valvulaAbierta = true;
        simulando = true;
    }

    public void reiniciar() {
        simulando = false;
        consumoActivo = false;
        valvulaAbierta = false;
        nivel = 0.0;
        tiempo = 0;
        modoAutomatico = true;
    }
}



