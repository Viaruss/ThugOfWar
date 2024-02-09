import java.awt.*;
import javax.swing.*;
import java.io.PrintWriter;

public class Interface {
    int team;
    PrintWriter writer;
    JFrame frame;
    JProgressBar bar;
    JButton pullButton, closeButton;



    Interface(int currentScore, PrintWriter pw) {
        this.writer = pw;

        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setSize(400, 75);
        frame.setLayout(new FlowLayout());
        frame.setResizable(false);


        Object[] teams = {"Team Left", "Team Right"};
        int[] teamsValues = {-1, 1};

        team = JOptionPane.showOptionDialog(frame,"Choose Your Team:", "Welcome to Tug Of War!", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, teams, teams[0]);
        team = teamsValues[team];
        frame.setTitle(team == -1 ? "Left Player" : "Right Player");

        bar = new JProgressBar(-20, 20);
        bar.setValue(currentScore);
        bar.setStringPainted(true);
        frame.add(bar);

        pullButton = new JButton("Pull!");
        pullButton.addActionListener(event -> writer.println(team));
        frame.add(pullButton);

        closeButton = new JButton("Disconnect");
        closeButton.addActionListener(close -> writer.println(444));
        frame.add(closeButton);

        frame.setVisible(true);
    }

    public int endGame(String message, String message2){
        Object[] postGameOptions = {"Disconnect"};
        return JOptionPane.showOptionDialog(frame, message, message2, JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, postGameOptions, postGameOptions[0]);
    }
    public void setScore(int score) {
        bar.setValue(score);
    }

    public void close(){
        frame.dispose();
    }

}
