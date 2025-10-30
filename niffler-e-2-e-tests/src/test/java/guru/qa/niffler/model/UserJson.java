package guru.qa.niffler.model;


import guru.qa.niffler.data.entity.userdata.UserEntity;

import java.util.UUID;

public record UserJson(
        UUID id,
        String username,
        CurrencyValues currencyValues,
        String fullname,
        String firstname,
        String surname,
        byte[] photo,
        byte[] photoSmall
) {
    public static UserJson fromEntity(UserEntity ue) {
        return new UserJson(
                ue.getId(),
                ue.getUsername(),
                ue.getCurrency(),
                ue.getFullname(),
                ue.getFirstname(),
                ue.getSurname(),
                ue.getPhoto(),
                ue.getPhotoSmall()
        );
    }
}

