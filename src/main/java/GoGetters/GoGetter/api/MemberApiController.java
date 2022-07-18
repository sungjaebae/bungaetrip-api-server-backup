package GoGetters.GoGetter.api;

import GoGetters.GoGetter.domain.Member;
import GoGetters.GoGetter.dto.RequestDto.CreateUserFirebaseRequest;
import GoGetters.GoGetter.dto.RequestDto.CreateUserRequest;
import GoGetters.GoGetter.dto.RequestDto.LoginRequest;
import GoGetters.GoGetter.dto.ResponseDto.UserResponse;
import GoGetters.GoGetter.dto.Result;
import GoGetters.GoGetter.service.MemberService;
import GoGetters.GoGetter.util.CookieUtil;
import GoGetters.GoGetter.util.JwtUtil;
import GoGetters.GoGetter.util.RedisUtil;
import GoGetters.GoGetter.util.RequestUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.core.Authentication;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class MemberApiController {
    final FirebaseAuth firebaseAuth;

    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;
    private final MemberService memberService;

    private final RedisUtil redisUtil;

    @PostMapping("")
    public Long register(
            @RequestHeader("Authorization") String authorization,
            @RequestBody CreateUserFirebaseRequest request
    ) {
        System.out.println("google login controller init : ");
        System.out.println(authorization);
        // TOKEN을 가져온다.
        FirebaseToken decodedToken;
        try {
            String token = RequestUtil.getAuthorizationToken(authorization);
//            String token="";
            System.out.println("controller token : " + token);

            decodedToken = firebaseAuth.verifyIdToken(token);
            System.out.println("controller decodedToken : " + decodedToken);
        } catch (IllegalArgumentException | FirebaseAuthException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "{\"code\":\"INVALID_TOKEN\", \"message\":\"" + e.getMessage() + "\"}");
        }
        System.out.println(decodedToken.getName());
        System.out.println(decodedToken.getEmail());
        System.out.println(decodedToken.getUid());
        System.out.println(decodedToken.getIssuer());
        System.out.println(decodedToken.getTenantId());
//        System.out.println(request.getGender());
//        System.out.println(request.getNickname());
//        System.out.println(request.getAge());

        JSONObject jsonObject = new JSONObject(decodedToken.getClaims());
        System.out.println(jsonObject);
        Member member = new Member(decodedToken.getEmail(), decodedToken.getUid());
        Long saved = null;
        try {
            saved = memberService.join(member);
        } catch (InterruptedException e) {
            //여기서 기존가입한 회원의 정보를 수정하면 됨.
            throw new RuntimeException("이미 회원이 존재합니다", e);
        }
        return saved;
    }

    @GetMapping("/me")
    public UserResponse getUserMe(Authentication authentication) {
        Member customMember = ((Member) authentication.getPrincipal());
        return new UserResponse(customMember);
    }

    @PostMapping("/signup")
    public Result signUpUser(@RequestBody CreateUserRequest request) {
        System.out.println("signup");
        Member member = new Member(request.getUsername(), request.getEmail(), request.getPassword(),
                request.getNickname(), request.getAge(), request.getGender());
        try {
            memberService.join(member);
            System.out.println("회원가입을 성공적으로 완료했습니다.");
        } catch (Exception e) {
            throw new IllegalStateException("회원가입을 하는 중 오류가 발생했습니다.", e);
        }

        return new Result(null);
    }

//    @PostMapping("/login")
//    public Result login(
//            @RequestHeader("Authorization") String authorization,
//            @RequestBody LoginRequest loginRequest,
//                        HttpServletRequest request,
//                        HttpServletResponse response) {
//        System.out.println("login request");
//        try {
//            final Member member = memberService.loginUser(loginRequest.getEmail(), loginRequest.getPassword());
//            final String token = jwtUtil.generateToken(member);
////            final String refreshJwt = jwtUtil.generateRefreshToken(member);
//            Cookie accessToken = cookieUtil.createCookie(JwtUtil.ACCESS_TOKEN_NAME, token);
////            Cookie refreshToken = cookieUtil.createCookie(JwtUtil.REFRESH_TOKEN_NAME, refreshJwt);
////            redisUtil.setDataExpire(refreshJwt, member.getUsername(), JwtUtil.REFRESH_TOKEN_VALIDATION_SECOND);
//            response.addCookie(accessToken);
////            response.addCookie(refreshToken);
//            System.out.println(token);
//
//            return new Result(new Response("success", "로그인에 성공했습니다", token));
//        } catch (Exception e) {
//            return new Result(new Response("error", "로그인에 실패했습니다.", e.getMessage()));
//        }
//    }

    @GetMapping("/redirect")
    public void redirect() {
        System.out.println("test gmail");
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class Response {
        private String code;
        private String message;
        private String token;


    }
}