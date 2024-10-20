package com.example.kiddo.Model;

import java.util.Map;

public class TaskInfo {
    private String taskName;
    private String date;
    private String imageUrl; // رابط الصورة
    private int points; // النقاط
    private boolean isCompleted; // حالة المهمة

    public TaskInfo(String taskName, String date, String imageUrl, int points, boolean isCompleted) {
        this.taskName = taskName;
        this.date = date;
        this.imageUrl = imageUrl;
        this.points = points;
        this.isCompleted = isCompleted;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    // getters
    public String getTaskName() { return taskName; }
    public String getDate() { return date; }
    public String getImageUrl() { return imageUrl; }
    public int getPoints() { return points; }
    public boolean isCompleted() { return isCompleted; }

    public static TaskInfo fromMap(Map<String, Object> data) {
        String taskName = (String) data.get("taskName");
        String date = (String) data.get("date");
        String imageUrl = (String) data.get("imageUrl");
      //  int points = ((Long) data.get("points")).intValue(); // تحويل Long إلى int
       // boolean isCompleted = (Boolean) data.get("isCompleted");

        return new TaskInfo(taskName, date, imageUrl, 0, true);
    }
}