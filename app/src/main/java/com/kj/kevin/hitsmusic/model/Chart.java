package com.kj.kevin.hitsmusic.model;

/**
 * Created by Kevinkj_Lin on 2018/5/7.
 */

public class Chart {
    private String id;
    private String title;
    private String description;
    private String imgUrl;
    private String chartUrl;

    public Chart(String id, String title, String description, String imgUrl, String chartUrl) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imgUrl = imgUrl;
        this.chartUrl = chartUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getChartUrl() {
        return chartUrl;
    }

    public void setChartUrl(String chartUrl) {
        this.chartUrl = chartUrl;
    }
}
