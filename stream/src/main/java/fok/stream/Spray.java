package fok.stream;

import fok.stream.action.Action;
import fok.stream.action.AsyncAction;
import fok.stream.schedule.Scheduler;
import fok.stream.subscribe.Subsriber;

public class Spray<Input, Output> {
    Spray mUpStream;
    Action<Input, Output> mAction;
    AsyncAction<Input, Output> mAsyncAction;
    Scheduler mScheduler;
    Input mInput;
    Stream mStream;

    Spray(Stream stream) {
        mStream = stream;
    }

    public <C> Spray<Output, C> to(Action<Output, C> action) {
        Spray<Output, C> stream = new Spray<>(mStream);
        stream.mAction = action;
        stream.mUpStream = this;
        return stream;
    }

    public <C> Spray<Output, C> to(AsyncAction<Output, C> asyncAction) {
        Spray<Output, C> stream = new Spray<>(mStream);
        stream.mAsyncAction = asyncAction;
        stream.mUpStream = this;
        return stream;
    }

    public Spray<Input, Output> on(Scheduler scheduler) {
        Spray<Input, Output> stream = new Spray<>(mStream);
        stream.mScheduler = scheduler;
        stream.mUpStream = this;
        mStream.mScheduled = true;
        return stream;
    }

    public void subscribe(Subsriber<Output> subscriber) {
        new SprayExecutor<Input, Output>().executeEntrance(subscriber, mStream, this);
    }

}
