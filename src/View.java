import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

public class View extends JPanel {
    private Model model;
    private JTextField setPointField;
    private JButton botonConsumo;
    private JButton botonModo;
    private Rectangle areaValvula;

    public View(Model model) {
        this.model = model;
        this.setPointField = new JTextField("0.70", 5);

        // Inicializar el área de la válvula
        int centroX = 230;
        int centroY = 135;
        int ancho = 50;
        int alto = 40;
        areaValvula = new Rectangle(centroX - ancho / 2, centroY - alto / 2, ancho, alto);

        // Botón de modo en la parte superior
        botonModo = new JButton("Modo Automático");
        botonModo.setBounds(270, 20, 150, 30);

        // Botón de consumo en la parte inferior
        botonConsumo = new JButton("Activar Consumo");
        botonConsumo.setBounds(270, 480, 150, 30);

        botonConsumo.setEnabled(false);
        botonModo.setEnabled(false);

        // Aplicamos los estilos a los botones.

        applyButtonStyles(botonConsumo);
        applyButtonStyles(botonModo);

        // Agregamos los botones a nuestra intefaz.

        this.add(botonConsumo);
        this.add(botonModo);

        this.setLayout(null);
    }

    public void updateBotonConsumoStyle(boolean activo) {
        // Vuelve al estilo original cuando no está activo
        Color colorFondoActivado = Color.decode("#4285EC");  // Fondo claro
        Color colorTextoActivado = Color.decode("#FFFFFF");  // Texto oscuro
        Color colorFondoDesactivado = Color.decode("#EAEDF1");  // Fondo claro
        Color colorTextoDesactivado = Color.decode("#040502");  // Texto oscuro

        if (activo) {
            botonConsumo.setBackground(colorFondoActivado);
            botonConsumo.setForeground(colorTextoActivado);
        } else {
            botonConsumo.setBackground(colorFondoDesactivado);
            botonConsumo.setForeground(colorTextoDesactivado);
        }
        botonConsumo.setOpaque(true);
        botonConsumo.repaint();
    }

    public void applyButtonStyles(JButton button) {
        Color colorFondo = Color.decode("#EAEDF1");  // Fondo
        Color colorTexto = Color.decode("#040502");  // Texto

        button.setBackground(colorFondo);
        button.setForeground(colorTexto);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setOpaque(true);
    }

    public JTextField getSetPointField() {
        return setPointField;
    }

    public JButton getBotonConsumo() {
        return botonConsumo;
    }

    public JButton getBotonModo() {
        return botonModo;
    }

    public Rectangle getAreaValvula() {
        return areaValvula;
    }

    public void setBotonConsumoEnabled(boolean enabled) {
        updateBotonConsumoStyle(enabled);  // <- Aquí
        botonConsumo.setEnabled(enabled);
    }

    public void setBotonModoEnabled(boolean enabled) {
        botonModo.setEnabled(enabled);
    }

    public void setBotonModoText(String text) {
        botonModo.setText(text);
    }

    public void setBotonConsumoText(String text) {
        botonConsumo.setText(text);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(new Color(239, 245, 253));
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int tanqueX = 300, tanqueY = 100, tanqueW = 100, tanqueH = 300;
        int tuberiaEntradaX = tanqueX - 140;
        int tuberiaEntradaY = tanqueY + 30;

        // ----------- TUBERIA ENTRADA -------------
        g2.setColor(new Color(135, 206, 250));
        // Draw the main rectangle part (straight pipe body)
        g2.fillRect(tuberiaEntradaX + 5, tuberiaEntradaY, 55, 10);

// Draw the left rounded cap
        g2.fillArc(tuberiaEntradaX, tuberiaEntradaY, 10, 10, 90, 180);
        g2.setColor(model.isValvulaAbierta() ? new Color(135, 206, 250) : Color.GRAY);
        g2.fillRoundRect(tuberiaEntradaX + 80, tuberiaEntradaY, 60, 10, 0, 0);

        // Coordenadas del centro de la válvula
        int centroX = tuberiaEntradaX + 70;
        int centroY = tuberiaEntradaY + 5;

        // ----------- VALVULA ------------
        if (model.isValvulaAbierta()) {
            dibujarValvula(g2, centroX, centroY, Color.GREEN);
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial", Font.PLAIN, 12));
            g2.drawString("Válvula Abierta", tuberiaEntradaX + 25, tuberiaEntradaY + 39);
        } else {
            dibujarValvula(g2, centroX, centroY, Color.RED);
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial", Font.PLAIN, 12));
            g2.drawString("Válvula Cerrada", tuberiaEntradaX + 25, tuberiaEntradaY + 39);
        }

        // ------------ Tanque ------------
        g2.setColor(Color.GRAY);
        g2.fillOval(tanqueX, tanqueY, tanqueW, 30);
        g2.fillRect(tanqueX, tanqueY + 15, tanqueW, tanqueH);
        g2.fillOval(tanqueX, tanqueY + tanqueH, tanqueW, 30);

        // ---------- AGUA DEL TANQUE -----------
        int nivelPix = (int) (tanqueH * model.getNivel());
        if (model.getNivel() > 0) {
            g2.setColor(new Color(135, 206, 250));
            g2.fillRect(tanqueX, tanqueY + tanqueH - nivelPix + 15, tanqueW, nivelPix);
            g2.fillOval(tanqueX, tanqueY + tanqueH - nivelPix + nivelPix - 1, tanqueW, 30);
        }

        // ---------- SALIDA TUBERIA -----------
        int tuberiaSalidaY = tanqueY + tanqueH + 30;
        int tuberiaSalidaX = tanqueX + tanqueW / 2 - 5;
        g2.setColor(model.isConsumoActivo() && model.getNivel() > 0 ? new Color(135, 206, 250) : Color.GRAY);
        g2.fillRect(tuberiaSalidaX, tuberiaSalidaY, 10, 20);
        g2.fillRect(tuberiaSalidaX, tuberiaSalidaY + 20, 100, 10);
        g2.fillRect(tuberiaSalidaX + 100, tuberiaSalidaY + 20, 10, 20);

        // -------- LINEAS DE NIVEL ---------
        int lineaNivelY = tanqueY + tanqueH - 280;
        int lineaNivelX = tanqueX + tanqueW + 10;
        int widthHorizontalLevelLine = 50;

        // ----------- NIVEL NUMERICO - BOX ----------
        int numLevelBoxY = tanqueY + 80;
        int numLevelBoxX = tanqueX + tanqueW + 80;
        int numLevelDottedY = numLevelBoxY + 80;

        g2.setColor(Color.BLACK);
        g2.drawString("NIVEL NUMÉRICO", numLevelBoxX, numLevelBoxY - 10);
        g2.setColor(new Color(135, 206, 250));
        g2.fillRect(numLevelBoxX, numLevelBoxY, 70, 30);
        g2.setColor(Color.BLACK);
        g2.drawString(String.format("%.2f m", model.getNivel()), numLevelBoxX + 15, numLevelBoxY + 20);

        // Salida tubería
        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(lineaNivelX, lineaNivelY, widthHorizontalLevelLine, 2);
        g2.fillRect(lineaNivelX + 50, lineaNivelY, 2, 282);
        g2.fillRect(lineaNivelX, lineaNivelY + 280, widthHorizontalLevelLine, 2);

        // Variables Linea Salida LT LC
        int statusBoxY = tanqueY + 165;
        int statusBoxX = tanqueX + tanqueW + 100;

        // LT BOX
        g2.setColor(new Color(135, 206, 250));
        g2.fillOval(statusBoxX - 60, tanqueY + 140, 40, 40);
        g2.setColor(Color.BLACK);
        g2.drawString("LT", statusBoxX - 47, statusBoxY);

        // LC BOX
        g2.setColor(new Color(144, 238, 144));
        g2.fillOval(statusBoxX + 100, statusBoxY - 25, 40, 40);
        g2.setColor(Color.BLACK);
        g2.drawString("LC", statusBoxX + 113, statusBoxY);

        // SP BOX
        g2.setColor(new Color(173, 216, 230));
        g2.fillRect(statusBoxX + 180, statusBoxY - 20, 90, 30);
        g2.setColor(Color.BLACK);
        g2.drawString("SP = " + String.format("%.2f", model.getSetPoint()) + " m", statusBoxX + 190, statusBoxY);

        // LINEA PUNTEADA - HACIA LC
        Stroke oldStroke = g2.getStroke();
        g2.setColor(Color.BLACK);
        float[] dashPattern = {6f, 6f};
        Stroke dashed = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, dashPattern, 0);
        g2.setStroke(dashed);
        g2.drawLine(statusBoxX - 20, numLevelDottedY, statusBoxX + 100, numLevelDottedY);
        g2.setStroke(oldStroke);
        int arrowSize = 10;
        double angle = Math.atan2(numLevelDottedY - numLevelDottedY, (statusBoxX + 100) - (statusBoxX - 20));
        int xArrow1 = (int) ((statusBoxX + 100) - arrowSize * Math.cos(angle - Math.PI / 6));
        int yArrow1 = (int) (numLevelDottedY - arrowSize * Math.sin(angle - Math.PI / 6));
        int xArrow2 = (int) ((statusBoxX + 100) - arrowSize * Math.cos(angle + Math.PI / 6));
        int yArrow2 = (int) (numLevelDottedY - arrowSize * Math.sin(angle + Math.PI / 6));
        Polygon arrowHead = new Polygon();
        arrowHead.addPoint(statusBoxX + 100, numLevelDottedY);
        arrowHead.addPoint(xArrow1, yArrow1);
        arrowHead.addPoint(xArrow2, yArrow2);
        g2.fillPolygon(arrowHead);

        // LÍNEA CON FLECHA HACIA LA IZQUIERDA - DESDE SP HACIA LC
        Stroke oldStrokeB = g2.getStroke();
        g2.setColor(Color.BLACK);
        Stroke solidB = new BasicStroke(2);
        g2.setStroke(solidB);
        g2.drawLine(statusBoxX + 177, numLevelDottedY, statusBoxX + 143, numLevelDottedY);
        g2.setStroke(oldStrokeB);
        int arrowSizeB = 10;
        double angleB = Math.atan2(numLevelDottedY - numLevelDottedY, (statusBoxX + 143) - (statusBoxX + 177));
        int xArrow1B = (int) ((statusBoxX + 143) - arrowSizeB * Math.cos(angleB - Math.PI / 6));
        int yArrow1B = (int) (numLevelDottedY - arrowSizeB * Math.sin(angleB - Math.PI / 6));
        int xArrow2B = (int) ((statusBoxX + 143) - arrowSizeB * Math.cos(angleB + Math.PI / 6));
        int yArrow2B = (int) (numLevelDottedY - arrowSizeB * Math.sin(angleB + Math.PI / 6));
        Polygon arrowHeadB = new Polygon();
        arrowHeadB.addPoint(statusBoxX + 143, numLevelDottedY);
        arrowHeadB.addPoint(xArrow1B, yArrow1B);
        arrowHeadB.addPoint(xArrow2B, yArrow2B);
        g2.fillPolygon(arrowHeadB);

        // Linea Valvula a LC
        Stroke oldStrokeC = g2.getStroke();
        g2.setColor(Color.BLACK);
        float[] dashPatternC = {6f, 6f};
        Stroke dashedStrokeC = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, dashPatternC, 0);
        g2.setStroke(dashedStrokeC);
        g2.drawLine(statusBoxX - 270, 70, statusBoxX - 270, 110);
        g2.drawLine(statusBoxX + 120, 70, statusBoxX + 120, 240);
        g2.drawLine(statusBoxX - 270, statusBoxY - 200, statusBoxX + 124, statusBoxY - 200);
        g2.setStroke(oldStrokeC);
    }

    // Metodos de Dibujo
    private void dibujarValvula(Graphics2D g2, int centroX, int centroY, Color colorValvula) {
        g2.setColor(colorValvula);
        Polygon trianguloIzq = new Polygon();
        trianguloIzq.addPoint(centroX, centroY);
        trianguloIzq.addPoint(centroX - 20, centroY - 10);
        trianguloIzq.addPoint(centroX - 20, centroY + 10);
        g2.fill(trianguloIzq);
        Polygon trianguloDer = new Polygon();
        trianguloDer.addPoint(centroX, centroY);
        trianguloDer.addPoint(centroX + 20, centroY - 10);
        trianguloDer.addPoint(centroX + 20, centroY + 10);
        g2.fill(trianguloDer);
        g2.fillRect(centroX - 3, centroY - 15, 4, 15);
        g2.fillArc(centroX - 11, centroY - 20, 20, 20, 0, 180);
    }
}

