package juhe.jiangdajiuye.utils.lifecycle;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * class description here
 *
 *  控制activity是否是前台的util
 *
 * @author wangqiang
 * @since 2017-07-31
 */

public class AppLifeCycle implements Application.ActivityLifecycleCallbacks {
    private static final String TAG = "AppLifeCycle";
    private Listeners listeners = new Listeners();
    private static Activity current;
    private AppLifeCycle(){}
    public interface Listener {
        void onBecameForeground(Activity activity);

        void onBecameBackground(Activity activity);
    }
    public interface Binding {
        void unbind();
    }
    private interface Callback {
        void invoke(Listener listener);
    }
    public Binding addListener(Listener listener) {
        return listeners.add(listener);
    }
    private static class Listeners {
        private List<WeakReference<Listener>> listeners = new CopyOnWriteArrayList<>();

        public Binding add(Listener listener) {
            final WeakReference<Listener> wr = new WeakReference<>(listener);
            listeners.add(wr);
            return new Binding() {
                public void unbind() {
                    listeners.remove(wr);
                }
            };
        }

        public void each(Callback callback) {
            for (Iterator<WeakReference<Listener>> it = listeners.iterator(); it.hasNext(); ) {
                try {
                    WeakReference<Listener> wr = it.next();
                    Listener l = wr.get();
                    if (l != null)
                        callback.invoke(l);
                    else
                        it.remove();
                } catch (Exception exc) {
                }
            }
        }
    }
    private static Callback becameForeground = new Callback() {
        @Override
        public void invoke(Listener listener) {
            listener.onBecameForeground(current);
        }
    };

    private static Callback becameBackground = new Callback() {
        @Override
        public void invoke(Listener listener) {
            listener.onBecameBackground(current);
        }
    };
    public static AppLifeCycle instance ;
    public static AppLifeCycle getInstance(Application application){
        if( null == instance){
                    instance = new AppLifeCycle();
            application.registerActivityLifecycleCallbacks(instance);
        }
        return instance;
    }
    private boolean foreground;

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if (!foreground && (activity != null && !activity.isChangingConfigurations())) {
            foreground = true;
            listeners.each(becameForeground);
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {
        current = activity;
        if (!foreground && (activity != null && !activity.isChangingConfigurations())) {
            foreground = true;
            listeners.each(becameForeground);
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
        if (foreground) {
            if ((activity == current) && (activity != null && !activity.isChangingConfigurations())) {
                foreground = false;
                listeners.each(becameBackground);
            }
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }
}
