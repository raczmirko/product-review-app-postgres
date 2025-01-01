package hu.okrim.productreviewappcomplete.service;

import hu.okrim.productreviewappcomplete.model.ReviewBody;
import hu.okrim.productreviewappcomplete.model.compositeKey.ReviewBodyId;

import java.util.List;

public interface ReviewBodyService {
    void save (ReviewBody reviewBody);
    void saveAll (List<ReviewBody> reviewBodyList);
    void deleteById(ReviewBodyId reviewBodyId);
}
