public class ConcretePlayer implements Player{
    int player;
    int wins;

    ConcretePlayer(int player) {
        this.player = player;
        this.wins = 0;
    }

    @Override
    public boolean isPlayerOne() {
        if(player == 1){
            return true;
        }
        return false;
    }

    public void playerWin() {
        wins++;
    }

    @Override
    public int getWins() {
        return wins;
    }
}
