import javax.swing.*;
import java.awt.*;

public class ControlNivelTanque {
    public static void main(String[] args) {
        JFrame frame = new JFrame("ControlNivelTanque");
        frame.setSize(1000, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel panelSuperior = new JPanel();
        JPanel panelInferior = new JPanel();

        // Crear el Modelo, la Vista y el Controlador
        Model model = new Model();
        View view = new View(model);
        Controller controller = new Controller(model, view);

        // Obtener el campo de texto setPoint de la vista
        JTextField campoSP = view.getSetPointField();
        JLabel funcionConsumoLabel = new JLabel("Función de consumo: f(t) = 0.004 + 0.003 * sen(t)");
        funcionConsumoLabel.setBounds(10, 550, 300, 20); // Ejemplo de posición

        JButton iniciar = new JButton("Iniciar");
        JButton pausar = new JButton("Pausar");
        JButton reiniciar = new JButton("Reiniciar");

        // Agregar componentes a los paneles
        panelSuperior.add(new JLabel("SetPoint (0–1):"));
        panelSuperior.add(campoSP);
        panelSuperior.add(iniciar);
        panelSuperior.add(pausar);
        panelSuperior.add(reiniciar);
        panelInferior.add(funcionConsumoLabel);

        // Agregar listeners a los botones, conectándolos al controlador
        iniciar.addActionListener(e -> controller.iniciar());
        pausar.addActionListener(e -> controller.pausar());
        reiniciar.addActionListener(e -> controller.reiniciar());

        // Agregar los paneles y la vista al frame
        frame.add(panelSuperior, BorderLayout.NORTH);
        frame.add(panelInferior, BorderLayout.SOUTH);
        frame.add(view, BorderLayout.CENTER);

        // Hacer visible el frame
        frame.setVisible(true);
    }
}