package tarakjian.mike.phishthing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button gobutton = (Button) findViewById(R.id.showdategobutton);
    }

    public void startWebplayer(View view) {
       Intent intent = new Intent(this, WebPlayer.class);
       EditText showdateform = (EditText) findViewById(R.id.showdateform);
       String showdate = showdateform.getText().toString();
       intent.putExtra("showdate", showdate);
       startActivity(intent);
    }

}