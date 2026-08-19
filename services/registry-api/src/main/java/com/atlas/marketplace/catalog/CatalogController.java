package com.atlas.marketplace.catalog;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/v1/skills") @Validated
public class CatalogController {
    private final CatalogService catalog;
    CatalogController(CatalogService catalog){this.catalog=catalog;}
    @GetMapping CatalogService.SearchResponse search(@RequestParam(defaultValue="") @Size(max=200) String q,
        @RequestParam(defaultValue="") @Size(max=80) String category,@RequestParam(defaultValue="0") @Min(0) int page,
        @RequestParam(defaultValue="20") @Min(1) @Max(50) int size){return catalog.search(q,category,page,size);}
    @GetMapping("/{slug}") CatalogService.Detail detail(@PathVariable @Size(max=100) String slug){return catalog.detail(slug);}
}
