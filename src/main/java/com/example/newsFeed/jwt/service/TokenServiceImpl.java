package com.example.newsFeed.jwt.service;

import com.example.newsFeed.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TokenServiceImpl {

    private final UserRepository userRepository;

}
