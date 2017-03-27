package juhe.jiangdajiuye.Service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * class description here
 *
 * @version 1.0.0
 * @outher wangqiang
 * @project jiangdajiuye
 * @since 2017-02-26
 */
public class MusicService extends Service{
    private String TAG = "MusicService";

    public MusicService() {
    }
    @Override
    public void onCreate (){

    }
  /*Called by the system every time a client explicitly
     starts the service by calling startService(Intent),
        providing the arguments it supplied and a unique
     integer token representing the start request.
     Do not call this method directly.
     */

    /**
     * For backwards compatibility, t
     * he default implementation calls onStart(Intent, int)
     * and returns either START_STICKY or START_STICKY_COMPATIBILITY.
     * @param intent  Intent: The Intent supplied to startService(Intent), as given.
     *                This may be null if the service is being restarted after its process has gone away,
     *                and it had previously returned anything except START_STICKY_COMPATIBILITY.
     * @param flags  int: Additional data about this start request.
     *               Currently either 0, START_FLAG_REDELIVERY, or START_FLAG_RETRY.
     * @param startId int: A unique integer representing this specific request to start.
     *                Use with stopSelfResult(int).
     * @return
     */
    @Override
    public int onStartCommand (Intent intent,
                               int flags,
                               int startId){
        return START_STICKY;
    }
    @Override
    public void onDestroy (){

    }
    @Override
    public void onLowMemory (){

    }

    @Override
    public boolean onUnbind (Intent intent){

        return true;
    }
    /**
     * Return the communication channel to the service.  May return null if
     * clients can not bind to the service.  The returned
     * {@link IBinder} is usually for a complex interface
     * that has been <a href="{@docRoot}guide/components/aidl.html">described using
     * aidl</a>.
     * <p>
     * <p><em>Note that unlike other application components, calls on to the
     * IBinder interface returned here may not happen on the main thread
     * of the process</em>.  More information about the main thread can be found in
     * <a href="{@docRoot}guide/topics/fundamentals/processes-and-threads.html">Processes and
     * Threads</a>.</p>
     *
     * @param intent The Intent that was used to bind to this service,
     *               as given to {@link Context#bindService
     *               Context.bindService}.  Note that any extras that were included with
     *               the Intent at that point will <em>not</em> be seen here.
     * @return Return an IBinder through which clients can call on to the
     * service.
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void unregisterReceiver(BroadcastReceiver receiver) {
        super.unregisterReceiver(receiver);
    }
}
