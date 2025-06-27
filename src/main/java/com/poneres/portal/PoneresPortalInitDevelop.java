package com.poneres.portal;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile({"local", "local-no-auth"})
public class PoneresPortalInitDevelop extends AbstractPoneresPortalInit {

    @PostConstruct
    public void init() {
        super.init();
    }
}
