package com.proj.batchtutorial.core.domain.orders;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * packageName   : com.proj.batchtutorial.core.domain.orders
 * fileName      : OrdersRepository
 * author        : kang_jungwoo
 * date          : 2023/05/01
 * description   :
 * ===========================================================
 * DATE              AUTHOR               NOTE
 * -----------------------------------------------------------
 * 2023/05/01       kang_jungwoo         최초 생성
 */
public interface OrdersRepository extends JpaRepository<Orders, Integer> {
}
