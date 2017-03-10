package sn.tab28.touba.mosquee;

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnBufferingUpdateListener;
import io.vov.vitamio.MediaPlayer.OnInfoListener;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author xadimouSALIH
 * @Link http://www.tab28.com
 */

public class ToubaMosqueeActivity extends Activity implements OnInfoListener,
		OnBufferingUpdateListener {
	AsyncTask<Void, Void, Void> mRegisterTask;
	AlertDialogManager alert = new AlertDialogManager();
	public static String name;
	public static String appname;
	public static String email;
	private String lienImg = new StringBuffer(CommonUtilities.var1).reverse()
			.toString()
			+ new StringBuffer(CommonUtilities.var2).reverse().toString()
			+ new StringBuffer(CommonUtilities.var3).reverse().toString()
			+ new StringBuffer(CommonUtilities.var4).reverse().toString()
			+ new StringBuffer(CommonUtilities.var5).reverse().toString();
	private Uri uri;
	private VideoView mVideoView;
	private ProgressBar pb;
	private TextView downloadRateView, loadRateView;

	@Override
	public void onBackPressed() {
		AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
				ToubaMosqueeActivity.this);
		alertDialog2.setTitle(R.string.app_name);
		alertDialog2.setMessage(R.string.app_exit_message);
		alertDialog2.setIcon(R.drawable.ic_launcher);
		alertDialog2.setPositiveButton(R.string.str_yes,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						System.exit(0);
					}
				});
		alertDialog2.setNegativeButton(R.string.str_no,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					}
				});
		alertDialog2.show();

	}

	public boolean isOnline() {
		try {
			ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netInfo = cm.getActiveNetworkInfo();
			if (netInfo != null && netInfo.isConnected()) {
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	@Override
	public void onCreate(Bundle icicle) {
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		super.onCreate(icicle);
		if (isOnline()) {

			if (!LibsChecker.checkVitamioLibs(this))
				return;
			setContentView(R.layout.mtlayout);
			mVideoView = (VideoView) findViewById(R.id.buffer);
			pb = (ProgressBar) findViewById(R.id.probar);
			downloadRateView = (TextView) findViewById(R.id.download_rate);
			loadRateView = (TextView) findViewById(R.id.load_rate);
			uri = Uri.parse(lienImg);
			mVideoView.setVideoURI(uri);
			mVideoView.setMediaController(new MediaController(this));
			mVideoView.requestFocus();
			mVideoView.setOnInfoListener(this);
			mVideoView.setOnBufferingUpdateListener(this);
			mVideoView
					.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
						@Override
						public void onPrepared(MediaPlayer mediaPlayer) {
							mediaPlayer.setPlaybackSpeed(1.0f);
						}
					});
		} else {
			Toast.makeText(
					ToubaMosqueeActivity.this,
					ToubaMosqueeActivity.this.getString(R.string.no_connection),
					Toast.LENGTH_LONG).show();
		}

	}

	@Override
	public boolean onInfo(MediaPlayer mp, int what, int extra) {
		switch (what) {
		case MediaPlayer.MEDIA_INFO_BUFFERING_START:
			if (mVideoView.isPlaying()) {
				mVideoView.pause();
				pb.setVisibility(View.VISIBLE);
				downloadRateView.setText("");
				loadRateView.setText("");
				downloadRateView.setVisibility(View.VISIBLE);
				loadRateView.setVisibility(View.VISIBLE);
			}
			break;
		case MediaPlayer.MEDIA_INFO_BUFFERING_END:
			mVideoView.start();
			mVideoView.setVideoLayout(VideoView.VIDEO_LAYOUT_ZOOM, 0);
			pb.setVisibility(View.GONE);
			downloadRateView.setVisibility(View.GONE);
			loadRateView.setVisibility(View.GONE);
			break;
		case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
			downloadRateView.setText("" + extra + "kb/s" + "  ");
			break;
		}
		return true;
	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		loadRateView.setText(percent + "%");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_about:
			openOptionsDialog();
			return true;
		case R.id.app_exit:
			exitOptionsDialog();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void exitOptionsDialog() {
		new AlertDialog.Builder(this)
				.setTitle(R.string.str_exit)
				.setMessage(R.string.app_exit_message)
				.setNegativeButton(R.string.str_no,
						new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface dialoginterface, int i) {
							}
						})
				.setPositiveButton(R.string.str_ok,
						new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface dialoginterface, int i) {
								System.exit(0);
							}
						}).show();
	}

	private void openOptionsDialog() {
		AboutDialog about = new AboutDialog(this);
		about.setTitle(Html.fromHtml(this.getString(R.string.app_about)));
		about.show();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
