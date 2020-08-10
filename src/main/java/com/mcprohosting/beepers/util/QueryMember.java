package com.mcprohosting.beepers.util;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import java.util.Arrays;
import java.util.List;

public class QueryMember {
    public static final List<String> staffRoles = Arrays.asList(
            // Founder
            "715275511273881652",
            // Co-Founder
            "715780738967928863",
            // Community Manager
            "715322089669787688",
            // Officers
            "715322088134803537",
            // Coordinators / Managers
            "715322092022923355",
            // SysOps
            "715381551919267843",
            // DevOps
            "718187717783060551",
            // Design
            "715384578898001920",
            // Support
            "715347876460363817",
            // Partnerships
            "715431277645266954",
            // Lead Mod
            "715322092811190282",
            // Discord Moderators
            "715322093486473237"
    );

    public static boolean isStaff(Member member) {
        List<Role> roles = member.getRoles();
        for (Role role : roles) {
            if (staffRoles.contains(role.getId())) {
                return true;
            }
        }
        return false;
    }
}
