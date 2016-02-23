package com.tv.ui.metro.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by liuhuadong on 9/23/14.
 */
public class Block<T>  extends DisplayItem implements Serializable {
    private static final long serialVersionUID = 2L;
    public Filter              filters;
    public ArrayList<T>        items;
    public ArrayList<Block<T>> blocks;
    public ArrayList<Block<T>> footers;

    public String toString(){
        return "\n\nBlock: " + super.toString() +" \n\titems:"+items + "\n\tend\n\n\n";
    }
}

