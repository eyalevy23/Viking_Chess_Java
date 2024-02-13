import java.util.ArrayList;

public class Board {
    public static final int BOARD_SIZE = 11;
    public static final int KING_POSSTION = 5;
    private ConcretePiece[][] board ;
    public Position kingPosition;
    private ArrayList<ConcretePiece[][]> previousBoard;
    
    Board() {
        this.board = new ConcretePiece[BOARD_SIZE][BOARD_SIZE];
        this.previousBoard = new ArrayList<ConcretePiece[][]>(); 
    }

    public void startGame(Player player1, Player player2) {
        // make sure evry squre is empty 
        for(int i = 0; i < BOARD_SIZE; i++) {
            for(int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = null;
            }
        }
        previousBoard.clear();

        // Initializing the board

        // second player Pieces
        int[][] player2PwanPossition = {{0,3}, {0,4}, {0,5}, {0,6}, {0,7}, {1,5},
                                        {3,0}, {4,0}, {5,0}, {6,0}, {7,0}, {5,1},
                                        {3,10}, {4,10}, {5,10}, {6,10}, {7,10}, {5,9},
                                        {10,3}, {10,4}, {10,5}, {10,6}, {10,7}, {9,5}};

        int [][] player1PwanPossition = {{3,5}, {4,4}, {4,5}, {4,6}, {5,3}, {5,4}, {5,6}, {5,7},
                                        {6,4}, {6,5},{6,6}, {7,5}};

        int x, y;
        // Initializing player2 position
        for(int[] position : player2PwanPossition) {
            x = position[0];
            y = position[1];
            board[x][y] = new Pawn(player2); 
        }

        // Initializing player 1 position
        for(int[] position : player1PwanPossition) {
            x = position[0];
            y = position[1];
            board[x][y] = new Pawn(player1); 
        }
        board[KING_POSSTION][KING_POSSTION] = new King(player1);
        kingPosition = new Position(KING_POSSTION, KING_POSSTION);
    }

    public void move(Position a, Position b){
        int a_x = a.getX();
        int a_y = a.getY();
        int b_x = b.getX();
        int b_y = b.getY();
        board[b_x][b_y] = board[a_x][a_y];
        board[a_x][a_y] = null;
    }

    public boolean isMoveable (Position position1, Position position2) {

        Piece piece = getPiece(position1);
        String type = piece.getType();

        // in case there is piece in position2
        if(getPiece(position2) != null) {return false;}

        // check if piece move in straight line
        Boolean straightLine = (position1.getX() == position2.getX()) ||
            (position1.getY() == position2.getY());

        if(!straightLine) {return false;}

        // check if piece jump over other piece 

        //  check in case of moving by row
        if(position1.getX() == position2.getX()){
            int col1 = Math.min(position1.getY(), position2.getY()); 
            int col2 = Math.max(position1.getY(), position2.getY());
            for(int i = col1+1 ; i < col2 ; i++) {
                if(board[position1.getX()][i] != null){
                    return false;
                }
            }
        }

        // check in case of moving by column
        if(position1.getY() == position2.getY()){
            int row1 = Math.min(position1.getX(), position2.getX()); 
            int row2 = Math.max(position1.getX(), position2.getX());
            for(int i = row1+1 ; i < row2 ; i++) {
                if(board[i][position1.getY()] != null){
                    return false;
                }
            }
        }

        switch (type) {
            case "♙":
                // in case player try to put pwan in corner
                if(isInCorners(position2)) {return false;}
                break;
        
            case "♔":
                // trace the king
                kingPosition = position2; 
                break;
        }
        return true;
    }

    public void checkForcapture(Position position) {
        int x = position.getX(), y = position.getY();
        Piece piece = getPiece(position);

        // capture with wall

        // check if piece can capture with the edge of the board by row 
        if(isEdgeCapture(x,y) || isCornerCapture(x, y)) {
            int newY = (y > 5) ? y+1 : y-1; 
            Piece otherPiece = board[x][newY];
            if(otherPiece != null) {
                Boolean playerPiece1 = piece.getOwner().isPlayerOne();
                Boolean playerpiece2 = otherPiece.getOwner().isPlayerOne();
                if(playerPiece1 != playerpiece2) {
                    killPwan(x, newY);
                }
            }
        }

        // check if piece can capture with the edge of the board by column
        if(isEdgeCapture(y, x) || isCornerCapture(y, x)) {
            int newX = (x >5 ) ? x + 1 : x - 1; 
            Piece otherPiece = board[newX][y];
            if(otherPiece != null) {
                boolean playerPiece1 = piece.getOwner().isPlayerOne();
                boolean playerPiece2 = otherPiece.getOwner().isPlayerOne();
                if(playerPiece1 != playerPiece2) {
                    killPwan(newX, y);
                }
            }

        }

        // capture with pieces 
        boolean up  = isPawnNear(x - 1, y, piece);
        boolean down = isPawnNear(x + 1, y, piece);
        boolean right = isPawnNear(x, y + 1, piece);
        boolean left = isPawnNear(x, y - 1, piece);

        if(up && isPawnSandwiched(x - 2, y, piece)) {
            killPwan(x-1, y);
        }
        if(down && isPawnSandwiched(x + 2 , y, piece)) {
            killPwan(x+1, y);
        }
        if(right && isPawnSandwiched(x, y + 2, piece)) {
            killPwan(x, y+1);
        }
        if(left && isPawnSandwiched(x, y - 2, piece)) {
            killPwan(x, y-1);
        }
    }

    public Boolean isPawnSandwiched(int row, int col, Piece originalPiece) {
        // Check if the position is within the bounds of the board
        if (row < 0 || row >= BOARD_SIZE || col < 0 || col >= BOARD_SIZE) {
            return false;
        }
    
        Piece piece = board[row][col];
    
        // If there's no piece at the specified position, return false
        if (piece == null) {
            return false;
        }
    
        // Check if the piece belongs to the same player as the original piece
        return piece.getOwner().isPlayerOne() == originalPiece.getOwner().isPlayerOne();
    }

    public boolean isPawnNear(int row, int col, Piece originalPiece) {
    
        // Check if the position is within the bounds of the board
        if (row < 0 || row >= BOARD_SIZE || col < 0 || col >= BOARD_SIZE) {
            return false;
        }
    
        Piece piece = board[row][col];
    
        // If there's no piece at the specified position, return false
        if (piece == null) {
            return false;
        }
    
        // Check if the piece belongs to the same player as the original piece
        return piece.getOwner().isPlayerOne() != originalPiece.getOwner().isPlayerOne();
    }
    

    public Boolean isEdgeCapture(int x, int y) {
        return((y == 9 || y == 1) &&  x != 10 && x != 0 );
    }

    public Boolean isCornerCapture(int x, int y) {
        return (y == 8 || y == 2) && (x == 0 || x == 10);
    }


    public boolean isKingInCorner() {
        return isInCorners(kingPosition);
    }

    public boolean isKingSurround() {
        int x = kingPosition.getX();
        int y = kingPosition.getY();

        boolean up = isEnemyPiece(x-1, y) || isKingOnEdge(x, Direction.UP);
        boolean down = isEnemyPiece(x+1, y) || isKingOnEdge(x, Direction.DOWN);
        boolean right = isEnemyPiece(x, y+1) || isKingOnEdge(y, Direction.RIGHT);
        boolean left = isEnemyPiece(x, y-1) || isKingOnEdge(y, Direction.LEFT);
        
        return up && down && right && left;
    }

    public boolean isKingOnEdge(int coordinate, Direction direction) {
        switch (direction) {
            case UP:
                return coordinate == 0;
            case DOWN:
                return coordinate == BOARD_SIZE - 1;
            case RIGHT:
                return coordinate == BOARD_SIZE - 1;
            case LEFT:
                return coordinate == 0;
            default:
                return false;
        }
    }

    private enum Direction {
        UP, DOWN, RIGHT, LEFT
    }

    public boolean isEnemyPiece(int x, int y) {
        if (x < 0 || x >= BOARD_SIZE || y < 0 || y >= BOARD_SIZE) {
            return false; // Ensure coordinates are within the board bounds
        }

        Piece piece = board[x][y];

        if (piece == null) {
            return false;
        }

        // Check if the piece belongs to the opponent player
        return !piece.getOwner().isPlayerOne();
    }

    public Piece getPiece(Position position) {
        return board[position.getX()][position.getY()];
    }

    public Boolean isInCorners(Position position) {
        // corners = {{0,0}, {10,0}, {0,10}, {10, 10}}; 
        int x = position.getX();
        int y = position.getY();

        return (x == 0 || x == 10) && (y == 0 || y == 10);
    }

    public void killPwan(int x, int y) {
        if(board[x][y].getType() == "♙") {
            board[x][y] = null;
        }
    }

    public void undo(int totalMoves) {

        // Retrieve the previous board state
        ConcretePiece[][] previousBoardState = previousBoard.remove(previousBoard.size() - 1);
    
        // Set the current board to the previous board state
        this.board = previousBoardState;
    }

    public void saveBoard() {
        previousBoard.add(deepCopy(board));
    }

    public ConcretePiece[][] deepCopy(ConcretePiece[][] board) {
        ConcretePiece[][] copyBoard = new ConcretePiece[11][11];

        for(int i = 0; i < 11; i++) {
            for(int j = 0; j < 11; j++) {
                copyBoard[i][j] = board[i][j];
            }
        }
        return copyBoard;
    }

    public int boardSize() {
        return BOARD_SIZE;
    }
}