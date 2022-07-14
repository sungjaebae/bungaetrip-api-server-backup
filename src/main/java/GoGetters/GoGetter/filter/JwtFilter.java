//package GoGetters.GoGetter.filter;
//
//import GoGetters.GoGetter.util.RequestUtil;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseAuthException;
//import com.google.firebase.auth.FirebaseToken;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.http.HttpStatus;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.NoSuchElementException;
//
//@Slf4j
//public class JwtFilter extends OncePerRequestFilter {
//
//    private UserDetailsService userDetailsService;
//    private FirebaseAuth firebaseAuth;
//
//    public JwtFilter(UserDetailsService userDetailsService, FirebaseAuth firebaseAuth) {
//        this.userDetailsService = userDetailsService;
//        this.firebaseAuth = firebaseAuth;
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//
//        // get the token from the request
//        FirebaseToken decodedToken;
//        String header = request.getHeader("Authorization");
//        if (header == null || !header.startsWith("Bearer ")) {
//            setUnauthorizedResponse(response, "INVALID_HEADER");
//            return;
//        }
//        String token = header.substring(7);
//        try{
//            System.out.println("start decodedToken");
//            decodedToken = firebaseAuth.verifyIdToken(token);
//            System.out.println(decodedToken);
//        } catch (FirebaseAuthException e) {
//            setUnauthorizedResponse(response, "INVALID_TOKEN");
//            return;
//        }
//        try{
//            UserDetails user = userDetailsService.loadUserByUsername(decodedToken.getUid());
//            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
//                    user, null, user.getAuthorities());
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//        } catch(NoSuchElementException e){
//            setUnauthorizedResponse(response, "USER_NOT_FOUND");
//            return;
//        }
//        filterChain.doFilter(request, response);
//
//
//    }
//
//
//    private void setUnauthorizedResponse(HttpServletResponse response, String code) throws IOException {
//        response.setStatus(HttpStatus.SC_UNAUTHORIZED);
//        response.setContentType("application/json");
//        response.getWriter().write("{\"code\":\""+code+"\"}");
//    }
//}