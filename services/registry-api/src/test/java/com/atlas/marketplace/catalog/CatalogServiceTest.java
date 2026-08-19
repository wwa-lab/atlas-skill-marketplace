package com.atlas.marketplace.catalog;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

class CatalogServiceTest {
    @Test void resolvesPilotAliases(){
        assertThat(CatalogService.canonicalCategory("AS400")).isEqualTo("IBM_ISERIES");
        assertThat(CatalogService.canonicalCategory("IBM i")).isEqualTo("IBM_ISERIES");
        assertThat(CatalogService.canonicalCategory("iSeries")).isEqualTo("IBM_ISERIES");
    }
}
