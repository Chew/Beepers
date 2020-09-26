package com.mcprohosting.beepers.objects;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;

public enum Roles {
    // Founder
    FOUNDER("715275511273881652", Rank.STAFF),
    // Co-Founder
    CO_FOUNDER("715780738967928863", Rank.STAFF),
    // Community Manager
    COMMUNITY_MANAGER("715322089669787688", Rank.STAFF),
    // Officers
    OFFICERS("715322088134803537", Rank.STAFF),
    // Coordinators / Managers
    MANAGERS("715322092022923355", Rank.STAFF),
    // SysOps
    SYSOPS("715381551919267843", Rank.STAFF),
    // DevOps
    DEVOPS("718187717783060551", Rank.STAFF),
    // Design
    DESIGN("715384578898001920", Rank.STAFF),
    // Support
    SUPPORT("715347876460363817", Rank.STAFF),
    // Partnerships
    PARTNERSHIPS("715431277645266954", Rank.STAFF),
    // Lead Mod
    LEAD_MOD("715322092811190282", Rank.MOD),
    // Discord Moderators
    MODERATORS("715322093486473237", Rank.MOD),
    // Server Owners
    SERVER_OWNER("715393459816759296", Rank.CUSTOMER);

    public final String id;
    public final Rank rank;

    Roles(String id, Rank rank) {
        this.id = id;
        this.rank = rank;
    }

    public String getId() {
        return id;
    }

    public static Roles fromId(String id) {
        for (Roles role : Roles.values()) {
            if (role.getId().equals(id))
                return role;
        }
        return null;
    }

    public Role getAsRole(Guild server) {
        return server.getRoleById(getId());
    }

    public enum Rank {
        STAFF(4),
        MOD(3),
        CUSTOMER(2),
        MEMBER(1);

        private final int priority;

        Rank(int priority) {
            this.priority = priority;
        }

        public int getPriority() {
            return priority;
        }
    }
}
