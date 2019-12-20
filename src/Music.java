import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;

public class Music {
    private JButton button=new JButton();
    private ImageIcon laBa=new ImageIcon("src/Image/laba.jpg");
    private ImageIcon laBa2=new ImageIcon("src/Image/laba2.jpg");
    private Clip clip;
    public JButton Music(){
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        JLabel label = new JLabel(laBa);
        JLabel label1=new JLabel(laBa2);
        laBa2.setImage(laBa2.getImage().getScaledInstance(50,50,0));
        laBa.setImage(laBa.getImage().getScaledInstance(50,50, 0));
        button.add(label);
        button.setText("b");
        try{
            File file=new File("src/music/DAISHI DANCE - Take me hand.wav");
            AudioInputStream audioStream=AudioSystem.getAudioInputStream(file);
            clip=AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        }catch(Exception e){e.printStackTrace();}
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(button.getText().equals("b")){
                    clip.stop();
                    button.setText("a");
                    button.add(label);
                }
                else{
                    clip.start();
                    button.setText("b");
                    button.add(label1);
                }
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBorderPainted(true);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBorderPainted(false);
            }

        });
        return button;
    }
}
