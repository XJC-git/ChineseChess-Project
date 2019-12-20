public class c extends Chess {
    private int currentX;
    private int currentY;
    private int X;
    private int Y;
    private int[][] chess;
    public c(int x, int y){
        this.currentX = x;
        this.currentY = y;
    }
    @Override
    public boolean check(int[][] chess, int x, int y) {
        this.X=x;
        this.Y=y;
        this.chess=chess;
        boolean answer = false;
        if(direction()&&color()&&checkJ()) {
                    switch (compare()) {
                        case 1://左
                            for (int temp = 1; temp <= currentY - Y; temp++) {
                                if(currentY-Y==1){
                                    answer=true;
                                }
                                else{
                                    if (chess[currentX][currentY - temp] == 0) {
                                        answer = true;
                                    }
                                    else if (chess[currentX][currentY - temp] != 0) {
                                        answer = false;
                                        break;
                                    }
                                    if(currentY-temp-1==Y){
                                        break;
                                    }
                                }
                                }

                            break;
                        case 2://右
                            for (int temp = 1; temp <= Y - currentY; temp++) {
                                if(Y-currentY==1){
                                    answer=true;
                                }
                                else {
                                    if (chess[currentX][currentY + temp] == 0) {
                                        answer = true;
                                    } else if (chess[currentX][currentY + temp] != 0) {
                                        answer = false;
                                        break;
                                    }
                                    if(currentY+temp+1==Y){
                                        break;
                                    }
                                }
                                }
                            break;
                        case 3://下
                            for (int temp = 1; temp <= X - currentX; temp++) {
                                if(X-currentX==1){
                                    answer=true;
                                }
                                else {
                                    if (chess[currentX + temp][currentY] == 0) {
                                        answer = true;
                                    } else if (chess[currentX + temp][currentY] != 0) {
                                        answer = false;
                                        break;
                                    }
                                    if(currentX+temp+1==X){
                                        break;
                                    }
                                }
                                }
                            break;
                        case 4://上
                            for (int temp = 1; temp <= currentX - X; temp++) {
                                if(currentX-X==1){
                                    answer=true;
                                }
                                else{
                                    if (chess[currentX - temp][currentY] == 0) {
                                        answer = true;
                                    } else if (chess[currentX - temp][currentY] != 0) {
                                        answer = false;
                                        break;
                                    }
                                    if(currentX-temp-1==X){
                                        break;
                                    }
                                }
                                }
                            break;
                    }
        }
        return answer;
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
    public int compare(){
        int count=0;
        if(X==currentX&&Y<currentY){
            count=1;//车往左走
        }
        else if(X==currentX&&Y>currentY){
            count=2;//车往右走
        }
        else if(X>currentX&&Y==currentY){
            count=3;//车往下走
        }
        else if(X<currentX&&Y==currentY){
            count=4;//车往上走
        }
        return count;
    }
    public boolean direction(){
        boolean flag=false;
        if(X==currentX||Y==currentY){
            flag=true;
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

}
