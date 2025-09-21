package com.backend.jvconstructions.util;

import com.backend.jvconstructions.dto.ProjectDTOs;
import com.backend.jvconstructions.entity.Project;
import com.backend.jvconstructions.entity.ProjectImage;

import java.util.ArrayList;
import java.util.function.Function;

public class DtoMapper {
    public static ProjectDTOs.ProjectDetailDTO toDetail(Project p, Function<String, String> cdn) {
        return new ProjectDTOs.ProjectDetailDTO(
                p.getId(),
                p.getCode(),
                p.getName(),
                p.getDescription(),
                p.getCity(),
                p.getProjectStatus(),
                p.getStartDate(),
                p.getEndDate(),
                (p.getImages() != null ? p.getImages() : new ArrayList<ProjectImage>())
                        .stream().sorted((a, b) -> {
                    int so = Integer.compare(nz(a.getSortOrder()), nz(b.getSortOrder()));
                    return so != 0 ? so : Long.compare(a.getId(), b.getId());
                }).map(i -> new ProjectDTOs.ImageDTO(
                        i.getId(),
                        cdn.apply(i.getS3Key()),
                        i.getMimeType(),
                        i.getWidth(),
                        i.getHeight(),
                        nz(i.getSortOrder()),
                        Boolean.TRUE.equals(i.getHero())
                        )
                ).toList()
        );
    }

    public static ProjectDTOs.ProjectCardDTO toCard(Project p, Function<String, String> cdn) {
        String hero = null;
        if (p.getHeroImageId() != null && p.getImages() != null) {
            for (ProjectImage i : p.getImages()) {
                if (i != null && i.getId() != null && i.getId().equals(p.getHeroImageId()) && i.getS3Key() != null) {
                    hero = cdn.apply(i.getS3Key());
                    break;
                }
            }
        }
        if (hero == null && p.getImages() != null && !p.getImages().isEmpty()) {
            ProjectImage firstImage = p.getImages().get(0);
            if (firstImage != null && firstImage.getS3Key() != null) {
                hero = cdn.apply(firstImage.getS3Key());
            }
        }
        return new ProjectDTOs.ProjectCardDTO(p.getId(), p.getCode(), p.getName(),
                p.getCity(), p.getProjectStatus(), hero, p.getUpdatedAt());
    }

    private static int nz(Integer i) { return i == null ? 0 : i; }
}
