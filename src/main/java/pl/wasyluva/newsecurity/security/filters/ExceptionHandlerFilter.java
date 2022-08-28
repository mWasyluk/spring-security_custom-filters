package pl.wasyluva.newsecurity.security.filters;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import pl.wasyluva.newsecurity.security.exceptions.BlackListException;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ExceptionHandlerFilter implements Filter {
    /**
     * ExceptionHandlerFilter is the Filter implementation that calls the FilterChain to catch every potential Exception and overwrite the response content appropriately.
     * The filter is prepared to handle the BlackListException explicitly and every other Exception implementation universally.
     * */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // Check if another filter throws an Exception
        try {
            filterChain.doFilter(servletRequest, servletResponse);

        } catch (Exception e) {
            // Set default response variables
            HttpServletResponse response = (HttpServletResponse)servletResponse;
            response.setHeader("Content-Type", MediaType.TEXT_HTML_VALUE);
            response.setCharacterEncoding("UTF-8");

            // Check if the Exception is an instance of the BlackListException and overwrite the response content appropriately.
            if ( e instanceof BlackListException) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write("" +
                        "<body style=\"background-color:black; color: red; font-size: 1.5em; display: flex; align-items: center; text-align: center;\">\n" +
                        "    <div style=\"width: 100%;\">\n" +
                        "        <h1>Nie powinno cię tu być!</h1>\n" +
                        "        <p>Użytkownik został powiadomiony o próbie włamania na konto. Sprawa jest kierowana do płokułatułu.</p>\n" +
                        "    </div>\n" +
                        "</body>");
            }
            // Overwrite the response content universally regardless of the type of Exception
            else {
                response.setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());
                response.getWriter().write("" +
                        "<body style=\"background-color:black; color: red; font-size: 1.5em; display: flex; align-items: center; text-align: center;\">\n" +
                        "    <div style=\"width: 100%;\">\n" +
                        "        <h1>Upsss! Coś poszło nie tak.</h1>\n" +
                        "        <p>Odśwież stronę, aby spróbować ponownie.</p>\n" +
                        "    </div>\n" +
                        "</body>");
            }
        }
    }
}