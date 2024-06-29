package shurona.wordfinder.user.domain;

import jakarta.persistence.*;
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

    public User() {
    }

    /**
     * @param nickname
     * @param loginId
     * @param password
     */
    public User(String nickname, String loginId, String password) {
        this.nickname = nickname;
        this.loginId = loginId;

        // TODO: 해쉬로 저장해야 한다.
        this.password = password;

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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", nickname='" + nickname + '\'' +
                ", loginId='" + loginId + '\'' +
                '}';
    }
}
