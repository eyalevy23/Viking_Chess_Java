# VIKING CHESS

## Description
This project is a Java implementation of the classic game of Viking Chess. It includes classes for the game logic, player management, and board representation. The game follows standard Checkers rules, including piece movement, capturing, and win conditions.

## Features
- Board Representation: The game board is represented as an 11x11 grid, with pieces placed according to the rules of Checkers.
- Player Management: The game supports two players, with each player controlling their own set of pieces.
- Move Validation: Moves are validated according to the rules of Checkers, ensuring that players can only make legal moves.
- Piece Capturing: Pieces can capture opponent pieces by cornering opponent pieces.
- Win Condition: The game checks for win conditions, with the first player to either move their king piece to one of the corners of the board or surrounded opponent king with their own pawns..
- Undo Last Move: Players can undo their last move using the `undoLastMove()` method in the `GameLogic` class.
- Reset Game: The game can be reset to its initial state using the `reset()` method in the `GameLogic` class.

## Classes
- `GameLogic`: Implements the game logic, including turn management, move execution, win condition checking, and resetting the game.
- `Board`: Represents the game board and manages piece placement, move validation, capturing, and win conditions.
- `ConcretePiece`: Abstract class representing a concrete piece on the board, such as a king or a pawn.
- `ConcretePlayer`: Represents a player in the game, tracking their player number and number of wins.
- `King`: Subclass of `ConcretePiece` representing the king piece.
- `Pawn`: Subclass of `ConcretePiece` representing the pawn piece.
- `Position`: Represents a position on the game board, with x and y coordinates.

## Usage
1. Run the `main` class to start the game.
2. Players take turns making moves by specifying the position of the piece they want to move and the position they want to move it to.
3. The game validates the move and executes it if it is legal.
4. The game continues until one player wins or the game is reset.

