package com.mcprohosting.beepers.util;

import com.mcprohosting.beepers.objects.Roles;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import java.util.Arrays;
import java.util.List;

public class QueryMember {
    public static boolean isStaff(Member member) {
        List<Role> roles = member.getRoles();
        for (Role role : roles) {
            if (Arrays.asList(Arrays.stream(Roles.values()).map(rol -> role.getId()).toArray()).contains(role.getId())) {
                Roles mcproRole = Roles.fromId(role.getId());
                if (mcproRole != null && Roles.Rank.STAFF == mcproRole.rank) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean hasPower(Member member) {
        List<Role> roles = member.getRoles();
        for (Role role : roles) {
            if (Arrays.asList(Arrays.stream(Roles.values()).map(rol -> role.getId()).toArray()).contains(role.getId())) {
                Roles mcproRole = Roles.fromId(role.getId());
                if (mcproRole != null && mcproRole.rank.getPriority() >= 3) {
                    return true;
                }
            }
        }
        return false;
    }
}
