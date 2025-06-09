import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GraphPanel extends JPanel {
    private final List<Double> niveles;
    private final List<Double> tiempos;
    private final double intervaloTiempo = 20.0; // Intervalo de tiempo fijo en segundos

    public GraphPanel() {
        this.niveles = new ArrayList<>();
        this.tiempos = new ArrayList<>();
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    public void addNivel(double nivel) {
        if (!tiempos.isEmpty() && tiempos.get(tiempos.size() - 1) >= intervaloTiempo) {
            // Eliminar valores fuera del intervalo de tiempo
            while (!tiempos.isEmpty() && tiempos.get(0) < tiempos.get(tiempos.size() - 1) - intervaloTiempo) {
                niveles.remove(0);
                tiempos.remove(0);
            }
        }
        niveles.add(nivel);
        tiempos.add(tiempos.isEmpty() ? 0 : tiempos.get(tiempos.size() - 1) + 0.2); // Incrementar tiempo
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Fondo con bordes redondeados
        g2.setColor(new Color(246, 249, 253)); // Color suave similar al header
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

        // Borde de la gráfica
        g2.setColor(new Color(180, 180, 180));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);

        // Dimensiones
        int padding = 40;
        int width = getWidth() - 2 * padding;
        int height = getHeight() - 2 * padding;

        // Dibujar cuadrícula con líneas suaves
        g2.setColor(new Color(220, 220, 220));
        g2.setStroke(new BasicStroke(1)); // Líneas suaves
        for (int i = 0; i <= 10; i++) {
            int y = padding + i * height / 10;
            g2.drawLine(padding, y, padding + width, y); // Líneas horizontales
            int x = padding + i * width / 10;
            g2.drawLine(x, padding, x, padding + height); // Líneas verticales
        }

        // Ejes
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2)); // Ejes más gruesos
        g2.drawLine(padding, padding + height, padding + width, padding + height); // Eje X
        g2.drawLine(padding, padding, padding, padding + height); // Eje Y

        // Títulos con estilo moderno
        g2.setFont(new Font("Arial", Font.BOLD, 12));
        g2.setColor(new Color(60, 60, 60));
        g2.drawString("Tiempo (s)", padding + width / 2 - 40, padding + height + 32);
        g2.drawString("Nivel", padding - 30, padding - 10);

        // Puntos de referencia en el eje Y
        g2.setFont(new Font("Arial", Font.PLAIN, 12));
        for (int i = 0; i <= 10; i++) {
            int y = padding + i * height / 10;
            String label = String.format("%.1f", 1.0 - i * 0.1);
            g2.drawString(label, padding - 30, y + 5);
        }

        // Puntos de referencia en el eje X
        if (!tiempos.isEmpty()) {
            double tiempoInicio = tiempos.get(0);
            double tiempoFin = tiempos.get(tiempos.size() - 1);
            for (int i = 0; i <= 10; i++) {
                int x = padding + i * width / 10;
                double tiempoEtiqueta = tiempoInicio + (tiempoFin - tiempoInicio) * i / 10;
                String label = String.format("%.1f", tiempoEtiqueta);
                g2.drawString(label, x - 10, padding + height + 20);
            }
        }

        // Dibujar gráfica
        if (niveles.size() > 1) {
            g2.setColor(new Color(75, 113, 248)); // Azul moderno para la línea
            int prevX = padding;
            int prevY = padding + height - (int) (niveles.get(0) * height);
            double tiempoInicio = tiempos.get(0);
            double tiempoFin = tiempos.get(tiempos.size() - 1);
            for (int i = 1; i < niveles.size(); i++) {
                int x = padding + (int) ((tiempos.get(i) - tiempoInicio) / (tiempoFin - tiempoInicio) * width);
                int y = padding + height - (int) (niveles.get(i) * height);
                g2.drawLine(prevX, prevY, x, y); // Línea entre puntos
                prevX = x;
                prevY = y;
            }
        }
    }
}
