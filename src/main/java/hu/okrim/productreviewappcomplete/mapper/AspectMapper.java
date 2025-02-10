package hu.okrim.productreviewappcomplete.mapper;

import hu.okrim.productreviewappcomplete.dto.AspectDTO;
import hu.okrim.productreviewappcomplete.model.Aspect;

public class AspectMapper {
    public static AspectDTO mapToAspectDTO (Aspect aspect){
        return new AspectDTO(
                aspect.getId(),
                aspect.getName(),
                aspect.getQuestion(),
                aspect.getCategory()
        );
    }
    public static Aspect mapToAspect (AspectDTO aspectDTO){
        return new Aspect(
                aspectDTO.getId() != null ? aspectDTO.getId() : null,
                aspectDTO.getName(),
                aspectDTO.getQuestion(),
                aspectDTO.getCategory()
        );
    }
}
