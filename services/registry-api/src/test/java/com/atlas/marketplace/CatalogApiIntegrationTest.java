package com.atlas.marketplace;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest @AutoConfigureMockMvc @ActiveProfiles("test")
class CatalogApiIntegrationTest {
    @Autowired MockMvc mvc;
    @Test void catalogRequiresAuthentication() throws Exception {mvc.perform(get("/api/v1/skills")).andExpect(status().isUnauthorized());}
    @Test void searchesSeededSkill() throws Exception {mvc.perform(get("/api/v1/skills").param("category","AS400").with(httpBasic("atlas-local","local-only")))
        .andExpect(status().isOk()).andExpect(jsonPath("$.items[0].slug").value("rpgle-program-analysis"))
        .andExpect(jsonPath("$.items[0].curatedPreview").isNotEmpty());}
}
