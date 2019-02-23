package fok.stream.schedule;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressWarnings("WeakerAccess")
public class SchedulerFactory {

    private static BaseScheduler sIo;
    private static BaseScheduler sUi;

    public static BaseScheduler io() {
        if (sIo == null) {
            final ExecutorService ioExecutor = Executors.newSingleThreadExecutor();
            sIo = new BaseScheduler() {
                @Override
                public void execute(Runnable runnable) {
                    ioExecutor.submit(runnable);
                }

                @Override
                public String name() {
                    return "IO - Scheduler";
                }
            };
        }
        return sIo;
    }

    public static BaseScheduler ui() {
        if (sUi == null) {
            final Handler handler = new Handler(Looper.getMainLooper());
            sUi = new BaseScheduler() {
                @Override
                public void execute(Runnable runnable) {
                    handler.post(runnable);
                }

                @Override
                public String name() {
                    return "UI - Scheduler";
                }
            };
        }
        return sUi;
    }

    public static BaseScheduler current() {
        Looper looper = Looper.myLooper() == null ? Looper.getMainLooper() : Looper.myLooper();
        final Handler handler = new Handler(looper);

        return new BaseScheduler() {
            @Override
            public void execute(Runnable runnable) {
                handler.post(runnable);
            }

            @Override
            public String name() {
                return "Current - Scheduler";
            }
        };
    }
}
