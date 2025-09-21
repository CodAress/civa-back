package com.civa.platform.iam.application.internal.queryservices;

import com.civa.platform.iam.domain.model.aggregates.User;
import com.civa.platform.iam.domain.model.queries.GetAllUsersQuery;
import com.civa.platform.iam.domain.model.queries.GetAllUsersPagedQuery;
import com.civa.platform.iam.domain.model.queries.GetUserByIdQuery;
import com.civa.platform.iam.domain.model.queries.GetUserByUsernameQuery;
import com.civa.platform.iam.domain.services.UserQueryService;
import com.civa.platform.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import com.civa.platform.shared.domain.model.valueobjects.PageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserQueryServiceImpl implements UserQueryService {
    private final UserRepository userRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserQueryServiceImpl.class);

    public UserQueryServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> handle(GetAllUsersQuery query) {
        var users = userRepository.findAllActive();
        LOGGER.info("Found {} active users", users.size());
        return users;
    }

    @Override
    public PageResponse<User> handle(GetAllUsersPagedQuery query) {
        var pageable = query.pageRequest().toPageable();
        var userPage = userRepository.findAllActive(pageable);
        
        LOGGER.info("Found {} active users on page {} of {}", 
                   userPage.getNumberOfElements(), 
                   userPage.getNumber(), 
                   userPage.getTotalPages());
        
        return PageResponse.from(userPage);
    }

    @Override
    public Optional<User> handle(GetUserByIdQuery query) {
        return userRepository.findByIdAndActive(query.userId());
    }

    @Override
    public Optional<User> handle(GetUserByUsernameQuery query) {
        return userRepository.findByUsernameAndActive(query.username());
    }
}
