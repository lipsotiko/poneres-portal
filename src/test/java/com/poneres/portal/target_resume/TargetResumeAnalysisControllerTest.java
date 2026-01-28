package com.poneres.portal.target_resume;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("local-no-auth")
class TargetResumeAnalysisControllerTest {

    @Autowired
    private TargetResumeAnalysisController targetResumeAnalysisController;

    @Test
    public void test95() throws InterruptedException {
        TargetResumeAnalysisRequest request = new TargetResumeAnalysisRequest();
        TargetResumeMetadata metadata = new TargetResumeMetadata();
        metadata.setJobDescription("""
                Responsibilities:
                - Design and develop backend services using Java and Spring Boot
                - Build and maintain REST APIs consumed by web and mobile clients
                - Deploy and monitor applications on AWS infrastructure
                - Collaborate with product managers and frontend engineers
                - Write unit and integration tests to ensure code quality

                Skills:
                - Java
                - Spring Boot
                - AWS cloud services
                - SQL
                - Docker
                - Git version control

                Required Qualifications:
                - Strong experience with Java and object-oriented design
                - Experience building RESTful APIs and backend services
                - Solid understanding of SQL and relational databases
                - Experience working in a production environment

                Preferred Qualifications:
                - Experience with Python or other scripting languages
                - Familiarity with Kubernetes
                - Knowledge of CI/CD pipelines
                - Experience mentoring junior engineers

                Other:
                  We are a fast-growing technology company building scalable cloud-based platforms
                  used by millions of users worldwide. Our engineering team values collaboration,
                  clean architecture, and continuous improvement.""");
        metadata.setResume("""
                John Doe
                  Senior Backend Software Engineer

                  Summary:
                  Backend software engineer with 7+ years of experience designing and building
                  scalable backend systems using Java. Strong background in RESTful API development,
                  cloud infrastructure, and SQL databases. Experienced working in production
                  environments and collaborating with cross-functional teams.

                  Skills:
                  - Java
                  - Spring Boot
                  - RESTful API development
                  - AWS cloud services
                  - SQL and relational databases
                  - Docker
                  - Git
                  - Unit and integration testing

                  Professional Experience:
                  Senior Backend Software Engineer
                  ABC Technology Solutions
                  2019 - Present

                  - Designed and developed backend services using Java and Spring Boot
                  - Built and maintained REST APIs for web and mobile applications
                  - Deployed and supported applications running on AWS infrastructure
                  - Worked extensively with SQL databases to design schemas and optimize queries
                  - Collaborated with frontend engineers and product managers
                  - Wrote unit and integration tests to ensure reliability and code quality

                  Backend Software Engineer
                  XYZ Systems
                  2016 - 2019

                  - Developed Java-based backend applications in a microservices architecture
                  - Implemented RESTful APIs and integrated third-party services
                  - Supported production deployments and resolved system issues
                  - Used Docker for local development and application packaging

                  Projects:
                  Cloud-Based Order Management System
                  - Designed and implemented backend services using Java and Spring Boot
                  - Exposed REST APIs consumed by multiple client applications
                  - Deployed services to AWS using containerized Docker workloads
                  - Integrated SQL database for transactional data storage

                  Education:
                  Bachelor of Science in Computer Science
                  University of Somewhere

                  Other:
                  - Familiar with CI/CD concepts and automated build pipelines
                  - Comfortable working in Agile development environments
                """);
        request.setMetadata(metadata);
        TargetResumeAnalysisResponse analyse = targetResumeAnalysisController.analyse(request);
        assertEquals(95, analyse.getScore());
    }
}
