package sg.mas.ibizzy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private RingsView ringsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);;
        setContentView(R.layout.activity_main);
        ringsView = (RingsView) findViewById(R.id.ringsView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ringsView.startRingsAnimation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ringsView.stopRingsAnimation();
    }
}
