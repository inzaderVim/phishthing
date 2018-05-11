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
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT) // TODO
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SeekBar seekbar = (SeekBar) findViewById(R.id.seekBar);
        final ImageButton prevbutton = (ImageButton) findViewById(R.id.prevbutton);
        final ImageButton playbutton = (ImageButton) findViewById(R.id.playbutton);
        final ImageButton nextbutton = (ImageButton) findViewById(R.id.nextbutton);
        final TextView songtitle = (TextView) findViewById(R.id.songtitle);
        final TextView location = (TextView) findViewById(R.id.location);

        final WebView myWebView = (WebView) findViewById(R.id.webview);
        myWebView.setWebChromeClient(new WebChromeClient());
        myWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(final WebView view, String url) {
                view.evaluateJavascript("document.getElementsByTagName('header')[0].style.display='none';", null);
                view.evaluateJavascript("document.getElementById('main').style.marginTop='0px';", null);
                //view.evaluateJavascript("App.player.playNext();", null);
                //view.evaluateJavascript("App.player_view.togglePause();", null);
                //view.evaluateJavascript("App.player.play();", null);

                // set page title
                view.evaluateJavascript("App.router.currentView.pageTitle();", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String s) {
                        setTitle(s.substring(1, 12)); //TODO, 12 catches the date since its always mm/dd/yyyy
                    }
                });

                prevbutton.setOnClickListener(new View.OnClickListener()
                {
                    public void onClick(View v)
                    {
                        view.evaluateJavascript("App.player_view.playPrev();", null);
                    }
                });

                playbutton.setOnClickListener(new View.OnClickListener()
                {
                    public void onClick(View v)
                    {
                        view.evaluateJavascript("App.player_view.togglePause();", null);
                    }
                });

                nextbutton.setOnClickListener(new View.OnClickListener()
                {
                    public void onClick(View v)
                    {
                        view.evaluateJavascript("App.player_view.playNext();", null);
                    }
                });

            }

            // on url change, e.g. song change
            @Override
            public void onLoadResource(WebView view, String url) {
                // set seek bar duration, doesn't actually work
                view.evaluateJavascript("App.player.get('currentTrack').sound.duration;", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String s) {
                        try {
                            seekbar.setMax(Integer.parseInt(s));
                        } catch (NumberFormatException e) {}
                    }});

                // set song title
                view.evaluateJavascript("App.player.get('currentTrack').get('title');", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String s) {
                        if (s.length() > 0 && !s.equals("null"))
                            songtitle.setText(s.substring(1, s.length() - 1));
                        else
                            songtitle.setText("Nothing playing"); // TODO ref
                    }
                });

                // set location
                view.evaluateJavascript("App.player.get('playlist').show.get('location');", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String s) {
                        if (s.length() > 0 && !s.equals("null"))
                            location.setText(s.substring(1, s.length() - 1));
                        else
                            location.setText("-");
                    }
                });

                // hide player bar
                view.evaluateJavascript("document.getElementById('player').style.display='none';", null);
            }

            //App.player.get('currentTrack')
            //App.player_view.togglePause()
            //App.player.get("currentTrack").sound.paused
            //App.player.get("currentTrack").get("title")
            //App.player.get("currentTrack").sound.position
            //App.player.get("currentTrack").sound.setPosition(X)
            //App.player.get("currentTrack").sound.position
            //App.player.get("currentTrack").sound.duration
            //App.player.get("playlist").show.get("location")
            //App.player.get("playlist").show.get("show_date")
            //App.router.currentView.pageTitle()

        });

        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setAppCachePath(getApplicationContext().getCacheDir().getAbsolutePath());
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setDatabaseEnabled(true);

        myWebView.loadUrl("http://www.phishtracks.com/shows/1997-12-07");

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
                        try {
                            seekbar.setMax(Integer.parseInt(s));
                        } catch (NumberFormatException e) {}
                    }
                });
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                System.out.print("ok");
            }
        });
    }

}
