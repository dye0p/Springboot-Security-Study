package com.springbootproject.springbootsecuritytest.config.oauth;

import com.springbootproject.springbootsecuritytest.config.auth.PrincipalDetails;
import com.springbootproject.springbootsecuritytest.model.User;
import com.springbootproject.springbootsecuritytest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    private final UserRepository userRepository;

    //구글로 부터 받은 userRequest 데이터에 대한 후처리되는 함수
    //함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("userRequest.getClientRegistration() = " + userRequest.getClientRegistration());
        System.out.println("userRequest.getAccessToken().getTokenValue() = " + userRequest.getAccessToken().getTokenValue());

        OAuth2User oauth2User = super.loadUser(userRequest);
        /*
         * 구글 로그인(버튼) 클릭-> 구글 로그인 창-> 로그인 완료-> code를 리턴-> (OAuth-Client 라이브러리)-> AccessToken 요청
         * userRequest 정보 -> loadUser함수 호출-> 구글로부터 회원 프로필 받아옴
         * */
        System.out.println("getAttributes" + oauth2User.getAttributes());

        //회원가입 강제 진행
        String provider = userRequest.getClientRegistration().getClientId(); //google
        String providerId = oauth2User.getAttribute("sub");
        String username = provider + "_" + providerId;
        String email = oauth2User.getAttribute("email");
        String role = "ROLE_USER";
        String password = bCryptPasswordEncoder.encode("겟인데어");

        //회원가입 중복체크
        User userEntity = userRepository.findByUsername(username);

        //존재하는 회원이 없다면
        if (userEntity == null) {
            //회원 생성 (빌터 패턴)
            userEntity = User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .provider(provider)
                    .providerId(providerId)
                    .role(role)
                    .build();

            //생성된 회원 저장
            userRepository.save(userEntity);
        }

        return new PrincipalDetails(userEntity, oauth2User.getAttributes());
    }
}

