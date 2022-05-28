package com.coldrain.jwt.auth;

import com.coldrain.jwt.model.Role;
import com.coldrain.jwt.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class PrincipalDetails implements UserDetails {

    // 자신이 가지고 있는 User
    private User user;

    public PrincipalDetails(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override // 이게 정확히 뭔지 모르겠음 ( 부트 블로그 52강 8분 33초 )
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Role role = user.getRole();
        String authority = role.getAuthority();
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority); // ?
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(simpleGrantedAuthority);
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
