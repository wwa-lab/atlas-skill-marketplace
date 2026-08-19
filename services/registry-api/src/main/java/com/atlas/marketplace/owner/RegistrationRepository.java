package com.atlas.marketplace.owner; import java.util.UUID; import org.springframework.data.jpa.repository.JpaRepository;
interface RegistrationRepository extends JpaRepository<RegistrationEntity,UUID>{}
