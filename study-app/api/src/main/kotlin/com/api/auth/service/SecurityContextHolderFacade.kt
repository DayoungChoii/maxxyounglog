package com.api.auth.service

import org.springframework.security.core.context.SecurityContextHolder

class SecurityContextHolderFacade {
    companion object {
        fun getId(): Long {
            return SecurityContextHolder.getContext().authentication.name.toLong()
        }
    }
}