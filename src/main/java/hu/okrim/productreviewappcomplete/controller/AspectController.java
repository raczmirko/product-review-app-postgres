package hu.okrim.productreviewappcomplete.controller;

import hu.okrim.productreviewappcomplete.dto.AspectDTO;
import hu.okrim.productreviewappcomplete.mapper.AspectMapper;
import hu.okrim.productreviewappcomplete.model.Aspect;
import hu.okrim.productreviewappcomplete.model.Category;
import hu.okrim.productreviewappcomplete.service.AspectService;
import hu.okrim.productreviewappcomplete.service.CategoryService;
import hu.okrim.productreviewappcomplete.specification.AspectSpecificationBuilder;
import hu.okrim.productreviewappcomplete.util.SqlExceptionMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/aspect")
public class AspectController {
    @Autowired
    AspectService aspectService;
    @Autowired
    CategoryService categoryService;

    @GetMapping("/all")
    public ResponseEntity<List<Aspect>> getAspects() {
        List<Aspect> aspects = aspectService.findAll();
        if (aspects.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(aspects, HttpStatus.OK);
    }

    @PostMapping("/{id}/delete")
    public ResponseEntity<?> deleteAspect(@PathVariable("id") Long id) {
        try {
            aspectService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception ex) {
            String message = SqlExceptionMessageHandler.aspectDeleteErrorMessage(ex);
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }
    }

    @PostMapping("multi-delete/{ids}")
    public ResponseEntity<?> deleteAspects(@PathVariable("ids") Long[] ids) {
        try {
            for (Long id : ids) {
                aspectService.deleteById(id);
            }
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception ex) {
            String message = ex.getMessage();
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/{id}/modify")
    public ResponseEntity<?> modifyAspect(@PathVariable("id") Long id, @RequestBody AspectDTO aspectDTO) {
        Aspect existingAspect = aspectService.findById(id);

        if (existingAspect == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        existingAspect.setName(aspectDTO.getName());
        existingAspect.setQuestion(aspectDTO.getQuestion());
        existingAspect.setCategory(aspectDTO.getCategory());

        try {
            aspectService.save(existingAspect);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception ex) {
            String errorMessage = SqlExceptionMessageHandler.aspectUpdateErrorMessage(ex);
            return new ResponseEntity<>(errorMessage, HttpStatus.CONFLICT);
        }

    }

    @PostMapping("/create")
    public ResponseEntity<?> createAspect(@RequestBody AspectDTO aspectDTO) {
        if (aspectNameNotPresentInHierarchy(aspectDTO.getName(), aspectDTO.getCategory())) {
            aspectService.save(AspectMapper.mapToAspect(aspectDTO));
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            String errorMessage = "ERROR: An aspect with the same name is already defined in the category hierarchy.";
            return new ResponseEntity<>(errorMessage, HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Aspect>> searchAspects(@RequestParam(value = "searchText", required = false) String searchText,
                                                      @RequestParam(value = "searchColumn", required = false) String searchColumn,
                                                      @RequestParam(value = "quickFilterValues", required = false) String quickFilterValues,
                                                      @RequestParam("pageSize") Integer pageSize,
                                                      @RequestParam("pageNumber") Integer pageNumber,
                                                      @RequestParam("orderByColumn") String orderByColumn,
                                                      @RequestParam("orderByDirection") String orderByDirection
    ) {
        AspectSpecificationBuilder<Aspect> aspectSpecificationBuilder = new AspectSpecificationBuilder<>();
        if (searchColumn != null) {
            switch (searchColumn) {
                case "id" -> aspectSpecificationBuilder.withId(searchText);
                case "name" -> aspectSpecificationBuilder.withName(searchText);
                case "question" -> aspectSpecificationBuilder.withQuestion(searchText);
                case "category" -> aspectSpecificationBuilder.withCategoryName(searchText);
                default -> {

                }
            }
        } else {
            if (quickFilterValues != null && !quickFilterValues.isEmpty()) {
                // When searchColumn is not provided all fields are searched
                aspectSpecificationBuilder.withQuickFilterValues(List.of(quickFilterValues.split(",")));
            }
        }
        Specification<Aspect> specification = aspectSpecificationBuilder.build();
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by(orderByDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, orderByColumn));
        Page<Aspect> aspectsPage = aspectService.findAllBySpecification(specification, pageable);
        return new ResponseEntity<>(aspectsPage, HttpStatus.OK);
    }

    @GetMapping("/{id}/available-aspects")
    public ResponseEntity<Set<Aspect>> getAvailableAspects(@PathVariable("id") Long categoryId) {
        // Find the category by the provided ID
        Category category = categoryService.findById(categoryId);
        // Find all aspects
        Set<Aspect> availableAspects = new HashSet<>(aspectService.findAll());
        // Find all aspects that are already assigned in the category tree
        Set<Aspect> alreadyAssignedAspects = new HashSet<>(getAspectsOfCategoryTree(category));
        // Remove already assigned aspects from available ones
        availableAspects.removeAll(alreadyAssignedAspects);
        return new ResponseEntity<>(availableAspects, HttpStatus.OK);
    }

    public Set<Aspect> getAspectsOfCategoryTree(Category category) {
        // Create a list with all categories in hierarchy
        ArrayList<Category> categories = new ArrayList<>(categoryService.findAllCategoriesInHierarchy(category));
        // Create a set that stores the result aspects
        Set<Aspect> aspects = new HashSet<>();
        // Iterate through the categories and add all of their aspects to the result set
        for (Category c : categories) {
            aspects.addAll(aspectService.findByCategory(c));
        }
        return aspects;
    }

    private boolean aspectNameNotPresentInHierarchy(String aspectName, Category category) {
        Set<Aspect> aspectsOfHierarchy = getAspectsOfCategoryTree(category);
        return aspectsOfHierarchy.stream()
                .noneMatch(aspect -> aspect.getName().equalsIgnoreCase(aspectName));
    }

    @GetMapping("/{id}/category-aspects")
    public ResponseEntity<Set<Aspect>> getAspectsByCategory(@PathVariable("id") Long categoryId) {
        // Find the category by the provided ID
        Category category = categoryService.findById(categoryId);
        Set<Aspect> categoryAspects = getAspectsOfCategoryTree(category);
        return new ResponseEntity<>(categoryAspects, HttpStatus.OK);
    }
}
