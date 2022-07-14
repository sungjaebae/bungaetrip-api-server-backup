package GoGetters.GoGetter.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Member")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Member
//        implements UserDetails
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true)
    private String username;
    private String uid;
    private String email;

    private String nickName;
    private String password;

    private Integer age;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private UserRole role=UserRole.ROLE_NOT_PERMITTED;


    @CreationTimestamp
    private LocalDateTime createAt;

    @UpdateTimestamp
    private LocalDateTime updateAt;


    public Member(String email, String password, String nickName, Integer age, Gender gender, String uid) {

        this(email, uid);
        this.nickName = nickName;
        this.age = age;
        this.gender = gender;
        this.password = password;
    }
    public Member(String email, String password, String nickName, Integer age, Gender gender) {
        this.email=email;
        this.nickName = nickName;
        this.age = age;
        this.gender = gender;
        this.password = password;
    }
    public Member(String email, String nickName, Integer age, Gender gender, String uid) {
        this(email, uid);
        this.nickName=nickName;
        this.age=age;
        this.gender=gender;
    }

    public Member(String email, String uid) {
        this.email = email;
        this.uid = uid;
    }

    public Member(String username) {
        this.username=username;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", nickname='" + nickName + '\'' +
                ", role=" + role +
                ", createAt=" + createAt +
                ", updateAt=" + updateAt +
                '}';
    }

    public void encodePassword(String enCodedPassword) {
        this.password=enCodedPassword;
    }
//
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return null;
//    }
//
//    @Override
//    public String getUsername() {
//        return email;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return false;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return false;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return false;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return false;
//    }
}
