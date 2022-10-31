package ro.dev.trellteam.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ro.dev.trellteam.domain.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByUsername(String username);
    Account save(Account account);

    @Modifying
    @Query("delete from ACCOUNT where id=:id")
    void deleteById(@Param("id") Long id);

    @Modifying
    @Query("delete from ACCOUNT where username=:username")
    void deleteByUsername(@Param("username") String username);
}
