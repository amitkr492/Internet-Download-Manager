package Mymetadata;

import java.util.ArrayList;
import java.util.HashMap;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author amit
 */


class MyMetadat
{
    public static int x;
    public static int y;
    public int getX() { return this.x; }
    public int getY() { return this.y; }
    public void setX(int newX) { this.x = x; }
    public void setY(int newY) { this.y = y; }
   // MyMetadata(int x, int y) { this.x = x; this.y = y; }

    static final private MyMetadat INSTANCE = new MyMetadat();
    static public MyMetadat getInstance() { return INSTANCE; }
    
    
    
    
}