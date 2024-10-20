package com.example.kiddo.parent;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kiddo.Model.Child;
import com.example.kiddo.R;

import java.util.List;

public class ChildAdapter extends ArrayAdapter<Child> {
    private final Context context;
    private final List<Child> children;

    public ChildAdapter(Context context, List<Child> children) {
        super(context, R.layout.list_item_child, children);
        this.context = context;
        this.children = children;
    }

    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item_child, parent, false);
        }

        Child child = children.get(position);
        Button button = view.findViewById(R.id.button_child);

        // تعيين اسم الطفل
        button.setText(child.getUsername());

        // تعيين OnClickListener للزر
        button.setOnClickListener(v -> {
            Toast.makeText(context, "عرض تفاصيل " + child.getUsername(), Toast.LENGTH_SHORT).show();
            // هنا يمكن إضافة الكود لانتقال إلى تفاصيل الطفل
            Intent intent = new Intent(this.getContext(), ChildActivity.class);
            intent.putExtra("child_id", child.getId()); // تمرير معرف الطفل أو بيانات أخرى

            context.startActivity(intent);
        });

        return view;
    }
}