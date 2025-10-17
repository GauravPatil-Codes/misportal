package com.cmsfoundation.misportal.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "quick_updates")
public class QuickUpdate {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "project")
    private String project;
    
    @Column(name = "update_type")
    private String updateType;
    
    @Column(name = "update_title")
    private String updateTitle;
    
    @Column(name = "progress_description", columnDefinition = "TEXT")
    private String progressDescription;
    
    @Column(name = "overall_progress")
    private Integer overallProgress;
    
    @Column(name = "current_challenges", columnDefinition = "TEXT")
    private String currentChallenges;
    
    @Column(name = "next_steps", columnDefinition = "TEXT")
    private String nextSteps;
    
    @Column(name = "media_files_count")
    private Integer mediaFilesCount;
    
    @Embedded
    private MediaFiles mediaFiles;
    
    @Column(name = "status")
    private String status;
    
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
    public QuickUpdate() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getUpdateType() {
		return updateType;
	}

	public void setUpdateType(String updateType) {
		this.updateType = updateType;
	}

	public String getUpdateTitle() {
		return updateTitle;
	}

	public void setUpdateTitle(String updateTitle) {
		this.updateTitle = updateTitle;
	}

	public String getProgressDescription() {
		return progressDescription;
	}

	public void setProgressDescription(String progressDescription) {
		this.progressDescription = progressDescription;
	}

	public Integer getOverallProgress() {
		return overallProgress;
	}

	public void setOverallProgress(Integer overallProgress) {
		this.overallProgress = overallProgress;
	}

	public String getCurrentChallenges() {
		return currentChallenges;
	}

	public void setCurrentChallenges(String currentChallenges) {
		this.currentChallenges = currentChallenges;
	}

	public String getNextSteps() {
		return nextSteps;
	}

	public void setNextSteps(String nextSteps) {
		this.nextSteps = nextSteps;
	}

	public Integer getMediaFilesCount() {
		return mediaFilesCount;
	}

	public void setMediaFilesCount(Integer mediaFilesCount) {
		this.mediaFilesCount = mediaFilesCount;
	}

	public MediaFiles getMediaFiles() {
		return mediaFiles;
	}

	public void setMediaFiles(MediaFiles mediaFiles) {
		this.mediaFiles = mediaFiles;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public QuickUpdate(Long id, String project, String updateType, String updateTitle, String progressDescription,
			Integer overallProgress, String currentChallenges, String nextSteps, Integer mediaFilesCount,
			MediaFiles mediaFiles, String status, LocalDateTime createdAt, LocalDateTime updatedAt) {
		super();
		this.id = id;
		this.project = project;
		this.updateType = updateType;
		this.updateTitle = updateTitle;
		this.progressDescription = progressDescription;
		this.overallProgress = overallProgress;
		this.currentChallenges = currentChallenges;
		this.nextSteps = nextSteps;
		this.mediaFilesCount = mediaFilesCount;
		this.mediaFiles = mediaFiles;
		this.status = status;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	@Override
	public String toString() {
		return "QuickUpdate [id=" + id + ", project=" + project + ", updateType=" + updateType + ", updateTitle="
				+ updateTitle + ", progressDescription=" + progressDescription + ", overallProgress=" + overallProgress
				+ ", currentChallenges=" + currentChallenges + ", nextSteps=" + nextSteps + ", mediaFilesCount="
				+ mediaFilesCount + ", mediaFiles=" + mediaFiles + ", status=" + status + ", createdAt=" + createdAt
				+ ", updatedAt=" + updatedAt + "]";
	}
    
    // Note: Getters, setters, toString, and constructors will be added by user as mentioned


}

@Embeddable
class MediaFiles {
    
    @JsonProperty("imageUrl")  // ← Map JSON "imageUrl" to this field
    @Column(name = "media_file_1")
    private String mediaFile1;
    
    @JsonProperty("videoUrl")  // ← Map JSON "videoUrl" to this field
    @Column(name = "media_file_2")
    private String mediaFile2;
    
    @JsonProperty("documentUrl")  // ← Map JSON "documentUrl" to this field
    @Column(name = "media_file_3")
    private String mediaFile3;
    
    
    
    public MediaFiles(String mediaFile1, String mediaFile2, String mediaFile3) {
        this.mediaFile1 = mediaFile1;
        this.mediaFile2 = mediaFile2;
        this.mediaFile3 = mediaFile3;
    }
    
    @JsonProperty("imageUrl")  // ← Also add to getters
    public String getMediaFile1() {
        return mediaFile1;
    }

    @JsonProperty("imageUrl")  // ← And setters
    public void setMediaFile1(String mediaFile1) {
        this.mediaFile1 = mediaFile1;
    }

    @JsonProperty("videoUrl")
    public String getMediaFile2() {
        return mediaFile2;
    }

    @JsonProperty("videoUrl")
    public void setMediaFile2(String mediaFile2) {
        this.mediaFile2 = mediaFile2;
    }

    @JsonProperty("documentUrl")
    public String getMediaFile3() {
        return mediaFile3;
    }

    @JsonProperty("documentUrl")
    public void setMediaFile3(String mediaFile3) {
        this.mediaFile3 = mediaFile3;
    }

    @Override
    public String toString() {
        return "MediaFiles [mediaFile1=" + mediaFile1 + ", mediaFile2=" + mediaFile2 
                + ", mediaFile3=" + mediaFile3 + "]";
    }



	// Default constructor
    public MediaFiles() {}
}