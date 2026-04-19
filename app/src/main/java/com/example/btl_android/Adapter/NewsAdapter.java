package com.example.btl_android.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.btl_android.Object.NewsItem;
import com.example.btl_android.R;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private Context context;
    private List<NewsItem> newsList;

    public NewsAdapter(Context context, List<NewsItem> newsList) {
        this.context = context;
        this.newsList = newsList;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Nạp file giao diện item_news.xml bạn vừa tạo ở Bước 3
        View view = LayoutInflater.from(context).inflate(R.layout.item_news, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        NewsItem news = newsList.get(position);

        // Hiển thị chữ: Tiêu đề và Ngày đăng
        holder.tvTitle.setText(news.getTitle());
        holder.tvDate.setText(news.getPubDate());

        // Sử dụng thư viện Glide để tải ảnh từ mạng (imageUrl) và gắn vào ImageView
        Glide.with(context)
                .load(news.getImageUrl())
                .placeholder(R.drawable.ic_launcher_background) // Ảnh mặc định trong lúc chờ tải
                .error(R.drawable.ic_launcher_background)       // Ảnh mặc định nếu tải lỗi
                .into(holder.imgThumbnail);

        // Bắt sự kiện khi người dùng click vào 1 bài báo
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sử dụng Intent để mở link bài báo bằng trình duyệt mặc định của máy
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(news.getLink()));
                context.startActivity(browserIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (newsList != null) {
            return newsList.size();
        }
        return 0;
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDate;
        ImageView imgThumbnail;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvNewsTitle);
            tvDate = itemView.findViewById(R.id.tvNewsDate);
            imgThumbnail = itemView.findViewById(R.id.imgNewsThumbnail);
        }
    }
}