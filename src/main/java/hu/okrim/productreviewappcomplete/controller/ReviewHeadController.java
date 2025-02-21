package hu.okrim.productreviewappcomplete.controller;

import hu.okrim.productreviewappcomplete.dto.AspectWithValueDTO;
import hu.okrim.productreviewappcomplete.dto.ReviewHeadDTO;
import hu.okrim.productreviewappcomplete.model.Product;
import hu.okrim.productreviewappcomplete.model.ReviewBody;
import hu.okrim.productreviewappcomplete.model.ReviewHead;
import hu.okrim.productreviewappcomplete.model.User;
import hu.okrim.productreviewappcomplete.model.compositeKey.ReviewBodyId;
import hu.okrim.productreviewappcomplete.model.compositeKey.ReviewHeadId;
import hu.okrim.productreviewappcomplete.service.ProductService;
import hu.okrim.productreviewappcomplete.service.ReviewBodyService;
import hu.okrim.productreviewappcomplete.service.ReviewHeadService;
import hu.okrim.productreviewappcomplete.service.UserService;
import hu.okrim.productreviewappcomplete.specification.ReviewHeadSpecificationBuilder;
import hu.okrim.productreviewappcomplete.util.AuthorizationUtil;
import hu.okrim.productreviewappcomplete.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/review-head")
public class ReviewHeadController {

    @Autowired
    private ReviewHeadService reviewHeadService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ReviewBodyService reviewBodyService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthorizationUtil authorizationUtil;

    @GetMapping("/{user}/{product}")
    public ResponseEntity<ReviewHead> findById(@PathVariable("user") Long userId, @PathVariable("product") Long productId) {
        ReviewHeadId id = new ReviewHeadId(userId, productId);
        ReviewHead reviewHead = reviewHeadService.findById(id);
        if (reviewHead != null) {
            return new ResponseEntity<>(reviewHead, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<ReviewHead>> getAllReviewHeads() {
        List<ReviewHead> reviewHeads = reviewHeadService.findAll();
        if (reviewHeads.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(reviewHeads, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<HttpStatus> createReviewHead(@RequestBody ReviewHeadDTO reviewHeadDTO) {
        User user = userService.findByUsername(reviewHeadDTO.getUsername());
        ReviewHeadId reviewHeadId = new ReviewHeadId(user.getId(), reviewHeadDTO.getProduct().getId());
        ReviewHead reviewHead = new ReviewHead(
                reviewHeadId,
                user,
                reviewHeadDTO.getProduct(),
                LocalDateTime.now(),
                reviewHeadDTO.getDescription(),
                reviewHeadDTO.getRecommended(),
                reviewHeadDTO.getPurchaseCountry(),
                reviewHeadDTO.getValueForPrice()
        );
        reviewHeadService.save(reviewHead);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteReviewHead(@RequestBody Map<String, String> request,
                                              HttpServletRequest httpRequest) {
        String username = request.get("username");
        Long productId = Long.parseLong(request.get("productId"));

        ResponseEntity<?> authorizationResponse = authorizationUtil.checkAuthorization(httpRequest, username);
        String userRole = jwtUtil.extractUserRoleFromToken(httpRequest);
        if (authorizationResponse != null && !userRole.equals("ADMIN")) {
            return authorizationResponse;
        }

        User user = userService.findByUsername(username);
        ReviewHeadId id = new ReviewHeadId(user.getId(), productId);

        try {
            reviewHeadService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/multi-delete")
    public ResponseEntity<?> deleteReviewHeads(@RequestBody List<String> keyPairs,
                                               HttpServletRequest httpRequest) {
        //Frontend is sending key pairs in "username-productId" format
        for (String keyPair : keyPairs) {
            String[] parts = keyPair.split("-");
            String username = parts[0];
            Long productId = Long.parseLong(parts[1]);

            //For each key pair (a review) user's authorization should be checked
            ResponseEntity<?> authorizationResponse = authorizationUtil.checkAuthorization(httpRequest, username);
            String userRole = jwtUtil.extractUserRoleFromToken(httpRequest);
            if (authorizationResponse != null && !userRole.equals("ADMIN")) {
                return authorizationResponse;
            }

            User user = userService.findByUsername(username);
            ReviewHeadId id = new ReviewHeadId(user.getId(), productId);

            try {
                reviewHeadService.deleteById(id);
            } catch (Exception ex) {
                return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
            }
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{username}/{productId}/modify")
    public ResponseEntity<?> modifyReviewHead(@PathVariable("username") String username,
                                              @PathVariable("productId") Long productId,
                                              @RequestBody ReviewHeadDTO reviewHeadDTO,
                                              HttpServletRequest httpRequest) {
        ResponseEntity<?> authorizationResponse = authorizationUtil.checkAuthorization(httpRequest, username);
        String userRole = jwtUtil.extractUserRoleFromToken(httpRequest);
        if (authorizationResponse != null && !userRole.equals("ADMIN")) {
            return authorizationResponse;
        }

        User user = userService.findByUsername(username);
        Product product = productService.findById(productId);
        ReviewHeadId id = new ReviewHeadId(user.getId(), product.getId());

        ReviewHead existingReviewHead = reviewHeadService.findById(id);
        if (existingReviewHead == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Update the existing reviewHead with the new data
        existingReviewHead.setDescription(reviewHeadDTO.getDescription());
        existingReviewHead.setDate(LocalDateTime.now());
        existingReviewHead.setRecommended(reviewHeadDTO.getRecommended());
        existingReviewHead.setPurchaseCountry(reviewHeadDTO.getPurchaseCountry());
        existingReviewHead.setValueForPrice(reviewHeadDTO.getValueForPrice());

        reviewHeadService.save(existingReviewHead);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{username}/{productId}/attach-review-body")
    public ResponseEntity<?> attachReviewBody(@PathVariable("username") String username,
                                              @PathVariable("productId") Long productId,
                                              @RequestBody List<AspectWithValueDTO> aspects,
                                              HttpServletRequest httpRequest) {
        ResponseEntity<?> authorizationResponse = authorizationUtil.checkAuthorization(httpRequest, username);
        if (authorizationResponse != null) {
            return authorizationResponse;
        }

        User user = userService.findByUsername(username);
        Product product = productService.findById(productId);
        ReviewHeadId headId = new ReviewHeadId(user.getId(), product.getId());
        ReviewHead reviewHead = reviewHeadService.findById(headId);
        if (reviewHead == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<ReviewBody> reviewBodies = new ArrayList<>();
        for (AspectWithValueDTO aspect : aspects) {
            ReviewBodyId bodyId = new ReviewBodyId(user.getId(), product.getId(), aspect.getId());
            //If aspect is sent with a null score it should be deleted from reviewBody
            if (aspect.getScore() == null) {
                reviewBodyService.deleteById(bodyId);
            } else {
                reviewBodies.add(new ReviewBody(bodyId, aspect.getScore(), reviewHead));
            }
        }

        try {
            reviewBodyService.saveAll(reviewBodies);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception ex) {
            String message = ex.getMessage();
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ReviewHead>> searchAspects(@RequestParam(value = "searchText", required = false) String searchText,
                                                          @RequestParam(value = "searchColumn", required = false) String searchColumn,
                                                          @RequestParam(value = "quickFilterValues", required = false) String quickFilterValues,
                                                          @RequestParam("pageSize") Integer pageSize,
                                                          @RequestParam("pageNumber") Integer pageNumber,
                                                          @RequestParam("orderByColumn") String orderByColumn,
                                                          @RequestParam("orderByDirection") String orderByDirection
    ) {
        ReviewHeadSpecificationBuilder<ReviewHead> reviewHeadSpecificationBuilder = new ReviewHeadSpecificationBuilder<>();
        if (searchColumn != null) {
            switch (searchColumn) {
                case "productName" -> reviewHeadSpecificationBuilder.withArticleName(searchText);
                case "user" -> reviewHeadSpecificationBuilder.withUsername(searchText);
                case "description" -> reviewHeadSpecificationBuilder.withDescription(searchText);
                case "date" -> reviewHeadSpecificationBuilder.withDate(searchText);
                case "recommended" -> reviewHeadSpecificationBuilder.withRecommended(searchText);
                case "valueForPrice" -> reviewHeadSpecificationBuilder.withValueForPrice(searchText);
                case "purchaseCountry" -> reviewHeadSpecificationBuilder.withPurchaseCountryName(searchText);
                default -> {

                }
            }
        } else {
            if (quickFilterValues != null && !quickFilterValues.isEmpty()) {
                // When searchColumn is not provided all fields are searched
                reviewHeadSpecificationBuilder.withQuickFilterValues(List.of(quickFilterValues.split(",")));
            }
        }
        Specification<ReviewHead> specification = reviewHeadSpecificationBuilder.build();
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by(orderByDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, orderByColumn));
        Page<ReviewHead> aspectsPage = reviewHeadService.findAllBySpecification(specification, pageable);
        return new ResponseEntity<>(aspectsPage, HttpStatus.OK);
    }

    @GetMapping("/{username}/{productId}/is-review-possible")
    public ResponseEntity<?> checkIfReviewExists(@PathVariable("username") String username,
                                                 @PathVariable("productId") Long productId) {
        User user = userService.findByUsername(username);
        Product product = productService.findById(productId);
        Optional<ReviewHead> reviewHead = reviewHeadService.findByUserAndProduct(user, product);
        if (reviewHead.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            String errorMessage = "You already have a review for this product.";
            return new ResponseEntity<>(errorMessage, HttpStatus.CONFLICT);
        }
    }
}
