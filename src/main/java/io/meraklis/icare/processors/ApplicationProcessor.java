package io.meraklis.icare.processors;

import java.io.IOException;
import java.util.Map;

public interface ApplicationProcessor {
    byte[] process( Map<String, Object> metadata) throws IOException;
}
