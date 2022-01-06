package net.iambartz.lightrank.api.player;

import net.iambartz.lightrank.api.invite.Invite;

import java.util.Set;

public interface Requestor {
    Set<Invite> getInvites();
    boolean containsInviteFrom(String playerName);
    void addInvite(Invite invite);
    void removeInvite(Invite invite);
    void clearExpiredInvites();
}
