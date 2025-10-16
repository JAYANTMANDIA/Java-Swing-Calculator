package calculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CalculatorApp extends JFrame implements ActionListener, KeyListener {

    private JTextField display;
    private String operator = "";
    private double num1 = 0;
    private double memory = 0;

    private boolean darkTheme = true;

    public CalculatorApp() {
        setTitle("Calculator App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new GridBagLayout());
        addKeyListener(this);
        setFocusable(true);

        JPanel calculatorPanel = new JPanel();
        calculatorPanel.setLayout(new BorderLayout(10, 10));
        calculatorPanel.setBackground(new Color(40, 44, 52));
        calculatorPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        calculatorPanel.setPreferredSize(new Dimension(400, 600));

        // Display
        display = new JTextField();
        display.setFont(new Font("Segoe UI", Font.BOLD, 32));
        display.setEditable(false);
        display.setBackground(new Color(60, 63, 65));
        display.setForeground(Color.WHITE);
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        calculatorPanel.add(display, BorderLayout.NORTH);

        // Buttons
        JPanel buttonPanel = new JPanel(new GridLayout(6, 4, 10, 10));
        buttonPanel.setBackground(new Color(40, 44, 52));

        String[] buttons = {
            "MC", "MR", "M+", "M-",
            "C", "←", "%", "/",
            "7", "8", "9", "*",
            "4", "5", "6", "-",
            "1", "2", "3", "+",
            "+/-", "0", ".", "="
        };

        for (String text : buttons) {
            JButton button = new JButton(text);
            button.setFont(new Font("Segoe UI", Font.BOLD, 24));
            button.setFocusPainted(false);
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            button.setForeground(Color.WHITE);
            button.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            button.setOpaque(true);
            button.setBackground(getButtonColor(text));

            // Hover effect
            button.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    button.setBackground(button.getBackground().darker());
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    button.setBackground(getButtonColor(text));
                }
            });

            button.addActionListener(this);
            buttonPanel.add(button);
        }

        calculatorPanel.add(buttonPanel, BorderLayout.CENTER);

        // Theme toggle
        JButton themeBtn = new JButton("Toggle Theme");
        themeBtn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        themeBtn.addActionListener(e -> toggleTheme());
        calculatorPanel.add(themeBtn, BorderLayout.SOUTH);

        add(calculatorPanel);
        setVisible(true);
    }

    private Color getButtonColor(String text) {
        if (text.matches("[0-9]")) return new Color(97, 218, 251);
        if (text.equals("=")) return new Color(40, 167, 69);
        if (text.equals("C") || text.equals("←")) return new Color(220, 53, 69);
        if (text.equals("%") || text.equals("+/-")) return new Color(255, 193, 7);
        if (text.matches("MC|MR|M\\+|M-")) return new Color(123, 31, 162);
        return new Color(75, 110, 175); // operators
    }

    private void toggleTheme() {
        darkTheme = !darkTheme;
        Color bg = darkTheme ? new Color(40,44,52) : Color.WHITE;
        Color fg = darkTheme ? Color.WHITE : Color.BLACK;
        display.setBackground(bg);
        display.setForeground(fg);
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        switch(command) {
            case "C":
                display.setText("");
                operator = "";
                num1 = 0;
                break;
            case "←":
                String text = display.getText();
                if (!text.isEmpty()) display.setText(text.substring(0, text.length()-1));
                break;
            case "+": case "-": case "*": case "/": case "%":
                try {
                    num1 = Double.parseDouble(display.getText());
                    operator = command;
                    display.setText(display.getText() + " " + operator + " ");
                } catch(Exception ex) { display.setText("Error"); }
                break;
            case "=":
                try {
                    String[] parts = display.getText().split(" ");
                    if(parts.length < 3) return;
                    double n1 = Double.parseDouble(parts[0]);
                    double n2 = Double.parseDouble(parts[2]);
                    String op = parts[1];
                    double res=0;
                    switch(op) {
                        case "+": res=n1+n2; break;
                        case "-": res=n1-n2; break;
                        case "*": res=n1*n2; break;
                        case "/": res=n2!=0?n1/n2:Double.NaN; break;
                        case "%": res=n1%n2; break;
                    }
                    display.setText(String.valueOf(res));
                } catch(Exception ex){ display.setText("Error"); }
                break;
            case "+/-":
                try {
                    double val = Double.parseDouble(display.getText());
                    display.setText(String.valueOf(-val));
                } catch(Exception ex){ display.setText("Error"); }
                break;
            case "MC": memory=0; break;
            case "MR": display.setText(String.valueOf(memory)); break;
            case "M+": memory += Double.parseDouble(display.getText()); break;
            case "M-": memory -= Double.parseDouble(display.getText()); break;
            default:
                display.setText(display.getText()+command);
                break;
        }
    }

    // Keyboard support
    public void keyTyped(KeyEvent e) { actionPerformed(new ActionEvent(e.getSource(), ActionEvent.ACTION_PERFORMED, String.valueOf(e.getKeyChar()))); }
    public void keyPressed(KeyEvent e) {}
    public void keyReleased(KeyEvent e) {}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CalculatorApp::new);
    }
}
