import javax.swing.*;

public class ChessFrame {
    public static void main(String[] args) {
        boolean continue_Game = true;
        while(continue_Game){//打开欢迎页面，跳转至WelcomeWindow
            WelcomeWindow welcomeWindow = new WelcomeWindow();
            welcomeWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            welcomeWindow.setVisible(true);
            welcomeWindow.setSize(850,600);
            continue_Game = false;
        }

    }
}
