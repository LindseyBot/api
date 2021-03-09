package net.notfab.lindsey.api.advice.security;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServerAuthorizationFilter extends OncePerRequestFilter {

    private final SessionProvider provider;
    private final Pattern pattern = Pattern.compile("guilds/(\\d+)");

    public ServerAuthorizationFilter(SessionProvider provider) {
        this.provider = provider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        Matcher matcher = this.pattern.matcher(request.getRequestURI());
        if (!matcher.find()) {
            chain.doFilter(request, response);
            return;
        }
        long guild = Long.parseLong(matcher.group(1));
        if (!provider.hasAccess(guild)) {
            throw new BadCredentialsException("Not authorized");
        }
        chain.doFilter(request, response);
    }

}
