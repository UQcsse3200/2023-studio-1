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
    /** String to be called to trigger task */
    private final String trigger;
    /** Length of task run */
    private final float duration;
    /** Current run time */
    private float runningTime = 0f;
    /** Current priority, initial is -1 */
    private int priority = -1;
    /** Active priority that is set when event is triggered */
    private final int activePriority;
    /** Global time source */
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

    /**
     * When task is created, attach event listener to task owner.
     * @param taskRunner Task runner to attach to
     */
    @Override
    public void create(TaskRunner taskRunner) {
        super.create(taskRunner);
        timeSource = ServiceLocator.getTimeSource();

        // Listen for the trigger event to set the active priority
        this.owner.getEntity().getEvents().addListener(trigger, this::triggerActivePriority);
    }

    /**
     * Start task, setting current run time to 0.
     */
    @Override
    public void start() {
        super.start();
        runningTime = 0f;
    }

    /**
     * Sets the priority of the task to the active priority when triggered by an event.
     */
    public void triggerActivePriority() {
        this.priority = activePriority;
    }

    /**
     * Increment running time every update.
     */
    @Override
    public void update() {
        runningTime += timeSource.getDeltaTime();

        // Reset priority if the running time exceeds the duration
        if (runningTime > duration) {
            this.priority = -1;
            stop();
        }
    }

    /**
     * Stops task and sets back to inactive priority.
     */
    @Override
    public void stop() {
        super.stop();
        runningTime = 0f;
        this.priority = -1;
    }

    /**
     * Return priority
     * @return priority
     */
    @Override
    public int getPriority() {
        return priority;
    }
}
