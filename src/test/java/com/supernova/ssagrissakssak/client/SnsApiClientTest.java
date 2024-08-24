package com.supernova.ssagrissakssak.client;

import com.supernova.ssagrissakssak.core.enums.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SnsApiClientTest {

    private SnsApiClient snsApiClient;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        snsApiClient = new SnsApiClient(restTemplate);
    }

    @Test
    void callSnsLikeApi_Success() {
        when(restTemplate.postForEntity(anyString(), eq(null), eq(String.class))).thenReturn(null);

        SnsApiClient.SnsApiRequest request = new SnsApiClient.SnsApiRequest(ContentType.FACEBOOK, "test123");
        SnsApiClient.SnsApiResponse response = snsApiClient.callSnsLikeApi(request);

        assertTrue(response.isSuccess());
        verify(restTemplate, times(1)).postForEntity(anyString(), eq(null), eq(String.class));
    }
}