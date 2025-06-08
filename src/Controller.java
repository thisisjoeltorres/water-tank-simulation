import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.InputStream;
import java.util.Objects;
import javax.sound.sampled.*;

public class Controller {
    private Model model;
    private View view;
    private Timer timer;
    private boolean alarmNotFired = true;
    private Clip alarmaClip;

    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;

        // Initialize the timer
        timer = new Timer(150, e -> {
            this.model.actualizar();

            // Validaciones de seguridad
            validarNiveles();

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

    private void validarNiveles() {
        double nivel = model.getNivel();

        if (model.estaLleno()) {
            model.cerrarValvulaEntrada();

            if (alarmNotFired) {
                alarmNotFired = false;
                reproducirAlarma();
                mostrarPopupAlarma("¡Tanque lleno!");
            }

        } else {
            if (nivel < Model.MAX_LEVEL && nivel > Model.MIN_LEVEL && !alarmNotFired) {
                alarmNotFired = true;
            }

            if (model.estaVacio()) {
                if (alarmNotFired) {
                    alarmNotFired = false;
                    reproducirAlarma();
                    mostrarPopupAlarma("¡Tanque vacio!");
                }
                model.cerrarValvulaSalida();
            }
        }

        view.setBotonConsumoText(model.isConsumoActivo() ? "Desactivar Consumo" : "Activar Consumo");
    }

    private void reproducirAlarma() {
        try {
            InputStream is = Objects.requireNonNull(getClass().getResource("alarm-sound.wav")).openStream();
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(is);
            alarmaClip = AudioSystem.getClip();
            alarmaClip.open(audioInput);
            alarmaClip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void detenerAlarma() {
        if (alarmaClip != null && alarmaClip.isRunning()) {
            alarmaClip.stop();
            alarmaClip.close();
        }
    }

    private void mostrarPopupAlarma(String label) {
        SwingUtilities.invokeLater(() -> {
            JButton stopButton = new JButton("Detener alarma");
            JPanel panel = new JPanel();
            panel.add(new JLabel(label));
            panel.add(stopButton);

            JDialog dialog = new JDialog((JFrame) null, "Alerta de Seguridad", true);
            dialog.getContentPane().add(panel);
            dialog.pack();
            dialog.setLocationRelativeTo(null);

            stopButton.addActionListener(ev -> {
                detenerAlarma();
                dialog.dispose();
            });

            dialog.setVisible(true);
        });
    }

    private boolean puedeAbrirValvula() {
        return !model.isModoAutomatico() && model.getNivel() < Model.MAX_LEVEL;
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
        detenerAlarma();
        model.reiniciar();
        view.setBotonModoEnabled(false);
        timer.stop();
        view.repaint();
    }
}

