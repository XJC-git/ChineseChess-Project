# ChineseChess-Project
#中国象棋 - UDP局域网对战/低级人工智障/悔棋/存储棋谱/加载棋谱/UDP LAN battle / low level artificial mental retardation / regret chess / store #chess manual / load chess manual
### 简介
棋盘通过在每一个点布局按钮（JButton）的方式覆盖整个棋盘，通过刷新按钮的图片背景达到移动棋子等目的。
- `ChessFrame`  启动WelcomWindow，打开欢迎界面
- `WelcomWindow`  加载欢迎页面，通过面板上不同的按钮开启不同种类的棋盘
- `ChessBoard` 功能实现的代码都在这里
- `c,s,sh,j,m,p,x`  每一个象棋棋子的运行规则，继承chess类，拥有check和move两个方法
- `/Image/` 存储棋盘和棋子的图片
- `/PVEdata/` 存储一些常见棋局作为人机对战数据
- `/save/` 存储用户要求的棋局和棋谱
- `/Audio/` 存储背景音乐

### 关于存储和读取
#### 棋盘(.chessboard)
1.使用单个英文字符表示棋子，黑方大写，红方小写。具体见下表。

棋子 | 英文助记 | 黑方（大写）| 红方（小写）
:---- |:---- |:---- |:----
將 / 帥 | [G]eneral | G | g
士 / 仕 | [A]dvisor | A | a
象 / 相 | [E]lephant | E | e
馬 / 傌 | [H]orse | H | h
車 / 俥 | [C]hariot | C | c
砲 / 炮 | Ca[N]non | N | n
卒 / 兵 | [S]oldier | S | s

2.符号示例:         
` ---------# 这是楚河汉界 `   
`@LAST_MOVER=BLACK # 最后一次移动方为黑方  `   
` #这是长度为一整行的注释 `



#### 棋谱(.chessmoveseq)
1. 每行按照如下规则： `[初始本方x]` `[初始本方y]` `[目标本方x]` `[目标本方y]`    
2. 坐标系原点：对黑方为棋盘左上角，对红方为棋盘右下角，原点坐标 (1,1)    
3. 所有行棋结束后，以一个新行结束文件     
4. 解析：默认解析至文件结束，遇到无效步骤，应跳过无效步骤继续执行，并提示用户无效步骤的具 体位置和内容

##### 示例 
红：兵七进一： 7 4 7 5      
黑：炮2平3： 2 3 3 3
###### 存储示例
`@TOTAL_STEP=2 @@`    
`7 4 7 5`   
`2 3 3 3`



### Brief introduction            
The chessboard covers the whole chessboard by arranging buttons (JButton) at each point, and moves the chessboard by refreshing the picture background of the button.            
- `Chessframe` starts welcomwindow and opens the welcome interface            
- `Welcomewindow` loads the welcome page, and opens different kinds of chessboards through different buttons on the panel            -Here are all the codes of chessboard function implementation            
- `C,s,SH,J,m,P,x` the running rules of each chess piece, inherit the chess class, and have two methods of check and move          
- `/image/` store pictures of chessboard and pieces            
- `/pvedata/` store some common chess games as man-machine battle data            
- `/save/` save the chess game and chess manual required by the user            
- `/audio/` store background music

### About storage and reading            
#### Chessboard            
1. A single English character is used to represent a chess piece, with black capital and red lowercase. See the table below for details.

Chessmen | English mnemonic | black (capital) | red (lowercase)
:---- |:---- |:---- |:----
將 / 帥 | [G]eneral | G | g
士 / 仕 | [A]dvisor | A | a
象 / 相 | [E]lephant | E | e
馬 / 傌 | [H]orse | H | h
車 / 俥 | [C]hariot | C | c
砲 / 炮 | Ca[N]non | N | n
卒 / 兵 | [S]oldier | S | s
