package com.amano.springBoot.repo;

import com.amano.springBoot.entity.Love;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LoveRepo extends JpaRepository<Love, Long>, JpaSpecificationExecutor<Love> {

}
