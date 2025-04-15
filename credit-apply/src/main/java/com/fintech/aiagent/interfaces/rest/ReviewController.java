package com.fintech.aiagent.interfaces.rest;

import com.fintech.aiagent.application.dto.ReviewCaseDTO;
import com.fintech.aiagent.application.dto.ReviewSubmitRequest;
import com.fintech.aiagent.application.dto.ReviewSubmitResponse;
import com.fintech.aiagent.application.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/review")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/list")
//    @PreAuthorize("hasRole('REVIEWER')")
    public ResponseEntity<List<ReviewCaseDTO>> getReviewCases(
            @RequestParam(required = false) String riskLevel,
            @RequestParam(defaultValue = "10") int limit) {
        List<ReviewCaseDTO> cases = reviewService.getReviewCases(riskLevel, limit);
        return ResponseEntity.ok(cases);
    }

    @PostMapping("/submit")
//    @PreAuthorize("hasRole('REVIEWER')")
    public ResponseEntity<ReviewSubmitResponse> submitReview(@RequestBody ReviewSubmitRequest request) {
        ReviewSubmitResponse response = reviewService.submitReview(request);
        return ResponseEntity.ok(response);
    }
}