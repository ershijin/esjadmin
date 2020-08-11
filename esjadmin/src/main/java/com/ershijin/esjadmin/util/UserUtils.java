package com.ershijin.esjadmin.util;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class UserUtils {
    public static UserDetails getCurrentUser() {
//        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
