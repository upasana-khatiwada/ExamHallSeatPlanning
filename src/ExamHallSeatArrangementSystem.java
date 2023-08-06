import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ExamHallSeatArrangementSystem {
    private JFrame mainFrame;
    private ExamHall examHall;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ExamHallSeatArrangementSystem().createAndShowGUI());
    }

    private void createAndShowGUI() {
        mainFrame = new JFrame("Exam Hall Seat Arrangement");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //mainFrame.setLocationRelativeTo(null);
        mainFrame.setResizable(false);
        mainFrame.setSize(600, 600);

        JPanel topPanel = createTopPanel();
        JTextArea outputArea = createOutputArea();

        mainFrame.add(topPanel, BorderLayout.NORTH);
        mainFrame.add(new JScrollPane(outputArea), BorderLayout.CENTER);

        mainFrame.setVisible(true);
        mainFrame.setLocationRelativeTo(null);
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));

        JLabel rowsLabel = new JLabel("Number of Rows:");
        JLabel columnsLabel = new JLabel("Number of Columns:");
        JTextField rowsField = new JTextField(10);
        JTextField columnsField = new JTextField(10);
        JButton createHallButton = new JButton("Create Exam Hall");
        JButton assignSeatButton = new JButton("Assign Seat");
        JButton displayButton = new JButton("Display Seating Arrangement");

        // Create ExamHall button listener
        createHallButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int rows = getInput(rowsField.getText());
                int columns = getInput(columnsField.getText());
                if (rows > 0 && columns > 0) {
                    examHall = new ExamHall(rows, columns);
                    appendToOutputArea("Exam hall created with " + rows + " rows and " + columns + " columns.\n");
                    assignSeatButton.setEnabled(true);
                    displayButton.setEnabled(true);
                } else {
                    appendToOutputArea("Please enter valid positive integers for rows and columns.\n");
                }
            }
        });

        // Assign Seat button listener
        assignSeatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (examHall == null) {
                    appendToOutputArea("Create an exam hall first.\n");
                    return;
                }
                new AssignSeatDialog(examHall).setVisible(true);
            }
        });
        assignSeatButton.setEnabled(false);

        // Display Seating Arrangement button listener
        displayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (examHall == null) {
                    appendToOutputArea("Create an exam hall first.\n");
                    return;
                }
                new DisplaySeatingDialog(examHall).setVisible(true);
            }
        });
        displayButton.setEnabled(false);

        // Add components to panel
        panel.add(rowsLabel);
        panel.add(rowsField);
        panel.add(columnsLabel);
        panel.add(columnsField);
        panel.add(createHallButton);
        panel.add(assignSeatButton);
        panel.add(displayButton);

        return panel;
    }

    private JTextArea createOutputArea() {
        JTextArea outputArea = new JTextArea(20, 40);
        outputArea.setEditable(false);
        return outputArea;
    }

    private int getInput(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(mainFrame, "Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            return -1; // Return a sentinel value indicating an error
        }
    }

    private void appendToOutputArea(String message) {
        JTextArea outputArea = (JTextArea) ((JScrollPane) mainFrame.getContentPane().getComponent(1)).getViewport().getView();
        outputArea.append(message);
    }

    // Define ExamHall class here (same as in the previous response)

    private class AssignSeatDialog extends JDialog {
        private ExamHall examHall;
        private JTextField studentNameField;
        private JComboBox<String> seatComboBox;
        private JButton assignButton;

        public AssignSeatDialog(ExamHall examHall) {
            this.examHall = examHall;

            setTitle("Assign Seat");
            setSize(400, 300);
            setLayout(new BorderLayout());

            JPanel topPanel = new JPanel(new FlowLayout());
            studentNameField = new JTextField(15);
            seatComboBox = new JComboBox<>();
            assignButton = new JButton("Assign Seat");

            topPanel.add(new JLabel("Enter student name: "));
            topPanel.add(studentNameField);

            JPanel middlePanel = new JPanel(new FlowLayout());
            middlePanel.add(new JLabel("Select seat: "));
            middlePanel.add(seatComboBox);

            add(topPanel, BorderLayout.NORTH);
            add(middlePanel, BorderLayout.CENTER);
            add(assignButton, BorderLayout.SOUTH);

            assignButton.addActionListener(new AssignButtonListener());

            setLocationRelativeTo(null); // Center the dialog

            populateSeatComboBox();
        }

        private void populateSeatComboBox() {
            seatComboBox.removeAllItems();
            for (int i = 0; i < examHall.getRows(); i++) {
                for (int j = 0; j < examHall.getColumns(); j++) {
                    if (!examHall.isSeatOccupied(i, j)) {
                        seatComboBox.addItem("Row " + i + ", Column " + j);
                    }
                }
            }
        }

        private class AssignButtonListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                String studentName = studentNameField.getText();

                if (studentName.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid student name.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int selectedIndex = seatComboBox.getSelectedIndex();
                if (selectedIndex == -1) {
                    JOptionPane.showMessageDialog(null, "No available seats to assign.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String seatInfo = seatComboBox.getItemAt(selectedIndex);
                String[] seatParts = seatInfo.split(", ");
                int row = Integer.parseInt(seatParts[0].substring(4));
                int column = Integer.parseInt(seatParts[1].substring(7));

                examHall.assignSeat(row, column, studentName);
                appendToOutputArea("Seat assigned to " + studentName + " at " + seatInfo + ".\n");
                populateSeatComboBox();
            }
        }
    }

    public class DisplaySeatingDialog extends JDialog {
        private ExamHall examHall;
        private JTextArea seatingArea;

        public DisplaySeatingDialog(ExamHall examHall) {
            this.examHall = examHall;

            setTitle("Seating Arrangement");
            setSize(400, 300);
            setLayout(new BorderLayout());

            seatingArea = new JTextArea(10, 20);
            seatingArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(seatingArea);

            add(scrollPane, BorderLayout.CENTER);
            refreshSeatingArea();

            setLocationRelativeTo(null); // Center the dialog
        }

        private void refreshSeatingArea() {
            seatingArea.setText("Seating Arrangement:\n");
            for (int i = 0; i < examHall.getRows(); i++) {
                for (int j = 0; j < examHall.getColumns(); j++) {
                    if (examHall.isSeatOccupied(i, j)) {
                        seatingArea.append(examHall.getStudentNameAt(i, j) + "\t");
                    } else {
                        seatingArea.append("Empty\t");
                    }
                }
                seatingArea.append("\n");
            }

        }
    }
}