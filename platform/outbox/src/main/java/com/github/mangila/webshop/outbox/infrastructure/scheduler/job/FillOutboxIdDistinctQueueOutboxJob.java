package com.github.mangila.webshop.outbox.infrastructure.scheduler.job;

import com.github.mangila.webshop.outbox.application.action.query.FindAllOutboxIdsByStatusQueryAction;
import com.github.mangila.webshop.outbox.domain.cqrs.query.FindAllOutboxIdByStatusQuery;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.outbox.infrastructure.OutboxIdDistinctQueue;
import com.github.mangila.webshop.shared.SimpleJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * The FillEventQueueOutboxJob is responsible for populating an internal event queue
 * with outbox IDs that are fetched based on their status. This job interacts
 * with the FindAllOutboxIdsByStatusQueryAction to retrieve the outbox IDs and adds
 * them to an instance of InternalDistinctQueue.
 * <p>
 * Primary responsibilities:
 * - Fetch outbox IDs with a specific status (PENDING) within a predefined limit.
 * - Log the IDs being added to the queue.
 * - Add the fetched IDs to the event queue.
 * <p>
 * Implements the {@code SimpleTask} interface, allowing this job to be identified
 * and executed with a predefined key.
 * <p>
 * Key details:
 * - Uses the {@code OutboxJobKey} for identification.
 * - Executes the task by querying for a maximum of 120 PENDING outbox IDs and
 * populates them into the event queue while logging the process.
 * - Thread-safe operations on the queue ensure integrity of the data during concurrent executions.
 * <p>
 * Constructor Parameters:
 *
 * @param findAllOutboxIdsByStatusQueryAction A service used to retrieve a list of outbox IDs filtered by their status.
 * @param outboxIdDistinctQueue               The internal queue to populate with fetched outbox IDs.
 */
public record FillOutboxIdDistinctQueueOutboxJob(
        FindAllOutboxIdsByStatusQueryAction findAllOutboxIdsByStatusQueryAction,
        OutboxIdDistinctQueue outboxIdDistinctQueue) implements SimpleJob<OutboxJobKey> {

    private static final Logger log = LoggerFactory.getLogger(FillOutboxIdDistinctQueueOutboxJob.class);

    public static final OutboxJobKey KEY = new OutboxJobKey("FILL_OUTBOX_ID_DISTINCT_QUEUE");

    @Override
    public OutboxJobKey key() {
        return KEY;
    }

    @Override
    public void execute() {
        List<OutboxId> ids = findAllOutboxIdsByStatusQueryAction.execute(FindAllOutboxIdByStatusQuery.pending());
        outboxIdDistinctQueue.fillQueue(ids);
        log.debug("Fetched {} outbox IDs for processing -- {}", ids.size(), ids);
    }
}
