package app;
import app.dashboard.EVotingApp;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new EVotingApp();
            }
        });
    }
}
