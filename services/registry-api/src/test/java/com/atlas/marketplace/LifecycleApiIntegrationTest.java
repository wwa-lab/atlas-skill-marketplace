package com.atlas.marketplace;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.junit.jupiter.api.Test; import org.springframework.beans.factory.annotation.Autowired; import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest; import org.springframework.http.MediaType; import org.springframework.test.context.ActiveProfiles; import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest @AutoConfigureMockMvc @ActiveProfiles("test")
class LifecycleApiIntegrationTest {
 @Autowired MockMvc mvc;
 private static final String BODY="{\"operationType\":\"INSTALL\",\"skillId\":\"74b1504c-7ec3-4ec0-a90a-2a8e9efdc224\",\"hostCode\":\"VSCODE_COPILOT_CHAT\",\"scope\":{\"type\":\"PERSONAL\"},\"targetVersion\":\"1.2.0\"}";
 @Test void replaysSameIdempotentIntent() throws Exception {for(int i=0;i<2;i++)mvc.perform(post("/api/v1/installations/operations").with(httpBasic("atlas-local","local-only"))
  .header("Idempotency-Key","install-test-001").contentType(MediaType.APPLICATION_JSON).content(BODY)).andExpect(status().isOk()).andExpect(jsonPath("$.state").value("SUCCEEDED"));}
}
