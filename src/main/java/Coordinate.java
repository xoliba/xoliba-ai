/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author eerop, filip
 */
public class Coordinate {
    
    private int x, y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public boolean equals(Coordinate c){
        if(c == null){
            return false;
        }
        if(c.getClass() != this.getClass()){
            return false;
        }
        return this.x == c.x && this.y == c.y;
    }
}
