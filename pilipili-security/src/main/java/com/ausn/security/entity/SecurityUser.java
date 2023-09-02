package com.ausn.security.entity;

import com.ausn.entity.PUser;
import lombok.NoArgsConstructor;
import org.apache.ibatis.javassist.Loader;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @Author: 付显贵
 * @DateTime: 2023/9/2 19:14
 * @Description:
 */

public class SecurityUser implements UserDetails
{
    private final PUser pUser;

    private List<GrantedAuthority> authorities;

    public SecurityUser(PUser pUser)
    {
        this.pUser=pUser;
    }

    public PUser getPuser()
    {
        return pUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        GrantedAuthority authority = new GrantedAuthority()
        {
            @Override
            public String getAuthority()
            {
                return "user:common";
            }
        };
        authorities.add(authority);
        return authorities;
    }

    @Override
    public String getPassword()
    {
        String password=pUser.getPassword();
        pUser.setPassword(null); //erase the password
        return password;
    }

    @Override
    public String getUsername()
    {
        return pUser.getPhoneNumber();
    }

    @Override
    public boolean isAccountNonExpired()
    {
        return true;
    }

    @Override
    public boolean isAccountNonLocked()
    {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired()
    {
        return true;
    }

    @Override
    public boolean isEnabled()
    {
        return true;
    }
}
