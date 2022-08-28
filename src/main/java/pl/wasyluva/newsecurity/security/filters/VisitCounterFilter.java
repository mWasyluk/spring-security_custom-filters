package pl.wasyluva.newsecurity.security.filters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class VisitCounterFilter extends OncePerRequestFilter {
    /**
     * VisitCounterFilter is the Filter implementation that counts a visits number for an authenticated User and save the number in a Database.
     * */

    // Simulation of the Database that stores the visits number
    private final Map<String, Long> sampleDatabase = new HashMap<>();

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        // Retrieve the request parameters
        String ip = request.getRemoteAddr();
        String method = request.getMethod();
        String requestURL = String.valueOf(request.getRequestURL());
        log.info("Przyjęto żądanie typu: " + method + ", pod URL: " + requestURL + ", od IP: " + ip + ".");

        // Set the current visits number for the current IP
        long visitsNumber = 0;
        if ( sampleDatabase.get(ip) != null){
            visitsNumber = sampleDatabase.get(ip);
        }

        // Call the next filter
        filterChain.doFilter(request, response);

        // Save the number in the Database
        log.info("Łączna ilość wizyt z adresu IP: " + ip + " to: " + (++visitsNumber));
        sampleDatabase.put(ip, visitsNumber);

        // Check if the number was updated
        if (sampleDatabase.get(ip) != visitsNumber) {
            log.error("Coś poszło nie tak w trakcie zapisywania w bazie danych ilości wizyt z IP: " + ip + ".");
        }
    }
}
