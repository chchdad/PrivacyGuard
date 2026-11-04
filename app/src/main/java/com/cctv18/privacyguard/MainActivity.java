package com.cctv18.privacyguard;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.graphics.Color;
import android.view.Gravity;
import java.io.DataOutputStream;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        layout.setBackgroundColor(Color.parseColor("#121212"));

        Button btnOn = new Button(this);
        btnOn.setText("🔴 开启物理拉闸 (切断音视频)");
        btnOn.setTextSize(20);
        btnOn.setTextColor(Color.WHITE);
        btnOn.setBackgroundColor(Color.parseColor("#b71c1c"));
        btnOn.setPadding(0, 50, 0, 50);
        btnOn.setOnClickListener(v -> execRoot("echo 1 > /sys/kernel/cam_kill && echo 1 > /sys/kernel/mic_kill"));

        Button btnOff = new Button(this);
        btnOff.setText("🟢 关闭拉闸 (恢复硬件)");
        btnOff.setTextSize(20);
        btnOff.setTextColor(Color.WHITE);
        btnOff.setBackgroundColor(Color.parseColor("#1b5e20"));
        btnOff.setPadding(0, 50, 0, 50);
        btnOff.setOnClickListener(v -> execRoot("echo 0 > /sys/kernel/cam_kill && echo 0 > /sys/kernel/mic_kill"));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(50, 50, 50, 50);

        layout.addView(btnOn, params);
        layout.addView(btnOff, params);
        
        setContentView(layout);
    }

    private void execRoot(String cmd) {
        try {
            Process p = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(p.getOutputStream());
            os.writeBytes(cmd + "\n");
            os.writeBytes("exit\n");
            os.flush();
            p.waitFor();
            Toast.makeText(this, "内核指令下发成功！", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "需要 Root 权限", Toast.LENGTH_LONG).show();
        }
    }
}
