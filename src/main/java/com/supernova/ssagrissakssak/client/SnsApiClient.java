package com.supernova.ssagrissakssak.client;

import com.supernova.ssagrissakssak.core.enums.ContentType;
import com.supernova.ssagrissakssak.core.exception.ErrorCode;
import com.supernova.ssagrissakssak.core.exception.ExternalApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class SnsApiClient {

    private final RestTemplate restTemplate;

    @Autowired
    public SnsApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * SNS API 요청에 필요한 정보를 담는 클래스입니다.
     */
    public static class SnsApiRequest {
        private final ContentType type;
        private final Long id;

        public SnsApiRequest(ContentType type, Long id) {
            this.type = type;
            this.id = id;
        }

        public ContentType getType() {
            return type;
        }

        public Long getId() {
            return id;
        }
    }

    /**
     * SNS API 응답 결과를 나타내는 클래스입니다.
     */
    public static class SnsApiResponse {
        private final boolean isSuccess;

        public SnsApiResponse(boolean isSuccess) {
            this.isSuccess = isSuccess;
        }

        public boolean isSuccess() {
            return isSuccess;
        }
    }

    /**
     * SNS의 좋아요 API를 호출합니다.
     *
     * @param request API 요청 정보
     * @return API 호출 결과
     * @throws ExternalApiException API 호출 중 오류가 발생했을 때 발생합니다.
     */
    public SnsApiResponse callSnsLikeApi(SnsApiRequest request) {
        String url = request.getType().getLikeUrl(request.getId());
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, null, String.class);
            return new SnsApiResponse(response.getStatusCode().is2xxSuccessful());
        } catch (RestClientException e) {
            throw new ExternalApiException(ErrorCode.EXTERNAL_API_ERROR, e);
        }
    }

}