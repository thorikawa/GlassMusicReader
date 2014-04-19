package com.polysfactory.glassmusicreader;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.glass.widget.CardScrollView;
import com.polysfactory.headgesturedetector.HeadGestureDetector;
import com.polysfactory.headgesturedetector.OnHeadGestureListener;
import com.polysfactory.lib.guitarparty.GuitarpartyClient;
import com.polysfactory.lib.guitarparty.GuitarpartyService;
import com.polysfactory.lib.guitarparty.entity.Song;

public class MainActivity extends Activity implements OnHeadGestureListener, LoaderCallbacks<Song> {

	private static final String QUERY = "query";
	private HeadGestureDetector mHeadGestureDetector;

	private CardScrollView mCardScrollView;
	private ListCardAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		mHeadGestureDetector = new HeadGestureDetector(this);
		mHeadGestureDetector.setOnHeadGestureListener(this);

		mCardScrollView = new CardScrollView(this);
		setContentView(mCardScrollView);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mHeadGestureDetector.start();

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			mCardScrollView.deactivate();
			List<String> voiceResults = bundle.getStringArrayList(RecognizerIntent.EXTRA_RESULTS);
			if (voiceResults.isEmpty()) {
				Toast.makeText(this, "No song to display", Toast.LENGTH_LONG).show();
				return;
			}
			String query = voiceResults.get(0);
			Bundle args = new Bundle();
			args.putString(QUERY, query);
			getLoaderManager().initLoader(0, args, this);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		mHeadGestureDetector.stop();
	}

	private void prev() {
		if (mAdapter == null) {
			return;
		}
		int index = mCardScrollView.getSelectedItemPosition() - 1;
		if (index < 0) {
			index = 0;
		}
		updateText(index);
	}

	private void next() {
		if (mAdapter == null) {
			return;
		}
		int index = mCardScrollView.getSelectedItemPosition() + 1;
		if (index >= mAdapter.getCount()) {
			index = mAdapter.getCount() - 1;
		}
		updateText(index);
	}

	private void updateText(int index) {
		mCardScrollView.setSelection(index);
	}

	@Override
	public void onNod() {
	}

	@Override
	public void onShakeToLeft() {
		prev();
	}

	@Override
	public void onShakeToRight() {
		next();
	}

	@Override
	public Loader<Song> onCreateLoader(int id, Bundle args) {
		String query = args.getString(QUERY);
		Loader<Song> loader = new SongLoader(this, query);
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<Song> loader, Song song) {
		if (song == null) {
			Toast.makeText(this, "No song to display", Toast.LENGTH_LONG).show();
			finish();
			return;
		}
		List<String> lyrics = song.getBodyAsList();
		List<String> twoplay = new ArrayList<String>();
		int i = 0;
		String t = "";
		for (String s : lyrics) {
			s = s.replaceAll("(\\[.{1,6}\\])", "<font color='#00FF00'>$1</font>");
			if (i == 1) {
				t += "<br />" + s;
				twoplay.add(t);
				t = "";
			} else {
				t += s;
			}
			i = (i + 1) % 2;
		}
		if (!t.isEmpty()) {
			twoplay.add(t);
		}
		// Log.d(C.TAG, lyrics.toString());
		// mAdapter = new ListCardAdapter(this, song.getTitle(), lyrics);
		mAdapter = new ListCardAdapter(this, song.getTitle(), twoplay);
		mCardScrollView.setAdapter(mAdapter);
		mCardScrollView.activate();
	}

	@Override
	public void onLoaderReset(Loader<Song> arg0) {
	}

	private static class SongLoader extends AsyncTaskLoader<Song> {

		private String query;

		public SongLoader(Context context, String query) {
			super(context);
			this.query = query;
		}

		@Override
		protected void onStartLoading() {
			super.onStartLoading();
			forceLoad();
		}

		@Override
		public Song loadInBackground() {
			GuitarpartyService service = new GuitarpartyClient().getService(C.API_KEY);
			List<Song> songs = service.getSongs(query).getObjects();
			if (!songs.isEmpty()) {
				return songs.get(0);
			} else {
				return null;
			}
		}
	}
}
