package com.example.btl_android.ai;

import com.example.btl_android.BuildConfig;

import org.json.JSONArray;
import org.json.JSONObject;
import okhttp3.*;

public class GroqAIService {

    private static final String API_KEY = BuildConfig.GROQ_API_KEY;

    private static final String ENDPOINT =
            "https://api.groq.com/openai/v1/chat/completions";

    public static String analyzeMedicalRecord(
            String reason,
            String before,
            String after,
            String diagnosis
    ) throws Exception {

        if (API_KEY == null || API_KEY.isBlank()) {
            return "Thiếu GROQ_API_KEY. Hãy thêm key vào file local.properties (không commit file này).";
        }

        OkHttpClient client = new OkHttpClient();

        String prompt =
                "Bạn là bác sĩ chuyên khoa.\n\n"
                        + "Phân tích hồ sơ bệnh án sau:\n"
                        + "Nguyên nhân: " + reason + "\n"
                        + "Trước điều trị: " + before + "\n"
                        + "Sau điều trị: " + after + "\n"
                        + "Chẩn đoán: " + diagnosis + "\n\n"
                        + "1. Đánh giá mức độ nguy cơ\n"
                        + "2. Giải thích ngắn gọn\n"
                        + "3. Đưa ra khuyến nghị điều trị";

        JSONObject json = new JSONObject();


        json.put("model", "llama-3.1-8b-instant");

        JSONArray messages = new JSONArray();

        JSONObject message = new JSONObject();
        message.put("role", "user");
        message.put("content", prompt);

        messages.put(message);
        json.put("messages", messages);

        RequestBody body = RequestBody.create(
                json.toString(),
                MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url(ENDPOINT)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        Response response = client.newCall(request).execute();

        String responseBody = response.body() != null
                ? response.body().string()
                : "";

        if (!response.isSuccessful()) {
            return "HTTP Error: " + response.code() + "\n" + responseBody;
        }

        JSONObject result = new JSONObject(responseBody);

        if (result.has("error")) {
            return "AI Error:\n" + result.getJSONObject("error").toString(2);
        }

        if (result.has("choices")) {
            return result
                    .getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content");
        }

        return "AI trả về dữ liệu không hợp lệ:\n" + responseBody;
    }
}