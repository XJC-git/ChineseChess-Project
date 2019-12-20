public class x extends Chess {
    private int currentX;
    private int currentY;
    private int X;
    private int Y;
    private int [][] chess;
    public x(int currentX,int currentY){
        this.currentX=currentX;//y坐标
        this.currentY=currentY;//x坐标
    }
    @Override
    public boolean check(int[][] chess, int x, int y) {
        int differenceX=currentX-x;//y方向位移
        int differenceY=currentY-y;//x方向位移
        this.X=x;//落点y坐标
        this.Y=y;//落点x坐标
        this.chess=chess;
        boolean flag=false;
        if(color()&&checkJ()&&checkColor()){
            if(Math.abs(differenceX)==2&&Math.abs(differenceY)==2&&isValid()&&checkPosition()){
            flag=true;

        }
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
    public boolean isValid(){
        boolean flag=true;
        switch (direction()){
            case 1://左上
                if(chess[currentX-1][currentY-1]!=0){
                    flag=false;
                }
                break;
            case 2://左下
                if(chess[currentX+1][currentY-1]!=0){
                    flag=false;
                }
                break;
            case 3://右上
                if(chess[currentX-1][currentY+1]!=0){
                    flag=false;
                }
                break;
            case 4:
                if(chess[currentX+1][currentY+1]!=0){
                    flag=false;
                }
                break;
        }
        return flag;
    }
    public int direction(){
        int number=0;
            if(X<currentX&&Y<currentY){
                number=1;//左上
            }
            else if(X>currentX&&Y<currentY){
                number=2;//左下
            }
            else if(X<currentX&&Y>currentY){
                number=3;//右上
            }
            else if(X>currentX&&Y>currentY){
                number=4;//右下
            }
        return number;
    }
    public boolean checkPosition(){
        boolean flag=false;
        switch (chess[currentX][currentY]){
            case 4:
                if(currentX<=9&&currentX>=5){
                    flag=true;
                }
            break;
            case 14:
                if(currentX <= 4){
                   flag=true;
                }
                break;
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
    public  boolean checkColor(){
        boolean flag=true;
        switch (chess[currentX][currentY]){
            case 4:
                if(X<5){
                    flag=false;
                }
                break;
            case 14:
                if(X>4){
                    flag=false;
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
            if(isValid()){
                chessBoard[x][y]=chessBoard[currentX][currentY];
                chessBoard[currentX][currentY]=0;
                this.currentX=x;
                this.currentY=y;
            }
        }
        return chessBoard;
    }

}
