package com.proj.batchtutorial.core.domain.accounts;

import com.proj.batchtutorial.core.domain.orders.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * packageName   : com.proj.batchtutorial.core.domain.accounts
 * fileName      : AccountsRepository
 * author        : kang_jungwoo
 * date          : 2023/05/01
 * description   :
 * ===========================================================
 * DATE              AUTHOR               NOTE
 * -----------------------------------------------------------
 * 2023/05/01       kang_jungwoo         최초 생성
 */
public interface AccountsRepository extends JpaRepository<Accounts, Integer> {
}
