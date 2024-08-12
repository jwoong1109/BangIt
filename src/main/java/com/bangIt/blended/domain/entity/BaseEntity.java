package com.bangIt.blended.domain.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@Getter
@MappedSuperclass
public class BaseEntity {
	
	@CreationTimestamp
	@Column(columnDefinition = "timestamp")
	protected LocalDateTime createdAt;
	
	@UpdateTimestamp
	@Column(columnDefinition = "timestamp")
	protected LocalDateTime updatedAt;

}
