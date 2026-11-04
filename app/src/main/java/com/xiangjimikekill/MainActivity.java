package com.xiangjimikekill;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        layout.setBackgroundColor(Color.parseColor("#0A0A0A")); // 极致暗黑背景

        // 现代感圆角按钮背景 (开启)
        GradientDrawable bgOn = new GradientDrawable();
        bgOn.setColor(Color.parseColor("#d32f2f"));
        bgOn.setCornerRadius(45f);

        // 现代感圆角按钮背景 (关闭)
        GradientDrawable bgOff = new GradientDrawable();
        bgOff.setColor(Color.parseColor("#2e7d32"));
        bgOff.setCornerRadius(45f);

        TextView title = new TextView(this);
        title.setText("HARDWARE KILL-SWITCH");
        title.setTextColor(Color.parseColor("#555555"));
        title.setTextSize(18);
        title.setGravity(Gravity.CENTER);
        title.setPadding(0, 0, 0, 80);

        Button btnOn = new Button(this);
        btnOn.setText("⛔ 物理级拉闸 (切断)");
        btnOn.setTextSize(20);
        btnOn.setTextColor(Color.WHITE);
        btnOn.setBackground(bgOn);
        btnOn.setElevation(15f);
        btnOn.setPadding(0, 60, 0, 60);
        btnOn.setOnClickListener(v -> execRoot("echo 1 > /sys/kernel/cam_kill && echo 1 > /sys/kernel/mic_kill"));

        Button btnOff = new Button(this);
        btnOff.setText("✅ 恢复硬件 (通电)");
        btnOff.setTextSize(20);
        btnOff.setTextColor(Color.WHITE);
        btnOff.setBackground(bgOff);
        btnOff.setElevation(15f);
        btnOff.setPadding(0, 60, 0, 60);
        btnOff.setOnClickListener(v -> execRoot("echo 0 > /sys/kernel/cam_kill && echo 0 > /sys/kernel/mic_kill"));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(60, 40, 60, 40);

        layout.addView(title);
        layout.addView(btnOn, params);
        layout.addView(btnOff, params);
        
        setContentView(layout);
    }

    // 强化版底层指令执行，带完整错误捕获
    private void execRoot(String cmd) {
        try {
            // 使用更可靠的 su -c 调用方式
            Process p = Runtime.getRuntime().exec(new String[]{"su", "-c", cmd});
            p.waitFor();
            
            // 读取底层报错信息
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            String err = stdError.readLine();
            
            if (p.exitValue() == 0) {
                Toast.makeText(this, "⚡ 内核节点修改成功！", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "❌ 失败: " + (err != null ? err : "KernelSU 静默拒绝了 Root 权限！"), Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "⚠️ 执行异常: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
