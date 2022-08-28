package pl.wasyluva.newsecurity.security.filters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.wasyluva.newsecurity.security.exceptions.BlackListException;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
public class BlackListFilter extends OncePerRequestFilter {
    /**
     * BlackListFilter is a Filter implementation that verifies if the request IP is put on the User's blacklist.
     * The Filter throws a BlackListException if an authenticated User log into the App using prohibited IP address.
     * */

    // Simulation of the Database that stores the blacklist's IPs
    private final Map<String, Set<String>> blackListsDatabase = new HashMap<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Add a black list element for the main user
        blackListsDatabase.put("wasyl", Collections.singleton("192.168.0.24"));

        // Resolve a request principal
        String ip = request.getRemoteAddr();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication != null ? authentication.getPrincipal() : "preAuthenticated";
        String principalUsername = ( principal instanceof User) ? ((User)principal).getUsername() : principal.toString();

        // Check if the User's black list contains currently used IP address
        Set<String> usersBlackList = blackListsDatabase.get(principalUsername);
        if (usersBlackList != null && usersBlackList.contains(ip)){
            log.error("Alert bezpieczeństwa - próba logaowania do konta: " + principalUsername + ", z zabronionego IP: " + ip + ". ");
            throw new BlackListException();
        }

        // Call the next filter
        filterChain.doFilter(request, response);
    }
}
