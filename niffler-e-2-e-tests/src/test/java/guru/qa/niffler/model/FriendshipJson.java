package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.data.entity.userdata.FriendshipEntity;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)

public record FriendshipJson(
        @JsonProperty("requester")
        UserEntity requester,
        @JsonProperty("addressee")
        UserEntity addressee,
        @JsonProperty("createdDate")
        Date createdDate,
        @JsonProperty("status")
        FriendshipStatus status) {

    public static FriendshipJson fromEntity(FriendshipEntity friendship) {
        return new FriendshipJson(
                friendship.getRequester(),
                friendship.getAddressee(),
                friendship.getCreatedDate(),
                FriendshipStatus.valueOf(friendship.getStatus().name())
        );
    }
}
