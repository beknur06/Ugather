package ugather.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ugather.model.AppUser;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Integer> {
    Optional<AppUser> findByUsername(String username);
}
