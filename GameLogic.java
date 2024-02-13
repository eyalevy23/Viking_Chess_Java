// import org.junit.jupiter.api.Named;

public class GameLogic implements PlayableLogic{
    
    private Board board;
    private ConcretePlayer player1;
    private ConcretePlayer player2;
    private boolean player1Turn, player2Turn;
    private int totalMoves = 0;

    GameLogic() {
        this.board = new Board();
        this.player1 = new ConcretePlayer(1);
        this.player2 = new ConcretePlayer(2);
        board.startGame(player1, player2);
        this.player1Turn = false;
        this.player2Turn = true;
    }

    @Override
    public boolean move(Position a, Position b) {
        Player onwer = getPieceAtPosition(a).getOwner();

        // check whose turn it is
        if(player1Turn && onwer != player1 || player2Turn && onwer != player2){
            return false;
        }

        if(board.isMoveable(a,b)) {
            board.saveBoard();
            board.move(a, b);
            if(isGameOver(b)) return true; 
            board.checkForcapture(b);
            turnOver();
            return true;
        }
        return false;
    }

    public boolean isGameOver(Position position) {

        boolean isKingCorner = board.isKingInCorner();
        boolean isKingSurrounded = board.isKingSurround();
    
        if (isKingCorner || isKingSurrounded) {
            (isKingCorner ? player1 : player2).playerWin();
            reset(); // Reset the game
            return true; // Game over
        }
        return false; // Game continues
    }
    

    @Override
    public Piece getPieceAtPosition(Position position) {
        return board.getPiece(position);
    }

    @Override
    public Player getFirstPlayer() {
        return player1;
    }

    @Override
    public Player getSecondPlayer() {
        return player2;
    }

    @Override
    public boolean isGameFinished() {
        return false;
    }

    @Override
    public boolean isSecondPlayerTurn() {
        return !player1Turn;
    }


    @Override
    public void reset() {
        board.startGame(player1, player2);
        totalMoves = 0;
        player1Turn = false;
        player2Turn = true;
    }

    @Override
    public void undoLastMove() {
        if(totalMoves > 0) {
            board.undo(totalMoves);
            totalMoves--;
            player1Turn = !player1Turn;
            player2Turn = !player2Turn;
        }
    }

    @Override
    public int getBoardSize() {
        return board.boardSize();
    }

    public void turnOver() {
        player1Turn = !player1Turn;
        player2Turn = !player2Turn;
        totalMoves++;
    }
}

