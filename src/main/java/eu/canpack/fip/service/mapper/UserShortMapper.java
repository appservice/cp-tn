package eu.canpack.fip.service.mapper;

import eu.canpack.fip.bo.client.ClientMapper;
import eu.canpack.fip.bo.estimation.dto.EstimationCreateMapper;
import eu.canpack.fip.domain.User;
import eu.canpack.fip.service.dto.UserShortDTO;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Service;

/**
 * CP S.A.
 * Created by lukasz.mochel on 02.12.2017.
 */
@Mapper(componentModel = "spring", uses = {})
public interface UserShortMapper {

    default UserShortDTO enityToDTO(User user) {
        if(user==null){
            return null;
        }
        UserShortDTO userShortDTO = new UserShortDTO();
        userShortDTO.setFirstName(user.getFirstName());
        userShortDTO.setLastName(user.getLastName());
        userShortDTO.setId(user.getId());
        return userShortDTO;
    }

    default public User userFromId(Long id) {
        if (id == null) {
            return null;
        }
        User user = new User();
        user.setId(id);
        return user;
    }

}
