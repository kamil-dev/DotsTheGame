package main.java.view;

import main.java.controller.NavigateMouseListener;
import main.java.model.Settings;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SettingsFrame extends JFrame {
    private int width = 500;
    private int height = 525;
    private Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.MAGENTA, Color.WHITE, Color.BLACK, Color.CYAN,
                new Color(102, 153, 0), new Color(204, 0, 204), new Color(0, 153, 204)};
    private JTextField boardSizeTextField, playerOneNameTextField, playerTwoNameTextField;
    private JSpinner hoursSpinner, minutesSpinner, secondsSpinner;
    private JComboBox<Color> playerOneComboBox, playerTwoComboBox;
    private Color backgroundColor, foregroundColor;
    private Font fontLarge, fontNormal;

    public SettingsFrame(){
        setSize(width,height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setIconImage(new ImageIcon("src/main/resources/1320183166943884936_128.png").getImage());
        setVisible(true);
        setResizable(false);
        backgroundColor = Settings.gameSettings.getGlobalTheme().getBackgroundColor();
        foregroundColor = Settings.gameSettings.getGlobalTheme().getForegroundColor();
        fontLarge = Settings.gameSettings.getGlobalTheme().getFontLarge();
        fontNormal = Settings.gameSettings.getGlobalTheme().getFontNormal();
        setTitle("SETTINGS");
        createPanel();
    }

    private void createPanel(){
        JPanel settingsPanel = new JPanel();
        add(settingsPanel, BorderLayout.CENTER);
        settingsPanel.setLayout(null);

        settingsPanel.setBackground(backgroundColor);

        try {
            BufferedImage gameNameIcon = ImageIO.read(new File("src\\main\\resources\\picturetopeople.org-d3b02807efef49a5e8992c90051993385db6615ff94978b17a.png"));
            JLabel gameNameLabel = new JLabel(new ImageIcon(gameNameIcon.getScaledInstance(140,60,Image.SCALE_SMOOTH)));
            gameNameLabel.setBounds((width - 140)/2, 20, 140,60);
            settingsPanel.add(gameNameLabel);
        } catch (IOException e) {
            System.err.println("The game name icon could not be read");
        }


        JLabel playerOneLabel = new JLabel("Player 1");
        playerOneLabel.setBounds(50,120, 100,20);
        playerOneLabel.setFont(fontLarge);
        playerOneLabel.setForeground(foregroundColor);

        JLabel playerTwoLabel = new JLabel("Player 2");
        playerTwoLabel.setBounds( width - 150,120, 100,20);
        playerTwoLabel.setFont(fontLarge);
        playerTwoLabel.setForeground(foregroundColor);
        playerTwoLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        JLabel nameLabel = new JLabel("name");
        nameLabel.setForeground(foregroundColor);
        nameLabel.setFont(fontNormal);
        nameLabel.setBounds(width - 300, 160, 100, 20);
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);

        playerOneNameTextField = new JTextField();
        playerOneNameTextField.setBounds(50,160,100,20);

        playerTwoNameTextField = new JTextField();
        playerTwoNameTextField.setBounds(width - 150,160,100,20);

        JLabel colorLabel = new JLabel("color");
        colorLabel.setForeground(foregroundColor);
        colorLabel.setFont(fontNormal);
        colorLabel.setBounds(width - 300, 200, 100, 20);
        colorLabel.setHorizontalAlignment(SwingConstants.CENTER);

        playerOneComboBox = new JComboBox<>(colors);
        playerOneComboBox.setBounds(50,200,100,20);
        playerOneComboBox.setSelectedIndex(0);

        playerTwoComboBox = new JComboBox<>(colors);
        playerTwoComboBox.setBounds(width - 150,200,100,20);
        playerTwoComboBox.setSelectedIndex(1);

        ComboBoxRenderer renderer = new ComboBoxRenderer();
        renderer.setPreferredSize(new Dimension(50,15));
        playerOneComboBox.setRenderer(renderer);
        playerTwoComboBox.setRenderer(renderer);

        JLabel boardSizeLabel = new JLabel("Board Size:");
        boardSizeLabel.setForeground(foregroundColor);
        boardSizeLabel.setFont(fontNormal);
        boardSizeLabel.setBounds(50,height - 140,120,20);

        boardSizeTextField = new JTextField();
        boardSizeTextField.setBounds(180,height - 140,50,20);
        boardSizeTextField.setToolTipText("Type in an integer between 10 and 40");

        JLabel timerLabel = new JLabel("Timer (h.m.s):");
        timerLabel.setForeground(foregroundColor);
        timerLabel.setFont(fontNormal);
        timerLabel.setBounds(50,height - 100,120,20);

//        timerTextField = new JTextField();
//        timerTextField.setBounds(160,height - 100,50,20);
//        timerTextField.setToolTipText("T");

        hoursSpinner = new JSpinner(new SpinnerNumberModel(0,0,10,1));
        hoursSpinner.setToolTipText("Hours");
        hoursSpinner.setBounds(180,height-100, 35,20);
        minutesSpinner = new JSpinner(new SpinnerNumberModel(30,0,59,1));
        minutesSpinner.setToolTipText("Minutes");
        minutesSpinner.setBounds(225,height-100, 35,20);
        secondsSpinner = new JSpinner(new SpinnerNumberModel(0,0,59,1));
        secondsSpinner.setToolTipText("Seconds");
        secondsSpinner.setBounds(270,height-100, 35,20);

        JLabel acceptLabel = new JLabel("Accept");
        acceptLabel.setForeground(foregroundColor);
        acceptLabel.setFont(fontLarge);
        acceptLabel.addMouseListener(new NavigateMouseListener(acceptLabel, this));
        acceptLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        acceptLabel.setBounds(width - 150,height - 140, 100,25);

        JLabel cancelLabel = new JLabel("Cancel");
        cancelLabel.setFont(fontLarge);
        cancelLabel.setForeground(foregroundColor);
        cancelLabel.addMouseListener(new NavigateMouseListener(cancelLabel, this));
        cancelLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        cancelLabel.setBounds(width - 150,height - 100, 100,25);


        settingsPanel.add(playerOneLabel);
        settingsPanel.add(playerTwoLabel);
        settingsPanel.add(playerOneNameTextField);
        settingsPanel.add(nameLabel);
        settingsPanel.add(playerTwoNameTextField);
        settingsPanel.add(colorLabel);
        settingsPanel.add(playerOneComboBox);
        settingsPanel.add(playerTwoComboBox);
        settingsPanel.add(boardSizeLabel);
        settingsPanel.add(boardSizeTextField);
        settingsPanel.add(timerLabel);
        settingsPanel.add(hoursSpinner);
        settingsPanel.add(minutesSpinner);
        settingsPanel.add(secondsSpinner);
        settingsPanel.add(acceptLabel);
        settingsPanel.add(cancelLabel);

    }

    public Color getP1Color(){
        return (Color)playerOneComboBox.getSelectedItem();
    }

    public Color getP2Color(){
        return (Color)playerTwoComboBox.getSelectedItem();
    }

    public String getP1Name(){
        return playerOneNameTextField.getText();
    }

    public String getP2Name(){
        return playerTwoNameTextField.getText();
    }

    public Integer getBoardSize() {
        try {
            return Integer.parseInt(boardSizeTextField.getText());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public Integer getTimer() {
        try {
            int timer = (int)hoursSpinner.getValue() * 3600 + (int)minutesSpinner.getValue() * 60 + (int)secondsSpinner.getValue();
            return timer;
        } catch (NumberFormatException e) {
            return null;
        }
    }



}
