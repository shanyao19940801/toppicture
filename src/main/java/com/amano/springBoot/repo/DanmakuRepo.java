package com.amano.springBoot.repo;

import com.amano.springBoot.entity.Danmaku;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DanmakuRepo extends JpaRepository<Danmaku, Long>, JpaSpecificationExecutor<Danmaku> {
}
