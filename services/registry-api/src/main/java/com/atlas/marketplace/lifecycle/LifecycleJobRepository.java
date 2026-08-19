package com.atlas.marketplace.lifecycle;
import java.util.Optional; import java.util.UUID; import org.springframework.data.jpa.repository.JpaRepository;
interface LifecycleJobRepository extends JpaRepository<LifecycleJobEntity, UUID>{Optional<LifecycleJobEntity> findByIdempotencyKey(String key);}
