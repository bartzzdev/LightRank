package net.iambartz.lightrank.api.invite;

import net.iambartz.lightrank.api.player.PlayerSession;

import java.util.UUID;

public interface Invite {
    PlayerSession getSender();
    void accept();
    void deny();
}
