package com.example.kiddo.Model;

public class Point {
    String childId;
    int pointsNumber;


    public Point(String childId, int pointsNumber) {
        this.childId = childId;
        this.pointsNumber = pointsNumber;
    }

    public int getPointsNumber() {
        return pointsNumber;
    }

    public String getChildId() {
        return childId;
    }
}
