package com.polysfactory.glassmusicreader;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.android.glass.timeline.LiveCard;
import com.google.android.glass.timeline.TimelineManager;
import com.polysfactory.headgesturedetector.HeadGestureDetector;
import com.polysfactory.headgesturedetector.OnHeadGestureListener;

/**
 * Service owning the LiveCard living in the timeline.
 */
public class FlipperService extends Service implements OnHeadGestureListener {

    private static final String LIVE_CARD_ID = "flipper";
    private static final String[] TEXTS = { "メロスは激怒した。必ず、かの邪智暴虐じゃちぼうぎゃくの王を除かなければならぬと決意した。",
            "メロスには政治がわからぬ。メロスは、村の牧人である。笛を吹き、羊と遊んで暮して来た。", "けれども邪悪に対しては、人一倍に敏感であった。",
            "きょう未明メロスは村を出発し、野を越え山越え、十里はなれた此このシラクスの市にやって来た。", "メロスには父も、母も無い。女房も無い。十六の、内気な妹と二人暮しだ。",
            "この妹は、村の或る律気な一牧人を、近々、花婿はなむことして迎える事になっていた。結婚式も間近かなのである。" };

    private TimelineManager mTimelineManager;
    private LiveCard mLiveCard;
    private int mIndex = 0;
    private HeadGestureDetector mHeadGestureDetector;

    @Override
    public void onCreate() {
        super.onCreate();
        mTimelineManager = TimelineManager.from(this);

        mHeadGestureDetector = new HeadGestureDetector(this);
        mHeadGestureDetector.setOnHeadGestureListener(this);
        mHeadGestureDetector.start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mLiveCard == null) {
            Log.d(C.TAG, "Publishing LiveCard");
			mLiveCard = mTimelineManager.createLiveCard(LIVE_CARD_ID);
            Intent menu = new Intent(this, MainActivity.class);
            mLiveCard.setAction(PendingIntent.getActivity(this, 0, menu, 0));
            mLiveCard.publish(LiveCard.PublishMode.REVEAL);
        }

        updateText();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (mLiveCard != null && mLiveCard.isPublished()) {
            Log.d(C.TAG, "Unpublishing LiveCard");
            mLiveCard.unpublish();
            mLiveCard = null;
        }
        mHeadGestureDetector.stop();
        super.onDestroy();
    }

    private void prev() {
        mIndex--;
        if (mIndex < 0) {
            mIndex = 0;
        }
        updateText();
    }

    private void next() {
        mIndex++;
        if (mIndex >= TEXTS.length) {
            mIndex = TEXTS.length - 1;
        }
        updateText();
    }

    private void updateText() {
        RemoteViews views = new RemoteViews(this.getPackageName(), R.layout.text_view);
        views.setTextViewText(R.id.text, TEXTS[mIndex]);
        mLiveCard.setViews(views);
    }

    @Override
    public void onNod() {
        Log.d(C.TAG, "Nod");
        // Toast.makeText(this, "Nod", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onShakeToLeft() {
        Log.d(C.TAG, "ShakeToLeft");
        // Toast.makeText(this, "Left", Toast.LENGTH_SHORT).show();
        prev();
    }

    @Override
    public void onShakeToRight() {
        Log.d(C.TAG, "ShakeToRight");
        // Toast.makeText(this, "Right", Toast.LENGTH_SHORT).show();
        next();
    }
}
