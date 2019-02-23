package fok.stream;

import android.os.Looper;

import fok.stream.action.AsyncAction;
import fok.stream.schedule.Scheduler;
import fok.stream.schedule.SchedulerFactory;
import fok.stream.subscribe.Subsriber;

class SprayExecutor<I, O> {
    void executeEntrance(final Subsriber<O> subscriber, Stream mStream, Spray<I, O> spray) {
        Looper looper = Looper.myLooper() == null ? Looper.getMainLooper() : Looper.myLooper();
        if (looper != Looper.myLooper()) {
            mStream.mScheduled = true;
        }
        execute(new AbstractSchedulerSubscribe<O>(SchedulerFactory.current()) {
            @Override
            public void subscribe(O value) {
                subscriber.subscribe(value);
            }
        }, spray);
    }

    @SuppressWarnings("unchecked")
    private <Input, Output> void execute(final AbstractSchedulerSubscribe<Output> subscriber,
                                         final Spray<Input, Output> spray) {
        if (spray.mScheduler != null) {
            AbstractSchedulerSubscribe<Output> schedulerSubscribe = new AbstractSchedulerSubscribe<Output>(
                spray.mScheduler) {
                @Override
                public void subscribe(final Output value) {
                    subscriber.mExecuteScheduler.execute(new Runnable() {
                        @Override
                        public void run() {
                            subscriber.subscribe(value);

                        }
                    });

                }
            };
            execute(schedulerSubscribe, spray.mUpStream);
            return;
        }

        if (spray.mAction != null) {

            if (spray.mUpStream != null) {
                AbstractSchedulerSubscribe<Input> schedulerSubscribe = new AbstractSchedulerSubscribe<Input>(
                    subscriber.mExecuteScheduler) {
                    @Override
                    public void subscribe(Input value) {
                        subscriber.subscribe(spray.mAction.action(value));
                    }
                };
                execute(schedulerSubscribe, spray.mUpStream);
            }

        } else if (spray.mAsyncAction != null) {

            if (spray.mUpStream != null) {
                AbstractSchedulerSubscribe<Input> schedulerSubscribe = new AbstractSchedulerSubscribe<Input>(
                    subscriber.mExecuteScheduler) {
                    @Override
                    public void subscribe(Input value) {
                        spray.mAsyncAction.action(value, new AsyncAction.Result<Output>() {
                            @Override
                            public void result(Output output) {
                                subscriber.subscribe(output);
                            }
                        });
                    }
                };
                execute(schedulerSubscribe, spray.mUpStream);
            }

        } else {
            if (spray.mStream.mScheduled) {
                Runnable callable = new Runnable() {
                    @Override
                    public void run() {
                        subscriber.subscribe((Output)spray.mInput);

                    }
                };
                subscriber.mExecuteScheduler.execute(callable);
            } else {
                subscriber.subscribe((Output)spray.mInput);
            }

        }
    }

    abstract class AbstractSchedulerSubscribe<T> implements Subsriber<T> {
        private final Scheduler mExecuteScheduler;

        AbstractSchedulerSubscribe(Scheduler scheduler) {
            mExecuteScheduler = scheduler;
        }
    }
}
