import javax.swing.*;

public class j extends Chess {
    private int currentX;
    private int currentY;
    private int X;
    private int Y;
    private int [][] chess;
    public j(int currentX,int currentY){
        this.currentX=currentX;
        this.currentY=currentY;
    }
    @Override
    public boolean check(int[][] chess, int x, int y) {
        this.X=x;
        this.Y=y;
        this.chess=chess;
        int differenceX=Math.abs(currentX-X);
        int differenceY=Math.abs(currentY-Y);
        boolean flag=false;
       if(color()&&isValid()&&checkJ()){
           if((differenceX == 1 && differenceY == 0)||(differenceY==1&&differenceX==0)){
               flag=true;
           }
       }
        return flag;
    }
    public boolean isValid(){
        boolean flag=false;
        switch (chess[currentX][currentY]) {
            case 7:
                if(Y>=3&&Y<=5&&X>=7&&X<=9){
                    flag=true;
                }
                break;
            case 17:
                if(Y>=3&&Y<=5&&X>=0&&X<=2){
                    flag=true;
                }
                break;
        }
        return flag;
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
    @Override
    public int[][] move(int[][] chess, int x, int y) {
        int [][]chessBoard=new int[10][9];
        for(int i=0;i<10;i++){
            for(int j=0;j<9;j++){
                chessBoard[i][j]=chess[i][j];
            }
        }
        if(check(chess,x,y)){
            chessBoard[x][y] = chess[currentX][currentY];
            chessBoard[currentX][currentY] = 0;
            currentX = x;
            currentY = y;
        }
        return chessBoard;
    }
    public boolean checkJ(){
        boolean flag= true;
        switch (chess[currentX][currentY]){
            case 7:
                for(int i=0;i<3;i++){
                    if(chess[i][Y]==17){
                        for(int temp=i+1;temp<X;temp++){
                            if(chess[temp][Y]!=0){
                                flag=true;
                                break;
                            }
                            else {
                                flag=false;
                            }
                        }
                    }
                }
                break;
            case 17:
                for(int i=9;i>6;i--){
                    if(chess[i][Y]==7){
                        for(int temp=1;i-temp>X;temp++){
                            if(chess[temp][Y]!=0){
                                flag=true;
                                break;
                            }
                            else {
                                flag=false;
                            }
                        }
                    }
                }
                break;
        }
        return flag;
    }
}
