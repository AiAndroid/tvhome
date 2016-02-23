package com.tv.ui.metro.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by liuhuadonbg on 1/24/15.
 */
public class VideoBlocks<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    public ArrayList<T> blocks;

    public String toString(){
        return "\n\nVideoBlocks: blocks:"+blocks + "\n\tend\n\n\n";
    }
}