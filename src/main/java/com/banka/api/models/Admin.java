package com.banka.api.models;

import com.banka.api.enums.Role;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class Admin extends Usuario {
    @Override
    public void onCreate() {
        if (super.getRole() == null)
            super.setRole(Role.ROLE_ADMIN);

        super.onCreate();
    }
}
