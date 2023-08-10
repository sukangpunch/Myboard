package com.springboot.board.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Builder
@ToString
@EqualsAndHashCode
@Table(name = "user_tb")
@NoArgsConstructor // 외부에서의 생성을 열어 둘 필요가 없을 때 / 보안적으로 권장된다
@AllArgsConstructor
//UserDetailsService를 통해 입력된 로그인 정보를 가지고 데이터베이스에서 사용자 정보를 가져오는 역할을 수행.
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true)
    private String uid;

    //JSON 직렬화시(User 객체를 JSON으로 변환시) password 필드는 무시됩니다.
    // 그러나 JSON 역직렬화시(JSON을 User 객체로 변환시)에는 password 필드가 사용되지 않으므로, 비밀번호가 역직렬화 과정에서 노출되는 것을 방지
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    // 엔티티 클래스 내에서 컬렉션 타입(예: List, Set, Map 등)의 필드를 매핑하기 위함
    // 컬렉션 타입의 필드를 엔티티가 아닌 별도의 테이블에 저장
    //해당 필드가 빌더 패턴을 사용하여 객체를 생성할 때 값이 지정되지 않았을 때에도 기본값을 가지게 된다.
    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    private List<Board> boardList = new ArrayList<>();

    //사용자의 권한 정보를 반환하는 역할
    //List<SimpleGrantedAuthority> 형태로 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public String getUsername() {
        return this.uid;
    }

    //계정의 상태정보는 나중에 다루자
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //계정의 상태정보는 나중에 다루자
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //계정의 상태정보는 나중에 다루자
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //계정의 상태정보는 나중에 다루자
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isEnabled() {
        return true;
    }
}
