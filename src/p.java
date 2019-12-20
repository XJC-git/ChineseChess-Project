import javax.swing.*;

public class p extends Chess{
    private int currentX;
    private int currentY;
    private int X;
    private int Y;
    private int [][] chess;
    public p(int currentX,int currentY){
        this.currentX=currentX;
        this.currentY=currentY;
    }
    @Override
    public boolean check(int[][] chess, int x, int y) {
        this.X=x;
        this.Y=y;
        this.chess=chess;
        boolean flag=false;
        switch (checkWay()){
            case 2:
            case 4:
            case 6:
            case 8:
                if(color()&&chess[X][Y]!=0&&checkJ()){
                 flag=true;
                }
                break;
            case 1:
            case 3:
            case 5:
            case 7:
                if(chess[X][Y]==0&&checkJ()){
                    flag=true;
                }
                break;
            case 11:
            case 12:
            case 13:
            case 14:
                    flag=false;
                break;
            case 21:
            case 22:
            case 23:
            case 24:
                if(checkJ()){
                    flag=true;
                }
                break;
        }
        return flag;
    }
    public boolean direction(){
        boolean flag=false;
        if(X==currentX||Y==currentY){
            flag=true;
        }
        return flag;
    }
    public int compare(){
        int count=0;
        if(X==currentX&&Y<currentY){
            count=1;//炮往左走
        }
        else if(X==currentX&&Y>currentY){
            count=2;//炮往右走
        }
        else if(X>currentX&&Y==currentY){
            count=3;//炮往下走
        }
        else if(X<currentX&&Y==currentY) {
            count = 4;//炮往上走
        }
        return count;
    }
    public int checkWay(){
        int count = 0;
        int number = 0;
        if(direction()){
            switch (compare()) {
                case 1://炮左走
                    if(currentY-Y>1){
                        for (int temp = 1; temp <=currentY - Y-1; temp++) {
                        if (chess[currentX][currentY - temp] != 0) {
                            number++;
                        }
                        }
                        if (number == 0) {
                            count = 1;//向左直走
                        } else if (number == 1) {
                            count = 2;//向左跳
                        }
                        else if(number>1){
                            count=11;
                        }
                    }
                    else if(currentY-Y==1){
                        if (chess[currentX][currentY-1]==0){
                            count=21;
                            break;
                        }
                    }
                    break;
                case 2://右
                    if(Y-currentY>1) {
                        for (int temp = 1; temp <= Y - currentY - 1; temp++) {
                            if (chess[currentX][currentY + temp] != 0) {
                                number++;
                            }
                        }
                            if (number == 0) {
                                count = 3;//向右直走
                            } else if (number == 1) {
                                count = 4;//向右跳
                            } else if (number > 1) {
                                count = 12;
                            }
                    }
                    else if(Y-currentY==1){
                            if(chess[currentX][currentY+1]==0){
                                count=22;
                                break;
                            }
                    }
                    break;
                case 3://下
                    if(X-currentX>1){
                        for (int temp = 1; temp <=X - currentX-1; temp++) {
                            if (chess[currentX + temp][currentY] != 0) {
                                number++;
                            }
                        }
                            if (number == 0) {
                                count = 5;//向下直走
                            }
                            else if(number==1){
                                count = 6;//向下跳
                            }
                            else if(number>1){
                                count=13;
                            }
                        } else if(X-currentX==1){
                        if(chess[currentX+1][currentY]==0){
                            count=23;
                        }
                    }
                    break;
                case 4://上
                    if(currentX-X>1){
                        for (int temp = 1; temp <=currentX - X-1; temp++) {
                            if (chess[currentX - temp][currentY] != 0) {
                                number++;
                            }
                        }
                        if (number == 0) {
                            count = 7;//向上直走
                        } else if (number == 1) {
                            count = 8;//向上跳
                        }
                        else if(number>1){
                            count=14;
                        }
                    }
                    else if(currentX-X==1){
                        if(chess[currentX-1][currentY]==0){
                            count=23;
                        }
                    }
                    break;
            }

        }
        return count;
    }
    private boolean color(){
        int count1=0;
        int count2=0;
        boolean flag;
        if(chess[currentX][currentY]<=7&&chess[currentX][currentY]>0){
            count1=1;
        }
        else if(chess[currentX][currentY]>=11&&chess[currentX][currentY]<=17){
            count1=2;
        }
        else if(chess[currentX][currentY]==0){
            count1=3;
        }
        if(chess[X][Y]<=7&&chess[X][Y]>0){
            count2=1;
        }
        else if(chess[X][Y]>=11&&chess[X][Y]<=17){
            count2=2;
        }
        else if(chess[X][Y]==0) {
            count2=3;
        }
        if(count1==count2){
            flag=false;
        }
        else{
            flag=true;
        }
        return flag;
    }
    public boolean checkJ(){
        boolean flag=true;
        int count=0;
        int count1=0;
        int rX=0,bX = 0;
        for(int i=0;i<=9;i++){
            if(chess[i][currentY]==7){
                count = 1;
                rX=i;
            }
            else if(chess[i][currentY]==17){
                count1=1;
                bX=i;
            }
        }
        if(count==1&&count1==1){
            for(int i=bX+1;i<rX;i++){
                if (chess[i][currentY] != 0 && chess[i][currentY] != chess[currentX][currentY]) {
                    flag = true;
                    break;
                }
                else {
                    flag=false;
                }
            }
        }
        return flag;
    }
    @Override
    public int[][] move(int[][] chess, int x, int y) {
            int [][]chessBoard=new int[10][9];
            for(int i=0;i<10;i++){
                for(int j=0;j<9;j++){
                    chessBoard[i][j]=chess[i][j];
                }
            }
            if(check(chess,x,y)){
                chessBoard[x][y]=chess[currentX][currentY];
                chessBoard[currentX][currentY]=0;
                this.currentX=x;
                this.currentY=y;
            }
            return chessBoard;
        }
}

