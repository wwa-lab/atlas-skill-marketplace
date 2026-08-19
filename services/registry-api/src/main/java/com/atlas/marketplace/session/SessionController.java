package com.atlas.marketplace.session;

import java.security.Principal;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController @RequestMapping("/api/v1/session")
public class SessionController {
    @GetMapping SessionView current(Principal principal){return new SessionView(principal.getName(),"Atlas Local User",List.of("CONSUMER","OWNER","SME"),true);}
    record SessionView(String id,String displayName,List<String> roles,boolean localProfile){}
}
