package com.example.btl_android.Object;

public class NewsItem {
    private String title;      // Tiêu đề bài báo
    private String link;
    private String imageUrl;
    private String pubDate;

    public NewsItem(String title, String link, String imageUrl, String pubDate) {
        this.title = title;
        this.link = link;
        this.imageUrl = imageUrl;
        this.pubDate = pubDate;
    }

    // Các hàm Getter để lấy dữ liệu ra
    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getPubDate() {
        return pubDate;
    }

    // Các hàm Setter (nếu sau này cần thay đổi dữ liệu)
    public void setTitle(String title) {
        this.title = title;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }
}