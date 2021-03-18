import java.util.ArrayList; 
 
public class BetterAI implements IOthelloAI { 
   /**
    * This class is an AI that can be used for either player 1 or player 2 in a game of Othello. It uses a minimax algorithm to look a certain number of moves ahead, 
    * check the score at that point, and return that value. It will, using recursion, constantly compare the score of all possible moves to each other, and choose the
    * move that results in Max having the highest number of tokens.
    *
    * @param s The current state of the game. Where are the tokens, what colour are they.
    * @param depth How many moves ahead should it look? Can be set by the user.
    * @param betterAIcolour The colour of BetterAI. White or black. Will always look at that colours tokens. 
    * @param isMaxPlayer Boolean to check if its max player or min player at this level in the tree. !!Alternates as depth increments!!.  
    * @param alpha For pruning purposes. 
    * @param beta For pruning purposes.
    * @return
    * @author Ethan Stocks 
    */

    public Position decideMove (GameState s) { 
       
        ArrayList<Position> moves = s.legalMoves();         // Checks all available moves
        if (moves.isEmpty()) {                              // If none available, change player
            return new Position(-1,-1);
        }
        if (moves.size()==1) {
            return moves.get(0);                            // If 1 available, return that move. No point in wasting time looking moves ahead comparing moves, if you only have 1.
        }

        else {                                              // If there are available moves, feed each into MiniMax, and return move with the highest utility for BetterAI. 
            int betterAIcolour;
            if (s.getPlayerInTurn()==1) {
                betterAIcolour=0;
            }
            else {
                betterAIcolour=1;
            }
            Position bestMove = null;          
            int maxEval = Integer.MIN_VALUE;   
            int depth = 5;                     // set depth here. Gets funky above 5.
            int alpha = Integer.MIN_VALUE;
            int beta = Integer.MAX_VALUE;
            for (Position move:moves) {
                GameState newState = new GameState(s.getBoard(), s.getPlayerInTurn());
                newState.insertToken(move);
                int letsGoo = MiniMax(newState, depth, betterAIcolour, false, alpha, beta);
                if (letsGoo>maxEval) {
                    bestMove=move;
                }
            } 
            return bestMove; 
        }      
    } 
  
    public int MiniMax(GameState s, int depth, int betterAIcolour, boolean isMaxPlayer, int alpha, int beta){ 
        ArrayList<Position> moves = s.legalMoves();      
        if (s.isFinished() || depth == 0) {          // check if game is over, or if at leaves of tree. 
            int[] tokens = s.countTokens();
            if (betterAIcolour==0) {
                return tokens[0] -= tokens[1];
            }
            else {
                return tokens[1] -= tokens[0];
            }    
        } 
        if (moves.isEmpty()) {                       // check if just current player has no moves. Can be true without game being over.
            int[] tokens = s.countTokens();    
            if (betterAIcolour==0) {
                return tokens[0] -= tokens[1];
            }
            else {
                return tokens[1] -= tokens[0];
            } 
        }        
        if (isMaxPlayer) {                           // if max node, begin recursion process with child nodes being min
            int maxEval = Integer.MIN_VALUE;
            for (Position move : moves) {
                GameState newState = new GameState(s.getBoard(), s.getPlayerInTurn());
                newState.insertToken(move);
                int eval=MiniMax(newState, depth-1, betterAIcolour, false, alpha, beta);
                maxEval = Math.max(maxEval, eval);
                alpha=Math.max(alpha,eval);
                if (beta<=alpha) {
                    break;
                }
            } 
            return maxEval;
        }
        else {                                       // if min node, begin recursion process with child nodes being max
            int minEval = Integer.MAX_VALUE;            
            for (Position move : moves) { 
                GameState newState = new GameState(s.getBoard(), s.getPlayerInTurn());
                newState.insertToken(move);
                int eval=MiniMax(newState, depth-1, betterAIcolour, true, alpha, beta);
                minEval=Math.min(minEval, eval);
                beta = Math.min(beta,eval);
                if (beta <= alpha) {
                    break;
                }
            } 
            return minEval;  
        }    
    } 
}