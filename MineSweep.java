import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class MineSweep {
  String[][] board;
  int size;
  int[][] bombs;
  public MineSweep (int s, String f) throws FileNotFoundException {
    size = s;
    getBombs(f);
    board = new String[s][s];
    for(int i = 0; i < size; i++) {
      for(int j = 0; j < size; j++) {
        board[i][j] = "x";
      }
    }
    REPL();
  }

  private void getBombs(String file) throws FileNotFoundException {
    bombs = new int[10][2];
    for(int i = 0; i < 10; i++) {
      bombs[i][0] = -1;
      bombs[i][1] = -1;
    }
    File f = new File(file);
    Scanner input = new Scanner(f);

    int cnt = 0;
    while(cnt < 10 && input.hasNext()) {
      String s = input.nextLine();
      Scanner str = new Scanner(s);
      bombs[cnt][0] = str.nextInt();
      bombs[cnt][1] = str.nextInt();
      cnt++;
    }
  }
  private void REPL () {
    Scanner input = new Scanner(System.in);
    int row = 0;
    int col = 0;
    while(row < size && col < size) {
      System.out.println("TAKE A GUESS : ");
      row = input.nextInt();
      col = input.nextInt();
      if(!updateBoard(row, col)) {
        System.out.println("YOU HIT A BOMB !!!");
        markBombs();
        row = size + 1;
      }
      printBoard();
      System.out.println();
    }
  }
  
  private boolean updateBoard(int row, int col) {
    if(isBomb(row, col))
      return false;
    mark(row, col);
    return true;
  }

  private void markBombs() {
    for(int i = 0; i < 10; i++) {
      if(bombs[i][0] > 0 && bombs[i][1] > 0) {
        board[bombs[i][0]][bombs[i][1]] = "B";
      } else {
        break;
      }
    }
  }

  private void mark(int row, int col) {
    int cnt;
    int r;
    int c;
    for(int i = -1; i <= 1; i ++) {
      for(int j = -1; j <= 1; j ++) {
        if(i == 0 && j == 0) continue;
        r = row + i;
        c = col + j;
        if(r >= 0 && r < size && c >= 0 && c < size) {
          if(board[r][c] == "x" && !isBomb(r, c)) {
            cnt = count(r, c);
            board[r][c] = Integer.toString(cnt);
            if(cnt == 0 && !isBomb(r, c))
              mark(r, c);
          } 
        }
      }
    } 
  }

  private int count (int x, int y) {
    int cnt = 0;
    int xp = x;
    int yp = y;
    for(int i = -1; i <= 1; i ++) {
      for(int j = -1; j <= 1; j ++) {
        xp = x + i;
        yp = y + j;
        if(xp >= 0 && xp < size && yp >= 0 && yp < size) {
          if(isBomb(xp, yp))
            cnt++;
        }
      }
    }
    return cnt;
  }

  private boolean isBomb(int x, int y) {
    boolean ret = false;
    for(int i = 0; i < 10; i++) {
      ret |= ((bombs[i][0] == x) && (bombs[i][1] == y));
    }
    return ret;
  }

  private void printBoard () {
    String s = "";
    for(int i = 0; i < size; i++) {
      for(int j = 0; j < size; j++) {
        s += " " + board[i][j];
      }
      System.out.println(s);
      s = "";
    }
  }
  
  public static void main (String[] args) {
    int size = Integer.parseInt(args[0]);
    try {
      MineSweep game = new MineSweep(size, args[1]);
    } catch (FileNotFoundException e) {
      System.out.println("got an exception");
    }
  }
}
