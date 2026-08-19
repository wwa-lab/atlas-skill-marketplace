package com.atlas.marketplace.lifecycle;

import jakarta.validation.Valid;
import java.security.Principal;
import java.util.UUID;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/v1")
public class LifecycleController {
    private final LifecycleService lifecycle; LifecycleController(LifecycleService lifecycle){this.lifecycle=lifecycle;}
    @PostMapping("/installations/operations") LifecycleService.OperationView create(@RequestHeader("Idempotency-Key") String key,@Valid @RequestBody LifecycleService.CreateOperation request,Principal principal){return lifecycle.create(key,request,principal);}
    @GetMapping("/operations/{id}") LifecycleService.OperationView get(@PathVariable UUID id){return lifecycle.get(id);}
}
