package main;

import api.AnalysisException;
import api.AnalysisService;
import api.DataSet;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ServiceLoader;

public class Main extends JFrame {

    private JPanel contentPane;
    private JTextField textField;
    private JTable table;
    private DefaultListModel<String> defaultListModel = new DefaultListModel<>();
    private ArrayList<AnalysisService> services = new ArrayList<>();
    private String[] names;
    private ArrayList<String[]> values = new ArrayList<>();
    private JTextField txtParameters;
    private JTextField textField_2;
    private DataSet result;
    private DataSet data;
    private JTable table_1;
    private JTextField textField_1;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Main frame = new Main();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public Main() {

        ServiceLoader<AnalysisService> loader = ServiceLoader.load(AnalysisService.class);
        for (AnalysisService service : loader) {
            services.add(service);
            defaultListModel.addElement(service.getName());
        }


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 900, 478);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        textField = new JTextField("C:\\Users\\otlal\\Desktop\\test.csv");
        textField.setFont(new Font("Tahoma", Font.PLAIN, 12));
        textField.setBounds(10, 10, 343, 19);
        contentPane.add(textField);
        textField.setColumns(10);

        txtParameters = new JTextField();
        txtParameters.setFont(new Font("Tahoma", Font.PLAIN, 12));
        txtParameters.setEditable(false);
        txtParameters.setText("Parameters:");
        txtParameters.setBounds(390, 270, 104, 19);
        contentPane.add(txtParameters);
        txtParameters.setColumns(10);

        textField_2 = new JTextField();
        textField_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
        textField_2.setBounds(390, 299, 104, 19);
        contentPane.add(textField_2);
        textField_2.setColumns(10);

        textField_1 = new JTextField();
        textField_1.setEditable(false);
        textField_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
        textField_1.setBounds(565, 11, 311, 19);
        contentPane.add(textField_1);
        textField_1.setColumns(10);

        JButton btnNewButton = new JButton("Open csv");
        btnNewButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                File file = new File(textField.getText());

                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line = reader.readLine();
                    names = line.split(";");
                    int columns = names.length;

                    while ((line = reader.readLine()) != null) {
                        String[] rowValues = line.replace(',', '.').split(";");
                        if (columns != rowValues.length)
                            throw new IllegalArgumentException("All rows should be the same size.");
                        values.add(rowValues);
                    }
                    table.setModel(new MyTableModel(values.toArray(new String[][]{}), names));
                    contentPane.repaint();
                    data = new DataSet();
                    data.setData(values.toArray(new String[][]{}));
                    data.setHeader(names);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }
        });
        btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
        btnNewButton.setBounds(390, 9, 104, 21);
        contentPane.add(btnNewButton);

        JList list = new JList(defaultListModel);
        list.setBounds(363, 40, 161, 220);
        contentPane.add(list);

        JButton btnS = new JButton("Submit");
        btnS.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (list.getSelectedIndex() == -1) {
                    JOptionPane.showMessageDialog(contentPane, "Choose operation.");
                } else {
                    try {
                        if (textField_2.getText().isEmpty()) {
                            services.get(list.getSelectedIndex()).setOptions(new String[]{"0"});

                        } else {
                            services.get(list.getSelectedIndex()).setOptions(textField_2.getText().split(" "));
                        }
                        services.get(list.getSelectedIndex()).submit(data);
                        JOptionPane.showMessageDialog(contentPane, "Submitted. :)");
                    } catch (AnalysisException ex) {
                        JOptionPane.showMessageDialog(contentPane, ex.getMessage());
                    }
                }
            }
        });
        btnS.setFont(new Font("Tahoma", Font.PLAIN, 12));
        btnS.setBounds(390, 328, 104, 21);
        contentPane.add(btnS);

        JCheckBox chckbxNewCheckBox = new JCheckBox("Clear memory");
        chckbxNewCheckBox.setFont(new Font("Tahoma", Font.PLAIN, 12));
        chckbxNewCheckBox.setBounds(390, 367, 104, 21);
        contentPane.add(chckbxNewCheckBox);

        JButton btnResult = new JButton("Result");
        btnResult.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (list.getSelectedIndex() == -1) {
                    JOptionPane.showMessageDialog(contentPane, "Choose operation.");
                } else {
                    try {
                        result = services.get(list.getSelectedIndex()).retrieve(chckbxNewCheckBox.isSelected());
                        table_1.setModel(new MyTableModel(result.getData(), result.getHeader()));
                        textField_1.setText(services.get(list.getSelectedIndex()).getName() + " results:");
                    } catch (AnalysisException ex) {
                        JOptionPane.showMessageDialog(contentPane, ex.getMessage());
                    }
                }
            }
        });
        btnResult.setFont(new Font("Tahoma", Font.PLAIN, 12));
        btnResult.setBounds(390, 410, 104, 21);
        contentPane.add(btnResult);

        JPanel panel = new JPanel();
        panel.setBounds(10, 39, 343, 392);
        contentPane.add(panel);
        panel.setLayout(new BorderLayout(0, 0));

        table = new JTable();
        panel.add(table, BorderLayout.CENTER);
        panel.add(table.getTableHeader(), BorderLayout.NORTH);

        JPanel panel_1 = new JPanel();
        panel_1.setBounds(565, 40, 311, 388);
        contentPane.add(panel_1);
        panel_1.setLayout(new BorderLayout(0, 0));

        table_1 = new JTable();
        panel_1.add(table_1, BorderLayout.CENTER);
        panel_1.add(table_1.getTableHeader(), BorderLayout.NORTH);
    }
}
