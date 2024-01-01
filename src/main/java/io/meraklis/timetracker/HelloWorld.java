package io.meraklis.timetracker;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class HelloWorld {

    @Id
    private String id;
    private String something;
}
