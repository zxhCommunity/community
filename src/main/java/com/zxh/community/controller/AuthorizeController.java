package com.zxh.community.controller;

import com.zxh.community.dto.AccessTokenDTO;
import com.zxh.community.dto.GithubUser;
import com.zxh.community.mapper.UserMapper;
import com.zxh.community.module.User;
import com.zxh.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @author zxh
 * @create 2021-12-08-20:25
 */
@Controller
public class AuthorizeController {
    @Autowired
    private GithubProvider githubProvider;
    @Value("4dbd132e46b0f154d00b")
    private String clientId;
    @Value("4dac88536b1c61432e015544df6e22ccb6c50eb8")
    private String clientSecret;
    @Value("${github.redirect.uri}")
    private String redirectUri;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           // HttpServletRequest request,
                           HttpServletResponse response
    ){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setState(state);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubUser = githubProvider.getUser(accessToken);
        System.out.println(githubUser.getName());
        if(githubUser!=null){
            User user = new User();
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
            response.addCookie(new Cookie("token",token));//登陆成功后把token写入Cookie
           // request.getSession().setAttribute("user",githubUser);
            return "redirect:/";
        }else{
            return "redirect:/";
        }
    }
}
