package com.mobiotsec.goingnative;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    TextView mResultWidget = null;

    public native int checkFlag(String str);

    static {
        System.loadLibrary("goingnative");
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0096R.layout.activity_main);
        final EditText flagWidget = (EditText) findViewById(C0096R.C0099id.flag);
        final TextView resultWidget = (TextView) findViewById(C0096R.C0099id.result);
        this.mResultWidget = resultWidget;
        flagWidget.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                MainActivity.this.mResultWidget.setText("");
            }

            public void afterTextChanged(Editable s) {
            }
        });
        ((Button) findViewById(C0096R.C0099id.checkflag)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                resultWidget.setText(MainActivity.this.splitFlag(flagWidget.getText().toString()));
            }
        });
    }

    public String splitFlag(String flag) {
        if (!flag.startsWith("FLAG{") || !flag.endsWith("}")) {
            return "Invalid flag";
        }
        String flag2 = flag.replace("FLAG{", "").replace("}", "");
        if (flag2.length() != 15 || checkFlag(flag2) == -1) {
            return "Invalid flag";
        }
        return "Correct flag!";
    }
}
