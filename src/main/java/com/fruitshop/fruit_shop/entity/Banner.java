package com.fruitshop.fruit_shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "banners")
@Getter
@Setter
public class Banner {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String title;

	private String description;

	@Lob
	@Column(columnDefinition = "LONGTEXT")
	private String image;

	private String link;

	@Enumerated(EnumType.STRING)
	private BannerPosition position;

	@Enumerated(EnumType.STRING)
	private BannerStatus status;

	public enum BannerPosition {
		home_top, home_slider
	}

	public enum BannerStatus {
		active, inactive
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public BannerPosition getPosition() {
		return position;
	}

	public void setPosition(BannerPosition position) {
		this.position = position;
	}

	public BannerStatus getStatus() {
		return status;
	}

	public void setStatus(BannerStatus status) {
		this.status = status;
	}

}