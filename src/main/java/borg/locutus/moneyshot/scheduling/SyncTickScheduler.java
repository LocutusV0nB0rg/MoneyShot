package borg.locutus.moneyshot.scheduling;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;


public class SyncTickScheduler {
    @SubscribeEvent
    public void onTick( TickEvent.ClientTickEvent event ) {

        List<SyncTask> ranTasks = new ArrayList<SyncTask>();

        for (SyncTask task : tasks) {
            if (task.waitTimeExceeded()) {
                task.runTask();
                ranTasks.add(task);
            }
        }

        for (SyncTask task : ranTasks) {
            tasks.remove(task);
        }
    }

    private static final List<SyncTask> tasks = new ArrayList<SyncTask>();

    public static void scheduleNewSyncTask(Runnable runnable, int tickDelay) {
        SyncTask newTask = new SyncTask(tickDelay, runnable);

        tasks.add(newTask);
    }

}
