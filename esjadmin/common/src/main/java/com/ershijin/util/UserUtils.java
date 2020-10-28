package com.ershijin.util;

import org.springframework.security.core.context.SecurityContextHolder;
import com.ershijin.model.UserPrincipal;


public class UserUtils {
    public static UserPrincipal getCurrentUser() {
        return  (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
