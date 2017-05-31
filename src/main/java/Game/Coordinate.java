package Game;

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
    
    public int x, y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    @Override
    public boolean equals(Object o){
        if(o == null || o.getClass() != this.getClass()){
            return false;
        } else if (this.hashCode() == o.hashCode()){
            return true;
        }
        return false;
        
    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash = 97 * hash + this.x;
        hash = 97 * hash + this.y;
        return hash;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
