package borg.locutus.moneyshot.scheduling;

public class SyncTask {
    private final long creationTime = System.currentTimeMillis();
    private final int tickDelay;
    private final Runnable runnable;

    public SyncTask(int tickDelay, Runnable runnable) {
        this.tickDelay = tickDelay;
        this.runnable = runnable;
    }

    public void runTask() {
        runnable.run();
    }

    public boolean waitTimeExceeded() {
        long delayInMillis = tickDelay * 50;
        long currentTime = System.currentTimeMillis();

        return (creationTime + delayInMillis <= currentTime);
    }
}
