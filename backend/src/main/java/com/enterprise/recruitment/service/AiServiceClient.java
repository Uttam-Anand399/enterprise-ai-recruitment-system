package com.enterprise.recruitment.service;

import com.enterprise.recruitment.dto.AiMatchResponse;
import com.enterprise.recruitment.dto.AiParseResumeResponse;
import java.io.IOException;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Component
public class AiServiceClient {

    private final RestClient restClient;

    public AiServiceClient(@Value("${services.ai.base-url}") String aiBaseUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(aiBaseUrl)
                .build();
    }

    public AiParseResumeResponse parseResume(byte[] fileBytes, String filename) {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new NamedByteArrayResource(fileBytes, filename));

        return restClient.post()
                .uri("/parse-resume")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(body)
                .retrieve()
                .body(AiParseResumeResponse.class);
    }

    public AiMatchResponse matchJob(String resumeText, String jobDescription) {
        return restClient.post()
                .uri("/match-job")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("resumeText", resumeText, "jobDescription", jobDescription))
                .retrieve()
                .body(AiMatchResponse.class);
    }

    private static final class NamedByteArrayResource extends ByteArrayResource {
        private final String filename;

        private NamedByteArrayResource(byte[] byteArray, String filename) {
            super(byteArray);
            this.filename = filename;
        }

        @Override
        public String getFilename() {
            return filename;
        }

        @Override
        public long contentLength() throws IOException {
            return getByteArray().length;
        }
    }
}
