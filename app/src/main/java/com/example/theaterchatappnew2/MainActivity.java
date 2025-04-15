package com.example.theaterchatappnew2;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private EditText messageInput;
    private Button sendButton;
    private TextView chatMessages;
    private ScrollView scrollView;  // Add ScrollView reference

    private final OkHttpClient client = new OkHttpClient();
    private final String BASE_URL = "http://10.0.2.2:5000/chatbot";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        messageInput = findViewById(R.id.messageInput);
        sendButton = findViewById(R.id.sendButton);
        chatMessages = findViewById(R.id.chatMessages);
        scrollView = findViewById(R.id.scrollView);  // Connect to layout

        sendButton.setOnClickListener(v -> {
            String userMessage = messageInput.getText().toString().trim();
            if (!userMessage.isEmpty()) {
                appendChat("You: " + userMessage);
                sendMessageToBackend(userMessage);
                messageInput.setText("");
            }
        });
    }

    private void sendMessageToBackend(String message) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        String json = "{\"message\":\"" + message + "\"}";
        RequestBody body = RequestBody.create(json, JSON);

        Request request = new Request.Builder()
                .url(BASE_URL)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> appendChat("Error: " + e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String reply = response.isSuccessful()
                        ? parseBotReply(response.body().string())
                        : "Server error";

                runOnUiThread(() -> appendChat("Bot: " + reply));
            }
        });
    }

    private String parseBotReply(String json) {
        try {
            JSONObject obj = new JSONObject(json);
            return obj.getString("reply");
        } catch (Exception e) {
            return "Invalid response";
        }
    }

    //  Helper method to update chat + scroll down
    private void appendChat(String message) {
        chatMessages.append("\n" + message);
        scrollView.post(() -> scrollView.fullScroll(ScrollView.FOCUS_DOWN));
    }
}


