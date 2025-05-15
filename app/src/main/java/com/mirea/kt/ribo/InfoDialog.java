package com.mirea.kt.ribo;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class InfoDialog {
    public static void showDialog(Context context, String message) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_info, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.show();

        TextView detected_text = dialogView.findViewById(R.id.detected_text);
        detected_text.setText(message);

        ImageButton shareBtn = dialogView.findViewById(R.id.share_button);
        shareBtn.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, message);

            if (shareIntent.resolveActivity(context.getPackageManager()) != null) {
                startActivity(context,Intent.createChooser(shareIntent, "Поделиться через"), null);
            } else {
                Toast.makeText(context, "Нет приложений для отправки", Toast.LENGTH_SHORT).show();
            }
        });

        Button positiveBtn = dialogView.findViewById(R.id.positive_btn);
        positiveBtn.setOnClickListener(v -> dialog.dismiss());

    }
}
