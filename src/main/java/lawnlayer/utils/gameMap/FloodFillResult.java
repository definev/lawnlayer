package lawnlayer.utils;

import java.util.ArrayList;

public class FloodFillResult {
    public FloodFillResult(boolean isSuccess, ArrayList<ArrayList<Character>> result) {
        this.isSuccess = isSuccess;
        this.result = result;
    }
    public boolean isSuccess;
    public ArrayList<ArrayList<Character>> result;
}
