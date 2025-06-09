import com.formdev.flatlaf.intellijthemes.FlatCyanLightIJTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ControlNivelTanque {
    public static void main(String[] args) {
        FlatCyanLightIJTheme.setup();

        try {
            UIManager.setLookAndFeel( new FlatCyanLightIJTheme() );
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("ControlNivelTanque");
        frame.setSize(1250, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(new BorderLayout());

        JPanel panelSuperior = new JPanel();
        JPanel panelInferior = new JPanel();

        // Crear el Modelo, la Vista y el Controlador
        Model model = new Model();
        View view = new View(model);
        Controller controller = new Controller(model, view);

        // Obtener el campo de texto setPoint de la vista
        JTextField campoSP = view.getSetPointField();
        JLabel funcionConsumoLabel = new JLabel("Función de consumo: f(t) = 0.004 + 0.01 * sen(t)");
        funcionConsumoLabel.setBounds(10, 550, 300, 20); // Ejemplo de posición

        JButton iniciar = new JButton("►  Iniciar");
        JButton pausar = new JButton("⏸  Pausar");
        JButton reiniciar = new JButton("⟳  Reiniciar");

        // Aplicar estilos

        view.applyButtonStyles(iniciar);
        view.applyButtonStyles(pausar);
        view.applyButtonStyles(reiniciar);

        // Agregar componentes a los paneles
        panelSuperior.setBackground(new Color(246, 249, 253));
        panelSuperior.add(new JLabel("SetPoint (0–1):"));;

        panelSuperior.add(campoSP);
        panelSuperior.add(iniciar);
        panelSuperior.add(pausar);
        panelSuperior.add(reiniciar);
        panelInferior.add(funcionConsumoLabel);

        // Llamamos a la función que modifica el panel superior
        aplicarEstiloPanelSuperior(panelSuperior);

        // Agregar listeners a los botones, conectándolos al controlador
        iniciar.addActionListener(e -> controller.iniciar());
        pausar.addActionListener(e -> controller.pausar());
        reiniciar.addActionListener(e -> controller.reiniciar());

        // Agregar los paneles y la vista al frame

        // Contenedor para margen superior (16px)
        JPanel contenedorSuperior = new JPanel();
        contenedorSuperior.setLayout(new BoxLayout(contenedorSuperior, BoxLayout.Y_AXIS));
        contenedorSuperior.setBackground(new Color(239, 245, 253));
        contenedorSuperior.add(Box.createRigidArea(new Dimension(0, 16))); // margen superior

        // Wrapper para centrar panelSuperior con su ancho personalizado
        JPanel wrapperPanelSuperior = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        wrapperPanelSuperior.setOpaque(false);
        wrapperPanelSuperior.setBackground(new Color(239, 245, 253));
        wrapperPanelSuperior.add(panelSuperior);

        contenedorSuperior.add(wrapperPanelSuperior);

        frame.add(contenedorSuperior, BorderLayout.NORTH);

        frame.add(panelInferior, BorderLayout.SOUTH);
        frame.add(view, BorderLayout.CENTER);
        // frame.add(view.getGraphPanel(), BorderLayout.EAST); // Añadir la gráfica al lado derecho

        // Ajustar tamaño panelSuperior al 50% del ancho frame
        frame.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                int frameWidth = frame.getContentPane().getWidth();
                panelSuperior.setPreferredSize(new Dimension(frameWidth / 2 + 200, 50));
                panelSuperior.revalidate();
            }
        });

        // Hacer visible el frame
        frame.setVisible(true);

        // Disparar el resize manual para aplicar tamaño inicial
        int frameWidth = frame.getContentPane().getWidth();
        panelSuperior.setPreferredSize(new Dimension(frameWidth / 2 + 200, 50));
        panelSuperior.revalidate();
    }

    // Función para aplicar padding top, borde redondeado y ajustar estilos
    private static void aplicarEstiloPanelSuperior(JPanel panel) {
        // Padding top 16px (0 izquierda, 0 abajo, 0 derecha)
        panel.setBorder(new EmptyBorder(11, 10, 10, 10));

        panel.setOpaque(false);
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));

        panel.setUI(new RoundedPanelUI());
    }

    static class RoundedPanelUI extends javax.swing.plaf.basic.BasicPanelUI {
        @Override
        public void paint(Graphics g, JComponent c) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(new Color(246, 249, 253));
            g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 20, 20);

            g2.setColor(new Color(180, 180, 180));
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(0, 0, c.getWidth() - 1, c.getHeight() - 1, 20, 20);

            g2.dispose();

            super.paint(g, c);
        }
    }
}