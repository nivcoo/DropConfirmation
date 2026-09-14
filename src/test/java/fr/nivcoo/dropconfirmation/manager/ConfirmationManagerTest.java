package fr.nivcoo.dropconfirmation.manager;

import fr.nivcoo.dropconfirmation.config.MainConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class ConfirmationManagerTest {

    private final AtomicLong clock = new AtomicLong();
    private final ConfirmationManager manager = new ConfirmationManager(clock::get);
    private final MainConfig config = new MainConfig();
    private final UUID firstPlayer = UUID.randomUUID();
    private final UUID secondPlayer = UUID.randomUUID();

    @BeforeEach
    void configureGlobalConfirmation() {
        config.perItemConfirmation = false;
        config.secondsBeforeReset = 5;
        config.resetConfirmAfterDrop = false;
    }

    @Test
    void requiresAnotherConfirmationAfterDropWhenResetIsEnabled() {
        config.resetConfirmAfterDrop = true;

        assertTrue(manager.shouldCancel(firstPlayer, null, config));
        assertFalse(manager.shouldCancel(firstPlayer, null, config));
        assertTrue(manager.shouldCancel(firstPlayer, null, config));
        assertFalse(manager.shouldCancel(firstPlayer, null, config));
    }

    @Test
    void extendsTheWindowAfterEachConfirmedDropWhenResetIsDisabled() {
        assertTrue(manager.shouldCancel(firstPlayer, null, config));
        clock.set(TimeUnit.SECONDS.toNanos(4));
        assertFalse(manager.shouldCancel(firstPlayer, null, config));
        clock.set(TimeUnit.SECONDS.toNanos(8));
        assertFalse(manager.shouldCancel(firstPlayer, null, config));
        clock.set(TimeUnit.SECONDS.toNanos(12));
        assertFalse(manager.shouldCancel(firstPlayer, null, config));
    }

    @Test
    void expiresAtTheExactTimeoutBoundary() {
        assertTrue(manager.shouldCancel(firstPlayer, null, config));
        clock.set(TimeUnit.SECONDS.toNanos(5));
        assertTrue(manager.shouldCancel(firstPlayer, null, config));
        clock.incrementAndGet();
        assertFalse(manager.shouldCancel(firstPlayer, null, config));
    }

    @Test
    void doesNotShareConfirmationBetweenPlayers() {
        assertTrue(manager.shouldCancel(firstPlayer, null, config));
        assertTrue(manager.shouldCancel(secondPlayer, null, config));
        assertFalse(manager.shouldCancel(firstPlayer, null, config));
        assertFalse(manager.shouldCancel(secondPlayer, null, config));
    }

    @Test
    void clearsOnlyThePlayerLeavingTheServer() {
        assertTrue(manager.shouldCancel(firstPlayer, null, config));
        assertTrue(manager.shouldCancel(secondPlayer, null, config));

        manager.clear(firstPlayer);

        assertTrue(manager.shouldCancel(firstPlayer, null, config));
        assertFalse(manager.shouldCancel(secondPlayer, null, config));
    }

    @Test
    void clearsAllPendingConfirmationsOnReload() {
        assertTrue(manager.shouldCancel(firstPlayer, null, config));
        assertTrue(manager.shouldCancel(secondPlayer, null, config));

        manager.clear();

        assertTrue(manager.shouldCancel(firstPlayer, null, config));
        assertTrue(manager.shouldCancel(secondPlayer, null, config));
    }
}
