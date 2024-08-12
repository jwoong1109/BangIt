package com.bangIt.blended.domain.entity;

import java.util.List;
import java.util.Set;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.boot.autoconfigure.info.ProjectInfoProperties.Build;

import com.bangIt.blended.domain.dto.place.PlaceDetailDTO;
import com.bangIt.blended.domain.enums.PlaceTheme;
import com.bangIt.blended.domain.enums.PlaceType;
import com.bangIt.blended.domain.enums.Region;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@DynamicUpdate
@SequenceGenerator(name = "gen_place", sequenceName = "seq+place", initialValue = 1, allocationSize = 1)
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "place")
@Getter
@Entity
public class PlaceEntity extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen_place")
	private long id;
	
	@Column(nullable = false)
	private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Region region;

    @Column(nullable = false)
    private String detailedAddress;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PlaceType type;

    @ElementCollection(targetClass = PlaceTheme.class)
    @CollectionTable(name = "place_themes", joinColumns = @JoinColumn(name = "place_id"))
    @Column(name = "theme")
    @Enumerated(EnumType.STRING)
    private Set<PlaceTheme> themes;

//    @Column(nullable = false)
//    private String mainImagePath;
//
//    @ElementCollection
//    @CollectionTable(name = "place_additional_images", joinColumns = @JoinColumn(name = "place_id"))
//    @Column(name = "image_path")
//    private List<String> additionalImagePaths;

    @Column
    private Double latitude;

    @Column
    private Double longitude;

    //숙소 상세 페이지 dto
	public PlaceDetailDTO toPlaceDetailDTO() {
		return PlaceDetailDTO.builder()
				.id(id)
				.name(name)
				.description(description)
				.detailedAddress(detailedAddress)
				.region(region)
				.type(type)
				.themes(themes)
				.latitude(latitude)
				.longitude(longitude)
				.build();
	}
	
	//상세페이지 dto
//		public NoticeDetailDTO toNoticeDetailDTO() {
//			return NoticeDetailDTO.builder()
//					.no(no)
//					.title(title)
//					.content(content)
//					.division(division)
//					.createdAt(createdAt)
//					.updatedAt(updatedAt)
//					.fixed(fixed)
//					.build();
//		}
	
	
}
