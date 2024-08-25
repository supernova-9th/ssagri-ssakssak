package com.supernova.ssagrissakssak.client;

import com.supernova.ssagrissakssak.core.enums.ContentType;
import com.supernova.ssagrissakssak.core.exception.SnsApiException;
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
        private final String contentId;

        public SnsApiRequest(ContentType type, String contentId) {
            this.type = type;
            this.contentId = contentId;
        }

        public ContentType getType() {
            return type;
        }

        public String getContentId() {
            return contentId;
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
     * @throws SnsApiException API 호출 중 오류가 발생했을 때 발생합니다.
     */
    public SnsApiResponse callSnsLikeApi(SnsApiRequest request) {
        String url = getSnsLikeUrl(request.type, request.contentId);
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, null, String.class);
            return new SnsApiResponse(response.getStatusCode().is2xxSuccessful());
        } catch (RestClientException e) {
            throw new SnsApiException("좋아요 API 호출 실패: " + e.getMessage(), e);
        }
    }

    private String getSnsLikeUrl(ContentType type, String contentId) {
        return switch (type) {
            case FACEBOOK -> "https://www.facebook.com/likes/" + contentId;
            case TWITTER -> "https://www.twitter.com/likes/" + contentId;
            case INSTAGRAM -> "https://www.instagram.com/likes/" + contentId;
            case THREADS -> "https://www.threads.net/likes/" + contentId;
            default -> throw new IllegalArgumentException("지원하지 않는 서비스 입니다.");
        };
    }
}