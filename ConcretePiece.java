public abstract class ConcretePiece implements Piece {
    Player player;
    String type;

    ConcretePiece(Player player, String type) {
        this.player = player;
        this.type = type;
    }

    @Override
    public Player getOwner() {
        return player;
    }

    @Override
    public String getType() {
        return type;
    }

    public String toString() {
        return type;
    }
}
