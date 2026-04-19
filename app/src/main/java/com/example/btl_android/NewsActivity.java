package com.example.btl_android;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_android.Adapter.NewsAdapter;
import com.example.btl_android.Object.NewsItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NewsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewNews;
    private NewsAdapter newsAdapter;
    private List<NewsItem> newsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        //Back
        com.google.android.material.appbar.MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                finish();
            }
        });
        // 1. Ánh xạ View
        recyclerViewNews = findViewById(R.id.recyclerViewNews);
        recyclerViewNews.setLayoutManager(new LinearLayoutManager(this));

        // 2. Khởi tạo danh sách và Adapter
        newsList = new ArrayList<>();
        newsAdapter = new NewsAdapter(this, newsList);
        recyclerViewNews.setAdapter(newsAdapter);

        // 3. Gọi hàm tải tin tức từ RSS VNExpress (Chuyên mục Sức khỏe)
        fetchRssNews("https://vnexpress.net/rss/suc-khoe.rss");
    }

    private void fetchRssNews(String rssUrl) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    // Thêm User-Agent giả lập trình duyệt và Timeout 15 giây
                    Document doc = Jsoup.connect(rssUrl)
                            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                            .timeout(15000)
                            .get();

                    Elements items = doc.select("item");

                    for (Element item : items) {
                        String title = item.select("title").text();
                        String link = item.select("link").text();
                        String pubDate = item.select("pubDate").text();
                        String description = item.select("description").text();

                        Document docDescription = Jsoup.parse(description);
                        Element imgElement = docDescription.select("img").first();

                        String imageUrl = "";
                        if (imgElement != null) {
                            imageUrl = imgElement.attr("src");
                        }

                        newsList.add(new NewsItem(title, link, imageUrl, pubDate));
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            newsAdapter.notifyDataSetChanged();
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            // In lỗi chi tiết ra Logcat để dễ debug nếu vẫn không chạy
                            android.util.Log.e("NewsActivity", "Lỗi tải tin tức: " + e.getMessage());
                            Toast.makeText(NewsActivity.this, "Lỗi tải tin tức: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }
}