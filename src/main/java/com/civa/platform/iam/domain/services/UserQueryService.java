package com.civa.platform.iam.domain.services;

import com.civa.platform.iam.domain.model.aggregates.User;
import com.civa.platform.iam.domain.model.queries.GetAllUsersQuery;
import com.civa.platform.iam.domain.model.queries.GetAllUsersPagedQuery;
import com.civa.platform.iam.domain.model.queries.GetUserByIdQuery;
import com.civa.platform.iam.domain.model.queries.GetUserByUsernameQuery;
import com.civa.platform.shared.domain.model.valueobjects.PageResponse;

import java.util.List;
import java.util.Optional;

public interface UserQueryService {
    List<User> handle(GetAllUsersQuery query);
    PageResponse<User> handle(GetAllUsersPagedQuery query);
    Optional<User> handle(GetUserByIdQuery query);
    Optional<User> handle(GetUserByUsernameQuery query);
}
