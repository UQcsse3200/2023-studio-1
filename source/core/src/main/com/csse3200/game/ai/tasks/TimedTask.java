package com.csse3200.game.ai.tasks;

import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.ai.tasks.TaskRunner;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;

/**
 * Have a task run for a set amount of time (depending on priority). Must be triggered by event. Task will run
 * unless overridden by a task with higher priority.
 */
public class TimedTask extends DefaultTask implements PriorityTask {
    private final String trigger;
    private final float duration;
    private float runningTime = 0f;
    private int priority = -1;
    private final int activePriority;
    private GameTime timeSource;


    public TimedTask(String trigger, float duration, int priority) {
        this.trigger = trigger;
        this.duration = duration;
        this.activePriority = priority;
    }

    @Override
    public void create(TaskRunner taskRunner) {
        super.create(taskRunner);
        timeSource = ServiceLocator.getTimeSource();

        this.owner.getEntity().getEvents().addListener(trigger, this::triggerActivePriority);
    }

    public void triggerActivePriority() {
        this.priority = activePriority;
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void update() {
        runningTime += timeSource.getDeltaTime();
        if (runningTime > duration) {
            this.priority = -1;
        }
    }

    @Override
    public void stop() {
        super.stop();
        runningTime = 0f;
        this.priority = -1;
    }

    @Override
    public int getPriority() {
        return priority;
    }
}
