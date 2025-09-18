package com.cmsfoundation.misportal.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "media_evidence")
public class MediaEvidence {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "media_evidence")
    private String mediaEvidence;
    
    @Column(name = "title")
    private String title;
    
    @Column(name = "video_link")
    private String videoLink;
    
    @Column(name = "image_link")
    private String imageLink;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // Default constructor
    public MediaEvidence() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMediaEvidence() {
		return mediaEvidence;
	}

	public void setMediaEvidence(String mediaEvidence) {
		this.mediaEvidence = mediaEvidence;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getVideoLink() {
		return videoLink;
	}

	public void setVideoLink(String videoLink) {
		this.videoLink = videoLink;
	}

	public String getImageLink() {
		return imageLink;
	}

	public void setImageLink(String imageLink) {
		this.imageLink = imageLink;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public MediaEvidence(Long id, String mediaEvidence, String title, String videoLink, String imageLink,
			LocalDateTime createdAt, LocalDateTime updatedAt) {
		super();
		this.id = id;
		this.mediaEvidence = mediaEvidence;
		this.title = title;
		this.videoLink = videoLink;
		this.imageLink = imageLink;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	@Override
	public String toString() {
		return "MediaEvidence [id=" + id + ", mediaEvidence=" + mediaEvidence + ", title=" + title + ", videoLink="
				+ videoLink + ", imageLink=" + imageLink + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt
				+ "]";
	}
    
    
    
    // Note: Getters, setters, toString, and constructors will be added by user as mentioned
}