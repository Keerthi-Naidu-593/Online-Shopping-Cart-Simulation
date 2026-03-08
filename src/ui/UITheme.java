/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author KEERTHI
 */
package ui;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import javax.swing.table.JTableHeader;
public class UITheme {

    // ============ COLORS ============
    public static final Color PRIMARY = new Color(52, 152, 219);
    public static final Color PRIMARY_DARK = new Color(41, 128, 185);
    public static final Color SECONDARY = new Color(44, 62, 80);
    public static final Color SUCCESS = new Color(46, 204, 113);
    public static final Color DANGER = new Color(231, 76, 60);
    public static final Color WARNING = new Color(241, 196, 15);

    public static final Color BG_PRIMARY = new Color(255, 255, 255);
    public static final Color BG_SECONDARY = new Color(247, 250, 252);
    public static final Color BG_TERTIARY = new Color(236, 240, 241);

    public static final Color TEXT_PRIMARY = new Color(44, 62, 80);
    public static final Color TEXT_SECONDARY = new Color(127, 140, 141);
    public static final Color BORDER = new Color(189, 195, 199);
    public static final Color DIVIDER = new Color(224, 224, 224);

    // ============ SETUP ============
    static {
        setupLookAndFeel();
    }

    public static void setupLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ============ BUTTON STYLE BASE ============
    private static void styleButton(JButton button) {

        button.setUI(new javax.swing.plaf.basic.BasicButtonUI());

        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setFocusable(false);

        button.setContentAreaFilled(true);
        button.setOpaque(true);
        button.setBorderPainted(true);

        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.setMargin(new Insets(10, 20, 10, 20));

        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0,0,0,40),1),
                BorderFactory.createEmptyBorder(10,20,10,20)
        ));

        button.setPreferredSize(new Dimension(140,45));
        button.setMinimumSize(new Dimension(140,45));
    }

    // ============ PRIMARY BUTTON ============
    public static JButton createPrimaryButton(String text) {

        JButton button = new JButton(text);
        styleButton(button);

        button.setBackground(PRIMARY);
        button.setForeground(Color.WHITE);

        button.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY_DARK);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY);
            }
        });

        return button;
    }

    // ============ SECONDARY BUTTON ============
    public static JButton createSecondaryButton(String text) {

        JButton button = new JButton(text);
        styleButton(button);

        button.setBackground(SECONDARY);
        button.setForeground(Color.WHITE);

        button.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(35,50,63));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(SECONDARY);
            }
        });

        return button;
    }

    // ============ SUCCESS BUTTON ============
    public static JButton createSuccessButton(String text) {

        JButton button = new JButton(text);
        styleButton(button);

        button.setBackground(SUCCESS);
        button.setForeground(Color.WHITE);

        button.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(39,174,96));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(SUCCESS);
            }
        });

        return button;
    }

    // ============ DANGER BUTTON ============
    public static JButton createDangerButton(String text) {

        JButton button = new JButton(text);
        styleButton(button);

        button.setBackground(DANGER);
        button.setForeground(Color.WHITE);

        button.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(192,57,43));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(DANGER);
            }
        });

        return button;
    }

    // ============ WARNING BUTTON ============
    public static JButton createWarningButton(String text) {

        JButton button = new JButton(text);
        styleButton(button);

        button.setBackground(WARNING);
        button.setForeground(new Color(44,62,80));

        button.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(205,167,13));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(WARNING);
            }
        });

        return button;
    }

    // ============ LABELS ============
    public static JLabel createTitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 28));
        label.setForeground(SECONDARY);
        return label;
    }

    public static JLabel createHeading(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        label.setForeground(SECONDARY);
        return label;
    }

    public static JLabel createSubheading(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(TEXT_PRIMARY);
        return label;
    }

    public static JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 12));
        label.setForeground(TEXT_PRIMARY);
        return label;
    }

    // ============ TEXT FIELD ============
    public static JTextField createTextField(int columns) {

        JTextField field = new JTextField(columns);

        field.setBackground(BG_PRIMARY);
        field.setForeground(TEXT_PRIMARY);
        field.setCaretColor(PRIMARY);

        field.setFont(new Font("Arial", Font.PLAIN, 12));

        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER,1),
                BorderFactory.createEmptyBorder(8,10,8,10)
        ));

        field.setPreferredSize(new Dimension(columns * 8,40));

        return field;
    }

    // ============ PASSWORD FIELD ============
    public static JPasswordField createPasswordField(int columns) {

        JPasswordField field = new JPasswordField(columns);

        field.setBackground(BG_PRIMARY);
        field.setForeground(TEXT_PRIMARY);
        field.setCaretColor(PRIMARY);

        field.setFont(new Font("Arial", Font.PLAIN, 12));

        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER,1),
                BorderFactory.createEmptyBorder(8,10,8,10)
        ));

        field.setPreferredSize(new Dimension(columns * 8,40));

        return field;
    }

    // ============ CUSTOM ROUNDED BORDER ============
    public static class RoundedBorder extends AbstractBorder {

        private int radius;
        private Color color;
        private int thickness;

        public RoundedBorder(int radius, Color color, int thickness) {
            this.radius = radius;
            this.color = color;
            this.thickness = thickness;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {

            Graphics2D g2d = (Graphics2D) g;

            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.setColor(color);

            g2d.setStroke(new BasicStroke(thickness));

            g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(8,8,8,8);
        }
    }
    
}