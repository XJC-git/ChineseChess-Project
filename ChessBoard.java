
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Random;

public class ChessBoard extends JFrame {
    private int clickCount = 1;
    public boolean nowPlaying = true;
    private int[][] chessBoard = new int[10][9];
    private int type;
    private JButton[][] buttonList = new JButton[10][9];
    private JButton regret,saveMove,saveChessBoard;
    private JPanel panel = new JPanel(new BorderLayout());
    private JPanel panel1 = new JPanel(new GridLayout(10,9,20,20));
    private JPanel panel2 = new JPanel(new GridLayout(4,1));
    private JLayeredPane chessPane = new JLayeredPane();
    private ArrayList<Integer> moveseq = new ArrayList();
    private Chess[][] chessBoardNow = new Chess[10][9];
    private int selectedX , selectedY,nowSelectedX,nowSelectedY;
    private int step;
    private int[][] initial = initialChessBoard();
    private JButton displayNowPlaying = new JButton("现在行棋方：红方");
    private JPanel panel3 = new JPanel(new GridLayout(7,1));
    public boolean stop = false;
    private InetAddress myID;//自己id地址
    private InetAddress youID;//目标ID地址
    private int sendport;//发送端口
    private int receiveport = 2333;//接收端口
    private JButton start,connect,end;
    private DatagramSocket sock =  null;
    private int createCount = 0;
    private ImageIcon lalala = new ImageIcon("src/Image/tu1.png");
    private JLabel tupian ;
    private String[] predict;
    private File[] predictFile;
    private File selectedPVEModel;
    private boolean predictedUSERMoving = true;
    private int PVEReadingCount = 1;
    public boolean BLACK = false;
    public ChessBoard(int type) throws IOException {
        super("Playing");
        this.type = type;
        tupian = new JLabel(lalala);
        panel1 = new JPanel() {
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon image = new ImageIcon("src/Image/chessBoard1.jpg");
                image.setImage(image.getImage().getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_AREA_AVERAGING));
                g.drawImage(image.getImage(), 0, 0, this);
            }
        };
        panel1.setLayout(new GridLayout(10, 9, 15, 15));
        if (type == 0 || type == 1) {
            initialize();
            File tempFile = new File("src/save/moveseq.moveseq");
            if(tempFile.exists())
            {
                tempFile.delete();
            }
            tempFile.createNewFile();
        }
        else if (type == 3||type>=10) {
            initial = regretLoad();
            initialize();
            if(step%2==1){nowPlaying = false;}
            refresh();
        }
        else if (type == 4) {
            initialize();
            multiPlay();
        }
        else if(type==7){
            initialize();
            predict = getStartFromFile();
        }
        else {
            load();
            if (!stop) {
                JOptionPane.showMessageDialog(null, String.format("加载成功"), "提示", JOptionPane.INFORMATION_MESSAGE);
            }
            fresh();refresh();
        }
        if (type!=4) {
            regret = new JButton("悔棋",lalala);regret = setBackGround(regret);
            regret.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        ChessBoard temp1 = new ChessBoard(type+10);
                        temp1.setSize(750, 700);
                        temp1.setVisible(true);

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                    dispose();
                }
            });
            saveMove = new JButton("保存棋谱",lalala);saveMove = setBackGround(saveMove);
            saveMove.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        saveMoveseq();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            saveChessBoard = new JButton("保存棋盘",lalala);saveChessBoard = setBackGround(saveChessBoard);
            saveChessBoard.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        saveChessBoard();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            if(type!=7){panel2.add(regret);}
            panel2.add(saveMove);
            panel2.add(saveChessBoard);
            displayNowPlaying.setContentAreaFilled(false);
            panel2.add(displayNowPlaying);
            panel.add(panel1, BorderLayout.CENTER);
            panel.add(panel2, BorderLayout.EAST);
            add(panel);
        }
        else{
            JButton start = new JButton("开始接受连接");
            start.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    try {
                        startListen();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            panel3.add(start);
            panel.add(panel1,BorderLayout.CENTER);panel.add(panel3,BorderLayout.EAST);

            add(panel);
        }
    }
    protected void initialize(){
        chessBoard = initial;
        for(int i = 0;i<10;i++){
            for(int j = 0;j<9;j++){
                if(initial[i][j] == 1){
                    JButton temp = new JButton();
                    ImageIcon square=new ImageIcon("src/Image/s1.png");
                    square.setImage(square.getImage().getScaledInstance(75,75, 0));
                    temp.setIcon(square);
                    buttonList[i][j] = temp;
                    chessBoardNow[i][j] = new s(i,j);
                }
                else if(initial[i][j] == 11){
                    JButton temp = new JButton();
                    ImageIcon square=new ImageIcon("src/Image/s2.png");
                    square.setImage(square.getImage().getScaledInstance(75,75, 0));
                    temp.setIcon(square);
                    buttonList[i][j] = temp;
                    chessBoardNow[i][j] = new s(i,j);
                }
                else if(initial[i][j] == 2){
                    JButton temp = new JButton();
                    ImageIcon square=new ImageIcon("src/Image/p1.png");
                    square.setImage(square.getImage().getScaledInstance(75,75, 0));
                    temp.setIcon(square);
                    buttonList[i][j] = temp;
                    chessBoardNow[i][j] = new p(i,j);
                    selectedX = i;
                    selectedY = j;
                }
                else if(initial[i][j] == 12){
                    JButton temp = new JButton();
                    ImageIcon square=new ImageIcon("src/Image/p2.png");
                    square.setImage(square.getImage().getScaledInstance(75,75, 0));
                    temp.setIcon(square);
                    buttonList[i][j] = temp;
                    chessBoardNow[i][j] = new p(i,j);
                }
                else if(initial[i][j] == 3){
                    JButton temp = new JButton();
                    ImageIcon square=new ImageIcon("src/Image/sh1.png");
                    square.setImage(square.getImage().getScaledInstance(75,75, 0));
                    temp.setIcon(square);
                    buttonList[i][j] = temp;
                    chessBoardNow[i][j] = new sh(i,j);
                }
                else if(initial[i][j] == 13){
                    JButton temp = new JButton();
                    ImageIcon square=new ImageIcon("src/Image/sh2.png");
                    square.setImage(square.getImage().getScaledInstance(75,75, 0));
                    temp.setIcon(square);
                    buttonList[i][j] = temp;
                    chessBoardNow[i][j] = new sh(i,j);
                }
                else if(initial[i][j] == 4){
                    JButton temp = new JButton();
                    ImageIcon square=new ImageIcon("src/Image/x1.png");
                    square.setImage(square.getImage().getScaledInstance(75,75, 0));
                    temp.setIcon(square);
                    buttonList[i][j] = temp;
                    chessBoardNow[i][j] = new x(i,j);
                }
                else if(initial[i][j] == 14){
                    JButton temp = new JButton();
                    ImageIcon square=new ImageIcon("src/Image/x2.png");
                    square.setImage(square.getImage().getScaledInstance(75,75, 0));
                    temp.setIcon(square);
                    buttonList[i][j] = temp;
                    chessBoardNow[i][j] = new x(i,j);
                }
                else if(initial[i][j] == 15){
                    JButton temp = new JButton();
                    ImageIcon square=new ImageIcon("src/Image/m2.png");
                    square.setImage(square.getImage().getScaledInstance(75,75, 0));
                    temp.setIcon(square);
                    buttonList[i][j] = temp;
                    chessBoardNow[i][j] = new m(i,j);
                }
                else if(initial[i][j] == 5){
                    JButton temp = new JButton();
                    ImageIcon square=new ImageIcon("src/Image/m1.png");
                    square.setImage(square.getImage().getScaledInstance(75,75, 0));
                    temp.setIcon(square);
                    buttonList[i][j] = temp;
                    chessBoardNow[i][j] = new m(i,j);
                }
                else if(initial[i][j] == 6){
                    JButton temp = new JButton();
                    ImageIcon square=new ImageIcon("src/Image/c1.png");
                    square.setImage(square.getImage().getScaledInstance(75,75, 0));
                    temp.setIcon(square);
                    buttonList[i][j] = temp;
                    chessBoardNow[i][j] = new c(i,j);
                }
                else if(initial[i][j] == 16){
                    JButton temp = new JButton();
                    ImageIcon square=new ImageIcon("src/Image/c2.png");
                    square.setImage(square.getImage().getScaledInstance(75,75, 0));
                    temp.setIcon(square);
                    buttonList[i][j] = temp;
                    chessBoardNow[i][j] = new c(i,j);
                }
                else if(initial[i][j] == 7){
                    JButton temp = new JButton();
                    ImageIcon square=new ImageIcon("src/Image/j1.png");
                    square.setImage(square.getImage().getScaledInstance(75,75, 0));
                    temp.setIcon(square);
                    buttonList[i][j] = temp;
                    chessBoardNow[i][j] = new j(i,j);}
                else if(initial[i][j] == 17){
                    JButton temp = new JButton();
                    ImageIcon square=new ImageIcon("src/Image/j2.png");
                    square.setImage(square.getImage().getScaledInstance(75,75, 0));
                    temp.setIcon(square);
                    buttonList[i][j] = temp;
                    chessBoardNow[i][j] = new j(i,j);}

            }
        }
        for(int i = 9;i>=0;i--){
            for(int j =0;j<9;j++){
                setButton(i,j);
                panel1.add(buttonList[i][j],9-i,j);
            }
        }


    }
    public void load() throws IOException {
        JFileChooser jfc=new JFileChooser();
        jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES );
        jfc.showDialog(new JLabel(), "选择");
        File file=jfc.getSelectedFile();
        if(file.getName().endsWith(".chessboard")){
            String strFile = file.getName();
            int dot = strFile.indexOf('.');
            strFile = strFile.substring(0,dot);
            boolean twice = false;
            File[] subFiles = file.getParentFile().listFiles();
            for(int i = 0;i<subFiles.length;i++){
                if(subFiles[i].getName().contains(strFile)&&subFiles[i].getName().contains(".chessmoveseq")){
                    initial = checkChessBoardFile(file);
                    initial = checkMoveSeqFile(subFiles[i]);
                    twice = true;
                    initialize();
                }
            }
            if(!twice){initial = checkChessBoardFile(file);initialize();}
        }
        else if(file.getName().endsWith(".chessmoveseq")){
            initial = checkMoveSeqFile(file);
            if(!stop){initialize();}
        }
        else{
            JOptionPane.showMessageDialog(null, "您选择的文件有误", "【错误】", JOptionPane.ERROR_MESSAGE);
            stop = true;
        }


    }
    public void setButton(int i,int j){
        JButton temp;
        if(buttonList[i][j]!=null){
            temp = buttonList[i][j];
        }
        else{
            temp = new JButton();}
        temp.setSize(20,20);
        temp.setIconTextGap(0);
        temp.setBorderPainted(false);
        temp.setContentAreaFilled(false);
        temp.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if(checkAccess(i,j)&&chessBoard[i][j]!=0&&checkAccess(i,j)) {
                    fresh();
                    for (int m = 0; m < 10; m++) {
                        for (int n = 0; n < 9; n++) {
                            if (chessBoardNow[i][j].check(chessBoard, m, n)) {
                                buttonList[m][n].setBorderPainted(true);
                            }
                        }
                    }
                    clickCount++;selectedX = i;selectedY = j;}
                else if(chessBoardNow[selectedX][selectedY].check(chessBoard,i,j)&&checkAccess(selectedX,selectedY)){
                    if(chessBoard[i][j]==7||chessBoard[i][j]==17){
                        JOptionPane.showMessageDialog(null, String.format("将军！"), " ", JOptionPane.WARNING_MESSAGE);
                        if(type == 7){
                            try {
                                rankingList();
                                dispose();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                        else if(type==4){
                        send(String.format("move_%d_%d_%d_%d",9-selectedX,8-selectedY,9-i,8-j),youID,sendport);dispose();}
                        else{type = 666;}
                    }
                    chessBoard = chessBoardNow[selectedX][selectedY].move(chessBoard,i,j);
                    chessBoardNow[i][j] = chessBoardNow[selectedX][selectedY];
                    chessBoardNow[selectedX][selectedY] = null;
                    buttonList[i][j].setIcon(buttonList[selectedX][selectedY].getIcon());buttonList[selectedX][selectedY] = null;setButton(selectedX,selectedY);
                    int[] temp = changeToMoveSeq(selectedX,selectedY,i,j);
                    for(int i = 0;i<4;i++){
                        moveseq.add(temp[i]);
                    }step++;
                    try {
                        saveMoveseq();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    if(type>=10){type=8;}
                    if(type==4){
                        fresh();refresh();
                        send(String.format("move_%d_%d_%d_%d",9-selectedX,8-selectedY,9-i,8-j),youID,sendport);
                        JOptionPane.showMessageDialog(null, String.format("等待对手....."), "提示", JOptionPane.INFORMATION_MESSAGE);
                        try {
                            startListen();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                    else if(type == 7&&nowPlaying){
                        nowPlaying = false;
                        int[] moving = new int[]{selectedX, selectedY, i, j};
                        checkPlayerStartMoving(String.format("%d %d %d %d",moving[0],moving[1],moving[2],moving[3]));
                        if(step<=10){
                            if(predictedUSERMoving){
                                PVEReadingCount++;
                                String line = "";
                                InputStreamReader reader1 = null;
                                try {
                                    reader1 = new InputStreamReader(
                                            new FileInputStream(selectedPVEModel));
                                } catch (FileNotFoundException ex) {
                                    ex.printStackTrace();
                                }
                                BufferedReader scanner1 = new BufferedReader(reader1);
                                int nowCount = 1;
                                try {
                                    line = scanner1.readLine();
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                                while(nowCount<PVEReadingCount&&line!=null){
                                    try {
                                        line = scanner1.readLine();
                                    } catch (IOException ex) {
                                        ex.printStackTrace();
                                    }
                                    nowCount++;
                                }
                                if (line.contains("#")) {
                                    line = line.substring(0, line.indexOf("#"));
                                }
                                String[] doMoving = line.split(" ");
                                selectedX = Integer.parseInt(doMoving[0]);
                                selectedY = Integer.parseInt(doMoving[1]);
                                int i1 = Integer.parseInt(doMoving[2]);
                                int j1 = Integer.parseInt(doMoving[3]);
                                if(chessBoardNow[selectedX][selectedY]!=null&&chessBoardNow[selectedX][selectedY].check(chessBoard,i1,j1)){
                                chessBoard = chessBoardNow[selectedX][selectedY].move(chessBoard,i1,j1);
                                chessBoardNow[i1][j1] = chessBoardNow[selectedX][selectedY];
                                chessBoardNow[selectedX][selectedY] = null;
                                buttonList[i1][j1].setIcon(buttonList[selectedX][selectedY].getIcon());buttonList[selectedX][selectedY] = null;setButton(selectedX,selectedY);
                                temp = changeToMoveSeq(selectedX,selectedY,i1,j1);}
                                else{
                                    int[] moveNext = PVENextMove();
                                    selectedX = moveNext[0];
                                    selectedY = moveNext[1];
                                    int i2 = moveNext[2];
                                    int j2 = moveNext[3];
                                    chessBoard = chessBoardNow[selectedX][selectedY].move(chessBoard,i2,j2);
                                    chessBoardNow[i2][j2] = chessBoardNow[selectedX][selectedY];
                                    chessBoardNow[selectedX][selectedY] = null;
                                    buttonList[i2][j2].setIcon(buttonList[selectedX][selectedY].getIcon());buttonList[selectedX][selectedY] = null;setButton(selectedX,selectedY);
                                    temp = changeToMoveSeq(selectedX,selectedY,i2,j2);
                                }

                            }
                            else{
                                int[] moveNext = PVENextMove();
                                selectedX = moveNext[0];
                                selectedY = moveNext[1];
                                int i2 = moveNext[2];
                                int j2 = moveNext[3];
                                chessBoard = chessBoardNow[selectedX][selectedY].move(chessBoard,i2,j2);
                                chessBoardNow[i2][j2] = chessBoardNow[selectedX][selectedY];
                                chessBoardNow[selectedX][selectedY] = null;
                                buttonList[i2][j2].setIcon(buttonList[selectedX][selectedY].getIcon());buttonList[selectedX][selectedY] = null;setButton(selectedX,selectedY);
                                temp = changeToMoveSeq(selectedX,selectedY,i2,j2);
                            }
                        }
                        else{
                            Random random = new Random();
                            int number = random.nextInt(3 )+1;
                            if(chessBoardNow[9-selectedX][8-selectedY]!=null&&chessBoardNow[9-selectedX][8-selectedY].check(chessBoard,9-i,8-j)){
                                if(number==1){
                                    chessBoard = chessBoardNow[9-selectedX][8-selectedY].move(chessBoard,9-i,8-j);
                                    chessBoardNow[9-i][8-j] = chessBoardNow[9-selectedX][8-selectedY];
                                    chessBoardNow[9-selectedX][8-selectedY] = null;
                                    buttonList[9-i][8-j].setIcon(buttonList[9-selectedX][8-selectedY].getIcon());buttonList[9-selectedX][8-selectedY] = null;setButton(9-selectedX,8-selectedY);
                                    temp = changeToMoveSeq(9-selectedX,8-selectedY,9-i,8-j);
                                }
                                else{
                                    int[] moveNext = PVENextMove();
                                    selectedX = moveNext[0];
                                    selectedY = moveNext[1];
                                    int i3 = moveNext[2];
                                    int j3 = moveNext[3];
                                    chessBoard = chessBoardNow[selectedX][selectedY].move(chessBoard,i3,j3);
                                    chessBoardNow[i3][j3] = chessBoardNow[selectedX][selectedY];
                                    chessBoardNow[selectedX][selectedY] = null;
                                    buttonList[i3][j3].setIcon(buttonList[selectedX][selectedY].getIcon());buttonList[selectedX][selectedY] = null;setButton(selectedX,selectedY);
                                    temp = changeToMoveSeq(selectedX,selectedY,i3,j3);
                                }
                            }
                            else{
                                int[] moveNext = PVENextMove();
                                selectedX = moveNext[0];
                                selectedY = moveNext[1];
                                int i4 = moveNext[2];
                                int j4 = moveNext[3];
                                chessBoard = chessBoardNow[selectedX][selectedY].move(chessBoard,i4,j4);
                                chessBoardNow[i4][j4] = chessBoardNow[selectedX][selectedY];
                                chessBoardNow[selectedX][selectedY] = null;
                                buttonList[i4][j4].setIcon(buttonList[selectedX][selectedY].getIcon());buttonList[selectedX][selectedY] = null;setButton(selectedX,selectedY);
                                temp = changeToMoveSeq(selectedX,selectedY,i4,j4);
                            }
                        }
                        int checkMate = 0;
                        for(int i = 0;i<10;i++){
                            for(int j = 0;j<9;j++){
                                if(chessBoard[i][j]==7){checkMate = 1;}
                            }
                        }
                        if(checkMate == 0){JOptionPane.showMessageDialog(null, "对不起QAQ，您输了", "提示", JOptionPane.WARNING_MESSAGE);dispose();}
                    }
                    selectedX = i;selectedY = j;nowPlaying = !nowPlaying;fresh();refresh();


                }
                else{
                    JOptionPane.showMessageDialog(null, "您选择的棋子有误", "【错误】", JOptionPane.ERROR_MESSAGE);
                    stop = true;
                }

            }
            @Override
            public void mousePressed(MouseEvent e) {

            }
            @Override
            public void mouseReleased(MouseEvent e) { }
            @Override
            public void mouseEntered(MouseEvent e) { }
            @Override
            public void mouseExited(MouseEvent e) { }
        });
        buttonList[i][j] = temp;
    }
    private class chessClickListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }
    private boolean checkAccess(int x,int y){
        boolean answer = false;
        boolean judge = true;

        if (nowPlaying) {
            judge = true;
        }
        else {
            judge = false;
        }
        if((chessBoard[x][y]>10&& !judge)||(chessBoard[x][y]<10&&judge)){
            answer = true;
        }
        if((type == 4||type ==7||type == 666)&&!nowPlaying){
            answer = false;
        }
        return answer;
    }
    protected int[][] initialChessBoard(){
        int[][] initial = new int[10][9];
        initial= new int[][]{{16, 15, 14, 13, 17, 13, 14, 15, 16}, {0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 12, 0, 0, 0, 0, 0, 12, 0}, {11, 0, 11, 0, 11, 0, 11, 0, 11}, {0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0}, {1, 0, 1, 0, 1, 0, 1, 0, 1}, {0, 2, 0, 0, 0, 0, 0, 2, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0}, {6, 5, 4, 3, 7, 3, 4, 5, 6}};
        return initial;
    }
    private void refresh(){
        panel1.removeAll();
        for(int i = 9;i>=0;i--){
            for(int j =0;j<9;j++){
                panel1.add(buttonList[i][j],9-i,j);
            }
        }
        if(!nowPlaying){displayNowPlaying.setText("现在行棋方：黑方");}
        else{displayNowPlaying.setText("现在行棋方：红方");}
        panel2.repaint();
    }
    private void fresh(){
        for(int i = 0;i<10;i++){
            for(int j =0;j<9;j++){
                buttonList[i][j].setBorderPainted(false);
            }
        }
    }
    private int[] changeToMoveSeq(int originalX,int originalY,int x,int y){
        int[] answer = new int[4];
        if(nowPlaying){
            answer[1] = 10-originalX;
            answer[0] = 9-originalY;
            answer[3] = 10-x;
            answer[2] = 9-y;
        }
        else{
            answer[1] = originalX+1;
            answer[0] = originalY+1;
            answer[3] = x+1;
            answer[2] = y+1;
        }
        return answer;
    }
    protected void saveMoveseq() throws IOException {
        File write = new File("src/save/moveseq.moveseq");
        BufferedWriter out = new BufferedWriter(new FileWriter(write));
        out.write(String.format("@TOTAL_STEP=%d\n" + "@@\n\n",step));
        for(int i = 0;i<moveseq.size();i++){
            if((i+1)%4!=0){
                out.write(String.format("%d ",moveseq.get(i)));
            }
            else{
                out.write(String.format("%d\n",moveseq.get(i)));
            }
        }
        out.close();
    }
    protected void saveChessBoard() throws IOException {
        File write = new File("src/save/chessboard.chessboard");
        BufferedWriter out = new BufferedWriter(new FileWriter(write));
        if(nowPlaying){
            out.write("@LAST_MOVER=BLACK\n@@\n\n");
        }
        else{
            out.write("@LAST_MOVER=RED\n@@\n\n");
        }
        for(int i = 0;i<10;i++){
            if(i==5){out.write("---------\n");}
            for(int j = 0;j<9;j++){
                int a = chessBoard[i][j];
                switch(a){
                    case 0:
                        out.write(".");
                        break;
                    case 1:
                        out.write("s");
                        break;
                    case 2:
                        out.write("n");
                        break;
                    case 3:
                        out.write("a");
                        break;
                    case 4:
                        out.write("e");
                        break;
                    case 5:
                        out.write("h");
                        break;
                    case 6:
                        out.write("c");
                        break;
                    case 7:
                        out.write("g");
                        break;
                    case 11:
                        out.write("S");
                        break;
                    case 12:
                        out.write("N");
                        break;
                    case 13:
                        out.write("A");
                        break;
                    case 14:
                        out.write("E");
                        break;
                    case 15:
                        out.write("H");
                        break;
                    case 16:
                        out.write("C");
                        break;
                    case 17:
                        out.write("G");
                        break;
                }
            }
            out.write("\n");
        }
        out.close();

    }
    private int[][] checkMoveSeqFile(File input) throws IOException {
        int[][] answer = initial;
        String line = "";
        InputStreamReader reader = new InputStreamReader(
                new FileInputStream(input));
        BufferedReader scanner = new BufferedReader(reader);
        line = scanner.readLine();
        int lineCount = 0;
        int step;
        while (line != null&& !stop) {
            lineCount++;
            if(line.contains("#")){
                line = line.substring(0,line.indexOf("#"));
            }
            if(lineCount>3) {
                String[] temp = line.split(" ");
                if(temp.length!=4){JOptionPane.showMessageDialog(null, String.format("文件损坏！\n在第%d行与目标数据出入%d个数据\n文件位置：%s",lineCount,Math.abs(temp.length-4),input.getPath()), "【错误】", JOptionPane.ERROR_MESSAGE);line = scanner.readLine();continue;}
                else{
                    boolean first = true;
                    boolean second = true;
                    int a=0,b=0,c=0,d=0,player;
                    int temp233 = 0;
                    if(nowPlaying){temp233 = 1;player = 1;}
                    else{temp233 = 0;player = 2;}
                    switch(temp233){
                        case 1:
                            a =9- Integer.parseInt(temp[0]);b =10- Integer.parseInt(temp[1]);c =9- Integer.parseInt(temp[2]);d =10- Integer.parseInt(temp[3]);
                            break;
                        case 0:
                            a = Integer.parseInt(temp[0])-1;b = Integer.parseInt(temp[1])-1;c = Integer.parseInt(temp[2])-1;d = Integer.parseInt(temp[3])-1;
                            break;
                    }
                    if(a>9|b>10|a<0|b<0|c>9|c<0|d>10|d<0){JOptionPane.showMessageDialog(null, String.format("文件损坏！\n在第%d行棋子位置在边界外 (Position Out Of Range)\n文件位置：%s",lineCount,input.getPath()), "【错误】", JOptionPane.ERROR_MESSAGE);line = scanner.readLine();continue;}
                    else if(answer[b][a]==0||String.valueOf(answer[b][a]).length()!=player){JOptionPane.showMessageDialog(null, String.format("文件损坏！\n在第%d行起始位置无棋子 / 无本方棋子 / 非本方目标类型棋子(Invalid From Position)\n文件位置：%s",lineCount,input.getPath()), "【错误】", JOptionPane.ERROR_MESSAGE);line = scanner.readLine();continue; }
                    else if(String.valueOf(answer[d][c]).length()==player&&answer[d][c]!=0){JOptionPane.showMessageDialog(null, String.format("文件损坏！\n在第%d行 目标位置存在本方棋子 (Invalid To Position)\n文件位置：%s",lineCount,input.getPath()), "【错误】", JOptionPane.ERROR_MESSAGE);line = scanner.readLine();continue;}
                    else{
                        boolean check = true ;
                        Chess tempChess;
                        switch(answer[b][a]){
                            case 11:
                            case 1:
                                tempChess = new s(b,a);
                                check = tempChess.check(answer,d,c);
                                break;
                            case 12:
                            case 2:
                                tempChess = new p(b,a);
                                check = tempChess.check(answer,d,c);
                                break;
                            case 3:
                            case 13:
                                tempChess = new sh(b,a);
                                check = tempChess.check(answer,d,c);
                                break;
                            case 4:
                            case 14:
                                tempChess = new x(b,a);
                                check = tempChess.check(answer,d,c);
                                break;
                            case 5:
                            case 15:
                                tempChess = new m(b,a);
                                check = tempChess.check(answer,d,c);
                                break;
                            case 6:
                            case 16:
                                tempChess = new c(b,a);
                                check = tempChess.check(answer,d,c);
                                break;
                            case 7:
                            case 17:
                                tempChess = new j(b,a);
                                check = tempChess.check(answer,d,c);
                                break;

                        }
                        if(!check){JOptionPane.showMessageDialog(null, String.format("文件损坏！\n在第%d行 行期棋子移动非法 (Invalid Move Pattern)\n文件位置：%s",lineCount,input.getPath()), "【错误】", JOptionPane.ERROR_MESSAGE);line = scanner.readLine();continue;}
                        else{answer[d][c] = answer[b][a];answer[b][a] = 0;int[] temp666 = changeToMoveSeq(b,a,d,c);
                            for(int i = 0;i<4;i++){
                                moveseq.add(temp666[i]);
                            }
                            nowPlaying = !nowPlaying;}
                    }
                }


            }
            line = scanner.readLine();
        }
        return answer;
    }
    private int[][] checkChessBoardFile(File input) throws IOException {
        int djb = 4;
        int[][] answer = initialChessBoard();
        String line = "";
        InputStreamReader reader = new InputStreamReader(
                new FileInputStream(input));
        BufferedReader scanner = new BufferedReader(reader);
        line = scanner.readLine();
        int lineCount = 0;
        while (line!= null&& !stop) {
            lineCount++;
            if (line.contains("#")) {
                line = line.substring(0, line.indexOf("#"));
            }
            else if (lineCount == 1) {
                String a = line.substring(12);
                switch (a){
                    case "B":
                        nowPlaying = true;
                        break;
                    case "R":
                        nowPlaying = false;
                        break;
                }
            }
            else if(lineCount>3){
                if(line.trim().length()!=9){JOptionPane.showMessageDialog(null, String.format("文件损坏！\n在第%d行棋局长宽错误 (Invalid Dimension)出入%d个数据\n文件位置：%s",lineCount,Math.abs(line.length()-4),input.getPath()), "【错误】", JOptionPane.ERROR_MESSAGE);stop = true;}
                else if(lineCount==9&&!line.equals("---------")||line.trim().length()!=9){JOptionPane.showMessageDialog(null, String.format("文件损坏！\n在第%d行楚河汉界缺失 (River Missing)\n文件位置：%s",lineCount,input.getPath()), "【错误】", JOptionPane.ERROR_MESSAGE);stop = true;}
                else if(lineCount ==9&&line.equals("---------")){djb=5;line = scanner.readLine();continue;
                }
                else{answer[lineCount-djb]=chessBoardToString(line);}
            }
            line = scanner.readLine();
        }
        for(int i = 1;i<=17;i++){
            int[] normal = new int[]{0,5,2,2,2,2,2,1,0,0,0,5,2,2,2,2,2,1};
            if(i>=8&&i<=10){continue;}
            int jb = count(answer,i);
            if(jb!=normal[i]){
                String a = null;
                switch(i){
                    case 11:
                    case 1:
                        a = "兵";
                        break;
                    case 12:
                    case 2:
                        a = "炮";
                        break;
                    case 3:
                    case 13:
                        a = "士";
                        break;
                    case 4:
                    case 14:
                        a = "相";
                        break;
                    case 5:
                    case 15:
                        a = "马";
                        break;
                    case 6:
                    case 16:
                        a = "车";
                        break;
                    case 7:
                    case 17:
                        a = "将";
                        break;

                }
                if(i<10){JOptionPane.showMessageDialog(null, String.format("文件损坏！\n棋子数量错误 (Invalid Chess Amount)：红方 %s 出入%d个\n文件位置：%s",a,jb-normal[i],input.getPath()), "【错误】", JOptionPane.ERROR_MESSAGE);stop = true;}
                else{JOptionPane.showMessageDialog(null, String.format("文件损坏！\n棋子数量错误 (Invalid Chess Amount)：黑方 %s 出入%d个\n文件位置：%s",a,jb-normal[i],input.getPath()), "【错误】", JOptionPane.ERROR_MESSAGE);stop = true;}
            }
        }
        return answer;
    }
    protected int[][] regretLoad() throws IOException {
        int[][] answer = initialChessBoard();
        String line = "";
        File input = new File("src/save/moveseq.moveseq");
        int lineCount = 0;
        String s;
        StringBuilder sb = new StringBuilder();
        InputStreamReader reader1 = new InputStreamReader(new FileInputStream(input));
        BufferedReader scanner1 = new BufferedReader(reader1);
        int i = 0;
        while((s=scanner1.readLine())!=null){
            if(s.contains("@TOT")){
                step = Integer.parseInt(s.substring(12,s.trim().length()));
                s = String.format("@TOTAL_STEP=%d",step-1);
            }
            sb.append(s).append("\n");
        }
        input.delete();
        input.createNewFile();
        BufferedWriter writer = new BufferedWriter(new FileWriter(input));
        String ddd = sb.substring(0, sb.toString().substring(0, sb.length()-1).lastIndexOf("\n"));
        writer.write(ddd+"\n");
        writer.close();
        InputStreamReader reader = new InputStreamReader(new FileInputStream(input));
        BufferedReader scanner = new BufferedReader(reader);line = scanner.readLine();
        while (line != null) {
            lineCount++;
            if(lineCount==1){
                step = Integer.parseInt(line.substring(12,line.trim().length()));
            }
            else if(lineCount>3) {
                int a=0,b=0,c=0,d=0;
                String[] temp = line.split(" ");
                switch((lineCount-3)%2){
                        case 1:
                            a =9- Integer.parseInt(temp[0]);b =10- Integer.parseInt(temp[1]);c =9- Integer.parseInt(temp[2]);d =10- Integer.parseInt(temp[3]);
                            break;
                        case 0:
                            a = Integer.parseInt(temp[0])-1;b = Integer.parseInt(temp[1])-1;c = Integer.parseInt(temp[2])-1;d = Integer.parseInt(temp[3])-1;
                            break;
                }
                answer[d][c] = answer[b][a];answer[b][a] = 0;
            }

            line = scanner.readLine();
        }
        reader.close();
        reader1.close();
        scanner.close();
        scanner1.close();
        return answer;
    }
    private void multiPlay(){

        JLabel IPlabel = new JLabel("IP：");
        JLabel otherPortlabel = new JLabel("目标端口");
        JTextField ip_address = new JTextField("192.168.x.x");
        JTextField otherPort = new JTextField("2333");
        connect = new JButton("连接",lalala);connect = setBackGround(connect);
        connect.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    String ip = ip_address.getText();//获取当前目标ip地址
                    sendport = Integer.parseInt(otherPort.getText());//获取目标连接端口
                    myID = InetAddress.getLocalHost();//获取本地ip地址
                    youID = InetAddress.getByName(ip);//获取目标ip地址
                    send("connect_",youID,sendport);
                    startListen();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        end = new JButton("断开连接",lalala);end = setBackGround(end);
        end.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                send("end_",youID,sendport);
                dispose();
            }
        });
        IPlabel.setBounds(10, 10, 40, 20);
        ip_address.setBounds(new Rectangle(60,10,50,20));
        panel3.add(IPlabel);
        panel3.add(ip_address);
        panel3.add(otherPortlabel);
        panel3.add(otherPort);
        panel3.add(connect);
        panel3.add(end);
    }
    private void send(String str,InetAddress ip,int port){ //发送数据包
        DatagramSocket s = null;
        try{
            s = new DatagramSocket();//创建一个数据报套接字
            byte data[] = new byte[100];
            data = str.getBytes();
            DatagramPacket pocket = new DatagramPacket(data,data.length,ip,port);
            s.send(pocket);//发送自寻址包
        }catch(IOException ex)
        {
            ex.printStackTrace();
        }
    }
    private void startListen() throws IOException {
        byte data[] = new byte[100];
        DatagramPacket bag = new DatagramPacket(data,data.length);
        boolean keep = true;
        if(createCount ==0|receiveport!=2333&&createCount!=666){
        sock= new DatagramSocket(receiveport);
        createCount++;}
        while(keep){
            sock.receive(bag);
            String order = new String(data);
            String s[] = new String[10];
            s = order.split("_");
            youID = bag.getAddress();
            sendport = receiveport;
            createCount = 666;
            if(s[0].equals("connect")){
                nowPlaying = false;
                send("success_",youID,sendport);
                JOptionPane.showMessageDialog(null, String.format("连接成功\n对手来自:%s-%s",bag.getAddress().getHostAddress(),bag.getPort()), "提示", JOptionPane.INFORMATION_MESSAGE);
            }
            else if(s[0].equals("success")){
                nowPlaying = true;
                keep = false;
                JOptionPane.showMessageDialog(null, String.format("连接成功\n对手来自:%s-%s",bag.getAddress().getHostAddress(),bag.getPort()), "提示", JOptionPane.INFORMATION_MESSAGE);
            }
            else if(s[0].equals("move")){//接收对手走棋数据
                keep = false;
                selectedX = Integer.parseInt(s[1]);
                selectedY = Integer.parseInt(s[2]);
                int i = Integer.parseInt(s[3]);
                int j = Integer.parseInt(s[4].trim());
                JOptionPane.showMessageDialog(null, String.format("对手已走棋：%d %d %d %d",selectedX,selectedY,i,j), "【提示】", JOptionPane.INFORMATION_MESSAGE);
                if(chessBoard[i][j] == 7){JOptionPane.showMessageDialog(null, String.format("你输了"), "【提示】", JOptionPane.INFORMATION_MESSAGE);dispose();}chessBoard = chessBoardNow[selectedX][selectedY].move(chessBoard,i,j);
                chessBoardNow[i][j] = chessBoardNow[selectedX][selectedY];
                chessBoardNow[selectedX][selectedY] = null;
                buttonList[i][j].setIcon(buttonList[selectedX][selectedY].getIcon());buttonList[selectedX][selectedY] = null;setButton(selectedX,selectedY);

                selectedX = i;selectedY = j;buttonList[i][j].setBorderPainted(true);refresh();
                nowPlaying = !nowPlaying;

            }
            else if (s[0].equals("end")){
                keep = false;
                JOptionPane.showMessageDialog(null, String.format("对手离线\n对手来自:%s-%s",bag.getAddress().getHostAddress(),bag.getPort()), "提示", JOptionPane.INFORMATION_MESSAGE);
            }
        }

    }
    private int[] chessBoardToString(String input){
        int[] answer  = new int[9];
        for(int i = 0;i<9;i++){
            String temp = input.substring(i,i+1);
            switch (temp){
                case ".":
                    answer[i] = 0;
                    break;
                case "G":
                    answer[i] = 17;
                    break;
                case "A":
                    answer[i] = 13;
                    break;
                case "E":
                    answer[i] = 14;
                    break;
                case "H":
                    answer[i] = 15;
                    break;
                case "C":
                    answer[i] = 16;
                    break;
                case "N":
                    answer[i] = 12;
                    break;
                case "S":
                    answer[i] = 11;
                    break;
                case "g":
                    answer[i] = 7;
                    break;
                case "a":
                    answer[i] = 3;
                    break;
                case "e":
                    answer[i] = 4;
                    break;
                case "h":
                    answer[i] = 5;
                    break;
                case "c":
                    answer[i] = 6;
                    break;
                case "n":
                    answer[i] = 2;
                    break;
                case "s":
                    answer[i] = 1;
                    break;
            }
        }
        return answer;
    }
    private int count(int[][] input,int type){
        int count =0;
        for(int i = 0;i<10;i++){
            for(int j = 0;j<9;j++){
                if(input[i][j]==type){count++;}
            }
        }
        return count;
    }
    private JButton setBackGround(JButton enter){
        Font f=new Font("宋体",Font.BOLD,20);
        enter.setBounds(900, 380, 200, 270);
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

    private int calculate(int [][]chessBoard1){
        int sum=0;
        for(int i=0;i<10;i++){
            for(int j=0;j<9;j++){
                switch(chessBoard1[i][j]){
                    case 11:
                        sum=sum+1;
                        break;
                    case 12:
                        sum=sum+2;
                        break;
                    case 13:
                        sum=sum+3;
                        break;
                    case 14:
                        sum=sum+4;
                        break;
                    case 15:
                        sum=sum+5;
                        break;
                    case 16:
                        sum=sum+6;
                        break;
                    case 17:
                        sum=sum+99;
                        break;
                    case 1:
                        sum-=1;
                        break;
                    case 2:
                        sum-=2;
                        break;
                    case 3:
                        sum-=3;
                        break;
                    case 4:
                        sum-=4;
                        break;
                    case 5:
                        sum-=5;
                        break;
                    case 6:
                        sum-=6;
                        break;
                    case 7:
                        sum-=99;
                        break;

                }
            }
        }
        return sum;
    }
    private void checkPlayerStartMoving(String input){
        ArrayList<File> selected = new ArrayList<>();
        for(int i = 0;i<predict.length;i++){
            if(predict[i].trim().equals(input.trim())){
                selected.add(predictFile[i]);
            }
        }
        if(selected.size()==0){predictedUSERMoving=false;}
        else if(selected.size()==1){
            selectedPVEModel = selected.get(0);
        }
        else{
            int number = (int)(Math.random()*selected.size());
            selectedPVEModel = selected.get(number);
        }
    }
    private String[] getStartFromFile() throws IOException {
        File file = new File("src/PVEdata");
        File[] seek = file.listFiles();
        ArrayList<String> temp = new ArrayList<>();
        for(int i = 0;i<seek.length;i++){
            String line = "";
            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(seek[i]));
            BufferedReader scanner = new BufferedReader(reader);
            line = scanner.readLine();
            if(line.contains("#")){
                line = line.substring(0,line.indexOf("#"));
            }
            temp.add(line.trim());
        }
        String[] answer = temp.toArray(new String[temp.size()]);
        predictFile = seek;
        return answer;
    }
    private int[] PVENextMove(){
        ArrayList<String> location = new ArrayList<>();
        for(int i = 0;i<10;i++){
            for(int j = 0;j<9;j++){
                if(chessBoard[i][j]>10){
                    location.add(String.format("%d %d",i,j));
                }
            }
        }
        ArrayList<Integer> pointList = new ArrayList<>();
        ArrayList<String> doList = new ArrayList<>();
        for(int k = 0;k<location.size();k++) {
            String[] spilted = location.get(k).split(" ");
            int a = Integer.parseInt(spilted[0]);
            int b = Integer.parseInt(spilted[1]);
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 9; j++) {
                    if(chessBoardNow[a][b].check(chessBoard,i,j)){
                        int[][] chessBoardClone = new int[10][9];
                        for(int q = 0;q<10;q++){
                            for(int s = 0;s<9;s++ ){
                                chessBoardClone[q][s] = chessBoard[q][s];
                            }
                        }
                        chessBoardClone[i][j] = chessBoardClone[a][b];
                        chessBoardClone[a][b] = 0;
                        int point = calculate(chessBoardClone);
                        pointList.add(point);
                        doList.add(String.format("%d %d %d %d",a,b,i,j));
                    }
                }
            }
        }
        int maxPoint = pointList.get(0);
        int index = 0;
        for(int i = 0;i<pointList.size();i++){
            if(pointList.get(i)>maxPoint){
                maxPoint = pointList.get(i);
                index = i;
            }
        }
        int[] answer = new int[4];
        String[] p = doList.get(index).split(" ");
        answer[0] = Integer.parseInt(p[0]);
        answer[1] = Integer.parseInt(p[1]);
        answer[2] = Integer.parseInt(p[2]);
        answer[3] = Integer.parseInt(p[3]);
        return answer;
    }
    private void rankingList() throws IOException {
        FileWriter writer = new FileWriter("src/ranking/ranklist.txt", true);
        String inputValue = JOptionPane.showInputDialog("敢问阁下尊姓大名：");
        double steptemp = (double) step;
        writer.write(String.format("%s %d\n",inputValue, (int)((1/steptemp)*1000000)));
        writer.close();
    }

}
