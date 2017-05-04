package de.paul_woitaschek.exoplayerbug;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection.Factory;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;


public class MainActivity extends Activity {

  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    final ViewGroup container = (ViewGroup) this.findViewById(android.R.id.content);

    final SurfaceView surface = new SurfaceView(this);
    container.addView(surface);

    setupPlayer(surface);

    container.setOnClickListener(new OnClickListener() {
      public final void onClick(View it) {
        if (container.getChildCount() == 0) {
          // if there are no views, add the surface
          container.addView(surface);
        } else {
          // if the surface is attached, detach it
          container.removeView(surface);
        }
      }
    });
  }

  private void setupPlayer(SurfaceView surface) {
    Factory videoTrackSelectionFactory = new Factory(new DefaultBandwidthMeter());
    final DefaultTrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
    SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);

    DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(this, this.getPackageName());
    Uri dashUri = Uri.parse("http://www-itec.uni-klu.ac.at/ftp/datasets/DASHDataset2014/BigBuckBunny/15sec/BigBuckBunny_15s_simple_2014_05_09.mpd");
    DashMediaSource mediaSource = new DashMediaSource(dashUri, dataSourceFactory, new DefaultDashChunkSource.Factory(dataSourceFactory), null, null);
    player.prepare(mediaSource);

    player.setVideoSurfaceView(surface);
    player.setPlayWhenReady(true);
  }
}
