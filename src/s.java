

public class s extends Chess {
    private int currentX;
    private int currentY;
    private int X;
    private int Y;
    private int [][] chess;
    public s(int currentX,int currentY){
        this.currentX=currentX;
        this.currentY=currentY;
    }
    @Override
    public boolean check(int[][] chess, int x, int y) {
        this.X=x;//落点y坐标
        this.Y=y;//落点x坐标
        this.chess=chess;
        boolean flag=false;
        int differenceX=currentX-x;
        int differenceY=currentY-y;
        if(color()&&checkJ()) {
            switch(checkPosition()){//判断棋子所处位置
                case 1:
                    if(differenceX==1&&differenceY==0){
                        flag=true;
                    }//判断未过河的红棋的走法是否合法
                    break;
                case 2:
                    if((differenceX==1&&differenceY==0)||(differenceX==0&&Math.abs(differenceY)==1)){
                        flag=true;
                    }//判断过河的红棋的走法是否合法
                    break;
                case 3:
                    if (differenceX == -1 && differenceY == 0) {
                        flag=true;
                    }//判断未过河的黑棋走法是否合法
                    break;
                case 4:
                    if((differenceX==-1&&differenceY==0)||(differenceX==0&&Math.abs(differenceY)==1)){
                        flag=true;
                    }//判断过河黑棋的走法是否合法
                    break;
            }
        }
        return flag;
    }
    public int checkPosition(){
        int count=0;
        switch (chess[currentX][currentY]){
            case 1:
                if(currentX<=9&&currentX>=5){
                    count=1;
                }//将未过河的红棋返还为1
                else if(currentX<=4){
                    count=2;
                }//将过河的红棋返回为2
                break;
            case 11:
                if(currentX <= 4){
                    count=3;
                }//将未过河的黑棋返回为3
                else if(currentX <= 9){
                    count=4;
                }//将过河的黑棋返回为4
                break;
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
    }//判断落点与点击棋子是否为同种数字
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
    public int [][] move(int [][] chess,int x, int y) {
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
