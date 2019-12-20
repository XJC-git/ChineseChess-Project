public class m extends Chess {
    private int currentX;
    private int currentY;
    private int X;
    private int Y;
    private int [][] chess;
    public m(int currentX,int currentY){
        this.currentX=currentX;//y坐标
        this.currentY=currentY;//x坐标
    }
    @Override
    public boolean check(int[][] chess, int x, int y) {
        int differenceX=currentX-x;//y坐标的差
        int differenceY=currentY-y;//x坐标的差
        this.X=x;//落点的y坐标
        this.Y=y;//落点的x坐标
        this.chess=chess;//传入棋盘
        boolean flag=false;
        if(color()&&checkJ()){
            if(Math.abs(differenceX)==1&&Math.abs(differenceY)==2&&isValid()){
                flag=true;
            }
            else if(Math.abs(differenceX)==2&&Math.abs(differenceY)==1&&isValid()){
                flag=true;
            }
        }//如果落点不是己方棋子，判断路径是否合法
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
    public int direction(){
        int number = 0;
        int differenceX=currentX-X;//y方向移动的距离
        int differenceY=currentY-Y;//x方向移动的距离
            if(differenceX==2&&differenceY==1){
                number=1;//上偏左
            }
            else if(differenceX==1&&differenceY==2){
                number=2;//左偏上
            }
            else if(differenceX==-1&&differenceY==2){
                number=2;//左偏下
            }
            else if(differenceX==-2&&differenceY==1){
                number=4;//下偏左
            }
            else if(differenceX==-2&&differenceY==-1){
                number=4;//下偏右
            }
            else if(differenceX==1&&differenceY==-2){
                number=3;//右偏下
            }
            else if(differenceX==-1&&differenceY==-2){
                number=3;//右偏上
            }
            else if(differenceX==2&&differenceY==-1){
                number=1;//上偏右
            }
        return number;
    }
    public boolean isValid(){
        boolean flag=true;
        switch (direction()){
            case 1://向上是别马腿
                if(chess[currentX-1][currentY]!=0){
                    flag=false;
                }
                break;
            case 2://向左是别马腿
                if(chess[currentX][currentY-1]!=0){
                    flag=false;
                }
                break;
            case 3://向右是别马腿
                if(chess[currentX][currentY+1]!=0){
                    flag=false;
                }
                break;
            case 4://向下是别马腿
                if(chess[currentX+1][currentY]!=0){
                    flag=false;
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
