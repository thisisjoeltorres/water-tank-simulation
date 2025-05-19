public class Model {
    private double nivel = 0.0;
    private double setPoint = 0.7;
    private boolean valvulaAbierta = false;
    private boolean simulando = true;
    private boolean consumoActivo = false;
    private double tiempo = 0;
    private boolean modoAutomatico = true;

    public double getNivel() {
        return nivel;
    }

    public void setNivel(double nivel) {
        this.nivel = nivel;
    }

    public double getSetPoint() {
        return setPoint;
    }

    public void setSetPoint(double setPoint) {
        this.setPoint = setPoint;
    }

    public boolean isValvulaAbierta() {
        return valvulaAbierta;
    }

    public void setValvulaAbierta(boolean valvulaAbierta) {
        this.valvulaAbierta = valvulaAbierta;
    }

    public boolean isSimulando() {
        return simulando;
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

    public double getTiempo() {
        return tiempo;
    }

    public void setTiempo(double tiempo) {
        this.tiempo = tiempo;
    }

    public boolean isModoAutomatico() {
        return modoAutomatico;
    }

    public void setModoAutomatico(boolean modoAutomatico) {
        this.modoAutomatico = modoAutomatico;
    }

    public double funcionConsumo(double tiempo) {
        return 0.002 + 0.001 * Math.sin(tiempo);
    }

    // Actualización según el modo
    public void actualizar() {
        tiempo += 0.1;
        if (!simulando) return;

        if (modoAutomatico) {
            valvulaAbierta = nivel < setPoint;
        } else {
            if (nivel > setPoint) {
                valvulaAbierta = false;
            }
        }

        if (valvulaAbierta) {
            nivel += 0.01;
            if (nivel > 1.0) nivel = 1.0;
        }

        if (modoAutomatico) {
            if (nivel >= setPoint) consumoActivo = true;
        }

        if (consumoActivo) {
            double consumo = funcionConsumo(tiempo);
            nivel -= consumo;
            if (nivel < 0) nivel = 0;
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



