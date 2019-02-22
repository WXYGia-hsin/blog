package com.wxy.blog.service;

import com.wxy.blog.entity.Vote;

public interface VoteService {
    Vote getVoteByid(Long id);
    void removeVote(Long id);
}
