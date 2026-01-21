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
    public void test() throws InterruptedException {
        TargetResumeAnalysisRequest request = new TargetResumeAnalysisRequest();
        TargetResumeMetadata metadata = new TargetResumeMetadata();
        metadata.setJobDescription("Responsibilities:\n" +
                "- Design and develop backend services using Java and Spring Boot\n" +
                "- Build and maintain REST APIs consumed by web and mobile clients\n" +
                "- Deploy and monitor applications on AWS infrastructure\n" +
                "- Collaborate with product managers and frontend engineers\n" +
                "- Write unit and integration tests to ensure code quality\n" +
                "\n" +
                "Skills:\n" +
                "- Java\n" +
                "- Spring Boot\n" +
                "- AWS cloud services\n" +
                "- SQL\n" +
                "- Docker\n" +
                "- Git version control\n" +
                "\n" +
                "Required Qualifications:\n" +
                "- Strong experience with Java and object-oriented design\n" +
                "- Experience building RESTful APIs and backend services\n" +
                "- Solid understanding of SQL and relational databases\n" +
                "- Experience working in a production environment\n" +
                "\n" +
                "Preferred Qualifications:\n" +
                "- Experience with Python or other scripting languages\n" +
                "- Familiarity with Kubernetes\n" +
                "- Knowledge of CI/CD pipelines\n" +
                "- Experience mentoring junior engineers \n" +
                "\n" +
                "Other:\n" +
                "  We are a fast-growing technology company building scalable cloud-based platforms\n" +
                "  used by millions of users worldwide. Our engineering team values collaboration,\n" +
                "  clean architecture, and continuous improvement.");
        metadata.setResume("John Doe\n" +
                "  Senior Backend Software Engineer\n" +
                "\n" +
                "  Summary:\n" +
                "  Backend software engineer with 7+ years of experience designing and building\n" +
                "  scalable backend systems using Java. Strong background in RESTful API development,\n" +
                "  cloud infrastructure, and SQL databases. Experienced working in production\n" +
                "  environments and collaborating with cross-functional teams.\n" +
                "\n" +
                "  Skills:\n" +
                "  - Java\n" +
                "  - Spring Boot\n" +
                "  - RESTful API development\n" +
                "  - AWS cloud services\n" +
                "  - SQL and relational databases\n" +
                "  - Docker\n" +
                "  - Git\n" +
                "  - Unit and integration testing\n" +
                "\n" +
                "  Professional Experience:\n" +
                "  Senior Backend Software Engineer\n" +
                "  ABC Technology Solutions\n" +
                "  2019 - Present\n" +
                "\n" +
                "  - Designed and developed backend services using Java and Spring Boot\n" +
                "  - Built and maintained REST APIs for web and mobile applications\n" +
                "  - Deployed and supported applications running on AWS infrastructure\n" +
                "  - Worked extensively with SQL databases to design schemas and optimize queries\n" +
                "  - Collaborated with frontend engineers and product managers\n" +
                "  - Wrote unit and integration tests to ensure reliability and code quality\n" +
                "\n" +
                "  Backend Software Engineer\n" +
                "  XYZ Systems\n" +
                "  2016 - 2019\n" +
                "\n" +
                "  - Developed Java-based backend applications in a microservices architecture\n" +
                "  - Implemented RESTful APIs and integrated third-party services\n" +
                "  - Supported production deployments and resolved system issues\n" +
                "  - Used Docker for local development and application packaging\n" +
                "\n" +
                "  Projects:\n" +
                "  Cloud-Based Order Management System\n" +
                "  - Designed and implemented backend services using Java and Spring Boot\n" +
                "  - Exposed REST APIs consumed by multiple client applications\n" +
                "  - Deployed services to AWS using containerized Docker workloads\n" +
                "  - Integrated SQL database for transactional data storage\n" +
                "\n" +
                "  Education:\n" +
                "  Bachelor of Science in Computer Science\n" +
                "  University of Somewhere\n" +
                "\n" +
                "  Other:\n" +
                "  - Familiar with CI/CD concepts and automated build pipelines\n" +
                "  - Comfortable working in Agile development environments");
        request.setMetadata(metadata);
        TargetResumeAnalysisResponse analyse = targetResumeAnalysisController.analyse(request);
        assertEquals(88, analyse.getScore());
    }
}
