/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author KEERTHI
 */
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import ui.LoginFrame;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            System.out.println("╔════════════════════════════════════════╗");
            System.out.println("║   🛍️  SHOPPING CART APPLICATION 🛍️     ║");
            System.out.println("║          Starting...                    ║");
            System.out.println("╚════════════════════════════════════════╝\n");
            try {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) {
        e.printStackTrace();
    }
            new LoginFrame();
        });
    }
}