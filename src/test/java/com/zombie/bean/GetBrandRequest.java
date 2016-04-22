package com.zombie.bean;

import java.util.List;

/**
 *
 */

public class GetBrandRequest {
    private List<String> brand;
    private int start;
    private int end;

    public List<String> getBrand() {
        return brand;
    }

    public void setBrand(List<String> brand) {
        this.brand = brand;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }
}
