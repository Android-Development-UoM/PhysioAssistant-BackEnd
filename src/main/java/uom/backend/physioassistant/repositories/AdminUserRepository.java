package uom.backend.physioassistant.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uom.backend.physioassistant.models.users.Admin;

@Repository
public interface AdminUserRepository extends JpaRepository<Admin, String> {
}
