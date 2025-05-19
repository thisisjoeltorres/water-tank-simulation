import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Controller {
    private Model model;
    private View view;
    private Timer timer;

    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;

        // Initialize the timer
        timer = new Timer(150, e -> {
            this.model.actualizar();
            this.view.repaint();
        });

        // Add action listeners to the buttons in the view
        view.getBotonModo().addActionListener(e -> toggleModo());
        view.getBotonConsumo().addActionListener(e -> toggleConsumo());

        // Mouse listener for the valve in the view
        view.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (view.getAreaValvula().contains(e.getPoint()) && puedeAbrirValvula()) {
                    model.setValvulaAbierta(!model.isValvulaAbierta());
                    if (model.isValvulaAbierta()) {
                        model.abrirValvula();
                    }
                    view.repaint();
                }
            }
        });

        view.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (view.getAreaValvula().contains(e.getPoint())) {
                    if (puedeAbrirValvula()) {
                        view.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    } else {
                        view.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    }
                } else {
                    view.setCursor(Cursor.getDefaultCursor());
                }
            }
        });
    }

    private boolean puedeAbrirValvula() {
        return !model.isModoAutomatico() && model.getNivel() < model.getSetPoint();
    }

    private void toggleModo() {
        model.setModoAutomatico(!model.isModoAutomatico());
        view.setBotonModoText(model.isModoAutomatico() ? "Modo Automático" : "Modo Manual");
        model.setValvulaAbierta(false);
        model.setConsumoActivo(false);
        view.setBotonConsumoEnabled(!model.isModoAutomatico());
        view.repaint();
    }

    private void toggleConsumo() {
        model.setConsumoActivo(!model.isConsumoActivo());
        view.setBotonConsumoText(model.isConsumoActivo() ? "Desactivar Consumo" : "Activar Consumo");
        view.repaint();
    }

    public void iniciar() {
        try {
            double sp = Double.parseDouble(view.getSetPointField().getText());
            if (sp < 0 || sp > 1.0) {
                throw new NumberFormatException();
            }
            model.setSetPoint(sp);
            model.setSimulando(true);
            model.setModoAutomatico(true);
            model.setValvulaAbierta(true);
            model.setConsumoActivo(false);
            view.setBotonModoEnabled(true);
            timer.start();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "SetPoint inválido (0–1)", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void pausar() {
        model.setSimulando(false);
        view.setBotonModoEnabled(false);
        timer.stop();
        view.repaint();
    }

    public void reiniciar() {
        model.reiniciar();
        view.setBotonModoEnabled(false);
        timer.stop();
        view.repaint();
    }

    public void abrirValvula() {
        model.setValvulaAbierta(true);
        model.setSimulando(true);
        view.repaint();
    }

    public void startSimulation() {
        timer.start();
    }
}

