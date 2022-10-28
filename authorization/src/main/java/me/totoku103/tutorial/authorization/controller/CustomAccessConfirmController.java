package me.totoku103.tutorial.authorization.controller;

import lombok.RequiredArgsConstructor;
import me.totoku103.tutorial.authorization.service.CustomClientDetailService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@SessionAttributes("authorizationRequest")
@RequiredArgsConstructor
public class CustomAccessConfirmController {

    private final CustomClientDetailService customClientDetailService;

    @RequestMapping("/oauth/confirm_access")
    public ModelAndView getAccessConfirmation1(Map<String, Object> model,
                                               HttpServletRequest request,
                                               Principal principal) throws Exception {
        final AuthorizationRequest authorizationRequest = (AuthorizationRequest) model.get("authorizationRequest");
        final String userName = principal.getName();
        final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) principal;
        if(usernamePasswordAuthenticationToken.getPrincipal() instanceof User) {
            final User userInfo = (User) usernamePasswordAuthenticationToken.getPrincipal();
            model.put("grant", userInfo.getAuthorities().stream().map(d -> d.getAuthority()).collect(Collectors.joining(",")));
        }

        final String clientId = authorizationRequest.getClientId();
        final String requestPath = ServletUriComponentsBuilder.fromContextPath(request).build().getPath();
        final Map<String, String> scopes = (Map<String, String>) request.getAttribute("scopes");

        final HashMap<String, String> items = new HashMap<>();
        for (String key : scopes.keySet()) {
            items.put(key, scopes.get(key));
        }

        model.put("userName", userName);
        model.put("items", items);
        model.put("clientId", clientId);
        model.put("requestPath", requestPath);
        return new ModelAndView("confirmAccess", model);
    }

}
