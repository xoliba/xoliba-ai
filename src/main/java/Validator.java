/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author eerop
 */
public class Validator {
    
    private int[][] board;

    public void updateBoard(int[][] board) {
        this.board = board;
    }

    public int[] getPossibleMoves(){
        return new int[3];
    }
    
    
}
