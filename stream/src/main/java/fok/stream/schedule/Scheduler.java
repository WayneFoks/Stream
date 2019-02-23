package fok.stream.schedule;

public interface Scheduler {
    /**
     * 执行操作
     */
    void execute(Runnable runnable);
}