package com.civa.platform.iam.infrastructure.hashing.bcrypt;

import com.civa.platform.iam.application.internal.outboundedservices.hashing.HashingService;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface BCryptHashingService extends HashingService, PasswordEncoder {
}
