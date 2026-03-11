package com.fruitshop.fruit_shop.repository;

import com.fruitshop.fruit_shop.entity.Order;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Integer> {

	Page<Order> findByReceiverNameContainingIgnoreCase(String keyword, Pageable pageable);

	@Query("SELECT COALESCE(SUM(o.total),0) FROM Order o")
	Double sumTotalPrice();

	@Query("""
			    SELECT COALESCE(SUM(o.total),0)
			    FROM Order o
			    WHERE MONTH(o.createdAt)=MONTH(CURRENT_DATE)
			    AND YEAR(o.createdAt)=YEAR(CURRENT_DATE)
			""")
	Double sumThisMonth();

	@Query("""
			    SELECT COALESCE(SUM(o.total),0)
			    FROM Order o
			    WHERE MONTH(o.createdAt)=MONTH(CURRENT_DATE)-1
			    AND YEAR(o.createdAt)=YEAR(CURRENT_DATE)
			""")
	Double sumLastMonth();

	@Query("SELECT SUM(o.total) FROM Order o WHERE DATE(o.createdAt) = :date")
	Long sumByDate(@Param("date") LocalDate date);

	// Tổng số đơn theo ngày
	@Query("SELECT COUNT(o) FROM Order o WHERE DATE(o.createdAt) = :date")
	Long countByDate(@Param("date") LocalDate date);

	Page<Order> findByUserId(Integer userId, Pageable pageable);

	@Query("""
			SELECT o FROM Order o 
			WHERE o.user.id = :userId 
			AND CAST(o.id AS string) LIKE %:keyword%
			""")
			Page<Order> searchByUser(Integer userId, String keyword, Pageable pageable);
}