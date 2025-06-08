import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class View extends JPanel {
    private Model model;
    private JTextField setPointField;
    private JButton botonConsumo;
    private JButton botonModo;
    private Rectangle areaValvula;
    private Image imagenCasa;

    public View(Model model) {
        this.model = model;
        this.setPointField = new JTextField("0.70", 5);

        // Inicializar el área de la válvula
        int centroX = 230;
        int centroY = 71;
        int ancho = 50;
        int alto = 40;
        areaValvula = new Rectangle(centroX - ancho / 2, centroY - alto / 2, ancho, alto);

        // Botón de modo en la parte superior
        botonModo = new JButton("Modo Automático");
        botonModo.setBounds(270, 520, 150, 30);

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

        // Imagen Casa

        try {
            imagenCasa = new ImageIcon(Objects.requireNonNull(getClass().getResource("house.png"))).getImage();
        } catch (Exception e) {
            System.out.println("No se pudo cargar la imagen: " + e.getMessage());
        }

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

        int tanqueX = 400, tanqueY = 100, tanqueW = 100, tanqueH = 300;
        int tuberiaEntradaX = tanqueX - 140;
        int tuberiaEntradaY = tanqueY - 30;

        // ----------- TUBERIA ENTRADA -------------
        g2.setColor(new Color(75, 113, 248));
        // Draw the main rectangle part (straight pipe body)
        g2.fillRect(tuberiaEntradaX - 90, tuberiaEntradaY, 45, 10);

        // Tuberia entre la valvula de entrada y seguridad. Azul solo si la principal esta abierta.

        g2.setColor(model.isValvulaAbierta() ? new Color(75, 113, 248) : Color.GRAY);
        g2.fillRoundRect(tuberiaEntradaX - 20, tuberiaEntradaY, 80, 10, 0, 0);

        // Draw the left rounded cap. Azul solo si la principal y la de seguridad estan abiertas.
        g2.setColor(model.isValvulaAbierta() && model.isValvulaSeguridadAbierta() ? new Color(75, 113, 248) : Color.GRAY);
        g2.fillRoundRect(tuberiaEntradaX + 80, tuberiaEntradaY, 105, 10, 0, 0);
        g2.setColor(model.isValvulaAbierta() && model.isValvulaSeguridadAbierta() ? new Color(75, 113, 248) : Color.GRAY);
        g2.fillRoundRect(tuberiaEntradaX + 185, tuberiaEntradaY, 10, 40, 0, 0);

        // Coordenadas del centro de la válvula
        int centroX = tuberiaEntradaX - 30; // 70 inicial
        int centroY = tuberiaEntradaY + 5;

        // ----------- VALVULA ------------
        if (model.isValvulaAbierta()) {
            dibujarValvula(g2, centroX, centroY, Color.GREEN, false);
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial", Font.PLAIN, 12));
            g2.drawString("Válvula Abierta", tuberiaEntradaX - 75, tuberiaEntradaY + 32);
        } else {
            dibujarValvula(g2, centroX, centroY, Color.RED, false);
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial", Font.PLAIN, 12));
            g2.drawString("Válvula Cerrada", tuberiaEntradaX - 75, tuberiaEntradaY + 32);
        }

        // ----------- VALVULA DE SEGURIDAD ------------
        if (model.isValvulaSeguridadAbierta()) {
            dibujarValvula(g2, centroX + 100, centroY, Color.GREEN, true);
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial", Font.PLAIN, 12));
            g2.drawString("Activada", tuberiaEntradaX + 45, tuberiaEntradaY + 32);
        } else {
            dibujarValvula(g2, centroX + 100, centroY, Color.RED, true);
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial", Font.PLAIN, 12));
            g2.drawString("Desactivada", tuberiaEntradaX + 40, tuberiaEntradaY + 32);
        }

        // ------------ Tanque ------------
        g2.setColor(Color.GRAY);
        g2.fillOval(tanqueX, tanqueY, tanqueW, 30);
        g2.fillRect(tanqueX, tanqueY + 15, tanqueW, tanqueH);
        g2.fillOval(tanqueX, tanqueY + tanqueH, tanqueW, 30);

        // ---------- AGUA DEL TANQUE -----------
        int nivelPix = (int) (tanqueH * model.getNivel());
        if (model.getNivel() > 0) {
            g2.setColor(new Color(75, 113, 248));
            g2.fillRect(tanqueX, tanqueY + tanqueH - nivelPix + 15, tanqueW, nivelPix);
            g2.fillOval(tanqueX, tanqueY + tanqueH - nivelPix + nivelPix - 1, tanqueW, 30);
        }

        // ---------- SALIDA TUBERIA -----------
        int tuberiaSalidaY = tanqueY + tanqueH + 30;
        int tuberiaSalidaX = tanqueX + tanqueW / 2 - 5;
        g2.setColor(model.isConsumoActivo() && model.getNivel() > 0 ? new Color(75, 113, 248) : Color.GRAY);
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

        // SECURITY BOX
        g2.setColor(new Color(168,184,208));
        g2.fillRect(numLevelBoxX - 400, numLevelBoxY + 55, 100, 50);
        g2.setColor(Color.BLACK);
        g2.drawString("LÓGICA DE", numLevelBoxX - 390, numLevelBoxY + 75);
        g2.drawString("SEGURIDAD", numLevelBoxX - 390, numLevelBoxY + 90);
        g2.setColor(Color.BLACK);
        g2.drawString(String.format("%.2f m", model.getNivel()), numLevelBoxX + 15, numLevelBoxY + 20);

        // NUMLEVEL BOX

        g2.setColor(Color.BLACK);
        g2.drawString("NIVEL NUMÉRICO", numLevelBoxX, numLevelBoxY - 10);
        g2.setColor(new Color(135, 206, 250));
        g2.fillRect(numLevelBoxX, numLevelBoxY, 70, 30);
        g2.setColor(Color.BLACK);
        g2.drawString(String.format("%.2f m", model.getNivel()), numLevelBoxX + 15, numLevelBoxY + 20);

        // Lineas de nivel - Principal
        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(lineaNivelX, lineaNivelY, widthHorizontalLevelLine, 2);
        g2.fillRect(lineaNivelX + 50, lineaNivelY, 2, 282);
        g2.fillRect(lineaNivelX, lineaNivelY + 280, widthHorizontalLevelLine, 2);

        // Lineas de nivel - Seguridad
        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(lineaNivelX - 170, lineaNivelY, widthHorizontalLevelLine, 2);
        g2.fillRect(lineaNivelX - 170, lineaNivelY, 2, 282);
        g2.fillRect(lineaNivelX - 170, lineaNivelY + 280, widthHorizontalLevelLine, 2);

        // Variables Linea Salida LT LC
        int statusBoxY = tanqueY + 165;
        int statusBoxX = tanqueX + tanqueW + 100;

        // Sistema de Seguridad

        // LT BOX SECURITY
        g2.setColor(new Color(135, 206, 250));
        g2.fillOval(statusBoxX - 280, tanqueY + 140, 40, 40);
        g2.setColor(Color.BLACK);
        g2.drawString("LT", statusBoxX - 267, statusBoxY); // x2 + (-13)

        // Sistema Tanque

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

        // LINEAS HACIA SP Y VALVULA

        // Lineas Valvula Principal

        g2.drawLine(statusBoxX - 370, 28, statusBoxX - 370, 50);
        g2.drawLine(statusBoxX + 120, 30, statusBoxX + 120, 240);
        g2.drawLine(statusBoxX - 370, statusBoxY - 240, statusBoxX + 120, statusBoxY - 240);

        // Lineas Valvula Seguridad

        g2.setColor(Color.GRAY);
        g2.drawLine(statusBoxX - 271, 14, statusBoxX - 271, 50); // Linea sobre valvula de seguridad
        g2.drawLine(statusBoxX - 480, statusBoxY - 255, statusBoxX - 270, statusBoxY - 255); // Linea horizontal desde valvula de seguridad
        g2.drawLine(statusBoxX - 480, 14, statusBoxX - 480, 170); // Linea vertical proveniente de Valvula
        g2.drawLine(statusBoxX - 480, statusBoxY - 100, statusBoxX - 375, statusBoxY - 100); // Linea horizontal entre Logica y Valvula
        g2.drawLine(statusBoxX - 375, 170, statusBoxX - 375, 235); // Linea vertical arriba de Logica

        // LÍNEA CON FLECHA HACIA LA IZQUIERDA - DESDE SP HACIA LC

        Stroke oldStrokeD = g2.getStroke();
        g2.setStroke(dashed);
        int x1 = statusBoxX - 320;
        int x2 = statusBoxX - 282;
        int y = numLevelDottedY;
        g2.drawLine(x1, y, x2, y);
        int arrowSizeSec = 10;
        double angleSec = Math.atan2(0, x2 - x1);
        int xArrowSec1 = (int) (x2 - arrowSizeSec * Math.cos(angleSec - Math.PI / 6));
        int yArrowSec1 = (int) (y - arrowSizeSec * Math.sin(angleSec - Math.PI / 6));
        int xArrowSec2 = (int) (x2 - arrowSizeSec * Math.cos(angleSec + Math.PI / 6));
        int yArrowSec2 = (int) (y - arrowSizeSec * Math.sin(angleSec + Math.PI / 6));
        Polygon arrowHeadSec = new Polygon();
        arrowHeadSec.addPoint(x2, y);        // punta
        arrowHeadSec.addPoint(xArrowSec1, yArrowSec1); // parte superior en ángulo
        arrowHeadSec.addPoint(xArrowSec2, yArrowSec2); // parte inferior en ángulo
        g2.fillPolygon(arrowHeadSec);

        g2.setStroke(oldStrokeD);

        // Imagen Casa

        if (imagenCasa != null) {
            g.drawImage(imagenCasa, 535, 450, 120, 85, this);  // x, y, width, height
        }
    }

    // Metodos de Dibujo
    private void dibujarValvula(Graphics2D g2, int centroX, int centroY, Color colorValvula, boolean valvulaSeguridad) {
        g2.setColor(colorValvula);

        // Triángulo izquierdo
        Polygon trianguloIzq = new Polygon();
        trianguloIzq.addPoint(centroX, centroY);
        trianguloIzq.addPoint(centroX - 20, centroY - 10);
        trianguloIzq.addPoint(centroX - 20, centroY + 10);
        g2.fill(trianguloIzq);

        // Triángulo derecho
        Polygon trianguloDer = new Polygon();
        trianguloDer.addPoint(centroX, centroY);
        trianguloDer.addPoint(centroX + 20, centroY - 10);
        trianguloDer.addPoint(centroX + 20, centroY + 10);
        g2.fill(trianguloDer);

        // Conector vertical
        g2.fillRect(centroX - 3, centroY - 15, 4, 15);

        // Parte superior: semicirculo o cuadrado
        if (valvulaSeguridad) {
            // Cuadrado
            g2.fillRect(centroX - 11, centroY - 20, 20, 10);
        } else {
            // Semicírculo
            g2.fillArc(centroX - 11, centroY - 20, 20, 20, 0, 180);
        }
    }
}

