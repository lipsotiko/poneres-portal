package com.poneres.portal.target_resume;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/target-resume-analysis")
public class TargetResumeAnalysisController {

    @PostMapping
    public TargetResumeAnalysisResponse save(@RequestBody TargetResumeAnalysisRequest request) throws InterruptedException {
        System.out.println(request);
        Thread.sleep(3000);
        return TargetResumeAnalysisResponse.builder().score(75).build();
    }
}
