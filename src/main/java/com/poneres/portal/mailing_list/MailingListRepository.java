package com.poneres.portal.mailing_list;

import com.poneres.portal.user.Organization;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MailingListRepository extends MongoRepository<MailingListJoinRequest, String> {
    List<MailingListJoinRequest> findByOrganization(Organization organization);

    Boolean existsByOrganizationAndEmail(Organization organization, String email);
}
