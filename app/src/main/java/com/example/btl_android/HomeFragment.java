package com.example.btl_android;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private EditText edtSearchService;
    private RecyclerView rvSearchResults;
    private LinearLayout layoutMainContent;
    private LinearLayout layoutSpecialist, layoutGeneralCheckup, layoutDentistry,
            layoutCardiology, layoutDermatology, layoutOphthalmology, layoutTest;

    private View layoutNews;
    private View layoutNewsContent;

    private SearchAdapter searchAdapter;
    private List<ServiceItem> allServices;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Ánh xạ View
        edtSearchService = view.findViewById(R.id.edtSearchService);
        rvSearchResults = view.findViewById(R.id.rvSearchResults);
        layoutMainContent = view.findViewById(R.id.layoutMainContent);

        // Các view dịch vụ ở màn hình chính
        layoutSpecialist = view.findViewById(R.id.layoutSpecialist);
        layoutGeneralCheckup = view.findViewById(R.id.layoutGeneralCheckup);
        layoutDentistry = view.findViewById(R.id.layoutDentistry);
        layoutCardiology = view.findViewById(R.id.layoutCardiology);
        layoutDermatology = view.findViewById(R.id.layoutDermatology);
        layoutOphthalmology = view.findViewById(R.id.layoutOphthalmology);
        layoutTest = view.findViewById(R.id.layoutTest);

        // BỔ SUNG: Ánh xạ nút Tin tức (Giả sử ID trong file XML của bạn là layoutNews)
        layoutNews = view.findViewById(R.id.layoutNews);
        layoutNewsContent = view.findViewById(R.id.layoutNewsContent);

        // 2. Gán sự kiện click cho lưới dịch vụ
        layoutSpecialist.setOnClickListener(this);
        layoutGeneralCheckup.setOnClickListener(this);
        layoutDentistry.setOnClickListener(this);
        layoutCardiology.setOnClickListener(this);
        layoutDermatology.setOnClickListener(this);
        layoutOphthalmology.setOnClickListener(this);
        layoutTest.setOnClickListener(this);

        // BỔ SUNG: Gán sự kiện click cho nút Tin tức
        if (layoutNews != null) {
            layoutNews.setOnClickListener(this);
        }
        if (layoutNewsContent != null) {
            layoutNewsContent.setOnClickListener(this);
        }

        // 3. Khởi tạo danh sách toàn bộ dịch vụ cho tính năng tìm kiếm
        initServiceList();

        // Cài đặt RecyclerView cho kết quả tìm kiếm
        rvSearchResults.setLayoutManager(new LinearLayoutManager(getContext()));
        searchAdapter = new SearchAdapter(new ArrayList<>());
        rvSearchResults.setAdapter(searchAdapter);

        // 4. Lắng nghe sự thay đổi trên thanh tìm kiếm
        edtSearchService.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String keyword = s.toString().trim();
                if (keyword.isEmpty()) {
                    // Nếu ô tìm kiếm trống: Ẩn danh sách kết quả, Hiện lại lưới màn hình chính
                    rvSearchResults.setVisibility(View.GONE);
                    layoutMainContent.setVisibility(View.VISIBLE);
                } else {
                    // Nếu có nhập chữ: Ẩn màn hình chính, Hiện danh sách kết quả
                    rvSearchResults.setVisibility(View.VISIBLE);
                    layoutMainContent.setVisibility(View.GONE);
                    filterSearchList(keyword);
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
    }

    // Khởi tạo data dịch vụ
    private void initServiceList() {
        allServices = new ArrayList<>();
        allServices.add(new ServiceItem("Khám chuyên khoa", "Dịch vụ khám và tư vấn với các bác sĩ chuyên khoa hàng đầu...", R.drawable.ic_stethoscope));
        allServices.add(new ServiceItem("Khám tổng quát", "Dịch vụ khám sức khỏe tổng quát giúp đánh giá toàn diện...", R.drawable.ic_general_checkup));
        allServices.add(new ServiceItem("Khám nha khoa", "Chăm sóc sức khỏe răng miệng toàn diện...", R.drawable.ic_dentistry));
        allServices.add(new ServiceItem("Khám tim mạch", "Tầm soát và điều trị các bệnh lý liên quan đến tim mạch...", R.drawable.ic_cardiology));
        allServices.add(new ServiceItem("Khám da liễu", "Điều trị các bệnh về da, tóc, móng và các vấn đề thẩm mỹ da.", R.drawable.ic_dermatology));
        allServices.add(new ServiceItem("Khám nhãn khoa", "Kiểm tra thị lực, đo khúc xạ và điều trị các bệnh về mắt.", R.drawable.ic_ophthalmology));
        allServices.add(new ServiceItem("Xét nghiệm", "Cung cấp các dịch vụ xét nghiệm máu, nước tiểu...", R.drawable.ic_test));
    }

    // Hàm lọc danh sách hiển thị lên RecyclerView
    private void filterSearchList(String keyword) {
        List<ServiceItem> filteredList = new ArrayList<>();
        String normalizedKeyword = removeAccents(keyword.toLowerCase());

        for (ServiceItem item : allServices) {
            String normalizedTitle = removeAccents(item.getTitle().toLowerCase());
            if (normalizedTitle.contains(normalizedKeyword)) {
                filteredList.add(item);
            }
        }
        searchAdapter.updateList(filteredList);
    }

    // Hàm chuẩn hóa chuỗi, xóa dấu tiếng Việt
    private String removeAccents(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("").replace('đ', 'd').replace('Đ', 'D');
    }

    // Xử lý Click trên Lưới dịch vụ mặc định (Grid)
    @Override
    public void onClick(View v) {
        int viewId = v.getId();

        if (viewId == R.id.layoutNews || viewId == R.id.layoutNewsContent) {
            if (!isAdded() || getActivity() == null) {
                return;
            }
            Intent intent = new Intent(requireActivity(), NewsActivity.class);
            startActivity(intent);
            return;
        }

        String title = "";
        String description = "";

        if (viewId == R.id.layoutSpecialist) {
            title = allServices.get(0).getTitle();
            description = allServices.get(0).getDescription();
        } else if (viewId == R.id.layoutGeneralCheckup) {
            title = allServices.get(1).getTitle();
            description = allServices.get(1).getDescription();
        } else if (viewId == R.id.layoutDentistry) {
            title = allServices.get(2).getTitle();
            description = allServices.get(2).getDescription();
        } else if (viewId == R.id.layoutCardiology) {
            title = allServices.get(3).getTitle();
            description = allServices.get(3).getDescription();
        } else if (viewId == R.id.layoutDermatology) {
            title = allServices.get(4).getTitle();
            description = allServices.get(4).getDescription();
        } else if (viewId == R.id.layoutOphthalmology) {
            title = allServices.get(5).getTitle();
            description = allServices.get(5).getDescription();
        } else if (viewId == R.id.layoutTest) {
            title = allServices.get(6).getTitle();
            description = allServices.get(6).getDescription();
        }

        // Nếu title có dữ liệu (tức là đã click vào 1 trong các dịch vụ) thì mới chuyển trang
        if (!title.isEmpty()) {
            navigateToDetail(title, description);
        }
    }

    // Hàm chuyển tới màn hình Chi tiết dịch vụ
    private void navigateToDetail(String title, String description) {
        if (!isAdded() || getActivity() == null) {
            return;
        }

        try {
            Intent intent = new Intent(requireActivity(), ServiceDetailActivity.class);
            intent.putExtra("SERVICE_TITLE", title);
            intent.putExtra("SERVICE_DESCRIPTION", description);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(requireContext(), "Không thể mở chi tiết dịch vụ", Toast.LENGTH_SHORT).show();
        }
    }

    // 1. Đối tượng lưu trữ thông tin dịch vụ
    private static class ServiceItem {
        private String title;
        private String description;
        private int iconResId;

        public ServiceItem(String title, String description, int iconResId) {
            this.title = title;
            this.description = description;
            this.iconResId = iconResId;
        }

        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public int getIconResId() { return iconResId; }
    }

    // 2. Adapter kết nối dữ liệu lên RecyclerView
    private class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
        private List<ServiceItem> list;

        public SearchAdapter(List<ServiceItem> list) {
            this.list = list;
        }

        public void updateList(List<ServiceItem> newList) {
            this.list = newList;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_service, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            ServiceItem item = list.get(position);
            holder.tvTitle.setText(item.getTitle());
            holder.imgIcon.setImageResource(item.getIconResId());

            // Bắt sự kiện click vào dòng kết quả tìm kiếm
            holder.itemView.setOnClickListener(v -> {
                navigateToDetail(item.getTitle(), item.getDescription());
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvTitle;
            ImageView imgIcon;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvTitle = itemView.findViewById(R.id.tvSearchTitle);
                imgIcon = itemView.findViewById(R.id.imgSearchIcon);
            }
        }
    }
}