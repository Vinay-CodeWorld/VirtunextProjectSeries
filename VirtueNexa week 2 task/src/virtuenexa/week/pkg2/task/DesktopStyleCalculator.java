/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package virtuenexa.week.pkg2.task;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Stack;
/**
 *
 * @author Admin
 */
public class DesktopStyleCalculator {
    private JTextField display;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DesktopStyleCalculator().createUI());
    }

    public void createUI() {
        JFrame frame = new JFrame("Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(350, 450);
        frame.setResizable(false);

        display = new JTextField();
        display.setFont(new Font("Segoe UI", Font.BOLD, 28));
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setEditable(false);
        display.setPreferredSize(new Dimension(350, 60));

        JPanel buttonPanel = new JPanel(new GridLayout(5, 4, 5, 5));
        String[] buttons = {
            "7", "8", "9", "/",
            "4", "5", "6", "*",
            "1", "2", "3", "-",
            "0", ".", "=", "+",
            "C", "(", ")", "←"
        };

        for (String text : buttons) {
            JButton btn = createButton(text);
            buttonPanel.add(btn);
        }

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(display);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(buttonPanel);

        frame.setContentPane(mainPanel);
        frame.setVisible(true);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 20));
        button.setFocusPainted(false);
        button.setBackground(Color.WHITE);
        button.setForeground(Color.BLACK);
        button.setPreferredSize(new Dimension(80, 60));

        button.addActionListener(e -> handleClick(text));
        return button;
    }

    private void handleClick(String text) {
        if (text.equals("C")) {
            display.setText("");
        } else if (text.equals("←")) {
            String current = display.getText();
            if (!current.isEmpty()) {
                display.setText(current.substring(0, current.length() - 1));
            }
        } else if (text.equals("=")) {
            evaluateExpression();
        } else {
            display.setText(display.getText() + text);
        }
    }

    private void evaluateExpression() {
        String expr = display.getText();
        try {
            if (expr.isEmpty()) {
                display.setText("Empty");
                return;
            }
            
            double result = evaluate(expr);
            display.setText(String.valueOf(result));
        } catch (Exception e) {
            display.setText("Error");
        }
    }
    
    // Custom expression evaluator for basic arithmetic
    private double evaluate(String expression) {
        // Remove all whitespace
        expression = expression.replaceAll("\\s+", "");
        
        Stack<Double> numbers = new Stack<>();
        Stack<Character> operators = new Stack<>();
        
        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            
            if (c == ' ') {
                continue;
            }
            
            if (c == '(') {
                operators.push(c);
            } else if (c == ')') {
                while (operators.peek() != '(') {
                    numbers.push(applyOperation(operators.pop(), numbers.pop(), numbers.pop()));
                }
                operators.pop();
            } else if (isOperator(c)) {
                while (!operators.empty() && hasPrecedence(c, operators.peek())) {
                    numbers.push(applyOperation(operators.pop(), numbers.pop(), numbers.pop()));
                }
                operators.push(c);
            } else {
                StringBuilder sb = new StringBuilder();
                while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    sb.append(expression.charAt(i++));
                }
                i--;
                numbers.push(Double.parseDouble(sb.toString()));
            }
        }
        
        while (!operators.empty()) {
            numbers.push(applyOperation(operators.pop(), numbers.pop(), numbers.pop()));
        }
        
        return numbers.pop();
    }
    
    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }
    
    private boolean hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')') {
            return false;
        }
        if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-')) {
            return false;
        }
        return true;
    }
    
    private double applyOperation(char op, double b, double a) {
        switch (op) {
            case '+': return a + b;
            case '-': return a - b;
            case '*': return a * b;
            case '/': 
                if (b == 0) throw new ArithmeticException("Division by zero");
                return a / b;
        }
        return 0;
    }
}