package net.iambartz.lightrank.api.invite;

import java.time.LocalDateTime;
import java.time.LocalTime;

public interface ExpiringInvite extends Invite {
    LocalTime getTimeReceived();
    boolean hasExpired();
}
