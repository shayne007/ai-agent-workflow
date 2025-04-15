package aiagent.interfaces.rest;

import aiagent.application.dto.CreditQueryResponse;
import aiagent.application.service.CreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/credit")
public class CreditController {

    private final CreditService creditService;

    @Autowired
    public CreditController(CreditService creditService) {
        this.creditService = creditService;
    }

    @GetMapping("/query/{userId}")
    public ResponseEntity<CreditQueryResponse> queryCreditData(@PathVariable String userId) {
        CreditQueryResponse response = creditService.queryCreditData(userId);
        return ResponseEntity.ok(response);
    }
}