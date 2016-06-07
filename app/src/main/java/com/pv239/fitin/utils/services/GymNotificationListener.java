package com.pv239.fitin.utils.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.pv239.fitin.MainActivity;
import com.pv239.fitin.R;
import com.pv239.fitin.domain.Gym;
import com.pv239.fitin.domain.User;
import com.pv239.fitin.utils.Constants;
import com.pv239.fitin.utils.DataManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vlado on 07.06.2016.
 */
public class GymNotificationListener extends Service {
    boolean mRunning = false;
    // False, if register listener, true, if listening to new values
    boolean listen = false;

    List<String> loadedGyms = new ArrayList<>();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mRunning = false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(mRunning) {
            return START_STICKY;
        }
        mRunning = true;

        User user = (User) DataManager.getInstance().getObject(Constants.USER);
        Firebase ref = new Firebase(Constants.FIREBASE_REF + "gyms/");


        for(final String favGymId: user.getFavouriteGyms()) {
            Log.i(Constants.TAG, favGymId);
            Firebase gymRef = ref.child(favGymId);
            gymRef/*.orderByKey().limitToLast(1)*/.addValueEventListener(

                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(loadedGyms.contains(favGymId)) {
                                Gym gym = dataSnapshot.getValue(Gym.class);
                                gym.setId(dataSnapshot.getKey());
                                Log.i(Constants.TAG, gym.toString());
                                showNotification(gym);
                            } else {
                                loadedGyms.add(favGymId);
                            }
//                            String s = dataSnapshot.getValue().toString();
//                            Log.i(Constants.TAG, s);
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
        }

        return START_STICKY;
    }

    private void showNotification(Gym gym){
        //Creating a notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(Constants.GYM_ID, gym.getId());
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(pendingIntent);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setContentTitle(getString(R.string.gym) + " " + gym.getName() + " " + getString(R.string.changed));
        builder.setContentText(getString(R.string.notification_gym_changed));
        builder.setAutoCancel(true);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(((int) System.currentTimeMillis()), builder.build());
    }
}
