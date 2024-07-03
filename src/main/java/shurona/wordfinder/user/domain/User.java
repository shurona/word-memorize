package shurona.wordfinder.user.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
import shurona.wordfinder.common.DateInfoEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "_User")
//@SequenceGenerator(name = "user_id_seq", sequenceName = "USER_SEQ_ID", allocationSize = 1)
public class User extends DateInfoEntity {
    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "USER_ID")
    private Long id;

    private String nickname;

    private String loginId;

    private String password;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'NORMAL'")
    private UserRole role;

    public User() {
    }

    /**
     * 유저 생성
     */
    public User(String nickname, String loginId, String password) {
        this.nickname = nickname;
        this.loginId = loginId;

        this.password = password;

        // role 설정 => 멤버십 업그레이드일 때 변경을 한다.
        this.role = UserRole.NORMAL;

        // 날짜 저장
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getLoginId() {
        return loginId;
    }

    public String getPassword() {
        return password;
    }

    public UserRole getRole() {
        return role;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", nickname='" + nickname + '\'' +
                ", loginId='" + loginId + '\'' +
                '}';
    }
}
