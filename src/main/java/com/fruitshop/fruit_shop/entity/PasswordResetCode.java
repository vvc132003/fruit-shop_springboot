package com.fruitshop.fruit_shop.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "password_reset_codes")
public class PasswordResetCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String code;

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

    // getters setters
    public Integer getId() {
        return id;
    }

   

    public User getUser() {
		return user;
	}



	public void setUser(User user) {
		this.user = user;
	}



	public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(LocalDateTime expiredAt) {
        this.expiredAt = expiredAt;
    }

	public void setId(Integer id) {
		this.id = id;
	}
}