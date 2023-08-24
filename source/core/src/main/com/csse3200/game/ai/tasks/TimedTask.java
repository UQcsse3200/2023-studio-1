package com.csse3200.game.ai.tasks;

import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.ai.tasks.TaskRunner;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;

/**
 * A task that runs for a set amount of time (depending on priority). This task must be triggered by an event.
 * The task will run unless overridden by a task with higher priority.
 */
public class TimedTask extends DefaultTask implements PriorityTask {
    private final String trigger;
    private final float duration;
    private float runningTime = 0f;
    private int priority = -1;
    private final int activePriority;
    private GameTime timeSource;

    /**
     * Constructs a TimedTask with specified parameters.
     *
     * @param trigger        The event trigger that activates this task.
     * @param duration       The duration for which this task will run.
     * @param priority       The initial priority of the task.
     */
    public TimedTask(String trigger, float duration, int priority) {
        this.trigger = trigger;
        this.duration = duration;
        this.activePriority = priority;
    }

    @Override
    public void create(TaskRunner taskRunner) {
        super.create(taskRunner);
        timeSource = ServiceLocator.getTimeSource();

        // Listen for the trigger event to set the active priority
        this.owner.getEntity().getEvents().addListener(trigger, this::triggerActivePriority);
    }

    /**
     * Sets the priority of the task to the active priority when triggered by an event.
     */
    public void triggerActivePriority() {
        this.priority = activePriority;
    }

    @Override
    public void update() {
        runningTime += timeSource.getDeltaTime();

        // Reset priority if the running time exceeds the duration
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
