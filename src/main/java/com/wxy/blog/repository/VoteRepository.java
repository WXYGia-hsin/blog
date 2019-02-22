package com.wxy.blog.repository;

import com.wxy.blog.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote,Long>{
}
