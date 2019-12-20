public class sh extends Chess{
    private int currentX;
    private int currentY;
    private int X;
    private int Y;
    private int [][] chess;
    public sh(int currentX,int currentY){
        this.currentX=currentX;
        this.currentY=currentY;
    }
    @Override
    public boolean check(int[][] chess, int x, int y) {
        int differenceX=Math.abs(currentX-x);
        int differenceY=Math.abs(currentY-y);
        this.X=x;
        this.Y=y;
        this.chess=chess;
        boolean flag=false;
        if(color()&&isValid()&&checkJ()){
            if(differenceX==1&&differenceY==1){
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
    public boolean isValid(){
        boolean flag=false;
        switch (chess[currentX][currentY]) {
            case 3:
            if(Y>=3&&Y<=5&&X>=7&&X<=9){
                flag=true;
            }
            break;
            case 13:
                if(Y>=3&&Y<=5&&X>=0&&X<=2){
                    flag=true;
                }
                break;
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
