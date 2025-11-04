package guru.qa.niffler.data.entity.userdata;


import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.UserJson;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
public class UserEntity implements Serializable {
    private UUID id;
    private String username;
    private CurrencyValues currency;
    private String firstname;
    private String surname;
    private String fullname;
    private byte[] photo;
    private byte[] photoSmall;



    public static UserEntity fromJson(UserJson userJson){
        UserEntity ue = new UserEntity();
        ue.setId(userJson.id());
        ue.setUsername(userJson.username());
        ue.setCurrency(userJson.currencyValues());
        ue.setFirstname(userJson.firstname());
        ue.setSurname(userJson.surname());
        ue.setPhoto(userJson.photo());
        ue.setPhotoSmall(userJson.photoSmall());
        ue.setFullname(userJson.fullname());
        return ue;
    }
}