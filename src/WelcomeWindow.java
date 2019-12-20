import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

public class WelcomeWindow extends JFrame {
    private JButton start_PVP , start_PVE,rankingList,load,multiPlay;
    private ImageIcon lalala = new ImageIcon("src/Image/tu.png");
    public WelcomeWindow(){
        super("CS102A Chinese Chess Project");//加载欢迎面板
        JPanel panel1 = new JPanel(null);
        JPanel panel2 = new JPanel(null);
        JPanel panel = new JPanel(new GridLayout(1,2));
        panel1 = new JPanel() {
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon image = new ImageIcon("src/Image/LAOYU.jpg");
                image.setImage(image.getImage().getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_AREA_AVERAGING));
                g.drawImage(image.getImage(), 0, 0, this);
            }
        };
        panel2 = new JPanel() {
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon image = new ImageIcon("src/Image/LAOYU1.jpg");
                image.setImage(image.getImage().getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_AREA_AVERAGING));
                g.drawImage(image.getImage(), 0, 0, this);
            }
        };
        panel2.setLayout(null);
        panel1.setLayout(null);
        start_PVP = new JButton("双人游戏",lalala);start_PVP = setBackGround(start_PVP);start_PVP.setBounds(150,50,180,70);start_PVP.addActionListener(new start_PvPListener());panel2.add(start_PVP);
        start_PVE = new JButton("人机对战",lalala);start_PVE = setBackGround(start_PVE);start_PVE.setBounds(150,140,180,70);start_PVE.addActionListener(new start_PvEListener());panel2.add(start_PVE);
        multiPlay = new JButton("局域网联机对战",lalala);multiPlay = setBackGround(multiPlay);multiPlay.setBounds(150,230,180,70);multiPlay.addActionListener(new multiPlayListener());panel2.add(multiPlay);
        rankingList = new JButton("排行榜",lalala);rankingList = setBackGround(rankingList);rankingList.setBounds(150,320,180,70);rankingList.addActionListener(new rankingListListener());panel2.add(rankingList);
        load = new JButton("加载棋局",lalala);load= setBackGround(load);load.setBounds(150,410,180,70);load.addActionListener(new loadListener());panel2.add(load);
        Music temp = new Music();JButton setMusic = temp.Music();setMusic.setBounds(0,450,100,100);panel1.add(setMusic);


        panel2.setSize(300,600);

        panel.add(panel1);
        panel.add(panel2);
        add(panel);
    }
    private class start_PvPListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ChessBoard chessBoard = null;//打开双人对战页面
            try {
                chessBoard = new ChessBoard(0);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            chessBoard.setSize(750,700);
            chessBoard.setVisible(true);
        }
    }
    private class start_PvEListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ChessBoard chessBoard = null;//打开人机对战页面
            try {
                chessBoard = new ChessBoard(7);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            chessBoard.setSize(750,700);
            chessBoard.setVisible(true);
        }
    }
    private class rankingListListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFrame frame = new JFrame();
            frame.setBounds(100, 100, 300, 500);
            frame.getContentPane().setLayout(null);
            JScrollPane scrollpane=new JScrollPane();
            scrollpane.setBounds(0,0,300,500);
            File file=new File("src/ranking/ranklist.txt");
            String line = "";
            ArrayList str = new ArrayList();
            String str1;
            StringBuffer str3=new StringBuffer();
            int count = 0;
            InputStreamReader reader = null;
            try { reader = new InputStreamReader(
                        new FileInputStream(file));
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
            BufferedReader scanner = new BufferedReader(reader);
            while(line!=null){
                try {
                    line = scanner.readLine();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                str1=String.format("%s",line);
                if(line!=null){
                    str.add(str1);
                    count++;
                }
            }
            String temp1;
            String temp2;
            String [][] re=new String[count][2];
            for(int i=0;i<count;i++){
                String []temp=str.get(i).toString().split(" ");
                re[i][0]=temp[0];
                re[i][1]=temp[1];
            }
           for(int i=0;i<count;i++){
               for(int j=0;j<count-1;j++){
                   if(Integer.parseInt(re[j][1])<Integer.parseInt(re[j+1][1])){
                       temp1=re[j][0];
                       temp2=re[j][1];
                       re[j][0]=re[j+1][0];
                       re[j][1]=re[j+1][1];
                       re[j+1][0]=temp1;
                       re[j+1][1]=temp2;
                   }
               }
           }
           for(int i=0;i<count;i++){
               String str2=String.format("%s\n","排名"+(i+1)+" "+"ID："+re[i][0]+" 分数："+re[i][1]);
               str3.append(str2);
           }
            JTextArea text1= new JTextArea(String.valueOf(str3));
            Font f=new Font("宋体",Font.BOLD,16);
            text1.setFont(f);
            scrollpane.setViewportView(text1);
            frame.add(scrollpane);
            frame.setVisible(true);
        }
    }
    private class loadListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ChessBoard chessBoard = null;
            try {
                chessBoard = new ChessBoard(2);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            if(!chessBoard.stop){
                chessBoard.setSize(750,700);
                chessBoard.setVisible(true);
            }
        }
    }
    private class multiPlayListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ChessBoard multyplay = null;
            try {
                multyplay = new ChessBoard(4);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            multyplay.setSize(750,700);
            multyplay.setVisible(true);
        }
    }
    private JButton setBackGround(JButton enter){
        Font f=new Font("宋体",Font.BOLD,16);
        enter.setBounds(900, 380, 400, 350);
        enter.setHorizontalTextPosition(SwingConstants.CENTER);
        enter.setFont(f);
        enter.setOpaque(false);//设置控件是否透明，true为不透明，false为透明
        enter.setContentAreaFilled(false);//设置图片填满按钮所在的区域
        enter.setMargin(new Insets(0, 0, 0, 0));//设置按钮边框和标签文字之间的距离
        enter.setFocusPainted(false);//设置这个按钮是不是获得焦点
        enter.setBorderPainted(false);//设置是否绘制边框
        enter.setBorder(null);//设置边框
        return enter;
    }
}

