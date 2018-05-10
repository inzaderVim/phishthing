package tarakjian.mike.phishthing;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;
import android.widget.ImageButton;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT) // TODO
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ImageButton playbutton = (ImageButton) findViewById(R.id.playbutton);
        final SeekBar seekbar = (SeekBar) findViewById(R.id.seekBar);

        final WebView myWebView = (WebView) findViewById(R.id.webview);
        myWebView.setWebChromeClient(new WebChromeClient());
        myWebView.setWebViewClient(new WebViewClient() {

            public void onPageFinished(final WebView view, String url) {
                view.evaluateJavascript("document.getElementsByTagName('header')[0].style.display='none';", null);
                view.evaluateJavascript("document.getElementById('main').style.marginTop='0px';", null);
                view.evaluateJavascript("App.player.playNext();", null);
                view.evaluateJavascript("App.player_view.togglePause();", null);
                //view.evaluateJavascript("App.player.play();", null);

                view.evaluateJavascript("App.router.currentView.pageTitle();", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String s) {
                        setTitle(s.substring(1, s.length() - 2));
                    }
                });

                playbutton.setOnClickListener(new View.OnClickListener()
                {
                    public void onClick(View v)
                    {
                        view.evaluateJavascript("App.player_view.togglePause();", null);
                    }
                });

            }

            //App.player.get('currentTrack')
            //App.player_view.togglePause()
            //App.player.get("currentTrack").sound.paused
            //App.player.get("currentTrack").get("title")
            //App.player.get("currentTrack").sound.position
            //App.player.get("currentTrack").sound.setPosition(X)
            //App.player.get("currentTrack").sound.position
            //App.player.get("currentTrack").sound.duration
            //

        });

        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setAppCachePath(getApplicationContext().getCacheDir().getAbsolutePath());
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setDatabaseEnabled(true);

        myWebView.loadUrl("http://www.phishtracks.com/shows/2010-08-09");

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                myWebView.evaluateJavascript("App.player.get('currentTrack').sound.setPosition(" + Integer.toString(progress) + ");", null);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                myWebView.evaluateJavascript("App.player.get('currentTrack').sound.duration;", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String s) {
                        seekbar.setMax(Integer.parseInt(s));
                    }});
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                System.out.print("ok");
            }
        });
    }


}
