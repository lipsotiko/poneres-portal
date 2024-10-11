package com.poneres.portal.signatures;

import com.poneres.portal.helpers.RestApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SignwellSignatureService implements SignatureService {

    @Autowired
    private RestApiService restApiService;


}
