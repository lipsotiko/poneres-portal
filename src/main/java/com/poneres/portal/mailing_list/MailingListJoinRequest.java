package com.poneres.portal.mailing_list;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MailingListJoinRequest {
    @Id
    private String id;
    private String organization;
    private String firstName;
    private String lastName;
    private String email;
}
