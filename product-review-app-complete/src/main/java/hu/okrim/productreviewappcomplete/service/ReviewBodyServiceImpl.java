package hu.okrim.productreviewappcomplete.service;

import hu.okrim.productreviewappcomplete.model.ReviewBody;
import hu.okrim.productreviewappcomplete.model.compositeKey.ReviewBodyId;
import hu.okrim.productreviewappcomplete.repository.ReviewBodyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewBodyServiceImpl implements ReviewBodyService{
    @Autowired
    ReviewBodyRepository reviewBodyRepository;
    @Override
    public void save(ReviewBody reviewBody) {
        reviewBodyRepository.save(reviewBody);
    }

    @Override
    public void saveAll(List<ReviewBody> reviewBodyList) {
        reviewBodyRepository.saveAll(reviewBodyList);
    }

    @Override
    public void deleteById(ReviewBodyId reviewBodyId) {
        reviewBodyRepository.deleteById(reviewBodyId);
    }
}
