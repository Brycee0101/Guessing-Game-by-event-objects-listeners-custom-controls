//@author Bryce Stephen Halnin

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.EventListener;

// Custom Event Object
class GuessEvent extends EventObject {
    private int guess;

    public GuessEvent(Object source, int guess) {
        super(source);
        this.guess = guess;
    }

    public int getGuess() {
        return guess;
    }
}

// Custom Listener Interface
interface GuessListener extends EventListener {
    void guessSubmitted(GuessEvent event);
}

// Custom JPanel for Input Form
class InputPanel extends JPanel {
    private JTextField guessTextField;
    private GuessListener guessListener;

    public InputPanel() {
        setLayout(new GridBagLayout());

        JLabel enterLabel = new JLabel("Enter a Number [1-100]");
        enterLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        add(enterLabel, createConstraints(0, 0, 1, 1));

        guessTextField = new JTextField(3);
        guessTextField.setFont(new Font("Arial", Font.PLAIN, 72));
        add(guessTextField, createConstraints(0, 1, 1, 1));

        guessTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String guessText = guessTextField.getText();
                int guess = Integer.parseInt(guessText);

                if (guessListener != null) {
                    guessListener.guessSubmitted(new GuessEvent(this, guess));
                }

                guessTextField.setText("");
            }
        });
    }

    public void setGuessListener(GuessListener listener) {
        guessListener = listener;
    }

    private GridBagConstraints createConstraints(int gridx, int gridy, int gridwidth, int gridheight) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = gridx;
        constraints.gridy = gridy;
        constraints.gridwidth = gridwidth;
        constraints.gridheight = gridheight;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5, 5, 5, 5);
        return constraints;
    }
}

// Custom JPanel for Feedback
class FeedbackPanel extends JPanel {
    private JLabel feedbackLabel;
    private StringBuilder feedbackText;

    public FeedbackPanel() {
        setLayout(new BorderLayout());

        feedbackText = new StringBuilder();

        feedbackLabel = new JLabel();
        feedbackLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(feedbackLabel);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void displayMessage(String message) {
        feedbackText.append(message).append("<br>");
        feedbackLabel.setText("<html>" + feedbackText.toString() + "</html>");
    }
}

// Custom JFrame for Main Frame
class MainFrame extends JFrame implements GuessListener {
    private FeedbackPanel feedbackPanel;

    public MainFrame() {
        setTitle("Higher-Lower Game");
        setSize(720, 555);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Higher-Lower Game");
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 52));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new GridLayout(1, 2));

        InputPanel inputPanel = new InputPanel();
        inputPanel.setBorder(BorderFactory.createTitledBorder("InputForm"));
        inputPanel.setGuessListener(this);
        mainPanel.add(inputPanel);

        feedbackPanel = new FeedbackPanel();
        feedbackPanel.setBorder(BorderFactory.createTitledBorder("Feedback"));
        mainPanel.add(feedbackPanel);

        add(mainPanel, BorderLayout.CENTER);
    }

    public void guessSubmitted(GuessEvent event) {
        int guess = event.getGuess();
        int target = (int) (Math.random() * 10) + 1;
        if (guess > target) {
            feedbackPanel.displayMessage("Your guess is higher than the target");
        } else if (guess < target) {
            feedbackPanel.displayMessage("Your guess is lower than the target");
        } else {
            feedbackPanel.displayMessage("Congratulations, your guess is correct!");
        }
    }
}

// Main class to run the program
public class GuessingGame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                MainFrame frame = new MainFrame();
                frame.setVisible(true);
            }
        });
    }
}
