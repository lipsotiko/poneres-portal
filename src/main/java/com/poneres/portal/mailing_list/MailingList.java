package com.poneres.portal.mailing_list;

import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class MailingList {
    public String emails;
    public List<MailingListJoinRequest> requests;

    public MailingList(List<MailingListJoinRequest> requests) {
        this.requests = requests;
        emails = requests.stream().map(MailingListJoinRequest::getEmail).collect(Collectors.joining(","));
    }
}
